package vast;

import java.awt.*;
import java.util.*;

class Site {

    public point2d coord = new point2d();

    public int num;

    public int ref_count = 0;

    public Vector edge_idxlist = new Vector();

    public Site(double x, double y) {
        coord.x = x;
        coord.y = y;
    }

    public double dist(Site s) {
        return coord.distance(s.coord);
    }
}

;

class Edge {

    public double a, b, c;

    public Site[] ep = new Site[2];

    public Site[] reg = new Site[2];

    public int num;
}

;

class Halfedge {

    public Halfedge ELleft, ELright;

    public Edge ELedge;

    public int ELref_count;

    public int ELpm;

    public Site vertex;

    public double ystar;

    public Halfedge PQnext;
}

;

public class SFVoronoi {

    public SFVoronoi() {
        DELETED = new Edge();
        DELETED.a = DELETED.b = DELETED.c = (-2);
        le = 0;
        re = 1;
    }

    private boolean invalidated = false;

    private Hashtable sites = new Hashtable();

    public TreeMap mSites = new TreeMap();

    public Vector mEdges = new Vector();

    public Vector mVertices = new Vector();

    public void insert(int id, Point coord) {
        if (sites.containsKey(new Integer(id)) == false) {
            invalidated = true;
            sites.put(new Integer(id), new point2d((double) coord.x, (double) coord.y));
        }
    }

    public void remove(int id) {
        if (sites.remove(new Integer(id)) != null) invalidated = true;
    }

    public void update(int id, Point coord) {
        invalidated = true;
        point2d pt = (point2d) sites.get(new Integer(id));
        if (pt == null) insert(id, coord); else {
            pt.x = coord.x;
            pt.y = coord.y;
        }
    }

    public Point get(int id) {
        point2d p = (point2d) sites.get(new Integer(id));
        return new Point((int) p.x, (int) p.y);
    }

    public boolean contains(int id, Point coord) {
        if (sites.containsKey(new Integer(id)) == false) return false;
        recompute();
        return insideRegion(id, new point2d(coord.x, coord.y));
    }

    public boolean is_boundary(int id, Point center, int radius) {
        point2d coord = (point2d) sites.get(new Integer(id));
        if (coord == null) return false;
        recompute();
        point2d c = new point2d(center.x, center.y);
        int idx1, idx2, edge_idx;
        Enumeration e = ((Site) mSites.get(coord)).edge_idxlist.elements();
        while (e.hasMoreElements()) {
            edge_idx = ((Integer) e.nextElement()).intValue();
            idx1 = ((line2d) mEdges.elementAt(edge_idx)).vertexIndex[0];
            idx2 = ((line2d) mEdges.elementAt(edge_idx)).vertexIndex[1];
            if (idx1 == -1 || idx2 == -1 || c.distance((point2d) mVertices.elementAt(idx1)) >= (double) radius || c.distance((point2d) mVertices.elementAt(idx2)) >= (double) radius) {
                return true;
            }
        }
        return false;
    }

    public boolean is_enclosing(int id, int center_node_id) {
        Vector v = get_en(center_node_id);
        return (v == null ? false : v.contains(new Integer(id)));
    }

    public Vector get_en(int id) {
        point2d coord = (point2d) sites.get(new Integer(id));
        if (coord == null) return null;
        recompute();
        Vector en_list = new Vector();
        Enumeration e = ((Site) mSites.get(coord)).edge_idxlist.elements();
        while (e.hasMoreElements()) {
            int edge_idx = ((Integer) e.nextElement()).intValue();
            line2d line = (line2d) mEdges.elementAt(edge_idx);
            int en_id = (line.bisectingID[0] == id ? line.bisectingID[1] : line.bisectingID[0]);
            en_list.add(new Integer(en_id));
        }
        return en_list;
    }

    public boolean overlaps(int id, Point center, int radius) {
        point2d coord = (point2d) sites.get(new Integer(id));
        if (coord == null) return false;
        point2d c = new point2d(center.x, center.y);
        return (coord.distance(c) <= (double) (radius) ? true : false);
    }

