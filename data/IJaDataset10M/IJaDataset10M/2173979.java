package draughts.ki;

import java.awt.Point;
import ooprg.interfaces.Move;

public class GameMove extends Move {

    public GameMove(Point src, Point dst) {
        iSrc_ = src.x;
        jSrc_ = src.y;
        iDst_ = dst.x;
        jDst_ = dst.y;
    }

    public GameMove(int iSrc, int jSrc, int iDst, int jDst) {
        iSrc_ = iSrc;
        jSrc_ = jSrc;
        iDst_ = iDst;
        jDst_ = jDst;
    }

    public GameMove(Move m) {
        iSrc_ = m.getSource().y;
        jSrc_ = m.getSource().x;
        iDst_ = m.getDest().y;
        jDst_ = m.getDest().x;
    }

    int iSrc_;

    int jSrc_;

    int iDst_;

    int jDst_;

    public Point getSourceInternal() {
        return new Point(iSrc_, jSrc_);
    }

    public Point getDestInternal() {
        return new Point(iDst_, jDst_);
    }

    @Override
    public Point getSource() {
        return new Point(jSrc_, iSrc_);
    }

    @Override
    public Point getDest() {
        return new Point(jDst_, iDst_);
    }

    @Override
    public boolean equals(Object e) {
        if (!(e instanceof GameMove)) {
            return false;
        }
        GameMove other = (GameMove) e;
        return (iSrc_ == other.iSrc_ && jSrc_ == other.jSrc_ && iDst_ == other.iDst_ && jDst_ == other.jDst_);
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("[" + getSourceInternal().x + ";" + getSourceInternal().y + "] ==> ");
        s.append("[" + getDestInternal().x + ";" + getDestInternal().y + "]");
        return s.toString();
    }
}
