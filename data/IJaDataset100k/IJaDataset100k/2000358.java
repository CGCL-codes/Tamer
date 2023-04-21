package jpianotrain.gui.action;

import static jpianotrain.util.ResourceKeys.ACTION_CHORD_NAME;
import static jpianotrain.util.ResourceKeys.ACTION_CIRCLE_OF_FIFTHS_NAME;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import jpianotrain.ApplicationContext;
import jpianotrain.gui.ChordPane;
import jpianotrain.util.ResourceFactory;

/**
 * 
 * @author onkobu
 * @since 0.0.3
 */
public class ChordAction extends AbstractAction {

    public ChordAction() {
        super(ACTION_CHORD_NAME);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dlg == null) {
            dlg = new JDialog(ApplicationContext.getInstance().getDefaultDialogOwner(), ResourceFactory.getString(ACTION_CHORD_NAME), false);
            dlg.setLayout(new BorderLayout());
            dlg.add(new ChordPane(), BorderLayout.CENTER);
            dlg.pack();
        }
        if (dlg.isVisible()) {
            dlg.toFront();
        } else {
            dlg.setVisible(true);
        }
    }

    private JDialog dlg;
}
