package ces.sf.oa.util;

public class Msg {

    String toPhone;

    String msg;

    int failureCount = 0;

    public Msg(String toPhone, String msg) {
        this.toPhone = toPhone;
        this.msg = msg;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void failure() {
        this.failureCount++;
    }
}
