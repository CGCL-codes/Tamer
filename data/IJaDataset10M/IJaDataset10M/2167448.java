package api.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Class GenericFrame.java
 * @description Abstract frame for the application
 * @author Sébastien Faure  <sebastien.faure3@gmail.com>
 * @version 2011-07-18
 */
public abstract class GenericFrame extends JFrame implements ActionListener, WindowListener, KeyListener {

    protected int x = 0;

    protected int y = 0;

    /**
     * Constructor
     */
    protected GenericFrame() {
        this("Untitled");
    }

    /**
     * Constructor
     * @param title
     */
    protected GenericFrame(String title) {
        this.setIconImage(new ImageIcon("media/des.gif").getImage());
        this.setTitle(title);
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.addWindowListener(this);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = screen.width;
        y = screen.height;
    }

    /**
     * Display the frame, at center of screen
     * @param quest
     */
    public void showTheFrame(String quest) {
        this.setLocation((x - this.getSize().width) / 2, (y - this.getSize().height) / 2);
        this.setVisible(true);
    }

    /**
     * Hide the frame
     */
    public void hideTheFrame() {
        this.setVisible(false);
    }
}
