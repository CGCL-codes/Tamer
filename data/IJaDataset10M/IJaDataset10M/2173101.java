package islandev.testing;

/**
 *
 * @author  mmg20
 */
public class Biro implements PrintingThing, java.io.Serializable {

    protected double ballPointWidth = 0.3;

    protected Ink ink;

    /** Creates a new instance of Biro */
    public Biro(Double width) {
        ink = new Ink("Green");
        ballPointWidth = width.doubleValue();
    }

    public double getBallPointWidth() {
        return ballPointWidth;
    }

    public String toString() {
        return "Ball = " + ballPointWidth + ", Ink = " + ink;
    }

    public void print() {
        System.out.println("" + this);
    }
}
