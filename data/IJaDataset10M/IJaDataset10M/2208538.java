package synology.ui;

import javax.swing.JFrame;
import synology.SynoController;

public class SynoFrame extends JFrame {

    private SynoController controller;

    public SynoFrame() {
        controller = new SynoController();
    }

    public SynoController getController() {
        return controller;
    }
}
