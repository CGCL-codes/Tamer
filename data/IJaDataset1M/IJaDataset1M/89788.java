package org.osll.tictactoe.transport.tcp;

import java.io.Serializable;

public class ApponentNameResponse extends DefaultResponse implements Serializable {

    private String name;

    public ApponentNameResponse() {
        super(Type.APPONENT_NAME);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
