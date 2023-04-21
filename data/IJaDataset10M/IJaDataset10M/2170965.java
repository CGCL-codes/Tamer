package hm.core.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * TODO: verbessern
 */
public class ProgressDialog extends JDialog {

    private JLabel north;

    private JLabel south;

    private JProgressBar prog;

    public ProgressDialog(JFrame parent, String title, String text, int progressValue) {
        north = new JLabel();
        south = new JLabel();
        prog = new JProgressBar();
        setUndecorated(true);
        setTitle(title);
        south.setText(text);
        prog.setValue(progressValue);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add("North", north);
        c.add("South", south);
        c.add("Center", prog);
        if (parent == null) {
            Toolkit tk = getToolkit();
            Dimension dim = tk.getScreenSize();
            setBounds(dim.width / 2 - 50, dim.height / 2 - 25, 100, 50);
        }
    }

    public void setText(String text) {
        south.setText(text);
    }

    public void setTitle(String title) {
        north.setText(title);
    }

    public void setProgressValue(int value) {
        prog.setValue(value);
    }
}
