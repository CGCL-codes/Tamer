package org.deft.configuration;

import org.deft.operation.OperationConfiguration;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactRepresentation;

public interface OperationGui extends ChainableGui {

    public void initializeGui(OperationConfiguration config, Artifact artifact, ArtifactRepresentation artifactRepr);
}
