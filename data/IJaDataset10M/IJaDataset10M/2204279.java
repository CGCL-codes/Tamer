package exemplo2;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LogoAnimatorJPanel extends JPanel {

    private static final String IMAGE_NAME = "deitel";

    protected ImageIcon images[];

    private final int TOTAL_IMAGES = 30;

    private int currentImage = 0;

    private final int ANIMATION_DELAY = 50;

    private int width;

    private int height;

    private Timer animationTimer;

    public LogoAnimatorJPanel() {
        images = new ImageIcon[TOTAL_IMAGES];
        for (int count = 0; count < images.length; count++) images[count] = new ImageIcon(getClass().getResource("images/" + IMAGE_NAME + count + ".gif"));
        width = images[0].getIconWidth();
        height = images[0].getIconHeight();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        images[currentImage].paintIcon(this, g, 0, 0);
        if (animationTimer.isRunning()) currentImage = (currentImage + 1) % TOTAL_IMAGES;
    }

    public void startAnimation() {
        if (animationTimer == null) {
            currentImage = 0;
            animationTimer = new Timer(ANIMATION_DELAY, new TimerHandler());
            animationTimer.start();
        } else {
            if (!animationTimer.isRunning()) animationTimer.restart();
        }
    }

    public void stopAnimation() {
        animationTimer.stop();
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    private class TimerHandler implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            repaint();
        }
    }
}
