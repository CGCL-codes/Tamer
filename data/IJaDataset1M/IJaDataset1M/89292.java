package org.osll.tictactoe.client.gui;

import javax.swing.*;
import javax.swing.border.Border;
import org.osll.tictactoe.Team;
import java.awt.*;
import java.awt.event.*;

class RectangleArea extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8441338878656593167L;

    private Point point = null;

    private Field field = null;

    public RectangleArea(final Field field) {
        this.field = field;
        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        Border loweredBevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
        setBorder(compound);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int dimX = field.height();
                int dimY = field.width();
                int width = getWidth();
                int height = getHeight();
                int xSize = width / dimX;
                int ySize = height / dimY;
                int row = x / xSize;
                int col = y / ySize;
                Team state = field.get(row, col);
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int dimX = field.height();
        int dimY = field.width();
        int width = getWidth();
        int height = getHeight();
        int xSize = width / dimX;
        int ySize = height / dimY;
        for (int row = 0; row < dimX; ++row) for (int col = 0; col < dimY; ++col) {
            Team state = field.get(row, col);
            if (state == Team.X) {
                g.setColor(Color.blue);
                g.fillRect(row * xSize + 1, col * ySize + 1, xSize - 1, ySize - 1);
                g.setColor(Color.green);
                g.fillRect(row * xSize + 2, col * ySize + 2, xSize - 3, ySize - 3);
            } else {
                g.setColor(Color.blue);
                g.fillRect(row * xSize + 1, col * ySize + 1, xSize - 1, ySize - 1);
                g.setColor(Color.red);
                g.fillRect(row * xSize + 2, col * ySize + 2, xSize - 3, ySize - 3);
            }
        }
    }
}
