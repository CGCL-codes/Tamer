package jp.go.ipa.jgcl;

import org.magiclight.common.MLUtil;
import org.magiclight.common.VectorNS;

/**
 * Base class for free form surface with control points
 * @author Information-technology Promotion Agency, Japan
 */
public abstract class FreeformSurfaceWithControlPoints3D extends BoundedSurface3D {

    /**
	 * Contro, points array
	 */
    protected Pnt3D[][] controlPoints;

    /**
	 * Weights
	 */
    protected double[][] weights;

    /**
	 * Control point array
	 */
    private double[][][] controlPointsArray = null;

    /**
	 * Constructor
	 */
    protected FreeformSurfaceWithControlPoints3D() {
        super();
    }

    /**
	 * Constructor
	 *
	 * @param controlPoints
	 */
    protected FreeformSurfaceWithControlPoints3D(Pnt3D[][] controlPoints) {
        super();
        int[] npnts = setControlPoints(controlPoints);
        weights = null;
    }

    /**
	 * Constructor
	 *
	 * @param controlPoints
	 * @param weights
	 */
    protected FreeformSurfaceWithControlPoints3D(Pnt3D[][] controlPoints, double[][] weights) {
        super();
        int[] npnts = setControlPoints(controlPoints);
        setWeights(npnts, weights);
    }

    /**
	 * Control points
	 *
	 * @param cpArray
	 */
    protected FreeformSurfaceWithControlPoints3D(double[][][] cpArray) {
        this(cpArray, true);
    }

    /**
	 * Constructor
	 *
	 * @param cpArray
	 * @param doCheck
	 */
    protected FreeformSurfaceWithControlPoints3D(double[][][] cpArray, boolean doCheck) {
        super();
        int uNpnts = cpArray.length;
        int vNpnts = cpArray[0].length;
        int[] npnts = null;
        int dimension = cpArray[0][0].length;
        Pnt3D[][] cp = new Pnt3D[uNpnts][vNpnts];
        double[][] wt = null;
        boolean isPoly = (dimension == 3);
        int i, j, k;
        if (!isPoly) {
            double[] tmp = new double[4];
            wt = new double[uNpnts][vNpnts];
            for (i = 0; i < uNpnts; i++) {
                for (j = 0; j < vNpnts; j++) {
                    for (k = 0; k < 4; k++) {
                        tmp[k] = cpArray[i][j][k];
                    }
                    convRational0Deriv(tmp);
                    cp[i][j] = new CPnt3D(tmp[0], tmp[1], tmp[2]);
                    wt[i][j] = tmp[3];
                }
            }
        } else {
            for (i = 0; i < uNpnts; i++) {
                for (j = 0; j < vNpnts; j++) {
                    cp[i][j] = new CPnt3D(cpArray[i][j][0], cpArray[i][j][1], cpArray[i][j][2]);
                }
            }
        }
        if (doCheck) {
            npnts = setControlPoints(cp);
        } else {
            this.controlPoints = cp;
        }
        if (isPoly) {
            this.weights = null;
        } else {
            if (doCheck) {
                setWeights(npnts, wt);
            } else {
                this.weights = wt;
            }
        }
    }

    /**
	 * Constructor
	 *
	 * @param controlPoints
	 * @param weights
	 * @param doCheck
	 */
    protected FreeformSurfaceWithControlPoints3D(Pnt3D[][] controlPoints, double[][] weights, boolean doCheck) {
        super();
        if (doCheck) {
            int[] npnts = setControlPoints(controlPoints);
            if (weights == null) {
                weights = null;
            } else {
                setWeights(npnts, weights);
            }
        } else {
            this.controlPoints = controlPoints;
            this.weights = weights;
        }
    }

