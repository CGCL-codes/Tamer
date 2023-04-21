package gov.nasa.gsfc.visbard.ovt.util;

import gov.nasa.gsfc.visbard.ovt.Const;
import gov.nasa.gsfc.visbard.ovt.datatype.*;
import gov.nasa.gsfc.visbard.ovt.mag.*;
import gov.nasa.gsfc.visbard.ovt.mag.model.*;
import gov.nasa.gsfc.visbard.ovt.object.*;

/**
 * This class holds all transformation stuff.
 * @author  root
 * @version
 */
public class Trans {

    /** Degrees in 1 radian */
    public static final double RAD = 57.295779513;

    /** equals to 0 */
    public static final int MAGNETIC_DIPOLE = 0;

    /** equals to 1 */
    public static final int ECCENTRIC_DIPOLE = 1;

    protected double[] Eccrr;

    protected double[] Eccdx;

    protected double[] Eccdy;

    protected double[] Eccdz;

    /** Transformation matrix from sm to gsm */
    protected Matrix3x3 sm_gsm;

    /** Transformation matrix from geo to gsm */
    protected Matrix3x3 geo_gsm;

    /** Transformation matrix from geo to gei */
    protected Matrix3x3 geo_gei;

    /** Transformation matrix from gei to gsm */
    protected Matrix3x3 gei_gsm;

    /** Transformation matrix from gei to gse */
    protected Matrix3x3 gei_gse;

    /** Sine of dipole tilt. By default dipole tilt set to zero
   * @see #getSint()
   */
    protected double sint = 0;

    /** Cosine of dipole tilt. By default dipole tilt set to zero
   * @see #getCost()
   */
    protected double cost = 1;

    protected double mjd;

    protected IgrfModel igrfModel;

    /** Creates new Trans */
    public Trans(double mjd, IgrfModel igrf) {
        igrfModel = igrf;
        Eccrr = igrf.getEccrr(mjd);
        Eccdx = igrf.getEccdx(mjd);
        Eccdy = igrf.getEccdy(mjd);
        Eccdz = igrf.getEccdz(mjd);
        sint = getDipoleTiltSine(mjd, Eccdz);
        cost = Math.sqrt(1 - sint * sint);
        sm_gsm = sm_gsm_trans_matrix(mjd, Eccdz);
        geo_gsm = geo_gsm_trans_matrix(mjd, Eccdz);
        geo_gei = geo_gei_trans_matrix(mjd);
        gei_gsm = gei_gsm_trans_matrix(mjd, Eccdz);
        gei_gse = gei_gse_trans_matrix(mjd);
        this.mjd = mjd;
    }

    /** Returns MagPacks mjd
   * @see #setMjd(double)
   */
    public double getMjd() {
        return mjd;
    }

    /** Returns the sine of the dipole tilt angle
   * @see #getCost() #getDipoleTilt()
   */
    public double getSint() {
        return sint;
    }

    /** Returns the cosine of the dipole tilt angle
   * @see #getSint() #getDipoleTilt()
   */
    public double getCost() {
        return cost;
    }

    /**  Returns dipole tilt angle in RADIANS
   * @see #getSint() #getCost()
   */
    public double getDipoleTilt() {
        return Math.asin(getSint());
    }

    public static double getDipoleTiltSine(double mjd, double[] Eccdz) {
        double sunv[] = Utils.sunmjd(mjd);
        double[][] temp = new double[3][];
        temp[0] = gei2geo(sunv, mjd);
        double sint = Vect.dot(temp[0], Eccdz);
        return sint;
    }

    public double[] gei2geo(double gei[]) {
        return gei_geo_trans_matrix().multiply(gei);
    }

    public static double[] gei2geo(double gei[], double mjd) {
        return gei_geo_trans_matrix(mjd).multiply(gei);
    }

    public double[] geo2gei(double geo[]) {
        return geo_gei_trans_matrix().multiply(geo);
    }

    public double[] geo2gsm(double geo[]) {
        return geo_gsm_trans_matrix().multiply(geo);
    }

    public double[] gsm2geo(double gsm[]) {
        return gsm_geo_trans_matrix().multiply(gsm);
    }

