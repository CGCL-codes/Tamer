package org.gameplate.games.tuw;

import org.gameplate.boardgame.*;
import org.gameplate.event.*;
import org.gameplate.ui.ClickAndDragGUI;
import org.gameplate.util.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.*;

public class TuwGUI extends ClickAndDragGUI {

    public static final String IMAGE_DIRECTORY = "images";

    public static final String BG_IMAGE = "background.jpg";

    private Image kowalskiBackground;

    private Image kowalskiBackgroundHi;

    private Image buffer;

    private Image[] player1Images;

    private Image[] player2Images;

    private Component observer;

    private Graphics bufferGraphics;

    private MediaTracker mt;

    private TuwPosition pendingFromPosition = null;

    private TuwPosition pendingToPosition = null;

    public TuwGUI(TuwBoard board, TuwRules rules, ImageLoader imageLoader, Component observer) {
        super(board, rules, observer);
        this.observer = observer;
        player1Images = new Image[8];
        player2Images = new Image[8];
        try {
            mt = new MediaTracker(observer);
            URL baseURL = imageLoader.getDocumentBase();
            kowalskiBackground = imageLoader.getImage(baseURL, "background.jpg");
            kowalskiBackgroundHi = imageLoader.getImage(baseURL, "background_hi.jpg");
            mt.addImage(kowalskiBackground, 0);
            mt.addImage(kowalskiBackgroundHi, 0);
            for (int i = 1; i <= 8; i++) {
                player1Images[i - 1] = imageLoader.getImage(baseURL, "player1_" + i + ".gif");
                player2Images[i - 1] = imageLoader.getImage(baseURL, "player2_" + i + ".gif");
                mt.addImage(player1Images[i - 1], i);
                mt.addImage(player2Images[i - 1], i);
            }
            mt.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int bufferWidth = kowalskiBackground.getWidth(observer);
        int bufferHeight = kowalskiBackground.getHeight(observer);
        buffer = observer.createImage(bufferWidth, bufferHeight);
        if (buffer == null) {
            buffer = new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_RGB);
        }
        bufferGraphics = buffer.getGraphics();
        mt.addImage(buffer, buffer.hashCode());
    }

    public void mousePressed(Position position) {
        pendingFromPosition = (TuwPosition) position;
        pendingToPosition = pendingFromPosition;
        repaint();
    }

    public void mouseDragged(Position position) {
        pendingToPosition = (TuwPosition) position;
        repaint();
    }

    public void mouseReleased(Position position) {
        boolean needsRepaint = (pendingFromPosition != null && pendingFromPosition.equals(pendingToPosition));
        pendingFromPosition = null;
        pendingToPosition = null;
        if (needsRepaint) {
            repaint();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(kowalskiBackground.getWidth(observer), kowalskiBackground.getHeight(observer));
    }

    public void paint(Graphics g) {
        TuwFigure figure = null;
        Position[] addPositions = null;
        Figure[] addFigures = null;
        bufferGraphics.drawImage(kowalskiBackground, 0, 0, observer);
        if (pendingFromPosition != null) {
            int x = pendingFromPosition.getX() * 46 + 7;
            int y = pendingFromPosition.getY() * 46 + 7;
            bufferGraphics.drawImage(kowalskiBackgroundHi, x, y, x + 46, y + 46, x, y, x + 46, y + 46, observer);
            Move[] moves = getRules().getLegalMoves(pendingFromPosition, getBoard());
            for (int i = 0; i < moves.length; i++) {
                TuwMove move = (TuwMove) moves[i];
                TuwPosition position = move.getToPosition();
                x = position.getX() * 46 + 7;
                y = position.getY() * 46 + 7;
                bufferGraphics.drawImage(kowalskiBackgroundHi, x, y, x + 46, y + 46, x, y, x + 46, y + 46, observer);
                if (position.equals(pendingToPosition)) {
                    addPositions = move.getAddPositions();
                    addFigures = move.getAddFigures();
                }
            }
            if (addPositions != null) {
                for (int fromIndex = 0; fromIndex < addPositions.length; fromIndex++) {
                    if (addPositions[fromIndex].equals(pendingFromPosition)) {
                        figure = (TuwFigure) addFigures[fromIndex];
                        x = pendingFromPosition.getX() * 46 + 7;
                        y = pendingFromPosition.getY() * 46 + 7;
                        bufferGraphics.drawImage(figure.getColor() == TuwFigure.SILVER ? player1Images[figure.getFigure()] : player2Images[figure.getFigure()], x, y, observer);
                        break;
                    }
                }
                for (int toIndex = 0; toIndex < addPositions.length; toIndex++) {
                    if (addPositions[toIndex].equals(pendingToPosition)) {
                        figure = (TuwFigure) addFigures[toIndex];
                        x = pendingToPosition.getX() * 46 + 7;
                        y = pendingToPosition.getY() * 46 + 7;
                        bufferGraphics.drawImage(figure.getColor() == TuwFigure.SILVER ? player1Images[figure.getFigure()] : player2Images[figure.getFigure()], x, y, observer);
                        break;
                    }
                }
            }
        }
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                try {
                    TuwPosition position = new TuwPosition(x, y);
                    if (addPositions == null || position.equals(pendingFromPosition) && position.equals(pendingToPosition) || !position.equals(pendingFromPosition) && !position.equals(pendingToPosition)) {
                        figure = (TuwFigure) getBoard().getFigure(position);
                        if (figure != null) {
                            bufferGraphics.drawImage(figure.getColor() == TuwFigure.SILVER ? player1Images[figure.getFigure()] : player2Images[figure.getFigure()], 7 + 46 * x, 7 + 46 * y, observer);
                        }
                    }
                } catch (IllegalPositionException e) {
                    System.err.println(e);
                }
            }
        }
        try {
            mt.waitForID(buffer.hashCode());
        } catch (InterruptedException ignore) {
        }
        g.drawImage(buffer, 0, 0, observer);
    }

    public Position toPosition(Point p) {
        return new TuwPosition((p.x - 7) / 46, (p.y - 7) / 46);
    }

    public void repaint() {
        observer.repaint();
    }

    public Graphics getGraphics() {
        throw new UnsupportedOperationException();
    }
}
