package ijaux.hypergeom.index;

import ijaux.Util;
import java.io.Serializable;

public class BaseIndex extends GridIndex implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8538968068406821206L;

    /**
	 * 
	 */
    protected int[] dc;

    public BaseIndex(int[] dims) {
        reshape(dims);
        coords = new int[n];
        dc = Util.cumprodarr(dim, dim.length);
    }

    public BaseIndex(int[] dims, int idx) {
        reshape(dims);
        index = idx;
        coords = new int[n];
        dc = Util.cumprodarr(dim, dim.length);
        warp();
    }

    public void setDim(int[] dims) {
        n = dims.length;
        dim = dims;
        warp();
    }

    public void setIndex(int idx) {
        index = idx;
        updated = true;
        warp();
    }

    @Override
    public int indexOf(int[] x) {
        int idc = x[0];
        for (int i = 1; i < n; i++) {
            idc += x[i] * dc[i - 1];
        }
        return idc;
    }

    public void warp() {
        int[] ret = new int[n];
        int rt = index;
        boolean inlimit = (rt / maxind == 0) && isValid();
        if (!inlimit) {
            if (debug) System.out.println("not in limit");
            for (int i = 0; i < n; i++) {
                int auxdim = Math.max(dim[i], coords[i]);
                ret[i] = rt % auxdim;
                rt = rt / auxdim;
            }
        } else {
            for (int i = 0; i < n; i++) {
                ret[i] = rt % dim[i];
                rt = rt / dim[i];
            }
        }
        coords = ret;
    }

    public boolean isValid() {
        boolean valid = true;
        for (int m = 0; m < coords.length; m++) {
            final boolean dim1 = (dim[m] == 1) && (coords[m] == dim[m]);
            valid = valid && (coords[m] < dim[m] || dim1) && (coords[m] >= 0);
        }
        isvalid = valid;
        return isvalid;
    }

    public boolean valid(int[] coord) {
        boolean valid = true;
        for (int m = 0; m < coord.length; m++) {
            final boolean dim1 = (dim[m] == 1) && (coord[m] == dim[m]);
            valid = valid && (coord[m] < dim[m] || dim1) && (coord[m] >= 0);
        }
        return valid;
    }

    public int[] getCoordinates() {
        if (updated) warp();
        return coords;
    }

    public int inc(int k, int step) {
        if ((k > n) || (k < 0)) return index;
        if (updated) warp();
        coords[k] += step;
        updated = true;
        index = indexOf(coords);
        return index;
    }

    public int dec(int k, int step) {
        if ((k > n) || (k < 0)) return index;
        if (updated) warp();
        coords[k] -= -step;
        updated = true;
        index = indexOf(coords);
        return index;
    }

    public int max() {
        int p = 1;
        for (int d : dim) p *= d;
        return p;
    }
}
