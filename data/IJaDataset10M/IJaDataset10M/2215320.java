package com.polarrose.jetty.deployer.maven;

/**
 * @author Stefan Arentz
 */
class MavenMetaData {

    private String snapshotTimestamp;

    private String snapshotBuildNumber;

    public MavenMetaData(String snapshotTimestamp, String snapshotBuildNumber) {
        this.snapshotTimestamp = snapshotTimestamp;
        this.snapshotBuildNumber = snapshotBuildNumber;
    }

    public String getSnapshotTimestamp() {
        return snapshotTimestamp;
    }

    public void setSnapshotTimestamp(String snapshotTimestamp) {
        this.snapshotTimestamp = snapshotTimestamp;
    }

    public String getSnapshotBuildNumber() {
        return snapshotBuildNumber;
    }

    public void setSnapshotBuildNumber(String snapshotBuildNumber) {
        this.snapshotBuildNumber = snapshotBuildNumber;
    }

    public String toString() {
        return "MavenMetaData[snapshotTimestamp=" + snapshotTimestamp + " snapshotBuildNumber=" + snapshotBuildNumber + "]";
    }
}
