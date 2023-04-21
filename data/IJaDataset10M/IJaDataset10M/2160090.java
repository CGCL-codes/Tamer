package org.deft.operation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactRepresentation;

public abstract class AbstractOperationChain implements OperationChain {

    private List<Operation> operations = new LinkedList<Operation>();

    private OperationChainConfiguration chainConfig = new OperationChainConfiguration();

    public void addOperation(Operation operation) {
        checkCompatibility(operation);
        operations.add(operation);
        chainConfig.addInitialConfiguration(operation);
    }

    private void checkCompatibility(Operation newOperation) {
    }

    @Override
    public int getOperationCount() {
        return operations.size();
    }

    @Override
    public ArtifactRepresentation executeOperations(Artifact artifact) {
        int operationCount = operations.size();
        ArtifactRepresentation rep = executeOperations(artifact, operationCount);
        return rep;
    }

    @Override
    public OperationChainConfiguration getChainConfigurations() {
        return chainConfig;
    }

    @Override
    public void resetChainConfigurations() {
        chainConfig.reset();
    }

    @Override
    public ArtifactRepresentation executeOperations(Artifact artifact, int steps) {
        ArtifactRepresentation rep = execute(artifact, artifact.getLatestRevision(), steps);
        return rep;
    }

    @Override
    public ArtifactRepresentation executeOperationsOnRevision(Artifact artifact, int revision) {
        ArtifactRepresentation rep = execute(artifact, revision, getOperationCount());
        return rep;
    }

    private ArtifactRepresentation execute(Artifact artifact, int revision, int steps) {
        initBeforeExecution(artifact);
        ArtifactRepresentation currentRep = getInitialArtifactRepresentation(artifact, revision);
        ArtifactType type = artifact.getArtifactType();
        try {
            for (int i = 1; i <= steps; i++) {
                Operation operation = operations.get(i - 1);
                OperationConfiguration config = getChainConfigurations().getConfiguration(operation);
                ArtifactRepresentation newRep = operation.executeOperation(currentRep, type, config);
                currentRep = newRep;
            }
            return currentRep;
        } catch (Exception e) {
            System.err.println("Could not compute representation");
            e.printStackTrace();
            return null;
        } finally {
            cleanUpAfterExecution();
        }
    }

    @Override
    public void initBeforeExecution(Artifact artifact) {
    }

    @Override
    public void cleanUpAfterExecution() {
    }

    @Override
    public Operation getOperation(int index) {
        Operation operation = operations.get(index);
        return operation;
    }

    @Override
    public String getFinalRepresentationType() {
        Operation lastOperation = getLastOperation();
        if (lastOperation != null) {
            String type = lastOperation.getResultingRepresentationType();
            return type;
        }
        return null;
    }

    private Operation getLastOperation() {
        if (!operations.isEmpty()) {
            return operations.get(operations.size() - 1);
        }
        return null;
    }

    @Override
    public Iterator<Operation> iterator() {
        return operations.iterator();
    }
}
