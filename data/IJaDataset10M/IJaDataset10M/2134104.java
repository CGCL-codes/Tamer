package apollo.gui.detailviewers.sequencealigner.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import apollo.gui.detailviewers.sequencealigner.MultiSequenceAlignerPanel;
import apollo.gui.detailviewers.sequencealigner.filters.Filter;
import apollo.gui.detailviewers.sequencealigner.menus.AddFilterPanel;
import apollo.gui.detailviewers.sequencealigner.menus.AddFilterPanel.TYPES;

public class AddFilterAction extends AbstractAction {

    private MultiSequenceAlignerPanel alignerPanel;

    private AddFilterPanel.TYPES type;

    public AddFilterAction(MultiSequenceAlignerPanel msap, AddFilterPanel.TYPES t) {
        super("Filter " + t);
        alignerPanel = msap;
        type = t;
    }

    public void actionPerformed(ActionEvent arg0) {
        AddFilterPanel p = new AddFilterPanel(alignerPanel, type);
        JDialog d = p.createDialog();
        d.show();
        Object type = p.getInputValue();
        if (type == JOptionPane.UNINITIALIZED_VALUE) {
            type = null;
        }
        if (type != null) {
            alignerPanel.getResultFilter().add((Filter) type);
            alignerPanel.reformatResultPanel();
            alignerPanel.getResultPanel().repaint();
            alignerPanel.getOverview().updateState();
            alignerPanel.getOverview().repaint();
        }
    }
}
