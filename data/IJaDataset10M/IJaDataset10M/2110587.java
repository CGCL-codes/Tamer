package net.sf.dslrunner.example;

import net.sf.dslrunner.Box;

public class FecharBox implements Box {

    public FecharBox(Dummy dummy) {
    }

    public void execute() {
        System.out.println("fechando...");
    }
}
