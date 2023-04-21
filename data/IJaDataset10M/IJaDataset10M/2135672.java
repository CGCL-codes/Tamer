package org.deft.operationchain;

import org.deft.operation.AbstractOperationChain;
import org.deft.operation.ByteArray2OntologyOperation;
import org.deft.operation.Ontology2IndividualDataOperation;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.artifacttype.requirementsontology.RequirementsOntologyArtifactType;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.representation.bytearray.ByteArrayRepresentation;

public class RequirementsOntology2IndividualDataOperationChain extends AbstractOperationChain {

    public static String ID = "org.deft.operationchain.ro2individualdata";

    public RequirementsOntology2IndividualDataOperationChain() {
        setupChain();
    }

    private void setupChain() {
        addOperation(new ByteArray2OntologyOperation());
        addOperation(new Ontology2IndividualDataOperation());
    }

    @Override
    public ArtifactRepresentation getInitialArtifactRepresentation(Artifact artifact, int revision) {
        byte[] content = artifact.getArtifactContent(revision);
        ByteArrayRepresentation rep = new ByteArrayRepresentation(content);
        return rep;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public boolean isValidInputArtifactType(ArtifactType artifactType) {
        boolean isRO = artifactType instanceof RequirementsOntologyArtifactType;
        return isRO;
    }

    @Override
    public String getDescription() {
        return "Individual Data";
    }
}
