package org.gerhardb.jibs.games.missingTile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.gerhardb.lib.awt.GraphicsUtil;
import org.gerhardb.lib.image.ImageChangeUtil;

class Tile extends JComponent implements MouseListener {

    private static final int MINIMUM_SIZE = 30;

    private static final Dimension SIZE = new Dimension(MINIMUM_SIZE, MINIMUM_SIZE);

    int myIndex;

    TilePanel myPanel;

    int myPlayIndex;

    private boolean iAmEmpty = false;

    boolean iCanBeMoved = false;

    boolean outsideNorth = false;

    boolean outsideSouth = false;

    boolean outsideEast = false;

    boolean outsideWest = false;

    int tileNorth = 0;

    int tileSouth = 0;

    int tileEast = 0;

    int tileWest = 0;

    /**
	 * 
	 * @param frame
	 * @param index
	 *           count from 1
	 */
    public Tile(int index, TilePanel panel) {
        this.myIndex = index;
        this.myPlayIndex = index;
        this.myPanel = panel;
        addMouseListener(this);
        super.setOpaque(false);
    }

    void updateTileDisplay(int emptyTile) {
        if (emptyTile == this.myIndex) {
            this.iAmEmpty = true;
        } else {
            this.iAmEmpty = false;
        }
        this.iCanBeMoved = false;
        if (emptyTile == this.tileNorth) {
            this.iCanBeMoved = true;
        } else if (emptyTile == this.tileSouth) {
            this.iCanBeMoved = true;
        } else if (emptyTile == this.tileEast) {
            this.iCanBeMoved = true;
        } else if (emptyTile == this.tileWest) {
            this.iCanBeMoved = true;
        }
        repaint();
    }

    void resetTile() {
        this.myPlayIndex = this.myIndex;
        this.iAmEmpty = false;
        this.iCanBeMoved = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (this.iCanBeMoved) {
            g.setColor(Color.pink);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (this.iAmEmpty) {
            g.setColor(Color.green);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            BufferedImage tileImage = this.myPanel.getImage(this.myPlayIndex - 1);
            if (tileImage != null) {
                paintImage(g, g2, tileImage);
            } else {
                g.setColor(Color.white);
                g.fillRect(0, 0, getWidth(), getHeight());
                paintTestPattern(g, g2);
            }
        }
        if (this.myPanel.isGameInProgress) {
            g2.setPaint(Color.black);
            BasicStroke stroke = new BasicStroke(4.0f);
            g2.setStroke(stroke);
            if (!this.outsideNorth) {
                g2.drawLine(0, 0, super.getWidth(), 0);
            }
            if (!this.outsideSouth) {
                g2.drawLine(0, super.getHeight(), super.getWidth(), super.getHeight());
            }
            if (!this.outsideEast) {
                g2.drawLine(super.getWidth(), 0, super.getWidth(), super.getHeight());
            }
            if (!this.outsideWest) {
                g2.drawLine(0, 0, 0, super.getHeight());
            }
        }
        g.dispose();
    }

    private void paintImage(Graphics g, Graphics2D g2, BufferedImage tileImage) {
        g2.setPaint(Color.black);
        String msg = "x";
        Dimension textSize = GraphicsUtil.setFont(g, msg, super.getWidth());
        int imgHeight = super.getHeight();
        int imgWidth = super.getWidth();
        int topOfText = 0;
        if (imgWidth > 0 && imgHeight > 0) {
            BufferedImage sized = ImageChangeUtil.fitAspectDown(tileImage, imgWidth, imgHeight);
            int centerX = ((super.getWidth() - sized.getWidth()) / 2);
            int centerY = ((super.getHeight() - sized.getHeight()) / 2);
            g.drawImage(sized, centerX, centerY, this);
            topOfText = centerY + sized.getHeight();
        }
        if (textSize.getWidth() > 0) {
            int textX = (int) ((super.getWidth() - textSize.getWidth()) / 2);
            FontMetrics fm = g.getFontMetrics();
            int textY = (topOfText + fm.getMaxAscent());
            g.drawString(msg, textX, textY);
        }
    }

    private void paintTestPattern(Graphics g, Graphics2D g2) {
        g2.setPaint(Color.black);
        String tileIndex = Integer.toString(this.myIndex);
        Dimension textSize = GraphicsUtil.setFont(g, tileIndex, super.getWidth());
        if (textSize.getWidth() > 0) {
            int textX = (int) ((super.getWidth() - textSize.getWidth()) / 2);
            int textY = (int) ((super.getHeight() - textSize.getHeight()) / 2);
            FontMetrics fm = g.getFontMetrics();
            textY = textY + fm.getAscent();
            int offset = 10;
            String msg = Integer.toString(this.tileNorth);
            g.drawString(msg, textX, offset * 2);
            msg = Integer.toString(this.tileSouth);
            textSize = GraphicsUtil.setFont(g, msg, super.getWidth());
            int y = (int) (super.getHeight() - textSize.getHeight() + fm.getAscent() - offset);
            g.drawString(msg, textX, y);
            msg = Integer.toString(this.tileEast);
            textSize = GraphicsUtil.setFont(g, msg, super.getWidth());
            int x = (int) (super.getWidth() - textSize.getWidth() - offset);
            g.drawString(msg, x, textY);
            msg = Integer.toString(this.tileWest);
            g.drawString(msg, offset, textY);
            int line = offset * 3;
            g2.drawRect(line, line, super.getWidth() - line * 2, super.getHeight() - line * 2);
            g2.setPaint(Color.red);
            String playIndex = Integer.toString(this.myPlayIndex);
            Font f = new Font("Default", Font.PLAIN, 48);
            g.setFont(f);
            fm = g.getFontMetrics();
            g.drawString(playIndex, ((super.getWidth() - fm.stringWidth(playIndex)) / 2), (((super.getHeight() - fm.getHeight()) / 2) + fm.getAscent()));
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        int size = super.getHeight();
        if (size > super.getWidth()) {
            size = super.getWidth();
        }
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMaximumSize() {
        int size = super.getHeight();
        if (size > super.getWidth()) {
            size = super.getWidth();
        }
        return new Dimension(size, size);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!this.myPanel.isGameInProgress) {
            return;
        }
        if (this.iCanBeMoved) {
            this.myPanel.moveMade(this);
        } else {
            this.getToolkit().beep();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
