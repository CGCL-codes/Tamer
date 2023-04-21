package gov.sns.apps.rfsimulator;

/**
 *
 * @author y32
 */
public class Feedforward {

    double k;

    double kp;

    double ki;

    Signal input;

    Signal errp;

    Signal erri;

    /** Creates a new instance of Feedforward */
    public Feedforward() {
        k = 0.;
        kp = 0.;
        ki = 0.;
    }

    public void setgain(double g, double gp, double gi) {
        k = g;
        kp = gp;
        ki = gi;
    }

    public Signal getout(Signal in, Signal ep, Signal ei) {
        input = new Signal(in);
        errp = new Signal(ep);
        erri = new Signal(ei);
        erri.multiply(ki);
        errp.multiply(kp);
        errp.plus(erri);
        errp.multiply(k);
        input.plus(errp);
        return input;
    }
}
