package com.markpiper.tvtray.gui;

import com.markpiper.tvtray.EventListener;
import com.markpiper.tvtray.Channel;

public interface ProgressListener extends EventListener {

    /**
     * Callback method during fetching to indicate progress
     * 
     * @param progress - percentage of fetching completed
     */
    public void fetchingProgress(int progress);

    /**
     * Callback method when error occurs during fetches
     * 
     * @param e
     */
    public void errorOccurred(Channel c, Exception e);
}
