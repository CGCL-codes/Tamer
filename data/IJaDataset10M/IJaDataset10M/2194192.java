package net.jankenpoi.sudokuki.ui.swing;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TranslateAction extends AbstractAction {

    private final JFrame frame;

    public TranslateAction(JFrame frame) {
        this.frame = frame;
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TranslateDialog dlg = new TranslateDialog(frame);
        dlg.setVisible(true);
    }
}
