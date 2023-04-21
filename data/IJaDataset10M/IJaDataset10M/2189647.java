package com.android.quicksearchbox.ui;

import com.android.quicksearchbox.SuggestionCursor;
import com.android.quicksearchbox.Suggestions;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.ViewGroup;

/**
 * View shown at the bottom of the suggestions list.
 */
public abstract class SuggestionsFooter {

    private final DataSetObserver mDataSetObserver = new AdapterObserver();

    private final Context mContext;

    private SuggestionsAdapter mAdapter;

    public SuggestionsFooter(Context context) {
        mContext = context;
    }

    public abstract void addToContainer(ViewGroup parent);

    protected abstract void onSuggestionsChanged();

    protected Context getContext() {
        return mContext;
    }

    public void setAdapter(SuggestionsAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        onSuggestionsChanged();
    }

    protected Suggestions getSuggestions() {
        return mAdapter == null ? null : mAdapter.getSuggestions();
    }

    protected SuggestionCursor getCurrentSuggestions() {
        return mAdapter == null ? null : mAdapter.getCurrentSuggestions();
    }

    private class AdapterObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            onSuggestionsChanged();
        }

        @Override
        public void onInvalidated() {
            onSuggestionsChanged();
        }
    }
}