    public int closest_to(Point coord) {
        Object[] keys = sites.keySet().toArray();
        Object[] points = sites.values().toArray();
        point2d p = new point2d(coord.x, coord.y);
        int closest = ((Integer) keys[0]).intValue();
        double min_dist = p.distance((point2d) points[0]);
        point2d pt;
        double d;
        for (int i = 1; i < sites.size(); i++) {
            pt = (point2d) points[i];
            if ((d = p.distance(pt)) < min_dist) {
                min_dist = d;
                closest = ((Integer) keys[i]).intValue();
            }
        }
        return closest;
    }

    public Hashtable get_sites() {
        return sites;
    }

    public int size() {
        return sites.size();
    }

    int recomput_count = 0;

    private void recompute() {
        if (invalidated == false) return;
        mSites.clear();
        mEdges.clear();
        mVertices.clear();
        sorted = false;
        triangulate = false;
        plot = true;
        debug = true;
        readsites();
        curr_site = mSites.values().iterator();
        geominit();
        if (plot) plotinit();
        voronoi(triangulate);
        invalidated = false;
    }

    private boolean insideRegion(int id, point2d p) {
        boolean b1, b2;
        Site site = (Site) mSites.get((point2d) sites.get(new Integer(id)));
        Enumeration e = site.edge_idxlist.elements();
        point2d coord = site.coord;
        int edge_idx;
        line2d edge;
        while (e.hasMoreElements()) {
            edge_idx = ((Integer) e.nextElement()).intValue();
            edge = (line2d) mEdges.elementAt(edge_idx);
            b1 = (edge.a * coord.x + edge.b * coord.y > edge.c);
            b2 = (edge.a * p.x + edge.b * p.y > edge.c);
            if (Math.abs(edge.a * p.x + edge.b * p.y - edge.c) < 0.001) b2 = b1;
            if (b1 != b2) return false;
        }
        if (e.hasMoreElements() == false) return true; else return false;
    }

    private Site nextone() {
        if (curr_site.hasNext() == false) return null;
        Site s = (Site) curr_site.next();
        return s;
    }

    private void readsites() {
        nsites = sites.size();
        Object[] points = sites.values().toArray();
        Object[] keys = sites.keySet().toArray();
        xmin = xmax = ((point2d) points[0]).x;
        ymin = ymax = ((point2d) points[0]).y;
        point2d pt;
        Site s;
        for (int i = 0; i < nsites; i++) {
            pt = (point2d) points[i];
            s = new Site(pt.x, pt.y);
            s.num = ((Integer) keys[i]).intValue();
            mSites.put(new point2d(pt), s);
            if (pt.x < xmin) xmin = pt.x; else if (pt.x > xmax) xmax = pt.x;
            if (pt.y < ymin) ymin = pt.y; else if (pt.y > ymax) ymax = pt.y;
        }
        Iterator it = mSites.values().iterator();
        while (it.hasNext()) {
            s = (Site) it.next();
        }
    }

    private boolean triangulate, sorted, plot, debug;

    private double xmin, xmax, ymin, ymax, deltax, deltay;

    private int nsites;

    private Iterator curr_site;

    private int sqrt_nsites;

    private int nvertices;

    private Site bottomsite;

    private int nedges;

    private Halfedge[] PQhash;

    private int PQhashsize;

    private int PQcount;

    private int PQmin;

    private Halfedge ELleftend, ELrightend;

    private int ELhashsize;

    private Halfedge[] ELhash;

    private Edge DELETED;

    private int le;

    private int re;

    private void geominit() {
        nvertices = 0;
        nedges = 0;
        double sn = nsites + 4;
        sqrt_nsites = (int) Math.sqrt(sn);
        deltay = ymax - ymin;
        deltax = xmax - xmin;
    }

