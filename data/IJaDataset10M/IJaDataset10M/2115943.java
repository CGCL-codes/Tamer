package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GUI {

    JFrame frame;

    public GUI() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(new MenuPanel(), BorderLayout.WEST);
        frame.setVisible(true);
        frame.pack();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
