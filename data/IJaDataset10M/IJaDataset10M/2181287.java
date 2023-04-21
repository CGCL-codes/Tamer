package nhap.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import nhap.swing.NethackState;

public class PlayAction implements ActionListener {

    private final NethackState nethackState;

    public PlayAction(NethackState nethackState) {
        this.nethackState = nethackState;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                nethackState.setPlaying(true);
            }
        });
    }
}
