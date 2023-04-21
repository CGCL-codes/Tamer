package org.jogre.reversi.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import org.jogre.client.JogreController;
import org.jogre.client.awt.JogreComponent;
import org.jogre.common.Player;
import org.jogre.common.playerstate.PlayerState;
import org.jogre.common.util.GameProperties;
import org.jogre.common.util.JogreLogger;

/**
 * Component for showing score based on color and seat number
 *
 * @author Garrett Lehman (Gman)
 * @version Alpha 0.2.3
 */
public class ReversiScoreComponent extends JogreComponent {

    private ReversiModel model = null;

    private int seat = -1;

    /**
	 * Default constructor
	 *
	 * @param model
	 * @param width
	 * @param height
	 */
    public ReversiScoreComponent(ReversiModel model, int width, int height) {
        this.model = model;
        setPreferredSize(new Dimension(width, height));
        repaint();
    }

    /**
	 * Refresh the component.
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    public void paintComponent(Graphics g) {
        drawBackground(g);
        drawScores(g);
    }

    /**
	 * Draw background
	 *
	 * @param g Graphics
	 */
    public void drawBackground(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
	 * Draw scores
	 *
	 * @param g Graphics
	 */
    public void drawScores(Graphics g) {
        Color c0 = GameProperties.getPlayerColour(0);
        Color c1 = GameProperties.getPlayerColour(1);
        int w = getWidth() / 2;
        int h = getHeight();
        Font letterFont = new Font("SansSerif", Font.BOLD, 12);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(letterFont);
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D bounds = null;
        LineMetrics metrics = null;
        float width = 0;
        float lineheight = 0;
        float ascent = 0;
        float x0 = 0;
        float y0 = 0;
        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2d.setColor(c0);
        g2d.fillRect(1, 1, w - 2, h - 3);
        int score0 = this.model.piecesOwned(0);
        String score0Text = String.valueOf(score0);
        bounds = letterFont.getStringBounds(score0Text, frc);
        metrics = letterFont.getLineMetrics(score0Text, frc);
        width = (float) bounds.getWidth();
        lineheight = metrics.getHeight();
        ascent = metrics.getAscent();
        x0 = (float) ((w - width) / 2);
        y0 = (float) ((h - lineheight) / 2 + ascent);
        g2d.setColor(c1);
        g2d.drawString(score0Text, x0, y0);
        g2d.setColor(c1);
        g2d.fillRect(w, 1, w - 2, h - 3);
        int score1 = this.model.piecesOwned(1);
        String score1Text = String.valueOf(score1);
        bounds = letterFont.getStringBounds(score1Text, frc);
        metrics = letterFont.getLineMetrics(score1Text, frc);
        width = (float) bounds.getWidth();
        lineheight = metrics.getHeight();
        ascent = metrics.getAscent();
        x0 = (float) ((w - width) / 2);
        y0 = (float) ((h - lineheight) / 2 + ascent);
        g2d.setColor(c0);
        g2d.drawString(score1Text, x0 + w, y0);
        if (seat > -1) {
            g2d.setColor(Color.white);
            if (seat == 0) {
                g2d.drawRect(1, 1, w - 3, h - 4);
                g2d.drawRect(2, 2, w - 5, h - 6);
                g2d.drawRect(3, 3, w - 7, h - 8);
            } else {
                g2d.drawRect(w, 1, w - 3, h - 4);
                g2d.drawRect(w + 1, 2, w - 5, h - 6);
                g2d.drawRect(w + 2, 3, w - 7, h - 8);
            }
        }
    }

    public void setSeat(int seat) {
        this.seat = seat;
        repaint();
    }
}
