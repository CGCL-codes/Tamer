package android.net;

/**
 * A class for representing UNIX credentials passed via ancillary data
 * on UNIX domain sockets. See "man 7 unix" on a desktop linux distro.
 */
public class Credentials {

    /** pid of process. root peers may lie. */
    private final int pid;

    /** uid of process. root peers may lie. */
    private final int uid;

    /** gid of process. root peers may lie. */
    private final int gid;

    public Credentials(int pid, int uid, int gid) {
        this.pid = pid;
        this.uid = uid;
        this.gid = gid;
    }

    public int getPid() {
        return pid;
    }

    public int getUid() {
        return uid;
    }

    public int getGid() {
        return gid;
    }
}
