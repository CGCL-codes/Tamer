package com.dukesoftware.utils.swing.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class OKCancelListenerExtendsWindowAdapter extends WindowAdapter implements ActionListener {

    public final void actionPerformed(ActionEvent e) {
        JButton but = (JButton) e.getSource();
        if (but.getText() == OKCancelListener.OK) {
            ok();
        } else {
            cancel();
        }
    }

    public abstract void cancel();

    public abstract void ok();

    /**
	 * @param butAcl
	 * @return
	 */
    public JPanel createOKCancelPanel() {
        return OKCancelListener.createOKCancelPanel(this);
    }
}
