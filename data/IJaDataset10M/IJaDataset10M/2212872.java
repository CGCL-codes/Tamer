package visual3d.util.wrapper.cgcwrapper.cgc;

import java.awt.*;
import java.lang.Math;

public class cChain {

    static final int SCREENWIDTH = 350;

    cVertexList list;

    cVertexList listcopy;

    private int[] inters = new int[SCREENWIDTH];

    private int[] linklen = new int[1000];

    private int[] linklenback = new int[1000];

    private cVertex newpoint;

    private cVertex c;

    private cVertex q;

    cVertex v1;

    cVertex v2;

    int L1;

    int L2;

    int L3;

    int totlength;

    int halflength;

    int i = 0;

    int m;

    int firstlinks = 0;

    int nlinks;

    int nlinksback = 0;

    private boolean toClose = false;

    int r1;

    int r2;

    public int intersection = 0;

    public cChain(cVertexList list) {
        this.list = list;
    }

    public void SetAChain(cPointd Jk, cPointi target) {
        cPointi vaux1;
        cPointi vaux2;
        vaux1 = new cPointi(list.head.v.x, list.head.v.y);
        vaux2 = new cPointi(target.x, target.y);
        list.ClearVertexList();
        list.SetVertex(vaux1.x, vaux1.y);
        list.SetVertex((int) (Jk.x + .5), (int) (Jk.y + .5));
        list.SetVertex(vaux2.x, vaux2.y);
    }

    public void ClearChain() {
        nlinksback = 0;
        firstlinks = 0;
        listcopy = new cVertexList();
    }

    public double Length(cPointi point1, cPointi point2) {
        return (Math.sqrt((double) Math.abs((((point1.x - point2.x) * (point1.x - point2.x)) + ((point1.y - point2.y) * (point1.y - point2.y))))));
    }

    public double Length2(cPointi v) {
        double ss;
        ss = 0;
        ss = (double) ((v.x * v.x) + (v.y * v.y));
        return ss;
    }

    public void SubVec(cPointi a, cPointi b, cPointi c) {
        c.x = a.x - b.x;
        c.y = a.y - b.y;
    }

