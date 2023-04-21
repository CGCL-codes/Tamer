package soccer.client.action;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import soccer.client.Replayer;
import soccer.client.SoccerMaster;

public class PlayBackLogPlayAction extends AbstractClientAction {

    public PlayBackLogPlayAction() {
        super();
        putValue(NAME, "Play Back the log file");
        URL imgURL = SoccerMaster.class.getResource("/imag/vback.gif");
        ImageIcon icon = new ImageIcon(imgURL);
        putValue(SMALL_ICON, icon);
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (getSoccerMaster().replayer != null) getSoccerMaster().replayer.setStatus(Replayer.BACK);
    }
}
