package gov.nasa.gsfc.visbard.vis.coord;

/**
 * The Psi class implements the GSE-GSM angle (psi)
 * @author  rchimiak
 * @version $Revision: 1.1 $
 */
public class Psi extends Angle {

    private double psi = 0.0;

    /**Creates a new instance of Psi */
    public Psi(double mjd, double hours) {
        Qe qe = new Qe(mjd, hours);
        psi = Math.atan(qe.y / qe.z);
    }

    /**Returns the GSE-GSM angle
     * @return psi, the GSE-GSM angle 
     */
    public double getPsi() {
        return psi;
    }
}
