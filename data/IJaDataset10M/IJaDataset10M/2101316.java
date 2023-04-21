package surnyoworks;

/**
 *
 * @author kruskal
 */
public class Szimulator extends Os {

    private double delta;

    private Halozat halo;

    public Szimulator(Halozat halozat) {
        halo = halozat;
    }

    public void setdelta(double a, String s) {
        delta = a;
    }

    public void run(String s) {
        halo.frissit("");
    }
}
