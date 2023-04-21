package Controllers.Reporter;

import Messages.InputBox;
import Messages.OKMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author nrovinskiy
 */
public class NewGroupListener implements ActionListener {

    public static final int DEFAULT_GROUP_LIMIT = 7;

    ReporterController rcHost;

    public NewGroupListener(ReporterController host) {
        rcHost = host;
    }

    public void actionPerformed(ActionEvent e) {
        if (rcHost.jcbGroups.getItemCount() >= 7) {
            OKMessage okm = new OKMessage((JFrame) rcHost.getOwner(), "You can not have more then seven groups!");
            okm.setVisible(true);
            okm = null;
            return;
        }
        InputBox ipGroup = new InputBox((JFrame) rcHost.getOwner(), "Enter the name of new group");
        ipGroup.setVisible(true);
        if ((ipGroup.getResult() != null) && (ipGroup.getResult().trim().length() != 0)) {
            rcHost.jcbGroups.addItem(ipGroup.getResult());
            rcHost.jcbGroups.setSelectedItem(ipGroup.getResult());
            rcHost.LoadReasons(ipGroup.getResult());
        }
        ipGroup = null;
    }
}
