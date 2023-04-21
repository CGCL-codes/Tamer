package edu.nctu.csie.jichang.dp.structural;

public class AdapterDefault {

    public static void main(String[] args) {
        DefaultInterface tDefault = new AdapterClassA();
        tDefault.methodA();
        tDefault.methodB();
        tDefault.methodC();
    }
}

interface DefaultInterface {

    void methodA();

    void methodB();

    void methodC();
}

abstract class DefaultAdapter implements DefaultInterface {

    public void methodA() {
        System.out.println("do Default A");
    }

    public void methodB() {
        System.out.println("do Default B");
    }
}

class AdapterClassA extends DefaultAdapter {

    public void methodC() {
        System.out.println("do ClassA C");
    }
}

class AdapterClassB extends DefaultAdapter {

    public void methodC() {
        System.out.println("do ClassB C");
    }
}
