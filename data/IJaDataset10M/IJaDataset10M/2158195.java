package net.sf.opengroove.client.g3com;

public class Resolution {

    private String realm;

    private long time;

    private ServerContext[] result;

    public String getRealm() {
        return realm;
    }

    public long getTime() {
        return time;
    }

    public ServerContext[] getResult() {
        return result;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setResult(ServerContext[] result) {
        this.result = result;
    }
}
