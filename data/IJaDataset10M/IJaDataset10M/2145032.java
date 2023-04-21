package de.intarsys.pdf.content;

import de.intarsys.pdf.cos.COSDictionary;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.pd.PDImage;
import de.intarsys.pdf.pd.PDShading;

/**
 * Only text related operations will reach the device.
 * <p>
 * This implementation will ignore clipping paths!
 * 
 */
public class CSTextFilter extends CSDeviceFilter {

    public CSTextFilter(ICSDevice device) {
        super(device);
    }

    @Override
    protected void doImage(COSName name, PDImage image) throws CSException {
    }

    @Override
    public void doShading(COSName resourceName, PDShading shading) {
    }

    @Override
    public void inlineImage(PDImage img) {
    }

    @Override
    public void markedContentBegin(COSName tag) {
    }

    @Override
    public void markedContentBeginProperties(COSName tag, COSName resourceName, COSDictionary properties) {
    }

    @Override
    public void markedContentEnd() {
    }

    @Override
    public void markedContentPoint(COSName tag) {
    }

    @Override
    public void markedContentPointProperties(COSName tag, COSName resourceName, COSDictionary properties) {
    }

    @Override
    public void pathClipEvenOdd() {
    }

    @Override
    public void pathClipNonZero() {
    }

    @Override
    public void pathClose() {
    }

    @Override
    public void pathCloseFillStrokeEvenOdd() {
    }

    @Override
    public void pathCloseFillStrokeNonZero() {
    }

    @Override
    public void pathCloseStroke() {
    }

    @Override
    public void pathEnd() {
    }

    @Override
    public void pathFillEvenOdd() {
    }

    @Override
    public void pathFillNonZero() {
    }

    @Override
    public void pathFillStrokeEvenOdd() {
    }

    @Override
    public void pathFillStrokeNonZero() {
    }

    @Override
    public void pathStroke() {
    }

    @Override
    public void penCurveToC(float x1, float y1, float x2, float y2, float x3, float y3) {
    }

    @Override
    public void penCurveToV(float x2, float y2, float x3, float y3) {
    }

    @Override
    public void penCurveToY(float x1, float y1, float x3, float y3) {
    }

    @Override
    public void penLineTo(float x, float y) {
    }

    @Override
    public void penMoveTo(float x, float y) {
    }

    @Override
    public void penRectangle(float x, float y, float w, float h) {
    }
}
