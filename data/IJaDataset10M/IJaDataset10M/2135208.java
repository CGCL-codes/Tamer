package rene.zirkel.objects;

import rene.util.xml.XmlWriter;
import rene.zirkel.Zirkel;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.construction.Count;
import rene.zirkel.graphics.MyGraphics;
import rene.zirkel.graphics.MyGraphics13;

public class LineObject extends TwoPointLineObject {

    static Count N = new Count();

    public LineObject(final Construction c, final PointObject p1, final PointObject p2) {
        super(c, p1, p2);
        validate();
        updateText();
    }

    @Override
    public String getTag() {
        return "Line";
    }

    @Override
    public void updateText() {
        setText(text2(Zirkel.name("text.line"), P1.getName(), P2.getName()));
    }

    @Override
    public void validate() {
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
            if (R < 1e-10) {
                Valid = false;
                return;
            }
            DX /= R;
            DY /= R;
        }
    }

    @Override
    public void paint(final MyGraphics g, final ZirkelCanvas zc) {
        if (!Valid || mustHide(zc)) return;
        if (!Partial || zc.showHidden()) {
            super.paint(g, zc);
            return;
        }
        final double xm = (zc.minX() + zc.maxX()) / 2, ym = (zc.minY() + zc.maxY()) / 2;
        final double d = (xm - X1) * DY - (ym - Y1) * DX;
        final double x = xm - d * DY, y = ym + d * DX;
        final double a = Math.max(zc.maxX() - zc.minX(), zc.maxY() - zc.minY());
        if (Math.abs(d) > a) return;
        final double b = (x - X1) * DX + (y - Y1) * DY;
        k1 = b - a;
        k2 = b + a;
        k12valid = true;
        final double dd = (zc.maxX() - zc.minX()) / 20;
        double dmin = -dd, dmax = R + dd;
        if (Dep != null) {
            for (int i = 0; i < NDep; i++) {
                if (!Dep[i].valid() || Dep[i].mustHide(zc)) continue;
                final double s = project(Dep[i].getX(), Dep[i].getY());
                if (s - dd < dmin) dmin = s - dd; else if (s + dd > dmax) dmax = s + dd;
            }
        }
        if (k1 < dmin) k1 = dmin;
        if (k2 > dmax) k2 = dmax;
        final double c1 = zc.col(X1 + k1 * DX), c2 = zc.col(X1 + k2 * DX), r1 = zc.row(Y1 + k1 * DY), r2 = zc.row(Y1 + k2 * DY);
        if (isStrongSelected() && g instanceof MyGraphics13) {
            ((MyGraphics13) g).drawMarkerLine(c1, r1, c2, r2);
        }
        g.setColor(this);
        g.drawLine(c1, r1, c2, r2, this);
        final String s = getDisplayText();
        if (!s.equals("")) {
            g.setLabelColor(this);
            setFont(g);
            DisplaysText = true;
            if (KeepClose) {
                final double side = (YcOffset < 0) ? 1 : -1;
                drawLabel(g, s, zc, X1 + XcOffset * DX, Y1 + XcOffset * DY, side * DX, side * DY, 0, 0);
            } else drawLabel(g, s, zc, X1 + k2 * DX / 2, Y1 + k2 * DY / 2, DX, DY, XcOffset, YcOffset);
        }
    }

    @Override
    public void printArgs(final XmlWriter xml) {
        xml.printArg("from", P1.getName());
        xml.printArg("to", P2.getName());
        super.printArgs(xml);
    }

    @Override
    public void setDefaults() {
        super.setDefaults();
        setPartial(Cn.PartialLines);
    }

    @Override
    public void toggleHidden() {
        if (Hidden) {
            Partial = false;
            Hidden = false;
        } else {
            if (Partial) {
                Partial = false;
                Hidden = true;
            } else Partial = true;
        }
    }

    @Override
    public boolean hasUnit() {
        return false;
    }

    @Override
    public boolean nearto(final int c, final int r, final ZirkelCanvas zc) {
        if (!displays(zc)) return false;
        if (zc.showHidden() || !Partial || Dep == null || !k12valid) return super.nearto(c, r, zc);
        final double x = zc.x(c), y = zc.y(r);
        final double s = project(x, y);
        if (s < k1 || s > k2) return false;
        final double d = (x - X1) * DY - (y - Y1) * DX;
        Value = Math.abs(zc.col(zc.minX() + d) - zc.col(zc.minX()));
        return Value < zc.selectionSize() * 2;
    }
}
