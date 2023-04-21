package org.jnerve;

/** ResumeParameters contains parameters
  * for searching for file matches based on checksum and filesize.
  */
public class ResumeParameters {

    private String checksum;

    private int filesize;

    private int maxResults = 100;

    public void setChecksum(String s) {
        checksum = s;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setFilesize(int s) {
        filesize = s;
    }

    public int getFilesize() {
        return filesize;
    }

    public int getMaxResults() {
        return maxResults;
    }
}
