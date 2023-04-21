package repositorytests.org.deft.repository.mock;

import org.deft.operation.AbstractOperationChain;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactRepresentation;

public class DummyOperationChain extends AbstractOperationChain {

    private String id;

    public DummyOperationChain(String id) {
        this.id = id;
        addOperation(new DummyOperation());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ArtifactRepresentation getInitialArtifactRepresentation(Artifact artifact, int revision) {
        return null;
    }

    @Override
    public boolean isValidInputArtifactType(ArtifactType artifactType) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Dummy Description";
    }
}
