package edu.ucsd.ncmir.imod_model;

import edu.ucsd.ncmir.spl.graphics.Triplet;
import edu.ucsd.ncmir.spl.io.Accessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author spl
 * Reference: http://bio3d.colorado.edu/imod/doc/binspec.html
 */
public class MESH implements Type {

    private int _vsize;

    private int _lsize;

    private int _flag;

    private short _time;

    private short _surf;

    private float[][] _vert;

    private int[] _index;

    public MESH(Accessor accessor) throws IOException {
        this._vsize = accessor.getNextAsInteger();
        this._lsize = accessor.getNextAsInteger();
        this._flag = accessor.getNextAsInteger();
        this._time = (short) accessor.getNextAsShort();
        this._surf = (short) accessor.getNextAsShort();
        this._vert = new float[this._vsize][3];
        for (int i = 0; i < this._vsize; i++) for (int j = 0; j < 3; j++) this._vert[i][j] = accessor.getNextAsFloat();
        this._index = new int[this._lsize];
        for (int i = 0; i < this._lsize; i++) this._index[i] = accessor.getNextAsInteger();
    }

    /**
     * @return the _vsize
     */
    public int getVsize() {
        return this._vsize;
    }

    /**
     * @return the _lsize
     */
    public int getLsize() {
        return this._lsize;
    }

    /**
     * @return the _flag
     */
    public int getFlag() {
        return this._flag;
    }

    /**
     * @return the _time
     */
    public short getTime() {
        return this._time;
    }

    /**
     * @return the _surf
     */
    public short getSurf() {
        return this._surf;
    }

    /**
     * @return the _vert
     */
    public float[][] getVert() {
        return this._vert;
    }

    /**
     * @return the _index
     */
    public int[] getIndex() {
        return this._index;
    }

    public Iterator<Triplet[][][]> getPolygonIterator() {
        return new PolygonIterator(this._vert, this._index);
    }

    private class PolygonIterator implements Iterator<Triplet[][][]> {

        private float[][] _vert;

        private int[] _index;

        private int _i = 0;

        PolygonIterator(float[][] vert, int[] index) {
            this._vert = vert;
            this._index = index;
        }

        public void remove() {
        }

        public boolean hasNext() {
            return (this._i < this._index.length) && (this._index[this._i] != END_OF_LIST);
        }

        private static final int END_OF_LIST = -1;

        private static final int NEXT_IS_NORMAL = -20;

        private static final int BEGIN_PLAIN_POLYGON = -21;

        private static final int END_POLYGON = -22;

        private static final int BEGIN_VERTEX_NORMAL_PAIRS = -23;

        private static final int BEGIN_CONVEX_POLYGON_WITH_NORMALS = -24;

        private static final int BEGIN_VERTEX_NORMAL_POLYGON_PAIRS = -25;

        public Triplet[][][] next() {
            Triplet[][][] mesh = null;
            switch(this._index[this._i++]) {
                case BEGIN_VERTEX_NORMAL_PAIRS:
                    {
                        mesh = this.processWithVertexNormalIndices();
                        break;
                    }
                case BEGIN_VERTEX_NORMAL_POLYGON_PAIRS:
                    {
                        mesh = this.processWithVertexNormalPairs();
                        break;
                    }
                default:
                    {
                        new UnsupportedOperationException("Unsupported type. " + this._index[this._i - 1]);
                    }
            }
            return mesh;
        }

        private Triplet[][][] processWithVertexNormalIndices() {
            ArrayList<Triplet[][]> triangles = new ArrayList<Triplet[][]>();
            while ((this._i < this._index.length)) if (this._index[this._i] >= 0) {
                Triplet[][] triangle = new Triplet[3][];
                for (int i = 0; i < triangle.length; i++) triangle[i] = this.getPair(this._index[this._i++], this._index[this._i++]);
                triangles.add(triangle);
            } else {
                this._i++;
                break;
            }
            return triangles.toArray(new Triplet[0][][]);
        }

        private Triplet[] getPair(int v, int n) {
            Triplet[] vertex = new Triplet[2];
            vertex[0] = new Triplet(this._vert[v][0], this._vert[v][1], this._vert[v][2]);
            vertex[1] = new Triplet(this._vert[n][0], this._vert[n][1], this._vert[n][2]).unit();
            return vertex;
        }

        private Triplet[][][] processWithVertexNormalPairs() {
            ArrayList<Triplet[][]> triangles = new ArrayList<Triplet[][]>();
            while ((this._i < this._index.length)) if (this._index[this._i] >= 0) {
                Triplet[][] triangle = new Triplet[3][];
                for (int i = 0; i < triangle.length; i++) {
                    triangle[i] = this.getPair(this._index[this._i], this._index[this._i] + 1);
                    this._i++;
                }
                triangles.add(triangle);
            } else {
                this._i++;
                break;
            }
            return triangles.toArray(new Triplet[0][][]);
        }
    }
}
