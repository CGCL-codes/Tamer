package home.jes.ui.jazz.converters;

import home.jes.ui.jazz.Converter;
import home.jes.ui.jswing.*;
import org.jdom.Attribute;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * The <code>LayoutConverter</code> class defines a converter that creates LayoutManager objects
 * based on a provided String.
 * <h3>Examples for Valid XML attribute notations:</h3>
 * <pre>
 * <ul>
 * <li>layout="flowlayout"</li>
 * <li>layout="borderlayout"</li>
 * </ul>
 * </pre>
 *
 * @see java.awt.LayoutManager
 * @see home.jes.ui.jazz.ConverterLibrary
 */
public class LayoutConverter implements Converter {

    public static final String FLOW_LAYOUT = "flowlayout";

    public static final String BORDER_LAYOUT = "borderlayout";

    public static final String GRID_LAYOUT = "gridlayout";

    public static final String GRID2_LAYOUT = "gridlayout2";

    public static final String GRIDBAG_LAYOUT = "gridbaglayout";

    public static final String STACK_LAYOUT = "stacklayout";

    public static final String STACK2_LAYOUT = "stacklayout2";

    public static final String COMPASS_LAYOUT = "compasslayout";

    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type <code>Class</code> not used
     * @param attr <code>Attribute</code> value is needed for conversion
     * @return <code>Object</code>
     */
    public Object convert(final Class type, final Attribute attr) {
        LayoutManager lm = null;
        StringTokenizer st = new StringTokenizer(attr.getValue(), "(,)");
        String s = st.nextToken().trim();
        if (s != null) {
            s = s.toLowerCase();
            if (s.equals(LayoutConverter.FLOW_LAYOUT)) {
                int[] para = Util.ia(st);
                int l = para.length;
                if (2 == para.length) lm = new FlowLayout(FlowLayout.CENTER, para[0], para[1]); else if (para.length == 1) lm = new FlowLayout(FlowLayout.CENTER, para[0], para[0]); else lm = new FlowLayout();
            } else if (s.equals(LayoutConverter.BORDER_LAYOUT)) {
                int[] para = Util.ia(st);
                int l = para.length;
                if (2 <= para.length) lm = new BorderLayout(para[0], para[1]); else lm = new BorderLayout();
            } else if (s.equals(LayoutConverter.GRID_LAYOUT)) {
                int[] para = Util.ia(st);
                int l = para.length;
                if (4 <= para.length) lm = new GridLayout(para[0], para[1], para[2], para[3]); else if (2 <= para.length) lm = new GridLayout(para[0], para[1]); else lm = new GridLayout();
            } else if (s.equals(LayoutConverter.GRID2_LAYOUT)) {
                int[] para = Util.ia(st);
                int l = para.length;
                if (4 <= para.length) lm = new GridLayout2(para[0], para[1], para[2], para[3]); else if (2 <= para.length) lm = new GridLayout2(para[0], para[1]); else lm = new GridLayout2();
            } else if (s.equals(LayoutConverter.GRIDBAG_LAYOUT)) {
                lm = new GridBagLayout();
            } else if (s.equals(STACK_LAYOUT)) {
                int gap = 0;
                int direction = StackLayout.VERTICAL;
                String defaultConstraints = null;
                String[] para = Util.sa(st);
                if (para.length > 0) {
                    gap = Integer.parseInt(para[0]);
                }
                if (para.length >= 2) {
                    direction = (para[1].charAt(0) == 'v' ? StackLayout.VERTICAL : StackLayout.HORIZONTAL);
                }
                if (para.length == 3) {
                    defaultConstraints = para[2];
                }
                lm = new StackLayout(gap, direction, defaultConstraints);
            } else if (s.equals(STACK2_LAYOUT)) {
                int[] para = Util.ia(st);
                if (1 <= para.length) lm = new StackLayout2(para[0]); else lm = new StackLayout2();
            } else if (s.equals(COMPASS_LAYOUT)) {
                int[] para = Util.ia(st);
                if (2 <= para.length) lm = new CompassLayout(para[0], para[1]); else lm = new CompassLayout();
            } else if (s.equals("cardlayout")) lm = new CardLayout(); else {
                System.err.println("Unknown layout: " + s);
            }
        }
        return lm;
    }
}
