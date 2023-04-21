package rene.zirkel.objects;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.util.Enumeration;
import javax.swing.JPanel;
import rene.gui.Global;
import rene.gui.IconBar;
import rene.gui.MyLabel;
import rene.gui.TextFieldAction;
import rene.util.xml.XmlWriter;
import rene.zirkel.Zirkel;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.dialogs.EditConditionals;
import rene.zirkel.dialogs.ObjectEditDialog;
import rene.zirkel.graphics.MyGraphics;
import rene.zirkel.graphics.MyGraphics13;
import rene.zirkel.structures.Coordinates;

class LineEditDialog extends ObjectEditDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    TextField Length;

    Checkbox Fixed, Restricted;

    IconBar IC;

    public LineEditDialog(final Frame f, final PrimitiveLineObject o) {
        super(f, Zirkel.name("edit.line.title"), o);
    }

    @Override
    public void addFirst(final JPanel P) {
        final PrimitiveLineObject l = (PrimitiveLineObject) O;
        if (l instanceof SegmentObject) {
            Length = new TextFieldAction(this, "Length", ((SegmentObject) l).getStringLength(), 30);
            if (((SegmentObject) l).canFix()) {
                Fixed = new Checkbox("");
                Fixed.setState(((SegmentObject) l).fixed());
            } else {
                Length.setEditable(false);
            }
        }
        if ((l instanceof PlumbObject) && ((PlumbObject) l).canRestrict()) {
            Restricted = new Checkbox("");
            Restricted.setState(((PlumbObject) l).isRestricted());
        }
        if (Length != null) {
            P.add(new MyLabel(Zirkel.name("edit.segment.length")));
            P.add(Length);
        }
        if (Fixed != null) {
            P.add(new MyLabel(Zirkel.name("edit.fixed")));
            P.add(Fixed);
        }
        if (Restricted != null) {
            P.add(new MyLabel(Zirkel.name("edit.plumb.restricted")));
            P.add(Restricted);
        }
    }

    @Override
    public void addSecond(final JPanel P) {
        final PrimitiveLineObject l = (PrimitiveLineObject) O;
        IC = new IconBar(F);
        IC.setIconBarListener(this);
        if (!(l instanceof SegmentObject)) {
            IC.addToggleLeft("plines");
            IC.setState("plines", l.isPartial());
        }
        if (l instanceof SegmentObject) {
            IC.addToggleLeft("arrow");
            IC.setState("arrow", ((SegmentObject) l).isArrow());
        }
        P.add(new MyLabel(""));
        P.add(IC);
    }

    @Override
    public boolean showsValue() {
        return Length != null;
    }

    @Override
    public void doAction(final String o) {
        if (o.equals("Length") && Fixed != null) {
            Fixed.setState(true);
            super.doAction("OK");
        } else {
            super.doAction(o);
        }
    }

    @Override
    public void setAction() {
        if (Fixed != null) {
            try {
                ((SegmentObject) O).setFixed(Fixed.getState(), Length.getText());
            } catch (final Exception e) {
            }
        }
        if (Restricted != null) {
            ((PlumbObject) O).setRestricted(Restricted.getState());
        }
        if (!(O instanceof SegmentObject)) {
            ((PrimitiveLineObject) O).setPartial(IC.getState("plines"));
        } else if (O instanceof SegmentObject) {
            ((SegmentObject) O).setArrow(IC.getState("arrow"));
            Global.setParameter("unit.length", Unit.getText());
        }
    }

    @Override
    public void focusGained(final FocusEvent e) {
        if (Fixed != null && Fixed.getState()) {
            Length.requestFocus();
        } else {
            super.focusGained(e);
        }
    }
}

public class PrimitiveLineObject extends ConstructionObject implements PointonObject {

    protected double X1, Y1, DX, DY;

    protected PointObject P1;

    PointObject Dep[];

    int NDep;

    boolean Partial = false;

    public PrimitiveLineObject(final Construction c) {
        super(c);
        setColor(ColorIndex, SpecialColor);
    }

