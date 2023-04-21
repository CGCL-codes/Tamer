package edu.sfsu.powerrangers.jeopardy.gamestate;

import java.io.Serializable;

public class Host implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Host(String name) {
        this.name = name;
    }

    public Host clone() {
        return new Host(name);
    }

    public String getName() {
        return name;
    }
}
