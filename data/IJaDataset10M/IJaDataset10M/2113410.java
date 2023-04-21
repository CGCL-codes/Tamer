package jscicalc.pobject;

/**
 * Information for Pol (polar complex notation) button.
 * @see PolButton
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class Pol extends PObject {

    public Pol() {
        ftooltip = "Change to/from (complex) polar notation";
        fshortcut = 'o';
    }

    public String[] name_array() {
        return fname;
    }

    public static void main(String args[]) {
        Pol o = new Pol();
        javax.swing.JOptionPane.showMessageDialog(null, o.name());
    }

    private static final String[] fname = { "P", "o", "l" };
}
