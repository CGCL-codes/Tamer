package saadadb.collection;

import saadadb.database.MysqlWrapper;
import saadadb.util.SaadaConstant;
import cds.astro.Coo;
import cds.astro.Qbox;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Position extends SaadaInstance {

    public double pos_ra_csa = SaadaConstant.DOUBLE;

    public double pos_dec_csa = SaadaConstant.DOUBLE;

    public long sky_pixel_csa = SaadaConstant.LONG;

    public double pos_x_csa = SaadaConstant.DOUBLE;

    public double pos_y_csa = SaadaConstant.DOUBLE;

    public double pos_z_csa = SaadaConstant.DOUBLE;

    public double error_maj_csa = SaadaConstant.DOUBLE;

    public double error_min_csa = SaadaConstant.DOUBLE;

    public double error_angle_csa = SaadaConstant.DOUBLE;

    /**
	 * 
	 */
    public Position() {
        super();
    }

    public Position(double ra, double dec) {
        super();
        this.pos_ra_csa = ra;
        this.pos_dec_csa = dec;
    }

    public double getPos_ra_csa() {
        return this.pos_ra_csa;
    }

    public void setPos_dec_csa(double dec) {
        this.pos_dec_csa = dec;
    }

    public void setPos_ra_csa(double ra) {
        this.pos_ra_csa = ra;
    }

    public double getPos_dec_csa() {
        return this.pos_dec_csa;
    }

    public void setPos_ra_dec_csa(double ra, double dec) {
        this.pos_ra_csa = ra;
        this.pos_dec_csa = dec;
    }

    public long getSky_pixel_csa() {
        return this.sky_pixel_csa;
    }

    /**
	 * HTM replaces with QBox
	 * javadoc: http://aladin.u-strasbg.fr/java/doctech/cds/astro/Qbox.html
	 * Level 10 hardcoded
	 * @param level
	 */
    public void calculSky_pixel_csa() {
        if (Double.isNaN(this.pos_ra_csa) || Double.isNaN(this.pos_dec_csa)) {
            this.sky_pixel_csa = SaadaConstant.LONG;
        } else {
            this.sky_pixel_csa = (new Qbox(new Coo(this.pos_ra_csa, this.pos_dec_csa))).box();
        }
    }

    /**
	 * @param x
	 */
    public void setPos_x(double x) {
        this.pos_x_csa = x;
    }

    /**
	 * @param y
	 */
    public void setPos_y(double y) {
        this.pos_y_csa = y;
    }

    /**
	 * @param z
	 */
    public void setPos_z(double z) {
        this.pos_z_csa = z;
    }

    /**
	 * @return Returns the error_angle_csa.
	 */
    public double getError_angle_csa() {
        return error_angle_csa;
    }

    /**
	 * @return Returns the error_maj_csa.
	 */
    public double getError_maj_csa() {
        return error_maj_csa;
    }

    /**
	 * @return Returns the error_min_csa.
	 */
    public double getError_min_csa() {
        return error_min_csa;
    }

    /**
	 * Conventions used:
	 *  a = major axis  
	 *  b = minor axis
	 *  theta ]-90,90] = angle between X axis and major axis (=> angle betwenn north axis and major axis [0,180[)
	 * If your angle is given between north axis and major axis, use:
	 *  EllipseVarCov(a,b,EllipseVarCov.toFrameXY(thetaNE_deg))
	 * @param a = \sigma_maj
	 * @param b = \sigma_min
	 * @param theta_deg == \theta (degree from NOTHR axis)
	 */
    public void setError(double maj, double min, double theta) {
        double angle = theta;
        if (maj == min) {
            angle = 0.0;
        } else {
            while (angle <= -90.0) angle += 180.0;
            while (angle > 90.0) angle -= 180.0;
        }
        if (maj >= min) {
            this.error_maj_csa = maj;
            this.error_min_csa = min;
            this.error_angle_csa = angle;
        } else {
            this.error_maj_csa = min;
            this.error_min_csa = maj;
            this.error_angle_csa = (angle > 0.0) ? angle - 90.0 : angle + 90.0;
        }
    }

    /**
	 * Transform a canonical variance-covariance matrix (ellipse) into the variance-covariance
	 * matrice obtain after a rotation of the ellipse angle
	 * 
	 * @param  the variance, covariance ellipse
	 * @return [ \sigma_x , \sigma_y ]
	 */
    public final double[] toVarxVaryCovxy() {
        if (this.error_maj_csa == this.error_min_csa || this.error_angle_csa == 0.0) {
            return new double[] { this.error_maj_csa, this.error_min_csa };
        } else if (this.error_angle_csa == 90.0) {
            return new double[] { this.error_min_csa, this.error_maj_csa };
        } else {
            double theta_rad = Math.toRadians(90.0 - this.error_angle_csa);
            double cos2 = Math.cos(theta_rad);
            cos2 *= cos2;
            double sin2 = Math.sin(theta_rad);
            sin2 *= sin2;
            double maj_carre = this.error_maj_csa * this.error_maj_csa;
            double min_carre = this.error_min_csa * this.error_min_csa;
            return new double[] { Math.sqrt(maj_carre * cos2 + min_carre * sin2), Math.sqrt(maj_carre * sin2 + min_carre * cos2) };
        }
    }

    @Override
    public void setProduct_url_csa(String name) {
    }

    @Override
    public String getProduct_url_csa() {
        return null;
    }

    public static void main(String[] args) {
        Qbox.setLevel(10);
        System.out.println((new Qbox(new Coo(40.55, 42.77))).box());
        System.out.println(MysqlWrapper.getIsInCircleConstraint("a", 40.55, 42.77, 1));
    }
}