    public PrimitiveLineObject(final Construction c, final String Nme) {
        super(c, Nme);
        setColor(ColorIndex, SpecialColor);
    }

    public void setP1DXDY(final PointObject p, final double dx, final double dy) {
        P1 = p;
        DX = dx;
        DY = dy;
        X1 = P1.getX();
        Y1 = P1.getY();
    }

    @Override
    public void setDefaults() {
        setShowName(Global.getParameter("options.line.shownames", false));
        setShowValue(Global.getParameter("options.line.showvalues", false));
        setColor(Global.getParameter("options.line.color", 0), Global.getParameter("options.line.pcolor", (Color) null));
        setColorType(Global.getParameter("options.line.colortype", 0));
        setHidden(Cn.Hidden);
        setObtuse(Cn.Obtuse);
        setSolid(Cn.Solid);
        setLarge(Global.getParameter("options.line.large", false));
        setBold(Global.getParameter("options.line.bold", false));
    }

    @Override
    public void setTargetDefaults() {
        setShowName(Global.getParameter("options.line.shownames", false));
        setShowValue(Global.getParameter("options.line.showvalues", false));
        setColor(Global.getParameter("options.line.color", 0), Global.getParameter("options.line.pcolor", (Color) null));
        setColorType(Global.getParameter("options.line.colortype", 0));
        setLarge(Global.getParameter("options.line.large", false));
        setBold(Global.getParameter("options.line.bold", false));
    }

    @Override
    public String getTag() {
        return "Line";
    }

    double k1, k2;

    boolean k12valid = false;

    @Override
    public void paint(final MyGraphics g, final ZirkelCanvas zc) {
        if (!Valid || mustHide(zc)) {
            return;
        }
        final double xm = (zc.minX() + zc.maxX()) / 2, ym = (zc.minY() + zc.maxY()) / 2;
        final double d = (xm - X1) * DY - (ym - Y1) * DX;
        final double x = xm - d * DY, y = ym + d * DX;
        final double a = Math.max(zc.maxX() - zc.minX(), zc.maxY() - zc.minY());
        if (Math.abs(d) > a) {
            return;
        }
        final double b = (x - X1) * DX + (y - Y1) * DY;
        k1 = b - a;
        k2 = b + a;
        k12valid = true;
        if (Partial && Dep != null && !zc.showHidden()) {
            final double dd = (zc.maxX() - zc.minX()) / 20;
            double dmin = -dd, dmax = +dd;
            if (Dep != null) {
                for (int i = 0; i < NDep; i++) {
                    if (!Dep[i].valid() || Dep[i].mustHide(zc)) {
                        continue;
                    }
                    final double s = project(Dep[i].getX(), Dep[i].getY());
                    if (s - dd < dmin) {
                        dmin = s - dd;
                    } else if (s + dd > dmax) {
                        dmax = s + dd;
                    }
                }
            }
            if (k1 < dmin) {
                k1 = dmin;
            }
            if (k2 > dmax) {
                k2 = dmax;
            }
        }
        final double c1 = zc.col(X1 + k1 * DX), c2 = zc.col(X1 + k2 * DX), r1 = zc.row(Y1 + k1 * DY), r2 = zc.row(Y1 + k2 * DY);
        if (isStrongSelected() && g instanceof MyGraphics13) {
            ((MyGraphics13) g).drawMarkerLine(c1, r1, c2, r2);
        }
        g.setColor(this);
        if (tracked()) zc.UniversalTrack.drawTrackLine(this, c1, r1, c2, r2);
        g.drawLine(c1, r1, c2, r2, this);
        final String s = getDisplayText();
        if (!s.equals("")) {
            g.setLabelColor(this);
            setFont(g);
            DisplaysText = true;
            if (KeepClose) {
                final double side = (YcOffset < 0) ? 1 : -1;
                drawLabel(g, s, zc, X1 + XcOffset * DX, Y1 + XcOffset * DY, side * DX, side * DY, 0, 0);
            } else {
                drawLabel(g, s, zc, x + a / 5 * DX, y + a / 5 * DY, DX, DY, XcOffset, YcOffset);
            }
        }
    }

