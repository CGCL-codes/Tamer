package net.sourceforge.fluxion.runcible.graph.factory;

import net.sourceforge.fluxion.graph.Edge;
import net.sourceforge.fluxion.runcible.DataClause;
import net.sourceforge.fluxion.runcible.IndividualSelection;
import net.sourceforge.fluxion.runcible.Rule;
import net.sourceforge.fluxion.runcible.visitor.RuleVisitor;
import net.sourceforge.fluxion.runcible.graph.*;
import net.sourceforge.fluxion.runcible.graph.exception.UnsupportedTypeException;
import net.sourceforge.fluxion.runcible.graph.utils.OWLTypingUtils;
import net.sourceforge.fluxion.runcible.impl.InPlace;
import net.sourceforge.fluxion.runcible.impl.RootAt;
import net.sourceforge.fluxion.runcible.impl.SelectIndividualValue;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.*;
import java.net.URI;
import java.net.URL;

/**
 * SourceGraphFactory generates a graph representing the selections in a
 * mapping, and the relationships between them.
 *
 * @author Tony Burdett
 * @version 1.0
 * @date 20-Apr-2007
 */
public class SourceOWLGraphFactory implements OWLGraphFactory, RuleVisitor {

    private OWLGraph sourceGraph;

    private OWLMappingNode cursor;

    private SourceOWLGraphFactory() {
        sourceGraph = new DefaultGraph();
        sourceGraph.setName("Source_Ontology_Graph");
    }

    public SourceOWLGraphFactory(URL ontologyPhysicalLocation) {
        this();
        sourceGraph.setOWLOntologyLocation(ontologyPhysicalLocation);
    }

    public SourceOWLGraphFactory(OWLOntology owlOntology) {
        this();
        sourceGraph.setOWLOntology(owlOntology);
    }

    public OWLGraph getGraph() {
        return sourceGraph;
    }

    public void visit(Rule rule) {
        OWLMappingNode lastPointer = cursor;
        cursor = chartIndividualSelection(rule.getIndividualSelection());
        cursor.setLabel(rule.getForall().getVariableID());
        for (DataClause dataClause : rule.getDataClauses()) {
            chartDataClause(dataClause);
        }
        for (Rule nextRule : rule.getNextRules()) {
            visit(nextRule);
        }
        cursor = lastPointer;
    }

    protected OWLMappingNode chartIndividualSelection(IndividualSelection selection) {
        if (selection instanceof SelectIndividualValue) {
            return chartIndividualSelection((SelectIndividualValue) selection);
        }
        if (selection instanceof InPlace) {
            return chartIndividualSelection((InPlace) selection);
        }
        if (selection instanceof RootAt) {
            return chartIndividualSelection((RootAt) selection);
        }
        return cursor;
    }

    protected OWLMappingNode chartIndividualSelection(SelectIndividualValue ind_value) {
        if (cursor != null) {
            OWLMappingNode node = new WalkedToObjectNode();
            try {
                OWLOntology ontology = sourceGraph.getOWLOntology();
                OWLProperty property = retrieveProperty(ontology, ind_value.getObjectPropertyURI());
                OWLObject type = OWLTypingUtils.calculateOWLRangeType(ontology, cursor.getOWLObject(), property);
                node.setOWLObject(type);
            } catch (UnsupportedTypeException e) {
                System.err.println("Hit an error while trying to set what should be a correct " + "resource type? Occurred at SourceOWLGraphFactory at line 109");
                e.printStackTrace();
            }
            OWLEdge edge = new WalkEdge();
            OWLProperty prop = retrieveProperty(sourceGraph.getOWLOntology(), ind_value.getObjectPropertyURI());
            edge.setOWLProperty(prop);
            edge.setLabel(prop.getURI().getFragment());
            sourceGraph.add(node);
            edge.setHeadNode(cursor);
            edge.setTailNode(node);
            sourceGraph.add(edge);
            if (ind_value.hasNext()) {
                cursor = node;
                node = chartIndividualSelection(ind_value.getNext());
            }
            return node;
        }
        return cursor;
    }

