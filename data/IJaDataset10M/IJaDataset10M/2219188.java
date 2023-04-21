package testifie.ui.common.beans;

public class Sleep extends AbstractCommandBean {

    private long _for = 0;

    public void setFor(long time) {
        _for = time;
    }

    public long getFor() {
        return _for;
    }
}
