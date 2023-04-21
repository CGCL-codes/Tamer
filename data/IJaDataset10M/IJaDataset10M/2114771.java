package coat;

import java.awt.*;
import java.awt.geom.*;
import java.text.*;

/**
 * A constructor is not available. Use the createAxis() method.
 *
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 */
public class Axis implements IFixedDrawable {

    public static final int GRID_NONE = 0;

    public static final int GRID_MAJOR = 1;

    public static final int GRID_MAJOR_MINOR = 2;

    public static final int LABEL_HORIZONTAL = 0;

    public static final int LABEL_VERTICAL = 1;

    public static final double SQRT_TWO = 1.414213562;

    public static final double SQRT_TEN = 3.162277660;

    public static final double SQRT_TWENTY = 7.071067812;

    protected static final int PRETTY_AXIS_LABEL_COUNT = 8;

    public static final int X_AXIS = 0;

    public static final int Y_AXIS = 1;

    protected boolean tDrawXAxis = true;

    protected boolean tDrawYAxis = true;

    protected int iGridStyle = GRID_NONE;

    protected int iLabelStyleY = LABEL_HORIZONTAL;

    protected int iBigTicksLen = 4;

    protected int iSmallTicksLen = 2;

    protected int iSmallTicksCount = 2;

    /** Formatter for the decimal stuff. */
    protected DecimalFormat oFormat = Pane.createDecimalFormatter("###,##0.00##");

    /** Transformation for 90? drawings. */
    protected static AffineTransform oVerticalTrans = AffineTransform.getRotateInstance(Math.toRadians(90));

    /** Used for logarithmic label formatting. Performance variable. */
    protected static final String ZERO_STRING = "0000000000000000000000000000000000000000";

    /**
   * Creates a standard axis.
   */
    public Axis() {
    }

    /**
   * Returns the logarithm (base 10) to the given number.
   * 
   * @param dVal The number to be logithm'nd.
   * @return The logarithm of the number to base 10.
   */
    public static final double log10(double dVal) {
        return 0.4342944819d * Math.log(dVal);
    }

    /**
   * Returns the val'th power of base 10.
   * 
   * @param dVal The value to be powered.
   * @return The val'th power of base 10.
   */
    public static final double exp10(double dVal) {
        return Math.exp(dVal / 0.4342944819d);
    }

    /**
   * Returns the beautified (most correct) font height since the JDK
   * cannot handle this.
   */
    protected static final int getFontHeight(FontMetrics oMet) {
        return oMet.getAscent() - 2;
    }

    /**
   * Given a user range (by x_min and x_max) the available pixel width and the width
   * that an axis label will consume, I calculate a x_min_new and x_max_new inside
   * the given range as well as a delta.
   * <p>All those values are meant to look pretty for the human eye.
   * I will try to calculate the ticking such that at least two labels
   * will be set.
   * <p>With the parameter iApproximateNumberOfLabels you can specify how many labels
   * you want. If this is set to 0 (or any small value), I try to cram as many labels
   * as possible onto screen. This is a fuzzy value.
   * <p>Of course all that works also with y.
   * <p>The returned array has 3 positions: {x_min_new, x_max_new, delta}.
   * 
   * @param iXMin The minimum x value.
   * @param iXMax The maximum x value.
   * @param iWid The place that is available for the whole axis.
   * @param iStrWid The place that a string will consume.
   * @param iApproximateNumberOfLabels The approx desired number of labels.
   * @return {x_min_new, x_max_new, delta}.
   */
    public static final double[] calcPrettyRange(double dXMin, double dXMax, int iWid, int iStrWid, int iApproximateNumberOfLabels) {
        double dWid = dXMax - dXMin;
        double dRangeDtoU = dWid / (double) iWid;
        double dStrWid = (double) iStrWid * dRangeDtoU;
        double dDigitCount = Math.floor(log10(dWid / 2));
        double dAddi = Math.pow(10, dDigitCount);
        double dXFirst = Math.ceil(dXMin / dAddi) * dAddi;
        double dXLast = Math.floor(dXMax / dAddi) * dAddi;
        int iIter = 0;
        final int iMaxIter = 100;
        int iStep = 0;
        double dMult = 2;
        while (dAddi < dStrWid && iIter < iMaxIter) {
            dAddi *= dMult;
            iStep = (iStep + 1) % 3;
            dMult = 1 == iStep ? 2.5 : 2;
            iIter++;
        }
        dXLast = Math.floor(dXMax / dAddi) * dAddi;
        double dMaxLabels = (double) iApproximateNumberOfLabels * 1.5;
        double dPrettyWid = dXLast - dXFirst;
        double dLabelCount = Math.floor(dPrettyWid / dAddi);
        iIter = 0;
        while (dLabelCount > dMaxLabels && iIter < iMaxIter) {
            dAddi *= dMult;
            dMult = 2 == dMult ? 5 : 2;
            dXLast = Math.floor(dXMax / dAddi) * dAddi;
            dPrettyWid = dXLast - dXFirst;
            dLabelCount = Math.floor(dPrettyWid / dAddi);
            iIter++;
        }
        if (0.0 == dAddi || dXFirst == dXLast) {
            return new double[] { dXFirst, dXFirst, 0.0 };
        }
        return new double[] { dXFirst, dXLast, dAddi };
    }

