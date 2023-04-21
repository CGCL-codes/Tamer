package org.jod.anarchytrek;

public abstract class AInfo {

    private String _status;

    public String status() {
        return _status;
    }

    public void status(String aStatus) {
        _status = aStatus;
    }
}
