package org.jnerve.session;

/** ShareRecord objects represent a file shared by a user. 
  */
public class ShareRecord {

    private String filename;

    private String MD5Signature;

    private int size;

    private int bitrate;

    private int frequency;

    private int seconds;

    private String filenameUppercase;

    public ShareRecord(String filename, String MD5Signature, int size, int bitrate, int frequency, int seconds) {
        this.filename = filename;
        this.MD5Signature = MD5Signature;
        this.size = size;
        this.bitrate = bitrate;
        this.frequency = frequency;
        this.seconds = seconds;
        this.filenameUppercase = filename.toUpperCase();
    }

    public String getFilename() {
        return filename;
    }

    public String getFilenameUppercase() {
        return filenameUppercase;
    }

    public String getMD5Signature() {
        return MD5Signature;
    }

    public int getSize() {
        return size;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getSeconds() {
        return seconds;
    }
}
