package jpicedt.format.output.pstricks;

import jpicedt.graphic.io.formatter.Formatter;
import jpicedt.graphic.io.formatter.AbstractFormatter;
import jpicedt.graphic.*;
import jpicedt.graphic.model.*;
import java.awt.*;
import java.awt.geom.*;
import static jpicedt.format.output.pstricks.PstricksConstants.*;
import static jpicedt.graphic.model.StyleConstants.*;
import static jpicedt.graphic.model.PicAttributeName.*;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan2;
import static java.lang.Math.abs;

/**
 * Formats a PicEllipse using PsTricks macros, including pstricks-add's \\psellipticarc if applicable.
 * [SR:pending] arrows for flat ellipse
 * @author Vincent Guirardel, Sylvain Reynal
 * @since jpicedt 1.4
 * @version $Id: PicEllipseFormatter.java,v 1.21 2011/11/17 20:33:13 vincentb1 Exp $
 *
 */
public class PicEllipseFormatter extends AbstractFormatter {

    private PicEllipse ellipse;

    private PstricksFormatter factory;

    /**
	* @since jPicEdt 1.6
	*/
    public Element getElement() {
        return ellipse;
    }

    public PicEllipseFormatter(PicEllipse ellipse, PstricksFormatter factory) {
        this.ellipse = ellipse;
        this.factory = factory;
    }

    private boolean isInsidePSCustom() {
        BranchElement parent = ellipse.getParent();
        if (parent == null) return false; else {
            BranchElement.CompoundMode cm = parent.getCompoundMode();
            if (cm == BranchElement.CompoundMode.JOINT) return true;
        }
        return false;
    }