    public boolean Solven(int x, int y) {
        int halflength;
        cPointi target;
        cPointd Jk;
        cPointi J1;
        target = new cPointi(x, y);
        cVertex v0;
        v1 = list.head;
        for (i = 0; i < (list.n - 1); i++) {
            linklen[i] = (int) (Length(v1.v, v1.next.v) + .5);
            v1 = v1.next;
        }
        nlinks = list.n - 1;
        totlength = 0;
        for (i = 0; i < nlinks; i++) totlength += linklen[i];
        halflength = totlength / 2;
        if (nlinks > 2) {
            L1 = 0;
            for (m = 0; m < nlinks; m++) {
                if ((L1 + linklen[m]) > halflength) {
                    break;
                }
                L1 += linklen[m];
            }
            L2 = linklen[m];
            L3 = totlength - L1 - L2;
            firstlinks = m;
            for (i = 0; i < nlinks; i++) linklenback[i] = linklen[i];
            nlinksback = nlinks;
        } else if (nlinks == 2) {
            L1 = linklen[0];
            L2 = linklen[1];
            L3 = 0;
        } else {
            System.out.println("Just one link!!!");
            L1 = L2 = L3 = 0;
        }
        if ((nlinks == 3) && (nlinksback == 0)) {
            nlinksback = 3;
        }
        if (nlinks == 2) {
            Jk = new cPointd(0, 0);
            if (Solve2(L1, L2, target, Jk)) {
                System.out.println("Solve2 for 2 links: link1= " + L1 + ", link2= " + L2 + ", joint=\n");
                LineTo_d(Jk);
                SetAChain(Jk, target);
                return true;
            } else {
                return false;
            }
        } else {
            if (Solve3(L1, L2, L3, target)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean Solve3(int L1, int L2, int L3, cPointi target0) {
        cPointi target;
        cPointd Jk;
        cPointi J1;
        cPointi Ttarget;
        cPointi vaux1;
        cPointi vaux2;
        target = new cPointi(target0.x, target0.y);
        System.out.println("==>Solve3: links = " + L1 + ", " + L2 + ", " + L3);
        Jk = new cPointd(0, 0);
        if (Solve2(L1 + L2, L3, target, Jk)) {
            firstlinks++;
            nlinks = 2;
            System.out.println("Solve3: link1=" + (L1 + L2) + ", link2=" + L3 + ", joint=\n");
            LineTo_d(Jk);
            SetAChain(Jk, target);
            return true;
        } else if (Solve2(L1, L2 + L3, target, Jk)) {
            System.out.println("Solve3: link1= " + L1 + ", link2= " + (L2 + L3) + ", joint=\n");
            nlinks = 2;
            LineTo_d(Jk);
            SetAChain(Jk, target);
            return true;
        } else {
            J1 = new cPointi(L1, 0);
            Ttarget = new cPointi(0, 0);
            SubVec(target, J1, Ttarget);
            if (Solve2(L2, L3, Ttarget, Jk)) {
                Jk.x += L1;
                System.out.println("Solve3: link1=" + L1 + ", link2= " + L2 + ", link3= " + L1 + ", joints=\n");
                nlinks = 3;
                LineTo_i(J1);
                LineTo_d(Jk);
                SetAChain(Jk, target);
                cVertex VJ1 = new cVertex(list.head.v.x + J1.x, list.head.v.y);
                list.InsertBefore(VJ1, list.head.next);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean Solve2(int L1, int L2, cPointi target, cPointd J) {
        cPointi c1 = new cPointi(list.head.v.x, list.head.v.y);
        int nsoln;
        nsoln = TwoCircles(c1, L1, target, L2, J);
        return (nsoln != 0);
    }

    public int TwoCircles(cPointi c1, int r1, cPointi c2, int r2, cPointd p) {
        cPointi c;
        cPointd q;
        int nsoln = -1;
        c = new cPointi(0, 0);
        SubVec(c2, c1, c);
        q = new cPointd(0, 0);
        nsoln = TwoCircles0a(r1, c, r2, p);
        p.x = p.x + c1.x;
        p.y = p.y + c1.y;
        return nsoln;
    }

    public int TwoCircles0a(int r1, cPointi c2, int r2, cPointd p) {
        double dc2;
        double rplus2;
        double rminus2;
        double f;
        dc2 = Length2(c2);
        rplus2 = (r1 + r2) * (r1 + r2);
        rminus2 = (r1 - r2) * (r1 - r2);
        if ((dc2 > rplus2) || (dc2 < rminus2)) {
            return 0;
        }
        if (dc2 == rplus2) {
            f = r1 / (double) (r1 + r2);
            p.x = f * c2.x;
            p.y = f * c2.y;
            return 1;
        }
        if (dc2 == rminus2) {
            if (rminus2 == 0) {
                p.x = r1;
                p.y = 0;
                return 3;
            }
            f = r1 / (double) (r1 - r2);
            p.x = f * c2.x;
            p.x = f * c2.y;
            return 1;
        }
        int auxint = TwoCircles0b(r1, c2, r2, p);
        return auxint;
    }

    public int TwoCircles0b(int r1, cPointi c2, int r2, cPointd p) {
        double a2;
        cPointd q;
        double cost;
        double sint;
        a2 = Math.sqrt(Length2(c2));
        cost = c2.x / a2;
        sint = c2.y / a2;
        q = new cPointd(0, 0);
        TwoCircles00(r1, a2, r2, q);
        p.x = (cost * q.x) + (-sint * q.y);
        p.y = (sint * q.x) + (cost * q.y);
        return 2;
    }

    public void TwoCircles00(int r1, double a2, int r2, cPointd p) {
        double r1sq;
        double r2sq;
        r1sq = r1 * r1;
        r2sq = r2 * r2;
        p.x = (a2 + ((r1sq - r2sq) / a2)) / 2;
        p.y = Math.sqrt(r1sq - (p.x * p.x));
    }

    public cVertexList MakePoints(int lo, int hi1, int hi2, cVertex first, cVertex last, cVertexList listcopy) {
        double xaux;
        int lenaux = 0;
        cPointi v1 = new cPointi(0, 0);
        int sum = 0;
        for (i = lo; i < hi1; i++) {
            lenaux += linklenback[i];
        }
        sum = 0;
        for (i = lo; i < hi2; i++) {
            sum += linklenback[i];
            xaux = sum / (double) lenaux;
            v1.x = (int) (.5 + ((1 - xaux) * first.v.x) + (xaux * last.v.x));
            v1.y = (int) (.5 + ((1 - xaux) * first.v.y) + (xaux * last.v.y));
            listcopy.SetVertex(v1.x, v1.y);
        }
        return listcopy;
    }

    public void DrawDots(Graphics gContext, int w, int h) {
        cVertexList listcopy = new cVertexList();
        listcopy.SetVertex(list.head.v.x, list.head.v.y);
        if (nlinks == 3) {
            listcopy = MakePoints(0, firstlinks, firstlinks - 1, list.head, list.head.next, listcopy);
            listcopy.SetVertex(list.head.next.v.x, list.head.next.v.y);
            listcopy.SetVertex(list.head.next.next.v.x, list.head.next.next.v.y);
            listcopy = MakePoints(firstlinks + 1, nlinksback, nlinksback, list.head.next.next, list.head.prev, listcopy);
        } else {
            if (nlinksback > 2) {
                listcopy = MakePoints(0, firstlinks, firstlinks - 1, list.head, list.head.next, listcopy);
                listcopy.SetVertex(list.head.next.v.x, list.head.next.v.y);
                listcopy = MakePoints(firstlinks, nlinksback + 1, nlinksback - 1, list.head.prev.prev, list.head.prev, listcopy);
            }
        }
        listcopy.SetVertex(list.head.prev.v.x, list.head.prev.v.y);
        listcopy.DrawPoints(gContext, w, h);
    }

    public void LineTo_i(cPointi p) {
        System.out.println(" Line to i" + p.x + ", " + p.y);
    }

    public void MoveTo_i(cPointi p) {
        System.out.println(" Move to i" + p.x + ", " + p.y);
    }

    public void LineTo_d(cPointd p) {
        System.out.println(" Line to d" + p.x + ", " + p.y);
    }
}
