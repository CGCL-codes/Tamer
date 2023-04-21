package org.deft.repository.datamodel.adapter.ecore;

import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactReferenceUpdateType;
import org.deft.repository.datamodel.Chapter;
import org.deft.repository.datamodel.ecoredatamodel.ArtifactReference;
import org.deft.repository.repositorymanager.ContentManagementService;

public class DeftArtifactReferenceImpl implements org.deft.repository.datamodel.ArtifactReference {

    private ArtifactReference ecoreArtifactReference;

    private ContentManagementService cms;

    public DeftArtifactReferenceImpl(ArtifactReference ecoreArtifactReference, ContentManagementService cms) {
        this.ecoreArtifactReference = ecoreArtifactReference;
        this.cms = cms;
    }

    @Override
    public String getId() {
        return ecoreArtifactReference.getId();
    }

    @Override
    public org.deft.repository.datamodel.Chapter getChapter() {
        Chapter chapter = cms.getChapter(this);
        return chapter;
    }

    @Override
    public org.deft.repository.datamodel.Artifact getArtifact() {
        Artifact artifact = cms.getArtifact(this);
        return artifact;
    }

    @Override
    public boolean isUnchecked() {
        boolean isUnchecked = cms.isUnchecked(this);
        return isUnchecked;
    }

    public ArtifactReference getEcoreArtifactReference() {
        return ecoreArtifactReference;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DeftArtifactReferenceImpl) {
            DeftArtifactReferenceImpl doi = (DeftArtifactReferenceImpl) o;
            return doi.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String getIntegratorId() {
        return ecoreArtifactReference.getIntegratorId();
    }

    @Override
    public String getOperationChainId() {
        return ecoreArtifactReference.getOperationChainId();
    }

    @Override
    public int getLatestCheckedRevision() {
        return ecoreArtifactReference.getLatestCheckedRevision();
    }

    @Override
    public ArtifactReferenceUpdateType getUpdateType() {
        return cms.getUpdateType(this);
    }

    @Override
    public String toString() {
        return "Reference " + getId() + " from " + getChapter() + " to " + getArtifact();
    }
}
