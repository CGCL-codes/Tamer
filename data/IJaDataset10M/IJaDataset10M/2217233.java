package globusplugin;

import org.globus.gram.GramJob;

public class GlobusJobInfo {

    private String id;

    private int state;

    private long createTime;

    private long activeTime;

    public GlobusJobInfo(String id, int state, long createTime, long activeTime) {
        this.id = id;
        this.state = state;
        this.createTime = createTime;
        this.activeTime = activeTime;
    }

    public String getID() {
        return id;
    }

    public int getState() {
        return state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }
}