    protected OWLMappingNode chartIndividualSelection(InPlace in_place) {
        OWLMappingNode node = null;
        try {
            if (in_place.getOWLClassURI() != null) {
                node = new MemberOfNode();
                node.setOWLObject(retrieveClass(sourceGraph.getOWLOntology(), in_place.getOWLClassURI()));
            } else {
                OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
                OWLProperty property = retrieveProperty(sourceGraph.getOWLOntology(), in_place.getRestrictionProperty());
                OWLConstant constant = factory.getOWLUntypedConstant(in_place.getRestrictionValue());
                OWLRestriction restriction = factory.getOWLDataValueRestriction((OWLDataPropertyExpression) property, constant);
                node = new RestrictedNode();
                node.setOWLObject(restriction);
            }
        } catch (UnsupportedTypeException e) {
            System.err.println("Hit an error while trying to set what should be a correct resource " + "type? Occurred at SourceOWLGraphFactory at line 170");
            e.printStackTrace();
        }
        sourceGraph.add(node);
        if (cursor != null) {
            Edge edge = new IsAEdge();
            edge.setHeadNode(cursor);
            edge.setTailNode(node);
            sourceGraph.add(edge);
        }
        if (in_place.hasNext()) {
            cursor = node;
            node = chartIndividualSelection(in_place.getNext());
        }
        return node;
    }

    protected OWLMappingNode chartIndividualSelection(RootAt root_at) {
        OWLMappingNode node = null;
        try {
            if (root_at.getOWLClassURI() != null) {
                node = new MemberOfNode();
                node.setOWLObject(retrieveClass(sourceGraph.getOWLOntology(), root_at.getOWLClassURI()));
            }
        } catch (UnsupportedTypeException e) {
            System.err.println("Hit an error while trying to set what should be a correct " + "resource type? Occurred at SourceOWLGraphFactory at line 213");
            e.printStackTrace();
        }
        sourceGraph.add(node);
        if (cursor != null) {
            Edge edge = new NestedEdge();
            edge.setHeadNode(cursor);
            edge.setTailNode(node);
            sourceGraph.add(edge);
        }
        if (root_at.hasNext()) {
            cursor = node;
            node = chartIndividualSelection(root_at.getNext());
        }
        return node;
    }

    protected OWLMappingNode chartDataClause(DataClause dataClause) {
        if (cursor != null) {
            WalkedToDataNode node = new WalkedToDataNode();
            node.setLabel(dataClause.getVariableID());
            try {
                OWLOntology ontology = sourceGraph.getOWLOntology();
                OWLProperty property = retrieveProperty(ontology, dataClause.getDataSelection().getDataPropertyURI());
                OWLObject type = OWLTypingUtils.calculateOWLRangeType(ontology, cursor.getOWLObject(), property);
                node.setOWLObject(type);
            } catch (UnsupportedTypeException e) {
                System.err.println("Hit an error while trying to set what should be a correct " + "resource type? Occurred at SourceOWLGraphFactory at line 258");
                e.printStackTrace();
            }
            OWLEdge edge = new WalkEdge();
            OWLProperty property = retrieveProperty(sourceGraph.getOWLOntology(), dataClause.getDataSelection().getDataPropertyURI());
            edge.setOWLProperty(property);
            edge.setLabel(property.getURI().getFragment());
            sourceGraph.add(node);
            edge.setHeadNode(cursor);
            edge.setTailNode(node);
            sourceGraph.add(edge);
        }
        return cursor;
    }

    private static OWLClass retrieveClass(OWLOntology ontology, URI classURI) {
        for (OWLClass cls : ontology.getReferencedClasses()) {
            if (cls.getURI().toString().matches(classURI.toString())) {
                return cls;
            }
        }
        return null;
    }

    private static OWLProperty retrieveProperty(OWLOntology ontology, URI propertyURI) {
        for (OWLObjectProperty objProp : ontology.getReferencedObjectProperties()) {
            if (objProp.getURI().toString().matches(propertyURI.toString())) {
                return objProp;
            }
        }
        for (OWLDataProperty dataProp : ontology.getReferencedDataProperties()) {
            if (dataProp.getURI().toString().matches(propertyURI.toString())) {
                return dataProp;
            }
        }
        System.err.println("Property specified by the URI " + propertyURI.toString() + " is not present in the ontology for it's namespace - check versioning?");
        return null;
    }
}
