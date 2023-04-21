package org.escapek.core.dto.cmdb;

import org.escapek.core.dto.NodeDTO;

/**
 * 
 * @author nicolasjouanin
 * @see org.escapek.core.internal.model.cmdb.Relation
 *
 */
public class RelationDTO extends NodeDTO {

    private static final long serialVersionUID = -6958150121216679302L;

    private String instanceName;

    private String displayName;

    private String description;

    private Long relationClassId;

    private Long sourceCIInstanceId;

    private Long targetCIInstanceId;

    public RelationDTO() {
        super();
    }

    public RelationDTO(String name, String description) {
        this();
        setDisplayName(name);
        setDescription(description);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        firePropertyChange("displayName", this.displayName, displayName);
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        firePropertyChange("description", this.description, description);
        this.description = description;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        firePropertyChange("instanceName", this.instanceName, instanceName);
        this.instanceName = instanceName;
    }

    public Long getRelationClassId() {
        return relationClassId;
    }

    public void setRelationClassId(Long relationClassId) {
        firePropertyChange("relationClassId", this.relationClassId, relationClassId);
        this.relationClassId = relationClassId;
    }

    public Long getSourceCIInstanceId() {
        return sourceCIInstanceId;
    }

    public void setSourceCIInstanceId(Long sourceCIInstanceId) {
        firePropertyChange("sourceCIInstanceId", this.sourceCIInstanceId, sourceCIInstanceId);
        this.sourceCIInstanceId = sourceCIInstanceId;
    }

    public Long getTargetCIInstanceId() {
        return targetCIInstanceId;
    }

    public void setTargetCIInstanceId(Long targetCIInstanceId) {
        firePropertyChange("targetCIInstanceId", this.targetCIInstanceId, targetCIInstanceId);
        this.targetCIInstanceId = targetCIInstanceId;
    }
}
