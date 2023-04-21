package ds.asterisk.incubation;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class TESTHeader extends JFrame {

    public static void main(String[] args) {
        new TESTHeader();
    }

    public TESTHeader() {
        setSize(300, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        ITunesNavigationHeader header = new ITunesNavigationHeader();
        ITunesHeaderButtonUI ui = new ITunesHeaderButtonUI();
        add(header, BorderLayout.NORTH);
        setVisible(true);
    }
}
