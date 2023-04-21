package org.vfsutils.shell.remote;

public class Identity {

    String token;

    public Identity() {
        this.token = "brk" + Math.random();
    }

    public String toString() {
        return token;
    }
}
