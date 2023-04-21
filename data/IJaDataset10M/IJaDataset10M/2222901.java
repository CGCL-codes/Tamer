package com.um2.compression.grol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JPanel;

public class PanneauImageContour extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int prefferedSizeX = 200;

    private int prefferedSizeY = 200;

    private Image image = null;

    private PanneauCentre panneauCentre = null;

    private int coinSuperieurX = 0;

    private int coinSuperieurY = 0;

    public PanneauImageContour(PanneauCentre centre) {
        super(true);
        this.panneauCentre = centre;
    }

    public void setImage(Image img) {
        this.image = img;
    }

    public void recalculeImage() {
        if (this.getImageOriginale() != null) {
            BufferedImage buOriginale = Utilitaire.toBufferedImage(this.getImageOriginale());
            BufferedImage bufferContours = new BufferedImage(buOriginale.getWidth(), buOriginale.getHeight(), BufferedImage.TYPE_INT_RGB);
            float[] contour = new float[] { 0f, -1f, 0f, -1f, 4f, -1f, 0f, -1f, 0f };
            Kernel noyau = new Kernel(3, 3, contour);
            ConvolveOp operation = new ConvolveOp(noyau);
            operation.filter(buOriginale, bufferContours);
            BufferedImage bufferContoursAvecTransparence = new BufferedImage(buOriginale.getWidth(), buOriginale.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < bufferContoursAvecTransparence.getWidth(); i++) {
                for (int j = 0; j < bufferContoursAvecTransparence.getHeight(); j++) {
                    int alpha = (buOriginale.getRGB(i, j) >> 24) & 0xFF;
                    int rouge = (bufferContours.getRGB(i, j) >> 16) & 0xFF;
                    int vert = (bufferContours.getRGB(i, j) >> 8) & 0xFF;
                    int bleu = bufferContours.getRGB(i, j) & 0xFF;
                    int rgb = (alpha << 24) + (rouge << 16) + (vert << 8) + bleu;
                    bufferContoursAvecTransparence.setRGB(i, j, rgb);
                }
            }
            this.image = bufferContoursAvecTransparence;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.image == null) return new Dimension(this.prefferedSizeX, this.prefferedSizeY);
        return new Dimension(this.image.getWidth(this), this.image.getHeight(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.dessineGrillageDeFond(g);
        if (this.getImage() == null) return;
        coinSuperieurX = (this.getWidth() - this.getImage().getWidth(this)) / 2;
        if (coinSuperieurX < 0) coinSuperieurX = -coinSuperieurX;
        coinSuperieurY = (this.getHeight() - this.getImage().getHeight(this)) / 2;
        if (coinSuperieurY < 0) coinSuperieurY = -coinSuperieurX;
        g.drawImage(this.getImage(), coinSuperieurX, coinSuperieurY, this);
    }

    private void dessineGrillageDeFond(Graphics g) {
        int largeur = this.getWidth();
        if (this.getImage() != null) largeur = this.getImage().getWidth(this);
        int hauteur = this.getHeight();
        if (this.getImage() != null) hauteur = this.getImage().getHeight(this);
        if (this.getImage() != null) {
            coinSuperieurX = (this.getWidth() - this.getImage().getWidth(this)) / 2;
            if (coinSuperieurX < 0) coinSuperieurX = -coinSuperieurX;
            coinSuperieurY = (this.getHeight() - this.getImage().getHeight(this)) / 2;
            if (coinSuperieurY < 0) coinSuperieurY = -coinSuperieurX;
            g.clipRect(coinSuperieurX, coinSuperieurY, this.getImage().getWidth(this), this.getImage().getHeight(this));
        }
        boolean couleurClaire = true;
        boolean couleurClaireInitiale = true;
        for (int i = 0; i < largeur; i = i + 10) {
            couleurClaire = couleurClaireInitiale;
            couleurClaireInitiale = !couleurClaireInitiale;
            for (int j = 0; j < hauteur; j = j + 10) {
                if (couleurClaire) {
                    g.setColor(new Color(0x777777));
                } else {
                    g.setColor(new Color(0xBBBBBB));
                }
                g.fillRect(coinSuperieurX + i, coinSuperieurY + j, 10, 10);
                couleurClaire = !couleurClaire;
            }
        }
        g.clipRect(0, 0, this.getWidth(), this.getHeight());
    }

    public Image getImage() {
        return image;
    }

    public Image getImageOriginale() {
        return this.panneauCentre.getPanneauImageOriginale().getImage();
    }
}
