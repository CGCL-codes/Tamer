package edu.ucsd.ncmir.MicroBrightField;

import edu.ucsd.ncmir.geometry.GeometryComponent;
import edu.ucsd.ncmir.spl.minixml.DataConversionException;
import edu.ucsd.ncmir.spl.minixml.Element;
import edu.ucsd.ncmir.spl.numerical.SplineInterpolator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Branch extends ArrayList<GeometryComponent> implements GeometryComponent {

    private static final long serialVersionUID = 1L;

    private GeometryComponent _parent;

    Branch(GeometryComponent parent) {
        this._parent = parent;
    }

    private String _color = "Grey";

    void setColor(String color) {
        this._color = color;
        for (GeometryComponent c : this.getGeometryComponents()) if (c instanceof Branch) ((Branch) c).setColor(color);
    }

    public String getColor() {
        return this._color;
    }

    void addNode(double[] node_values) {
        this.addNode(new GeometryNode(this, node_values));
    }

    void addNode(GeometryNode node) {
        this.add(node);
    }

    void addSpine(double[] terminal) {
        this.addSpine(new GeometryNode(this, terminal));
    }

    void addSpine(Element spine) throws DataConversionException, ParseException {
        @SuppressWarnings("unchecked") List<Element> list = (List<Element>) spine.getChildren();
        Element[] children = list.toArray(new Element[list.size()]);
        for (Element child : children) {
            String name = child.getName();
            if (name.equals("point")) this.addSpine(new GeometryNode(this, child)); else throw new ParseException("Unexpected element " + name + " in spine object", 0);
        }
    }

    void addSpine(GeometryNode terminal) {
        for (int node = this.size() - 1; node >= 0; node--) {
            GeometryComponent gc = this.get(node);
            if (gc instanceof GeometryNode) {
                GeometryNode last = (GeometryNode) gc;
                this.add(new Spine(this, last, terminal));
                break;
            }
        }
    }

    void addSplit(GeometryComponent node) {
        this.add(node);
    }

    public GeometryComponent[] getGeometryComponents() {
        return this.toArray(new GeometryComponent[this.size()]);
    }

    @Override
    public String toString() {
        String s = "Branch: ";
        s += this.size() + " ";
        for (GeometryComponent dc : this.getGeometryComponents()) s += "\n" + dc.toString();
        return s;
    }

    public double[][] getComponentValues() {
        return null;
    }

    public GeometryComponent getParent() {
        return this._parent;
    }

    public void smooth() {
        this.smooth(null);
    }

    private void smooth(GeometryNode lastnode) {
        ArrayList<GeometryNode> list = new ArrayList<GeometryNode>();
        if (lastnode != null) list.add(lastnode);
        if (false) {
            for (GeometryComponent gc : this.getGeometryComponents()) if (gc instanceof GeometryNode) list.add(lastnode = (GeometryNode) gc); else if (gc instanceof Branch) ((Branch) gc).smooth(lastnode);
            if (list.size() > 3) {
                double[] x = new double[list.size()];
                double[] y = new double[list.size()];
                double[] z = new double[list.size()];
                double[] r = new double[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    double[] values = list.get(i).getNodeValues();
                    x[i] = values[0];
                    y[i] = values[1];
                    z[i] = values[2];
                    r[i] = values[3];
                }
                double[] s = new double[list.size()];
                ArrayList<Double> slist = new ArrayList<Double>();
                ArrayList<Double> xlist = new ArrayList<Double>();
                ArrayList<Double> ylist = new ArrayList<Double>();
                ArrayList<Double> zlist = new ArrayList<Double>();
                ArrayList<Double> rlist = new ArrayList<Double>();
                slist.add(new Double(0));
                xlist.add(new Double(x[0]));
                ylist.add(new Double(y[0]));
                zlist.add(new Double(z[0]));
                rlist.add(new Double(r[0]));
                double sp = 0;
                for (int i = 1; i < list.size(); i++) {
                    double dx = x[i] - x[i - 1];
                    double dy = y[i] - y[i - 1];
                    double dz = z[i] - z[i - 1];
                    double ds = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                    if (ds > 0) {
                        sp += ds;
                        slist.add(new Double(sp));
                        xlist.add(new Double(x[i]));
                        ylist.add(new Double(y[i]));
                        zlist.add(new Double(z[i]));
                        rlist.add(new Double(r[i]));
                    }
                }
                double[] X;
                double[] Y;
                double[] Z;
                double[] R;
                if (slist.size() > 3) {
                    s = new double[slist.size()];
                    x = new double[xlist.size()];
                    y = new double[ylist.size()];
                    z = new double[zlist.size()];
                    r = new double[rlist.size()];
                    Double[] sd = slist.toArray(new Double[slist.size()]);
                    Double[] xd = xlist.toArray(new Double[xlist.size()]);
                    Double[] yd = ylist.toArray(new Double[ylist.size()]);
                    Double[] zd = zlist.toArray(new Double[zlist.size()]);
                    Double[] rd = rlist.toArray(new Double[rlist.size()]);
                    for (int i = 0; i < s.length; i++) {
                        s[i] = sd[i].doubleValue();
                        x[i] = xd[i].doubleValue();
                        y[i] = yd[i].doubleValue();
                        z[i] = zd[i].doubleValue();
                        r[i] = rd[i].doubleValue();
                    }
                    SplineInterpolator xs = new SplineInterpolator(s, x);
                    SplineInterpolator ys = new SplineInterpolator(s, y);
                    SplineInterpolator zs = new SplineInterpolator(s, z);
                    SplineInterpolator rs = new SplineInterpolator(s, r);
                    double ds = s[s.length - 1] / (s.length - 1.0);
                    X = new double[list.size()];
                    Y = new double[list.size()];
                    Z = new double[list.size()];
                    R = new double[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        double sv = ds * i;
                        X[i] = xs.evaluate(sv);
                        Y[i] = ys.evaluate(sv);
                        Z[i] = zs.evaluate(sv);
                        R[i] = rs.evaluate(sv);
                    }
                } else {
                    X = new double[xlist.size()];
                    Y = new double[ylist.size()];
                    Z = new double[zlist.size()];
                    R = new double[rlist.size()];
                    Double[] xd = xlist.toArray(new Double[xlist.size()]);
                    Double[] yd = ylist.toArray(new Double[ylist.size()]);
                    Double[] zd = zlist.toArray(new Double[zlist.size()]);
                    Double[] rd = rlist.toArray(new Double[rlist.size()]);
                    for (int i = 0; i < xd.length; i++) {
                        X[i] = xd[i].doubleValue();
                        Y[i] = yd[i].doubleValue();
                        Z[i] = zd[i].doubleValue();
                        R[i] = rd[i].doubleValue();
                    }
                }
                for (int i = 1; i < X.length - 1; i++) {
                    int im1 = i - 1;
                    int ip1 = i + 1;
                    list.get(i).setNodeValues((X[im1] + X[i] + X[ip1]) / 3, (Y[im1] + Y[i] + Y[ip1]) / 3, (Z[im1] + Z[i] + Z[ip1]) / 3, (R[im1] + R[i] + R[ip1]) / 3);
                }
                while (list.size() > X.length) list.remove(X.length - 1);
            }
        }
    }
}
