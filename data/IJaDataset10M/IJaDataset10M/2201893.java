package edu.gsbme.geometrykernel.data.dim2;

import java.util.List;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.Error.NoObjectFoundInSearch;
import edu.gsbme.MMLParser2.Error.PathError;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.VirtualFMLTree;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.XPath.XPathScanner;
import edu.gsbme.geometrykernel.algorithm.RationalBezierSurfaceUtility;
import edu.gsbme.geometrykernel.algorithm.intersection.intersection;
import edu.gsbme.geometrykernel.data.Idata;
import edu.gsbme.geometrykernel.data.STORAGE_TYPE;
import edu.gsbme.geometrykernel.data.attributes.Attr;
import edu.gsbme.geometrykernel.data.attributes.Scalar;
import edu.gsbme.geometrykernel.data.dim0.point;
import edu.gsbme.geometrykernel.data.dim1.Ray;
import edu.gsbme.hdf5.parser.hdf5Parser;

/**
 * Rational Bezier surface class
 * @author David
 *
 */
public class RationalBezierSurface extends Surface {

    public RationalBezierSurface(String id) {
        super(id);
    }

    public RationalBezierSurface(String id, int degree1, int degree2) {
        super(id);
        control_points = new point[degree1 + 1][degree2 + 1];
    }