    /**
	 * Returns a copy of the control points array
	 *
	 * @return
	 */
    public Pnt3D[][] controlPoints() {
        Pnt3D[][] copied = new Pnt3D[controlPoints.length][controlPoints[0].length];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                copied[i][j] = controlPoints[i][j];
            }
        }
        return copied;
    }

    /**
	 * Return control point at i,j
	 *
	 * @param i
	 * @param j
	 * @return
	 */
    public Pnt3D controlPointAt(int i, int j) {
        return controlPoints[i][j];
    }

    /**
	 * Returns copy of weights (can be null)
	 *
	 * @return
	 */
    public double[][] weights() {
        if (weights == null) {
            return null;
        }
        return weights.clone();
    }

    /**
	 * Returns weight at i,j
	 *
	 * @param i 
	 * @param j
	 * @return
	 */
    public double weightAt(int i, int j) {
        if (weights == null) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        return weights[i][j];
    }

    /**
	 * Return number of U control points
	 *
	 * @return
	 */
    public int uNControlPoints() {
        return controlPoints.length;
    }

    /**
	 * Returns number of V control points
	 *
	 * @return
	 */
    public int vNControlPoints() {
        return controlPoints[0].length;
    }

    /**
	 * Returns number of control points (u*v)
	 *
	 * @return
	 */
    public int nControlPoints() {
        return uNControlPoints() * vNControlPoints();
    }

    /**
	 * Returns true if rational (has weights)
	 *
	 * @return
	 */
    public boolean isRational() {
        return weights != null;
    }

    /**
	 * Returns true if polynomial (has no weights)
	 *
	 * @return
	 */
    public boolean isPolynomial() {
        return weights == null;
    }

    /**
	 * Returns true if planar within tolerance
	 */
    abstract boolean isPlaner(ToleranceForDistance tol);

    /**
	 * Divide for mesh
	 */
    abstract FreeformSurfaceWithControlPoints3D[] divideForMesh(ToleranceForDistance tol);

    class MeshParam {

        int sgidx;

        int numer;

        int denom;

        MeshParam(int sgidx, int numer, int denom) {
            super();
            this.sgidx = sgidx;
            this.numer = numer;
            this.denom = denom;
        }

        private boolean isSame(MeshParam mate) {
            if (this.sgidx != mate.sgidx) {
                if ((this.sgidx == (mate.sgidx - 1)) && (this.numer == this.denom) && (mate.numer == 0)) {
                    return true;
                }
                if ((mate.sgidx == (this.sgidx - 1)) && (mate.numer == mate.denom) && (this.numer == 0)) {
                    return true;
                }
            } else {
                if (this.denom == mate.denom) {
                    if (this.numer == mate.numer) {
                        return true;
                    }
                } else {
                    int cmn_denom = GeometryMath.LCM(this.denom, mate.denom);
                    int a_numer = this.numer * (cmn_denom / this.denom);
                    int b_numer = mate.numer * (cmn_denom / mate.denom);
                    if (a_numer == b_numer) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    class SegInfo {

        MeshParam u_sp;

        MeshParam u_ep;

        MeshParam v_sp;

        MeshParam v_ep;

        SegInfo(MeshParam u_sp, MeshParam u_ep, MeshParam v_sp, MeshParam v_ep) {
            super();
            this.u_sp = u_sp;
            this.u_ep = u_ep;
            this.v_sp = v_sp;
            this.v_ep = v_ep;
        }
    }

    class GpList {

        VectorNS<MeshParam> list;

        GpList() {
            super();
            this.list = new VectorNS<MeshParam>();
        }

        int size() {
            return list.size();
        }

        MeshParam elementAt(int index) {
            return list.elementAt(index);
        }

        void addGp(MeshParam gp) {
            int n_list = size();
            for (int i = 0; i < n_list; i++) {
                if (gp.isSame(elementAt(i))) {
                    return;
                }
            }
            list.addElement(gp);
            return;
        }
    }

    void getSrfMesh(SegInfo si, ToleranceForDistance tol, GpList u_gp_list, GpList v_gp_list) {
        FreeformSurfaceWithControlPoints3D[] divsrf;
        FreeformSurfaceWithControlPoints3D lb_srf, rb_srf, lu_srf, ru_srf;
        SegInfo si_lb, si_rb;
        SegInfo si_lu, si_ru;
        MeshParam u_mp, v_mp;
        int ret_val;
        if (isPlaner(tol)) {
            u_gp_list.addGp(si.u_sp);
            u_gp_list.addGp(si.u_ep);
            v_gp_list.addGp(si.v_sp);
            v_gp_list.addGp(si.v_ep);
            return;
        }
        divsrf = divideForMesh(tol);
        lb_srf = divsrf[0];
        rb_srf = divsrf[1];
        lu_srf = divsrf[2];
        ru_srf = divsrf[3];
        if ((lb_srf == null) && (rb_srf == null) && (lu_srf == null) && (ru_srf == null)) {
            u_gp_list.addGp(si.u_sp);
            u_gp_list.addGp(si.u_ep);
            v_gp_list.addGp(si.v_sp);
            v_gp_list.addGp(si.v_ep);
            return;
        }
        u_mp = makeMidGp(si.u_sp, si.u_ep);
        v_mp = makeMidGp(si.v_sp, si.v_ep);
        if ((lb_srf != null) && (rb_srf != null) && (lu_srf != null) && (ru_srf != null)) {
            si_lb = new SegInfo(si.u_sp, u_mp, si.v_sp, v_mp);
            si_rb = new SegInfo(u_mp, si.u_ep, si.v_sp, v_mp);
            si_lu = new SegInfo(si.u_sp, u_mp, v_mp, si.v_ep);
            si_ru = new SegInfo(u_mp, si.u_ep, v_mp, si.v_ep);
            lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
            rb_srf.getSrfMesh(si_rb, tol, u_gp_list, v_gp_list);
            lu_srf.getSrfMesh(si_lu, tol, u_gp_list, v_gp_list);
            ru_srf.getSrfMesh(si_ru, tol, u_gp_list, v_gp_list);
        } else {
            if ((lb_srf != null) && (rb_srf == null) && (lu_srf != null) && (ru_srf == null)) {
                si_lb = new SegInfo(si.u_sp, si.u_ep, si.v_sp, v_mp);
                si_lu = new SegInfo(si.u_sp, si.u_ep, v_mp, si.v_ep);
                lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
                lu_srf.getSrfMesh(si_lu, tol, u_gp_list, v_gp_list);
            } else {
                if ((lb_srf != null) && (rb_srf != null) && (lu_srf == null) && (ru_srf == null)) {
                    si_lb = new SegInfo(si.u_sp, u_mp, si.v_sp, si.v_ep);
                    si_rb = new SegInfo(u_mp, si.u_ep, si.v_sp, si.v_ep);
                    lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
                    rb_srf.getSrfMesh(si_rb, tol, u_gp_list, v_gp_list);
                }
            }
        }
        return;
    }

    private MeshParam makeMidGp(MeshParam sp, MeshParam ep) {
        MeshParam mp;
        int sgidx, denom, numer;
        sgidx = sp.sgidx;
        denom = GeometryMath.LCM(sp.denom, ep.denom);
        if ((denom == sp.denom) || (denom == ep.denom)) {
            denom *= 2;
        }
        numer = ((sp.numer * (denom / sp.denom)) + (ep.numer * (denom / ep.denom))) / 2;
        return new MeshParam(sgidx, numer, denom);
    }

    private static boolean pointsAreColinear(double tol2, Pnt3D[] pnts, double[] info) {
        int npnts = pnts.length;
        int npnts_1 = npnts - 1;
        Pnt3D l_pnt;
        Vec3D uax;
        Vec3D evec, ecrs;
        double length;
        int i;
        if (npnts <= 1) {
            for (i = 0; i < 4; i++) {
                info[i] = 0.0;
            }
            return true;
        }
        l_pnt = pnts[0].longestPoint(pnts);
        uax = l_pnt.subtract(pnts[0]);
        if ((length = uax.lengthSquared()) < tol2) {
            for (i = 0; i < 3; i++) {
                info[i] = 0.0;
            }
            info[3] = Math.sqrt(length);
            return true;
        }
        length = Math.sqrt(length);
        uax = uax.divide(length);
        for (i = 1; i < npnts; i++) {
            evec = pnts[i].subtract(pnts[0]);
            ecrs = evec.cross(uax);
            if (ecrs.lengthSquared() > tol2) {
                return false;
            }
        }
        info[0] = uax.x();
        info[1] = uax.y();
        info[2] = uax.z();
        info[3] = length;
        return true;
    }

    static boolean vIsColinear(Pnt3D[][] pnts, double tolerance) {
        double tol2 = tolerance * tolerance;
        int u_uicp = pnts.length;
        int v_uicp = pnts[0].length;
        double[] info = new double[4];
        Vec3D[] dir = new Vec3D[2];
        double[] leng = new double[2];
        Vec3D[] tgt_vec = new Vec3D[2];
        Vec3D crs_prod;
        boolean result;
        int u;
        result = true;
        for (u = 0; u < u_uicp; u++) {
            if (!pointsAreColinear(tol2, pnts[u], info)) {
                result = false;
                break;
            }
            dir[1] = new LVec3D(info[0], info[1], info[2]);
            leng[1] = info[3];
            if (u == 0) {
                dir[0] = dir[1];
                leng[0] = leng[1];
                tgt_vec[0] = dir[0].multiply(leng[0]);
                continue;
            }
            tgt_vec[1] = dir[1].multiply(leng[1]);
            crs_prod = dir[0].cross(tgt_vec[1]);
            if (crs_prod.lengthSquared() > tol2) {
                result = false;
                break;
            }
            crs_prod = dir[1].cross(tgt_vec[0]);
            if (crs_prod.lengthSquared() > tol2) {
                result = false;
                break;
            }
        }
        return result;
    }

    static boolean uIsColinear(Pnt3D[][] pnts, double tolerance) {
        double tol2 = tolerance * tolerance;
        int u_uicp = pnts.length;
        int v_uicp = pnts[0].length;
        Pnt3D[] my_pnts = new Pnt3D[u_uicp];
        double[] info = new double[4];
        Vec3D[] dir = new Vec3D[2];
        double[] leng = new double[2];
        Vec3D[] tgt_vec = new Vec3D[2];
        Vec3D crs_prod;
        boolean result;
        int u, v;
        result = true;
        for (v = 0; v < v_uicp; v++) {
            for (u = 0; u < u_uicp; u++) {
                my_pnts[u] = pnts[u][v];
            }
            if (!pointsAreColinear(tol2, my_pnts, info)) {
                result = false;
                break;
            }
            dir[1] = new LVec3D(info[0], info[1], info[2]);
            leng[1] = info[3];
            if (v == 0) {
                dir[0] = dir[1];
                leng[0] = leng[1];
                tgt_vec[0] = dir[0].multiply(leng[0]);
                continue;
            }
            tgt_vec[1] = dir[1].multiply(leng[1]);
            crs_prod = dir[0].cross(tgt_vec[1]);
            if (crs_prod.lengthSquared() > tol2) {
                result = false;
                break;
            }
            crs_prod = dir[1].cross(tgt_vec[0]);
            if (crs_prod.lengthSquared() > tol2) {
                result = false;
                break;
            }
        }
        return result;
    }

    Mesh3D makeParamAndMesh(GpList u_gp_list, GpList v_gp_list, double[] u_kp, double[] v_kp) {
        PointOnSurface3D[][] mesh;
        int u_npnts, v_npnts;
        double[] u_params, v_params;
        int i, j;
        u_params = convGpList2Params(u_gp_list, u_kp);
        v_params = convGpList2Params(v_gp_list, v_kp);
        u_npnts = u_params.length;
        v_npnts = v_params.length;
        mesh = new PointOnSurface3D[u_npnts][v_npnts];
        for (i = 0; i < u_npnts; i++) {
            for (j = 0; j < v_npnts; j++) {
                try {
                    mesh[i][j] = new PointOnSurface3D(this, u_params[i], v_params[j], MLUtil.DEBUG);
                } catch (ExceptionGeometryInvalidArgumentValue e) {
                    throw new ExceptionGeometryFatal();
                }
            }
        }
        return new Mesh3D(mesh, false);
    }

    private static double[] convGpList2Params(GpList gp_list, double[] kp) {
        int n_params;
        double[] params;
        MeshParam gp;
        double lp;
        n_params = gp_list.size();
        params = new double[n_params];
        for (int i = 0; i < n_params; i++) {
            gp = gp_list.elementAt(i);
            lp = (double) gp.numer / (double) gp.denom;
            params[i] = (kp[gp.sgidx] * (1.0 - lp)) + (kp[gp.sgidx + 1] * lp);
        }
        GeometryUtil.sortDoubleArray(params, 0, (n_params - 1));
        return params;
    }

    /**
	 * Set control points
	 * @param controlPoints
	 * @return
	 */
    private int[] setControlPoints(Pnt3D[][] controlPoints) {
        int[] npnts = new int[2];
        if (controlPoints == null) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        if ((npnts[0] = controlPoints.length) < 2) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        for (int i = 0; i < npnts[0]; i++) {
            if (i == 0) {
                if ((npnts[1] = controlPoints[0].length) < 2) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
                this.controlPoints = new Pnt3D[npnts[0]][npnts[1]];
            } else {
                if (controlPoints[i].length != npnts[1]) {
                    throw new ExceptionGeometryInvalidArgumentValue("Number of weights must be same as number of control points");
                }
            }
            for (int j = 0; j < npnts[1]; j++) {
                if (controlPoints[i][j] == null) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
                this.controlPoints[i][j] = controlPoints[i][j];
            }
        }
        return npnts;
    }

    private void setWeights(int[] npnts, double[][] weights) {
        if (weights == null) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        if (weights.length != npnts[0]) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        double max_weight = 0.0;
        for (int i = 0; i < npnts[0]; i++) {
            for (int j = 0; j < npnts[1]; j++) {
                if (weights[i][j] > max_weight) {
                    max_weight = weights[i][j];
                }
            }
        }
        if (max_weight <= 0.0) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        this.weights = new double[npnts[0]][npnts[1]];
        for (int i = 0; i < npnts[0]; i++) {
            if (weights[i].length != npnts[1]) {
                throw new ExceptionGeometryInvalidArgumentValue();
            }
            for (int j = 0; j < npnts[1]; j++) {
                if (weights[i][j] <= 0.0 || !GeometryUtil.isDividable(max_weight, weights[i][j])) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
                this.weights[i][j] = weights[i][j];
            }
        }
    }

    /**
	 *
	 * @param isPoly
	 * @param uSize
	 * @param vSize
	 * @return
	 */
    protected static double[][][] allocateDoubleArray(boolean isPoly, int uSize, int vSize) {
        return new double[uSize][vSize][(isPoly) ? 3 : 4];
    }

    /**
	 *
	 * @param isPoly
	 * @param uUicp
	 * @param vUicp
	 * @param doubleArray
	 */
    protected void setCoordinatesToDoubleArray(boolean isPoly, int uUicp, int vUicp, double[][][] doubleArray) {
        if (isPoly) {
            for (int i = 0; i < uUicp; i++) {
                for (int j = 0; j < vUicp; j++) {
                    doubleArray[i][j][0] = controlPoints[i][j].x();
                    doubleArray[i][j][1] = controlPoints[i][j].y();
                    doubleArray[i][j][2] = controlPoints[i][j].z();
                }
            }
        } else {
            for (int i = 0; i < uUicp; i++) {
                for (int j = 0; j < vUicp; j++) {
                    doubleArray[i][j][0] = controlPoints[i][j].x() * weights[i][j];
                    doubleArray[i][j][1] = controlPoints[i][j].y() * weights[i][j];
                    doubleArray[i][j][2] = controlPoints[i][j].z() * weights[i][j];
                    doubleArray[i][j][3] = weights[i][j];
                }
            }
        }
    }

    /**
	 * Returns as double array
	 * @param isPoly
	 * @return
	 */
    protected double[][][] toDoubleArray(boolean isPoly) {
        if (controlPointsArray != null) {
            return controlPointsArray;
        }
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        controlPointsArray = FreeformSurfaceWithControlPoints3D.allocateDoubleArray(isPoly, uUicp, vUicp);
        setCoordinatesToDoubleArray(isPoly, uUicp, vUicp, controlPointsArray);
        return controlPointsArray;
    }

    /**
	 *
	 * @param d0
	 */
    protected void convRational0Deriv(double[] d0) {
        for (int i = 0; i < 3; i++) {
            d0[i] /= d0[3];
        }
    }

    /**
	 *
	 * @param d0
	 * @param du
	 * @param dv
	 */
    protected void convRational1Deriv(double[] d0, double[] du, double[] dv) {
        convRational0Deriv(d0);
        for (int i = 0; i < 3; i++) {
            du[i] = (du[i] - (du[3] * d0[i])) / d0[3];
            dv[i] = (dv[i] - (dv[3] * d0[i])) / d0[3];
        }
    }

    /**
	 *
	 * @param d0
	 * @param du
	 * @param dv
	 * @param duu
	 * @param duv
	 * @param dvv
	 */
    protected void convRational2Deriv(double[] d0, double[] du, double[] dv, double[] duu, double[] duv, double[] dvv) {
        convRational1Deriv(d0, du, dv);
        for (int i = 0; i < 3; i++) {
            duu[i] = (duu[i] - (duu[3] * d0[i]) - (2.0 * du[3] * du[i])) / d0[3];
            duv[i] = (duv[i] - (duv[3] * d0[i]) - (du[3] * dv[i]) - (dv[3] * du[i])) / d0[3];
            dvv[i] = (dvv[i] - (dvv[3] * d0[i]) - (2.0 * dv[3] * dv[i])) / d0[3];
        }
    }

    /**
	 * Returns an approximated enclosing box (around control points)
	 */
    EnclosingBox3D approximateEnclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double min_crd_z;
        double max_crd_x;
        double max_crd_y;
        double max_crd_z;
        int uN = uNControlPoints();
        int vN = vNControlPoints();
        Pnt3D point;
        double x, y, z;
        int i, j;
        point = controlPointAt(0, 0);
        max_crd_x = min_crd_x = point.x();
        max_crd_y = min_crd_y = point.y();
        max_crd_z = min_crd_z = point.z();
        for (i = 1; i < uN; i++) {
            point = controlPointAt(i, 0);
            x = point.x();
            y = point.y();
            z = point.z();
            if (x < min_crd_x) {
                min_crd_x = x;
            } else {
                if (x > max_crd_x) {
                    max_crd_x = x;
                }
            }
            if (y < min_crd_y) {
                min_crd_y = y;
            } else {
                if (y > max_crd_y) {
                    max_crd_y = y;
                }
            }
            if (z < min_crd_z) {
                min_crd_z = z;
            } else {
                if (z > max_crd_z) {
                    max_crd_z = z;
                }
            }
        }
        for (j = 1; j < vN; j++) {
            for (i = 0; i < uN; i++) {
                point = controlPointAt(i, j);
                x = point.x();
                y = point.y();
                z = point.z();
                if (x < min_crd_x) {
                    min_crd_x = x;
                } else {
                    if (x > max_crd_x) {
                        max_crd_x = x;
                    }
                }
                if (y < min_crd_y) {
                    min_crd_y = y;
                } else {
                    if (y > max_crd_y) {
                        max_crd_y = y;
                    }
                }
                if (z < min_crd_z) {
                    min_crd_z = z;
                } else {
                    if (z > max_crd_z) {
                        max_crd_z = z;
                    }
                }
            }
        }
        return new EnclosingBox3D(min_crd_x, min_crd_y, min_crd_z, max_crd_x, max_crd_y, max_crd_z);
    }

    double getMaxLengthOfUControlPolygons(boolean closed) {
        double result;
        double scale;
        int i, j, k;
        result = 0.0;
        for (j = 0; j < vNControlPoints(); j++) {
            scale = 0.0;
            for (k = 0, i = 1; i < uNControlPoints(); k++, i++) {
                scale += controlPointAt(k, j).distance(controlPointAt(i, j));
            }
            if (closed == true) {
                scale += controlPointAt(k, j).distance(controlPointAt(0, j));
            }
            if (result < scale) {
                result = scale;
            }
        }
        return result;
    }

    double getMaxLengthOfVControlPolygons(boolean closed) {
        double result;
        double scale;
        int i, j, k;
        result = 0.0;
        for (i = 0; i < uNControlPoints(); i++) {
            scale = 0.0;
            for (k = 0, j = 1; j < vNControlPoints(); k++, j++) {
                scale += controlPointAt(i, k).distance(controlPointAt(i, j));
            }
            if (closed == true) {
                scale += controlPointAt(i, k).distance(controlPointAt(i, 0));
            }
            if (result < scale) {
                result = scale;
            }
        }
        return result;
    }

    @Override
    public boolean isFreeform() {
        return true;
    }

    /**
	 * Create uniform weights
	 *
	 * @return
	 */
    public double[][] makeUniformWeights() {
        double[][] uniformWeights = new double[this.uNControlPoints()][this.vNControlPoints()];
        for (int ui = 0; ui < this.uNControlPoints(); ui++) {
            for (int vi = 0; vi < this.vNControlPoints(); vi++) {
                uniformWeights[ui][vi] = 1.0;
            }
        }
        return uniformWeights;
    }
}
