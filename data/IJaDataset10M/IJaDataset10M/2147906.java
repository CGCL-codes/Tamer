package com.netflexitysolutions.amazonws.ec2;

import java.util.Date;

/**
 * An instance of this class represents an EC2 bundle instance task.
 */
public class BundleInstanceInfo {

    private String instanceId;

    private String bundleId;

    private String state;

    private Date startTime;

    private Date updateTime;

    private String progress;

    private Storage storage;

    private Error error;

    public BundleInstanceInfo(String instanceId, String bundleId, String state, Date startTime, Date updateTime, String progress, Storage storage, Error error) {
        super();
        this.instanceId = instanceId;
        this.bundleId = bundleId;
        this.state = state;
        this.startTime = startTime;
        this.updateTime = updateTime;
        this.progress = progress;
        this.storage = storage;
        this.error = error;
    }

    /**
	 * @return the instanceId
	 */
    public String getInstanceId() {
        return instanceId;
    }

    /**
	 * @return the bundleId
	 */
    public String getBundleId() {
        return bundleId;
    }

    /**
	 * @return the state
	 */
    public String getState() {
        return state;
    }

    /**
	 * @return the startTime
	 */
    public Date getStartTime() {
        return startTime;
    }

    /**
	 * @return the updateTime
	 */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
	 * @return the progress
	 */
    public String getProgress() {
        return progress;
    }

    /**
	 * @return the error
	 */
    public Error getError() {
        return error;
    }

    /**
	 * @return the storage
	 */
    public Storage getStorage() {
        return storage;
    }
}
