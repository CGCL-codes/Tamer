package org.gzigzag.module;

import org.gzigzag.*;
import org.gzigzag.clang.*;
import java.awt.*;

/** A module interface to the Archimedes Procedural Layer executor/evaluator.
 */
public class Zaubertrank {

    String rcsid = "$Id: Zaubertrank.java,v 1.11 2001/04/23 21:56:15 bfallenstein Exp $";

    public static boolean dbg = false;

    static final void p(String s) {
        if (dbg) System.out.println(s);
    }

    static final void pa(String s) {
        System.out.println(s);
    }

    public static ZZModule module = new ZZModule() {

        public void action(String id, ZZCell code, ZZCell target, ZZView view, ZZView cview, String key, Point pt, ZZScene xi) {
            if (target == null) target = EditCursor.get(view.getViewcell());
            VirtualEditCursor ec = EditCursor.getVirtual(view.getViewcell());
            ZaubertrankTemplate.TreeCursor tc = new ZaubertrankTemplate.TreeCursor(ec);
            if (id.equals("Install")) {
                installArchimedes(target.getSpace());
            } else if (id.equals("New")) {
                ZZDefaultSpace.storeDirActionWaiting(view.getViewcell(), "Zaubertrank.New");
            } else if (id.equals("CreateExpression")) {
                ZZCell from = EditCursor.get(cview.getViewcell());
                Expression e = new Expression(target.getSpace(), from);
                e.connect(target);
                target.excise("d.cellview");
                getZaubertrankCellView(target.getSpace()).insert("d.cellview", 1, target);
                ZZCell dest = tc.moveToNextUndefined(1);
                if (dest != null) EditCursor.setStack(view.getViewcell(), tc.cur);
            } else if (id.equals("MakeAction")) {
                target.setText("ArchimedesClang.Run");
            } else if (id.equals("MoveIn")) {
                if (tc.moveIn(-1) != null) EditCursor.setStack(view.getViewcell(), tc.cur);
            } else if (id.equals("MoveOut")) {
                if (tc.moveOut() != null) EditCursor.setStack(view.getViewcell(), tc.cur);
            } else if (id.equals("MoveLeft") || id.equals("MoveRight")) {
                int dir = id.equals("MoveLeft") ? -1 : 1;
                if (tc.moveSidewards(dir) != null) EditCursor.setStack(view.getViewcell(), tc.cur);
            } else throw new ZZError("Zaubertrank action " + id + " not recognized.");
        }

        public void dirAction(String id, ZZCell win, ZZCell accursed, String vdim, int dir, ZZCell dataWin, ZZCell ctrlWin, ZZCell code) {
            if (id.equals("New")) {
                ZZCell c = accursed.N(vdim, dir);
                ZZCursorReal.set(win, c);
                c.setText("ArchimedesClang.Run");
                getZaubertrankCellView(c.getSpace()).insert("d.cellview", 1, c);
            } else throw new ZZError("Zaubertrank dir action " + id + " not recognized.");
        }

        public ZOb newZOb(String id) {
            return null;
        }
    };

    public static ZZCell installArchimedes(ZZSpace sp) {
        ZZCell c = ZZDefaultSpace.findOnSystemlist(sp, "TheZaubertrank", false);
        if (c == null) {
            c = ZZDefaultSpace.findOnSystemlist(sp, "TheZaubertrank", true);
            sp.getHomeCell().h("d.2", 1).insert("d.2", 1, c);
        }
        ZZCell res = c;
        c = c.getOrNewCell("d.1", 1);
        installArchimedesPrimitives(c);
        c = c.getOrNewCell("d.1", 1);
        installArchimedesBindings(c);
        return res;
    }

    public static void installArchimedesPrimitives(ZZCell start) {
        ZZSpace sp = start.getSpace();
        start.setText("Primitives");
        start.excise("d.2");
        String[] prims = AllPrimitives.standardPrimitives;
        for (int i = 0; i < prims.length; i++) {
            ZZCell p = AllPrimitives.getCell(sp, prims[i]);
            start.h("d.2", 1).connect("d.2", 1, p.zzclone());
        }
    }

    public static final ZZCell addAct(ZZCell start, String key, String act, ZZCell mode) {
        ZZCell c = start.N("d.2");
        c.setText(key);
        ZZCell d = c.N("d.1");
        d.setText(act);
        if (mode != null) mode.insert("d.3", 1, d);
        return c;
    }

    public static void installArchimedesBindings(ZZCell start) {
        ZZSpace sp = start.getSpace();
        start.setText("Bindings");
        start.excise("d.2");
        start.excise("d.3");
        ZZCell c = start.getOrNewCell("d.color", 1);
        c.setText(String.valueOf(new Color(180, 0, 255).getRGB()));
        ZZCell normal = ZZDefaultSpace.findOnSystemlist(start.getSpace(), "Bindings", true).getOrNewCell("d.1", 1);
        ZZCell ref = addAct(normal, "Ctrl-Z", "Zaubertrank.New", null);
        ref.insert("d.3", 1, start.N("d.2"));
        addAct(start, "Enter", "Zaubertrank.CreateExpression", null);
        addAct(start, "MouseClicked3", "Zaubertrank.CreateExpression", null);
        addAct(start, "MouseClicked1", "MOUSESETSTACK", null);
        addAct(start, "j", "Zaubertrank.MoveLeft", null);
        addAct(start, "J", "Zaubertrank.MoveLeft", null);
        addAct(start, "Left", "Zaubertrank.MoveLeft", null);
        addAct(start, "l", "Zaubertrank.MoveRight", null);
        addAct(start, "L", "Zaubertrank.MoveRight", null);
        addAct(start, "Right", "Zaubertrank.MoveRight", null);
        addAct(start, "i", "Zaubertrank.MoveIn", null);
        addAct(start, "I", "Zaubertrank.MoveIn", null);
        addAct(start, "k", "Zaubertrank.MoveIn", null);
        addAct(start, "Up", "Zaubertrank.MoveIn", null);
        addAct(start, ",", "Zaubertrank.MoveOut", null);
        addAct(start, "K", "Zaubertrank.MoveOut", null);
        addAct(start, "Down", "Zaubertrank.MoveOut", null);
        addAct(start, "q", "", null);
        addAct(start, "Q", "", null);
    }

    public static ZZCell getArchimedesBindings(ZZSpace sp) {
        ZZCell c = ZZDefaultSpace.findOnSystemlist(sp, "Archimedes", false);
        if (c != null) c = c.s("d.1", 2);
        if (c == null || c.s("d.2") == null) c = installArchimedes(sp).s("d.1", 2);
        return c;
    }

    public static ZZCell getZaubertrankCellView(ZZSpace sp) {
        ZZCell home = sp.getHomeCell();
        ZZCell c = home.s("d.zaubertrank-cellview");
        if (c != null) return c;
        c = home.N("d.zaubertrank-cellview");
        c.setText("The zaubertrank's cellview");
        ZZCell res = c;
        c = c.N("d.1");
        c.setText("ZaubertrankCellView");
        c = c.N("d.1");
        c = c.N("d.2");
        c.setText("EditBindings");
        c = c.N("d.1");
        getArchimedesBindings(sp).insert("d.3", 1, c);
        return res;
    }
}
