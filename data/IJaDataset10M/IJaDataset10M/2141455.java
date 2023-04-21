package rene.zirkel.objects;

import java.awt.Checkbox;
import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JPanel;
import rene.gui.Global;
import rene.gui.IconBar;
import rene.gui.MyLabel;
import rene.gui.MyTextField;
import rene.util.xml.XmlWriter;
import rene.zirkel.Zirkel;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.construction.ConstructionException;
import rene.zirkel.construction.Count;
import rene.zirkel.dialogs.EditConditionals;
import rene.zirkel.dialogs.ObjectEditDialog;
import rene.zirkel.graphics.MyGraphics;
import rene.zirkel.graphics.MyGraphics13;
import rene.zirkel.graphics.PolygonDrawer;
import rene.zirkel.graphics.PolygonFiller;
import rene.zirkel.structures.Coordinates;

class TrackEditDialog extends ObjectEditDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    IconBar IC, TypeIB;

    Checkbox Fixed, Discrete;

    MyTextField DMin;

    public TrackEditDialog(final ZirkelCanvas zc, final TrackObject o) {
        super(zc.getFrame(), Zirkel.name("edit.function.title"), o, "function");
    }

    @Override
    public void addFirst(final JPanel P) {
        Fixed = new Checkbox("");
        Fixed.setState(O.isFixed());
        P.add(new MyLabel(Zirkel.name("edit.fixed")));
        P.add(Fixed);
        DMin = new MyTextField("" + ((TrackObject) O).getDMin());
        P.add(new MyLabel(Zirkel.name("edit.track.dmin")));
        P.add(DMin);
    }

    @Override
    public void addSecond(final JPanel P) {
        final TrackObject T = (TrackObject) O;
        IC = new IconBar(F);
        IC.setIconBarListener(this);
        IC.addOnOffLeft("filled");
        IC.setState("filled", T.isFilled());
        IC.setIconBarListener(this);
        P.add(new MyLabel(""));
        P.add(IC);
        TypeIB = new IconBar(F);
        TypeIB.addToggleGroupLeft("type", 6);
        TypeIB.toggle("type", T.getType());
        P.add(new MyLabel(""));
        P.add(TypeIB);
        P.add(new MyLabel(Zirkel.name("edit.discrete")));
        P.add(Discrete = new Checkbox());
        Discrete.setState(T.isDiscrete());
    }

    @Override
    public void iconPressed(final String o) {
        if (o.equals("filled")) {
            if (IC.getState("filled")) {
                IB.setState("isback", true);
                ThicknessIB.setEnabled("solid", true);
            } else {
                IB.setState("isback", false);
                ThicknessIB.setState("solid", false);
                ThicknessIB.setEnabled("solid", false);
            }
        }
        super.iconPressed(o);
    }

    @Override
    public void setAction() {
        final TrackObject f = (TrackObject) O;
        f.setFixed(Fixed.getState());
        f.setFilled(IC.getState("filled"));
        try {
            f.setDMin(new Double(DMin.getText()).doubleValue());
        } catch (final Exception e) {
        }
        f.setType(TypeIB.getToggleState("type"));
        f.setDiscrete(Discrete.getState());
    }
}

/**
 * This object contains an automatic track. It is saved in the construction like
 * any other ConstructionObject. Validation is time consuming and is done only
 * on repaints from ZirkelCanvas.
 */
public class TrackObject extends ConstructionObject implements PointonObject {

    static Count N = new Count();

    public PointObject PM;

    public ConstructionObject O, P;

    public int PMax = 16, PN;

    public ConstructionObject PO[] = new ConstructionObject[PMax];

    public boolean Filled = false;

    public boolean Discrete = false;

    public int Type = 0;

    @Override
    public void setFilled(final boolean flag) {
        Filled = flag;
    }

    @Override
    public boolean isFilled() {
        return Filled;
    }

    public TrackObject(final Construction c) {
        super(c);
    }

    public TrackObject(final Construction c, final ConstructionObject p, final ConstructionObject po[], final int pn, final ConstructionObject o, final PointObject pm) {
        super(c);
        P = p;
        PN = pn;
        for (int i = 0; i < PN; i++) {
            PO[i] = po[i];
        }
        O = o;
        PM = pm;
        validate();
        updateText();
    }

