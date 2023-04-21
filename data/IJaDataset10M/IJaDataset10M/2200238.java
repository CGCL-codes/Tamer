package be.vds.jtb.swing.component.reflection;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JComponent;
import be.vds.jtb.swing.utils.GraphicsUtilities;

public class ReflectionPane extends JComponent {

    private static final long serialVersionUID = 8397669217772864085L;

    private BufferedImage image;

    private BufferedImage imageA;

    private ReflectionRenderer renderer = new ReflectionRenderer();

    public ReflectionPane() {
    }

    /** Creates a new instance of ReflectionPanel */
    public ReflectionPane(BufferedImage bufferedImage) {
        this.imageA = bufferedImage;
        this.image = renderer.createReflection(imageA);
    }

    /** Creates a new instance of ReflectionPanel */
    public ReflectionPane(BufferedImage bufferedImage, ReflectionRenderer reflectionRenderer) {
        this.imageA = bufferedImage;
        this.renderer = reflectionRenderer;
        this.image = renderer.createReflection(imageA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (imageA != null) {
            int w = getWidth();
            int x = (w - imageA.getWidth()) / 2;
            int y = 0;
            if (renderer.isBlurEnabled()) {
                x -= renderer.getEffectiveBlurRadius();
                y -= renderer.getEffectiveBlurRadius() + 1;
            }
            g.drawImage(image, x, y + imageA.getHeight(), null);
            if (renderer.isBlurEnabled()) {
                x += renderer.getEffectiveBlurRadius();
                y += renderer.getEffectiveBlurRadius() + 1;
            }
            g.drawImage(imageA, x, y, null);
        }
    }

    public void setOpacity(float opacity) {
        renderer.setOpacity(opacity);
        image = renderer.createReflection(imageA);
        repaint();
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.imageA = bufferedImage;
        this.image = renderer.createReflection(imageA);
    }
}
