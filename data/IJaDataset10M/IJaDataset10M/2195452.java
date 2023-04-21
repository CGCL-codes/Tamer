package org.gzigzag;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** A simple BoxType looking like the old CellBgFlobs.
 * Draws a filled (possibly with several colors) background rectangle,
 * surrounded by a rectangle of the current foreground color.
 */
public class SimpleBoxType extends BoxType {

    public static final String rcsid = "$Id: SimpleBoxType.java,v 1.2 2001/03/18 17:50:16 bfallenstein Exp $";

    public static final boolean dbg = true;

    public void renderBg(Graphics g, BoxedFlob f, int mx, int my, int md, int mw, int mh) {
        Color oldfg = g.getColor();
        if (f.solids != null) {
            for (int i = 0; i < f.nsolids; i++) {
                g.setColor(f.solids[i]);
                g.fillRect(mx + (mw * i) / f.nsolids, my, mw / f.nsolids + 1, mh);
            }
        } else {
            g.setColor(bg);
            g.fillRect(mx, my, mw, mh);
        }
        g.setColor(oldfg);
    }

    public void renderFrame(Graphics g, BoxedFlob f, int mx, int my, int md, int mw, int mh) {
        g.drawRect(mx - 1, my - 1, mw, mh);
        if (mh >= 14) {
            g.drawRect(mx - 2, my - 2, mw + 2, mh + 2);
        }
    }
}
