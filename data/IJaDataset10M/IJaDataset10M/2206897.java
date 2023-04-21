package com.fusteeno.gnutella.net;

import com.fusteeno.gnutella.util.Debug;

public class SearchResult {

    private Servent servent;

    private String filename;

    private long size;

    private int index;

    private String ip;

    private int port;

    private GUID guid;

    private String speed;

    private String extraData;

    private boolean uploadSpeedFlag;

    private boolean haveUploadedFlag;

    private boolean busyFlag;

    private boolean pushFlag;

    public SearchResult(long size, String filename, String ip, int port, int index, Servent servent, GUID guid, String extraData, String speed, boolean uploadSpeedFlag, boolean haveUploadedFlag, boolean busyFlag, boolean pushFlag) {
        this.servent = servent;
        this.index = index;
        this.size = size;
        this.filename = filename;
        this.ip = ip;
        this.port = port;
        this.guid = guid;
        this.extraData = extraData;
        this.speed = speed;
        this.uploadSpeedFlag = uploadSpeedFlag;
        this.haveUploadedFlag = haveUploadedFlag;
        this.busyFlag = busyFlag;
        this.pushFlag = pushFlag;
        Debug.log("File Name: " + filename);
        Debug.log("Speed: " + speed);
        Debug.log("Extra data: " + extraData);
        Debug.log("Size: " + size);
        Debug.log("Index:" + index);
    }

    public long getSize() {
        return size;
    }

    public String getFilename() {
        return filename;
    }

    public Servent getServent() {
        return servent;
    }

    public int getPort() {
        return port;
    }

    public int getIndex() {
        return index;
    }

    public GUID getServentID() {
        return guid;
    }

    public String getIP() {
        return ip;
    }

    public String getSpeed() {
        return speed;
    }

    public boolean isPushFlag() {
        return pushFlag;
    }

    public Download download(String id) {
        Download downloader = new Download(this, id);
        downloader.start();
        return downloader;
    }

    public String getExtraData() {
        return extraData;
    }
}
