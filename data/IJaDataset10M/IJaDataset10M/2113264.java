package thirdParty.jembench.micro;

import thirdParty.jembench.SerialBenchmark;

public class Checkcast extends SerialBenchmark {

    private final Checkcast cself;

    private final Object oself;

    public Checkcast() {
        oself = cself = this;
    }

    public String toString() {
        return "checkcast";
    }

    public int perform(int cnt) {
        Checkcast t = this;
        while (--cnt >= 0) t = (Checkcast) t.oself;
        return t.hashCode();
    }

    public int overhead(int cnt) {
        Checkcast t = this;
        while (--cnt >= 0) t = t.cself;
        return t.hashCode();
    }
}
