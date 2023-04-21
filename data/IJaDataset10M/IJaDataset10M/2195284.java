package com.android.globalsearch;

import static com.android.globalsearch.QueryMultiplexer.padQ;
import static com.android.globalsearch.QueryMultiplexer.ms;
import static com.android.globalsearch.QueryMultiplexer.pad;
import android.content.ComponentName;
import android.util.Log;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.CancellationException;
import java.util.ArrayList;

/**
 * Fires off tasks to validate shortcuts, and reports the results back to a
 * {@link SuggestionBacker}.  Also tells {@link com.android.globalsearch.ShortcutRepository} to
 * update the shortcut via {@link ShortcutRepository#refreshShortcut}.
 */
public class ShortcutRefresher {

    private static final String TAG = "GlobalSearch";

    private static final boolean DBG_LTNCY = false;

    private final Executor mExecutor;

    private final SourceLookup mSourceLookup;

    private final ArrayList<SuggestionData> mShortcuts;

    private final int mMaxToRefresh;

    private final SuggestionBacker mReceiver;

    private final ArrayList<ShortcutRefreshTask> mSent;

    private final ShortcutRepository mRepo;

    /**
     * @param executor Used to execute the tasks.
     * @param sourceLookup Used to lookup suggestion sources by component name.
     * @param shortcuts The shortcuts to refresh.
     * @param maxToRefresh The maximum number of shortcuts to refresh.
     * @param receiver Who to report back to.
     * @param shortcutRepository The repo is also told about shortcut refreshes.
     */
    public ShortcutRefresher(Executor executor, SourceLookup sourceLookup, ArrayList<SuggestionData> shortcuts, int maxToRefresh, SuggestionBacker receiver, ShortcutRepository shortcutRepository) {
        mExecutor = executor;
        mSourceLookup = sourceLookup;
        mShortcuts = shortcuts;
        mMaxToRefresh = maxToRefresh;
        mReceiver = receiver;
        mRepo = shortcutRepository;
        mSent = new ArrayList<ShortcutRefreshTask>(mMaxToRefresh);
    }

    /**
     * Sends off the refresher tasks.
     */
    public void refresh() {
        final int size = Math.min(mMaxToRefresh, mShortcuts.size());
        for (int i = 0; i < size; i++) {
            final SuggestionData shortcut = mShortcuts.get(i);
            final ComponentName componentName = shortcut.getSource();
            SuggestionSource source = mSourceLookup.getSourceByComponentName(componentName);
            if (source == null) {
                mExecutor.execute(new Runnable() {

                    public void run() {
                        if (mRepo != null) {
                            mRepo.refreshShortcut(componentName, shortcut.getShortcutId(), null);
                        }
                        mReceiver.onRefreshShortcut(componentName, shortcut.getShortcutId(), null);
                    }
                });
            } else {
                final ShortcutRefreshTask refreshTask = new ShortcutRefreshTask(source, shortcut, mReceiver, mRepo);
                refreshTask.setScheduledTime(System.nanoTime());
                mSent.add(refreshTask);
                mExecutor.execute(refreshTask);
            }
        }
    }

    /**
     * Cancels the tasks.
     */
    public void cancel() {
        for (ShortcutRefreshTask shortcutRefreshTask : mSent) {
            shortcutRefreshTask.cancel(true);
        }
    }

    /**
     * Validates a shortcut with a source and reports the result to a {@link SuggestionBacker}.
     */
    private static class ShortcutRefreshTask extends FutureTask<SuggestionData> {

        private final SuggestionSource mSource;

        private final String mShortcutId;

        private final SuggestionBacker mReceiver;

        private final ShortcutRepository mRepo;

        private long mScheduledTime = -1;

        private long mStartTime = -1;

        public void setScheduledTime(long scheduledTime) {
            mScheduledTime = scheduledTime;
        }

        @Override
        public void run() {
            mStartTime = System.nanoTime();
            super.run();
        }

        /**
         * @param source The source that should validate the shortcut.
         * @param shortcut The shortcut.
         * @param receiver Who to report back to when the result is in.
         * @param repo The repository to report the updated shortcut to.
         */
        ShortcutRefreshTask(SuggestionSource source, SuggestionData shortcut, SuggestionBacker receiver, ShortcutRepository repo) {
            super(source.getShortcutValidationTask(shortcut));
            mSource = source;
            mShortcutId = shortcut.getShortcutId();
            mReceiver = receiver;
            mRepo = repo;
        }

        @Override
        protected void done() {
            final boolean cancelled = isCancelled();
            if (DBG_LTNCY) logLatency(cancelled);
            if (cancelled) return;
            try {
                final SuggestionData refreshed = get();
                if (mRepo != null) {
                    mRepo.refreshShortcut(mSource.getComponentName(), mShortcutId, refreshed);
                }
                mReceiver.onRefreshShortcut(mSource.getComponentName(), mShortcutId, refreshed);
            } catch (CancellationException e) {
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
                Log.e(TAG, "failed to refresh shortcut from " + mSource.getComponentName().flattenToString() + " for shorcut id " + mShortcutId, e);
            } catch (RuntimeException ex) {
                Log.e(TAG, "Shortcut refresh error", ex);
            }
        }

        private void logLatency(boolean cancelled) {
            final boolean everStarted = mStartTime != -1;
            final long now = System.nanoTime();
            final String rawtname = Thread.currentThread().getName();
            final String tname = rawtname.substring(0, 3) + rawtname.substring(rawtname.length() - 3);
            long threadwait = ms(mStartTime - mScheduledTime);
            long durationMillis = ms(now - mStartTime);
            long total = ms(now - mScheduledTime);
            final StringBuilder sb = new StringBuilder(300);
            padQ(sb, "shortcut " + mShortcutId, 20);
            sb.append(mSource.getLabel().substring(0, 4)).append("  ");
            sb.append(tname).append("  ");
            sb.append("total=").append(total).append("\t");
            if (everStarted) {
                sb.append("twait=");
                pad(sb, Long.toString(threadwait), 8);
            }
            if (!cancelled) {
                sb.append("duration=");
                pad(sb, Long.toString(durationMillis), 8);
            } else {
                if (!everStarted) {
                    sb.append("twait=");
                    pad(sb, Long.toString(total), 8);
                    sb.append("(cancelled before running)");
                } else {
                    sb.append("duration=");
                    pad(sb, Long.toString(durationMillis), 8);
                    sb.append("(cancelled)");
                }
            }
            Log.d(TAG, sb.toString());
        }
    }
}
