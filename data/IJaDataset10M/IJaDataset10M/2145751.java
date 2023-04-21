package wmtecnology.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author wmontoza
 */
public class WPanel extends JPanel {

    public enum Possition {

        NOTHING, SIDE_TO_SIDE, CENTRALIZED, AJUST, EXTEND
    }

    ;

    private Image backgroundImage;

    private Possition backgroundImagePosition = Possition.NOTHING;

    private float alpha = 1.0f;

    private Point gradientInit;

    private Point gradientEnd;

    private Color gradientColor1;

    private Color gradientColor2;

    private int roundCorner = 0;

    private boolean antialiases = false;

    public WPanel() {
        setOpaque(false);
    }

    public WPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setOpaque(false);
    }

    public WPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public WPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setOpaque(false);
    }

    @Override
    public void setOpaque(boolean isOpaque) {
    }

    public int getRoundCorner() {
        return roundCorner;
    }

    public void setRoundCorner(int roundCorner) {
        this.roundCorner = roundCorner;
    }

    public Possition getBackgroundImagePosition() {
        return backgroundImagePosition;
    }

    public void setBackgroundImagePosition(Possition backgroundImagePosition) {
        this.backgroundImagePosition = backgroundImagePosition;
    }

    public boolean isAntialiases() {
        return antialiases;
    }

    public void setAntialiases(boolean antialiases) {
        this.antialiases = antialiases;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Color getGradientColor1() {
        return gradientColor1;
    }

    public void setGradientColor1(Color gradientColor1) {
        this.gradientColor1 = gradientColor1;
    }

    public Color getGradientColor2() {
        return gradientColor2;
    }

    public void setGradientColor2(Color gradientColor2) {
        this.gradientColor2 = gradientColor2;
    }

    public Point getGradientEnd() {
        return gradientEnd;
    }

    public void setGradientEnd(Point gradientEnd) {
        this.gradientEnd = gradientEnd;
    }

    public Point getGradientInit() {
        return gradientInit;
    }

    public void setGradientInit(Point gradientInit) {
        this.gradientInit = gradientInit;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setGradientColor1(bg);
        setGradientColor2(bg);
        setGradientInit(new Point(0, 0));
        setGradientEnd(new Point(0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        if (antialiases) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if (gradientColor1 != null && gradientColor2 != null && gradientInit != null && gradientEnd != null) {
            GradientPaint gp = new GradientPaint(gradientInit, gradientColor2, gradientEnd, gradientColor1);
            g2.setPaint(gp);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), roundCorner, roundCorner);
        if (backgroundImage != null) {
            Dimension tamView = new Dimension(getWidth(), getHeight());
            ImageIcon ic = new ImageIcon(backgroundImage);
            Dimension tamImage = new Dimension(ic.getIconWidth(), ic.getIconHeight());
            if (backgroundImagePosition == Possition.NOTHING) {
                g2.drawImage(backgroundImage, 0, 0, this);
            } else if (backgroundImagePosition == Possition.SIDE_TO_SIDE) {
                for (int y = 0; y < tamView.getHeight(); y = y + (int) tamImage.getHeight()) {
                    for (int x = 0; x < tamView.getWidth(); x = x + (int) tamImage.getWidth()) {
                        g2.drawImage(backgroundImage, x, y, this);
                    }
                }
            } else if (backgroundImagePosition == Possition.CENTRALIZED) {
                g2.drawImage(backgroundImage, (getWidth() / 2 - ((int) tamImage.getWidth() / 2)), getHeight() / 2 - (int) tamImage.getHeight() / 2, this);
            } else if (backgroundImagePosition == Possition.AJUST) {
                int num = (int) tamView.getWidth() - (int) tamImage.getWidth();
                g2.drawImage(backgroundImage, 0, 0, (int) tamImage.getWidth() + num, (int) tamImage.getHeight() + num, this);
            } else if (backgroundImagePosition == Possition.EXTEND) {
                g2.drawImage(backgroundImage, 0, 0, (int) tamView.getWidth(), (int) tamView.getHeight(), this);
            }
        }
        if (antialiases) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}
