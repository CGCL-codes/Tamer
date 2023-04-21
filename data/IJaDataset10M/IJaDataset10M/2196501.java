package schooltimetablecsp;

/**
 *
 * @author five_stars
 */
public class ConsecutiveHourConstraint extends Constraint {

    @Override
    public boolean valid(Subject ss, Dispo dd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int valid(Subject ss, Day dd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean valid(Dispo d1, Dispo d2) {
        return (d1.key == d2.key + 1 || d1.key == d2.key - 1);
    }
}