    /**
   * When logarithmic scaling is in effect, other rules apply.
   * Instead of using the original range, I break the range down to the log10 of the
   * values, which, in effect, means that the actual value at the tick must be
   * 10^value.
   * This has the convenience that even the minor ticks have automatically a logarithmic
   * distribution.
   * <p>The returned array has 3 positions: {x_min_new, x_max_new, delta}.
   * 
   * @param iXMin The minimum x value.
   * @param iXMax The maximum x value.
   * @param iWid The place that is available for the whole axis.
   * @param iStrWid The place that a string will consume.
   * @param iApproximateNumberOfLabels The approx desired number of labels.
   * @return {x_min_new, x_max_new, delta}.
   */
    public static final double[] calcPrettyRangeLogarithmic(double dXMin, double dXMax, int iWid, int iStrWid, int iApproximateNumberOfLabels) {
        if (dXMax < 0) {
            dXMax = Transformer.LOG_MIN_VALUE + (dXMax - dXMin);
        }
        if (dXMin < 0) {
            dXMin = Transformer.LOG_MIN_VALUE;
        }
        double dXFirst = Math.floor(log10(dXMin));
        double dXLast = Math.floor(log10(dXMax));
        double dAddi = 1;
        double dDigitCount = dXLast - dXFirst;
        int iMaxIter = 100, iIter = 0;
        while (iWid * dAddi / dDigitCount < 10 && iIter < iMaxIter) {
            dAddi++;
            iIter++;
        }
        return new double[] { dXFirst, dXLast, dAddi };
    }

