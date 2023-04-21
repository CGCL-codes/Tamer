package com.mob.android.memory;

import java.util.Arrays;
import java.util.Random;
import android.os.Handler;
import android.widget.GridView;

enum ImageState {

    EMPTY, COVERED, SHOWING
}

public class ImageAdapterData implements Runnable {

    private static ImageAdapterData instance = new ImageAdapterData();

    public Boolean picturesCoverInProgress = false;

    private GridView gridviewToUpdate;

    private Integer mFirstSelected = -1;

    private Integer mSecondSelected = -1;

    private Integer[] mThumbIds;

    private ImageState[] mImageState;

    private Handler mWaitToCoverHandler = new Handler();

    private final Random myRandom = new Random();

    private ImageAdapterData() {
        initImageData();
    }

    private void initImageData() {
        mThumbIds = new Integer[] { R.drawable.img0001, R.drawable.img0002, R.drawable.img0003, R.drawable.img0004, R.drawable.img0005, R.drawable.img0006, R.drawable.img0007, R.drawable.img0008, R.drawable.img0009, R.drawable.img0001, R.drawable.img0002, R.drawable.img0003, R.drawable.img0004, R.drawable.img0005, R.drawable.img0006, R.drawable.img0007, R.drawable.img0008, R.drawable.img0009 };
        mImageState = new ImageState[mThumbIds.length];
        Arrays.fill(mImageState, ImageState.COVERED);
        shuffle();
    }

    private void shuffle() {
        Integer swap;
        Integer index;
        for (int i = 0; i < mThumbIds.length; i++) {
            index = myRandom.nextInt(mThumbIds.length);
            swap = mThumbIds[index];
            mThumbIds[index] = mThumbIds[i];
            mThumbIds[i] = swap;
        }
    }

    public static ImageAdapterData getInstance() {
        return instance;
    }

    public void setGridView(GridView gridview) {
        gridviewToUpdate = gridview;
    }

    public Integer getImage(Integer position) {
        if (mImageState[position] == ImageState.EMPTY) {
            return R.drawable.empty;
        }
        return mImageState[position] == ImageState.SHOWING ? mThumbIds[position] : R.drawable.background;
    }

    public Integer getImageCount() {
        return mThumbIds.length;
    }

    public void handleClickedImage(Integer position) {
        if (mImageState[position] == ImageState.EMPTY) return;
        if (mImageState[position] != ImageState.SHOWING) {
            if (canSelect()) {
                mImageState[position] = ImageState.SHOWING;
                selectImage(position);
            }
        }
        if (testCompleteUncovered()) {
            initImageData();
        } else {
            checkSelectedAndRepaint();
        }
    }

    private Boolean testCompleteUncovered() {
        for (ImageState imageState : mImageState) {
            if (imageState != ImageState.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private void checkSelectedAndRepaint() {
        if (!canSelect()) {
            startWaitToCover();
        }
        gridviewToUpdate.invalidateViews();
    }

    private Boolean canSelect() {
        return (mFirstSelected < 0) || (mSecondSelected < 0);
    }

    private void resetSelection() {
        mFirstSelected = -1;
        mSecondSelected = -1;
        picturesCoverInProgress = false;
    }

    private void selectImage(Integer position) {
        if (mFirstSelected < 0) {
            mFirstSelected = position;
        } else {
            if (mSecondSelected < 0) {
                mSecondSelected = position;
            }
        }
    }

    private Boolean testSelected() {
        if ((mFirstSelected >= 0) && (mSecondSelected >= 0)) {
            return (mThumbIds[mFirstSelected].intValue() == mThumbIds[mSecondSelected].intValue());
        } else {
            return false;
        }
    }

    private void startWaitToCover() {
        picturesCoverInProgress = true;
        mWaitToCoverHandler.removeCallbacks(this);
        mWaitToCoverHandler.postDelayed(this, 2000);
    }

    private void setSelectedImagesToImageState(ImageState imageState) {
        mImageState[mFirstSelected] = imageState;
        mImageState[mSecondSelected] = imageState;
    }

    @Override
    public void run() {
        mWaitToCoverHandler.removeCallbacks(this);
        if (testSelected()) {
            setSelectedImagesToImageState(ImageState.EMPTY);
        } else {
            setSelectedImagesToImageState(ImageState.COVERED);
        }
        resetSelection();
        gridviewToUpdate.invalidateViews();
    }
}
