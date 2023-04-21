package org.jspar.place;

import org.jspar.model.Terminal;
import org.jspar.tile.Position;

public class SideSelector implements IOutsideConnectionEnumerator {

    private Position l;

    private Position ll;

    private Position b;

    private Position bb;

    private Position r;

    private Position rr;

    private Position t;

    private Position tt;

    private int lef, rig, top, bot;

    private int side;

    public int xSideCount, ySideCount;

    public double xgr, ygr;

    public double xgbr, ygbr;

    private boolean useAveraging;

    public SideSelector(boolean useAveraging) {
        this.useAveraging = useAveraging;
        lef = rig = top = bot = 0;
        l = new Position(0, 0);
        ll = new Position(0, 0);
        r = new Position(0, 0);
        rr = new Position(0, 0);
        b = new Position(0, 0);
        bb = new Position(0, 0);
        t = new Position(0, 0);
        tt = new Position(0, 0);
    }

    public int side() {
        return side;
    }

    public boolean hasNoSide() {
        return xSideCount == 0 && ySideCount == 0;
    }

    /**
	 * @param s a terminal from the string to place
	 * @param e a terminal from an other string already placed
	 */
    public void terminalsDo(Terminal s, Terminal e) {
        System.out.println("side-select: " + s.module().name() + "." + s.name() + " <-> " + e.module().name() + "." + e.name());
        Position gs = s.getGlobalPosition();
        Position ge = e.getGlobalPosition();
        if ((s.isOut() || s.isInOut()) && (e.isIn() || e.isInOut())) {
            if (useAveraging) {
                l = l.add(gs);
                ll = ll.add(ge);
            } else {
                if (ll.x() == 0 || ge.x() < ll.x()) {
                    l = gs;
                    ll = ge;
                } else if (ge.y() < ll.y()) {
                    ll = new Position(ll.x(), ge.y());
                }
            }
            lef += 1;
        }
        if ((s.isOut() || s.isInOut()) && (e.isOut() || e.isInOut())) {
            if (ge.x() > bb.x()) {
                b = gs;
                bb = ge;
            }
            bot += 1;
        }
        if ((s.isIn() || s.isInOut()) && (e.isOut() || e.isInOut())) {
            if (useAveraging) {
                r = r.add(gs);
                rr = rr.add(ge);
            } else {
                if (ge.x() > rr.x()) {
                    r = gs;
                    rr = ge;
                } else if (ge.y() > rr.y()) {
                    rr = new Position(rr.x(), ge.y());
                }
            }
            rig += 1;
        }
        if ((s.isIn() || s.isInOut()) && (e.isIn() || e.isInOut())) {
            if (ge.x() > tt.x()) {
                t = gs;
                tt = ge;
            }
            top += 1;
        }
    }

    public void completeSelection() {
        chooseSide(top, bot, lef, rig);
        switch(side) {
            case Terminal.UP:
                if (top == lef) {
                    setGravityBox(l, ll, (useAveraging == true) ? lef : 1);
                    side = Terminal.LEFT;
                    xSideCount = -1 * lef;
                    ySideCount = top;
                    break;
                } else if (top == rig) {
                    setGravityBox(r, rr, (useAveraging == true) ? rig : 1);
                    side = Terminal.RIGHT;
                    xSideCount = rig;
                    ySideCount = (rig != 1) ? top : 0;
                } else setGravityBox(t, tt, 1);
                break;
            case Terminal.DOWN:
                System.out.println("DOWN");
                throw new RuntimeException("to complete");
            case Terminal.LEFT:
                System.out.println("LEFT");
                throw new RuntimeException("to complete");
            case Terminal.RIGHT:
                setGravityBox(r, rr, useAveraging ? rig : 1);
                break;
        }
        System.out.println("gr=(" + xgr + "," + ygr + "),gbr=(" + xgbr + "," + ygbr + ")");
    }

    private void chooseSide(int top, int bot, int lef, int rig) {
        int maxIn = top > lef ? top : lef;
        int maxOut = bot > rig ? bot : rig;
        int max, nextMax;
        if (maxIn > maxOut) {
            if (lef > top) {
                side = Terminal.LEFT;
                max = -1 * lef;
                nextMax = bot > top ? -1 * bot : top;
            } else {
                side = Terminal.UP;
                max = top;
                nextMax = -1 * lef;
            }
        } else {
            if (rig > bot) {
                side = Terminal.RIGHT;
                max = rig;
                nextMax = bot > top ? -1 * bot : top;
            } else {
                side = Terminal.DOWN;
                max = -1 * bot;
                nextMax = -1 * lef;
            }
        }
        xSideCount = (side == Terminal.LEFT) || (side == Terminal.RIGHT) ? max : nextMax;
        ySideCount = (side == Terminal.UP) || (side == Terminal.DOWN) ? max : nextMax;
    }

    private void setGravityBox(Position b, Position bb, int count) {
        if (count > 0) {
            xgr = b.x() / (double) count;
            ygr = b.y() / (double) count;
            xgbr = bb.x() / (double) count;
            ygbr = bb.y() / (double) count;
        } else throw new RuntimeException("count == 0");
    }

    public int yGravityCenter() {
        return (int) (ygbr - ygr);
    }

    public int xGravityCenter() {
        return (int) (xgbr - xgr);
    }

    public void normalizeSideCounts() {
        if (xSideCount == 0) ySideCount = ySideCount / Math.abs(ySideCount); else if (ySideCount == 0) xSideCount = xSideCount / Math.abs(xSideCount);
    }
}
