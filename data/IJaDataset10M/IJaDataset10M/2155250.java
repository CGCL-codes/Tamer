package admin.view.poker.player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class CardImagePanel extends JPanel {

    private static final long serialVersionUID = 8667062862588485655L;

    private static final int DEFAULT_CARD_WIDTH = 42;

    private static final int DEFAULT_CARD_HEIGHT = 65;

    private Image cardImage;

    public CardImagePanel(Image cardImage) {
        this.cardImage = cardImage;
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT));
    }

    public CardImagePanel(Image cardImage, int prefWidth, int prefHeight) {
        this.cardImage = cardImage;
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(cardImage, 0, 0, getWidth(), getHeight(), null);
        super.paintComponent(g);
    }
}
