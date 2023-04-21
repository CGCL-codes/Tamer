package ag.ion.bion.officelayer.draw.shapes.properties;

import java.awt.Color;
import ag.ion.bion.officelayer.draw.shapes.data.Arrow;

/**
 * @see <a href=
 *      "http://api.openoffice.org/docs/common/ref/com/sun/star/drawing/LineProperties.html">
 *      OpenOffice documentation </a>
 * 
 * @author Sebastian Patschorke
 * 
 */
public interface ILineProperties {

    public static final String STYLE = "LineStyle";

    public static final String DASH = "LineDash";

    public static final String COLOR = "LineColor";

    public static final String TRANSPARENCE = "LineTransparence";

    public static final String WIDTH = "LineWidth";

    public static final String JOINT = "LineJoint";

    public static final String START_NAME = "LineStartName";

    public static final String START = "LineStart";

    public static final String END_NAME = "LineEndName";

    public static final String END = "LineEnd";

    public static final String START_CENTER = "LineStartCenter";

    public static final String START_WIDTH = "LineStartWidth";

    public static final String END_CENTER = "LineEndCenter";

    public static final String END_WIDTH = "LineEndWidth";

    public void setLineColor(Color color);

    public void setLineWidth(long width);

    public void setStartArrow(String name);

    public void setStartArrow(Arrow arrow);

    public void setEndArrow(String name);

    public void setEndArrow(Arrow arrow);

    public void setStartArrowWidth(int width);

    public void setEndArrowWidth(int width);
}
