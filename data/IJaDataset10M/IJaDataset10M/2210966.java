package jp.ac.keio.ae.comp.yamaguti.doddle.utils;

import java.io.*;
import java.util.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.data.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.ui.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * @author takeshi morita
 * 
 */
public class JenaModelMaker {

    public static final String SKOS_URI = "http://www.w3.org/2004/02/skos/core#";

    public static final Property SKOS_PREFLABEL = ResourceFactory.createProperty(SKOS_URI + "prefLabel");

    private static void addDefaultConceptInfo(Model ontology, Resource child, ConceptTreeNode node) {
        ontology.add(child, SKOS_PREFLABEL, ontology.createLiteral(node.getConcept().getWord()));
        Map<String, List<DODDLELiteral>> langLabelListMap = node.getLangLabelLiteralListMap();
        for (String lang : langLabelListMap.keySet()) {
            for (DODDLELiteral label : langLabelListMap.get(lang)) {
                ontology.add(child, RDFS.label, ontology.createLiteral(label.getString(), label.getLang()));
            }
        }
        Map<String, List<DODDLELiteral>> langDescriptionListMap = node.getLangDescriptionLiteralListMap();
        for (String lang : langDescriptionListMap.keySet()) {
            for (DODDLELiteral description : langDescriptionListMap.get(lang)) {
                ontology.add(child, RDFS.comment, ontology.createLiteral(description.getString(), description.getLang()));
            }
        }
    }

    private static Resource createResource(ConceptTreeNode node) {
        return ResourceFactory.createResource(node.getURI());
    }

    private static Resource getResource(ConceptTreeNode node, Model ontology) {
        ConceptTreeNode parentNode = (ConceptTreeNode) node.getParent();
        return ontology.getResource(parentNode.getURI());
    }

    private static void addClassStatement(String type, Model ontology, Resource child, Resource parent) {
        if (type == ConceptTreePanel.CLASS_ISA_TREE) {
            ontology.add(child, RDFS.subClassOf, parent);
        } else if (type == ConceptTreePanel.CLASS_HASA_TREE) {
            ontology.add(child, DODDLE.HASA_PROPERTY, parent);
        }
    }

    public static Model makeClassModel(ConceptTreeNode node, Model ontology, String type) {
        if (node == null) {
            return ontology;
        }
        if (node.isLeaf()) {
            Resource child = createResource(node);
            ontology.add(child, RDF.type, OWL.Class);
            addDefaultConceptInfo(ontology, child, node);
            if (node.getParent() != null) {
                Resource parent = getResource(node, ontology);
                addClassStatement(type, ontology, child, parent);
            }
        } else {
            if (node.isRoot()) {
                ontology.add(createResource(node), RDF.type, OWL.Class);
            } else {
                Resource child = createResource(node);
                ontology.add(child, RDF.type, OWL.Class);
                addDefaultConceptInfo(ontology, child, node);
                Resource parent = getResource(node, ontology);
                addClassStatement(type, ontology, child, parent);
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            ontology = makeClassModel((ConceptTreeNode) node.getChildAt(i), ontology, type);
        }
        return ontology;
    }

    private static void addRegion(Resource resource, Property region, Model ontology, Set<String> regionSet) {
        for (String uri : regionSet) {
            ontology.add(resource, region, ontology.getResource(uri));
        }
    }

    private static void addPropertyStatement(String type, Model ontology, Resource child, Resource parent) {
        if (type == ConceptTreePanel.PROPERTY_ISA_TREE) {
            ontology.add(child, RDFS.subPropertyOf, parent);
        } else if (type == ConceptTreePanel.PROPERTY_HASA_TREE) {
            ontology.add(child, DODDLE.HASA_PROPERTY, parent);
        }
    }

    public static Model makePropertyModel(ConceptTreeNode node, Model ontology, String type) {
        if (node == null) {
            return ontology;
        }
        if (node.isLeaf() && !node.isRoot()) {
            Resource child = createResource(node);
            ontology.add(child, RDF.type, OWL.ObjectProperty);
            addDefaultConceptInfo(ontology, child, node);
            VerbConcept vc = (VerbConcept) node.getConcept();
            addRegion(child, RDFS.domain, ontology, vc.getDomainSet());
            addRegion(child, RDFS.range, ontology, vc.getRangeSet());
            Resource parent = getResource(node, ontology);
            addPropertyStatement(type, ontology, child, parent);
        } else {
            if (node.isRoot()) {
                ontology.add(createResource(node), RDF.type, OWL.ObjectProperty);
            } else {
                Resource child = createResource(node);
                ontology.add(child, RDF.type, OWL.ObjectProperty);
                addDefaultConceptInfo(ontology, child, node);
                VerbConcept vc = (VerbConcept) node.getConcept();
                addRegion(child, RDFS.domain, ontology, vc.getDomainSet());
                addRegion(child, RDFS.range, ontology, vc.getRangeSet());
                Resource parent = getResource(node, ontology);
                addPropertyStatement(type, ontology, child, parent);
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            ontology = makePropertyModel((ConceptTreeNode) node.getChildAt(i), ontology, type);
        }
        return ontology;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }
        Translator.loadDODDLEComponentOntology(DODDLEConstants.LANG);
        String ontFileName = args[0];
        String outputFileName = args[1];
        File ontFile = new File(ontFileName);
        OWLOntologyManager.addRefOntology(ontFile);
        ReferenceOWLOntology refOnt = OWLOntologyManager.getRefOntology(ontFile.getAbsolutePath());
        Collection<String> classSet = refOnt.getClassSet();
        StringBuilder builder = new StringBuilder("");
        for (String uri : classSet) {
            Concept c = refOnt.getConcept(uri);
            builder.append(uri);
            builder.append(",");
            for (List<DODDLELiteral> literalList : c.getLangLabelListMap().values()) {
                for (DODDLELiteral literal : literalList) {
                    builder.append(literal.getString());
                    builder.append(",");
                }
            }
            for (List<DODDLELiteral> literalList : c.getLangDescriptionListMap().values()) {
                for (DODDLELiteral literal : literalList) {
                    builder.append(literal.getString());
                    builder.append(",");
                }
            }
            builder.append("\n");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
