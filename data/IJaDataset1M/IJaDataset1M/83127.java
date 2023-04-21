package jscicalc.pobject;

import jscicalc.OObject;

/**
 * Inverse operation.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class Inverse extends LFunction {

    /**
     * Constructor. Sets a tooltip and shortcut.
     */
    public Inverse() {
        ftooltip = "the inverse of <i>x</i>, 1/<i>x</i>";
        fshortcut = 'i';
    }

    /**
     * Calculate inverse of x.
     * @param x The value (left of symbol)
     * @return The result of the operation
     */
    public double function(double x) {
        return 1 / x;
    }

    /**
     * Calculate inverse of x.
     * @param x The value (left of symbol)
     * @return The result of the operation
     */
    public OObject function(OObject x) {
        return x.inverse();
    }

    public String shortName() {
        return "<i>x</i><sup>&#8722;1</sup>";
    }

    public String[] name_array() {
        return fname;
    }

    public static void main(String args[]) {
        PObject o = new Inverse();
        StringBuilder s = new StringBuilder("<html>");
        s.append(o.name());
        s.append("</html>");
        javax.swing.JOptionPane.showMessageDialog(null, s.toString());
    }

    private static final String[] fname = { "<sup>&#8722;</sup>", "<sup>1</sup>" };
}
