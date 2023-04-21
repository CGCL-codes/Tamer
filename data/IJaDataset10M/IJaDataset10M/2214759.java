package rene.zirkel.objects;

import java.awt.Color;
import java.awt.Frame;
import java.util.Enumeration;
import rene.dialogs.Warning;
import rene.gui.Global;
import rene.util.xml.XmlWriter;
import rene.zirkel.Zirkel;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.construction.ConstructionException;
import rene.zirkel.construction.Count;
import rene.zirkel.expression.Expression;
import rene.zirkel.expression.InvalidException;
import rene.zirkel.graphics.MyGraphics;
import rene.zirkel.graphics.MyGraphics13;
import eric.JGlobals;

/**
 * @author Rene Class for segments, derived from LineObject, TwoPointLineObject.
 *         Segments override various methods from lines. They have a length.
 *         Also the length can be fixed.
 */
public class SegmentObject extends TwoPointLineObject {

    static Count N = new Count();

    protected boolean Fixed = false;

    Expression E;

    boolean ExpressionFailed;

    boolean Arrow = false;

    int code_symbol = 0;

    public SegmentObject(final Construction c, final PointObject p1, final PointObject p2) {
        super(c, p1, p2);
        validate();
        updateText();
        Unit = Global.getParameter("unit.length", "");
    }

    @Override
    public String getTag() {
        return "Segment";
    }

    @Override
    public int getN() {
        return N.next();
    }

    @Override
    public void setDefaults() {
        Arrow = Cn.Vectors;
        setShowName(Global.getParameter("options.segment.shownames", false));
        setShowValue(Global.getParameter("options.segment.showvalues", false));
        setColor(Global.getParameter("options.segment.color", 0), Global.getParameter("options.segment.pcolor", (Color) null));
        setColorType(Global.getParameter("options.segment.colortype", 0));
        setHidden(Cn.Hidden);
        setObtuse(Cn.Obtuse);
        setSolid(Cn.Solid);
        setLarge(Global.getParameter("options.segment.large", false));
        setBold(Global.getParameter("options.segment.bold", false));
    }

    @Override
    public void setTargetDefaults() {
        setShowName(Global.getParameter("options.segment.shownames", false));
        setShowValue(Global.getParameter("options.segment.showvalues", false));
        setColor(Global.getParameter("options.segment.color", 0), Global.getParameter("options.segment.pcolor", (Color) null));
        setColorType(Global.getParameter("options.segment.colortype", 0));
        setLarge(Global.getParameter("options.segment.large", false));
        setBold(Global.getParameter("options.segment.bold", false));
    }

    @Override
    public void updateText() {
        if (!Fixed) {
            setText(text2(Zirkel.name("text.segment"), P1.getName(), P2.getName()));
        } else {
            if (E == null) {
                setText(text3(Zirkel.name("text.segment.fixed"), P1.getName(), P2.getName(), "" + round(R)));
            } else {
                setText(text3(Zirkel.name("text.segment.fixed"), P1.getName(), P2.getName(), "\"" + E.toString() + "\""));
            }
        }
    }

