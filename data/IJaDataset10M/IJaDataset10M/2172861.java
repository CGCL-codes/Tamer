package com.onehao.basic;

public class BreakTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (5 == i) {
                break;
            }
            System.out.println(i);
        }
    }
}