    public double[] gsm2sm(double gsm[]) {
        return gsm_sm_trans_matrix().multiply(gsm);
    }

    public Matrix3x3 sm_trans_matrix(int toCS) {
        switch(toCS) {
            case CoordinateSystem.SM:
                return new Matrix3x3();
            case CoordinateSystem.GEO:
                return sm_geo_trans_matrix();
            case CoordinateSystem.GEI:
                return sm_gei_trans_matrix();
            case CoordinateSystem.GSM:
                return sm_gsm_trans_matrix();
            case CoordinateSystem.GSE:
                return sm_gse_trans_matrix();
        }
        throw new IllegalArgumentException("Wrong CS '" + toCS + "'");
    }

    public Matrix3x3 geo_trans_matrix(int toCS) {
        switch(toCS) {
            case CoordinateSystem.GEO:
                return new Matrix3x3();
            case CoordinateSystem.SM:
                return geo_sm_trans_matrix();
            case CoordinateSystem.GEI:
                return geo_gei_trans_matrix();
            case CoordinateSystem.GSM:
                return geo_gsm_trans_matrix();
            case CoordinateSystem.GSE:
                return geo_gse_trans_matrix();
        }
        throw new IllegalArgumentException("Wrong CS '" + toCS + "'");
    }

    public Matrix3x3 gsm_trans_matrix(int toCS) {
        switch(toCS) {
            case CoordinateSystem.GSM:
                return new Matrix3x3();
            case CoordinateSystem.SM:
                return gsm_sm_trans_matrix();
            case CoordinateSystem.GEO:
                return gsm_geo_trans_matrix();
            case CoordinateSystem.GEI:
                return gsm_gei_trans_matrix();
            case CoordinateSystem.GSE:
                return gsm_gse_trans_matrix();
        }
        throw new IllegalArgumentException("Wrong CS '" + toCS + "'");
    }

    public Matrix3x3 gse_trans_matrix(int toCS) {
        switch(toCS) {
            case CoordinateSystem.GSM:
                return gse_gsm_trans_matrix();
            case CoordinateSystem.SM:
                return gse_sm_trans_matrix();
            case CoordinateSystem.GEO:
                return gse_geo_trans_matrix();
            case CoordinateSystem.GEI:
                return gse_gei_trans_matrix();
            case CoordinateSystem.GSE:
                return new Matrix3x3();
        }
        throw new IllegalArgumentException("Wrong CS '" + toCS + "'");
    }

    public Matrix3x3 trans_matrix(int fromCS, int toCS) {
        if (fromCS == toCS) return new Matrix3x3();
        switch(fromCS) {
            case CoordinateSystem.GEO:
                return geo_trans_matrix(toCS);
            case CoordinateSystem.SM:
                return sm_trans_matrix(toCS);
            case CoordinateSystem.GSM:
                return gsm_trans_matrix(toCS);
            case CoordinateSystem.GSE:
                return gse_trans_matrix(toCS);
        }
        throw new IllegalArgumentException("Wrong fromCS '" + fromCS + "'");
    }

    public Matrix3x3 geo_gsm_trans_matrix() {
        return geo_gsm;
    }

    /** */
    public Matrix3x3 geo_gei_trans_matrix() {
        return geo_gei;
    }

    /** */
    public Matrix3x3 geo_gse_trans_matrix() {
        return gei_gse_trans_matrix().multiply(geo_gei_trans_matrix());
    }

