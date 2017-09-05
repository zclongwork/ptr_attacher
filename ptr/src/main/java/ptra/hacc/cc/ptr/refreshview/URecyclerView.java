package ptra.hacc.cc.ptr.refreshview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ptra.hacc.cc.ptr.IExtraRefresh;

/**
 * Created by Hale Yang on 2017/9/4.
 * the recyclerView with  empty style
 */

public class URecyclerView extends RecyclerView implements IExtraRefresh{

    /*
    emptyView
     */
    private View mEmtpyView;
    private WrapAdapter mWrapAdapter;

    public URecyclerView(Context context) {
        super(context);
    }

    public URecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public URecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable TypedArray a){
    }


    @Override
    public void setAdapter(Adapter adapter) {
        adapter.registerAdapterDataObserver(mNativeDataObserver);
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
    }

    @Override
    public Adapter getAdapter() {
        return mWrapAdapter.mNativeAdapter;
    }

    @Override
    protected void onDetachedFromWindow() {
//        mWrapAdapter.mNativeAdapter.unregisterAdapterDataObserver(mNativeDataObserver);
        super.onDetachedFromWindow();
    }

    private AdapterDataObserver mNativeDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    @Override
    public void setEmptyView(@Nullable  View v) {
        this.mEmtpyView = v;
    }


    /**
     * Use the Adapter wrap the real adapter, then we can use
     * this adapter to bind empty view or other view
     */
    private class WrapAdapter extends Adapter<ViewHolder>{

        private Adapter mNativeAdapter;

        private boolean mNativeItemEmpty;

        /*
        the item count of wrap adapter item, now we have an empty view, so count equal 1
         */
        private static final int WRAP_ITEM_COUNT = 1;

        private static final int EMPTY_TYPE = 0x1;

        private WrapAdapter(@NonNull Adapter nativeAdapter) {
            super();
            this.mNativeAdapter = nativeAdapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(mNativeItemEmpty){
                return new WrapViewHolder(mEmtpyView);
            }else{
                return mNativeAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if(mNativeItemEmpty) {
                super.onBindViewHolder(holder, position, payloads);
            }else{
                mNativeAdapter.onBindViewHolder(holder, position, payloads);
            }
        }



        @Override
        public long getItemId(int position) {
            if(mNativeItemEmpty){
                return super.getItemId(position);
            }
            return super.getItemId(position);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onViewRecycled(ViewHolder holder) {
            if(mNativeItemEmpty){
                super.onViewRecycled(holder);
            }else{
                mNativeAdapter.onViewRecycled(holder);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            if(mNativeItemEmpty){
                return super.onFailedToRecycleView(holder);
            }else {
                return mNativeAdapter.onFailedToRecycleView(holder);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            if(mNativeItemEmpty) {
                super.onViewAttachedToWindow(holder);
            }else{
                mNativeAdapter.onViewAttachedToWindow(holder);
            }
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            int nativeCount = mNativeAdapter.getItemCount();
            mNativeItemEmpty = nativeCount == 0;
            int wrapCount = mEmtpyView != null ? WRAP_ITEM_COUNT : 0;
            return mNativeItemEmpty ? wrapCount : nativeCount;
        }

        @Override
        public int getItemViewType(int position) {
            int type;
            if(mNativeItemEmpty){
                type = EMPTY_TYPE;
            }else{
                type = mNativeAdapter.getItemViewType(position);
            }
            return type;
        }

    }

    private class WrapViewHolder extends  ViewHolder{

        private WrapViewHolder(View itemView) {
            super(itemView);
        }
    }


}
