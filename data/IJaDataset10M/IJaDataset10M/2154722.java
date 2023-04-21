package org.sqlanyware.sqlwclient.api.data.connections;

public class ConnectionStats {

    private boolean ok;

    private String status;

    public ConnectionStats() {
    }

    public boolean isOk() {
        return this.ok;
    }

    public void setOk(final boolean ok) {
        this.ok = ok;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
