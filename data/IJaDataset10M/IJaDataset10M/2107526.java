package ru.nnov.kino.web.model.torrent;

import ru.nnov.kino.tracker.model.*;
import ru.nnov.kino.web.model.torrent.TorrentInfo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents torent files
 * 
 * @author chernser
 */
public class TorrentFile implements Serializable {

    private String fileName;

    private Map<String, Peer> peers;

    private TorrentInfo info;

    private String announceUrl;

    private Long creationDate;

    private String comment;

    private String createdBy;

    private String encoding;

    public TorrentFile() {
        fileName = "none";
        peers = new HashMap<String, Peer>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Peer> getPeers() {
        return peers;
    }

    public void setPeers(Map<String, Peer> peers) {
        this.peers = peers;
    }

    public TorrentInfo getInfo() {
        return info;
    }

    public void setInfo(TorrentInfo info) {
        this.info = info;
    }

    public String getAnnounceUrl() {
        return announceUrl;
    }

    public void setAnnounceUrl(String announceUrl) {
        this.announceUrl = announceUrl;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
