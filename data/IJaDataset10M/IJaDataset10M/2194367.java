package com.android.layoutlib.bridge;

import com.android.layoutlib.api.ILayoutResult;
import java.awt.image.BufferedImage;

/**
 * Implementation of {@link ILayoutResult}
 */
public final class LayoutResult implements ILayoutResult {

    private final ILayoutViewInfo mRootView;

    private final BufferedImage mImage;

    private final int mSuccess;

    private final String mErrorMessage;

    /**
     * Creates a {@link #SUCCESS} {@link ILayoutResult} with the specified params
     * @param rootView
     * @param image
     */
    public LayoutResult(ILayoutViewInfo rootView, BufferedImage image) {
        mSuccess = SUCCESS;
        mErrorMessage = null;
        mRootView = rootView;
        mImage = image;
    }

    /**
     * Creates a LayoutResult with a specific success code and associated message
     * @param code
     * @param message
     */
    public LayoutResult(int code, String message) {
        mSuccess = code;
        mErrorMessage = message;
        mRootView = null;
        mImage = null;
    }

    public int getSuccess() {
        return mSuccess;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public BufferedImage getImage() {
        return mImage;
    }

    public ILayoutViewInfo getRootView() {
        return mRootView;
    }

    /**
     * Implementation of {@link ILayoutResult.ILayoutViewInfo}
     */
    public static final class LayoutViewInfo implements ILayoutViewInfo {

        private final Object mKey;

        private final String mName;

        private final int mLeft;

        private final int mRight;

        private final int mTop;

        private final int mBottom;

        private ILayoutViewInfo[] mChildren;

        public LayoutViewInfo(String name, Object key, int left, int top, int right, int bottom) {
            mName = name;
            mKey = key;
            mLeft = left;
            mRight = right;
            mTop = top;
            mBottom = bottom;
        }

        public void setChildren(ILayoutViewInfo[] children) {
            mChildren = children;
        }

        public ILayoutViewInfo[] getChildren() {
            return mChildren;
        }

        public Object getViewKey() {
            return mKey;
        }

        public String getName() {
            return mName;
        }

        public int getLeft() {
            return mLeft;
        }

        public int getTop() {
            return mTop;
        }

        public int getRight() {
            return mRight;
        }

        public int getBottom() {
            return mBottom;
        }
    }
}
