package net.sourceforge.theba.descriptors;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import net.sourceforge.theba.core.RegionDescriptor;
import net.sourceforge.theba.core.RegionMask;

public class OrientationDescriptor3D implements RegionDescriptor {

    public String getName() {
        return "Orientation (3D)";
    }

    public String getAbout() {
        return "Returns the orientation of the region based on the covariance matrix eigenvectors";
    }

    public boolean does3D() {
        return true;
    }

    public boolean isNumeric() {
        return false;
    }

    private String gatherstats(RegionMask mask) {
        int tot = 0;
        double xx = 0;
        double yy = 0;
        double zz = 0;
        double xy = 0;
        double xz = 0;
        double yz = 0;
        double mx = 0;
        double my = 0;
        double mz = 0;
        if (mask.getDepth() < 1) return "Unable to measure";
        for (int x = 0; x < mask.getWidth(); x = x + 1) {
            for (int y = 0; y < mask.getHeight(); y = y + 1) {
                for (int z = 0; z < mask.getDepth(); z = z + 1) {
                    if (mask.isSet(x, y, z)) {
                        tot++;
                        mx += x;
                        my += y;
                        mz += z;
                        xx += x * x;
                        yy += y * y;
                        zz += z * z;
                        xy += x * y;
                        xz += x * z;
                        yz += z * y;
                    }
                }
            }
        }
        mx = mx / tot;
        my = my / tot;
        mz = mz / tot;
        xx = xx / tot - mx * mx;
        yy = yy / tot - my * my;
        zz = zz / tot - mz * mz;
        xy = xy / tot - mx * my;
        xz = xz / tot - mx * mz;
        yz = yz / tot - my * mz;
        double[] m = { xx, xy, xz, xy, yy, yz, xz, yz, zz };
        Matrix matrix = new Matrix(m, 3);
        EigenvalueDecomposition ed = matrix.eig();
        double x1 = ed.getV().get(0, 2);
        double y1 = ed.getV().get(1, 2);
        double z1 = ed.getV().get(2, 2);
        double length = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
        x1 /= length;
        y1 /= length;
        z1 /= length;
        double radians = Math.acos(x1 / Math.sqrt(x1 * x1 + y1 * y1));
        double degrees = radians * 360 / (2 * Math.PI);
        if (z1 < 0) {
            x1 = -x1;
            y1 = -y1;
            z1 = -z1;
        }
        y1 = -y1;
        return "Orientation: " + x1 + " " + y1 + " " + z1 + " size: " + tot + " angle: " + degrees;
    }

    public Object measure(RegionMask mask) {
        return gatherstats(mask);
    }
}
