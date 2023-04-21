package de.hilses.droidreader;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class DroidReaderDocument {

    /**
	 * interface for notification on ready rendered bitmaps
	 */
    interface RenderListener {

        /**
		 * is called by the RenderThread when a new Pixmap is ready
		 */
        public void onNewRenderedPixmap();
    }

    private class DummyRenderListener implements RenderListener {

        public void onNewRenderedPixmap() {
            return;
        }
    }

    /**
	 * renders Pixmaps for PdfPage objects and manages a LIFO queue for requests
	 */
    class RenderThread extends Thread {

        static final String TAG = "DroidReaderRenderThread";

        protected static final boolean LOG = false;

        /**
		 * Thread state keeper
		 */
        boolean mRun = true;

        long mLazyStart = 0;

        public void newRenderJob(long lazyStart) {
            if (LOG) Log.d(TAG, "got a new render job");
            mLazyStart = lazyStart;
            interrupt();
        }

        /**
		 * Thread run() loop, stopping only when mRun is set to false
		 */
        @Override
        public void run() {
            while (mRun) {
                if (LOG) Log.d(TAG, "starting loop");
                try {
                    if (LOG) Log.d(TAG, "RenderThread going to sleep");
                    Thread.sleep(3600000);
                } catch (InterruptedException e) {
                    if (LOG) Log.d(TAG, "RenderThread woken up");
                }
                if (mRun) {
                    boolean doRender = true;
                    do {
                        if (mLazyStart > 0) {
                            try {
                                Thread.sleep(mLazyStart);
                                doRender = true;
                            } catch (InterruptedException e) {
                                doRender = false;
                            }
                        }
                    } while (doRender == false);
                }
                if (mRun) {
                    if (LOG) Log.d(TAG, "now rendering the current render job");
                    synchronized (mDocumentLock) {
                        if (mDocument.mHandle != 0 && mPage.mHandle != 0) {
                            if (mMetadataDirty) {
                                calcPageMetadata();
                            }
                            calcCenteredViewBox();
                            try {
                                if (LOG) Log.d(TAG, "now rendering: " + mViewBox.toShortString());
                                mView.render(mDocument, mPage, mViewBox, mPageMatrix);
                            } catch (PageRenderException e) {
                            }
                        }
                        mHavePixmap = true;
                    }
                    if (LOG) Log.d(TAG, "now alerting the RenderListener");
                    mRenderListener.onNewRenderedPixmap();
                }
            }
            if (LOG) Log.d(TAG, "shutting down.");
        }
    }

    static final String TAG = "DroidReaderDocument";

    protected static final boolean LOG = false;

    /**
	 * Constant: Zoom to fit page
	 */
    public static final float ZOOM_FIT = -1F;

    /**
	 * Constant: Zoom to fit page width
	 */
    protected static final float ZOOM_FIT_WIDTH = -2F;

    /**
	 * Constant: Zoom to fit page height
	 */
    protected static final float ZOOM_FIT_HEIGHT = -3F;

    protected static final int CONTENT_FIT_NONE = 0;

    protected static final int CONTENT_FIT_ALL = 1;

    protected static final int CONTENT_FIT_WIDTH = 2;

    protected static final int CONTENT_FIT_HEIGHT = 3;

    protected static final int RENDER_NOW = 0;

    protected static final int RENDER_LAZY = 250;

    protected static final int PAGE_LAST = -1;

    protected RenderThread mRenderThread = null;

    public final PdfDocument mDocument = new PdfDocument();

    public final PdfPage mPage = new PdfPage();

    final PdfView mView = new PdfView();

    boolean mMetadataDirty = false;

    boolean mHavePixmap = false;

    boolean mDoRender = false;

    public float mZoom = 1.0F;

    int mDpiX = 160;

    int mDpiY = 160;

    int mTileMaxX = 1024;

    int mTileMaxY = 1024;

    public int mRotation = 0;

    public int mOffsetX = 0;

    public int mOffsetY = 0;

    public int mMarginOffsetX = 0;

    public int mMarginOffsetY = 0;

    public int mContentFitMode = 0;

    int mDisplaySizeX = 1;

    int mDisplaySizeY = 1;

    int mPageSizeX = 0;

    int mPageSizeY = 0;

    int mOffsetMaxX = 0;

    int mOffsetMaxY = 0;

    Rect mViewBox = new Rect(0, 0, 1, 1);

    Matrix mPageMatrix = new Matrix();

    boolean mIsScrollingX = false;

    boolean mIsScrollingY = false;

    public boolean mHorizontalScrollLock = false;

    RenderListener mRenderListener = new DummyRenderListener();

    final Object mDocumentLock = new Object();

    public void open(String filename, String password, int pageNo) throws PasswordNeededException, WrongPasswordException, CannotRepairException, CannotDecryptXrefException, PageLoadException {
        if (LOG) Log.d(TAG, "opening document: " + filename);
        mOffsetX = mMarginOffsetX;
        mOffsetY = mMarginOffsetY;
        synchronized (mDocumentLock) {
            mPage.close();
            mDocument.open(filename, password);
            mPage.open(mDocument, pageNo, 0);
        }
        if (mContentFitMode != CONTENT_FIT_NONE) calcContentFit();
        mHavePixmap = false;
        mMetadataDirty = true;
        render(false);
    }

    public void openPage(int no, boolean isRelative) throws PageLoadException {
        if (LOG) Log.d(TAG, "opening page " + (isRelative ? "(rel) " : "(abs) ") + no);
        mOffsetX = mMarginOffsetX;
        mOffsetY = mMarginOffsetY;
        synchronized (mDocumentLock) {
            int realPageNo = ((isRelative ? mPage.no : 0) + no);
            if (!isRelative && (realPageNo == PAGE_LAST)) realPageNo = mDocument.pagecount;
            mPage.close();
            mPage.open(mDocument, realPageNo, 0);
        }
        if (mContentFitMode != CONTENT_FIT_NONE) calcContentFit();
        mHavePixmap = false;
        mMetadataDirty = true;
        render(false);
    }

    boolean havePage(int no, boolean isRelative) {
        synchronized (mDocumentLock) {
            int realPageNo = ((isRelative ? mPage.no : 0) + no);
            if ((realPageNo < 1) || (realPageNo > mDocument.pagecount)) return false;
        }
        return true;
    }

    public boolean isPageLoaded() {
        synchronized (mDocumentLock) {
            if (mDocument.mHandle == 0 || mPage.mHandle == 0) return false;
        }
        return true;
    }

    boolean havePixmap() {
        return mHavePixmap;
    }

    void render(long lazyStart) {
        if (mDoRender && (mRenderThread != null)) {
            mRenderThread.newRenderJob(lazyStart);
        }
    }

    void render(boolean lazyStart) {
        if (lazyStart) {
            render(RENDER_LAZY);
        } else {
            render(RENDER_NOW);
        }
    }

    public void setDisplayInvert(boolean invert) {
        mView.setDisplayInvert(invert);
    }

    public void setDpi(int x, int y) {
        if (LOG) Log.d(TAG, "setDpi: " + x + "," + y);
        mDpiX = x;
        mDpiY = y;
        mMetadataDirty = true;
        render(false);
    }

    public void setTileMax(int x, int y) {
        if (LOG) Log.d(TAG, "setTileMax: " + x + "," + y);
        mTileMaxX = x;
        mTileMaxY = y;
        mMetadataDirty = true;
        render(false);
    }

    void setRotation(int degrees, boolean isRelative) {
        if (LOG) Log.d(TAG, "setRotation: " + (isRelative ? "(rel) " : "(abs) ") + degrees + "°");
        mRotation = ((isRelative ? mRotation : 0) + degrees + 360) % 360;
        mMetadataDirty = true;
        render(false);
    }

    public void setZoom(float zoom, boolean isRelative) {
        if (LOG) Log.d(TAG, "setZoom: " + (isRelative ? "(rel) " : "(abs) ") + zoom);
        mZoom = (isRelative ? mZoom : 1) * zoom;
        mMetadataDirty = true;
        render(false);
    }

    void setContentFitMode(int newMode) {
        if (LOG) Log.d(TAG, "setContentFitMode: " + newMode);
        mContentFitMode = newMode;
        calcContentFit();
        mMetadataDirty = true;
        render(false);
    }

    void offset(int x, int y, boolean isRelative) {
        if (LOG) Log.d(TAG, "offset: " + (isRelative ? "(rel) " : "(abs) ") + x + "," + y);
        if (mMetadataDirty) calcPageMetadata();
        if (!mHorizontalScrollLock) mOffsetX = (isRelative ? mOffsetX : 0) + x;
        mOffsetY = (isRelative ? mOffsetY : 0) + y;
        if (mOffsetX > mOffsetMaxX) mOffsetX = mOffsetMaxX; else if (mOffsetX < 0) mOffsetX = 0;
        if (mOffsetY > mOffsetMaxY) mOffsetY = mOffsetMaxY; else if (mOffsetY < 0) mOffsetY = 0;
        if (!withinViewBox()) {
            render(true);
        }
    }

    void startRendering(int displaySizeX, int displaySizeY) {
        if (LOG) Log.d(TAG, "startRendering");
        mDisplaySizeX = displaySizeX;
        mDisplaySizeY = displaySizeY;
        mMetadataDirty = true;
        if (mContentFitMode != CONTENT_FIT_NONE) calcContentFit();
        mDoRender = true;
        if (mRenderThread == null) mRenderThread = new RenderThread();
        try {
            mRenderThread.start();
        } catch (IllegalThreadStateException e) {
        }
        render(false);
    }

    void stopRendering() {
        boolean retry = true;
        if (LOG) Log.d(TAG, "stopRendering");
        mDoRender = false;
        if (mRenderThread != null) {
            mRenderThread.mRun = false;
            mRenderThread.interrupt();
            while (retry) {
                try {
                    mRenderThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
            mRenderThread = null;
        }
    }

    private void calcContentFit() {
        float top;
        float bottom;
        float left;
        float right;
        float height;
        float width;
        top = mPage.mMediabox[3] - mPage.mContentbox[3];
        bottom = mPage.mMediabox[3] - mPage.mContentbox[1];
        left = mPage.mContentbox[0] - mPage.mMediabox[0];
        right = mPage.mContentbox[2] - mPage.mMediabox[1];
        if (top < 0) top = 0;
        if (bottom > mPage.mMediabox[3]) bottom = mPage.mMediabox[3];
        if (left < 0) left = 0;
        if (right > mPage.mMediabox[2]) right = mPage.mMediabox[2];
        if (((mPage.rotate + mRotation) % 180) == 90) {
            width = bottom - top;
            height = right - left;
        } else {
            height = bottom - top;
            width = right - left;
        }
        if (mContentFitMode == CONTENT_FIT_ALL) {
            float zoomH = mDisplaySizeY / height;
            float zoomW = mDisplaySizeX / width;
            if (zoomH < zoomW) {
                mZoom = zoomH * 72 / mDpiY;
            } else {
                mZoom = zoomW * 72 / mDpiX;
            }
        }
        if (mContentFitMode == CONTENT_FIT_HEIGHT) {
            mZoom = (mDisplaySizeY / height) * 72 / mDpiY;
        }
        if (mContentFitMode == CONTENT_FIT_WIDTH) {
            mZoom = (mDisplaySizeX / width) * 72 / mDpiX;
        }
        if (mZoom > (float) 4) mZoom = (float) 4;
        if (mZoom < (float) 0.05) mZoom = (float) 0.05;
        mOffsetX = (int) (left * mZoom / 72 * mDpiX);
        mOffsetY = (int) (top * mZoom / 72 * mDpiY);
        Log.d(TAG, String.format("calcContentFit(): (%d,%d) - (%d,%d) Zoom = %6.3f D = (%d,%d) R = %d", (int) left, (int) top, (int) right, (int) bottom, mZoom, (int) mDisplaySizeX, (int) mDisplaySizeY, mPage.rotate + mRotation));
    }

    private void calcPageMetadata() {
        if (LOG) Log.d(TAG, "calcPageMetadata()");
        float zoomX = mZoom * mDpiX / 72;
        float zoomY = mZoom * mDpiY / 72;
        float pageHeight = mPage.mMediabox[3] - mPage.mMediabox[1];
        float pageWidth = mPage.mMediabox[2] - mPage.mMediabox[0];
        mPageMatrix.reset();
        mPageMatrix.postScale(1, -1);
        mPageMatrix.postTranslate(-mPage.mMediabox[0], mPage.mMediabox[3]);
        mPageMatrix.postRotate(mPage.rotate + mRotation);
        if (((mPage.rotate + mRotation) % 360) == 90) {
            mPageMatrix.postTranslate(pageHeight, 0);
        } else if (((mPage.rotate + mRotation) % 360) == 180) {
            mPageMatrix.postTranslate(pageWidth, pageHeight);
        } else if (((mPage.rotate + mRotation) % 360) == 270) {
            mPageMatrix.postTranslate(0, pageWidth);
        }
        if (((mPage.rotate + mRotation) % 180) == 90) {
            float save = pageHeight;
            pageHeight = pageWidth;
            pageWidth = save;
        }
        if (mZoom == ZOOM_FIT) {
            float zoomH = ((float) mDisplaySizeY) / pageHeight;
            float zoomW = ((float) mDisplaySizeX) / pageWidth;
            if (zoomH < zoomW) {
                zoomX = zoomY = zoomH;
                mZoom = zoomY * 72 / mDpiY;
            } else {
                zoomX = zoomY = zoomW;
                mZoom = zoomX * 72 / mDpiX;
            }
        }
        if (mZoom == ZOOM_FIT_HEIGHT) {
            zoomX = zoomY = ((float) mDisplaySizeY) / pageHeight;
            mZoom = zoomY * 72 / mDpiY;
        }
        if (mZoom == ZOOM_FIT_WIDTH) {
            zoomX = zoomY = ((float) mDisplaySizeX) / pageWidth;
            mZoom = zoomX * 72 / mDpiX;
        }
        mPageMatrix.postScale(zoomX, zoomY);
        mPageSizeX = (int) (pageWidth * zoomX);
        mPageSizeY = (int) (pageHeight * zoomY);
        if (mPageSizeX <= mDisplaySizeX) {
            mIsScrollingX = false;
            mOffsetX = 0;
            mOffsetMaxX = 0;
        } else {
            mIsScrollingX = true;
            mOffsetMaxX = mPageSizeX - mDisplaySizeX;
        }
        if (mPageSizeY <= mDisplaySizeY) {
            mIsScrollingY = false;
            mOffsetY = 0;
            mOffsetMaxY = 0;
        } else {
            mIsScrollingY = true;
            mOffsetMaxY = mPageSizeY - mDisplaySizeY;
        }
        mMetadataDirty = false;
        if (LOG) Log.d(TAG, "calculated new display metadata");
    }

    private boolean withinViewBox() {
        if (mIsScrollingX && (((mOffsetX + mDisplaySizeX) > mViewBox.right) || (mOffsetX < mViewBox.left))) return false;
        if (mIsScrollingY && (((mOffsetY + mDisplaySizeY) > mViewBox.bottom) || (mOffsetY < mViewBox.top))) return false;
        return true;
    }

    void calcCenteredViewBox() {
        int offsetX = mOffsetX - ((mTileMaxX - mDisplaySizeX) / 2);
        int offsetY = mOffsetY - ((mTileMaxY - mDisplaySizeY) / 2);
        if (offsetX < 0) offsetX = 0;
        if (offsetY < 0) offsetY = 0;
        if (LOG) Log.d(TAG, "calcCenteredViewBox: new offsets " + offsetX + "," + offsetY);
        mViewBox.set(offsetX, offsetY, ((mPageSizeX <= mTileMaxX) ? mPageSizeX : (offsetX + mTileMaxX)), ((mPageSizeY <= mTileMaxY) ? mPageSizeY : (offsetY + mTileMaxY)));
    }

    public void closeDocument() {
        stopRendering();
        synchronized (mDocumentLock) {
            mPage.close();
            mDocument.close();
        }
    }
}