    /**
   * Calculates a pretty number of minor ticks for a given space on screen.
   * 
   * @param iSpaceBetweenMajorTicks The pixels between two major ticks.
   * @return Recommended number of minor ticks.
   */
    public static final int calcPrettyMinorTicks(int iSpaceBetweenMajorTicks) {
        if (iSpaceBetweenMajorTicks >= 80) {
            return 9;
        } else if (iSpaceBetweenMajorTicks >= 40) {
            return 4;
        } else if (iSpaceBetweenMajorTicks >= 4) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
   * Calculates the width of the labels for a given range.
   * 
   * @param oMet The font metrics for drawing the labels.
   * @param dX0 The minimum x position.
   * @param dXE The maximum x position.
   */
    public int calcRangeLabelWidth(FontMetrics oMet, double dX0, double dXE) {
        final double dWid = dXE - dX0;
        final int iTry1 = oMet.stringWidth(getAxisLabel(dX0, dWid));
        final int iTry2 = oMet.stringWidth(getAxisLabel(dXE, dWid));
        final int iFontWid = (int) Math.max(iTry1, iTry2);
        return iFontWid;
    }

    /**
   * Sets one of the axis (x or y) visible or invisible.
   * 
   * @param iPart Either X_AXIS or Y_AXIS.
   */
    public void setVisible(int iPart, boolean tVisible) {
        switch(iPart) {
            case X_AXIS:
                tDrawXAxis = tVisible;
                break;
            case Y_AXIS:
                tDrawYAxis = tVisible;
                break;
        }
    }

    /**
   * Indicates whether one of the axis (x or y) is visible.
   * 
   * @param iPart Either X_AXIS or Y_AXIS.
   */
    public boolean isVisible(int iPart) {
        switch(iPart) {
            case X_AXIS:
                return tDrawXAxis;
            case Y_AXIS:
                return tDrawYAxis;
        }
        throw new IllegalArgumentException("Axis2D.isVisible: There is no part \"" + iPart + "\".");
    }

    /**
   * Meant for subclasses that prefer fantasia labels. Overload if you like.
   * A label is the string representation for the value at any coordinate.
   *
   * @param dValue The value of the label (usually the position).
   * @param dRange The overall range in which we are - good for choosing the format.
   */
    protected String getAxisLabel(double dValue, double dRange) {
        if (dValue < 1E-4 && dValue > -1E-4 && dValue != 0.0) {
            oFormat.applyPattern("0.####E0#");
        } else if (dRange > 1E6) {
            oFormat.applyPattern("###,###,##0");
        } else if (dRange > 10) {
            oFormat.applyPattern("###,##0");
        } else if (dRange > 1) {
            oFormat.applyPattern("###,##0.0##");
        } else if (dRange > 1E-1) {
            oFormat.applyPattern("###,##0.00##");
        } else if (dRange > 1E-2) {
            oFormat.applyPattern("###,##0.000##");
        } else if (dRange > 1E-3) {
            oFormat.applyPattern("###,##0.0000####");
        } else if (dRange > 1E-6) {
            oFormat.applyPattern("###,##0.0000####");
        } else {
            oFormat.applyPattern("###,##0.########E0#");
        }
        return oFormat.format(dValue);
    }

    /**
   * Returns a logarithmic label for the given power.
   */
    protected String getAxisLabelLogarithmic(int iPow) {
        final int LOGARITHMIC_POWER_THRESHOLD = 6;
        if (iPow >= LOGARITHMIC_POWER_THRESHOLD) {
            return "1E" + iPow;
        } else if (iPow <= -(LOGARITHMIC_POWER_THRESHOLD - 2)) {
            return "1E" + iPow;
        } else if (iPow >= 0) {
            return "1" + ZERO_STRING.substring(0, iPow);
        } else {
            return "0." + ZERO_STRING.substring(0, -1 - iPow) + "1";
        }
    }

    /**
   * Returns the border space needed on the left.
   * Depends on the font currently used on the graphics object.
   * 
   * @param gGra The graphics for which to compute required space.
   */
    protected int getBorderSpaceLeft(Graphics2D gGra) {
        FontMetrics oMet = gGra.getFontMetrics();
        final int iBaseWid = iBigTicksLen + 2;
        switch(iLabelStyleY) {
            case LABEL_VERTICAL:
                return iBaseWid + getFontHeight(oMet);
            case LABEL_HORIZONTAL:
            default:
                return iBaseWid + oMet.stringWidth("00000000");
        }
    }

    /**
   * Returns the border space needed at the bottom.
   * Depends on the font currently used on the graphics object.
   * 
   * @param gGra The graphics for which to compute required space.
   */
    protected int getBorderSpaceDown(Graphics2D gGra) {
        final int iBaseHei = iBigTicksLen + 2;
        return iBaseHei + getFontHeight(gGra.getFontMetrics());
    }

    /**
   * Indicates whether this and the other IFixedDrawable can collaborate.
   * e.g. Axis can collaborate with another Axis (trivial).
   * Future versions may allow a collaboration of Legend and Logo.
   *
   * @param oDraw The other (possible compatible) IDrawable.
   */
    public boolean isCompatible(IFixedDrawable oDraw) {
        return oDraw instanceof Axis;
    }

    /**
   * Indicates whether the x axis should be visible.
   */
    public boolean isXAxisVisible() {
        return this.tDrawXAxis;
    }

    /**
   * Indicates whether the y axis should be visible.
   */
    public boolean isYAxisVisible() {
        return this.tDrawYAxis;
    }

    /**
   * Returns the grid style.
   * One of GRID_NONE, GRID_MAJOR, GRID_MAJOR_MINOR.
   */
    public int getGridStyle() {
        return iGridStyle;
    }

    /**
   * Sets a new grid style.
   * One of GRID_NONE, GRID_MAJOR, GRID_MAJOR_MINOR.
   * 
   * @param iGridStyle The new grid style.
   */
    public void setGridStyle(int iGridStyle) {
        this.iGridStyle = iGridStyle % 3;
    }

    /**
   * Returns the y axis label style.
   * One of LABEL_HORIZONTAL, LABEL_VERTICAL.
   */
    public int getYLabelStyle() {
        return this.iLabelStyleY;
    }

    /**
   * Sets the new y axis label style.
   * 
   * @param iLabelStyleY The new label style.
   */
    public void setYLabelStyle(int iLabelStyleY) {
        this.iLabelStyleY = iLabelStyleY;
    }

    /**
   * Draws a string vertically.
   */
    public void drawVerticalString(Graphics2D gGra, String sMsg, int iX, int iY) {
        AffineTransform oNormalTrans = gGra.getTransform();
        gGra.setTransform(oVerticalTrans);
        gGra.drawString(sMsg, iY, -iX);
        gGra.setTransform(oNormalTrans);
    }

    /**
   * Describes the properties for drawing an axis.
   */
    private static class AxisPosition {

        public boolean tLogAxis, tNonLinearTransformer;

        public int iLabelSpace, iSpaceBetweenLabels, iMinPixelBetweenTicks;

        public int iMinRectD, iMaxRectD, iSpanRectD;

        public double dMinRectU, dMaxRectU, dSpanRectU;

        public double dMinU, dMaxU, dDeltaU, dSpanU, dRangeUtoD, dMinorTickDelta;

        public int iMajorTickCount, iSpaceBetweenTicks, iMinorTickCount;

        public AxisPosition(int iLabelSpace, int iSpaceBetweenLabels, double dMinRectU, double dMaxRectU, int iMinRectD, int iMaxRectD, int iTransformModus) {
            this.iLabelSpace = iLabelSpace;
            this.iSpaceBetweenLabels = iSpaceBetweenLabels;
            this.iMinPixelBetweenTicks = iLabelSpace + iSpaceBetweenLabels;
            this.dMinRectU = dMinRectU;
            this.dMaxRectU = dMaxRectU;
            this.dSpanRectU = dMaxRectU - dMinRectU;
            this.iMinRectD = iMinRectD;
            this.iMaxRectD = iMaxRectD;
            this.iSpanRectD = iMaxRectD - iMinRectD;
            this.tNonLinearTransformer = -1 != iTransformModus && Transformer.LINEAR != iTransformModus;
            this.tLogAxis = tNonLinearTransformer && Transformer.LOGARITHMIC == iTransformModus;
            double[] adRange = tLogAxis ? calcPrettyRangeLogarithmic(dMinRectU, dMaxRectU, iSpanRectD, iMinPixelBetweenTicks, PRETTY_AXIS_LABEL_COUNT) : calcPrettyRange(dMinRectU, dMaxRectU, iSpanRectD, iMinPixelBetweenTicks, PRETTY_AXIS_LABEL_COUNT);
            if (tLogAxis && (adRange[1] - adRange[0]) < 2.0d) {
                tLogAxis = false;
                adRange = calcPrettyRange(dMinRectU, dMaxRectU, iSpanRectD, iMinPixelBetweenTicks, PRETTY_AXIS_LABEL_COUNT);
            }
            dMinU = adRange[0];
            dMaxU = adRange[1];
            dDeltaU = adRange[2];
            dSpanU = dMaxU - dMinU;
            dRangeUtoD = (double) iSpanRectD / dSpanRectU;
            iMajorTickCount = (int) Math.ceil((dMaxU - dMinU) / dDeltaU) + 1;
            iSpaceBetweenTicks = tLogAxis ? iSpanRectD / iMajorTickCount : (int) (dDeltaU * dRangeUtoD);
            iMinorTickCount = calcPrettyMinorTicks(iSpaceBetweenTicks);
            if (tLogAxis) {
                if (dDeltaU > 1.0) {
                    iMinorTickCount = 0;
                } else if (1 == iMinorTickCount) {
                    iMinorTickCount = iSpaceBetweenTicks > 15 ? 2 : 0;
                }
            }
            dMinorTickDelta = tLogAxis ? 9d / (double) (iMinorTickCount + 1) : dDeltaU / (iMinorTickCount + 1);
        }
    }

    /**
   * Draws outer x axis.
   */
    protected void drawX(Graphics2D gGra, int iBorderX, int iBorderY, Rectangle nRectD, Rectangle2D.Double nRectU, Transformer oTrans) {
        int iX0 = nRectD.x + iBorderX, iXE = nRectD.x + nRectD.width - 1, iY0 = nRectD.y, iYE = nRectD.y + nRectD.height - 1 - iBorderY;
        gGra.drawLine(iX0, iYE, iXE, iYE);
        FontMetrics oMet = gGra.getFontMetrics();
        final int iFontWid = calcRangeLabelWidth(oMet, nRectU.x, nRectU.x + nRectU.width);
        final int iXTransformModus = null == oTrans ? -1 : oTrans.getXTransformModus();
        AxisPosition oPos = new AxisPosition(iFontWid, 2, nRectU.x, nRectU.x + nRectU.width, iX0, iXE, iXTransformModus);
        Color nNormColor = gGra.getColor();
        float fNorm = (float) gGra.getBackground().getBlue() / 256f;
        float fMinor = fNorm * 0.9f;
        float fMajor = fMinor * 0.8f;
        Color nMinorColor = new Color(fMinor, fMinor, fMinor);
        Color nMajorColor = new Color(fMajor, fMajor, fMajor);
        Rectangle nSpaceD = new Rectangle(iX0, iY0, iXE - iX0, iYE - iY0);
        double dRelativeX = oPos.dMinU - nRectU.x;
        double dX;
        int iTickX;
        boolean tGridMinor = GRID_MAJOR_MINOR == iGridStyle;
        int iMinorX;
        for (int iCnt = -1; iCnt < oPos.iMajorTickCount; iCnt++) {
            iTickX = oPos.tLogAxis ? iXE : iX0 + (int) ((dRelativeX + (iCnt * oPos.dDeltaU)) * oPos.dRangeUtoD);
            if (iTickX > iXE) {
                break;
            }
            for (int iTic = 1; iTic <= oPos.iMinorTickCount; iTic++) {
                if (oPos.tLogAxis) {
                    dX = exp10(oPos.dMinU + iCnt) * (1 + (iTic * oPos.dMinorTickDelta));
                    iMinorX = oTrans.transformUtoD(dX, 0, nRectU, nSpaceD)[0];
                } else {
                    iMinorX = iTickX + (int) (iTic * (oPos.dMinorTickDelta * oPos.dRangeUtoD));
                }
                if (iMinorX > iX0 && iMinorX <= iXE) {
                    if (tGridMinor) {
                        gGra.setColor(nMinorColor);
                        gGra.drawLine(iMinorX, iY0, iMinorX, iYE);
                        gGra.setColor(nNormColor);
                    }
                    gGra.drawLine(iMinorX, iYE, iMinorX, iYE + iSmallTicksLen);
                }
                if (iMinorX > iXE) {
                    break;
                }
            }
        }
        boolean tGridMajor = GRID_MAJOR == iGridStyle || GRID_MAJOR_MINOR == iGridStyle;
        for (int iCnt = 0; iCnt < oPos.iMajorTickCount; iCnt++) {
            if (oPos.tLogAxis) {
                dX = exp10(oPos.dMinU + iCnt * oPos.dDeltaU);
                iTickX = oTrans.transformUtoD(dX, 0, nRectU, nSpaceD)[0];
            } else {
                iTickX = iX0 + (int) ((dRelativeX + (iCnt * oPos.dDeltaU)) * oPos.dRangeUtoD);
            }
            if (iTickX > iXE) {
                break;
            } else if (iTickX == iX0 - 1) {
                iTickX = iX0;
            }
            if (tGridMajor && iTickX > iX0) {
                gGra.setColor(nMajorColor);
                gGra.drawLine(iTickX, iY0, iTickX, iYE);
                gGra.setColor(nNormColor);
            }
            gGra.drawLine(iTickX, iYE, iTickX, iYE + iBigTicksLen);
        }
        if (Constants.ANTIALIAS) {
            gGra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        final int iFontHei = getFontHeight(oMet);
        int iStrX, iStrY = iYE + iFontHei + iBigTicksLen + 2, iStrWid, iStrWidHalf;
        int iMaxStrX = nRectD.x + nRectD.width;
        int iMinStrX = nRectD.x;
        String sVal;
        for (int iCnt = 0; iCnt < oPos.iMajorTickCount; iCnt++) {
            if (oPos.tLogAxis) {
                dX = exp10(oPos.dMinU + iCnt * oPos.dDeltaU);
                iTickX = oTrans.transformUtoD(dX, 0, nRectU, nSpaceD)[0];
            } else {
                dX = oPos.dMinU + (iCnt * oPos.dDeltaU);
                iTickX = iX0 + (int) ((dX - nRectU.x) * oPos.dRangeUtoD);
            }
            if (iTickX > iXE) {
                break;
            }
            if (!oPos.tLogAxis && oPos.tNonLinearTransformer) {
                dX = oTrans.transformDtoU(iTickX, 0, nSpaceD, nRectU)[0];
            }
            if (oPos.tLogAxis) {
                sVal = getAxisLabelLogarithmic((int) (oPos.dMinU + iCnt * oPos.dDeltaU));
            } else {
                sVal = getAxisLabel(dX, nRectU.width);
            }
            iStrWid = oMet.stringWidth(sVal);
            iStrWidHalf = iStrWid >> 1;
            iStrX = iTickX - iStrWidHalf;
            if (iStrX + iStrWid > iMaxStrX) {
                break;
            }
            if (iStrX >= iMinStrX) {
                gGra.drawString(sVal, iStrX, iStrY);
                iMinStrX = iStrX + iStrWid + 2;
            }
        }
    }

    /**
   * Draws outer y axis the horizontal style.
   */
    protected void drawY(Graphics2D gGra, int iBorderX, int iBorderY, Rectangle nRectD, Rectangle2D.Double nRectU, Transformer oTrans) {
        int iX0 = nRectD.x + iBorderX, iXE = nRectD.x + nRectD.width - 1, iY0 = nRectD.y, iYE = nRectD.y + nRectD.height - 1 - iBorderY;
        gGra.drawLine(iX0, iY0, iX0, iYE);
        FontMetrics oMet = gGra.getFontMetrics();
        final int iFontWid = calcRangeLabelWidth(oMet, nRectU.y, nRectU.y + nRectU.height);
        final int iFontHei = getFontHeight(oMet);
        final int iFontHeiHalf = iFontHei >> 1;
        final boolean tVerticalLabels = LABEL_VERTICAL == iLabelStyleY;
        final int iYTransformModus = null == oTrans ? -1 : oTrans.getYTransformModus();
        int iFontSpace = tVerticalLabels ? iFontWid : iFontHei;
        AxisPosition oPos = new AxisPosition(iFontSpace, 2, nRectU.y, nRectU.y + nRectU.height, iY0, iYE, iYTransformModus);
        Color nNormColor = gGra.getColor();
        float fNorm = (float) gGra.getBackground().getBlue() / 256f;
        float fMinor = fNorm * 0.9f;
        float fMajor = fMinor * 0.8f;
        Color nMinorColor = new Color(fMinor, fMinor, fMinor);
        Color nMajorColor = new Color(fMajor, fMajor, fMajor);
        Rectangle nSpaceD = new Rectangle(iX0, iY0, iXE - iX0, iYE - iY0);
        double dRelativeY = oPos.dMinU - nRectU.y;
        double dY;
        int iTickY;
        boolean tGridMinor = GRID_MAJOR_MINOR == iGridStyle;
        int iMinorY;
        for (int iCnt = -1; iCnt < oPos.iMajorTickCount; iCnt++) {
            iTickY = oPos.tLogAxis ? iY0 : iYE - (int) ((dRelativeY + (iCnt * oPos.dDeltaU)) * oPos.dRangeUtoD);
            if (iTickY < iY0) {
                break;
            }
            for (int iTic = 1; iTic <= oPos.iMinorTickCount; iTic++) {
                if (oPos.tLogAxis) {
                    dY = exp10(oPos.dMinU + iCnt) * (1 + (iTic * oPos.dMinorTickDelta));
                    iMinorY = oTrans.transformUtoD(0, dY, nRectU, nSpaceD)[1];
                } else {
                    iMinorY = iTickY - (int) (iTic * (oPos.dMinorTickDelta * oPos.dRangeUtoD));
                }
                if (iMinorY >= iY0 && iMinorY < iYE) {
                    if (tGridMinor) {
                        gGra.setColor(nMinorColor);
                        gGra.drawLine(iX0, iMinorY, iXE, iMinorY);
                        gGra.setColor(nNormColor);
                    }
                    gGra.drawLine(iX0, iMinorY, iX0 - iSmallTicksLen, iMinorY);
                }
                if (iMinorY < iY0) {
                    break;
                }
            }
        }
        boolean tGridMajor = GRID_MAJOR == iGridStyle || GRID_MAJOR_MINOR == iGridStyle;
        for (int iCnt = 0; iCnt < oPos.iMajorTickCount; iCnt++) {
            if (oPos.tLogAxis) {
                iTickY = oTrans.transformUtoD(0, exp10(oPos.dMinU + iCnt * oPos.dDeltaU), nRectU, nSpaceD)[1];
            } else {
                iTickY = iYE - (int) ((dRelativeY + (iCnt * oPos.dDeltaU)) * oPos.dRangeUtoD);
            }
            if (iTickY < iY0) {
                break;
            }
            if (tGridMajor && iTickY < iYE) {
                gGra.setColor(nMajorColor);
                gGra.drawLine(iX0, iTickY, iXE, iTickY);
                gGra.setColor(nNormColor);
            }
            gGra.drawLine(iX0, iTickY, iX0 - iBigTicksLen, iTickY);
        }
        if (Constants.ANTIALIAS) {
            gGra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        int iStrX = 0, iStrY, iStrWid;
        int iMinStrY, iMaxStrY;
        String sVal;
        if (tVerticalLabels) {
            final int iBorderLeft = getBorderSpaceLeft(gGra);
            iStrX = iX0 - iBorderLeft + 2;
            iMinStrY = nRectD.y;
            iMaxStrY = nRectD.y + nRectD.height - 2;
        } else {
            iMinStrY = nRectD.y + iFontHei;
            iMaxStrY = nRectD.y + nRectD.height;
        }
        for (int iCnt = 0; iCnt < oPos.iMajorTickCount; iCnt++) {
            if (oPos.tLogAxis) {
                dY = exp10(oPos.dMinU + iCnt * oPos.dDeltaU);
                iTickY = oTrans.transformUtoD(0, dY, nRectU, nSpaceD)[1];
            } else {
                dY = oPos.dMinU + (iCnt * oPos.dDeltaU);
                iTickY = iYE - (int) ((dY - nRectU.y) * oPos.dRangeUtoD);
            }
            if (iTickY < iY0) {
                break;
            }
            if (!oPos.tLogAxis && oPos.tNonLinearTransformer) {
                dY = oTrans.transformDtoU(0, iTickY, nSpaceD, nRectU)[1];
            }
            if (oPos.tLogAxis) {
                sVal = getAxisLabelLogarithmic((int) (oPos.dMinU + iCnt * oPos.dDeltaU));
            } else {
                sVal = getAxisLabel(dY, nRectU.height);
            }
            iStrWid = oMet.stringWidth(sVal);
            if (tVerticalLabels) {
                iStrY = iTickY - iStrWid / 2;
            } else {
                iStrX = iX0 - iBigTicksLen - 2 - iStrWid;
                iStrY = iTickY + iFontHeiHalf;
            }
            if (iStrY < iMinStrY) {
                break;
            }
            if (tVerticalLabels) {
                if (iStrY + iStrWid <= iMaxStrY) {
                    drawVerticalString(gGra, sVal, iStrX, iStrY);
                    iMaxStrY = iStrY - 2;
                }
            } else {
                if (iStrY <= iMaxStrY) {
                    gGra.drawString(sVal, iStrX, iStrY);
                    iMaxStrY = iStrY - oPos.iMinPixelBetweenTicks;
                }
            }
        }
    }

    /**
   * Handles all draw operations for the axis.
   */
    protected Rectangle drawVisibleAxis(Graphics2D gGra, Rectangle nRectD, Rectangle2D.Double nRectU, Transformer oTrans) {
        int iBorderX = getBorderSpaceLeft(gGra);
        int iBorderY = getBorderSpaceDown(gGra);
        if (this.tDrawXAxis) {
            drawX(gGra, iBorderX, iBorderY, nRectD, nRectU, oTrans);
        }
        if (this.tDrawYAxis) {
            drawY(gGra, iBorderX, iBorderY, nRectD, nRectU, oTrans);
        }
        Rectangle nNewRectD = new Rectangle(nRectD);
        nNewRectD.height -= iBorderY;
        nNewRectD.x += iBorderX;
        nNewRectD.width -= iBorderX;
        return nNewRectD;
    }

    /**
   * Draws on the given area.<p>
   * NOTE: Returns the space which is left after this one has claimed the space
   * it needs.
   * A normal IDrawable would return the given rectangle.
   * Note to developers: ALWAYS return a COPY of given nRectD, otherwise Graph.render()
   * will not work properly since in the normal case all IDrawable's share the same
   * nRectD object.
   *
   * @param gGra java.awt.Graphics2D
   * @param nRectD Device space.
   * @param nRectU User space.
   * @param oTrans Transformer. Used to decide on LINEAR or LOGARITHMIC style.
   */
    public java.awt.Rectangle draw(java.awt.Graphics2D gGra, java.awt.Rectangle nRectD, java.awt.geom.Rectangle2D.Double nRectU, Transformer oTrans) {
        if (null == nRectD || null == gGra || null == nRectU) {
            return nRectD;
        }
        if (Constants.ANTIALIAS) {
            gGra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        gGra.setColor(Color.black);
        return drawVisibleAxis(gGra, nRectD, nRectU, oTrans);
    }

    /**
   * Forces fixed axis to collaborate.
   * Used e.g. for drawing axis in overlay modus.
   * WARNING: some fields in aoPartnerDraws may be null if no collaborating partner exists.
   *
   * @param gGra The graphics object to paint on.
   * @param nRectD The device (pixel) space available for paint.
   * @param nRectU The user space that has to be painted.
   * @param oTrans For linear (standard) or logarithmic display.
   * @param aoPartnerDraws The partners for the mass drawing.
   * @param aoPartnerGraphs The graphs of the partners (user space, transformer).
   */
    public Rectangle massDraw(Graphics2D gGra, Rectangle nRectD, Rectangle2D.Double nRectU, Transformer oTrans, IFixedDrawable[] aoPartnerDraws, Graph[] aoPartnerGraphs) {
        if (null == nRectD || null == gGra || null == nRectU) {
            return nRectD;
        }
        gGra.setColor(Color.black);
        int iPartnerCount = aoPartnerDraws.length;
        int iBorderX = getBorderSpaceLeft(gGra);
        int iBorderY = getBorderSpaceDown(gGra);
        final int iAdditionalVerticalSpaceBetweenXAxis = 4;
        final int iAdditionalVerticalSpaceBetweenYAxis = iAdditionalVerticalSpaceBetweenXAxis;
        if (Constants.ANTIALIAS) {
            gGra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        Axis oAxis;
        int iXPos = 0;
        int iYPos = 0;
        for (int iPar = iPartnerCount - 1; iPar >= 0; iPar--) {
            if (null != aoPartnerDraws[iPar]) {
                oAxis = (Axis) aoPartnerDraws[iPar];
                if (oAxis.isYAxisVisible()) {
                    iXPos += iBorderX + iAdditionalVerticalSpaceBetweenYAxis;
                }
                if (oAxis.isXAxisVisible()) {
                    iYPos += iBorderY + iAdditionalVerticalSpaceBetweenXAxis;
                }
            }
        }
        Rectangle nNewRectD = new Rectangle(nRectD);
        nNewRectD.x += iXPos;
        nNewRectD.width -= iXPos;
        nNewRectD.height -= iYPos;
        Rectangle nResRectD = draw(gGra, nNewRectD, nRectU, oTrans);
        Rectangle nParRectD = new Rectangle(nNewRectD);
        int iOldGridStyle;
        for (int iPar = iPartnerCount - 1; iPar >= 0; iPar--) {
            if (null != aoPartnerDraws[iPar] && ((Axis) aoPartnerDraws[iPar]).isYAxisVisible()) {
                oAxis = (Axis) aoPartnerDraws[iPar];
                iOldGridStyle = oAxis.iGridStyle;
                oAxis.iGridStyle = GRID_NONE;
                nParRectD.x -= iBorderX + iAdditionalVerticalSpaceBetweenYAxis;
                nParRectD.height += iBorderY + iAdditionalVerticalSpaceBetweenXAxis;
                iXPos = nParRectD.x;
                nParRectD.x = nNewRectD.x;
                oAxis.drawX(gGra, iBorderX, iBorderY, nParRectD, aoPartnerGraphs[iPar].getSpaceU(), aoPartnerGraphs[iPar].getTransformer());
                nParRectD.x = iXPos;
                iYPos = nParRectD.height;
                nParRectD.height = nNewRectD.height;
                oAxis.drawY(gGra, iBorderX, iBorderY, nParRectD, aoPartnerGraphs[iPar].getSpaceU(), aoPartnerGraphs[iPar].getTransformer());
                nParRectD.height = iYPos;
                oAxis.iGridStyle = iOldGridStyle;
            }
        }
        return nResRectD;
    }
}
