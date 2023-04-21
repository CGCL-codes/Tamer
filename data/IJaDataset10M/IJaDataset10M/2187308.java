package jscicalc.pobject;

/**
 * Change base to binary.
 * @see BinButton
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class Bin extends PObject {

    /**
     * Constructor.
     */
    public Bin() {
        ftooltip = "Change base to binary";
        fshortcut = 'B';
    }

    public String[] name_array() {
        return fname;
    }

    public static void main(String args[]) {
        Equals o = new Equals();
        javax.swing.JOptionPane.showMessageDialog(null, o.name());
    }

    private static final String[] fname = { "B", "i", "n" };
}
