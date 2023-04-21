package hu.schmidtsoft.map.model.cut;

import hu.schmidtsoft.map.model.Rectangle;
import hu.schmidtsoft.transform.P2;

/**
 * Cut two konvex rectangles in 2d coordinate system and check whether they are
 * disjunct or not.
 * 
 * @author rizsi
 * 
 */
public class CutRectangles {

    public static boolean cuts(CutLine a, CutLine b) {
        P2 na = a.a.sub(a.b).meroleges();
        P2 nb = b.a.sub(b.b).meroleges();
        double ca = na.dot(a.a);
        double cb = nb.dot(b.a);
        return (Math.signum(na.dot(b.a) - ca) != Math.signum(na.dot(b.b) - ca)) && (Math.signum(nb.dot(a.a) - cb) != Math.signum(nb.dot(a.b) - cb));
    }

    /**
	 * Is the point on the right side of the line that is directed from a to b
	 * 
	 * @param l
	 * @return
	 */
    public static boolean isRight(CutLine l, P2 p) {
        P2 n = l.a.sub(l.b).meroleges();
        double c = n.dot(l.a);
        return n.dot(p) < c;
    }

    public static boolean inside(Rectangle a, P2 p) {
        return isRight(new CutLine(a.a, a.b), p) && isRight(new CutLine(a.b, a.c), p) && isRight(new CutLine(a.c, a.d), p) && isRight(new CutLine(a.d, a.a), p);
    }

    public static boolean cuts(Rectangle a, Rectangle b) {
        if (inside(a, b.a) || inside(a, b.b) || inside(a, b.c) || inside(a, b.d) || inside(b, a.a) || inside(b, a.b) || inside(b, a.c) || inside(b, a.d)) return true;
        CutLine a1 = new CutLine(a.a, a.b);
        CutLine a2 = new CutLine(a.b, a.c);
        CutLine a3 = new CutLine(a.c, a.d);
        CutLine a4 = new CutLine(a.d, a.a);
        CutLine b1 = new CutLine(b.a, b.b);
        CutLine b2 = new CutLine(b.b, b.c);
        CutLine b3 = new CutLine(b.c, b.d);
        CutLine b4 = new CutLine(b.d, b.a);
        return cuts(a1, b1) || cuts(a1, b2) || cuts(a1, b3) || cuts(a1, b4) || cuts(a2, b1) || cuts(a2, b2) || cuts(a2, b3) || cuts(a2, b4) || cuts(a3, b1) || cuts(a3, b2) || cuts(a3, b3) || cuts(a3, b4) || cuts(a4, b1) || cuts(a4, b2) || cuts(a4, b3) || cuts(a4, b4);
    }
}
