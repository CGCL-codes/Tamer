package cartago;

import java.io.*;

/**
 * Identifier of an artifact
 * 
 * @author aricci
 */
public class ArtifactId implements java.io.Serializable {

    private String name;

    private int id;

    private String artifactType;

    private WorkspaceId wspId;

    private AgentId creatorId;

    ArtifactId(String name, int id, String artifactType, WorkspaceId wspId, AgentId creatorId) {
        this.name = name;
        this.id = id;
        this.wspId = wspId;
        this.creatorId = creatorId;
        this.artifactType = artifactType;
    }

    public String toString() {
        return name;
    }

    /**
	 * Get the artifact logic name 
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get artifact unique id inside the workspace
	 * 
	 * @return
	 */
    public int getId() {
        return id;
    }

    /**
	 * Get artifact type (i.e. the name of the template)
	 * 
	 * @return
	 */
    public String getArtifactType() {
        return artifactType;
    }

    /**
	 * Get creator identifier.
	 * 
	 * @return
	 */
    public AgentId getCreatorId() {
        return creatorId;
    }

    /**
	 * Check if two ids are equal
	 */
    public boolean equals(Object aid) {
        return (aid != null) && (aid instanceof ArtifactId) && ((ArtifactId) aid).getId() == id;
    }

    /**
	 * Get the identifier of the workspace where the artifact is located.
	 * 
	 * @return
	 */
    public WorkspaceId getWorkspaceId() {
        return wspId;
    }

    public int hashCode() {
        return id;
    }
}
