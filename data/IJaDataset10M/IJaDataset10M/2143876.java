package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

/**
 * MainWindow.java
 * -----------------------------
 * @author Bertrand "fof" DAVID
 * 2008 scc-experimental
 * 4 août 08 15:33:08
 */
public class MainWindow extends JFrame {

    private JPanUsersList jpanuserslist = new JPanUsersList();

    public MainWindow() {
        this.setVisible(true);
        this.setTitle("SunnyChat - client version");
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        this.getContentPane().add(jpanuserslist, BorderLayout.EAST);
    }
}