    @Override
    public void setDefaults() {
        setShowName(Global.getParameter("options.locus.shownames", false));
        setShowValue(Global.getParameter("options.locus.showvalues", false));
        setColor(Global.getParameter("options.locus.color", 0), Global.getParameter("options.locus.pcolor", (Color) null));
        setColorType(Global.getParameter("options.locus.colortype", 0));
        setFilled(Global.getParameter("options.locus.filled", false));
        setHidden(Cn.Hidden);
        setObtuse(Cn.Obtuse);
        setSolid(Cn.Solid);
        setLarge(Cn.LargeFont);
        setBold(Cn.BoldFont);
    }

    @Override
    public void setTargetDefaults() {
        setShowName(Global.getParameter("options.locus.shownames", false));
        setShowValue(Global.getParameter("options.locus.showvalues", false));
        setColor(Global.getParameter("options.locus.color", 0), Global.getParameter("options.locus.pcolor", (Color) null));
        setColorType(Global.getParameter("options.locus.colortype", 0));
        setFilled(Global.getParameter("options.locus.filled", false));
    }

    @Override
    public String getTag() {
        return "Track";
    }

    @Override
    public int getN() {
        return N.next();
    }

    @Override
    public void updateText() {
        if (PM != null) {
            setText(text3(Zirkel.name("text.track"), P.getName(), PM.getName(), O.getName()));
        } else {
            setText(text2(Zirkel.name("text.trackof"), P.getName(), O.getName()));
        }
    }

    @Override
    public void validate() {
    }

    public PolygonFiller PF = null;

    @Override
    public void paint(final MyGraphics g, final ZirkelCanvas zc) {
        if (!Valid || mustHide(zc)) {
            return;
        }
        Coordinates C;
        Enumeration e = V.elements();
        double c0, r0, c, r;
        if (!Discrete && isStrongSelected() && g instanceof MyGraphics13) {
            final PolygonDrawer pm = new PolygonDrawer(g, this);
            pm.useAsMarker();
            if (e.hasMoreElements()) {
                C = (Coordinates) e.nextElement();
                c0 = zc.col(C.X);
                r0 = zc.row(C.Y);
                pm.drawTo(c0, r0);
                while (e.hasMoreElements()) {
                    C = (Coordinates) e.nextElement();
                    c = zc.col(C.X);
                    r = zc.row(C.Y);
                    if (Math.abs(c0 - c) < 100 && Math.abs(r0 - r) < 100) {
                        pm.drawTo(c, r);
                    } else {
                        if (isFilled()) {
                            PF.finishPolygon();
                        }
                        pm.finishPolygon();
                    }
                    c0 = c;
                    r0 = r;
                }
                pm.finishPolygon();
            }
        }
        e = V.elements();
        if (indicated()) {
            final boolean sel = P.indicated();
            P.setIndicated(true);
            g.setColor(P);
            P.setIndicated(sel);
        } else {
            g.setColor(this);
        }
        if (Discrete) {
            final int col = getColorIndex();
            final int th = getColorType();
            if (e.hasMoreElements()) {
                C = (Coordinates) e.nextElement();
                c0 = zc.col(C.X);
                r0 = zc.row(C.Y);
                PF.drawTo(c0, r0);
                while (e.hasMoreElements()) {
                    C = (Coordinates) e.nextElement();
                    c = zc.col(C.X);
                    r = zc.row(C.Y);
                    if (Math.abs(c0 - c) < 100 && Math.abs(r0 - r) < 100) {
                        if (isFilled()) {
                            PF.drawTo(c, r);
                        }
                    } else {
                        if (isFilled()) {
                            PF.finishPolygon();
                        }
                    }
                    if (C.Color >= 0) {
                        setColor(C.Color);
                    }
                    if (C.Thickness >= 0) {
                        setColorType(C.Thickness);
                    }
                    PointObject.drawPoint(g, zc, this, C.X, C.Y, Type);
                    setColor(col);
                    setColorType(th);
                    c0 = c;
                    r0 = r;
                }
                if (isFilled()) {
                    PF.finishPolygon();
                }
            }
            for (int i = 0; i < PN; i++) {
                e = VO[i].elements();
                g.setColor(PO[i]);
                if (e.hasMoreElements()) {
                    C = (Coordinates) e.nextElement();
                    while (e.hasMoreElements()) {
                        C = (Coordinates) e.nextElement();
                        if (C.Color >= 0) {
                            setColor(C.Color);
                        }
                        if (C.Thickness >= 0) {
                            setColorType(C.Thickness);
                        }
                        PointObject.drawPoint(g, zc, this, C.X, C.Y, Type);
                        setColor(col);
                        setColorType(th);
                    }
                }
            }
        } else {
            PolygonDrawer pd = new PolygonDrawer(g, this);
            if (isFilled()) {
                if (PF == null) {
                    PF = new PolygonFiller(g, this);
                }
                PF.start();
                PF.setGraphics(g);
            }
            if (e.hasMoreElements()) {
                C = (Coordinates) e.nextElement();
                c0 = zc.col(C.X);
                r0 = zc.row(C.Y);
                pd.drawTo(c0, r0);
                while (e.hasMoreElements()) {
                    C = (Coordinates) e.nextElement();
                    c = zc.col(C.X);
                    r = zc.row(C.Y);
                    if (Math.abs(c0 - c) < 100 && Math.abs(r0 - r) < 100) {
                        if (isFilled()) {
                            PF.drawTo(c, r);
                        }
                        pd.drawTo(c, r);
                    } else {
                        if (isFilled()) {
                            PF.finishPolygon();
                        }
                        pd.finishPolygon();
                    }
                    c0 = c;
                    r0 = r;
                }
                if (isFilled()) {
                    PF.finishPolygon();
                }
                pd.finishPolygon();
            }
            for (int i = 0; i < PN; i++) {
                e = VO[i].elements();
                g.setColor(PO[i]);
                pd = new PolygonDrawer(g, PO[i]);
                if (e.hasMoreElements()) {
                    C = (Coordinates) e.nextElement();
                    c0 = zc.col(C.X);
                    r0 = zc.row(C.Y);
                    while (e.hasMoreElements()) {
                        C = (Coordinates) e.nextElement();
                        c = zc.col(C.X);
                        r = zc.row(C.Y);
                        if (Math.abs(c0 - c) < 100 && Math.abs(r0 - r) < 100) {
                            pd.drawTo(c, r);
                        } else {
                            pd.finishPolygon();
                        }
                        c0 = c;
                        r0 = r;
                    }
                }
                pd.finishPolygon();
            }
        }
    }