    @Override
    public boolean canKeepClose() {
        return true;
    }

    @Override
    public void setKeepClose(final double x, final double y) {
        KeepClose = true;
        XcOffset = (x - X1) * DX + (y - Y1) * DY;
        YcOffset = (x - X1) * DY - (y - Y1) * DX;
    }

    @Override
    public boolean nearto(final int c, final int r, final ZirkelCanvas zc) {
        if (!displays(zc)) {
            return false;
        }
        final double x = zc.x(c), y = zc.y(r);
        final double d = (x - X1) * DY - (y - Y1) * DX;
        Value = Math.abs(zc.col(zc.minX() + d) - zc.col(zc.minX()));
        return Value < zc.selectionSize() * 2;
    }

    @Override
    public boolean onlynearto(final int c, final int r, final ZirkelCanvas zc) {
        return false;
    }

    public static Coordinates intersect(final PrimitiveLineObject l1, final PrimitiveLineObject l2) {
        final double det = -l1.DX * l2.DY + l1.DY * l2.DX;
        if (Math.abs(det) < 1e-10) {
            return null;
        }
        final double a = (-(l2.X1 - l1.X1) * l2.DY + (l2.Y1 - l1.Y1) * l2.DX) / det;
        return new Coordinates(l1.X1 + a * l1.DX, l1.Y1 + a * l1.DY);
    }

    public static Coordinates intersect(final PrimitiveLineObject l, final PrimitiveCircleObject c) {
        double x = c.getX(), y = c.getY();
        final double r = c.getR();
        final double d = (x - l.X1) * l.DY - (y - l.Y1) * l.DX;
        if (Math.abs(d) > r + 1e-10) {
            return null;
        }
        x -= d * l.DY;
        y += d * l.DX;
        double h = r * r - d * d;
        if (h > 0) {
            h = Math.sqrt(h);
        } else {
            h = 0;
        }
        return new Coordinates(x + h * l.DX, y + h * l.DY, x - h * l.DX, y - h * l.DY);
    }

    public static Coordinates intersect(final PrimitiveLineObject l, final QuadricObject q) {
        final double M = -l.DY, N2 = l.DX, P = -(M * l.X1 + N2 * l.Y1);
        final double A = q.X[0], B = q.X[1], C = q.X[2], D = q.X[3], E = q.X[4], F = q.X[5];
        double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        if (N2 != 0) {
            final double part1 = -2 * B * M * P - C * N2 * N2 + D * M * N2 + E * N2 * P;
            final double part2 = Math.abs(N2) * Math.sqrt(-2 * M * D * N2 * C + 4 * P * D * A * N2 + 4 * P * M * B * C + 4 * E * M * N2 * F - 2 * E * P * N2 * C - 2 * E * P * M * D - 4 * M * M * B * F - 4 * P * P * A * B - 4 * A * N2 * N2 * F + N2 * N2 * C * C + M * M * D * D + E * E * P * P);
            final double part3 = 2 * A * N2 * N2 + 2 * B * M * M + (-2 * E) * M * N2;
            x1 = (part1 + part2) / part3;
            if (Double.isNaN(x1)) {
                return null;
            }
            y1 = (-M * x1 - P) / N2;
            x2 = (part1 - part2) / part3;
            y2 = (-M * x2 - P) / N2;
            if (((x2 - x1) / l.DX) < 0) {
                return new Coordinates(x2, y2, x1, y1);
            }
        } else {
            x1 = -P / M;
            x2 = x1;
            final double part1 = -D * M * M + E * M * P;
            final double part2 = Math.abs(M) * Math.sqrt(4 * P * M * B * C - 2 * E * P * M * D - 4 * M * M * B * F - 4 * P * P * A * B + M * M * D * D + E * E * P * P);
            final double part3 = 2 * M * M * B;
            y1 = (part1 + part2) / part3;
            if (Double.isNaN(y1)) {
                return null;
            }
            y2 = (part1 - part2) / part3;
            if (((y2 - y1) / l.DY) < 0) {
                return new Coordinates(x2, y2, x1, y1);
            }
        }
        return new Coordinates(x1, y1, x2, y2);
    }