    public double getWeight(int x, int y) {
        if (getStorageTye() == STORAGE_TYPE.DIRECT) {
            return ((Scalar) control_points[y][x].getAttribute(Attr.weight.toString())).value;
        } else {
            try {
                FMLFactory factory = this.getReferenceFactory();
                VirtualFMLTree tree = factory.getVirtualTree();
                cTreeNodes node = tree.searchCellTree(getReferencePath());
                if (node.getSource() == FMLSource.XML) {
                    Element bs = (Element) node.getReferneceObject();
                    NodeList controlpts = XPathScanner.getScanner().searchNodeListXPath(bs, "*");
                    int pos = y * (getDegree1() + 1) + x;
                    Element ctrl = (Element) controlpts.item(pos);
                    if (ctrl.hasAttribute(Attributes.weight.toString())) return Double.valueOf(ctrl.getAttribute(Attributes.weight.toString())); else return 1;
                } else {
                    Dataset ds = (Dataset) node.getReferneceObject();
                    hdf5Parser h5parser = node.h5parser;
                    double[][][] table = h5parser.getCellHandler().convertDStoDoubleDim3Array(ds);
                    return table[y][x][4];
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void insertWeight(int x, int y, double w) {
        Scalar weight = new Scalar(Attr.weight, w);
        control_points[y][x].setAttribute(weight);
    }

    public int getDegree1() {
        if (getStorageTye() == STORAGE_TYPE.DIRECT) {
            return control_points[0].length - 1;
        } else {
            try {
                FMLFactory factory = this.getReferenceFactory();
                VirtualFMLTree tree = factory.getVirtualTree();
                cTreeNodes node = tree.searchCellTree(getReferencePath());
                if (node.getSource() == FMLSource.XML) {
                    Element bs = (Element) node.getReferneceObject();
                    return Integer.parseInt(bs.getAttribute(Attributes.degree_1.toString()));
                } else {
                    Dataset ds = (Dataset) node.getReferneceObject();
                    List attrList = ds.getMetadata();
                    Attribute attr = (Attribute) attrList.get(0);
                    int[] degree = (int[]) attr.getValue();
                    return degree[0];
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int getDegree2() {
        if (getStorageTye() == STORAGE_TYPE.DIRECT) {
            return control_points.length - 1;
        } else {
            try {
                FMLFactory factory = this.getReferenceFactory();
                VirtualFMLTree tree = factory.getVirtualTree();
                cTreeNodes node = tree.searchCellTree(getReferencePath());
                if (node.getSource() == FMLSource.XML) {
                    Element bs = (Element) node.getReferneceObject();
                    return Integer.parseInt(bs.getAttribute(Attributes.degree_2.toString()));
                } else {
                    Dataset ds = (Dataset) node.getReferneceObject();
                    List attrList = ds.getMetadata();
                    Attribute attr = (Attribute) attrList.get(0);
                    int[] degree = (int[]) attr.getValue();
                    return degree[1];
                }
            } catch (PathError e) {
                e.printStackTrace();
            } catch (NoObjectFoundInSearch e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public Vector3d normal(double s, double t) {
        return null;
    }

    public Point3d evaluate(double s, double t) {
        double[][][] coord = (double[][][]) getCoordinates();
        double[][] x = new double[coord.length][coord[0].length];
        double[][] y = new double[coord.length][coord[0].length];
        double[][] z = new double[coord.length][coord[0].length];
        double[][] w = new double[coord.length][coord[0].length];
        for (int i = 0; i < coord.length; i++) {
            for (int j = 0; j < coord[i].length; j++) {
                x[i][j] = coord[i][j][0];
                y[i][j] = coord[i][j][1];
                z[i][j] = coord[i][j][2];
                w[i][j] = coord[i][j][3];
            }
        }
        double x1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(s, t, x, w);
        double y1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(s, t, y, w);
        double z1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(s, t, z, w);
        return new Point3d(x1, y1, z1);
    }

    /**
	 * TODO need to verify this is correct
	 * @param x_resol
	 * @param y_resol
	 * @return
	 */
    public Point3d[][] generate_mesh(int x_resol, int y_resol) {
        Point3d[][] grid = new Point3d[y_resol][x_resol];
        double x_r = ((double) x_resol) - 1d;
        double y_r = ((double) y_resol) - 1d;
        double[][][] coord = (double[][][]) getCoordinates();
        double[][] x = new double[coord.length][coord[0].length];
        double[][] y = new double[coord.length][coord[0].length];
        double[][] z = new double[coord.length][coord[0].length];
        double[][] w = new double[coord.length][coord[0].length];
        for (int i = 0; i < coord.length; i++) {
            for (int j = 0; j < coord[i].length; j++) {
                x[i][j] = coord[i][j][0];
                y[i][j] = coord[i][j][1];
                z[i][j] = coord[i][j][2];
                w[i][j] = coord[i][j][3];
            }
        }
        for (double i = 0; i < y_r; i++) {
            double y_para = i / y_r;
            for (double j = 0; j < x_r; j++) {
                double x_para = j / x_r;
                double x1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(x_para, y_para, x, w);
                double y1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(x_para, y_para, y, w);
                double z1 = RationalBezierSurfaceUtility.getRationalBezierSurfacePoint(x_para, y_para, z, w);
                grid[(int) i][(int) j] = new Point3d(x1, y1, z1);
            }
        }
        return grid;
    }

    public boolean rayIntersection(Ray r) {
        double[][][] grid = (double[][][]) getCoordinates();
        for (int i = 0; i < getDegree2(); i++) {
            for (int j = 0; j < getDegree1(); j++) {
                point v1 = new point("", grid[i][j][0], grid[i][j][1], grid[i][j][2]);
                Scalar weight = new Scalar(Attr.weight, grid[i][j][3]);
                v1.setAttribute(weight);
                point v2 = new point("", grid[i][j + 1][0], grid[i][j + 1][1], grid[i][j + 1][2]);
                weight = new Scalar(Attr.weight, grid[i][j + 1][3]);
                v2.setAttribute(weight);
                point v3 = new point("", grid[i + 1][j + 1][0], grid[i + 1][j + 1][1], grid[i + 1][j + 1][2]);
                weight = new Scalar(Attr.weight, grid[i + 1][j + 1][3]);
                v3.setAttribute(weight);
                point v4 = new point("", grid[i + 1][j][0], grid[i + 1][j][1], grid[i + 1][j][2]);
                weight = new Scalar(Attr.weight, grid[i + 1][j][3]);
                v4.setAttribute(weight);
                point[] array = new point[] { v1, v2, v3, v4 };
                if (intersection.RayPolygonIntersection(array, r)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Idata object) {
        return false;
    }

    @Override
    public Idata clone() {
        RationalBezierSurface clone = new RationalBezierSurface(this.getID(), this.getDegree1(), this.getDegree2());
        if (getStorageTye() == STORAGE_TYPE.DIRECT) {
            clone.setStorageType(STORAGE_TYPE.DIRECT);
            for (int i = 0; i < control_points.length; i++) {
                for (int j = 0; j < control_points[i].length; j++) {
                    clone.control_points[i][j] = (point) this.control_points[i][j].clone();
                }
            }
        } else {
            clone.setStorageType(STORAGE_TYPE.REFERNECE);
            clone.setReferenceFactory(this.getReferenceFactory());
            clone.setReferencePath(this.getReferencePath());
        }
        return clone;
    }

    /**
	 * double[][][] , grid structure [][] with (x,y,z,w) stored in last []
	 */
    public Object getCoordinates() {
        try {
            if (getStorageTye() == STORAGE_TYPE.DIRECT) {
                double[][][] grid = new double[control_points.length][control_points[0].length][4];
                for (int i = 0; i < control_points.length; i++) {
                    for (int j = 0; j < control_points[i].length; j++) {
                        double[] coord = (double[]) control_points[i][j].getCoordinates();
                        double[] entry = new double[] { coord[0], coord[1], coord[2], getWeight(i, j) };
                        grid[i][j] = entry;
                    }
                }
                return grid;
            } else {
                FMLFactory factory = this.getReferenceFactory();
                VirtualFMLTree tree = factory.getVirtualTree();
                cTreeNodes node = tree.searchCellTree(getReferencePath());
                if (node.getSource() == FMLSource.XML) {
                    Element bs = (Element) node.getReferneceObject();
                    return factory.getDataAPIWorker().returnBezierSurfaceParameter(bs);
                } else {
                    Dataset ds = (Dataset) node.getReferneceObject();
                    hdf5Parser h5parser = node.h5parser;
                    double[][][] table = h5parser.getCellHandler().convertDStoDoubleDim3Array(ds);
                    return table;
                }
            }
        } catch (PathError e) {
            e.printStackTrace();
        } catch (NoObjectFoundInSearch e) {
            e.printStackTrace();
        }
        return null;
    }
}
