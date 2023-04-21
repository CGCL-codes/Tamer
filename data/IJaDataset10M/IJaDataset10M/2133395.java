package pubweb;

import java.io.Serializable;

public class Job implements Serializable, Comparable<Job> {

    private static final long serialVersionUID = 1L;

    private long id;

    private String user;

    private String desc;

    private int nProcs;

    private String consumer;

    public Job() {
    }

    public Job(long id, String user, String desc, int nProcs, String consumer) {
        this.id = id;
        this.user = user;
        this.desc = desc;
        this.nProcs = nProcs;
        this.consumer = consumer;
    }

    public int hashCode() {
        return (int) id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Job)) {
            return false;
        }
        Job job = (Job) obj;
        return id == job.id;
    }

    public String toString() {
        return "id: " + id + "; description: " + desc + "" + "; user: " + user + "; processors: " + nProcs;
    }

    public int compareTo(Job job) {
        if (id < job.id) {
            return -1;
        }
        if (id > job.id) {
            return 1;
        }
        return 0;
    }

    public long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getDescription() {
        return desc;
    }

    public int getNumberOfProcessors() {
        return nProcs;
    }

    public String getConsumerPeer() {
        return consumer;
    }
}
