package org.escapek.core.dto.cmdb;

import org.escapek.core.dto.NodeDTO;

public abstract class CILinkDTO extends NodeDTO {

    private static final long serialVersionUID = 2747672708781264799L;

    private String linkName;

    private int occurenceNumber;

    private Long sourceCIId;

    private Long linkDefId;

    private Long targetCIId;

    public Long getSourceCIId() {
        return sourceCIId;
    }

    public void setSourceCIId(Long instanceId) {
        firePropertyChanged("sourceCIId", this.sourceCIId, sourceCIId);
        sourceCIId = instanceId;
    }

    public Long getLinkDefId() {
        return linkDefId;
    }

    public void setLinkDefId(Long linkDefId) {
        firePropertyChanged("linkDefId", this.linkDefId, linkDefId);
        this.linkDefId = linkDefId;
    }

    public int getOccurenceNumber() {
        return occurenceNumber;
    }

    public void setOccurenceNumber(int occurenceNumber) {
        firePropertyChanged("occurenceNumber", this.occurenceNumber, occurenceNumber);
        this.occurenceNumber = occurenceNumber;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        firePropertyChanged("linkName", this.linkName, linkName);
        this.linkName = linkName;
    }

    public Long getTargetCIId() {
        return targetCIId;
    }

    public void setTargetCIId(Long targetCIId) {
        firePropertyChanged("targetCIId", this.targetCIId, targetCIId);
        this.targetCIId = targetCIId;
    }
}
