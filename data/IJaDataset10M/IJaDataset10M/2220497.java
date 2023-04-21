package client;

import java.util.TimerTask;
import javax.swing.JButton;

public class ActionButtonTask extends TimerTask {

    JButton btn;

    public ActionButtonTask(JButton b) {
        btn = b;
    }

    public void run() {
        btn.setBackground(null);
    }
}
