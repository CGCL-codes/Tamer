package gov.nasa.gsfc.visbard.ovt.mag;

/** Time dependent magnetic activity data */
public class MagActivity implements Cloneable {

    /** modified Julian day */
    protected double mjd = 12000;

    /** KP Index */
    public double KPindex;

    /** Interplanetary magnetic field. It has 3 components: Bx, By, Bz */
    public double[] imf = { 0, 0, 0 };

    /** Solar wind pressue <code>0.5</code> to <code>10</code> nT */
    public double PSW = 4;

    /** DSTIndex.  Range from <code>-100</code> to <code>20</code>  nT */
    public double DSTindex = -40;

    /** Create the object */
    MagActivity() {
    }

    /** Create MagActivity up to mjd. */
    public MagActivity(double anmjd) {
        mjd = anmjd;
        KPindex = 0;
        imf = new double[3];
        for (int i = 0; i < 3; i++) imf[i] = 0;
        PSW = 4;
        DSTindex = -40.0;
    }

    public Object clone() {
        MagActivity ma = new MagActivity();
        ma.mjd = mjd;
        ma.KPindex = KPindex;
        ma.imf = (double[]) imf.clone();
        ma.PSW = PSW;
        ma.DSTindex = DSTindex;
        return ma;
    }
}
