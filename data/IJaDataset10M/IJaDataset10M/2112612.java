package kfschmidt.manuallyregisterimages;

import java.util.Vector;
import java.awt.geom.AffineTransform;

public class TransformRecord {

    Vector mSliceTransforms;

    int mCurslice = -1;

    public TransformRecord() {
        mSliceTransforms = new Vector();
    }

    public void copyXFormFromSlice(int i) {
        try {
            SliceTransform fromslice = (SliceTransform) mSliceTransforms.elementAt(i);
            SliceTransform curslice = (SliceTransform) mSliceTransforms.elementAt(mCurslice);
            curslice.mRotation = fromslice.mRotation;
            curslice.mXOffset = fromslice.mXOffset;
            curslice.mYOffset = fromslice.mYOffset;
            curslice.mXScale = fromslice.mXScale;
            curslice.mYScale = fromslice.mYScale;
            syncXForm();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void translate(double dx, double dy) {
        setXOffset(getXOffset() + dx);
        setYOffset(getYOffset() + dy);
        syncXForm();
    }

    public void rotate(double rads) {
        setRotation(getRotation() + rads);
        syncXForm();
    }

    public void scale(double scalex, double scaley) {
        setXScale(getXScale() * scalex);
        setYScale(getYScale() * scaley);
        syncXForm();
    }

    public void delSlice() {
        mSliceTransforms.removeElementAt(mCurslice);
        decrementCurSlice();
    }

    public int getCurSlice() {
        return mCurslice;
    }

    public void setCurSlice(int slice) {
        if (slice < mSliceTransforms.size()) mCurslice = slice;
    }

    public void incrementCurSlice() {
        if (mCurslice < mSliceTransforms.size() - 1) mCurslice++;
    }

    public void decrementCurSlice() {
        if (mCurslice > 0) mCurslice--;
    }

    public int getSliceCount() {
        return mSliceTransforms.size();
    }

    public void addSlice() {
        mSliceTransforms.addElement(new SliceTransform());
        incrementCurSlice();
    }

    public double getRotation() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mRotation;
    }

    public double getXOffset() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mXOffset;
    }

    public double getYOffset() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mYOffset;
    }

    public double getXScale() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mXScale;
    }

    public double getYScale() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mYScale;
    }

    public RegImage getReferenceImage() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mReferenceImg;
    }

    public RegImage getSourceImage() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mSourceImg;
    }

    public AffineTransform getXForm() {
        return ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mSourcePixelsSpaceToReferencePixelSpaceTransform;
    }

    public void setRotation(double rot) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mRotation = rot;
        syncXForm();
    }

    public void setXOffset(double xoffset) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mXOffset = xoffset;
        syncXForm();
    }

    public void setYOffset(double yoffset) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mYOffset = yoffset;
        syncXForm();
    }

    public void setXScale(double xscale) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mXScale = xscale;
        syncXForm();
    }

    public void setYScale(double yscale) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mYScale = yscale;
        syncXForm();
    }

    public void setReferenceImage(RegImage ri) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mReferenceImg = ri;
    }

    public void setSourceImage(RegImage ri) {
        ((SliceTransform) mSliceTransforms.elementAt(mCurslice)).mSourceImg = ri;
    }

    private void syncXForm() {
        getXForm().setToIdentity();
        getXForm().translate(getXOffset(), getYOffset());
        double halfwidth = getSourceImage().getRendering().getWidth() / 2d;
        double halfheight = getSourceImage().getRendering().getHeight() / 2d;
        getXForm().translate(halfwidth, halfheight);
        getXForm().rotate(getRotation());
        getXForm().translate(-halfwidth, -halfheight);
        getXForm().scale(getXScale(), getYScale());
    }

    class SliceTransform {

        double mRotation = 0f;

        double mXOffset = 0f;

        double mYOffset = 0f;

        double mXScale = 1f;

        double mYScale = 1f;

        AffineTransform mSourcePixelsSpaceToReferencePixelSpaceTransform = new AffineTransform();

        RegImage mReferenceImg;

        RegImage mSourceImg;
    }
}
