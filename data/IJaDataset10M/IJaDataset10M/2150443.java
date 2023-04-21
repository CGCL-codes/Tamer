package simplex;

import java.util.*;
import javaview_test.*;

public class Simplex {

    private static final double EPSILON = 1.0e-6;

    private Vector[] vertices;

    public Simplex(Vector[] vertices) {
        this.vertices = vertices.clone();
    }

    public boolean contains(Vector vertex) {
        for (Vector v : vertices) {
            if (v == vertex) {
                return true;
            }
        }
        return false;
    }

    public Vector[] getVertices() {
        return vertices.clone();
    }

    public Vector[][] getFacets() {
        Vector[][] facets = new Vector[vertices.length][vertices.length - 1];
        for (int i = 0; i < vertices.length; i++) {
            int k = 0;
            for (int j = 0; j < vertices.length; j++) {
                if (i != j) {
                    facets[i][k++] = vertices[j];
                }
            }
        }
        return facets;
    }

    public static Vector[][] getBoundary(Set<Simplex> simplices) {
        Set<Vector[]> boundary = new HashSet<Vector[]>();
        for (Simplex simplex : simplices) {
            for (Vector[] facet : simplex.getFacets()) {
                if (boundary.contains(facet)) {
                    boundary.remove(facet);
                } else {
                    boundary.add(facet);
                }
            }
        }
        return boundary.toArray(new Vector[boundary.size()][]);
    }

    public int[] relation(Vector point) {
        int dim = vertices.length - 1;
        if (point.getDimensions() != dim) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        Matrix matrix = new Matrix(dim + 1, dim + 2);
        for (int column = 0; column < dim + 2; column++) {
            matrix.setElement(0, column, 1.0);
        }
        for (int row = 1; row < dim + 1; row++) {
            matrix.setElement(row, 0, point.getElement(row - 1));
            for (int column = 1; column < dim + 2; column++) {
                matrix.setElement(row, column, vertices[column - 1].getElement(row - 1));
            }
        }
        System.out.println(matrix);
        Vector vector = new Vector(dim);
        vector.cross(point, matrix);
        double content = vector.getElement(0);
        int[] result = new int[dim + 1];
        for (int i = 0; i < result.length; i++) {
            double value = vector.getElement(i + 1);
            if (Math.abs(value) <= EPSILON * Math.abs(content)) {
                result[i] = 0;
            } else if (value < 0) {
                result[i] = -1;
            } else {
                result[i] = 1;
            }
        }
        if (content < 0) {
            for (int i = 0; i < result.length; i++) {
                result[i] = -result[i];
            }
        }
        if (content == 0) {
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.abs(result[i]);
            }
        }
        return result;
    }

    public Vector isOutside(Vector point) {
        int[] result = relation(point);
        for (int i = 0; i < result.length; i++) {
            if (result[i] > 0) {
                return vertices[i];
            }
        }
        return null;
    }

    public int vsCircumcircle(Vector point) {
        int dim = point.getDimensions();
        Matrix matrix = new Matrix(vertices.length + 1, dim + 2);
        for (int row = 0; row < vertices.length; row++) {
            for (int column = 0; column < dim; column++) {
                matrix.setElement(row, column, vertices[row].getElement(column));
            }
            matrix.setElement(row, dim, 1);
            matrix.setElement(row, dim + 1, vertices[row].dot(vertices[row]));
        }
        for (int column = 0; column < dim; column++) {
            matrix.setElement(vertices.length, column, vertices[vertices.length - 1].getElement(column));
        }
        matrix.setElement(vertices.length, dim, 1);
        matrix.setElement(vertices.length, dim + 1, point.dot(point));
        double d = matrix.determinant();
        int result = (d < 0) ? -1 : (d > 0) ? 1 : 0;
        if (content() < 0) {
            result = -result;
        }
        return result;
    }

    /**
     * True iff simplices are neighbors.
     * Two simplices are neighbors if they are the same dimension and they share
     * a facet.
     * @param simplex the other Simplex
     * @return true iff this Simplex is a neighbor of simplex
     */
    public boolean isNeighbor(Simplex simplex) {
        if (vertices.length != simplex.vertices.length) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        int n = vertices.length;
        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (vertices[i] == vertices[j]) {
                    c++;
                    continue;
                }
            }
        }
        return (c == n - 1);
    }

    public double content() {
        int dim = vertices.length;
        Matrix matrix = new Matrix(dim, dim);
        for (int row = 0; row < dim; row++) {
            for (int column = 0; column < dim - 1; column++) {
                matrix.setElement(row, column, vertices[row].getElement(column));
            }
            matrix.setElement(row, dim - 1, 1.0);
        }
        int fact = 1;
        for (int i = 1; i < dim; i++) {
            fact *= i;
        }
        return matrix.determinant() / fact;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vector v : vertices) {
            sb.append("  ").append(v);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Vector v0 = new Vector(0, 0);
        Vector v1 = new Vector(1, 1);
        Vector v2 = new Vector(2, 2);
        Simplex simplex = new Simplex(new Vector[] { v0, new Vector(0, 1), new Vector(1, 0) });
        System.out.println(simplex);
        Vector[][] facets = simplex.getFacets();
        for (int i = 0; i < facets.length; i++) {
            for (int j = 0; j < facets[i].length; j++) {
                System.out.print(facets[i][j] + " ");
            }
            System.out.println();
        }
    }
}