    @Override
    public void validate() {
        ExpressionFailed = false;
        if (!P1.valid() || !P2.valid()) {
            Valid = false;
            return;
        } else {
            Valid = true;
            X1 = P1.getX();
            Y1 = P1.getY();
            X2 = P2.getX();
            Y2 = P2.getY();
            DX = X2 - X1;
            DY = Y2 - Y1;
            R = Math.sqrt(DX * DX + DY * DY);
            if (Fixed && E != null) {
                try {
                    final double FixedR = E.getValue();
                    if (FixedR < 1e-8) {
                        R = 0;
                        ExpressionFailed = true;
                        Valid = false;
                        return;
                    }
                    boolean movefirst = P1.moveableBy(this), movesecond = P2.moveableBy(this);
                    if (P2.getBound() != null) {
                        final ConstructionObject bound = P2.getBound();
                        if (bound instanceof RayObject) {
                            if (((RayObject) bound).getP1() == P1) {
                                movesecond = true;
                            }
                        }
                    } else if (P1.getBound() != null) {
                        final ConstructionObject bound = P1.getBound();
                        if (bound instanceof RayObject) {
                            if (((RayObject) bound).getP1() == P2) {
                                movefirst = true;
                                movesecond = false;
                            }
                        }
                    }
                    if (movesecond) {
                        if (R < 1e-10) {
                            P2.move(X1 + FixedR, Y1);
                        } else {
                            P2.move(X1 + FixedR * DX / R, Y1 + FixedR * DY / R);
                        }
                        P1.setUseAlpha(false);
                    } else if (movefirst) {
                        if (R < 1e-10) {
                            P1.move(X2 - FixedR, Y2);
                        } else {
                            P1.move(X2 - FixedR * DX / R, Y2 - FixedR * DY / R);
                        }
                        P2.setUseAlpha(false);
                    } else {
                        Fixed = false;
                    }
                    if (Fixed) {
                        X1 = P1.getX();
                        Y1 = P1.getY();
                        X2 = P2.getX();
                        Y2 = P2.getY();
                        DX = X2 - X1;
                        DY = Y2 - Y1;
                        R = Math.sqrt(DX * DX + DY * DY);
                        P2.movedBy(this);
                        P1.movedBy(this);
                    }
                } catch (final Exception e) {
                    ExpressionFailed = true;
                    Valid = false;
                    R = 0;
                    return;
                }
            }
            if (R < 1e-10) {
                R = 0;
                DX = 1;
                DY = 0;
            } else {
                DX /= R;
                DY /= R;
            }
        }
    }

