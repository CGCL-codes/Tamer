package com.dm;

public class Wind extends Instrument {

    static int getPrice() {
        return 2;
    }

    public static void main(String args[]) {
        Instrument inst = new Wind();
        System.out.println(inst.getName());
        System.out.println(inst.getPrice());
        System.out.println(Wind.getPrice());
    }
}
