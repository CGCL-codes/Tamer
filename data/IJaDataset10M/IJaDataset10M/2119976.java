package net.sf.buildbox.reactor.model;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * <p>Uniquely identifies a project+functionality; its concrete execution is {@link WorkerJob}.</p>
 *
 * @author Petr Kozelka
 * @since 1.3.2006
 */
public class JobId implements Serializable {

    private static final long serialVersionUID = 6543060576924212457L;

    private String projectId;

    private String targetName;

    /**
     * Identity must be known at construction time and is cached, because of its heavy usage.
     *
     * @param projectId    identification of the project to which this job is applied (typically just a location string)
     * @param targetName symbolic name of this activity - use null, will be automatically
     */
    public JobId(String projectId, String targetName) {
        this.projectId = projectId;
        this.targetName = targetName;
    }

    public JobId() {
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * Returns cached identity string.
     *
     * @return stringified identity
     * @deprecated
     */
    public final String getIdentifier() {
        return toString();
    }

    /**
     * Because {@link #getIdentifier()} gathers all identity fields, you should never need to override this
     *
     * @param theOtherObject the other object, see {@link Object#equals(Object)}
     * @return identity-equivalence (not all fields are part of identity!)
     */
    @Override
    public final boolean equals(Object theOtherObject) {
        if (this == theOtherObject) return true;
        if (theOtherObject == null || !(theOtherObject instanceof JobId)) return false;
        final JobId jobId = (JobId) theOtherObject;
        return toString().equals(jobId.toString());
    }

    /**
     * For reasons stated in {@link #equals(Object)}, you never need to override this.
     *
     * @return identity-hash
     */
    @Override
    public final int hashCode() {
        int result = projectId.hashCode();
        result = 31 * result + targetName.hashCode();
        return result;
    }

    /**
     * String for logging purposes only.
     *
     * @return logging string, also used for simple serialization in remote apis
     * @see #valueOf(String)
     */
    @Override
    public final String toString() {
        return projectId + "!" + targetName;
    }

    /**
     * @param text string with encoded job id
     * @return parsed {@link JobId}
     * @see #toString()
     */
    public static JobId valueOf(String text) {
        final int splitter1 = text.indexOf('!');
        final String projectId = text.substring(0, splitter1);
        final String targetName = text.substring(splitter1 + 1);
        return new JobId(projectId, targetName);
    }

    @XmlAttribute
    public String getProjectId() {
        return projectId;
    }

    /**
     * @return -
     */
    @XmlAttribute(name = "command")
    public String getTargetName() {
        return targetName;
    }
}
