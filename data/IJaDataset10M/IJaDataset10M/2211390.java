package com.towerfense;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GridTile extends JLabel implements MouseListener, MouseMotionListener {

    private FenseCanvas parent;

    private int gridX;

    private int gridY;

    private static BufferedImage GRID_32_IMG;

    private static BufferedImage GRID_64_IMG;

    private static BufferedImage GRID_128_IMG;

    private static ImageIcon GRID_32;

    private static ImageIcon GRID_64;

    private static ImageIcon GRID_128;

    static {
        try {
            GRID_32_IMG = ImageIO.read(new File("resources/grid32.png"));
            GRID_64_IMG = ImageIO.read(new File("resources/grid64.png"));
            GRID_128_IMG = ImageIO.read(new File("resources/grid128.png"));
            GRID_32 = new ImageIcon(GRID_32_IMG);
            GRID_64 = new ImageIcon(GRID_64_IMG);
            GRID_128 = new ImageIcon(GRID_128_IMG);
        } catch (Exception e) {
        }
    }

    boolean hover;

    boolean selected;

    GridTile north, east, west, south;

    private static final int MIN_HEIGHT = 32;

    private static final int MAX_HEIGHT = 128;

    private int length;

    private int height;

    private int length2;

    private int height2;

    public GridTile(FenseCanvas parent, int x, int y) {
        this.parent = parent;
        this.gridX = x;
        this.gridY = y;
        setOpaque(false);
        setBackground(Color.black);
        length = 128;
        height = 64;
        length2 = 64;
        height2 = 32;
        this.setIcon(GRID_64);
        hover = false;
        setSize(length + 1, height + 1);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        if (!hover) {
            super.paintComponent(g);
        } else {
            g.setColor(Color.white);
            g.fillPolygon(new int[] { 0, length2, length, length2, 0 }, new int[] { height2, 0, height2, height, height2 }, 5);
        }
    }

    public void zoomIn() {
        if (height >= MAX_HEIGHT) return;
        length *= 2;
        height *= 2;
        length2 *= 2;
        height2 *= 2;
        setSize(length + 1, height + 1);
        if (height == 32) {
            setIcon(GRID_32);
        } else if (height == 64) {
            setIcon(GRID_64);
        } else if (height == 128) {
            setIcon(GRID_128);
        }
        repaint();
    }

    public void zoomOut() {
        if (height <= MIN_HEIGHT) return;
        length /= 2;
        height /= 2;
        length2 /= 2;
        height2 /= 2;
        setSize(length + 1, height + 1);
        if (height == 32) {
            setIcon(GRID_32);
        } else if (height == 64) {
            setIcon(GRID_64);
        } else if (height == 128) {
            setIcon(GRID_128);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        parent.hover(this);
        int x = e.getX();
        int y = e.getY();
        if (x <= length2) {
            x /= 2;
            if (y <= height2) {
                y = height2 - y;
                if (y > x) {
                    parent.hover(this.north);
                } else {
                    parent.hover(this);
                }
            } else {
                y -= height2;
                if (y > x) {
                    parent.hover(this.west);
                } else {
                    parent.hover(this);
                }
            }
        } else {
            x -= length2;
            x /= 2;
            if (y <= height2) {
                if (y < x) {
                    parent.hover(this.east);
                } else {
                    parent.hover(this);
                }
            } else {
                y -= height2;
                y = height2 - y;
                if (y < x) {
                    parent.hover(this.south);
                } else {
                    parent.hover(this);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (e.getButton() == 1) parent.zoomIn();
            if (e.getButton() == 3) parent.zoomOut();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