    private Edge bisect(Site s1, Site s2) {
        double dx, dy, adx, ady;
        Edge newedge = new Edge();
        newedge.reg[0] = s1;
        newedge.reg[1] = s2;
        ref(s1);
        ref(s2);
        newedge.ep[0] = null;
        newedge.ep[1] = null;
        dx = s2.coord.x - s1.coord.x;
        dy = s2.coord.y - s1.coord.y;
        adx = (dx > 0 ? dx : -dx);
        ady = (dy > 0 ? dy : -dy);
        newedge.c = s1.coord.x * dx + s1.coord.y * dy + (dx * dx + dy * dy) * 0.5;
        if (adx > ady) {
            newedge.a = 1.0;
            newedge.b = dy / dx;
            newedge.c /= dx;
        } else {
            newedge.b = 1.0;
            newedge.a = dx / dy;
            newedge.c /= dy;
        }
        newedge.num = nedges++;
        out_bisector(newedge);
        return newedge;
    }

    private Site intersect(Halfedge el1, Halfedge el2, point2d p) {
        Edge e1, e2, e;
        Halfedge el;
        double d, xint, yint;
        e1 = el1.ELedge;
        e2 = el2.ELedge;
        if (e1 == null || e2 == null || (e1.reg[1] == e2.reg[1])) return null;
        d = e1.a * e2.b - e1.b * e2.a;
        if (-1.0e-10 < d && d < 1.0e-10) return null;
        xint = (e1.c * e2.b - e2.c * e1.b) / d;
        yint = (e2.c * e1.a - e1.c * e2.a) / d;
        if (e1.reg[1].coord.compareTo(e2.reg[1].coord) < 0) {
            el = el1;
            e = e1;
        } else {
            el = el2;
            e = e2;
        }
        boolean right_of_site = xint >= e.reg[1].coord.x;
        if ((right_of_site && el.ELpm == le) || (!right_of_site && el.ELpm == re)) return null;
        Site v = new Site(xint, yint);
        v.ref_count = 0;
        return v;
    }

    private boolean right_of(Halfedge el, point2d p) {
        boolean right_of_site, above, fast;
        double dxp, dyp, dxs, t1, t2, t3, yl;
        Edge e = el.ELedge;
        Site topsite = e.reg[1];
        right_of_site = p.x > topsite.coord.x;
        if (right_of_site && el.ELpm == le) return true;
        if (!right_of_site && el.ELpm == re) return false;
        if (e.a == 1.0) {
            dyp = p.y - topsite.coord.y;
            dxp = p.x - topsite.coord.x;
            fast = false;
            if ((!right_of_site & (e.b < 0.0)) | (right_of_site & (e.b >= 0.0))) fast = above = (dyp >= e.b * dxp); else {
                above = p.x + p.y * e.b > e.c;
                if (e.b < 0.0) above = !above;
                if (!above) fast = true;
            }
            if (!fast) {
                dxs = topsite.coord.x - (e.reg[0]).coord.x;
                if (dxs != 0) above = e.b * (dxp * dxp - dyp * dyp) < dxs * dyp * (1.0 + 2.0 * dxp / dxs + e.b * e.b); else above = false;
                if (e.b < 0.0) above = !above;
            }
            ;
        } else {
            yl = e.c - e.a * p.x;
            t1 = p.y - yl;
            t2 = p.x - topsite.coord.x;
            t3 = yl - topsite.coord.y;
            above = t1 * t1 > (t2 * t2 + t3 * t3);
        }
        return (el.ELpm == le ? above : !above);
    }

    private void endpoint(Edge e, int lr, Site s) {
        e.ep[lr] = s;
        ref(s);
        if (e.ep[re - lr] == null) return;
        out_ep(e);
        deref(e.reg[le]);
        deref(e.reg[re]);
    }

    private void makevertex(Site v) {
        v.num = nvertices++;
        out_vertex(v);
    }

    private void deref(Site v) {
        v.ref_count--;
    }

    private void ref(Site v) {
        v.ref_count++;
    }

    private double pxmin, pxmax, pymin, pymax, cradius;

