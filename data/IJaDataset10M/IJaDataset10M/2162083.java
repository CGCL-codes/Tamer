package utils.swing;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

public class ImageButton extends JButton {

    private static final long serialVersionUID = -3511010693817827224L;

    private static final int ALPHA_BAND = 3;

    private final BufferedImage fMask;

    public ImageButton(Icon quiet, Icon rollover, Icon pressed, Icon disabled, Icon mask) {
        super(quiet);
        if (quiet == null || rollover == null || pressed == null) {
            throw new IllegalArgumentException("L'icona non può essere NULL.");
        }
        if (mask == null) {
            throw new IllegalArgumentException("La maschera non può essere NULL.");
        }
        checkIconMatchesMaskBounds(quiet, mask);
        checkIconMatchesMaskBounds(rollover, mask);
        checkIconMatchesMaskBounds(pressed, mask);
        checkIconMatchesMaskBounds(disabled, mask);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setRolloverIcon(rollover);
        setPressedIcon(pressed);
        setDisabledIcon(disabled);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        fMask = createMask(mask);
    }

    private BufferedImage createMask(Icon mask) {
        BufferedImage image = new BufferedImage(mask.getIconWidth(), mask.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        mask.paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        return image;
    }

    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        if (fMask != null) {
            checkIconMatchesMaskBounds(defaultIcon, new ImageIcon(fMask));
        }
    }

    @Override
    public void updateUI() {
        setUI(new CustomButtonUI());
    }

    private static void checkIconMatchesMaskBounds(Icon icon, Icon mask) {
        if (mask.getIconWidth() != icon.getIconWidth() || mask.getIconHeight() != icon.getIconHeight()) {
            throw new IllegalArgumentException("La maschera deve avere le stesse dimensioni dell'icona.");
        }
    }

    private class CustomButtonUI extends BasicButtonUI {

        private Rectangle fIconRect;

        private boolean maskContains(int x, int y) {
            return fIconRect != null && fIconRect.contains(x, y) && fMask.getRaster().getSample(x - fIconRect.x, y - fIconRect.y, ALPHA_BAND) > 0;
        }

        @Override
        public boolean contains(JComponent c, int x, int y) {
            return maskContains(x, y);
        }

        @Override
        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
            super.paintIcon(g, c, iconRect);
            if (fIconRect == null || !fIconRect.equals(iconRect)) {
                fIconRect = new Rectangle(iconRect);
            }
        }
    }
}
