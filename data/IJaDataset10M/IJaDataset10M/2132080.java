package com.memoire.bu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFrame;
import com.memoire.fu.FuRefTable;

public class BuFormLayout implements LayoutManager2, Serializable {

    public static final int FIT = -4;

    public static final int SAME = -3;

    public static final int NONE = -2;

    public static final int ALL = -1;

    public static final int FIRST = 0;

    public static final int LAST = Integer.MAX_VALUE;

    static final class Constraint implements Cloneable, Serializable {

        int col;

        int row;

        int cspan;

        int rspan;

        boolean hfilled;

        boolean vfilled;

        float xalign;

        float yalign;

        Constraint() {
            this(0, 0, 1, 1, true, true, 0.f, 0.0f);
        }

        Constraint(int _col, int _row, int _cspan, int _rspan, boolean _hfilled, boolean _vfilled, float _xalign, float _yalign) {
            col = Math.max(0, _col);
            row = Math.max(0, _row);
            cspan = Math.max(1, _cspan);
            rspan = Math.max(1, _rspan);
            hfilled = _hfilled;
            vfilled = _vfilled;
            xalign = _xalign;
            yalign = _yalign;
        }

        public int hashCode() {
            return (col % 8) + (row % 8) * 8 + (cspan % 8) * 8 * 8 + (rspan % 8) * 8 * 8 * 8 + (hfilled ? 8 * 8 * 8 * 8 : 0) + (vfilled ? 8 * 8 * 8 * 8 * 8 : 0) + (int) (xalign * 100) + (int) (yalign * 100);
        }

