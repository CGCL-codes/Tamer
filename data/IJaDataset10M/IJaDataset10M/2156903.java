package org.deft.operation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.deft.operation.exception.DeftInvalidOperationChainException;
import org.deft.operation.exception.DeftInvalidOperationConfigurationException;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.representation.OwlRepresentation;
import org.deft.representation.table.TableRepresentation;
import org.deft.representation.table.data.Table;
import org.deft.representation.table.data.TableCell;
import org.deft.requirementsontology.GeneralTraceabilityMatrix;
import org.deft.requirementsontology.OntologyAccessor;
import org.deft.requirementsontology.ROModel;
import org.semanticweb.owl.model.OWLOntology;

public class Ontology2MatrixOperation implements Operation {

    @Override
    public TableRepresentation executeOperation(ArtifactRepresentation representation, ArtifactType type, OperationConfiguration config) {
        OWLOntology ontology = checkAndUnwrap(representation);
        Ontology2MatrixConfiguration o2mConfig = checkAndUnwrap(config);
        o2mConfig.initIfIsUnmodified(ontology);
        Table table = transformToTable(ontology, o2mConfig);
        TableRepresentation tr = new TableRepresentation(table);
        return tr;
    }

    private OWLOntology checkAndUnwrap(ArtifactRepresentation rep) {
        if (rep.getType().equals(OwlRepresentation.TYPE)) {
            OwlRepresentation or = (OwlRepresentation) rep;
            return or.getOntology();
        } else {
            throw new DeftInvalidOperationChainException();
        }
    }

    private Ontology2MatrixConfiguration checkAndUnwrap(OperationConfiguration config) {
        if (config instanceof Ontology2MatrixConfiguration) {
            Ontology2MatrixConfiguration o2mConfig = (Ontology2MatrixConfiguration) config;
            return o2mConfig;
        } else {
            throw new DeftInvalidOperationConfigurationException();
        }
    }

    private Table transformToTable(OWLOntology ontology, Ontology2MatrixConfiguration config) {
        OntologyAccessor oa = new OntologyAccessor(ontology);
        ROModel roModel = new ROModel(oa);
        List<String> sourceIndividuals = config.getSourceIndividuals();
        List<String> targetIndividuals = config.getTargetIndividuals();
        sourceIndividuals = removeIndividualsNotInOntology(roModel, sourceIndividuals);
        targetIndividuals = removeIndividualsNotInOntology(roModel, targetIndividuals);
        GeneralTraceabilityMatrix gtMatrix = new GeneralTraceabilityMatrix(sourceIndividuals, targetIndividuals, roModel);
        String[][] cells = gtMatrix.getMatrixCells();
        String[] rowHeaders = gtMatrix.getSourceIndividuals();
        String[] colHeaders = gtMatrix.getTargetIndividuals();
        Table table = new Table(cells);
        addRowHeaders(table, rowHeaders);
        addColumnHeaders(table, colHeaders);
        return table;
    }

    private List<String> removeIndividualsNotInOntology(ROModel roModel, List<String> indNames) {
        Set<String> namesInOnt = roModel.getOntologyIndividualNames();
        List<String> newInds = new LinkedList<String>();
        for (String name : indNames) {
            if (namesInOnt.contains(name)) {
                newInds.add(name);
            }
        }
        return newInds;
    }

    private void addRowHeaders(Table table, String[] headerContent) {
        for (String text : headerContent) {
            TableCell cell = new TableCell(text);
            table.addRowHeaderCell(cell);
        }
    }

    private void addColumnHeaders(Table table, String[] headerContent) {
        for (String text : headerContent) {
            TableCell cell = new TableCell(text);
            table.addColumnHeaderCell(cell);
        }
    }

    @Override
    public OperationConfiguration createConfiguration() {
        return new Ontology2MatrixConfiguration();
    }

    @Override
    public String getInputRepresentationType() {
        return OwlRepresentation.TYPE;
    }

    @Override
    public String getResultingRepresentationType() {
        return TableRepresentation.TYPE;
    }
}
