package medimagesuite.workspace2d.openfrombd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import medimagesuite.plugins.core.util.CenteredFrame;

public class CloseFrame implements ActionListener {

    private CenteredFrame frame;

    public CloseFrame(CenteredFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        this.frame.close();
    }
}
