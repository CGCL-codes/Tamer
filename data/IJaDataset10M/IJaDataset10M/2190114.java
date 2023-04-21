package EDU.Washington.grad.noth.cda.app;

import EDU.Washington.grad.noth.cda.ui.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainComponentListener extends ComponentAdapter {

    MainPanel mainPanel;

    public MainComponentListener(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void componentResized(ComponentEvent e) {
        mainPanel.dispatchEvent(e);
    }
}
