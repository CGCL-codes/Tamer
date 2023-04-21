package org.susan.java.basic;

public class AutoIncreasement {

    public static void main(String args[]) {
        int i = 3;
        int j = 3;
        System.out.println("++i:" + (++i));
        System.out.println("j++:" + (j++));
        System.out.println("i:" + i);
        System.out.println("j:" + j);
        System.out.println(i == (i++));
        System.out.println(i == (++i));
        float floatValue = 0.3F;
        System.out.println(floatValue++);
        System.out.println(floatValue);
        System.out.println("i:" + i);
        System.out.println("j:" + j);
        System.out.println(j++ + i++ + ++i + ++i + i++);
    }
}
