package kursach2;

/**
 * @author Vsevolod
 * 
 */
public class Struct {

    private int i;

    private double d;

    public Struct(int i, double d) {
        this.i = i;
        this.d = d;
    }

    public void set(int i, double d) {
        this.i = i;
        this.d = d;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getD() {
        return d;
    }
}
