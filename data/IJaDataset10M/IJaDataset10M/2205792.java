package soccer.client.action;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import soccer.client.SoccerMaster;

public class SetUpServerAction extends AbstractClientAction {

    public SetUpServerAction() {
        super();
        putValue(NAME, "Set Up Local Server...");
        URL imgURL = SoccerMaster.class.getResource("/imag/field.gif");
        ImageIcon icon = new ImageIcon(imgURL);
        putValue(SMALL_ICON, icon);
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent e) {
        getSoccerMaster().getDialogManager().getServerDialog().display();
    }
}
