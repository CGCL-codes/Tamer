package jeliot.historyview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * 
 * @author nmyller
 */
public class ImageCanvas extends JComponent {

    /**
	 * 
	 */
    Image i;

    public void paintComponent(Graphics g) {
        if (i != null) {
            g.drawImage(i, 0, 0, this);
            setPreferredSize(new Dimension(i.getWidth(this), i.getHeight(this)));
            invalidate();
        }
    }

    /**
	 * @param i
	 */
    public void setImage(Image i) {
        this.i = i;
    }
}
