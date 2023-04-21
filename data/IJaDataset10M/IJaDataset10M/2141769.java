package net.sourceforge.jepesi.controller;

public class JepesiFactory {

    public JepesiStarter getStarter() {
        return new JepesiControl();
    }
}
