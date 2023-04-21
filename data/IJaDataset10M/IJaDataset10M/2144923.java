package thirdParty.jembench.micro;

import thirdParty.jembench.SerialBenchmark;

public class Ldc extends SerialBenchmark {

    public String toString() {
        return "ldc iadd";
    }

    public int perform(int cnt) {
        int a = 0;
        int b = 123;
        int i;
        for (i = 0; i < cnt; ++i) {
            a = a + b + 12345678;
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
