package Jogo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JPanel;

class LeverageCanvas extends Canvas implements MouseListener {

    Board board = new Board();

    ControlGame controlGame;

    private int kpix = 40;

    private int w = 14;

    private int h = 9;

    private int iWidth;

    private int iHeight;

    int numclicks = 0;

    int xfrom = 1000, yfrom = 1000, xto = 1000, yto = 1000;

    Vector<Integer> xi = new Vector<Integer>();

    Vector<Integer> yi = new Vector<Integer>();

    boolean booleanDrawSelection = false;

    public LeverageCanvas(ControlGame controlGame) {
        iWidth = kpix * w;
        iHeight = kpix * h;
        this.controlGame = controlGame;
        addMouseListener(this);
    }

    public void paint(Graphics g) {
        setBackground(new Color(0, 100, 0));
        drawBoard(g);
        drawPieces(g);
        if (booleanDrawSelection) {
            for (int i = 0; i < xi.size(); i++) {
                drawSelection(g, xi.get(i), yi.get(i));
            }
        }
    }

    private void drawBoard(Graphics g) {
        int kx = kpix;
        int ky = kpix;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(kx * i, ky * j, kx, ky);
                if (board.getZone(j, i) == "SafetyZone") {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(kx * i, ky * j, kx - 1, ky - 1);
                }
                if (i == 1) {
                    g.setColor(Color.WHITE);
                    g.drawLine((iWidth / w), 0, (iWidth / w), iHeight);
                } else if (i == 7) {
                    g.setColor(Color.cyan);
                    g.drawLine((iWidth / w) * i, 0, (iWidth / w) * i, iHeight);
                } else if (i == 13) {
                    g.setColor(Color.white);
                    g.drawLine((iWidth / w) * i, 0, (iWidth / w) * i, iHeight);
                }
            }
        }
    }

    private void drawPieces(Graphics g) {
        Pieces p = new Pieces();
        int kl = 4, km = 9, ks = 12, ksc = 8;
        for (int i = 0; i < controlGame.getVectorPiecesSize(); i++) {
            p = controlGame.getPieceByIndex(i);
            if (p.getName() == "Large") {
                drawPiece(kl, p, g);
            } else if (p.getName() == "Medium") {
                drawPiece(km, p, g);
            } else if (p.getName() == "Small") {
                drawPiece(ks, p, g);
            } else {
                drawPiece(ksc, p, g);
            }
        }
    }

    private void drawPiece(int k, Pieces p, Graphics g) {
        g.setColor(p.getColor());
        g.fillOval((p.getSpotX()) * (kpix) + k, (p.getSpotY()) * (kpix) + k, (kpix) - (2 * k), (kpix) - (2 * k));
    }

    private void drawSelection(Graphics g, int x, int y) {
        g.setColor(Color.GREEN);
        g.drawRect(x * (kpix) + 1, y * (kpix) + 1, (kpix) - 3, (kpix) - 3);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        x = x / kpix;
        y = y / kpix;
        System.out.println("x=" + x + " y=" + y);
        movingPiece(x, y);
        repaint();
    }

    public void movingPiece(int x, int y) {
        Pieces p = new Pieces();
        if (x != 0 && x != 13) {
            switch(numclicks) {
                case 0:
                    if (controlGame.isTherePiece(x, y) && controlGame.getPieceByXY(x, y).getTeam() == controlGame.getTurn()) {
                        xfrom = x;
                        yfrom = y;
                        xi.add(x);
                        yi.add(y);
                        booleanDrawSelection = true;
                        numclicks = 1;
                        break;
                    } else break;
                case 1:
                    if (xfrom == x && yfrom == y) {
                        reset();
                        break;
                    } else {
                        if (canMoveTo(x, y)) {
                            xto = x;
                            yto = y;
                            xi.add(x);
                            yi.add(y);
                            booleanDrawSelection = true;
                            numclicks = 2;
                            break;
                        } else {
                            handleCantMove();
                            break;
                        }
                    }
                case 2:
                    if (xto == x && yto == y) {
                        xi.add(x);
                        yi.add(y);
                        move(xfrom, yfrom, xto, yto);
                        reset();
                        break;
                    } else {
                        if (canMoveTo(x, y)) {
                            xto = x;
                            yto = y;
                            xi.add(x);
                            yi.add(y);
                            booleanDrawSelection = true;
                            numclicks = 2;
                            break;
                        } else {
                            handleCantMove();
                            break;
                        }
                    }
            }
        }
    }

    private void move(int xfrom, int yfrom, int xto, int yto) {
        controlGame.jogo(xfrom, yfrom, xto, yto);
    }

    private void reset() {
        numclicks = 0;
        booleanDrawSelection = false;
        xfrom = 1000;
        yfrom = 1000;
        xto = 1000;
        yto = 1000;
        xi.removeAllElements();
        yi.removeAllElements();
    }

    private void handleCantMove() {
        reset();
    }

    private boolean canMoveTo(int x, int y) {
        return controlGame.canMoveTo(x, y);
    }
}