    /**
	 * \\rput{rotation}(centerX,centerY){\\psellipse[param](0,0)(greatAxis/2,smallAxis/2)} => plain ellipse<br>
	 * \\rput{rotationAngle}(centerX,centerY){\qline(min,0)(max,0)} => flat ellipse
	 */
    public String format() {
        StringBuffer bufAngleCorrection = null;
        String angles = null;
        String correctedAngles = null;
        StringBuffer buf = new StringBuffer(100);
        PicAttributeSet set = ellipse.getAttributeSet();
        PstricksFormatter.ParameterString paramStr = factory.createParameterString(ellipse);
        if (paramStr.isDefinedColourString()) buf.append(paramStr.getUserDefinedColourBuffer());
        boolean isPSCustom = isInsidePSCustom();
        double rotAngle = toDegrees(ellipse.getRotationAngle());
        PicPoint ptCenter = ellipse.getCtrlPt(PicEllipse.P_CENTER, null);
        if (isPSCustom) {
            if (ptCenter.x != 0 || ptCenter.y != 0) {
                buf.append("\\translate");
                buf.append(ptCenter);
            }
            if (rotAngle != 0) {
                buf.append("\\rotate{");
                buf.append(PEToolKit.doubleToString(rotAngle));
                buf.append("}");
            }
        } else {
            buf.append("\\rput{");
            buf.append(PEToolKit.doubleToString(toDegrees(ellipse.getRotationAngle())));
            buf.append("}");
            buf.append(ellipse.getCtrlPt(PicEllipse.P_CENTER, null));
            buf.append("{");
        }
        if (ellipse.isPlain() && ellipse.getArcType() != Arc2D.CHORD) {
            buf.append("\\psellipse");
            buf.append("[");
            buf.append(paramStr.getParameterBuffer());
            buf.append("]");
            buf.append("(0,0)");
            buf.append("(");
            buf.append(PEToolKit.doubleToString(ellipse.getGreatAxisLength() / 2));
            buf.append(",");
            buf.append(PEToolKit.doubleToString(ellipse.getSmallAxisLength() / 2));
            buf.append(")");
        } else if (ellipse.isFlat()) {
            double start = ellipse.getRotatedAngleStart();
            double end = ellipse.getRotatedAngleEnd();
            double min = min(cos(toRadians(start)), cos(toRadians(end)));
            double max = max(cos(toRadians(start)), cos(toRadians(end)));
            if ((start < 0 && end > 0) || (end > 360)) max = 1;
            if (end > 180) min = -1;
            if (ellipse.getArcType() == Arc2D.PIE) {
                if (min > 0) min = 0;
                if (max < 0) max = 0;
            }
            min *= ellipse.getGreatAxisLength();
            max *= ellipse.getGreatAxisLength();
            buf.append("\\qline(");
            buf.append(PEToolKit.doubleToString(min));
            buf.append(",0)(");
            buf.append(PEToolKit.doubleToString(max));
            buf.append(",0)");
        } else {
            if (ellipse.getArcType() == Arc2D.CHORD) {
                buf.append("\\pscustom");
                buf.append("[");
                buf.append(paramStr.getParameterBuffer());
                buf.append("]");
                buf.append("{\\psellipticarc");
            } else {
                if (ellipse.getArcType() == Arc2D.PIE) buf.append("\\psellipticwedge"); else buf.append("\\psellipticarc");
                buf.append("[");
                buf.append(paramStr.getParameterBuffer());
                buf.append("]");
                if (ellipse.getArcType() == Arc2D.OPEN) buf.append(PstricksUtilities.createPstricksStringFromArrows(ellipse));
            }
            buf.append("(0,0)");
            buf.append("(");
            buf.append(PEToolKit.doubleToString(0.5 * ellipse.getGreatAxisLength()));
            buf.append(",");
            buf.append(PEToolKit.doubleToString(0.5 * ellipse.getSmallAxisLength()));
            buf.append(")");
            buf.append("{");
            double start = ellipse.getRotatedAngleStart();
            double end = ellipse.getRotatedAngleEnd();
            if (start >= 360 || end >= 360) {
                start -= 360;
                end -= 360;
            }
            double correctedStart, correctedEnd;
            if (factory.getCustomProperties().getAngleCorrection() == PstricksAngleCorrection.ANGLE_CORRECTION_BY_JPICEDT || factory.getCustomProperties().getAngleCorrection() == PstricksAngleCorrection.ANGLE_CORRECTION_BY_JPICEDT_AUTO) {
                double ga = ellipse.getGreatAxisLength();
                double ssa = ellipse.getSmallAxisLength();
                double sa = abs(ssa);
                correctedStart = toDegrees(atan2(sa * sin(toRadians(start)), ga * cos(toRadians(start))));
                correctedEnd = toDegrees(atan2(sa * sin(toRadians(end)), ga * cos(toRadians(end))));
                if (ssa < 0) {
                    correctedStart = -(180 + correctedStart);
                    correctedEnd = -(180 + correctedEnd);
                    if (correctedStart <= -360 || correctedEnd <= -360) {
                        correctedStart += 360;
                        correctedEnd += 360;
                    }
                }
                if (factory.getCustomProperties().getAngleCorrection() == PstricksAngleCorrection.ANGLE_CORRECTION_BY_JPICEDT) {
                    start = correctedStart;
                    end = correctedEnd;
                } else {
                    buf.append("#1}{#2}");
                    bufAngleCorrection = new StringBuffer(50);
                    bufAngleCorrection.append(PEToolKit.doubleToString(start));
                    bufAngleCorrection.append("}{");
                    bufAngleCorrection.append(PEToolKit.doubleToString(end));
                    angles = bufAngleCorrection.toString();
                    bufAngleCorrection = new StringBuffer(50);
                    bufAngleCorrection.append(PEToolKit.doubleToString(correctedStart));
                    bufAngleCorrection.append("}{");
                    bufAngleCorrection.append(PEToolKit.doubleToString(correctedEnd));
                    correctedAngles = bufAngleCorrection.toString();
                    bufAngleCorrection = new StringBuffer(buf.length() + 50);
                    bufAngleCorrection.append("\\makeatletter\\def\\@tempa#1#2{%");
                    bufAngleCorrection.append(CR_LF);
                }
            }
            if (bufAngleCorrection == null) {
                buf.append(PEToolKit.doubleToString(start));
                buf.append("}{");
                buf.append(PEToolKit.doubleToString(end));
                buf.append("}");
            }
            if (ellipse.getArcType() == Arc2D.CHORD) buf.append("\\closepath}");
        }
        if (isPSCustom) {
            if (rotAngle != 0) {
                buf.append("\\rotate{");
                buf.append(PEToolKit.doubleToString(-rotAngle));
                buf.append("}");
            }
            if (ptCenter.x != 0 || ptCenter.y != 0) {
                buf.append("\\translate");
                buf.append(ptCenter.scale(0.0, 0.0, -1.0));
            }
        } else {
            buf.append("}");
        }
        buf.append(CR_LF);
        if (bufAngleCorrection != null) {
            bufAngleCorrection.append(buf);
            bufAngleCorrection.append("}\\@ifundefined{Pst@correctAnglefalse}{\\@tempa{");
            bufAngleCorrection.append(angles);
            bufAngleCorrection.append("}}{\\@tempa{");
            bufAngleCorrection.append(correctedAngles);
            bufAngleCorrection.append("}}\\makeatother");
            bufAngleCorrection.append(CR_LF);
            buf = bufAngleCorrection;
        }
        return buf.toString();
    }
}
