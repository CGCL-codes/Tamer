package com.ivis.xprocess.ui.datawrappers;

import java.util.Set;
import com.ivis.xprocess.core.ArtifactReferenceGroup;

public interface IContainArtifactReferences {

    public Set<ArtifactReferenceGroup> getArtifactReferences();

    public IElementWrapper getElementWrapper();
}
