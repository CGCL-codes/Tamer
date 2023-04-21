package hr.drezga.diplomski.fragmentbundle;

import hr.drezga.diplomski.hostbundle.IGreeter;

public class TestClass implements IGreeter {

    @Override
    public void sayHello() {
        System.out.println("Hello from fragment class");
    }
}