    public double getSum(final double x, final double y) throws ConstructionException {
        if (!Valid) {
            throw new ConstructionException("");
        }
        double sum = 0;
        boolean started = false;
        double x0 = 0, y0 = 0;
        final Enumeration e = V.elements();
        while (e.hasMoreElements()) {
            final Coordinates C = (Coordinates) e.nextElement();
            if (started) {
                sum += (-(C.X - x) * (y0 - y) + (C.Y - y) * (x0 - x)) / 2;
            }
            x0 = C.X;
            y0 = C.Y;
            started = true;
        }
        return sum;
    }

    public double getSum() throws ConstructionException {
        if (!Valid) {
            throw new ConstructionException("");
        }
        double sum = 0;
        boolean started = false;
        double x0 = 0, y0 = 0, x = 0, y = 0;
        final Enumeration e = V.elements();
        while (e.hasMoreElements()) {
            final Coordinates C = (Coordinates) e.nextElement();
            if (started) {
                sum += (-(C.X - x) * (y0 - y) + (C.Y - y) * (x0 - x)) / 2;
            } else {
                x = C.X;
                y = C.Y;
            }
            x0 = C.X;
            y0 = C.Y;
            started = true;
        }
        return sum;
    }

    public double getLength() throws ConstructionException {
        if (!Valid) {
            throw new ConstructionException("");
        }
        double sum = 0;
        boolean started = false;
        double x0 = 0, y0 = 0;
        final Enumeration e = V.elements();
        while (e.hasMoreElements()) {
            final Coordinates C = (Coordinates) e.nextElement();
            if (started) {
                sum += Math.sqrt((C.X - x0) * (C.X - x0) + (C.Y - y0) * (C.Y - y0));
            }
            x0 = C.X;
            y0 = C.Y;
            started = true;
        }
        return sum;
    }

