package hu.sztaki.lpds.submitter.grids.glite.status;

public class Job {

    private String userid;

    private long setstatustime = 0;

    private int status = 0;

    public Job(String puid, int pstat) {
        userid = puid;
        status = pstat;
        setstatustime = System.currentTimeMillis();
    }

    public String getUserid() {
        return userid;
    }

    public int getStatus() {
        return status;
    }

    public long getSetStatusTime() {
        return setstatustime;
    }

    public boolean setStatus(String puid, int pstat) {
        if (userid.equals(puid)) {
            status = pstat;
            setstatustime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
