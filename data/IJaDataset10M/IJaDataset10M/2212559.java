package org.iceinn.bank.view.mouvementview;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import org.iceinn.bank.data.Compte;

/**
 * @author Lionel FLAHAUT
 * 
 */
public class CompteDrawer {

    public static final int PADDING_WIDTH = 50;

    public static final int HEIGHT = 100;

    private static final Color BORDER = Color.decode("#666666");

    private Compte compte;

    private String labelCompte;

    private int x, y, height, width, arcW, arcH;

    private Color color;

    /**
	 * @return the color
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * @param color
	 *            the color to set
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * @return the arcW
	 */
    public int getArcW() {
        return arcW;
    }

    /**
	 * @param arcW
	 *            the arcW to set
	 */
    public void setArcW(int arcW) {
        this.arcW = arcW;
    }

    /**
	 * @return the arcH
	 */
    public int getArcH() {
        return arcH;
    }

    /**
	 * @param arcH
	 *            the arcH to set
	 */
    public void setArcH(int arcH) {
        this.arcH = arcH;
    }

    /**
	 * @param compte
	 */
    public CompteDrawer(Compte compte) {
        this.compte = compte;
    }

    /**
	 * @return the compte
	 */
    public Compte getCompte() {
        return compte;
    }

    /**
	 * @param compte
	 *            the compte to set
	 */
    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    /**
	 * @return the x
	 */
    public int getX() {
        return x;
    }

    /**
	 * @param x
	 *            the x to set
	 */
    public void setX(int x) {
        this.x = x;
    }

    /**
	 * @return the y
	 */
    public int getY() {
        return y;
    }

    /**
	 * @param y
	 *            the y to set
	 */
    public void setY(int y) {
        this.y = y;
    }

    /**
	 * @return the height
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * @param height
	 *            the height to set
	 */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
	 * @return the width
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * @param width
	 *            the width to set
	 */
    public void setWidth(int width) {
        this.width = width;
    }

    public void draw(Graphics2D g2d, int x, int y, Color color, boolean center) {
        Color originalColor = g2d.getColor();
        Font font = g2d.getFont();
        Font newFont = font.deriveFont(Font.BOLD);
        g2d.setFont(newFont);
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        if (labelCompte == null) {
            labelCompte = compte.getLibelle() + " [" + compte.getNumero() + "]";
        }
        int textH = metrics.getHeight();
        int labelWidth = metrics.stringWidth(labelCompte);
        if (center) {
            setX(x - (labelWidth + 50) / 2);
            setY(y - (HEIGHT) / 2);
        } else {
            setX(x);
            setY(y);
        }
        setWidth(labelWidth + PADDING_WIDTH);
        setHeight(HEIGHT);
        setArcW(10);
        setArcH(10);
        setColor(color);
        RoundRectangle2D rectangle2d = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), getArcW(), getArcH());
        g2d.setColor(color);
        g2d.fill(rectangle2d);
        g2d.setColor(BORDER);
        g2d.draw(rectangle2d);
        g2d.setColor(originalColor);
        g2d.drawString(labelCompte, getX() + ((int) rectangle2d.getWidth() - labelWidth) / 2, getY() + 20);
        g2d.setFont(newFont.deriveFont(newFont.getSize() * 1.6f));
        metrics = g2d.getFontMetrics(g2d.getFont());
        textH = metrics.getHeight();
        String str = "" + getCompte().getSolde() + " €";
        g2d.drawString(str, getX() + ((int) rectangle2d.getWidth() - metrics.stringWidth(str)) / 2, getY() + (100 + textH) / 2);
        g2d.setFont(font);
    }
}
