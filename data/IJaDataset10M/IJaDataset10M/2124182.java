package gr.frame.actions;

import gr.frame.MainFrame;
import gr.frame.help.info.InfoJDialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class ShowInfoDialogAction extends AbstractAction {

    private static final String INFO_TITLE = "Info";

    public ShowInfoDialogAction(String name) {
        super(name);
    }

    public void actionPerformed(ActionEvent e) {
        InfoJDialog info = new InfoJDialog(MainFrame.getInstance(), INFO_TITLE);
        info.setVisible(true);
    }
}
