package edu.gsbme.geometrykernel.input.Comsol;

import java.util.Vector;

public class Manifold {

    public enum ManifoldClass {

        BezierCurve, BezierTri, BezierSurf, BSplineCurve, BSplineSurf, MeshCurve, MeshSurface, PolChain
    }

    public ManifoldClass classname;

    public int id;

    public int sdim;

    public int degree1;

    public int degree2;

    public int ControlPtCount;

    Vector<String[]> coordinates;

    Vector<String> weights;

    Vector matrix;

    public Manifold(int j) {
        coordinates = new Vector<String[]>();
        weights = new Vector<String>();
        matrix = new Vector();
        id = j;
    }

    public void SetClassName(ManifoldClass cname) {
        classname = cname;
    }

    public ManifoldClass returnClassName() {
        return classname;
    }

    public int returnID() {
        return id;
    }

    public int returnSDIM() {
        return sdim;
    }

    public void SetID(int id) {
        this.id = id;
    }

    public void SetSDIM(int sdim) {
        this.sdim = sdim;
    }

    public Vector<String[]> returnCoordinates() {
        return coordinates;
    }

    public Vector<String> returnWeights() {
        return weights;
    }

    public void InsertRow(String rowdata) {
        String[] row = rowdata.split("\\s");
        String[] coord = new String[row.length - 1];
        for (int i = 0; i < coord.length; i++) {
            coord[i] = row[i];
        }
        coordinates.add(coord);
        weights.add(row[row.length - 1]);
    }

    public void PrintAll() {
        System.out.println("Manifold : " + classname + ", ID : " + id);
        System.out.println("Dim : " + sdim + ", Degree 1 : " + degree1 + ", Degree 2 : " + degree2);
        for (int i = 0; i < coordinates.size(); i++) {
            String[] coord = coordinates.get(i);
            System.out.print(i + " : ");
            for (int j = 0; j < coord.length; j++) {
                System.out.print(coord[j] + " ");
            }
            System.out.print(weights.get(i));
            System.out.println("");
        }
    }

    public void PrintCoord() {
        for (int i = 0; i < coordinates.size(); i++) {
            String[] coord = coordinates.get(i);
            System.out.print(i + " : ");
            for (int j = 0; j < coord.length; j++) {
                System.out.print(coord[j] + " ");
            }
            System.out.println("");
        }
    }

    public void PrintWeight() {
        for (int i = 0; i < weights.size(); i++) {
            String x = weights.get(i);
            System.out.println(x);
        }
    }
}
