package jpicedt.graphic.io.formatter;

import jpicedt.graphic.*;
import jpicedt.graphic.model.*;
import java.awt.*;
import static jpicedt.graphic.io.formatter.JPICConstants.*;

/**
 * JPIC-XML formatter for PicParallelogram objects.
 * JPIC-XML syntax :
 * <parallelogram p1="(x1,y1)" p2="(x2,y2)" p3="(x3,y3)" other-attribs />
 *
 * @since jpicedt 1.4
 * @author Sylvain Reynal
 * @version $Id: JPICParallelogramFormatter.java,v 1.9 2011/07/23 05:24:58 vincentb1 Exp $ 
 *
 */
public class JPICParallelogramFormatter extends AbstractFormatter {

    /** the Element this formatter acts upon */
    private PicParallelogram obj;

    /** the factory that produced this formatter */
    private JPICFormatter factory;

    public Element getElement() {
        return obj;
    }

    /**
	 * @param obj the PicParallelogram to be formatted
	 * @param factory the factory that produced this formatter
	 */
    public JPICParallelogramFormatter(PicParallelogram obj, JPICFormatter factory) {
        this.obj = obj;
        this.factory = factory;
    }

    /**
	 * PsTricks formating algorithm
	 * ex :     \psframe[framearc=0.5,linecolor=red,linewidth=0.2,fillstyle=solid,fillcolor=blue](2,2)(5,7)
	 * @return a String representing this Element in the PsTricks 
	 */
    public String format() {
        StringBuffer buf = new StringBuffer(100);
        buf.append("<parallelogram");
        XmlAttributeSet map = new XmlAttributeSet();
        map.putNameValuePair("p1", obj.getCtrlPt(PicParallelogram.P_BL, null));
        map.putNameValuePair("p2", obj.getCtrlPt(PicParallelogram.P_BR, null));
        map.putNameValuePair("p3", obj.getCtrlPt(PicParallelogram.P_TR, null));
        map.putCommonAttributes(obj);
        buf.append(map.toXML());
        buf.append(" />");
        buf.append(CR_LF);
        return buf.toString();
    }
}
