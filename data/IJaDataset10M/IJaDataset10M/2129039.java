package bagaturchess.ucitracker.impl.gamemodel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import bagaturchess.bitboard.impl.movegen.MoveInt;

public class EvaluatedPosition implements Serializable {

    private static final long serialVersionUID = -714092980265258749L;

    private int originateMove;

    private transient String epd;

    private Set<EvaluatedMove> children;

    public EvaluatedPosition(String _epd, int _originateMove) {
        epd = _epd;
        originateMove = _originateMove;
    }

    public void setChildren(Set<EvaluatedMove> _children) {
        children = _children;
    }

    public Set<EvaluatedMove> getChildren() {
        return children;
    }

    public String getEpd() {
        return epd;
    }

    public int getOriginateMove() {
        return originateMove;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Originator: " + MoveInt.moveToString(originateMove) + " EPD: " + epd + "\r\n";
        Iterator<EvaluatedMove> iter = children.iterator();
        while (iter.hasNext()) {
            result += iter.next() + "\r\n";
        }
        return result;
    }
}
