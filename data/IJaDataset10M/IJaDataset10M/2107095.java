package uk.org.sgj.OHCApparatus.AutoFill;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import uk.org.sgj.OHCApparatus.Records.OHCBasicPanel;

/**
 *
 * @author Steffen
 */
public class AbbreviationAutoFill extends PairAutoFill {

    public AbbreviationAutoFill(String abbrFieldName, String extensionFieldName) {
        super(extensionFieldName, abbrFieldName);
    }

    public final void actionPerformed(ActionEvent e) {
        ac = new AbbreviationsChooseDialog(basicPanel, firstFieldName, secondFieldName);
        ac.setVisible(true);
    }
}
