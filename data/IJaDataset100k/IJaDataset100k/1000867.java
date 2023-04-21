package scouter.server.export;

public class WeightingInfo {

    private double numerator = 0, denom = 0;

    private final EntityContainer c;

    private final ExportMap m;

    public WeightingInfo(EntityContainer c, ExportMap m) {
        this.c = c;
        this.m = m;
    }

    public void add(double num, long den) {
        numerator += num;
        denom += den;
    }

    public double avg() {
        if (denom > 0) {
            return numerator / denom;
        } else {
            return 0;
        }
    }

    public EntityContainer c() {
        return c;
    }

    public ExportMap m() {
        return m;
    }

    @Override
    public String toString() {
        return super.toString() + "{numerator=" + numerator + ",denom=" + denom + ",entity=" + c + ",map=" + m + "}";
    }
}
