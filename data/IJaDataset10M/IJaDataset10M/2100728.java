package stms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;

/**
 *
 * @author Administrator
 */
public class AboutDialog extends JFrame {

    public AboutDialog() {
        super();
        setTitle("关于");
        this.setLayout(new GridLayout(4, 1));
        add(new JLabel("author: fanjun"));
        add(new JLabel("date: 8-17-2009"));
        add(new JLabel("platform: Netbeans IDE 6.5.1"));
        add(new JLabel("Java Swing"));
    }
}
