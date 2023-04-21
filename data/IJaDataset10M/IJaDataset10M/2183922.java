package com.ivis.xprocess.ui.draganddrop.actions;

import java.util.Collection;
import org.eclipse.swt.dnd.DND;
import com.ivis.xprocess.core.Artifact;
import com.ivis.xprocess.core.ArtifactReferenceGroup;
import com.ivis.xprocess.ui.datawrappers.process.ArtifactWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ArtifactReferenceWrapper;
import com.ivis.xprocess.ui.properties.DragDropMenuItems;

/**
 * Currently not in use.
 *
 */
public class LinkArtifactToReference extends MultiAction {

    @Override
    public String getItemText(Collection<?> dragObjects, Object dropObject) {
        return DragDropMenuItems.link_artifacts_to_reference;
    }

    @Override
    public void doAction(Collection<?> dragObjects, Object dropObject) {
    }

    @Override
    public boolean canDrop(Object dragObject, Object dropObject) {
        if (dragObject instanceof ArtifactWrapper && dropObject instanceof ArtifactReferenceWrapper) {
            ArtifactWrapper artifactWrapper = (ArtifactWrapper) dragObject;
            Artifact draggedArtifact = (Artifact) artifactWrapper.getElement();
            ArtifactReferenceWrapper artifactReferenceWrapper = (ArtifactReferenceWrapper) dropObject;
            ArtifactReferenceGroup artifactReference = (ArtifactReferenceGroup) artifactReferenceWrapper.getElement();
            for (Artifact artifact : artifactReference.getArtifacts()) {
                if (artifact == draggedArtifact) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canDrag(Object dragObject) {
        if (dragObject instanceof ArtifactWrapper) {
            return true;
        }
        return false;
    }

    @Override
    public int lightWeightCanDrop(Object dragObject, Object dropObject) {
        if (dragObject instanceof ArtifactWrapper && dropObject instanceof ArtifactReferenceWrapper) {
            ArtifactWrapper artifactWrapper = (ArtifactWrapper) dragObject;
            Artifact draggedArtifact = (Artifact) artifactWrapper.getElement();
            ArtifactReferenceWrapper artifactReferenceWrapper = (ArtifactReferenceWrapper) dropObject;
            ArtifactReferenceGroup artifactReference = (ArtifactReferenceGroup) artifactReferenceWrapper.getElement();
            for (Artifact artifact : artifactReference.getArtifacts()) {
                artifact.getOwner();
                if (artifact == draggedArtifact) {
                    return DND.DROP_NONE;
                }
            }
            return DND.DROP_LINK;
        }
        return DND.DROP_NONE;
    }

    @Override
    public String getItemText(Object dragObject, Object dropObject) {
        return DragDropMenuItems.link_artifact_to_reference;
    }

    @Override
    public void doAction(Object dragObject, Object dropObject) {
    }
}
