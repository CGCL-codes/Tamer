package com.jah0wl.catan;

import com.nokia.mid.ui.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import com.jah0wl.utils.*;
import com.jah0wl.catan.casillas.*;
import com.jah0wl.catan.utils.Dado;

class MapaCatan extends FullCanvas {

    public static final int yTranslation = -15;

    private static final int BIG_SIZE = 15;

    private static final int SMALL_SIZE = 8;

    private int size = SMALL_SIZE;

    private int rows;

    private int cols;

    Rectangle bounds;

    private Casilla[][] casillas;

    private Image buffImg;

    private Graphics buffGraph;

    private DirectGraphics buffDirectGraph;

    private static final int MAX_COL_HEX = 8;

    private static final int MAX_ROW_HEX = 4;

    private static final int[] nums = { 2, 3, 4, 5, 6, 8, 9, 10, 11, 12 };

    public MapaCatan(String s) {
        LoadMap(s);
        buffImg = Image.createImage(bounds.width, bounds.height);
        buffGraph = buffImg.getGraphics();
        paintIt(buffGraph);
    }

    private void LoadMap(String s) {
        int firstX = -2;
        int firstY = 6;
        cols = MAX_COL_HEX;
        rows = MAX_ROW_HEX;
        casillas = new Casilla[cols + 2][rows + 2];
        int r = Random.random(2) + 2;
        for (int y = 0; y <= rows + 1; y++) {
            for (int x = 0; x <= cols + 1; x++) {
                int cx, cy;
                if (x == 0) {
                    cx = firstX;
                    if (y == 0) {
                        cy = firstY;
                    } else {
                        cy = casillas[x][y - 1].y + 2 * (casillas[x][y - 1].getPoint(2).y - casillas[x][y - 1].y);
                    }
                } else if (x % 2 == 0) {
                    cx = casillas[x - 1][y].getPoint(1).x + size;
                    cy = casillas[x - 1][y].getPoint(1).y;
                } else {
                    cx = casillas[x - 1][y].getPoint(5).x + size;
                    cy = casillas[x - 1][y].getPoint(5).y;
                }
                if (x == 0 || y == 0 || x == cols + 1 || y == rows + 1) casillas[x][y] = new Agua(cx, cy, size); else {
                    if ((x % r == 0 || y % r == 0) && (x == 1 || y == 1 || x == 7 || y == 5)) {
                        int posi;
                        if (x == 1) posi = (Random.random(2) + 4) % 6; else if (y == 1) posi = Random.random(3) % 3; else if (x == 7) posi = Random.random(2) + 1; else posi = Random.random(3) + 2;
                        casillas[x][y] = new Agua(cx, cy, size, Random.random(6) % 6, posi);
                    } else casillas[x][y] = new Tierra(cx, cy, size, Random.random(6) % 6, nums[Random.random(10) % 10]);
                }
            }
        }
        bounds = new Rectangle();
        bounds.union(casillas[cols][1].getBounds());
        if (cols <= 1) bounds.union(casillas[1][rows].getBounds()); else bounds.union(casillas[2][rows].getBounds());
        bounds.width += size;
        bounds.height += size;
    }

    public void Move(int x, int y) {
        bounds.Move(x, y);
    }

    public int getSize() {
        return size;
    }

    public void setBig() {
        this.size = BIG_SIZE;
    }

    public void setSmall() {
        this.size = SMALL_SIZE;
    }

    public boolean isBig() {
        return (size == BIG_SIZE);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Point getPoint(int c, int r) {
        return casillas[c][r].getLocation();
    }

    public Point getPoint(int c, int r, int vertice) {
        return casillas[c][r].getPoint(vertice);
    }

    public Casilla getCasilla(int c, int r) {
        return casillas[c][r];
    }

    private void paintIt(Graphics g) {
        for (int y = 0; y <= rows + 1; y++) for (int x = 0; x <= cols + 1; x++) {
            casillas[x][y].paint(g);
        }
    }

    public void paint(Graphics g) {
        g.drawImage(buffImg, bounds.x, bounds.y, 0);
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }
}
