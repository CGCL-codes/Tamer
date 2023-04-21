package mswing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import mswing.print.MPrintInformations;

/**
 * Frame interne pour un JDesktopPane.
 *
 * @author Emeric Vernat
 */
public class MInternalFrame extends JInternalFrame implements MPrintInformations {

    private static final long serialVersionUID = 1L;

    private Icon oldFrameIcon;

    /**
	 * Constructeur.
	 */
    public MInternalFrame() {
        super("", true, true, true, true);
        if (UIManager.getLookAndFeel() != null && "Substance".equals(UIManager.getLookAndFeel().getName())) {
            setIconifiable(false);
        } else {
            oldFrameIcon = getFrameIcon();
        }
    }

    /** {@inheritDoc} */
    public void setIcon(boolean icon) throws java.beans.PropertyVetoException {
        try {
            if (icon && UIManager.getLookAndFeel() != null && !"Windows".equals(UIManager.getLookAndFeel().getName())) {
                toFront();
                repaint();
                final BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                final Graphics2D g2 = image.createGraphics();
                super.paint(g2);
                final Icon frameIcon = new ImageIcon(MUtilities.getFasterScaledInstance(image, 32, 32, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true));
                oldFrameIcon = getFrameIcon();
                setFrameIcon(frameIcon);
            } else {
                setFrameIcon(oldFrameIcon);
            }
        } catch (final Exception ex) {
            MUtilities.handleError(ex);
        }
        super.setIcon(icon);
    }

    /** {@inheritDoc} */
    public String getPrintTitle() {
        return getTitle();
    }

    /** {@inheritDoc} */
    public void setClosed(boolean closed) throws PropertyVetoException {
        super.setClosed(closed);
        setVisible(!closed);
    }
}
