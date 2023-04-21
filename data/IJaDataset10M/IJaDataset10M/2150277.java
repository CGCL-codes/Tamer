package edu.georgiasouthern.math.flexagon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * @author Emil
 *
 */
public class HexagonPanel extends JPanel {

    public static final Color[] faceColors = { Color.blue, Color.red, Color.red, Color.blue, Color.red, Color.red, Color.blue, Color.red, Color.red, Color.blue, Color.red, Color.red, Color.blue, Color.red, Color.red, Color.blue, Color.red, Color.red };

    int[] state = new int[] { -1, -1, -1, -1, -1, -1 };

    public HexagonPanel() {
        setPreferredSize(new Dimension(400, 400));
    }

    public void showHexagon(String state) {
        int[] pats = new int[6];
        String[] tokens = state.split("-");
        String token = tokens[0];
        String[] faces = token.trim().substring(token.trim().indexOf(" ")).trim().split(" ");
        int p = Integer.parseInt(faces[0]);
        pats[0] = p;
        for (int i = 1; i < pats.length; i++) {
            token = tokens[i];
            faces = token.trim().split(" ");
            p = Integer.parseInt(faces[0]);
            pats[i] = p;
        }
        showHexagon(pats[0], pats[1], pats[2], pats[3], pats[4], pats[5]);
    }

    public void showHexagon(int p1, int p2, int p3, int p4, int p5, int p6) {
        state[0] = p1;
        state[1] = p2;
        state[2] = p3;
        state[3] = p4;
        state[4] = p5;
        state[5] = p6;
        repaint();
    }

    public void paint(Graphics g) {
        if (state[0] == -1) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 6; i++) {
            Triangle.draw(g2d, i + 1, "" + (state[i]), faceColors[state[i]], Triangle.OUTSIDE);
        }
    }
}
