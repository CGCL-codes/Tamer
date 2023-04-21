package edu.ucsd.ncmir.dendist;

import edu.ucsd.ncmir.spl.graphics.Triplet;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author spl
 */
class MeshData extends ArrayList<Triplet[]> {

    private double _xmin = Double.MAX_VALUE;

    private double _xmax = -Double.MAX_VALUE;

    private double _ymin = Double.MAX_VALUE;

    private double _ymax = -Double.MAX_VALUE;

    private double _zmin = Double.MAX_VALUE;

    private double _zmax = -Double.MAX_VALUE;

    private double _ucentroid = 0;

    private double _vcentroid = 0;

    private double _wcentroid = 0;

    MeshData(ArrayList<Triplet[]> mesh_data) {
        HashSet<Triplet> unique_triplets = new HashSet<Triplet>();
        for (Triplet[] t : mesh_data) {
            for (int i = 0; i < 3; i++) {
                double u = t[i].getU();
                double v = t[i].getV();
                double w = t[i].getW();
                if (unique_triplets.add(t[i])) {
                    this._ucentroid += u;
                    this._vcentroid += v;
                    this._wcentroid += w;
                }
                if (this._xmin > u) this._xmin = u;
                if (this._ymin > v) this._ymin = v;
                if (this._zmin > w) this._zmin = w;
                if (this._xmax < u) this._xmax = u;
                if (this._ymax < v) this._ymax = v;
                if (this._zmax < w) this._zmax = w;
            }
            super.add(t);
        }
        this._ucentroid /= unique_triplets.size();
        this._vcentroid /= unique_triplets.size();
        this._wcentroid /= unique_triplets.size();
    }

    double[] getCentroid() {
        return new double[] { this._ucentroid, this._vcentroid, this._wcentroid };
    }

    double[][] getLimits() {
        return new double[][] { { this._xmin, this._xmax }, { this._ymin, this._ymax }, { this._zmin, this._zmax } };
    }

    double[] getSizes() {
        return new double[] { this._xmax - this._xmin, this._ymax - this._ymin, this._zmax - this._zmin };
    }
}
