package mysterychess.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tin Bui-Huy
 */
public class Soldier extends Role {

    public Soldier() {
        super(PieceName.soldier);
    }

    public boolean move(Point toPosition) {
        if (!isPossiblePoint(toPosition)) {
            return false;
        }
        Piece capturedPie = myPiece.getTeam().getMatch().getPieceAt(toPosition);
        if (capturedPie != null && capturedPie.getTeam().equals(myPiece.getTeam())) {
            return false;
        }
        if (capturedPie == null) {
            moveTo(toPosition);
        } else {
            capturePieceAt(toPosition);
        }
        return true;
    }

    private boolean inBorder(Point position) {
        if (myPiece.getTeam().getPosition() == Team.TeamPosition.TOP) {
            if (position.y < 0 || position.y > 4) {
                return false;
            }
        } else {
            if (position.y < 5 || position.y > 9) {
                return false;
            }
        }
        return true;
    }

    protected boolean isPossiblePoint(Point p) {
        if (myPiece.getPosition().x != p.x && myPiece.getPosition().y != p.y) {
            return false;
        }
        if (myPiece.getPosition().x == p.x) {
            if (myPiece.getTeam().getPosition() == Team.TeamPosition.TOP) {
                if (p.y - myPiece.getPosition().y != 1) {
                    return false;
                }
            } else {
                if (p.y - myPiece.getPosition().y != -1) {
                    return false;
                }
            }
        }
        if (myPiece.getPosition().y == p.y && Math.abs(myPiece.getPosition().x - p.x) > 1) {
            return false;
        }
        if (myPiece.getPosition().x - p.x != 0) {
            if (inBorder(myPiece.getPosition())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Point> possibleSteps() {
        List<Point> steps = new ArrayList<Point>();
        Point current = myPiece.getPosition();
        Point temp = null;
        if (myPiece.getTeam().getPosition() == Team.TeamPosition.TOP) {
            temp = new Point(current.x, current.y + 1);
        } else {
            temp = new Point(current.x, current.y - 1);
        }
        if (isPossiblePoint(temp) && !isDuplicated(temp)) {
            steps.add(temp);
        }
        for (int i = -1; i <= 1; i++) {
            if (i == 0) continue;
            temp = new Point(current.x + i, current.y);
            if (isPossiblePoint(temp) && !isDuplicated(temp)) {
                steps.add(temp);
            }
        }
        return steps;
    }
}
