package de.grogra.blocks.xFrogFileParser;

import de.grogra.blocks.Horn;
import de.grogra.graph.impl.Node;

public class Horn1 extends Expr {

    private final String hornImport = "import de.grogra.blocks.Horn;";

    private boolean applayShape = false;

    private String name = "";

    public Horn1(Expr a, Expr b, Expr c, Expr d, Expr e, Expr f, Expr g, Expr h, Expr i, Expr j, Expr k, Expr l, Expr m, Expr n, Expr o, Expr p, Expr q, Expr r, Expr s, Expr t, boolean applayShape) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.m = m;
        this.n = n;
        this.o = o;
        this.p = p;
        this.q = q;
        this.r = r;
        this.s = s;
        this.t = t;
        this.applayShape = applayShape;
        if (aktKeyFrame == 0) {
            name = new String(aktStructName);
            blocks.put(name, toXL());
            blocksGraphNodes.put(name, toGraph());
            if (!imports.contains(hornImport)) imports.add(hornImport);
            use_GFXColor = false;
            aktTexture = null;
        }
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    private String toXL() {
        Horn obj = new Horn();
        String ss = "";
        ss += "setName(" + name + "), ";
        if (!((FLOAT) (a.a.a.a)).equalsF5(obj.getSegments())) ss += "setSegments(" + a.a.a.a + "), ";
        if (!((FLOAT) (t.a.a.a)).equalsF5(obj.getLength())) ss += "setLength(" + t.a.a.a + "), ";
        if (!((FLOAT) (i.a.a)).equalsF5(obj.getRotX2())) ss += "setRotX2(" + i.a.a + "), ";
        if (!((FLOAT) (i.a.b)).equalsF5(obj.getRotX1())) ss += "setRotX1(" + i.a.b + "), ";
        if (!obj.getRotXMode().getClass().getSimpleName().equals(((Functions) i.a.c).getFunction().getClass().getSimpleName())) ss += "setRotXMode(" + ((Functions) i.a.c).toXL() + "), ";
        if (!((FLOAT) (j.a.a)).equalsF5(obj.getRotY2())) ss += "setRotY2(" + j.a.a + "), ";
        if (!((FLOAT) (j.a.b)).equalsF5(obj.getRotY1())) ss += "setRotY1(" + j.a.b + "), ";
        if (!obj.getRotYMode().getClass().getSimpleName().equals(((Functions) j.a.c).getFunction().getClass().getSimpleName())) ss += "setRotYMode(" + ((Functions) j.a.c).toXL() + "), ";
        if (!((FLOAT) (k.a.a)).equalsF5(obj.getRotZ2())) ss += "setRotZ2(" + k.a.a + "), ";
        if (!((FLOAT) (k.a.b)).equalsF5(obj.getRotZ1())) ss += "setRotZ1(" + k.a.b + "), ";
        if (!obj.getRotZMode().getClass().getSimpleName().equals(((Functions) k.a.c).getFunction().getClass().getSimpleName())) ss += "setRotZMode(" + ((Functions) k.a.c).toXL() + "), ";
        if (!((FLOAT) (l.a.a)).equalsF5(obj.getTransX2())) ss += "setTransX2(" + l.a.a + "), ";
        if (!((FLOAT) (l.a.b)).equalsF5(obj.getTransX1())) ss += "setTransX1(" + l.a.b + "), ";
        if (!obj.getTransXMode().getClass().getSimpleName().equals(((Functions) l.a.c).getFunction().getClass().getSimpleName())) ss += "setTransXMode(" + ((Functions) l.a.c).toXL() + "), ";
        if (!((FLOAT) (m.a.a)).equalsF5(obj.getTransY2())) ss += "setTransY2(" + m.a.a + "), ";
        if (!((FLOAT) (m.a.b)).equalsF5(obj.getTransY1())) ss += "setTransY1(" + m.a.b + "), ";
        if (!obj.getTransYMode().getClass().getSimpleName().equals(((Functions) m.a.c).getFunction().getClass().getSimpleName())) ss += "setTransYMode(" + ((Functions) m.a.c).toXL() + "), ";
        if (!((FLOAT) (n.a.a)).equalsF5(obj.getTransZ2())) ss += "setTransZ2(" + n.a.a + "), ";
        if (!((FLOAT) (n.a.b)).equalsF5(obj.getTransZ1())) ss += "setTransZ1(" + n.a.b + "), ";
        if (!obj.getTransZMode().getClass().getSimpleName().equals(((Functions) n.a.c).getFunction().getClass().getSimpleName())) ss += "setTransZMode(" + ((Functions) n.a.c).toXL() + "), ";
        if (!((FLOAT) (d.a.a)).equalsF5(obj.getRange2())) ss += "setRange2(" + d.a.a + "), ";
        if (!((FLOAT) (d.a.b)).equalsF5(obj.getRange1())) ss += "setRange1(" + d.a.b + "), ";
        if (!obj.getRangeMode().getClass().getSimpleName().equals(((Functions) d.a.c).getFunction().getClass().getSimpleName())) ss += "setRangeMode(" + ((Functions) d.a.c).toXL() + "), ";
        if (!((FLOAT) (c.a.a)).equalsF5(obj.getScale2())) ss += "setScale2(" + c.a.a + "), ";
        if (!((FLOAT) (c.a.b)).equalsF5(obj.getScale1())) ss += "setScale1(" + c.a.b + "), ";
        if (!obj.getScaleMode().getClass().getSimpleName().equals(((Functions) c.a.c).getFunction().getClass().getSimpleName())) ss += "setScaleMode(" + ((Functions) c.a.c).toXL() + "), ";
        if (!((FLOAT) (f.a.a)).equalsF5(obj.getSteps2())) ss += "setSteps2(" + f.a.a + "), ";
        if (!((FLOAT) (f.a.b)).equalsF5(obj.getSteps1())) ss += "setSteps1(" + f.a.b + "), ";
        if (!obj.getStepsMode().getClass().getSimpleName().equals(((Functions) f.a.c).getFunction().getClass().getSimpleName())) ss += "setStepsMode(" + ((Functions) f.a.c).toXL() + "), ";
        if (!((FLOAT) (h.a.a)).equalsF5(obj.getScrew2())) ss += "setScrew2(" + h.a.a + "), ";
        if (!((FLOAT) (h.a.b)).equalsF5(obj.getScrew1())) ss += "setScrew1(" + h.a.b + "), ";
        if (!obj.getScrewMode().getClass().getSimpleName().equals(((Functions) h.a.c).getFunction().getClass().getSimpleName())) ss += "setScrewMode(" + ((Functions) h.a.c).toXL() + "), ";
        if (!((FLOAT) (g.a.a)).equalsF5(obj.getFlap2())) ss += "setFlap2(" + g.a.a + "), ";
        if (!((FLOAT) (g.a.b)).equalsF5(obj.getFlap1())) ss += "setFlap1(" + g.a.b + "), ";
        if (!obj.getFlapMode().getClass().getSimpleName().equals(((Functions) g.a.c).getFunction().getClass().getSimpleName())) ss += "setFlapMode(" + ((Functions) g.a.c).toXL() + "), ";
        if (use_GFXColor) ss += color;
        if (ss.lastIndexOf(',') > 0) {
            ss = ss.substring(0, ss.length() - 2);
        }
        return "Horn().(" + ss + ")";
    }

