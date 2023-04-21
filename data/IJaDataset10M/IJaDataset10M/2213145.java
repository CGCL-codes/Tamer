package com.android.quicksearchbox;

import com.google.common.annotations.VisibleForTesting;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import java.util.ArrayList;

/**
 * A SuggestionCursor that is backed by a list of SuggestionData objects.
 */
public class DataSuggestionCursor extends AbstractSuggestionCursor {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    private final ArrayList<SuggestionData> mSuggestions;

    private int mPos;

    public DataSuggestionCursor(String userQuery) {
        super(userQuery);
        mSuggestions = new ArrayList<SuggestionData>();
        mPos = 0;
    }

    @VisibleForTesting
    public DataSuggestionCursor(String userQuery, SuggestionData... suggestions) {
        this(userQuery);
        for (SuggestionData suggestion : suggestions) {
            mSuggestions.add(suggestion);
        }
    }

    /**
     * Adds a suggestion.
     *
     * @param suggestion
     * @return {@code true}
     */
    public boolean add(SuggestionData suggestion) {
        mSuggestions.add(suggestion);
        notifyDataSetChanged();
        return true;
    }

    private SuggestionData current() {
        return mSuggestions.get(mPos);
    }

    public void close() {
        mSuggestions.clear();
    }

    public int getPosition() {
        return mPos;
    }

    public void moveTo(int pos) {
        mPos = pos;
    }

    public boolean moveToNext() {
        int size = mSuggestions.size();
        if (mPos >= size) {
            return false;
        }
        mPos++;
        return mPos < size;
    }

    public int getCount() {
        return mSuggestions.size();
    }

    /**
     * Register an observer that is called when changes happen to this data set.
     *
     * @param observer gets notified when the data set changes.
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    /**
     * Unregister an observer that has previously been registered with 
     * {@link #registerDataSetObserver(DataSetObserver)}
     *
     * @param observer the observer to unregister.
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    protected void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public String getShortcutId() {
        return current().getShortcutId();
    }

    public String getSuggestionFormat() {
        return current().getSuggestionFormat();
    }

    public String getSuggestionIcon1() {
        return current().getSuggestionIcon1();
    }

    public String getSuggestionIcon2() {
        return current().getSuggestionIcon2();
    }

    public String getSuggestionIntentAction() {
        return current().getSuggestionIntentAction();
    }

    public String getSuggestionIntentDataString() {
        return current().getSuggestionIntentDataString();
    }

    public String getSuggestionIntentExtraData() {
        return current().getSuggestionIntentExtraData();
    }

    public String getSuggestionKey() {
        return current().getSuggestionKey();
    }

    public String getSuggestionLogType() {
        return current().getSuggestionLogType();
    }

    public String getSuggestionQuery() {
        return current().getSuggestionQuery();
    }

    public Source getSuggestionSource() {
        return current().getSuggestionSource();
    }

    public String getSuggestionText1() {
        return current().getSuggestionText1();
    }

    public String getSuggestionText2() {
        return current().getSuggestionText2();
    }

    public String getSuggestionText2Url() {
        return current().getSuggestionText2Url();
    }

    public boolean isSpinnerWhileRefreshing() {
        return current().isSpinnerWhileRefreshing();
    }

    public boolean isSuggestionShortcut() {
        return false;
    }
}
