package sh;

import java.util.Vector;
import util.Point2I;
import util.Direction;
import path.AStar;
import path.IPathBoard;

class StealerAI {

    private Game game_;

    private int marineTargetType_ = Marine.STANDARD;

    private int targetObject_ = 0;

    StealerAI(Game game) {
        game_ = game;
    }

    void setMarineTypeTarget(int t) {
        marineTargetType_ = t;
    }

    void setOverFear(int f) {
    }

    void setTargetObject(int o) {
        targetObject_ = o;
    }

    private class StealerPathBoard implements IPathBoard {

        private Piece p_;

        private Map map_ = game_.getMap();

        StealerPathBoard(Piece p) {
            p_ = p;
        }

        public boolean isObstacle(int x, int y) {
            if ((map_.getTile(x, y) & TileType.TYPE_MASK) == TileType.ENTRY) return false; else {
                boolean surroundingCell = Math.abs(x - p_.getPosX()) <= 1 && Math.abs(y - p_.getPosY()) <= 1;
                return !map_.canMove(x, y, false, false, surroundingCell);
            }
        }

        public boolean isEnd(int x, int y) {
            if (targetObject_ != 0) {
                if (targetObject_ == map_.getObject(x, y)) return true; else {
                    Piece p = map_.getPiece(x, y);
                    if (p instanceof Marine) {
                        Marine m = (Marine) p;
                        return m.getCarrying() == targetObject_;
                    } else return false;
                }
            } else {
                Piece p = map_.getPiece(x, y);
                if (p instanceof Marine) {
                    Marine m = (Marine) p;
                    if (marineTargetType_ != Marine.STANDARD) return marineTargetType_ == m.getType(); else return true;
                } else return false;
            }
        }
    }

    private void moveBlips() {
        Vector blips = game_.getBlips();
        for (int i = 0; i < blips.size(); ++i) {
            Blip blip = (Blip) blips.elementAt(i);
            if (!blip.isDeleted()) {
                game_.startMove(blip);
                AStar as = new AStar();
                if (as.findPath(blip.getPosX(), blip.getPosY(), new StealerPathBoard(blip))) {
                    Vector path = as.getPath();
                    boolean flip = path.size() <= (blip.getActionPoints() + 1);
                    if (!flip) {
                        int size = Math.min(path.size(), blip.getActionPoints());
                        for (int j = 0; j < size; ++j) {
                            Point2I p = (Point2I) path.elementAt(j);
                            if (game_.findMarineLineofSight(p.x, p.y, false) != null) {
                                flip = true;
                                break;
                            }
                        }
                    }
                    if (flip) {
                        Point2I p = (Point2I) path.firstElement();
                        int dir = Direction.getDirection(blip.getPosX(), blip.getPosY(), p.x, p.y);
                        util.Debug.assert2(dir != Direction.NONE, "Invalid direction");
                        Stealer[] ns = game_.flipBlip(blip, dir);
                        for (int si = 0; si < ns.length; ++si) if (ns[si] != null) moveStealer(ns[si]);
                    } else {
                        game_.getListener().beginTurn(blip);
                        for (int j = 0; j < path.size(); ++j) {
                            Point2I p = (Point2I) path.elementAt(j);
                            int dir = Direction.getDirection(blip.getPosX(), blip.getPosY(), p.x, p.y);
                            if (!game_.move(blip, dir)) break;
                            if (blip.isDeleted()) break;
                        }
                    }
                }
            }
        }
    }

    private void moveStealer(Stealer stealer) {
        game_.startMove(stealer);
        AStar as = new AStar();
        if (as.findPath(stealer.getPosX(), stealer.getPosY(), new StealerPathBoard(stealer))) {
            Vector path = as.getPath();
            game_.getListener().beginTurn(stealer);
            for (int j = 0; j < path.size(); ++j) {
                Point2I p = (Point2I) path.elementAt(j);
                int dir = Direction.getDirection(stealer.getPosX(), stealer.getPosY(), p.x, p.y);
                if (!game_.move(stealer, dir, (j + 1) < path.size())) break;
                if (stealer.isDeleted()) break;
            }
        }
    }

    private void moveStealers() {
        Vector stealers = game_.getStealers();
        for (int i = 0; i < stealers.size(); ++i) {
            Stealer stealer = (Stealer) stealers.elementAt(i);
            if (!stealer.isDeleted()) {
                moveStealer(stealer);
            }
        }
    }

    void doTurn() {
        moveStealers();
        moveBlips();
    }
}
