package com.face.api.client.model;

public class RemovedTag {

    private String removed_tid;

    private String detected_tid;

    public String getRemovedTID() {
        return this.removed_tid;
    }

    public String getDetectedTID() {
        return this.detected_tid;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("removed tid : ").append(removed_tid).append("\n").append("detected_tid: ").append(detected_tid).append("\n").trimToSize();
        return sb.toString();
    }
}
