package com.android.providers.downloads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Downloads;

/**
 * Stores information about an individual download.
 */
public class DownloadInfo {

    public int mId;

    public String mUri;

    public boolean mNoIntegrity;

    public String mHint;

    public String mFileName;

    public String mMimeType;

    public int mDestination;

    public int mVisibility;

    public int mControl;

    public int mStatus;

    public int mNumFailed;

    public int mRetryAfter;

    public int mRedirectCount;

    public long mLastMod;

    public String mPackage;

    public String mClass;

    public String mExtras;

    public String mCookies;

    public String mUserAgent;

    public String mReferer;

    public int mTotalBytes;

    public int mCurrentBytes;

    public String mETag;

    public boolean mMediaScanned;

    public int mFuzz;

    public volatile boolean mHasActiveThread;

    public DownloadInfo(int id, String uri, boolean noIntegrity, String hint, String fileName, String mimeType, int destination, int visibility, int control, int status, int numFailed, int retryAfter, int redirectCount, long lastMod, String pckg, String clazz, String extras, String cookies, String userAgent, String referer, int totalBytes, int currentBytes, String eTag, boolean mediaScanned) {
        mId = id;
        mUri = uri;
        mNoIntegrity = noIntegrity;
        mHint = hint;
        mFileName = fileName;
        mMimeType = mimeType;
        mDestination = destination;
        mVisibility = visibility;
        mControl = control;
        mStatus = status;
        mNumFailed = numFailed;
        mRetryAfter = retryAfter;
        mRedirectCount = redirectCount;
        mLastMod = lastMod;
        mPackage = pckg;
        mClass = clazz;
        mExtras = extras;
        mCookies = cookies;
        mUserAgent = userAgent;
        mReferer = referer;
        mTotalBytes = totalBytes;
        mCurrentBytes = currentBytes;
        mETag = eTag;
        mMediaScanned = mediaScanned;
        mFuzz = Helpers.sRandom.nextInt(1001);
    }

    public void sendIntentIfRequested(Uri contentUri, Context context) {
        if (mPackage != null && mClass != null) {
            Intent intent = new Intent(Downloads.ACTION_DOWNLOAD_COMPLETED);
            intent.setClassName(mPackage, mClass);
            if (mExtras != null) {
                intent.putExtra(Downloads.COLUMN_NOTIFICATION_EXTRAS, mExtras);
            }
            intent.setData(contentUri);
            context.sendBroadcast(intent);
        }
    }

    /**
     * Returns the time when a download should be restarted. Must only
     * be called when numFailed > 0.
     */
    public long restartTime() {
        if (mRetryAfter > 0) {
            return mLastMod + mRetryAfter;
        }
        return mLastMod + Constants.RETRY_FIRST_DELAY * (1000 + mFuzz) * (1 << (mNumFailed - 1));
    }

    /**
     * Returns whether this download (which the download manager hasn't seen yet)
     * should be started.
     */
    public boolean isReadyToStart(long now) {
        if (mControl == Downloads.CONTROL_PAUSED) {
            return false;
        }
        if (mStatus == 0) {
            return true;
        }
        if (mStatus == Downloads.STATUS_PENDING) {
            return true;
        }
        if (mStatus == Downloads.STATUS_RUNNING) {
            return true;
        }
        if (mStatus == Downloads.STATUS_RUNNING_PAUSED) {
            if (mNumFailed == 0) {
                return true;
            }
            if (restartTime() < now) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether this download (which the download manager has already seen
     * and therefore potentially started) should be restarted.
     *
     * In a nutshell, this returns true if the download isn't already running
     * but should be, and it can know whether the download is already running
     * by checking the status.
     */
    public boolean isReadyToRestart(long now) {
        if (mControl == Downloads.CONTROL_PAUSED) {
            return false;
        }
        if (mStatus == 0) {
            return true;
        }
        if (mStatus == Downloads.STATUS_PENDING) {
            return true;
        }
        if (mStatus == Downloads.STATUS_RUNNING_PAUSED) {
            if (mNumFailed == 0) {
                return true;
            }
            if (restartTime() < now) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether this download has a visible notification after
     * completion.
     */
    public boolean hasCompletionNotification() {
        if (!Downloads.isStatusCompleted(mStatus)) {
            return false;
        }
        if (mVisibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether this download is allowed to use the network.
     */
    public boolean canUseNetwork(boolean available, boolean roaming) {
        if (!available) {
            return false;
        }
        if (mDestination == Downloads.DESTINATION_CACHE_PARTITION_NOROAMING) {
            return !roaming;
        } else {
            return true;
        }
    }
}
