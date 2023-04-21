package com.codethesis.jpgnviewer.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import com.codethesis.jpgnviewer.core.Bishop;
import com.codethesis.jpgnviewer.core.GameManager;
import com.codethesis.jpgnviewer.core.Images;
import com.codethesis.jpgnviewer.core.King;
import com.codethesis.jpgnviewer.core.Knight;
import com.codethesis.jpgnviewer.core.MalformedPositionException;
import com.codethesis.jpgnviewer.core.Pawn;
import com.codethesis.jpgnviewer.core.Piece;
import com.codethesis.jpgnviewer.core.Queen;
import com.codethesis.jpgnviewer.core.Rook;
import com.codethesis.pgnparse.Color;

/**
 *
 * @author Deyan Rizov
 *
 */
public class BoardCanvas {

    private Canvas canvas;

    private GameManager gameManager;

    private Image tableBuffer;

    private boolean flip = false;

    /**
	 * 
	 * @param parent
	 */
    public BoardCanvas(Composite parent) {
        this(parent, SWT.NONE);
    }

    /**
	 * 
	 * @param parent
	 * @param style
	 */
    public BoardCanvas(Composite parent, int style) {
        canvas = new Canvas(parent, style);
        drawTable();
        canvas.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent arg0) {
                draw();
            }
        });
    }

    /**
	 * 
	 * @return
	 */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
	 * 
	 */
    public void redraw() {
        canvas.redraw();
    }

    /**
	 * 
	 */
    public void redrawFully() {
        drawTable();
        redraw();
    }

    /**
	 * @return the gameManager
	 */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
	 * @param gameManager the gameManager to set
	 */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
	 * @return the flip
	 */
    public boolean isFlip() {
        return flip;
    }

    /**
	 * @param flip the flip to set
	 */
    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    /**
	 * 
	 */
    private void drawTable() {
        if (tableBuffer != null) {
            tableBuffer.dispose();
        }
        String white = "";
        String black = "";
        if (gameManager != null) {
            if (gameManager.getGame().containsTagKey("White")) {
                white += gameManager.getGame().getTag("White");
            } else {
                white += "Unknown";
            }
            if (gameManager.getGame().containsTagKey("Black")) {
                black += gameManager.getGame().getTag("Black");
            } else {
                black += "Unknown";
            }
            if (gameManager.getGame().containsTagKey("WhiteElo")) {
                white += " (" + gameManager.getGame().getTag("WhiteElo") + ")";
            } else {
                white += " (Unknown)";
            }
            if (gameManager.getGame().containsTagKey("BlackElo")) {
                black += " (" + gameManager.getGame().getTag("BlackElo") + ")";
            } else {
                black += " (Unknown)";
            }
        }
        tableBuffer = new Image(Display.getDefault(), 450, 510);
        GC g = new GC(tableBuffer);
        Point whiteTextExtent = null;
        Point blackTextExtent = null;
        Font font = new Font(canvas.getDisplay(), "Arial, Helvetsia, Tahoma", 10, SWT.BOLD);
        if (gameManager != null) {
            g.setFont(font);
            g.setTextAntialias(SWT.ON);
            whiteTextExtent = g.textExtent(white);
            blackTextExtent = g.textExtent(black);
        }
        if (flip) {
            g.drawImage(Images.BOARDB, 0, 30);
            if (gameManager != null) {
                g.drawString(white, (450 - whiteTextExtent.x) / 2, (30 - whiteTextExtent.y) / 2);
                g.drawString(black, (450 - blackTextExtent.x) / 2, 480 + (30 - blackTextExtent.y) / 2);
            }
        } else {
            g.drawImage(Images.BOARDW, 0, 30);
            if (gameManager != null) {
                g.drawString(black, (450 - blackTextExtent.x) / 2, (20 - blackTextExtent.y) / 2);
                g.drawString(white, (450 - whiteTextExtent.x) / 2, 480 + (20 - whiteTextExtent.y) / 2);
            }
        }
        font.dispose();
        g.dispose();
    }

    /**
	 * 
	 */
    private void draw() {
        Image buffer = new Image(Display.getDefault(), canvas.getBounds());
        GC g = new GC(buffer);
        g.drawImage(tableBuffer, 0, 0);
        if (gameManager != null) {
            if (flip) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 7; j >= 0; j--) {
                        try {
                            Piece piece = gameManager.getBoard().getPiece(j, i);
                            if (piece != null) {
                                g.drawImage(getIPieceImage(piece), 25 + (7 - j) * 50, 30 + 25 + i * 50);
                            }
                        } catch (MalformedPositionException e) {
                        }
                    }
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        try {
                            Piece piece = gameManager.getBoard().getPiece(j, i);
                            if (piece != null) {
                                g.drawImage(getIPieceImage(piece), 25 + j * 50, 30 + 25 + (7 - i) * 50);
                            }
                        } catch (MalformedPositionException e) {
                        }
                    }
                }
            }
        }
        g.dispose();
        g = new GC(canvas);
        g.drawImage(buffer, 0, 0);
        g.dispose();
        buffer.dispose();
    }

    /**
	 * 
	 * @param piece
	 * @return
	 */
    private Image getIPieceImage(Piece piece) {
        if (piece instanceof Pawn) {
            return piece.getColor() == Color.white ? Images.WP : Images.BP;
        } else if (piece instanceof Knight) {
            return piece.getColor() == Color.white ? Images.WN : Images.BN;
        } else if (piece instanceof Bishop) {
            return piece.getColor() == Color.white ? Images.WB : Images.BB;
        } else if (piece instanceof Rook) {
            return piece.getColor() == Color.white ? Images.WR : Images.BR;
        } else if (piece instanceof Queen) {
            return piece.getColor() == Color.white ? Images.WQ : Images.BQ;
        } else if (piece instanceof King) {
            return piece.getColor() == Color.white ? Images.WK : Images.BK;
        }
        return null;
    }
}