    @Override
    public void printArgs(final XmlWriter xml) {
        super.printArgs(xml);
        if (PM != null) {
            xml.printArg("point", PM.getName());
        }
        xml.printArg("on", O.getName());
        xml.printArg("track", P.getName());
        xml.printArg("dmin", "" + DMin);
        for (int i = 0; i < PN; i++) {
            xml.printArg("track" + i, PO[i].getName());
        }
        if (Filled) {
            xml.printArg("filled", "true");
        }
        if (Fixed) {
            xml.printArg("fixed", "true");
        }
        if (Discrete) {
            xml.printArg("discrete", "true");
        }
        printType(xml);
    }

    public void printType(final XmlWriter xml) {
        if (Type != 0) {
            switch(Type) {
                case PointObject.DIAMOND:
                    xml.printArg("shape", "diamond");
                    break;
                case PointObject.CIRCLE:
                    xml.printArg("shape", "circle");
                    break;
                case PointObject.DOT:
                    xml.printArg("shape", "dot");
                    break;
                case PointObject.CROSS:
                    xml.printArg("shape", "cross");
                    break;
                case PointObject.DCROSS:
                    xml.printArg("shape", "dcross");
                    break;
            }
        }
    }

    @Override
    public Enumeration depending() {
        super.depending();
        if (PM != null) {
            DL.add(PM);
        }
        DL.add(O);
        DL.add(P);
        for (int i = 0; i < PN; i++) {
            DL.add(PO[i]);
        }
        return DL.elements();
    }

    @Override
    public boolean equals(final ConstructionObject o) {
        return false;
    }

    @Override
    public void translate() {
        if (PM != null) {
            PM = (PointObject) PM.getTranslation();
        }
        O = O.getTranslation();
        P = P.getTranslation();
    }

    @Override
    public boolean maybeTransparent() {
        return false;
    }

    public Vector V = new Vector();

    public Vector VO[] = new Vector[PMax];

