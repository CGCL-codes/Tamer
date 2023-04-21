package tmp;

import java.awt.*;
import javax.swing.*;

public class TestMaxJFrame extends JFrame {

    public TestMaxJFrame() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        TestMaxJFrame t = new TestMaxJFrame();
        t.setVisible(true);
    }
}
