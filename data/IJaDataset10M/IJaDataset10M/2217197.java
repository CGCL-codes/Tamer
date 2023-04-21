package com.google.devtools.depan.util;

/**
 * A {@link ProgressListener} that slows down a too fast
 * {@link ProgressListener}. Take a progressListener as a constructor, and
 * instead of sending each progress information, send one over x (x = slowDown).
 * 
 * @author ycoppel@google.com (Yohann Coppel)
 * 
 */
public class QuickProgressListener implements ProgressListener {

    /**
   * The progressListener to slowDown
   */
    private ProgressListener listener;

    /**
   * slow down rate 
   */
    private int slowDown;

    /**
   * number of progress updates eaten without sending a real notification.
   */
    private int position = 0;

    /**
   * Create a {@link QuickProgressListener} monitoring and filtering the given
   * {@link ProgressListener}, slowing it down at the given rate.
   * 
   * @param listener progress listener to slowdown
   * @param slowDown slow down factor. n = send 1 notification over n.
   */
    public QuickProgressListener(ProgressListener listener, int slowDown) {
        this.listener = listener;
        this.slowDown = slowDown;
    }

    public void progress(String curentJob, int n, int total) {
        position++;
        if (position >= slowDown || n == total) {
            position = 0;
            listener.progress(curentJob, n, total);
        }
    }
}
