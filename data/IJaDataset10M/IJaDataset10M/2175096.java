package org.fao.gast.gui.panels.manag.conversion;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import org.dlib.gui.GuiUtil;
import org.dlib.gui.ProgressDialog;
import org.fao.gast.gui.panels.FormPanel;
import org.fao.gast.localization.Messages;

public class MainPanel extends FormPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5093359964802607711L;

    public MainPanel() {
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("convert")) convert();
    }

    private void convert() {
        Frame owner = GuiUtil.getFrame(this);
        ProgressDialog dialog = new ProgressDialog(owner, Messages.getString("MainPanel.convertingData"));
        Worker worker = new Worker(dialog);
        dialog.run(worker);
    }

    protected JComponent buildInnerPanel() {
        return null;
    }
}
