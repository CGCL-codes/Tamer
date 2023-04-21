package picasatagstopictures.download;

import java.util.Date;
import java.util.logging.Logger;
import picasatagstopictures.util.TimeStamp;

/**
 *
 * @author tom
 */
public class DownloadFile {

    private String url;

    private String localStatus;

    private String downloadStatus;

    private long lastModified;

    /**
     * Examples: clouds, satellites, markers
     */
    private String type;

    private Logger logger;

    public DownloadFile() {
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the localStatus
     */
    public String getLocalStatus() {
        return localStatus;
    }

    /**
     * @param localStatus the localStatus to set
     */
    public void setLocalStatus(String status) {
        this.localStatus = status;
    }

    /**
     * Return a formatted String of last modified.
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        String formattedLastModified = TimeStamp.getFullTimstamp(new Date(this.lastModified));
        return formattedLastModified;
    }

    /**
     * This is not stored in the config file.
     * Do not store in file. Use getLastUpdate() instead.
     * @return the lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Update from last modified date on server only to make the downloader
     * work correctly.
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Examples: clouds, satellites, markers
     * @return the type
     */
    public String getType() {
        if (this.type == null) {
            return null;
        }
        return type.toLowerCase();
    }

    /**
     * Examples: clouds, satellites, markers
     *
     * @param type the type to set. Is automatically set to lower case.
     */
    public void setType(String id) {
        this.type = id.toLowerCase();
    }

    /**
     *
     * @return the downloadStatus
     */
    public String getDownloadStatus() {
        return downloadStatus;
    }

    /**
     * @param downloadStatus the downloadStatus to set
     */
    public void setDownloadStatus(String updateStatus) {
        this.downloadStatus = updateStatus;
    }
}
