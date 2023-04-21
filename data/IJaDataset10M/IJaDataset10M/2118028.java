package edu.psu.its.lionshare.peerserver.wsclient;

import edu.psu.its.lionshare.database.Metadata;

public class MetadataStatus implements Status {

    private Metadata metadata = null;

    private long status = Status.STATUS_UNKNOWN;

    public MetadataStatus() {
    }

    public MetadataStatus(Metadata meta, int status) {
        this.metadata = meta;
        this.status = status;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata meta) {
        this.metadata = meta;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int convertStatus() {
        return -1;
    }
}
