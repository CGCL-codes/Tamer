package thirdParty.jembench.micro;

import thirdParty.jembench.SerialBenchmark;

public class Iinc extends SerialBenchmark {

    public String toString() {
        return "iinc";
    }

    public int perform(int cnt) {
        int a = 0;
        int b = 123;
        int i;
        for (i = 0; i < cnt; ++i) {
            a = a + (b++);
        }
        return a;
    }

    public int overhead(int cnt) {
        int a = 0;
        int b = 123;
        int i;
        for (i = 0; i < cnt; ++i) {
            a = a + b;
        }
        return a;
    }
}
