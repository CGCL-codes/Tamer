package unbbayes.learning;

/**
 * @author custodio
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FrequencyNode {

    private double frequency;

    private double[][] nik;

    private double[] ni;

    private double[] nk;

    private int ri, rk;

    public FrequencyNode(int ri, int rk) {
        this.ri = ri;
        this.rk = rk;
        nik = new double[ri][rk];
        ni = new double[ri];
        nk = new double[rk];
    }

    public int getRi() {
        return ri;
    }

    public int getRk() {
        return rk;
    }

    public void addFrequency(int il, int kl, double n) {
        frequency += n;
        nik[il][kl] += n;
        ni[il] += n;
        nk[kl] += n;
    }

    public void setNikFrequency(int il, int kl, double n) {
        nik[il][kl] = n;
    }

    public void setNiFrequency(int il, double n) {
        ni[il] = n;
    }

    public void setNkFrequency(int kl, double n) {
        nk[kl] = n;
    }

    public void setFFrequency(double n) {
        frequency = n;
    }

    public void addNikFrequency(int il, int kl, double n) {
        nik[il][kl] += n;
    }

    public void addNiFrequency(int il, double n) {
        ni[il] += n;
    }

    public void addNkFrequency(int kl, double n) {
        nk[kl] += n;
    }

    public void addFFrequency(double n) {
        frequency += n;
    }

    public double getJ() {
        return frequency;
    }

    public double getNi(int il) {
        return ni[il];
    }

    public double getNk(int kl) {
        return nk[kl];
    }

    public double getNik(int il, int kl) {
        return nik[il][kl];
    }
}