    public double getDX() {
        return DX;
    }

    public double getDY() {
        return DY;
    }

    @Override
    public double getX() {
        return X1;
    }

    @Override
    public double getY() {
        return Y1;
    }

    @Override
    public void edit(final ZirkelCanvas zc) {
        final ObjectEditDialog d = new LineEditDialog(zc.getFrame(), this);
        d.setVisible(true);
        zc.getConstruction().updateCircleDep();
        zc.repaint();
        if (d.wantsMore()) {
            new EditConditionals(zc.getFrame(), this);
            validate();
        }
    }

    @Override
    public String getEquation() {
        double y = DX, x = -DY;
        double c = y * Y1 + x * X1;
        if (c < 0) {
            c = -c;
            x = -x;
            y = -y;
        }
        if (Math.abs(x) < 1e-10 && y < 0) {
            c = -c;
            x = -x;
            y = -y;
        } else if (Math.abs(y) < 1e-10 && x < 0) {
            c = -c;
            x = -x;
            y = -y;
        }
        final String s = helpDisplayValue(true, x, "x");
        return s + helpDisplayValue(s.equals(""), y, "y") + "=" + ((Math.abs(c) < 1e-10) ? "0" : helpDisplayNumber(true, c));
    }

    /**
	 * Test, if the projection of (x,y) to the line contains that point.
	 */
    public boolean contains(final double x, final double y) {
        return true;
    }

    @Override
    public void printArgs(final XmlWriter xml) {
        if (Partial) {
            xml.printArg("partial", "true");
        }
    }

    public double project(final double x, final double y) {
        return (x - X1) * DX + (y - Y1) * DY;
    }

    @Override
    public boolean equals(final ConstructionObject o) {
        if (!(o instanceof PrimitiveLineObject) || !o.valid()) {
            return false;
        }
        final PrimitiveLineObject l = (PrimitiveLineObject) o;
        return equals(DX * l.DY - DY * l.DX, 0) && equals((X1 - l.X1) * DY - (Y1 - l.Y1) * DX, 0);
    }

    public PointObject getP1() {
        return P1;
    }

    public Enumeration points() {
        return depending();
    }

    @Override
    public boolean locallyLike(final ConstructionObject o) {
        if (!(o instanceof PrimitiveLineObject)) {
            return false;
        }
        return (equals(DX, ((PrimitiveLineObject) o).DX) && equals(DY, ((PrimitiveLineObject) o).DY)) || (equals(-DX, ((PrimitiveLineObject) o).DX) && equals(-DY, ((PrimitiveLineObject) o).DY));
    }

    @Override
    public boolean isPartial() {
        return Partial;
    }

    @Override
    public void setPartial(final boolean partial) {
        if (Partial == partial) {
            return;
        }
        Partial = partial;
        if (partial) {
            Dep = new PointObject[16];
            NDep = 0;
        } else {
            Dep = null;
        }
    }

    public void addDep(final PointObject p) {
        if (!Partial || Dep == null || NDep >= Dep.length) {
            return;
        }
        Dep[NDep++] = p;
    }

    @Override
    public void clearCircleDep() {
        NDep = 0;
    }

    @Override
    public int getDistance(final PointObject P) {
        final double h = project(P.getX(), P.getY());
        final double dx = P.getX() - (getX() + h * getDX());
        final double dy = P.getY() - (getY() + h * getDY());
        final double d = Math.sqrt(dx * dx + dy * dy);
        return (int) Math.round(d * Cn.getPixel());
    }

    public void project(final PointObject P) {
        final double h = project(P.getX(), P.getY());
        P.setXY(getX() + h * getDX(), getY() + h * getDY());
        P.setA(h);
    }

    public void project(final PointObject P, final double alpha) {
        P.setXY(getX() + alpha * getDX(), getY() + alpha * getDY());
    }

    public boolean canInteresectWith(final ConstructionObject o) {
        return true;
    }

    public void repulse(final PointObject P) {
        project(P);
    }
}
