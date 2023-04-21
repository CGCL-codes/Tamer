package uk.org.sgj.OHCApparatus.AutoFill;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import uk.org.sgj.OHCApparatus.Records.OHCBasicPanel;

/**
 *
 * @author Steffen
 */
public class NullAutoFill implements AutoFill {

    /** Creates a new instance of NullAutoFill */
    public NullAutoFill() {
    }

    public JButton getButton(OHCBasicPanel obp) {
        JButton duff = new JButton();
        duff.setEnabled(false);
        return (duff);
    }

    public void setEditable(boolean editable) {
    }
}
