package com.karpouzas.reversiui.panels;

import com.karpouzas.reversicore.Board;
import com.karpouzas.reversicore.minimax.Position;
import com.karpouzas.reversiui.utils.MyImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

/**
 * represents board panel
 * @author George Karpouzas
 */
public class BoardPanel extends Panel {

    private int width, height;

    private int squareSize;

    private MyImage empty, white, black;

    private Graphics2D g2;

    private Board board;

    /**
     * constructor specifying width, height, square size and images
     * @param width
     * @param height
     * @param squareSize
     * @param images
     */
    public BoardPanel(int width, int height, int squareSize, HashMap<String, Image> images) {
        board = new Board();
        this.width = width;
        this.height = height;
        this.squareSize = squareSize;
        empty = new MyImage(images.get("EMPTY"));
        white = new MyImage(images.get("WHITE"));
        black = new MyImage(images.get("BLACK"));
    }

    /**
     * reset game board - start new game
     */
    public void reset() {
        board = new Board();
        repaint();
    }

    /**
     * paint method 
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        g2 = (Graphics2D) g;
        g2.setBackground(Color.BLACK);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        int y = 80;
        g2.setColor(Color.WHITE);
        for (int i = 0; i < Board.NUMBERS.length; i++) {
            g2.drawString(Board.NUMBERS[i], 480, y);
            y += 52;
        }
        int x = 70;
        for (int i = 0; i < Board.LETTERS.length; i++) {
            g2.drawString(Board.LETTERS[i], x, 480);
            x += 52;
        }
        for (int row = 1; row <= 9; row++) {
            int sy = row * squareSize;
            for (int col = 1; col <= 9; col++) {
                int sx = col * squareSize;
                switch(board.getValue(col, row)) {
                    case Board.WHITE:
                        g2.drawImage(white.getImage(), sx, sy, this);
                        break;
                    case Board.BLACK:
                        g2.drawImage(black.getImage(), sx, sy, this);
                        break;
                    case Board.NUMBER:
                    case Board.LETTER:
                        break;
                    default:
                        g2.drawImage(empty.getImage(), sx, sy, this);
                        break;
                }
            }
        }
    }

    /**
     * change image where mouse was clicked
     * @param type
     * @param x
     * @param y
     */
    public void changeImage(int type, int x, int y) {
        switch(type) {
            case Board.WHITE:
                board.MakeWhiteMove(new Position(y, x));
                break;
            case Board.BLACK:
                board.MakeBlackMove(new Position(y, x));
                break;
        }
        repaint();
    }

    /**
     * get upper Left corner x coordinate
     * @return Integer
     */
    public int getUpperLeftPointX() {
        return squareSize;
    }

    /**
     * get lower right corder x coordinate
     * @return Integer
     */
    public int getLowerRightPointX() {
        return squareSize * 9;
    }

    /**
     * get upper Left corner y coordinate
     * @return Integer
     */
    public int getUpperLeftPointY() {
        return squareSize;
    }

    /**
     * get lower right corder y coordinate
     * @return Integer
     */
    public int getLowerRightPointY() {
        return squareSize * 9;
    }
}
