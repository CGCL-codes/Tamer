package jscicalc.pobject;

import jscicalc.AngleType;
import jscicalc.OObject;
import jscicalc.complex.Complex;

/**
 * Sine operation.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class Sin extends Trig {

    /**
     * Constructor. Sets a tooltip and shortcut.
     */
    public Sin(AngleType angleType) {
        super(angleType);
        ftooltip = "sine function";
        fshortcut = 's';
    }

    /**
     * Sine of x.
     * @param x The value (right of symbol)
     * @return The result of the operation
     */
    public double function(double x) {
        return Math.sin(x * scale);
    }

    /**
     * Sine of x.
     * @param x The value (right of symbol)
     * @return The result of the operation
     */
    public OObject function(OObject x) {
        if (x instanceof Complex) {
            Complex c = (Complex) x;
            if (scale != 1 && Math.abs(c.imaginary()) > 1e-6) throw new RuntimeException("Error");
            return c.scale(scale).sin();
        } else {
            return x.sin(angleType);
        }
    }

    public String[] name_array() {
        return fname;
    }

    public static void main(String args[]) {
        PObject o = new Sin(AngleType.DEGREES);
        StringBuilder s = new StringBuilder("<html>");
        s.append(o.name());
        s.append("</html>");
        javax.swing.JOptionPane.showMessageDialog(null, s.toString());
    }

    private static final String[] fname = { "s", "i", "n", " " };
}
