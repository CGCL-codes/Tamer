package jaolho.simplerisk.ui.board;

import jaolho.simplerisk.Piece;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiChooser implements MouseListener, MouseMotionListener {

    public final ArrayList chosen = new ArrayList();

    Board board;

    Point startPoint;

    public final Rectangle lastPainted = new Rectangle();

    public boolean choosingInProgress = false;

    public MultiChooser(Board board) {
        this.board = board;
    }

    public void paintChosen(Graphics g) {
        for (Iterator i = chosen.iterator(); i.hasNext(); ) {
            Piece p = (Piece) i.next();
            board.singePieceChooser.highlight(p, g);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (chosen.size() > 0) {
            chosen.clear();
            board.chosenCounter.updateChosenCount();
            board.paintPieces();
        }
    }

    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        choosingInProgress = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (chosen.size() > 0 && !choosingInProgress) return;
        if (board.multiPieceMover.choosenAreDragged || board.singlePieceMover.choosenIsDragged) return;
        choosingInProgress = true;
        chosen.clear();
        for (Iterator i = board.getUIPieces().values().iterator(); i.hasNext(); ) {
            Piece p = (Piece) i.next();
            if (p.owner.id == board.player.id) {
                int r = (int) (p.getSizeOnBoard(board) * board.singePieceChooser.highlightFactor / 2f);
                Point loc = p.getLocationOnBoard(board);
                if (loc.x - r > startPoint.x && loc.y - r > startPoint.y && loc.x + r < e.getX() && loc.y + r < e.getY() || loc.x + r < startPoint.x && loc.y + r < startPoint.y && loc.x - r > e.getX() && loc.y - r > e.getY()) {
                    p.recLocation();
                    chosen.add(p);
                    board.chosenCounter.updateChosenCount();
                } else {
                    if (chosen.contains(p)) chosen.remove(p);
                    board.chosenCounter.updateChosenCount();
                }
            }
        }
        int x = ((startPoint.x < e.getPoint().x) ? startPoint.x : e.getPoint().x);
        int y = ((startPoint.y < e.getPoint().y) ? startPoint.y : e.getPoint().y);
        int w = Math.abs(startPoint.x - e.getPoint().x);
        int h = Math.abs(startPoint.y - e.getPoint().y);
        lastPainted.setBounds(x, y, w, h);
        board.repaint(x, y, w, h);
    }

    public void mouseMoved(MouseEvent e) {
    }
}
