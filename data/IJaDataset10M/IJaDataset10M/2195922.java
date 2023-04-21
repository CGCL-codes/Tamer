package org.easysync.model.config;

import java.util.Date;

/**
 * The Class SyncConfig.
 */
public class SyncConfig {

    /** The source path. */
    private String sourcePath;

    /** The source resource type. */
    private int sourceResourceType;

    /** The target path. */
    private String targetPath;

    /** The target resource type. */
    private int targetResourceType;

    /** The sync direction. */
    private int syncDirection;

    /** The last sync. */
    private Date lastSync;

    /** The configversion. */
    private double configversion = 1.0;

    /**
	 * Gets the source path.
	 *
	 * @return the sourcePath
	 */
    public String getSourcePath() {
        return sourcePath;
    }

    /**
	 * Sets the source path.
	 *
	 * @param sourcePath the sourcePath to set
	 */
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
	 * Gets the source resource type.
	 *
	 * @return the sourceResourceType
	 */
    public int getSourceResourceType() {
        return sourceResourceType;
    }

    /**
	 * Sets the source resource type.
	 *
	 * @param sourceResourceType the sourceResourceType to set
	 */
    public void setSourceResourceType(int sourceResourceType) {
        this.sourceResourceType = sourceResourceType;
    }

    /**
	 * Gets the target path.
	 *
	 * @return the targetPath
	 */
    public String getTargetPath() {
        return targetPath;
    }

    /**
	 * Sets the target path.
	 *
	 * @param targetPath the targetPath to set
	 */
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    /**
	 * Gets the target resource type.
	 *
	 * @return the targetResourceType
	 */
    public int getTargetResourceType() {
        return targetResourceType;
    }

    /**
	 * Sets the target resource type.
	 *
	 * @param targetResourceType the targetResourceType to set
	 */
    public void setTargetResourceType(int targetResourceType) {
        this.targetResourceType = targetResourceType;
    }

    /**
	 * Gets the sync direction.
	 *
	 * @return the syncDirection
	 */
    public int getSyncDirection() {
        return syncDirection;
    }

    /**
	 * Sets the sync direction.
	 *
	 * @param syncDirection the syncDirection to set
	 */
    public void setSyncDirection(int syncDirection) {
        this.syncDirection = syncDirection;
    }

    /**
	 * Gets the last sync.
	 *
	 * @return the lastSync
	 */
    public Date getLastSync() {
        return lastSync;
    }

    /**
	 * Sets the last sync.
	 *
	 * @param lastSync the lastSync to set
	 */
    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    /**
	 * @return the configversion
	 */
    public double getConfigversion() {
        return configversion;
    }

    /**
	 * @param configversion the configversion to set
	 */
    public void setConfigversion(double configversion) {
        this.configversion = configversion;
    }
}
