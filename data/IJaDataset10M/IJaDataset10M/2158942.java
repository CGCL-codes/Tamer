package com.android.mms.util;

import com.google.android.mms.util.SqliteWrapper;
import com.android.mms.LogTag;
import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;

/**
 * Cache for information about draft messages on conversations.
 */
public class DraftCache {

    private static final String TAG = "Mms/draft";

    private static DraftCache sInstance;

    private final Context mContext;

    private HashSet<Long> mDraftSet = new HashSet<Long>(4);

    private final HashSet<OnDraftChangedListener> mChangeListeners = new HashSet<OnDraftChangedListener>(1);

    public interface OnDraftChangedListener {

        void onDraftChanged(long threadId, boolean hasDraft);
    }

    private DraftCache(Context context) {
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("DraftCache.constructor");
        }
        mContext = context;
        refresh();
    }

    static final String[] DRAFT_PROJECTION = new String[] { Conversations.THREAD_ID };

    static final int COLUMN_DRAFT_THREAD_ID = 0;

    /** To be called whenever the draft state might have changed.
     *  Dispatches work to a thread and returns immediately.
     */
    public void refresh() {
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("refresh");
        }
        new Thread(new Runnable() {

            public void run() {
                rebuildCache();
            }
        }).start();
    }

    /** Does the actual work of rebuilding the draft cache.
     */
    private synchronized void rebuildCache() {
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("rebuildCache");
        }
        HashSet<Long> oldDraftSet = mDraftSet;
        HashSet<Long> newDraftSet = new HashSet<Long>(oldDraftSet.size());
        Cursor cursor = SqliteWrapper.query(mContext, mContext.getContentResolver(), MmsSms.CONTENT_DRAFT_URI, DRAFT_PROJECTION, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    long threadId = cursor.getLong(COLUMN_DRAFT_THREAD_ID);
                    newDraftSet.add(threadId);
                    if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
                        log("rebuildCache: add tid=" + threadId);
                    }
                }
            }
        } finally {
            cursor.close();
        }
        mDraftSet = newDraftSet;
        if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
            dump();
        }
        if (mChangeListeners.size() < 1) {
            return;
        }
        Set<Long> added = new HashSet<Long>(newDraftSet);
        added.removeAll(oldDraftSet);
        Set<Long> removed = new HashSet<Long>(oldDraftSet);
        removed.removeAll(newDraftSet);
        for (OnDraftChangedListener l : mChangeListeners) {
            for (long threadId : added) {
                l.onDraftChanged(threadId, true);
            }
            for (long threadId : removed) {
                l.onDraftChanged(threadId, false);
            }
        }
    }

    /** Updates the has-draft status of a particular thread on
     *  a piecemeal basis, to be called when a draft has appeared
     *  or disappeared.
     */
    public synchronized void setDraftState(long threadId, boolean hasDraft) {
        if (threadId <= 0) {
            return;
        }
        boolean changed;
        if (hasDraft) {
            changed = mDraftSet.add(threadId);
        } else {
            changed = mDraftSet.remove(threadId);
        }
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("setDraftState: tid=" + threadId + ", value=" + hasDraft + ", changed=" + changed);
        }
        if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
            dump();
        }
        if (changed) {
            for (OnDraftChangedListener l : mChangeListeners) {
                l.onDraftChanged(threadId, hasDraft);
            }
        }
    }

    /** Returns true if the given thread ID has a draft associated
     *  with it, false if not.
     */
    public synchronized boolean hasDraft(long threadId) {
        return mDraftSet.contains(threadId);
    }

    public synchronized void addOnDraftChangedListener(OnDraftChangedListener l) {
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("addOnDraftChangedListener " + l);
        }
        mChangeListeners.add(l);
    }

    public synchronized void removeOnDraftChangedListener(OnDraftChangedListener l) {
        if (Log.isLoggable(LogTag.APP, Log.DEBUG)) {
            log("removeOnDraftChangedListener " + l);
        }
        mChangeListeners.remove(l);
    }

    /**
     * Initialize the global instance. Should call only once.
     */
    public static void init(Context context) {
        sInstance = new DraftCache(context);
    }

    /**
     * Get the global instance.
     */
    public static DraftCache getInstance() {
        return sInstance;
    }

    public void dump() {
        Log.i(TAG, "dump:");
        for (Long threadId : mDraftSet) {
            Log.i(TAG, "  tid: " + threadId);
        }
    }

    private void log(String format, Object... args) {
        String s = String.format(format, args);
        Log.d(TAG, "[DraftCache/" + Thread.currentThread().getId() + "] " + s);
    }
}