    private void out_bisector(Edge e) {
        line2d line = new line2d(e.a, e.b, e.c);
        line.bisectingID[0] = e.reg[le].num;
        line.bisectingID[1] = e.reg[re].num;
        point2d pt1 = (point2d) sites.get(new Integer(line.bisectingID[0]));
        point2d pt2 = (point2d) sites.get(new Integer(line.bisectingID[1]));
        ((Site) mSites.get(pt1)).edge_idxlist.add(new Integer(e.num));
        ((Site) mSites.get(pt2)).edge_idxlist.add(new Integer(e.num));
        mEdges.add(line);
    }

    private void out_ep(Edge e) {
        ((line2d) mEdges.elementAt(e.num)).vertexIndex[0] = (e.ep[le] != null) ? (e.ep[le].num) : (-1);
        ((line2d) mEdges.elementAt(e.num)).vertexIndex[1] = (e.ep[re] != null) ? (e.ep[re].num) : (-1);
        if (!triangulate & plot) clip_line(e);
    }

    private void out_vertex(Site v) {
        mVertices.add(new point2d(v.coord.x, v.coord.y));
    }

    private void plotinit() {
        double dy = ymax - ymin;
        ;
        double dx = xmax - xmin;
        double d = (dx > dy ? dx : dy) * 1.1;
        pxmin = xmin - (d - dx) / 2.0;
        pxmax = xmax + (d - dx) / 2.0;
        pymin = ymin - (d - dy) / 2.0;
        pymax = ymax + (d - dy) / 2.0;
        cradius = (pxmax - pxmin) / 350.0;
    }

    private void clip_line(Edge e) {
        Site s1, s2;
        double x1, x2, y1, y2;
        if (e.a == 1.0 && e.b >= 0.0) {
            s1 = e.ep[1];
            s2 = e.ep[0];
        } else {
            s1 = e.ep[0];
            s2 = e.ep[1];
        }
        if (e.a == 1.0) {
            y1 = pymin;
            if (s1 != null && s1.coord.y > pymin) y1 = s1.coord.y;
            if (y1 > pymax) return;
            x1 = e.c - e.b * y1;
            y2 = pymax;
            if (s2 != null && s2.coord.y < pymax) y2 = s2.coord.y;
            if (y2 < pymin) return;
            x2 = e.c - e.b * y2;
            if (((x1 > pxmax) & (x2 > pxmax)) | ((x1 < pxmin) & (x2 < pxmin))) return;
            if (x1 > pxmax) {
                x1 = pxmax;
                y1 = (e.c - x1) / e.b;
            }
            if (x1 < pxmin) {
                x1 = pxmin;
                y1 = (e.c - x1) / e.b;
            }
            if (x2 > pxmax) {
                x2 = pxmax;
                y2 = (e.c - x2) / e.b;
            }
            if (x2 < pxmin) {
                x2 = pxmin;
                y2 = (e.c - x2) / e.b;
            }
        } else {
            x1 = pxmin;
            if (s1 != null && s1.coord.x > pxmin) x1 = s1.coord.x;
            if (x1 > pxmax) return;
            y1 = e.c - e.a * x1;
            x2 = pxmax;
            if (s2 != null && s2.coord.x < pxmax) x2 = s2.coord.x;
            if (x2 < pxmin) return;
            y2 = e.c - e.a * x2;
            if (((y1 > pymax) & (y2 > pymax)) | ((y1 < pymin) & (y2 < pymin))) return;
            if (y1 > pymax) {
                y1 = pymax;
                x1 = (e.c - y1) / e.a;
            }
            if (y1 < pymin) {
                y1 = pymin;
                x1 = (e.c - y1) / e.a;
            }
            if (y2 > pymax) {
                y2 = pymax;
                x2 = (e.c - y2) / e.a;
            }
            if (y2 < pymin) {
                y2 = pymin;
                x2 = (e.c - y2) / e.a;
            }
        }
        ((line2d) mEdges.elementAt(e.num)).seg.p1 = new point2d(x1, y1);
        ((line2d) mEdges.elementAt(e.num)).seg.p2 = new point2d(x2, y2);
    }

