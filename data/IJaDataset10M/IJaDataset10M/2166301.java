package org.parallelj.mda.rt.controlflow.engine.test;

import org.parallelj.mda.rt.controlflow.engine.Engine;

public class ABCD extends Engine {

    int count;

    boolean isPredicate() {
        return true;
    }

    public Print newA() {
        return new Print();
    }

    public void enterA(Print print) {
        print.s = "a";
        this.count++;
    }

    public void exitA(Print print) {
    }

    public void enterB(Print print) {
        print.s = "b";
    }

    public void enterC(Print print) {
        print.s = "c";
    }

    public void d() {
        System.out.println("d");
    }

    public ABCD() {
        super(new ABCDFlow());
    }

    public static void main(String[] args) {
        new ABCD().run();
    }
}
