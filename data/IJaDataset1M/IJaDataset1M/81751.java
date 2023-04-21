package sandkasten;

import javax.swing.*;
import static java.lang.Math.random;

public class JInternalFrameDemo {

    static void addInternalToDesktop(JDesktopPane desktop) {
        JInternalFrame iframe;
        iframe = new JInternalFrame("Ein internes Fenster", true, true, true, true);
        iframe.setBounds((int) (random() * 100), (int) (random() * 100), 100 + (int) (random() * 400), 100 + (int) (random() * 300));
        iframe.add(new JScrollPane(new JTextArea()));
        iframe.setVisible(true);
        desktop.add(iframe);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JDesktopPane desktop = new JDesktopPane();
        f.add(desktop);
        f.setSize(500, 400);
        addInternalToDesktop(desktop);
        addInternalToDesktop(desktop);
        addInternalToDesktop(desktop);
        f.setVisible(true);
    }
}
