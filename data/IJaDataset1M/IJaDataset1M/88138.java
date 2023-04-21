package ntorrent.core.view.component.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import ntorrent.NtorrentApplication;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    private Dimension screenSize = toolkit.getScreenSize();

    private Image icon = toolkit.getImage("plugins/ntorrent/icons/ntorrent48.png");

    public Window() {
        this(NtorrentApplication.APP_NAME);
    }

    public Window(String title) {
        super(title);
        setIconImage(icon);
    }

    public void drawWindow() {
        validate();
        pack();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        setVisible(true);
    }

    public void closeWindow() {
        setVisible(false);
        dispose();
    }
}
