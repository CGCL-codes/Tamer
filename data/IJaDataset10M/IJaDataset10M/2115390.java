package iceGUI;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class DisplayWindow extends JFrame {

    private Container c;

    private int xDim = 500;

    private int yDim = 400;

    public DisplayWindow(String title, int x, int y) {
        super(title);
        xDim = x;
        yDim = y;
        c = this.getContentPane();
    }

    public DisplayWindow(String title) {
        super(title);
        c = this.getContentPane();
    }

    public DisplayWindow() {
        super("Display Window");
        c = this.getContentPane();
    }

    public void addPanel(JPanel p) {
        p.setPreferredSize(new Dimension(xDim, yDim));
        c.add(p);
    }

    public void showFrame() {
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
