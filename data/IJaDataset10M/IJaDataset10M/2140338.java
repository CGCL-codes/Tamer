package dnl.games.stragego.ui;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JComponent;

public class ImageLoader extends JComponent {

    private Image boardImage;

    private Image shieldImage;

    public ImageLoader() {
        MediaTracker tracker = new MediaTracker(this);
        URL url = getClass().getResource("stratego_board.png");
        boardImage = Toolkit.getDefaultToolkit().createImage(url);
        tracker.addImage(boardImage, 1);
        url = getClass().getResource("shield.png");
        shieldImage = Toolkit.getDefaultToolkit().createImage(url);
        tracker.addImage(shieldImage, 1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public Image getBoardImage() {
        return boardImage;
    }

    public Image getShieldImage() {
        return shieldImage;
    }
}
