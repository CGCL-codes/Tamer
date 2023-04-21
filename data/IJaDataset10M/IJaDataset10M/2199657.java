package org.emergent.antbite.savant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class is an ant data type to stores a list of
 * artifacts that compose a logic group.
 * </p>
 *
 * @author  Brian Pontarelli
 */
public class ArtifactGroup {

    /**
     * The name of the default fileset all artifacts are added to ({@value})
     * unless they belong to a group that specifies another fileset.
     */
    public static final String DEFAULT_FS = "fileset.deps";

    /**
     * The name of the default path all artifacts are added to ({@value}) unless
     * they belong to a group that specifies another path.
     */
    public static final String DEFAULT_PATH = "fileset.path";

    private List artifacts = new ArrayList();

    private List groups = new ArrayList();

    private int expireMinutes;

    private Date expireTime;

    private String classPathId;

    private String fileSetId;

    /**
     * Constructs a new <code>ArtifactGroupType</code>.
     */
    public ArtifactGroup() {
    }

    /**
     * Returns the live list of artifacts in this group.
     */
    public List getArtifacts() {
        return artifacts;
    }

    /**
     * Adds a new artifact to this group.
     *
     * @param   artifact The artifact
     */
    public void addArtifact(Artifact artifact) {
        if (artifact.getExpireminutes() == 0) {
            artifact.setExpireminutes(expireMinutes);
        }
        if (artifact.getExpiretime() == null) {
            artifact.setExpiretime(expireTime);
        }
        artifacts.add(artifact);
    }

    /**
     * Returns all the ArtifactGroups inside this artifact group.
     *
     * @return  The list of groups and never null
     */
    public List getArtifactGroups() {
        return groups;
    }

    /**
     * Adds a new nested artifact group or a reference.
     *
     * @param   group The group to add
     */
    public void addArtifactGroup(ArtifactGroup group) {
        if (group.getClasspathid() == null) {
            group.setClasspathid(classPathId);
        }
        if (group.getFilesetid() == null) {
            group.setFilesetid(fileSetId);
        }
        groups.add(group);
    }

    public String getClasspathid() {
        return classPathId;
    }

    public void setClasspathid(String classPathId) {
        this.classPathId = classPathId;
    }

    public String getFilesetid() {
        return fileSetId;
    }

    public void setFilesetid(String fileSetId) {
        this.fileSetId = fileSetId;
    }

    public int getExpireminutes() {
        return expireMinutes;
    }

    public void setExpireminutes(int expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public Date getExpiretime() {
        return expireTime;
    }

    public void setExpiretime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * Validates the group.
     *
     * @throws  SavantException If the group contains no artifacts
     */
    public void validate() throws SavantException {
        if (artifacts.size() == 0) {
            throw new SavantException("All artifact groups must have artifacts");
        }
    }
}
