package launcher.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JSeparator;
import launcher.util.GraphicsUtils;
import launcher.util.GraphicsUtils.Direction;

/**
 * JSeparator that keeps the 'icon lane' continuous between separated
 * {@link IconJMenuItem}s. The icon lane has a gradient fill equal to
 * {@link GraphicsUtils#ACTIVE_TITLE_BGCOLOUR()}.
 * 
 * @author Ramon Servadei
 * @version $Revision: 1.5 $
 * 
 */
public class IconJMenuJSeparator extends JSeparator {

    private static final long serialVersionUID = 1L;

    private static BufferedImage gradientBufferedImage;

    static {
        initialiseUI();
    }

    static final void initialiseUI() {
        gradientBufferedImage = GraphicsUtils.getTransparentGradientBufferedImage(IconJMenuItem.BACKGROUND_ICON_WIDTH, 4, GraphicsUtils.ACTIVE_TITLE_BGCOLOUR(), Direction.VERTICAL, 4);
    }

    public IconJMenuJSeparator() {
        super(JSeparator.HORIZONTAL);
    }

    @Override
    public void paint(Graphics g) {
        Graphics gcopy = g.create();
        try {
            if (!GraphicsUtils.IS_METAL() && !GraphicsUtils.IS_MOTIF()) {
            }
            gcopy.drawImage(gradientBufferedImage, 0, 0, null);
            int start = IconJMenuItem.BACKGROUND_ICON_WIDTH + IconJMenuItem.TEXT_ICON_SPACE;
            gcopy.drawLine(start, 0, getWidth(), 0);
            if (GraphicsUtils.IS_MOTIF()) {
                gcopy.setColor(Color.WHITE);
                gcopy.drawLine(start, 1, getWidth(), 1);
            }
        } finally {
            gcopy.dispose();
        }
    }
}
