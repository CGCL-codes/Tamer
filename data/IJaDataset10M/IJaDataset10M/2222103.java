package edu.ucla.stat.SOCR.util;

import java.awt.*;
import javax.swing.*;

public class RouletteWheel extends JComponent {

    int score, oldScore;

    static final int[] x = { 94, 119, 84, 156, 47, 170, 32, 157, 46, 32, 84, 149, 55, 130, 73, 162, 41, 172, 32, 41, 163, 33, 170, 56, 150, 74, 129, 95, 106, 63, 140, 36, 167, 37, 168, 65, 141, 106 };

    static final int[] y = { 31, 168, 33, 142, 57, 100, 99, 56, 142, 98, 167, 47, 151, 163, 36, 132, 65, 87, 111, 132, 65, 88, 110, 46, 149, 162, 34, 168, 30, 157, 40, 122, 76, 76, 122, 40, 157, 169 };

    boolean ballDrawn = false;

    static Image wheelImage;

    public RouletteWheel() {
        setSize(220, 220);
        setBackground(new Color(0, 128, 0));
    }

    public static void setImage(Image image) {
        wheelImage = image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(wheelImage, 0, 0, this);
        if (ballDrawn) {
            ballDrawn = false;
            drawBall();
        }
    }

    public void drawBall() {
        Graphics g = getGraphics();
        g.setXORMode(Color.yellow);
        if (ballDrawn) g.fillOval(x[oldScore], y[oldScore], 10, 10);
        g.fillOval(x[score], y[score], 10, 10);
        oldScore = score;
        ballDrawn = true;
    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public int spin() {
        setScore((int) (38 * Math.random()));
        return score;
    }

    public int getScore() {
        return score;
    }

    public String getLabel() {
        if (score == 37) return "00"; else return String.valueOf(score);
    }

    public void reset() {
        ballDrawn = false;
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(220, 220);
    }

    public Dimension getMinimumSize() {
        return new Dimension(220, 220);
    }
}