    @Override
    public void paint(final MyGraphics g, final ZirkelCanvas zc) {
        if (!Valid || mustHide(zc)) {
            return;
        }
        final double c1 = zc.col(X1), r1 = zc.row(Y1), c2 = zc.col(X2), r2 = zc.row(Y2);
        if (visible(zc)) {
            if (isStrongSelected() && g instanceof MyGraphics13) {
                ((MyGraphics13) g).drawMarkerLine(c1, r1, c2, r2);
            }
            g.setColor(this);
            if (tracked()) {
                zc.UniversalTrack.drawTrackLine(this, c1, r1, c2, r2);
            }
            g.drawLine(c1, r1, c2, r2, this);
            if (code_symbol > 0) {
                final double rr = 7;
                final double dd = 3;
                final double ob = 2;
                final double cM = (c1 + c2) / 2, rM = (r1 + r2) / 2;
                final double A = c2 - cM, B = r2 - rM;
                final double sqrt2 = Math.sqrt(B * B + A * A);
                final double xx1 = -(rr * B) / sqrt2 + cM - ob * A / sqrt2;
                final double yy1 = (rr * A) / sqrt2 + rM - ob * B / sqrt2;
                final double xx2 = (rr * B) / sqrt2 + cM + ob * A / sqrt2;
                final double yy2 = -(rr * A) / sqrt2 + rM + ob * B / sqrt2;
                final double xt = dd * A / sqrt2, yt = dd * B / sqrt2;
                switch(code_symbol) {
                    case 1:
                        g.drawLine(xx1, yy1, xx2, yy2, this);
                        break;
                    case 2:
                        g.drawLine(xx1 - xt, yy1 - yt, xx2 - xt, yy2 - yt, this);
                        g.drawLine(xx1 + xt, yy1 + yt, xx2 + xt, yy2 + yt, this);
                        break;
                    case 3:
                        g.drawLine(xx1 - 2 * xt, yy1 - 2 * yt, xx2 - 2 * xt, yy2 - 2 * yt, this);
                        g.drawLine(xx1, yy1, xx2, yy2, this);
                        g.drawLine(xx1 + 2 * xt, yy1 + 2 * yt, xx2 + 2 * xt, yy2 + 2 * yt, this);
                        break;
                    case 4:
                        g.drawLine(xx1 - 3 * xt, yy1 - 3 * yt, xx2 - 3 * xt, yy2 - 3 * yt, this);
                        g.drawLine(xx1 - xt, yy1 - yt, xx2 - xt, yy2 - yt, this);
                        g.drawLine(xx1 + xt, yy1 + yt, xx2 + xt, yy2 + yt, this);
                        g.drawLine(xx1 + 3 * xt, yy1 + 3 * yt, xx2 + 3 * xt, yy2 + 3 * yt, this);
                        break;
                    case 5:
                        g.drawLine(xx1 - 2 * xt, yy1 - 2 * yt, xx2 + 2 * xt, yy2 + 2 * yt, this);
                        g.drawLine(xx1 + 2 * xt, yy1 + 2 * yt, xx2 - 2 * xt, yy2 - 2 * yt, this);
                        break;
                    case 6:
                        g.drawCircle(cM, rM, 2 * dd, this);
                        break;
                }
            }
            if (Arrow) {
                final double a = Math.PI * 0.9;
                final double r = zc.dx(zc.scale(Global.getParameter("arrowsize", 15)));
                final double[] cols = new double[3];
                cols[0] = c2;
                cols[1] = zc.col(X2 + (DX * Math.cos(a) + DY * Math.sin(a)) * r);
                cols[2] = zc.col(X2 + (DX * Math.cos(-a) + DY * Math.sin(-a)) * r);
                final double[] rows = new double[3];
                rows[0] = r2;
                rows[1] = zc.row(Y2 + (-DX * Math.sin(a) + DY * Math.cos(a)) * r);
                rows[2] = zc.row(Y2 + (-DX * Math.sin(-a) + DY * Math.cos(-a)) * r);
                g.fillPolygon(cols, rows, 3, true, false, this);
            }
        }
        final String s = getDisplayText();
        if (!s.equals("")) {
            g.setLabelColor(this);
            setFont(g);
            DisplaysText = true;
            if (KeepClose) {
                final double side = (YcOffset < 0) ? 1 : -1;
                drawLabel(g, s, zc, X1 + XcOffset * (X2 - X1), Y1 + XcOffset * (Y2 - Y1), side * DX, side * DY, 0, 0);
            } else {
                drawLabel(g, s, zc, (X1 + X2) / 2, (Y1 + Y2) / 2, DX, DY, XcOffset, YcOffset);
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
        XcOffset = (x - X1) / R * DX + (y - Y1) / R * DY;
        YcOffset = (x - X1) / R * DY - (y - Y1) / R * DX;
    }

    @Override
    public String getDisplayValue() {
        return JGlobals.getLocaleNumber(R, "lengths");
    }

    /**
	 * see, if a point is on the segment or near to it.
	 */
    @Override
    public boolean nearto(final int c, final int r, final ZirkelCanvas zc) {
        if (ExpressionFailed && P1.valid()) {
            return P1.nearto(c, r, zc);
        }
        if (ExpressionFailed && P2.valid()) {
            return P2.nearto(c, r, zc);
        }
        if (!displays(zc)) {
            return false;
        }
        final double x = zc.x(c), y = zc.y(r);
        double d = (x - X1) * DY - (y - Y1) * DX;
        final double o = (x - X1) * DX + (y - Y1) * DY, o1 = (X2 - X1) * DX + (Y2 - Y1) * DY;
        if (o1 > 0) {
            if (o > o1) {
                d = Math.sqrt((x - X2) * (x - X2) + (y - Y2) * (y - Y2));
            } else if (o < 0) {
                d = Math.sqrt((x - X1) * (x - X1) + (y - Y1) * (y - Y1));
            }
        } else {
            if (o < o1) {
                d = Math.sqrt((x - X2) * (x - X2) + (y - Y2) * (y - Y2));
            } else if (o > 0) {
                d = Math.sqrt((x - X1) * (x - X1) + (y - Y1) * (y - Y1));
            }
        }
        Value = Math.abs(zc.col(zc.minX() + d) - zc.col(zc.minX())) * 0.9;
        return Value < zc.selectionSize();
    }

    /**
	 * true, if the segment is too small.
	 */
    @Override
    public boolean onlynearto(final int c, final int r, final ZirkelCanvas zc) {
        return R < zc.dx(3 * (int) zc.pointSize());
    }

    @Override
    public void printArgs(final XmlWriter xml) {
        xml.printArg("from", P1.getName());
        xml.printArg("to", P2.getName());
        if (Fixed && E != null) {
            xml.printArg("fixed", E.toString());
        }
        if (Arrow) {
            xml.printArg("arrow", "true");
        }
        if (code_symbol > 0) {
            xml.printArg("code_symbol", "" + code_symbol);
        }
        super.printArgs(xml);
    }

    @Override
    public double getLength() {
        return R;
    }

    @Override
    public boolean fixed() {
        return Fixed;
    }

    @Override
    public void setFixed(final boolean flag, final String s) throws ConstructionException {
        if (!flag || s.equals("")) {
            Fixed = false;
            E = null;
        } else {
            E = new Expression(s, getConstruction(), this);
            if (!E.isValid()) {
                throw new ConstructionException(E.getErrorText());
            }
            Fixed = true;
        }
        updateText();
    }

    @Override
    public void round() {
        try {
            setFixed(true, getDisplayValue());
            validate();
        } catch (final Exception e) {
        }
    }

    /**
	 * @return Segment can be fixed in length.
	 */
    @Override
    public boolean canFix() {
        return P1.moveableBy(this) || P2.moveableBy(this);
    }

    @Override
    public boolean contains(final double x, final double y) {
        final double a = (x - X1) * DX + (y - Y1) * DY;
        if (a < -1e-9 || a > R + 1e-9) {
            return false;
        }
        return true;
    }

    @Override
    public double project(final double x, final double y) {
        final double h = super.project(x, y);
        if (h < 0) {
            return 0;
        }
        if (h > R) {
            return R;
        }
        return h;
    }

    /**
	 * @return true, if equal.
	 */
    @Override
    public boolean equals(final ConstructionObject o) {
        if (!(o instanceof SegmentObject) || !o.valid()) {
            return false;
        }
        final SegmentObject l = (SegmentObject) o;
        return (equals(X1, l.X1) && equals(X2, l.X2) && equals(Y1, l.Y1) && equals(Y2, l.Y2)) || (equals(X1, l.X2) && equals(Y1, l.Y2) && equals(X2, l.X1) && equals(Y2, l.Y1));
    }

    @Override
    public void edit(final ZirkelCanvas zc) {
        if (!rene.zirkel.Zirkel.IsApplet) {
            eric.JGlobals.EditObject(this);
            return;
        }
        super.edit(zc);
        if (E != null && !E.isValid()) {
            final Frame F = zc.getFrame();
            final Warning w = new Warning(F, E.getErrorText(), Zirkel.name("warning"), true);
            w.center(F);
            w.setVisible(true);
        }
    }

    public boolean isValidFix() {
        return E != null && E.isValid();
    }

    @Override
    public String getStringLength() {
        if (E != null) {
            return E.toString();
        } else {
            return "" + round(R);
        }
    }

    public int getSegmentCode() {
        return code_symbol;
    }

    public void setSegmentCode(final int i) {
        code_symbol = i;
    }

    @Override
    public double getValue() throws ConstructionException {
        if (!Valid) {
            throw new InvalidException("exception.invalid");
        } else {
            return R;
        }
    }

    @Override
    public void translate() {
        super.translate();
        try {
            setFixed(Fixed, E.toString());
            E.translate();
        } catch (final Exception e) {
            Fixed = false;
        }
    }

    @Override
    public Enumeration depending() {
        if (!Fixed || E == null) {
            return super.depending();
        } else {
            super.depending();
            final Enumeration e = E.getDepList().elements();
            while (e.hasMoreElements()) {
                DL.add((ConstructionObject) e.nextElement());
            }
            return DL.elements();
        }
    }

    public void setArrow(final boolean arrow) {
        Arrow = arrow;
    }

    public boolean isArrow() {
        return Arrow;
    }

    @Override
    public void project(final PointObject P) {
        final double h = project(P.getX(), P.getY());
        P.setXY(getX() + h * getDX(), getY() + h * getDY());
        P.setA(h / getLength());
    }

    @Override
    public void project(final PointObject P, final double alpha) {
        final double d = alpha * getLength();
        P.setXY(getX() + d * getDX(), getY() + d * getDY());
    }

    @Override
    public boolean moveable() {
        if (!Fixed && P1.moveable() && P2.moveable()) {
            return true;
        }
        return false;
    }
}
