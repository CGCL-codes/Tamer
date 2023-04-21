package uk.ac.lkl.server.objectify;

import uk.ac.lkl.server.ServerUtils;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * Storage in common across objects and events
 * 
 * @author Ken Kahn
 *
 */
@Unindexed
@Cached
public abstract class ServerObject {

    @Indexed
    protected String projectKey;

    @Indexed
    protected long timeStamp;

    @Indexed
    private Long validUntil = Long.MAX_VALUE;

    public ServerObject(String projectKey, Long timeStamp) {
        this.projectKey = projectKey;
        if (timeStamp == null) {
            this.timeStamp = ServerUtils.currentTimeStamp();
        } else {
            this.timeStamp = timeStamp;
        }
    }

    public ServerObject(String projectKey) {
        this(projectKey, null);
    }

    public ServerObject() {
    }

    public String getProjectKey() {
        return projectKey;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getTimeStampString() {
        return Long.toString(timeStamp);
    }

    public Long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Long validUntil) {
        this.validUntil = validUntil;
    }
}
