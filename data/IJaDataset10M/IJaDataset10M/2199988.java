package com.gusto.engine.semsim.measures.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.gusto.engine.semsim.measures.ResourceSimilarity;
import com.gusto.engine.semsim.measures.SetSimilarity;
import com.gusto.engine.semsim.measures.Similarity;
import com.gusto.engine.semsim.measures.ValueSimilarity;
import com.gusto.engine.semsim.measures.exception.SimilarityException;
import com.gusto.engine.semsim.measures.types.SemTypes;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class PropertySimilarity implements ResourceSimilarity {

    private Logger log = Logger.getLogger(PropertySimilarity.class);

    private PropertySimilarityParam param = new PropertySimilarityParam();

    public void setParam(PropertySimilarityParam param) {
        this.param = param;
    }

    public String getId() {
        String id = "<" + getClass().getName() + "|";
        id += param.getProperty() + "|";
        id += param.getType() + "|";
        id += param.getSimilarity().getId() + "|";
        id += ">";
        return id;
    }

    public Double getSimilarity(Resource value1, Resource value2) throws SimilarityException {
        Model model = value1.getModel();
        Property property = model.getProperty(param.getProperty());
        SemTypes type = param.getType();
        Similarity similarity = param.getSimilarity();
        Double sim = getSimilarity(value1, value2, property, type, similarity);
        if (sim.equals(Double.NaN)) {
            sim = 0.0;
        }
        log.info("Property similarity '" + property.getLocalName() + "' (" + value1.getLocalName() + ", " + value2.getLocalName() + ") = " + (sim));
        return (sim);
    }

    private Double getSimilarity(Resource value1, Resource value2, Property property, SemTypes type, Similarity similarity) throws SimilarityException {
        StmtIterator stmtI1 = value1.listProperties(property);
        StmtIterator stmtI2 = value2.listProperties(property);
        if (type.equals(SemTypes.VALUE)) {
            ValueSimilarity valueSimilarity = (ValueSimilarity) similarity;
            if (stmtI1.hasNext() && stmtI2.hasNext()) {
                Literal literal1 = stmtI1.nextStatement().getLiteral();
                Literal literal2 = stmtI2.nextStatement().getLiteral();
                double sim = valueSimilarity.getSimilarity(literal1, literal2);
                return sim;
            }
        } else if (type.equals(SemTypes.RESOURCE)) {
            ResourceSimilarity resourceSimilarity = (ResourceSimilarity) similarity;
            if (stmtI1.hasNext() && stmtI2.hasNext()) {
                Resource resource1 = stmtI1.nextStatement().getResource();
                Resource resource2 = stmtI2.nextStatement().getResource();
                double sim = resourceSimilarity.getSimilarity(resource1, resource2);
                return sim;
            }
        } else if (type.equals(SemTypes.SET)) {
            SetSimilarity setSimilarity = (SetSimilarity) similarity;
            List<RDFNode> list1 = new ArrayList<RDFNode>();
            List<RDFNode> list2 = new ArrayList<RDFNode>();
            while (stmtI1.hasNext()) {
                Statement stmt = stmtI1.nextStatement();
                list1.add(stmt.getObject());
            }
            while (stmtI2.hasNext()) {
                Statement stmt = stmtI2.nextStatement();
                list2.add(stmt.getObject());
            }
            return setSimilarity.getSimilarity(list1, list2);
        }
        return Double.NaN;
    }
}