    private void PQinsert(Halfedge he, Site v, double offset) {
        Halfedge last, next;
        he.vertex = v;
        ref(v);
        he.ystar = v.coord.y + offset;
        last = PQhash[PQbucket(he)];
        while ((next = last.PQnext) != null && (he.ystar > next.ystar || (he.ystar == next.ystar && v.coord.x > next.vertex.coord.x))) last = next;
        he.PQnext = last.PQnext;
        last.PQnext = he;
        PQcount++;
    }

    private void PQdelete(Halfedge he) {
        Halfedge last;
        if (he.vertex != null) {
            last = PQhash[PQbucket(he)];
            while (last.PQnext != he) last = last.PQnext;
            last.PQnext = he.PQnext;
            PQcount--;
            deref(he.vertex);
            he.vertex = null;
        }
    }

    private int PQbucket(Halfedge he) {
        int bucket = (int) ((he.ystar - ymin) / deltay * PQhashsize);
        if (bucket < 0) bucket = 0;
        if (bucket >= PQhashsize) bucket = PQhashsize - 1;
        if (bucket < PQmin) PQmin = bucket;
        return bucket;
    }

    private boolean PQempty() {
        return (PQcount == 0);
    }

    private point2d PQ_min() {
        point2d answer = new point2d();
        while (PQhash[PQmin].PQnext == null) PQmin++;
        answer.x = PQhash[PQmin].PQnext.vertex.coord.x;
        answer.y = PQhash[PQmin].PQnext.ystar;
        return answer;
    }

    private Halfedge PQextractmin() {
        Halfedge curr;
        curr = PQhash[PQmin].PQnext;
        PQhash[PQmin].PQnext = curr.PQnext;
        PQcount--;
        return curr;
    }

    private void PQinitialize() {
        PQcount = 0;
        PQmin = 0;
        PQhashsize = 4 * sqrt_nsites;
        PQhash = new Halfedge[PQhashsize];
        for (int i = 0; i < PQhashsize; i++) {
            PQhash[i] = new Halfedge();
            PQhash[i].PQnext = null;
        }
    }

    private int ntry, totalsearch;

    private void ELinitialize() {
        ELhashsize = 2 * sqrt_nsites;
        ELhash = new Halfedge[ELhashsize];
        for (int i = 0; i < ELhashsize; i++) ELhash[i] = null;
        ELleftend = HEcreate(null, 0);
        ELrightend = HEcreate(null, 0);
        ELleftend.ELleft = null;
        ELleftend.ELright = ELrightend;
        ELrightend.ELleft = ELleftend;
        ELrightend.ELright = null;
        ELhash[0] = ELleftend;
        ELhash[ELhashsize - 1] = ELrightend;
    }

    private Halfedge HEcreate(Edge e, int pm) {
        Halfedge he = new Halfedge();
        he.ELedge = e;
        he.ELpm = pm;
        he.PQnext = null;
        he.vertex = null;
        he.ELref_count = 0;
        he.ystar = 0;
        return he;
    }

    private void ELinsert(Halfedge lb, Halfedge newH) {
        newH.ELleft = lb;
        newH.ELright = lb.ELright;
        lb.ELright.ELleft = newH;
        lb.ELright = newH;
    }

    private Halfedge ELgethash(int b) {
        Halfedge he;
        if (b < 0 || b >= ELhashsize) return null;
        he = ELhash[b];
        if (he == null || he.ELedge != DELETED) return he;
        ELhash[b] = null;
        return null;
    }

