package org.xmlvm.test.poly;

public class Poly6 {

    public static void main(String[] args) {
        Poly6Interface i = new Poly6Child();
        System.out.println(i.getStr());
    }
}

class Poly6Child implements Poly6Interface {

    public String getStr() {
        return "child";
    }
}

interface Poly6Interface {

    public String getStr();
}
