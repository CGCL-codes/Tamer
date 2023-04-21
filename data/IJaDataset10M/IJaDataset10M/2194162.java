package dk.andsen.hp41.types;

/**
 * The DseIsg object hold informations for a the DSE and ISG tests
 * @author andsen
 *
 */
public class DseIsg {

    private int count;

    private int limit;

    private int step;

    public void inc() {
        count += step;
    }

    public void dec() {
        count -= step;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
