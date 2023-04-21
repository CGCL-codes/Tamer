package com.iamnota.spellingpractice.Student.state;

public class Invalid extends State {

    private static Invalid me;

    protected Invalid() {
        ;
    }

    public static synchronized State getInstance() {
        if (Invalid.me == null) {
            Invalid.me = new Invalid();
        }
        return Invalid.me;
    }

    public String getName() {
        return "Invalid";
    }
}
