package org.deft.operation.bytearray2ecoreinstance;

import org.deft.operation.Operation;
import org.deft.operation.OperationConfiguration;
import org.deft.operation.exception.DeftInvalidOperationChainException;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.representation.bytearray.ByteArrayRepresentation;
import org.deft.representation.ecore.EcoreInstanceRepresentation;
import org.eclipse.emf.ecore.EObject;

/**
 * An operation that takes some input as byte array 
 * and returns an EObject, representing an Ecore instance.
 */
public abstract class ByteArray2EcoreInstanceOperation implements Operation {

    protected ArtifactType artifactType;

    protected abstract EObject getEcoreInstance(byte[] byteContent);

    @Override
    public ArtifactRepresentation executeOperation(ArtifactRepresentation representation, ArtifactType type, OperationConfiguration config) {
        this.artifactType = type;
        byte[] byteContent = checkAndUnwrap(representation);
        EObject instanceRoot = getEcoreInstance(byteContent);
        EcoreInstanceRepresentation eiar = new EcoreInstanceRepresentation(instanceRoot);
        return eiar;
    }

    private byte[] checkAndUnwrap(ArtifactRepresentation rep) {
        if (rep.getType().equals(ByteArrayRepresentation.TYPE)) {
            ByteArrayRepresentation bar = (ByteArrayRepresentation) rep;
            return bar.getByteArray();
        } else {
            throw new DeftInvalidOperationChainException();
        }
    }

    @Override
    public String getInputRepresentationType() {
        return ByteArrayRepresentation.TYPE;
    }

    @Override
    public String getResultingRepresentationType() {
        return EcoreInstanceRepresentation.TYPE;
    }
}
