package com.william.javacodelibrary;

public class TupleTest {

    public static TwoTuple<String, Integer> f() {
        return Tuple.tuple("a", 1);
    }

    public static TwoTuple f2() {
        return Tuple.tuple("b", 2);
    }

    public static ThreeTuple<String, String, String> g() {
        return Tuple.tuple("c", "c", "c");
    }

    public static FourTuple<String, String, String, String> h() {
        return Tuple.tuple("d", "d", "d", "d");
    }

    public static FiveTuple<String, String, String, String, String> i() {
        return Tuple.tuple("e", "e", "e", "e", "e");
    }

    public static void main(String[] args) {
        Object ttsi = f();
        System.out.println(ttsi);
        System.out.println(f2());
        System.out.println(g());
        System.out.println(h());
        System.out.println(i());
    }
}
