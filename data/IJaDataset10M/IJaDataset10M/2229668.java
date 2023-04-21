package pl.clareo.coroutines.examples;

import static pl.clareo.coroutines.user.Coroutines._;
import static pl.clareo.coroutines.user.Coroutines.yield;
import pl.clareo.coroutines.user.CoIterator;
import pl.clareo.coroutines.user.Coroutine;

public class NumberGenerator {

    @Coroutine
    public static CoIterator<Double, Void> fareySequence(int n) {
        int a = 0;
        int b = 1;
        int c = 1;
        int d = n;
        yield(((double) a) / b);
        while (c < n) {
            int k = (n + b) / d;
            int _a = a;
            int _b = b;
            a = c;
            b = d;
            c = k * c - _a;
            d = k * d - _b;
            yield(((double) a) / b);
        }
        return _();
    }

    @Coroutine
    public static CoIterator<Integer, Void> fibonacciNumbers() {
        int a = 0;
        int b = 1;
        while (true) {
            yield(a);
            int next = a + b;
            a = b;
            b = next;
        }
    }

    @Coroutine
    public static CoIterator<Integer, Void> integers() {
        int i = 0;
        while (true) {
            yield(i++);
        }
    }

    public static void main(String args[]) {
        for (Integer i : fibonacciNumbers().till(20)) {
            System.out.println(i);
        }
        for (Integer i : integers().till(10)) {
            System.out.println(i);
        }
        for (Double d : fareySequence(8).each()) {
            System.out.println(d);
        }
    }
}
