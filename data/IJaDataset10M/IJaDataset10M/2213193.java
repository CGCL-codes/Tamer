package net.sourceforge.exclusive.client;

public class ActiveDownloadInfo {

    public enum Status {

        Running, Paused, Aborted, Finished
    }

    ;

    public Status status;

    public String hash;

    public String[] piecesHash;

    public String filename;

    public long fileSize;

    public Client client;

    public ActiveDownloadInfo(Status status, String hash, String[] piecesHash, String filename, long length) {
        this.status = status;
        this.hash = hash;
        this.piecesHash = piecesHash;
        this.filename = filename;
        this.fileSize = length;
    }
}