        public boolean equals(Object _o) {
            if (!(_o instanceof Constraint)) return false;
            Constraint o = (Constraint) _o;
            return (col == o.col) && (row == o.row) && (cspan == o.cspan) && (rspan == o.rspan) && (hfilled == o.hfilled) && (vfilled == o.vfilled) && (xalign == o.xalign) && (yalign == o.yalign);
        }

        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException("clone not supported");
            }
        }
    }

    public static final Constraint constraint(int _col, int _row) {
        return constraint(_col, _row, 1, 1, true, true, 0.f, 0.0f);
    }

    public static final Constraint constraint(int _col, int _row, int _cspan, int _rspan) {
        return constraint(_col, _row, _cspan, _rspan, true, true, 0.f, 0.0f);
    }

    public static final Constraint constraint(int _col, int _row, boolean _hfilled, float _xalign) {
        return constraint(_col, _row, 1, 1, _hfilled, false, _xalign, 0.5f);
    }

    public static final Constraint constraint(int _col, int _row, int _cspan, boolean _hfilled, float _xalign) {
        return constraint(_col, _row, _cspan, 1, _hfilled, false, _xalign, 0.5f);
    }

    private static final FuRefTable CACHE = new FuRefTable(FuRefTable.WEAK, FuRefTable.WEAK);

    public static final Constraint constraint(int _col, int _row, int _cspan, int _rspan, boolean _hfilled, boolean _vfilled, float _xalign, float _yalign) {
        Constraint r = new Constraint(_col, _row, _cspan, _rspan, _hfilled, _vfilled, _xalign, _yalign);
        return cache(r);
    }

    static final Constraint cache(Constraint _e) {
        Constraint e = (Constraint) CACHE.get(_e);
        if (e == null) {
            e = _e;
            CACHE.put(e, e);
        }
        return e;
    }

    public BuFormLayout() {
        this(0, 0, LAST, LAST);
    }

    public BuFormLayout(int _hgap, int _vgap) {
        this(_hgap, _vgap, LAST, LAST);
    }

    public BuFormLayout(int _hgap, int _vgap, int _celastic, int _relastic) {
        constraints_ = new Hashtable(11);
        hgap_ = _hgap;
        vgap_ = _vgap;
        celastic_ = _celastic;
        relastic_ = _relastic;
    }

    protected Hashtable constraints_;

    protected int hgap_;

    public int getHgap() {
        return hgap_;
    }

    protected int vgap_;

    public int getVgap() {
        return vgap_;
    }

    protected int celastic_;

    public int getCelastic() {
        return celastic_;
    }

    protected int relastic_;

    public int getRelastic() {
        return relastic_;
    }

    public void addLayoutComponent(Component _comp, Object _cstr) {
        if (_cstr == null) constraints_.remove(_comp); else if (_cstr instanceof Constraint) constraints_.put(_comp, _cstr); else throw new IllegalArgumentException("illegal constraint for form layout: " + _cstr);
    }

    public void addLayoutComponent(String _name, Component _comp) {
        throw new IllegalArgumentException("illegal constraint for form layout: " + _name);
    }

    public void removeLayoutComponent(Component _comp) {
        constraints_.remove(_comp);
    }

    protected int getColumns(Container _parent) {
        int nbcol = 0;
        synchronized (_parent.getTreeLock()) {
            int nbc = _parent.getComponentCount();
            for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                nbcol = Math.max(nbcol, e.col + e.cspan);
            }
        }
        return nbcol;
    }

    protected int getRows(Container _parent) {
        int nbrow = 0;
        synchronized (_parent.getTreeLock()) {
            int nbc = _parent.getComponentCount();
            for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                nbrow = Math.max(nbrow, e.row + e.rspan);
            }
        }
        return nbrow;
    }

    public Dimension minimumLayoutSize(Container _parent) {
        synchronized (_parent.getTreeLock()) {
            Insets insets = _parent.getInsets();
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    public Dimension preferredLayoutSize(Container _parent) {
        synchronized (_parent.getTreeLock()) {
            int nbc = _parent.getComponentCount();
            int nbcol = getColumns(_parent);
            int nbrow = getRows(_parent);
            int[] wcol = new int[nbcol];
            int[] hrow = new int[nbrow];
            int maxcspan = 0;
            int maxrspan = 0;
            for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                maxcspan = Math.max(maxcspan, e.cspan);
                maxrspan = Math.max(maxrspan, e.rspan);
                if ((e.cspan == 1) || (e.rspan == 1)) {
                    Dimension d = c.getPreferredSize();
                    if (e.cspan == 1) wcol[e.col] = Math.max(wcol[e.col], d.width);
                    if (e.rspan == 1) hrow[e.row] = Math.max(hrow[e.row], d.height);
                }
            }
            if (celastic_ <= SAME) {
                int wmax = 0;
                for (int j = 0; j < nbcol; j++) if (wcol[j] > 0) wmax = Math.max(wmax, wcol[j]);
                for (int j = 0; j < nbcol; j++) if (wcol[j] > 0) wcol[j] = wmax;
            }
            if (relastic_ <= SAME) {
                int hmax = 0;
                for (int j = 0; j < nbrow; j++) if (hrow[j] > 0) hmax = Math.max(hmax, hrow[j]);
                for (int j = 0; j < nbrow; j++) if (hrow[j] > 0) hrow[j] = hmax;
            }
            for (int s = 2; s <= maxcspan; s++) for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                if (e.cspan == s) {
                    Dimension d = c.getPreferredSize();
                    int wc = d.width - wcol[e.col];
                    for (int j = 1; j < s - 1; j++) wc -= hgap_ + wcol[e.col + j];
                    wcol[e.col + s - 1] = Math.max(wcol[e.col + s - 1], wc);
                }
            }
            for (int s = 2; s <= maxrspan; s++) for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                if (e.rspan == s) {
                    Dimension d = c.getPreferredSize();
                    int hc = d.height - hrow[e.row];
                    for (int j = 1; j < s - 1; j++) hc -= hgap_ + hrow[e.row + j];
                    hrow[e.row + s - 1] = Math.max(hrow[e.row + s - 1], hc);
                }
            }
            wcol = checkMinWidths(_parent, wcol);
            hrow = checkMinHeights(_parent, hrow);
            nbcol = wcol.length;
            nbrow = hrow.length;
            int w = 0;
            for (int i = 0; i < nbcol; i++) {
                if (i > 0) w += hgap_;
                w += wcol[i];
            }
            int h = 0;
            for (int i = 0; i < nbrow; i++) {
                if (i > 0) h += vgap_;
                h += hrow[i];
            }
            Insets insets = _parent.getInsets();
            return new Dimension(w + insets.left + insets.right, h + insets.top + insets.bottom);
        }
    }

    public Dimension maximumLayoutSize(Container _parent) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    public float getLayoutAlignmentX(Container _parent) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container _parent) {
        return 0.5f;
    }

    public void invalidateLayout(Container _parent) {
    }

    protected int[] checkMinWidths(Container _parent, int[] _wcol) {
        return _wcol;
    }

    protected int[] checkMinHeights(Container _parent, int[] _hrow) {
        return _hrow;
    }

    public void layoutContainer(Container _parent) {
        synchronized (_parent.getTreeLock()) {
            Insets insets = _parent.getInsets();
            Dimension size = _parent.getSize();
            Rectangle bounds = new Rectangle(insets.left, insets.top, size.width - (insets.left + insets.right), size.height - (insets.top + insets.bottom));
            int nbc = _parent.getComponentCount();
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;
            int nbcol = getColumns(_parent);
            int nbrow = getRows(_parent);
            int[] wcol = new int[nbcol];
            int[] hrow = new int[nbrow];
            int maxcspan = 0;
            int maxrspan = 0;
            for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                maxcspan = Math.max(maxcspan, e.cspan);
                maxrspan = Math.max(maxrspan, e.rspan);
                if ((e.cspan == 1) || (e.rspan == 1)) {
                    Dimension d = c.getPreferredSize();
                    if (e.cspan == 1) wcol[e.col] = Math.max(wcol[e.col], d.width);
                    if (e.rspan == 1) hrow[e.row] = Math.max(hrow[e.row], d.height);
                }
            }
            if (celastic_ <= SAME) {
                int wmax = 0;
                for (int j = 0; j < nbcol; j++) if (wcol[j] > 0) wmax = Math.max(wmax, wcol[j]);
                for (int j = 0; j < nbcol; j++) if (wcol[j] > 0) wcol[j] = wmax;
            }
            if (relastic_ <= SAME) {
                int hmax = 0;
                for (int j = 0; j < nbrow; j++) if (hrow[j] > 0) hmax = Math.max(hmax, hrow[j]);
                for (int j = 0; j < nbrow; j++) if (hrow[j] > 0) hrow[j] = hmax;
            }
            for (int s = 2; s <= maxcspan; s++) for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                if (e.cspan == s) {
                    Dimension d = c.getPreferredSize();
                    int wc = d.width - wcol[e.col];
                    for (int j = 1; j < s - 1; j++) wc -= hgap_ + wcol[e.col + j];
                    wcol[e.col + s - 1] = Math.max(wcol[e.col + s - 1], wc);
                }
            }
            for (int s = 2; s <= maxrspan; s++) for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                if (e.rspan == s) {
                    Dimension d = c.getPreferredSize();
                    int hc = d.height - hrow[e.row];
                    for (int j = 1; j < s - 1; j++) hc -= hgap_ + hrow[e.row + j];
                    hrow[e.row + s - 1] = Math.max(hrow[e.row + s - 1], hc);
                }
            }
            wcol = checkMinWidths(_parent, wcol);
            hrow = checkMinHeights(_parent, hrow);
            nbcol = wcol.length;
            nbrow = hrow.length;
            if ((nbcol > 0) && (celastic_ >= ALL)) {
                w = 0;
                for (int j = 0; j < nbcol; j++) {
                    if (j > 0) w += hgap_;
                    w += wcol[j];
                }
                if (w < bounds.width) {
                    if ((celastic_ == ALL) || (celastic_ == FIT)) {
                        int dw = (bounds.width - w) / nbcol;
                        if (dw > 0) for (int j = 0; j < nbcol - 1; j++) wcol[j] += dw;
                        wcol[nbcol - 1] += bounds.width - w - (nbcol - 1) * dw;
                    } else {
                        wcol[Math.min(celastic_, nbcol - 1)] += bounds.width - w;
                    }
                }
            }
            if ((nbrow > 0) && (relastic_ >= ALL)) {
                h = 0;
                for (int j = 0; j < nbrow; j++) {
                    if (j > 0) h += vgap_;
                    h += hrow[j];
                }
                if (h < bounds.height) {
                    if ((relastic_ == ALL) || (relastic_ == FIT)) {
                        int dh = (bounds.height - h) / nbrow;
                        if (dh > 0) for (int j = 0; j < nbrow - 1; j++) hrow[j] += dh;
                        hrow[nbrow - 1] += bounds.height - h - (nbrow - 1) * dh;
                    } else {
                        hrow[Math.min(relastic_, nbrow - 1)] += bounds.height - h;
                    }
                }
            }
            for (int n = 0; n < nbc; n++) {
                Component c = _parent.getComponent(n);
                Constraint e = (Constraint) constraints_.get(c);
                if (e == null) continue;
                Dimension d = c.getPreferredSize();
                x = 0;
                y = 0;
                w = 0;
                h = 0;
                for (int j = 0; j < e.col; j++) {
                    x += hgap_;
                    x += wcol[j];
                }
                for (int j = 0; j < e.row; j++) {
                    y += vgap_;
                    y += hrow[j];
                }
                for (int j = 0; j < e.cspan; j++) {
                    if (j > 0) w += hgap_;
                    w += wcol[e.col + j];
                }
                for (int j = 0; j < e.rspan; j++) {
                    if (j > 0) h += vgap_;
                    h += hrow[e.row + j];
                }
                if (x > bounds.width) x = bounds.width;
                if (y > bounds.height) y = bounds.height;
                if (x + w > bounds.width) w = bounds.width - x;
                if (y + h > bounds.height) h = bounds.height - y;
                int wmax = w;
                int hmax = h;
                if (!e.hfilled) w = Math.min(w, d.width);
                if (!e.vfilled) h = Math.min(h, d.height);
                int dx = 0;
                int dy = 0;
                if (w < wmax) dx = (int) ((wmax - w) * e.xalign);
                if (h < hmax) dy = (int) ((hmax - h) * e.yalign);
                c.setBounds(bounds.x + x + dx, bounds.y + y + dy, w, h);
            }
        }
    }

    public Pack pack(Component _c) {
        Container p = _c.getParent();
        if (p == null) throw new IllegalArgumentException("no parent, use packIn() instead");
        if (p.getLayout() != this) throw new IllegalArgumentException("invalid layout");
        Constraint e = (Constraint) constraints_.get(_c);
        if (e == null) e = new Constraint();
        return new Pack(this, _c, e);
    }

    public Pack packIn(Component _c, Container _p) {
        if (_p.getLayout() != this) throw new IllegalArgumentException("invalid layout");
        Constraint e = (Constraint) constraints_.get(_c);
        if (e == null) e = new Constraint();
        if (_c.getParent() != _p) _p.add(_c, e);
        return new Pack(this, _c, e);
    }

    public Pack packAs(Component _c, Component _d) {
        Container p = _d.getParent();
        if (p.getLayout() != this) throw new IllegalArgumentException("invalid layout");
        Constraint e = (Constraint) constraints_.get(_d);
        if (e == null) e = new Constraint();
        if (_c.getParent() != p) p.add(_c, e);
        return new Pack(this, _c, e);
    }

    public static final class Pack {

        private BuFormLayout l_;

        private Component c_;

        private Constraint e_;

        Pack(BuFormLayout _l, Component _c, Constraint _e) {
            l_ = _l;
            c_ = _c;
            set(_e);
        }

        private void set(Constraint _e) {
            e_ = cache(_e);
            l_.constraints_.put(c_, e_);
        }

        public Pack col(int _col) {
            if (e_.col != _col) {
                Constraint e = (Constraint) e_.clone();
                e.col = Math.max(0, _col);
                set(e);
            }
            return this;
        }

        public Pack row(int _row) {
            if (e_.row != _row) {
                Constraint e = (Constraint) e_.clone();
                e.row = Math.max(0, _row);
                set(e);
            }
            return this;
        }

        public Pack cspan(int _cspan) {
            if (e_.cspan != _cspan) {
                Constraint e = (Constraint) e_.clone();
                e.cspan = Math.max(1, _cspan);
                set(e);
            }
            return this;
        }

        public Pack rspan(int _rspan) {
            if (e_.rspan != _rspan) {
                Constraint e = (Constraint) e_.clone();
                e.rspan = Math.max(1, _rspan);
                set(e);
            }
            return this;
        }

        public Pack hf(boolean _hfilled) {
            if (e_.hfilled != _hfilled) {
                Constraint e = (Constraint) e_.clone();
                e.hfilled = _hfilled;
                set(e);
            }
            return this;
        }

        public Pack vf(boolean _vfilled) {
            if (e_.vfilled != _vfilled) {
                Constraint e = (Constraint) e_.clone();
                e.vfilled = _vfilled;
                set(e);
            }
            return this;
        }

        public Pack hf() {
            return hf(true);
        }

        public Pack vf() {
            return vf(true);
        }

        public Pack xalign(float _xalign) {
            if (e_.xalign != _xalign) {
                Constraint e = (Constraint) e_.clone();
                e.xalign = _xalign;
                set(e);
            }
            return this;
        }

        public Pack yalign(float _yalign) {
            if (e_.yalign != _yalign) {
                Constraint e = (Constraint) e_.clone();
                e.yalign = _yalign;
                set(e);
            }
            return this;
        }

        public Pack left() {
            return xalign(0.f).hf(false);
        }

        public Pack right() {
            return xalign(1.f).hf(false);
        }

        public Pack center() {
            return xalign(0.5f).hf(false);
        }

        public Pack justify() {
            return left().hf();
        }

        public Pack top() {
            return yalign(0.f).vf(false);
        }

        public Pack bottom() {
            return yalign(1.f).vf(false);
        }

        public Pack vcenter() {
            return yalign(0.5f).vf(false);
        }

        public Pack vjustify() {
            return top().vf();
        }

        public Pack cnext() {
            Constraint e = (Constraint) e_.clone();
            e.col++;
            set(e);
            return this;
        }

        public Pack cprev() {
            Constraint e = (Constraint) e_.clone();
            e.col = Math.max(0, e.col - 1);
            set(e);
            return this;
        }

        public Pack rnext() {
            Constraint e = (Constraint) e_.clone();
            e.row++;
            set(e);
            return this;
        }

        public Pack rprev() {
            Constraint e = (Constraint) e_.clone();
            e.row = Math.max(0, e.row - 1);
            set(e);
            return this;
        }
    }

    public static void main(String[] _args) {
        JFrame f = new JFrame("Test Form Layout");
        Container c = f.getContentPane();
        BuFormLayout l = new BuFormLayout(5, 5, 2, 2);
        c.setLayout(l);
        c.add(new JButton("AAAA"), constraint(0, 0, 2, 1));
        c.add(new JButton("BBBB"), constraint(2, 0, 1, 2));
        c.add(new JButton("CCCCCCC"), constraint(0, 1));
        c.add(new JButton("DDDD"), constraint(1, 1));
        c.add(new JButton("F"), constraint(0, 2, 2, 1));
        c.add(new JButton("G"), constraint(2, 2));
        c.add(new JButton("H"), constraint(3, 0, 1, 5));
        c.add(new JButton("I"), constraint(0, 3, 3, 1));
        c.add(new JButton("X"), constraint(0, 4, 3, 1, false, false, 0.5f, 1.0f));
        f.pack();
        f.show();
    }
}