    private Node toGraph() {
        Horn obj = new Horn();
        obj.setName(name);
        obj.setSegments(((FLOAT) a.a.a.a).getValue());
        obj.setLength(((FLOAT) t.a.a.a).getValue());
        obj.setRotX2(((FLOAT) i.a.a).getValue());
        obj.setRotX1(((FLOAT) i.a.b).getValue());
        obj.setRotXMode(((Functions) i.a.c).getFunction());
        obj.setRotY2(((FLOAT) j.a.a).getValue());
        obj.setRotY1(((FLOAT) j.a.b).getValue());
        obj.setRotYMode(((Functions) j.a.c).getFunction());
        obj.setRotZ2(((FLOAT) k.a.a).getValue());
        obj.setRotZ1(((FLOAT) k.a.b).getValue());
        obj.setRotZMode(((Functions) k.a.c).getFunction());
        obj.setTransX2(((FLOAT) l.a.a).getValue());
        obj.setTransX1(((FLOAT) l.a.b).getValue());
        obj.setTransXMode(((Functions) l.a.c).getFunction());
        obj.setTransY2(((FLOAT) m.a.a).getValue());
        obj.setTransY1(((FLOAT) m.a.b).getValue());
        obj.setTransYMode(((Functions) m.a.c).getFunction());
        obj.setTransZ2(((FLOAT) n.a.a).getValue());
        obj.setTransZ1(((FLOAT) n.a.b).getValue());
        obj.setTransZMode(((Functions) n.a.c).getFunction());
        obj.setRange2(((FLOAT) d.a.a).getValue());
        obj.setRange1(((FLOAT) d.a.b).getValue());
        obj.setRangeMode(((Functions) d.a.c).getFunction());
        obj.setScale2(((FLOAT) c.a.a).getValue());
        obj.setScale1(((FLOAT) c.a.b).getValue());
        obj.setScaleMode(((Functions) c.a.c).getFunction());
        obj.setSteps2(((FLOAT) f.a.a).getValue());
        obj.setSteps1(((FLOAT) f.a.b).getValue());
        obj.setStepsMode(((Functions) f.a.c).getFunction());
        obj.setScrew2(((FLOAT) h.a.a).getValue());
        obj.setScrew1(((FLOAT) h.a.b).getValue());
        obj.setScrewMode(((Functions) h.a.c).getFunction());
        obj.setFlap2(((FLOAT) g.a.a).getValue());
        obj.setFlap1(((FLOAT) g.a.b).getValue());
        obj.setFlapMode(((Functions) g.a.c).getFunction());
        if (phong != null && aktTexture != null) phong.setDiffuse(aktTexture);
        if (use_GFXColor) obj.setMaterial(phong);
        return obj;
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return "Horn {\n" + a.toString() + "\n" + b.toString() + "\n" + c.toString() + "\n" + d.toString() + "\n" + e.toString() + "\n" + f.toString() + "\n" + g.toString() + "\n" + h.toString() + "\n" + i.toString() + "\n" + j.toString() + "\n" + k.toString() + "\n" + l.toString() + "\n" + m.toString() + "\n" + n.toString() + "\n" + o.toString() + "\n" + p.toString() + "\n" + q.toString() + "\n" + r.toString() + "\n" + ((applayShape) ? "ApplayShape\n" : "") + s.toString() + "\n" + t.toString() + "\n" + "}\n";
    }
}
