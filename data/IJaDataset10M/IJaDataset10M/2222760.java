package org.deft.requirementsontology;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import edu.uci.ics.jung.graph.util.EdgeType;

public class ROModel extends AbstractROModel {

    /**
	 * creates a requirements ontology model with a complete goal model graph (to a given ontology accessor)
	 * the goal model graph contains directed and undirected edges
	 */
    public ROModel(OntologyAccessor ontologyAccessor) {
        super(ontologyAccessor);
    }

    @Override
    protected void initializeGoalModelGraph() {
        this.initializeStandardGoalModelGraph();
    }

    /**
	 * inserts all object property edges into the goal model graph (to a given simple object property name)
	 * inserts an object property edge only if the object property edge between source individual and target individual does not already exists in the goal model graph
	 * inserts an object property edge only if source individual vertex and target individual vertex exist already in the goal model graph
	 * inserts an object property edge as an undirected edge into the goal model graph if the object property is symmetric
	 */
    @Override
    public void insertObjectProperty(String simpleObjectPropertyName) {
        Collection<IndividualVertex> sourceIndividualCollection = this.getGoalModelGraph().getVertices();
        Collection<IndividualVertex> targetIndividualCollection = this.getGoalModelGraph().getVertices();
        for (IndividualVertex sourceIndividualVertex : sourceIndividualCollection) {
            for (IndividualVertex targetIndividualVertex : targetIndividualCollection) {
                Collection<ObjectPropertyEdge> objectPropertyCollection = this.getObjectProperties(sourceIndividualVertex.getSimpleName(), targetIndividualVertex.getSimpleName());
                boolean objectPropertyAlreadyInGraph = false;
                for (ObjectPropertyEdge objectProperty : objectPropertyCollection) {
                    if (objectProperty.getSimpleName().equals(simpleObjectPropertyName)) {
                        objectPropertyAlreadyInGraph = true;
                    }
                }
                if (objectPropertyAlreadyInGraph == false) {
                    Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyMap = this.getOntologyAccessor().getOwlObjectProperties(sourceIndividualVertex.getSimpleName());
                    if (objectPropertyMap != null) {
                        for (OWLObjectPropertyExpression objectProperty : objectPropertyMap.keySet()) {
                            if (objectProperty.toString().equals(simpleObjectPropertyName)) {
                                for (OWLIndividual individual : objectPropertyMap.get(objectProperty)) {
                                    if (individual.toString().equals(targetIndividualVertex.getSimpleName())) {
                                        if (this.getOntologyAccessor().isSymmetricObjectProperty(objectProperty.toString()) == false) {
                                            this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), sourceIndividualVertex, targetIndividualVertex), sourceIndividualVertex, targetIndividualVertex, EdgeType.DIRECTED);
                                        } else {
                                            this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), sourceIndividualVertex, targetIndividualVertex), sourceIndividualVertex, targetIndividualVertex, EdgeType.UNDIRECTED);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * inserts all object property edges into the goal model graph (to a given simple individual name)
	 * inserts an object property edge only if source/target individual vertices exist already in the goal model graph
	 * inserts an object property edge as an undirected edge into the goal model graph if the object property is symmetric
	 */
    @Override
    public void insertObjectPropertiesForIndividual(String sourceIndividualName) {
        IndividualVertex sourceIndividualVertex = this.getIndividual(sourceIndividualName);
        if (sourceIndividualVertex != null) {
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyMap = this.getOntologyAccessor().getOwlObjectProperties(sourceIndividualName);
            if (objectPropertyMap != null) {
                for (OWLObjectPropertyExpression objectProperty : objectPropertyMap.keySet()) {
                    Set<OWLIndividual> individualSet = objectPropertyMap.get(objectProperty);
                    for (OWLIndividual targetIndividual : individualSet) {
                        Collection<IndividualVertex> individualVertexCollection = this.getIndividuals();
                        for (IndividualVertex targetIndividualVertex : individualVertexCollection) {
                            if (targetIndividual.toString().equals(targetIndividualVertex.getSimpleName())) {
                                Collection<ObjectPropertyEdge> objectPropertyCollection = this.getObjectProperties(sourceIndividualName, targetIndividualVertex.getSimpleName());
                                boolean objectPropertyAlreadyInGraph = false;
                                for (ObjectPropertyEdge propertyInGraph : objectPropertyCollection) {
                                    if (propertyInGraph.getSimpleName().equals(objectProperty.toString())) {
                                        objectPropertyAlreadyInGraph = true;
                                    }
                                }
                                if (objectPropertyAlreadyInGraph == false) {
                                    if (this.getOntologyAccessor().isSymmetricObjectProperty(objectProperty.toString()) == false) {
                                        this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), sourceIndividualVertex, targetIndividualVertex), sourceIndividualVertex, targetIndividualVertex, EdgeType.DIRECTED);
                                    } else {
                                        this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), sourceIndividualVertex, targetIndividualVertex), sourceIndividualVertex, targetIndividualVertex, EdgeType.UNDIRECTED);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * inserts an individual vertex into the goal model graph (to a given simple individual name)
	 * inserts only outgoing object properties of the given individual
	 * inserts an object property edge only if the target individual vertex already exists in the goal model graph
	 * inserts all data properties of the individual into the data property map
	 */
    @Override
    public void insertIndividualWithProperties(String simpleIndividualName) {
        this.insertIndividual(simpleIndividualName);
        Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyMap = this.getOntologyAccessor().getOwlObjectProperties(simpleIndividualName);
        if (objectPropertyMap != null) {
            for (OWLObjectPropertyExpression objectProperty : objectPropertyMap.keySet()) {
                for (OWLIndividual targetIndividual : objectPropertyMap.get(objectProperty)) {
                    if (this.containsIndividual(targetIndividual.toString())) {
                        Collection<ObjectPropertyEdge> objectPropertyCollection = this.getObjectProperties(simpleIndividualName, targetIndividual.toString());
                        boolean objectPropertyAlreadyInGraph = false;
                        for (ObjectPropertyEdge propertyInGraph : objectPropertyCollection) {
                            if (propertyInGraph.getSimpleName().equals(objectProperty.toString())) {
                                objectPropertyAlreadyInGraph = true;
                            }
                        }
                        if (objectPropertyAlreadyInGraph == false) {
                            if (this.getOntologyAccessor().isSymmetricObjectProperty(objectProperty.toString()) == false) {
                                this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), this.getIndividual(simpleIndividualName), this.getIndividual(targetIndividual.toString())), this.getIndividual(simpleIndividualName), this.getIndividual(targetIndividual.toString()), EdgeType.DIRECTED);
                            } else {
                                this.getGoalModelGraph().addEdge(new ObjectPropertyEdge(objectProperty.toString(), this.getIndividual(simpleIndividualName), this.getIndividual(targetIndividual.toString())), this.getIndividual(simpleIndividualName), this.getIndividual(targetIndividual.toString()), EdgeType.UNDIRECTED);
                            }
                        }
                    }
                }
            }
        }
        Map<OWLDataPropertyExpression, Set<OWLConstant>> dataPropertyMap = this.getOntologyAccessor().getOwlDataProperties(simpleIndividualName);
        if (dataPropertyMap != null) {
            for (OWLDataPropertyExpression dataProperty : dataPropertyMap.keySet()) {
                this.insertDataProperty(dataProperty.toString());
            }
        }
    }
}