    private Halfedge ELleftbnd(point2d p) {
        int i, bucket;
        Halfedge he;
        bucket = (int) ((p.x - xmin) / deltax * ELhashsize);
        if (bucket < 0) bucket = 0;
        if (bucket >= ELhashsize) bucket = ELhashsize - 1;
        he = ELgethash(bucket);
        if (he == null) {
            for (i = 1; true; i++) {
                if ((he = ELgethash(bucket - i)) != null) break;
                if ((he = ELgethash(bucket + i)) != null) break;
            }
        }
        if (he == ELleftend || (he != ELrightend && right_of(he, p))) {
            do {
                he = he.ELright;
            } while (he != ELrightend && right_of(he, p));
            he = he.ELleft;
        } else {
            do {
                he = he.ELleft;
            } while (he != ELleftend && !right_of(he, p));
        }
        if (bucket > 0 && bucket < ELhashsize - 1) {
            if (ELhash[bucket] != null) ELhash[bucket].ELref_count--;
            ELhash[bucket] = he;
            ELhash[bucket].ELref_count++;
        }
        return he;
    }

    private void ELdelete(Halfedge he) {
        he.ELleft.ELright = he.ELright;
        he.ELright.ELleft = he.ELleft;
        he.ELedge = DELETED;
    }

    private Halfedge ELright(Halfedge he) {
        return (he.ELright);
    }

    private Halfedge ELleft(Halfedge he) {
        return (he.ELleft);
    }

    private Site leftreg(Halfedge he) {
        if (he.ELedge == null) return bottomsite;
        return (he.ELpm == le ? he.ELedge.reg[le] : he.ELedge.reg[re]);
    }

    private Site rightreg(Halfedge he) {
        if (he.ELedge == null) {
            return bottomsite;
        }
        return (he.ELpm == le ? he.ELedge.reg[re] : he.ELedge.reg[le]);
    }

    private void voronoi(boolean triangulate) {
        Site newsite, bot, top, temp, p;
        Site v;
        point2d newintstar = new point2d();
        int pm;
        Halfedge lbnd, rbnd, llbnd, rrbnd, bisector;
        Edge e;
        PQinitialize();
        bottomsite = nextone();
        ELinitialize();
        newsite = nextone();
        while (true) {
            if (!PQempty()) newintstar = PQ_min();
            if (newsite != null && (PQempty() || newsite.coord.compareTo(newintstar) < 0)) {
                lbnd = ELleftbnd(newsite.coord);
                rbnd = ELright(lbnd);
                bot = rightreg(lbnd);
                e = bisect(bot, newsite);
                bisector = HEcreate(e, le);
                ELinsert(lbnd, bisector);
                if ((p = intersect(lbnd, bisector, null)) != null) {
                    PQdelete(lbnd);
                    PQinsert(lbnd, p, p.dist(newsite));
                }
                lbnd = bisector;
                bisector = HEcreate(e, re);
                ELinsert(lbnd, bisector);
                if ((p = intersect(bisector, rbnd, null)) != null) PQinsert(bisector, p, p.dist(newsite));
                newsite = nextone();
            } else if (!PQempty()) {
                lbnd = PQextractmin();
                llbnd = ELleft(lbnd);
                rbnd = ELright(lbnd);
                rrbnd = ELright(rbnd);
                bot = leftreg(lbnd);
                top = rightreg(rbnd);
                v = lbnd.vertex;
                makevertex(v);
                endpoint(lbnd.ELedge, lbnd.ELpm, v);
                endpoint(rbnd.ELedge, rbnd.ELpm, v);
                ELdelete(lbnd);
                PQdelete(rbnd);
                ELdelete(rbnd);
                pm = le;
                if (bot.coord.y > top.coord.y) {
                    temp = bot;
                    bot = top;
                    top = temp;
                    pm = re;
                }
                e = bisect(bot, top);
                bisector = HEcreate(e, pm);
                ELinsert(llbnd, bisector);
                endpoint(e, re - pm, v);
                deref(v);
                if ((p = intersect(llbnd, bisector, null)) != null) {
                    PQdelete(llbnd);
                    PQinsert(llbnd, p, p.dist(bot));
                }
                if ((p = intersect(bisector, rrbnd, null)) != null) PQinsert(bisector, p, p.dist(bot));
            } else break;
        }
        for (lbnd = ELright(ELleftend); lbnd != ELrightend; lbnd = ELright(lbnd)) {
            e = lbnd.ELedge;
            out_ep(e);
        }
    }
}