    @Override
    public boolean nearto(final int x, final int y, final ZirkelCanvas zc) {
        if (!displays(zc)) {
            return false;
        }
        final int size = (int) zc.selectionSize();
        final Enumeration e = V.elements();
        while (e.hasMoreElements()) {
            final Coordinates c = (Coordinates) e.nextElement();
            final double col = zc.col(c.X), row = zc.row(c.Y);
            if (Math.max(Math.abs(col - x), Math.abs(row - y)) < size) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onlynearto(final int c, final int r, final ZirkelCanvas zc) {
        return false;
    }

    public double da = 0;

    double oldx, oldy;

    public double X, Y, DX, DY;

    double XO[] = new double[PMax], YO[] = new double[PMax], DXO[] = new double[PMax], DYO[] = new double[PMax];

    int Omit = 1;

    public double mod(final double x) {
        if (x >= Math.PI) {
            return x - 2 * Math.PI;
        }
        if (x < -Math.PI) {
            return x + 2 * Math.PI;
        }
        return x;
    }

    public boolean DontProject = false;

    public double DMin = 0.005;

    /**
	 * Complicated procedure to recompute the automatic track. In principle, a
	 * moving point moves on some object, the coordinates of the tracked points
	 * or the intersections of the tracked lines are stored, as well as the
	 * positions of the moved point.
	 * 
	 * But if the tracked point gets invalid, the movement reverses and the
	 * interesections change.
	 * 
	 * Moreover, there is a list of secondary tracked objects.
	 * 
	 * @param zc
	 */
    public synchronized void docompute(final ZirkelCanvas zc) {
        V = new Vector();
        for (int i = 0; i < PN; i++) {
            VO[i] = new Vector();
        }
        if (O instanceof PrimitiveCircleObject) {
            zc.getConstruction().shouldSwitch(false);
            final PrimitiveCircleObject c = (PrimitiveCircleObject) O;
            final double x = c.getX(), y = c.getY(), r = c.getR();
            PM.project(c);
            double amin = 0, amax = 0, astart = 0, anull = 0;
            final double dmax = 0.5;
            boolean range = false;
            if (c.hasRange()) {
                range = true;
                final double a1 = c.getA1();
                final double a2 = c.getA2();
                double d = a2 - a1;
                while (d < 0) {
                    d += 2 * Math.PI;
                }
                while (d >= 2 * Math.PI) {
                    d -= 2 * Math.PI;
                }
                amin = astart = -d / 2;
                amax = d / 2;
                anull = (a1 + a2) / 2;
            } else {
                amin = astart = -Math.PI * 0.9999;
                amax = Math.PI * 0.9999;
            }
            double a = astart;
            PM.move(x + r * Math.cos(anull + a), y + r * Math.sin(anull + a));
            PM.project(c);
            zc.getConstruction().validate(this, PM);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    addCoordinates(V, P, x1, y1);
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO);
            final double dmin = DMin;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                PM.move(x + r * Math.cos(anull + a), y + r * Math.sin(anull + a));
                PM.project(c);
                zc.getConstruction().validate(this, PM);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    double x2 = 0, y2 = 0;
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        addCoordinates(V, P, x2, y2);
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    da = -da;
                }
                addSecondary(startedO);
                if (Break || System.currentTimeMillis() - time > 1000) {
                    break;
                }
            }
        } else if (O instanceof PrimitiveLineObject) {
            zc.getConstruction().shouldSwitch(false);
            final PrimitiveLineObject l = (PrimitiveLineObject) O;
            PM.project(l);
            final double lx = l.getX(), ly = l.getY(), ldx = l.getDX(), ldy = l.getDY();
            double amin = 0, amax = 0, astart = 0;
            double dmax = 0.5;
            boolean range = false;
            if (l instanceof RayObject) {
                amin = astart = 0;
                amax = Math.PI * 0.9999;
                range = true;
            } else if (l instanceof SegmentObject) {
                amin = astart = 0;
                final double r = ((SegmentObject) l).getLength();
                dmax = r / 20;
                amax = Math.atan(r) * 2;
                range = true;
            } else {
                amin = astart = -Math.PI * 0.99999;
                amax = Math.PI * 0.9999;
            }
            double a = astart;
            double hd = Math.tan(mod(a) / 2);
            PM.move(lx + hd * ldx, ly + hd * ldy);
            PM.project(l);
            zc.getConstruction().validate(this, PM);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    addCoordinates(V, P, x1, y1);
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO);
            final double dmin = DMin;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                hd = Math.tan(mod(a) / 2);
                PM.move(lx + hd * ldx, ly + hd * ldy);
                PM.project(l);
                zc.getConstruction().validate(this, PM);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    double x2 = 0, y2 = 0;
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        addCoordinates(V, P, x2, y2);
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    da = -da;
                }
                addSecondary(startedO);
                if (Break || System.currentTimeMillis() - time > 1000) {
                    break;
                }
            }
        } else if (O instanceof ExpressionObject) {
            zc.getConstruction().shouldSwitch(false);
            final ExpressionObject eo = (ExpressionObject) O;
            if (!eo.isSlider()) {
                return;
            }
            double amin = 0, amax = 0, astart = 0;
            double dmax = 0.5;
            boolean range = false;
            amin = astart = 0;
            final double r = 1;
            dmax = r / 20;
            amax = r;
            range = true;
            double a = astart;
            double hd = a;
            eo.setSliderPosition(0);
            zc.getConstruction().validate(P, null);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    addCoordinates(V, P, x1, y1);
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO);
            final double dmin = DMin;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            double x2 = 0, y2 = 0;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                hd = a;
                eo.setSliderPosition(hd);
                zc.getConstruction().validate(P, null);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        addCoordinates(V, P, x2, y2);
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    addCoordinates(V, P, x2, y2);
                    da = -da;
                }
                addSecondary(startedO);
                if (Break || System.currentTimeMillis() - time > 1000) {
                    break;
                }
            }
        }
    }

    public synchronized void compute(final ZirkelCanvas zc) {
        if (Fixed && !StartFix) {
            return;
        }
        StartFix = false;
        double x = 0, y = 0;
        if (PM != null) {
            x = PM.getX();
            y = PM.getY();
        } else if (O instanceof ExpressionObject && ((ExpressionObject) O).isSlider()) {
            x = ((ExpressionObject) O).getSliderPosition();
        }
        zc.getConstruction().clearSwitches();
        zc.getConstruction().shouldSwitch(true);
        DontProject = true;
        docompute(zc);
        DontProject = false;
        zc.getConstruction().shouldSwitch(false);
        zc.getConstruction().clearSwitches();
        if (PM != null) {
            PM.move(x, y);
        } else if (O instanceof ExpressionObject && ((ExpressionObject) O).isSlider()) {
            ((ExpressionObject) O).setSliderPosition(x);
        }
        final Enumeration e = V.elements();
        if (e.hasMoreElements()) {
            Coordinates c = (Coordinates) e.nextElement();
            x = c.X;
            y = c.Y;
            double col1 = zc.col(c.X);
            double row1 = zc.row(c.Y);
            while (e.hasMoreElements()) {
                c = (Coordinates) e.nextElement();
                final double col2 = zc.col(c.X);
                final double row2 = zc.row(c.Y);
                c.flag = Math.abs(col2 - col1) < 100 && Math.abs(row2 - row1) < 100;
                row1 = row2;
                col1 = col2;
            }
        }
        zc.dovalidate();
    }

    public void addSecondary(final boolean startedO[]) {
        for (int i = 0; i < PN; i++) {
            if (!PO[i].valid()) {
                continue;
            }
            if (PO[i] instanceof PointObject) {
                final PointObject p = (PointObject) PO[i];
                addCoordinates(VO[i], p, p.getX(), p.getY());
                VO[i].addElement(new Coordinates(p.getX(), p.getY()));
            } else if (PO[i] instanceof PrimitiveLineObject) {
                final PrimitiveLineObject L = (PrimitiveLineObject) PO[i];
                if (!startedO[i]) {
                    XO[i] = L.getX();
                    YO[i] = L.getY();
                    DXO[i] = L.getDX();
                    DYO[i] = L.getDY();
                    startedO[i] = true;
                } else {
                    double xx, yy, dx, dy;
                    xx = L.getX();
                    yy = L.getY();
                    dx = L.getDX();
                    dy = L.getDY();
                    final double det = dx * DYO[i] - dy * DXO[i];
                    if (Math.sqrt(Math.abs(det)) > 1e-9) {
                        final double h = (-(XO[i] - xx) * DYO[i] + DXO[i] * (YO[i] - yy)) / (-det);
                        XO[i] = xx;
                        YO[i] = yy;
                        DXO[i] = dx;
                        DYO[i] = dy;
                        addCoordinates(VO[i], PO[i], xx + h * dx, yy + h * dy);
                    }
                }
            }
        }
    }

    public void addCoordinates(final Vector v, final ConstructionObject p, final double x, final double y) {
        final Coordinates C = new Coordinates(x, y);
        if (Discrete && p instanceof PointObject) {
            C.Color = p.getColorIndex();
            C.Thickness = p.getColorType();
        }
        v.addElement(C);
    }

    public void addCoordinates(final Vector v, final ConstructionObject p) {
        v.addElement(new Coordinates(p.getX(), p.getY()));
    }

    public void addCoordinates(final Vector v, final double x, final double y) {
        v.addElement(new Coordinates(x, y));
    }

    public double StepSize = 5;

    public double updateDA(double da, final boolean valid, final double dist, final double dp, final double dmin, final double dmax, final ZirkelCanvas zc) {
        if (V.size() > 0 && valid) {
            if (dist < 1.2) {
                if (dp > zc.dx(StepSize)) {
                    da /= 2;
                } else if (dp < zc.dx(StepSize / 2)) {
                    da *= 2;
                }
                if (da > 0 && da < dmin) {
                    da = dmin;
                } else if (da < 0 && da > -dmin) {
                    da = -dmin;
                }
                if (da > dmax) {
                    da = dmax;
                } else if (da < -dmax) {
                    da = -dmax;
                }
            } else {
                if (dp > zc.dx(StepSize * 10)) {
                    da /= 2;
                } else if (dp < zc.dx(StepSize * 5)) {
                    da *= 2;
                }
                if (da > 0 && da < dmin) {
                    da = dmin;
                } else if (da < 0 && da > -dmin) {
                    da = -dmin;
                }
                if (da > dmax) {
                    da = dmax;
                } else if (da < -dmax) {
                    da = -dmax;
                }
            }
        }
        return da;
    }

    @Override
    public boolean hasUnit() {
        return false;
    }

    /**
	 * Project a point to this object.
	 */
    public synchronized void project(final PointObject P) {
        if (DontProject) {
            return;
        }
        final Enumeration e = V.elements();
        double x = 0, y = 0, x0 = 0, y0 = 0, dmin = 0;
        boolean started = false;
        while (e.hasMoreElements()) {
            final Coordinates c = (Coordinates) e.nextElement();
            final double x1 = c.X;
            final double y1 = c.Y;
            if (!started) {
                dmin = Math.sqrt((P.getX() - x1) * (P.getX() - x1) + (P.getY() - y1) * (P.getY() - y1));
                x0 = x = x1;
                y0 = y = y1;
                started = true;
            } else {
                if (c.flag) {
                    double h = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0);
                    if (h < 1e-10) {
                        h = 1e-10;
                    }
                    double g = (P.getX() - x0) * (x1 - x0) + (P.getY() - y0) * (y1 - y0);
                    if (g < 0) {
                        g = 0;
                    }
                    if (g > h) {
                        g = h;
                    }
                    final double x2 = x0 + g / h * (x1 - x0), y2 = y0 + g / h * (y1 - y0);
                    final double d = Math.sqrt((P.getX() - x2) * (P.getX() - x2) + (P.getY() - y2) * (P.getY() - y2));
                    if (d < dmin) {
                        dmin = d;
                        x = x2;
                        y = y2;
                    }
                }
                x0 = x1;
                y0 = y1;
            }
        }
        if (started) {
            P.setXY(x, y);
            P.Valid = true;
        } else {
            P.Valid = false;
        }
    }

    public synchronized int getDistance(final PointObject P) {
        final Enumeration e = V.elements();
        double x = 0, y = 0, x0 = 0, y0 = 0, dmin = 0;
        boolean started = false;
        while (e.hasMoreElements()) {
            final Coordinates c = (Coordinates) e.nextElement();
            final double x1 = c.X;
            final double y1 = c.Y;
            if (!started) {
                dmin = Math.sqrt((P.getX() - x1) * (P.getX() - x1) + (P.getY() - y1) * (P.getY() - y1));
                x0 = x = x1;
                y0 = y = y1;
                started = true;
            } else {
                if (c.flag) {
                    double h = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0);
                    if (h < 1e-10) {
                        h = 1e-10;
                    }
                    double g = (P.getX() - x0) * (x1 - x0) + (P.getY() - y0) * (y1 - y0);
                    if (g < 0) {
                        g = 0;
                    }
                    if (g > h) {
                        g = h;
                    }
                    final double x2 = x0 + g / h * (x1 - x0), y2 = y0 + g / h * (y1 - y0);
                    final double d = Math.sqrt((P.getX() - x2) * (P.getX() - x2) + (P.getY() - y2) * (P.getY() - y2));
                    if (d < dmin) {
                        dmin = d;
                        x = x2;
                        y = y2;
                    }
                }
                x0 = x1;
                y0 = y1;
            }
        }
        if (started) {
            P.Valid = true;
            final double dd = Math.sqrt((P.getX() - x) * (P.getX() - x) + (P.getY() - y) * (P.getY() - y));
            return (int) Math.round(dd * Cn.getPixel());
        } else {
            P.Valid = false;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public String getDisplayValue() {
        try {
            return "" + getSum();
        } catch (final Exception e) {
            return "???";
        }
    }

    public void project(final PointObject P, final double alpha) {
        project(P);
    }

    @Override
    public void edit(final ZirkelCanvas zc) {
        if (!rene.zirkel.Zirkel.IsApplet) {
            eric.JGlobals.EditObject(this);
            return;
        }
        final ObjectEditDialog d = new TrackEditDialog(zc, this);
        d.setVisible(true);
        zc.repaint();
        validate();
        if (d.wantsMore()) {
            new EditConditionals(zc.getFrame(), this);
            validate();
        }
    }

    @Override
    public boolean canDisplayName() {
        return false;
    }

    public boolean Fixed = false, StartFix;

    @Override
    public boolean isFixed() {
        return Fixed;
    }

    @Override
    public void setFixed(final boolean f) {
        Fixed = f;
        if (Fixed) {
            StartFix = true;
        }
    }

    public void setDMin(final double dmin) {
        DMin = dmin;
    }

    public double getDMin() {
        return DMin;
    }

    @Override
    public int getType() {
        return Type;
    }

    @Override
    public void setType(final int type) {
        Type = type;
    }

    public boolean isDiscrete() {
        return Discrete;
    }

    public void setDiscrete(final boolean discrete) {
        Discrete = discrete;
    }

    public boolean canInteresectWith(final ConstructionObject o) {
        return true;
    }

    public void repulse(final PointObject P) {
        project(P);
    }
}
