package misc;

public class Demand {

    final Good good;

    int priority;

    double filled;

    public Demand(Good good, int priority) {
        this.good = good;
        this.priority = priority;
        filled = 0.0;
    }
}
