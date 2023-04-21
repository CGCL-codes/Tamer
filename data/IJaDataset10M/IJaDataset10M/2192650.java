package jscicalc.pobject;

import jscicalc.AngleType;
import jscicalc.OObject;
import jscicalc.complex.Complex;

/**
 * Inverse tangent operation.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class ATan extends Trig {

    /**
     * Constructor. Sets a tooltip and shortcut.
     */
    public ATan(AngleType angleType) {
        super(angleType);
        ftooltip = "inverse tangent function";
        fshortcut = 't';
    }

    /**
     * Inverse tangent of x.
     * @param x The value (right of symbol)
     * @return The result of the operation
     */
    public double function(double x) {
        return Math.atan(x) * iscale;
    }

    /**
     * Inverse tangent of x.
     * @param x The value (right of symbol)
     * @return The result of the operation
     */
    public OObject function(OObject x) {
        if (x instanceof Complex) {
            Complex c = (Complex) x;
            if (scale != 1 && StrictMath.abs(c.imaginary()) > 1e-6) throw new RuntimeException("Error");
            return c.atan().scale(iscale);
        } else {
            return x.atan(angleType);
        }
    }

    public String[] name_array() {
        return fname;
    }

    public static void main(String args[]) {
        PObject o = new ATan(AngleType.DEGREES);
        StringBuilder s = new StringBuilder("<html>");
        s.append(o.name());
        s.append("</html>");
        javax.swing.JOptionPane.showMessageDialog(null, s.toString());
    }

    private static final String[] fname = { "t", "a", "n", "<sup>&#8722;</sup>", "<sup>1</sup>", " " };
}