    public Matrix3x3 gse_geo_trans_matrix() {
        return geo_gse_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 sm_gsm_trans_matrix() {
        return sm_gsm;
    }

    /** */
    public Matrix3x3 sm_gse_trans_matrix() {
        return gsm_gse_trans_matrix().multiply(sm_gsm_trans_matrix());
    }

    public Matrix3x3 gse_sm_trans_matrix() {
        return sm_gse_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 sm_geo_trans_matrix() {
        return gsm_geo_trans_matrix().multiply(sm_gsm_trans_matrix());
    }

    /** */
    public Matrix3x3 sm_gei_trans_matrix() {
        return gsm_gei_trans_matrix().multiply(sm_gsm_trans_matrix());
    }

    /** */
    public Matrix3x3 gei_sm_trans_matrix() {
        return sm_gei_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 gei_geo_trans_matrix() {
        return geo_gei_trans_matrix().getInverse();
    }

    public static Matrix3x3 gei_geo_trans_matrix(double mjd) {
        return geo_gei_trans_matrix(mjd).getInverse();
    }

    /** */
    public Matrix3x3 gei_gsm_trans_matrix() {
        return gei_gsm;
    }

    /** */
    public Matrix3x3 gei_gse_trans_matrix() {
        return gei_gse;
    }

    /** */
    public Matrix3x3 gse_gei_trans_matrix() {
        return gei_gse_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 gsm_sm_trans_matrix() {
        return sm_gsm_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 gsm_geo_trans_matrix() {
        return geo_gsm_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 gsm_gei_trans_matrix() {
        return gei_gsm_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 gsm_gse_trans_matrix() {
        return gei_gse_trans_matrix().multiply(gsm_gei_trans_matrix());
    }

    /** */
    public Matrix3x3 gse_gsm_trans_matrix() {
        return gsm_gse_trans_matrix().getInverse();
    }

    /** */
    public Matrix3x3 geo_sm_trans_matrix() {
        return sm_geo_trans_matrix().getInverse();
    }

    public static Matrix3x3 geo_gei_trans_matrix(double mjd) {
        double[][] m = new double[3][3];
        double theta, ct, st;
        theta = Time.getGSMTime(mjd);
        st = Math.sin(theta);
        ct = Math.cos(theta);
        int i, j;
        for (i = 0; i < 3; i++) for (j = 0; j < 3; j++) m[i][j] = 0;
        m[0][0] = ct;
        m[0][1] = -st;
        m[1][0] = st;
        m[1][1] = ct;
        m[2][2] = 1;
        return new Matrix3x3(m);
    }

    /** @param mjd time
   *  @param Eccdz excentric dipole coordinates???
   */
    protected static Matrix3x3 geo_gsm_trans_matrix(double mjd, double[] Eccdz) {
        double sunv[] = Utils.sunmjd(mjd);
        double[][] temp = new double[3][];
        temp[0] = gei2geo(sunv, mjd);
        temp[1] = Vect.crossn(Eccdz, temp[0]);
        temp[2] = Vect.crossn(temp[0], temp[1]);
        Matrix3x3 m = new Matrix3x3(temp);
        return m;
    }

    public static Matrix3x3 gei_gse_trans_matrix(double mjd) {
        double[][] geigse = new double[3][];
        double eqlipt[] = { 0.0, -0.398, 0.917 };
        geigse[0] = Utils.sunmjd(mjd);
        geigse[1] = Vect.crossn(eqlipt, geigse[0]);
        geigse[2] = Vect.crossn(geigse[0], geigse[1]);
        Matrix3x3 m = new Matrix3x3(geigse);
        return m;
    }

    public double[] gei2gse(double[] gei) {
        return gei_gse.multiply(gei);
    }

    public static Matrix3x3 gei_gsm_trans_matrix(double mjd, double[] Eccdz) {
        double[][] geigsm = new double[3][];
        double[] sunv = Utils.sunmjd(mjd);
        double theta = Time.getGSMTime(mjd);
        double st = Math.sin(theta);
        double ct = Math.cos(theta);
        double[] dipgei = new double[3];
        dipgei[0] = ct * Eccdz[0] - st * Eccdz[1];
        dipgei[1] = st * Eccdz[0] + ct * Eccdz[1];
        dipgei[2] = Eccdz[2];
        geigsm[1] = Vect.crossn(dipgei, sunv);
        geigsm[2] = Vect.crossn(sunv, geigsm[1]);
        geigsm[0] = Vect.crossn(geigsm[1], geigsm[2]);
        Matrix3x3 m = new Matrix3x3(geigsm);
        return m;
    }

    public static Matrix3x3 gei_gseq_trans_matrix(double mjd) {
        double[][] geigseq = new double[3][];
        double[] rotsun = { 0.122, -0.424, 0.899 };
        geigseq[0] = Utils.sunmjd(mjd);
        geigseq[1] = Vect.crossn(rotsun, geigseq[0]);
        geigseq[2] = Vect.crossn(geigseq[0], geigseq[1]);
        Matrix3x3 m = new Matrix3x3(geigseq);
        return m;
    }

    /** @param mjd time
   */
    protected static Matrix3x3 sm_gsm_trans_matrix(double mjd, double[] Eccdz) {
        Matrix3x3 m = new Matrix3x3();
        double st = getDipoleTiltSine(mjd, Eccdz);
        double ct = Math.sqrt(1 - st * st);
        m.set(0, 0, ct);
        m.set(0, 2, st);
        m.set(2, 0, -st);
        m.set(2, 2, ct);
        return m;
    }

    /**
   * transform geo(3) to gma(3) 
   * flag=0 magnetic dipole (MAGNETIC_DIPOLE)
   *     =1 eccentric dipole (ECCENTRIC_DIPOLE)
   */
    public double[] geo2gma(double geo[], int flag) {
        double gma[] = new double[3];
        geo_gma(flag, geo, gma, 1);
        return gma;
    }

    /**
   * transform gma(3) to geo(3) 
   * flag=0 magnetic dipole MAGNETIC_DIPOLE)
   *     =1 eccentric dipole (ECCENTRIC_DIPOLE)
   */
    public double[] gma2geo(double gma[], int flag) {
        double geo[] = new double[3];
        geo_gma(flag, geo, gma, -1);
        return geo;
    }

    /**
   * transform geo(3) to gma(3) if idir =1 and vice versa if idir=-1
   * flag=0 magnetic dipole MAGNETIC_DIPOLE)
   *     =1 eccentric dipole (ECCENTRIC_DIPOLE)
   */
    public void geo_gma(int flag, double[] geo, double[] gma, int idir) {
        double tmp[] = new double[3];
        double ff;
        ff = (flag == 1) ? 1.0 : 0.0;
        if (idir > 0) {
            for (int i = 0; i < 3; i++) tmp[i] = geo[i] - Eccrr[i];
            gma[0] = Vect.dot(Eccdx, tmp);
            gma[1] = Vect.dot(Eccdy, tmp);
            gma[2] = Vect.dot(Eccdz, tmp);
        } else {
            geo[0] = Eccdx[0] * gma[0] + Eccdy[0] * gma[1] + Eccdz[0] * gma[2] + Eccrr[0] * ff;
            geo[1] = Eccdx[1] * gma[0] + Eccdy[1] * gma[1] + Eccdz[1] * gma[2] + Eccrr[1] * ff;
            geo[2] = Eccdx[2] * gma[0] + Eccdy[2] * gma[1] + Eccdz[2] * gma[2] + Eccrr[2] * ff;
        }
    }

    /************************************************************************
  compute corrected magnetic coordinates at the reference altitude alt
  input:
  mjd:     modified julian day (use fdate() to find mjd! )
  geo(3):  geographic position (cartesian) in units of re=6371.2 km
  alt:     reference altitude (km)
  output:
  mlat:    corrected magnetic latitude (deg)
  mlong:   corrected magnetic longitude (deg)
  mlt:     corrected magnetic local time (hours)
  ell:     L value (equatorial distance of the field line igrf)
   *************************************************************************/
    public static double[] corrgma(MagProps magProps, double mjdx, double[] geo, double alt) {
        double[] res = { 0, 0, 0 };
        double gmst, r, mlat, mlong, mlt, ell;
        double[] ft, sv, foot;
        double sig;
        double[] spos;
        double cos2;
        Trans trans = magProps.getTrans(mjdx);
        ft = trans.geo2gma(geo, MAGNETIC_DIPOLE);
        sig = (ft[2] < 0.0) ? -1.0 : 1.0;
        sv = trans.igrfModel.bvGEO(geo, mjdx);
        r = Vect.dot(geo, sv) * ft[2];
        if (r > 0.) {
            res[0] = res[1] = res[2] = 0.0;
            return res;
        }
        boolean isIGRF = true;
        int currentExternalModel = magProps.getExternalModelType();
        if (magProps.getInternalModelType() != MagProps.IGRF) {
            magProps.setExternalModelType(MagProps.NOMODEL);
            isIGRF = false;
        }
        double[] gsmx = trans.geo2gsm(geo);
        FieldlineOvt fieldLine = Trace.traceline(magProps, mjdx, gsmx, 0.0, 10.0, 0, magProps.getXlim(), Const.NPF);
        MagPoint magPoint = fieldLine.lastPoint();
        foot = trans.gsm2geo(magPoint.gsm);
        ft = trans.geo2gma(foot, MAGNETIC_DIPOLE);
        r = Vect.absv(ft);
        if (isIGRF == false) magProps.setExternalModelType(currentExternalModel);
        cos2 = (ft[0] * ft[0] + ft[1] * ft[1]) / (r * r);
        if (cos2 < 4.8e-7) cos2 = 4.8e-7;
        ell = r / cos2;
        spos = Utils.sunmjd(mjdx);
        Matrix3x3 mtrx = trans.gei_geo_trans_matrix();
        sv = mtrx.multiply(spos);
        spos = trans.geo2gma(sv, MAGNETIC_DIPOLE);
        r = Math.sqrt((alt / gov.nasa.gsfc.visbard.ovt.Const.RE + 1.0) / ell);
        if (r > 1.0) r = 1.0;
        res[0] = Math.acos(r) * sig;
        res[1] = Math.atan2(ft[1], ft[0]);
        res[2] = Math.atan2(ft[1], ft[0]) - Math.atan2(spos[1], spos[0]) + Math.PI;
        return res;
    }

    /** Returns [0] - Magnetic Latitude, 
   * [1] - Magnetic Local Time
   */
    public static double[] xyz2MlatMlt(double[] xyz) {
        int X = 0;
        int Y = 1;
        int Z = 2;
        double mlat, mlt;
        double r = Vect.absv(xyz);
        double sint = xyz[2] / r;
        double cost = Math.sqrt(1 - sint * sint);
        double theta = Math.asin(sint);
        mlat = Utils.toDegrees(theta);
        mlt = 12 + 12 * Math.atan2(xyz[Y], xyz[X]) / Math.PI;
        if (Math.abs(mlt) == 24) mlt = 0;
        return new double[] { mlat, mlt };
    }

    /** Minimum altitude is 0. */
    public static double[] mlat_mlt2xyz(double mlat, double mlt, double alt_RE) {
        return Utils.sph2rec(1. + alt_RE, mlat, 15. * (mlt - 12.));
    }

    /** Returns [0] - Geografical Latitude, [1] - Geografical Longitude in degrees*/
    public static double[] xyz2LatLon(double[] xyz) {
        int X = 0;
        int Y = 1;
        int Z = 2;
        double irad = 180. / Math.PI;
        double phi, delta;
        double radius = Math.sqrt(xyz[0] * xyz[0] + xyz[1] * xyz[1] + xyz[2] * xyz[2]);
        if ((xyz[Y] == 0.) && (xyz[X] == 0.)) phi = 0; else phi = irad * Math.atan2(xyz[Y], xyz[X]);
        if (phi < 0.) phi = phi + 360.;
        double arg = xyz[Z] / radius;
        if (arg < 1.) {
            delta = irad * Math.asin(arg);
        } else {
            delta = 90.;
        }
        return new double[] { delta, phi };
    }

    /** Minimum altitude is 0. */
    public static double[] lat_lon2xyz(double lat, double lon, double alt_RE) {
        return Utils.sph2rec(1. + alt_RE, lat, lon);
    }

    public static void main(String[] args) {
        double[] r = { -1, -1, 0 };
        for (double mlt = 0; mlt <= 24; mlt += 1.) {
            double[] xyz = Trans.mlat_mlt2xyz(0, mlt, 0.);
            double[] res = Trans.xyz2MlatMlt(xyz);
            System.out.println("" + (int) (mlt) + " -> " + res[0] + "deg, " + res[1] + "h");
        }
    }
}
