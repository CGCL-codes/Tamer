package org.kablink.teaming.rest.v1.model;

import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "file")
public class FileProperties extends FileCommonProperties {

    private String name;

    private Long lockedBy;

    private Calendar lockExpiration;

    private Boolean incrementMajorVersion;

    private FileProperties() {
        super();
    }

    public FileProperties(String id, String name, HistoryStamp creation, HistoryStamp modification, Long length, Integer versionNumber, Integer majorVersion, Integer minorVersion, String note, Integer status, String webUrl, Long lockedBy, Calendar lockExpiration) {
        super(id, creation, modification, length, versionNumber, majorVersion, minorVersion, note, status, webUrl);
        this.name = name;
        this.lockedBy = lockedBy;
        this.lockExpiration = lockExpiration;
    }

    public FileProperties(String id, String name, HistoryStamp creation, HistoryStamp modification, Long length, Integer versionNumber, Integer majorVersion, Integer minorVersion, String note, Integer status, String webUrl, Long lockedBy, Date lockExpiration) {
        super(id, creation, modification, length, versionNumber, majorVersion, minorVersion, note, status, webUrl);
        this.name = name;
        this.lockedBy = lockedBy;
        if (lockExpiration != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lockExpiration);
            this.lockExpiration = cal;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Long lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Calendar getLockExpiration() {
        return lockExpiration;
    }

    public void setLockExpiration(Calendar lockExpiration) {
        this.lockExpiration = lockExpiration;
    }

    public Boolean getIncrementMajorVersion() {
        return incrementMajorVersion;
    }

    public void setIncrementMajorVersion(Boolean incrementMajorVersion) {
        this.incrementMajorVersion = incrementMajorVersion;
    }
}
