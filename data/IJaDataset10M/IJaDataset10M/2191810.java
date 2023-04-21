package org.gzigzag.module;

import org.gzigzag.*;
import java.util.*;
import java.awt.*;

/** Third, cardstack-like virtualcommunity view.
 */
public class VCView3 extends VirtualCommunity {

    public static final String rcsid = "$Id: VCView3.java,v 1.1 2001/02/27 20:02:21 veparkki Exp $";

    public static boolean dbg = false;

    private static void p(String s) {
        if (dbg) ZZLogger.log(s);
    }

    private static void pa(String s) {
        ZZLogger.log(s);
    }

    int dx = 10;

    int dy = 20;

    public void childSeries(FlobSet into, ZZCell cur, int x, int y, int w, float fract, double orot, int dir, int depth) {
        ZZCell s;
        if (cur.s(replies) != null) {
            s = cur.s(replies).s(replies_list);
            if (s != null) siblingSeries(into, s, x + (int) (w * fract), y, w, fract * magfract, orot, 1, depth + 1);
        }
    }

    public void siblingSeries(FlobSet into, ZZCell cur, int x, int y, int w, float fract, double orot, int dir, int depth) {
        if (depth >= 2) return;
        ZZCell s;
        int upstartx, upstarty, downstartx, downstarty;
        boolean domiddle = true;
        int count = 0;
        if (dir == 1) {
            count = cur.getRankLength(replies_list) - 1;
            if (count % 2 == 0) {
                domiddle = false;
            }
            cur = cur.s(replies_list, count / 2);
        }
        int cw = (int) (w * fract);
        if (domiddle) {
            int h = sibl.getHeight(cur, cfr(fract), cw);
            h /= depth + 1;
            sibl.makeFlob(into, cur, cur, cfr(fract), x, y - h / 2, 1, cw, h, orot, (dir == 0));
            if (dir >= 0) childSeries(into, cur, x, y, w, fract, orot, 1, depth);
            upstartx = x;
            upstarty = y - h / 2;
            downstartx = x;
            downstarty = y + h / 2;
        } else {
            upstartx = x;
            upstarty = y;
            downstartx = x;
            downstarty = y;
        }
        int cx = upstartx;
        int cy = upstarty;
        float cf = fract;
        s = cur;
        double rot = orot;
        rot = 0.0;
        cx = downstartx;
        cy = downstarty;
        cf = fract;
        s = cur;
        rot = orot;
        count = cur.getRankLength(replies_list) - 1;
        if (count > 0) {
            FlobInfo[] fi = new FlobInfo[count];
            int flth = 0;
            pa("COUNT: " + count);
            while ((s = s.s(replies_list, +1)) != null) {
                cf *= magfract;
                rot += 0.00;
                cw = (int) (w * cf);
                int ch = sibl.getHeight(s, cfr(cf), cw);
                ch /= depth + 1;
                fi[flth] = new FlobInfo(s, s, cfr(cf), cx, cy, 1, cw, ch, rot);
                pa("flth: " + flth);
                childSeries(into, s, cx, cy, w, fract, rot, 1, depth);
                flth++;
            }
            for (int i = 1; i <= flth; i++) {
                pa("i = " + i);
                int mc = 4;
                fi[flth - i].y += (flth - i) * (dy) + mc;
                fi[flth - i].x += (flth - i + 1) * dx;
                fi[flth - i].render(sibl, into);
            }
        }
        cx = upstartx;
        cy = upstarty;
        cf = fract;
        s = cur;
        rot = orot;
        while (s != null && (s = s.s(replies_list, -1)) != null) {
            if (s.s(replies_list, -1) == null) break;
            cf *= magfract;
            rot = 0.00;
            cw = (int) (w * cf);
            int ch = sibl.getHeight(s, cfr(cf), cw);
            ch /= depth + 1;
            cy -= dy;
            cx -= dx;
            sibl.makeFlob(into, s, s, cfr(cf), cx, cy, 1, cw, ch, rot, false);
            childSeries(into, s, cx, cy, w, fract, rot, 1, depth);
        }
        s = cur.h(replies_list).s(replies, -1);
        if (dir <= 0 && s != null) siblingSeries(into, s, x - (int) (w * fract * magfract), y, w, (float) (fract * magfract), orot, -1, depth + 1);
    }

    class FlobInfo {

        int x;

        int y;

        int h;

        int w;

        int d;

        ZZCell c;

        ZZCell hc;

        float fr;

        double rot;

        FlobInfo(ZZCell c, ZZCell hc, float fr, int x, int y, int d, int w, int h, double rot) {
            this.c = c;
            this.h = h;
            this.fr = fr;
            this.x = x;
            this.y = y;
            this.d = d;
            this.w = w;
            this.hc = hc;
            this.rot = rot;
        }

        void render(MessageFlobFactory mess, FlobSet into) {
            mess.makeFlob(into, c, hc, fr, x, y, d, w, h, rot, false);
        }
    }

    public void raster(FlobSet into, FlobFactory fact, ZZCell view, String[] dims, ZZCell accursed) {
        accursed = accursed.h(text);
        Dimension rd = into.getSize();
        Dimension d = rd;
        double rot = 0;
        siblingSeries(into, accursed, 3 * d.width / 10, d.height / 2, 2 * d.width / 5, (float) 1.0, 0, 0, 0);
    }
}
