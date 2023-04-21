package com.mnm.asynctaskmanager.core;

import android.content.res.Resources;
import android.os.AsyncTask;

public final class Task extends AsyncTask<Void, String, Boolean> {

    protected final Resources mResources;

    private Boolean mResult;

    private String mProgressMessage;

    private IProgressTracker mProgressTracker;

    public Task(Resources resources) {
        mResources = resources;
        mProgressMessage = resources.getString(com.mnm.asynctaskmanager.R.string.task_starting);
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
            if (mResult != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    protected void onCancelled() {
        mProgressTracker = null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgressMessage = values[0];
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResult = result;
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        mProgressTracker = null;
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        for (int i = 10; i > 0; --i) {
            if (isCancelled()) {
                return false;
            }
            try {
                publishProgress(mResources.getString(com.mnm.asynctaskmanager.R.string.task_working, i));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
