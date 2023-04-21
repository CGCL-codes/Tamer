package net.sourceforge.ondex.parser.n3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.args.FileArgumentDefinition;
import net.sourceforge.ondex.args.StringArgumentDefinition;
import net.sourceforge.ondex.args.StringMappingPairArgumentDefinition;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.DataSource;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.exception.type.AttributeNameMissingException;
import net.sourceforge.ondex.exception.type.DataSourceMissingException;
import net.sourceforge.ondex.exception.type.ConceptClassMissingException;
import net.sourceforge.ondex.exception.type.EvidenceTypeMissingException;
import net.sourceforge.ondex.exception.type.RelationTypeMissingException;
import net.sourceforge.ondex.parser.ONDEXParser;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Parser extends ONDEXParser {

    private static final String ccArg = "ConceptClassPredicate";

    private static final String ccDesc = "A pair of ConceptClass id and N3 predicate which should be assigned to this ConceptClass, a ConceptClass usually has more than one predicate.";

    private static final String rtArg = "RelationTypePredicate";

    private static final String rtDesc = "A pair of RelationType id and N3 predicate which should be treated as a RelationType, there can only be one predicate per RelationType.";

    private static final String nameArg = "ConceptNamePredicate";

    private static final String nameDesc = "Which predicates should be treated as ConceptName (with / without preferred flag) on a Concept, this can be multiple.";

    private static final String caArg = "ConceptAccessionPredicate";

    private static final String caDesc = "A pair of id of the DataSource of the ConceptAccession and the N3 predicate to be used as a ConceptAccession on a Concept.";

    private static final String anConceptArg = "ConceptAttributeNamePredicate";

    private static final String anConceptDesc = "A pair of id of the AttributeName of the Attribute and the SPAQRL query (concept id, Attribute value) to be used on Concepts.";

    private static final String anRelationArg = "RelationAttributeNamePredicate";

    private static final String anRelationDesc = "A pair of id of the AttributeName of the Attribute and the SPARQL query (fromConcept id, ofType URI, toConcept id, Attribute value) to be used on Relations.";

    private static final String cvArg = "DataSource";

    private static final String cvDesc = "The id of the DataSource (DataSource) created concepts should belong to.";

    @Override
    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        FileArgumentDefinition inFile = new FileArgumentDefinition(FileArgumentDefinition.INPUT_FILE, FileArgumentDefinition.INPUT_FILE_DESC, true, true, false, false);
        StringMappingPairArgumentDefinition cc = new StringMappingPairArgumentDefinition(ccArg, ccDesc, true, null, true);
        StringMappingPairArgumentDefinition rt = new StringMappingPairArgumentDefinition(rtArg, rtDesc, false, null, true);
        StringMappingPairArgumentDefinition name = new StringMappingPairArgumentDefinition(nameArg, nameDesc, false, null, true);
        StringMappingPairArgumentDefinition acc = new StringMappingPairArgumentDefinition(caArg, caDesc, false, null, true);
        StringMappingPairArgumentDefinition anC = new StringMappingPairArgumentDefinition(anConceptArg, anConceptDesc, false, null, true);
        StringMappingPairArgumentDefinition anR = new StringMappingPairArgumentDefinition(anRelationArg, anRelationDesc, false, null, true);
        StringArgumentDefinition cv = new StringArgumentDefinition(cvArg, cvDesc, false, "UC", false);
        return new ArgumentDefinition<?>[] { inFile, cc, rt, name, acc, anC, anR, cv };
    }

    @Override
    public String getId() {
        return "n3";
    }

    @Override
    public String getName() {
        return "Simple RDF N3 parser";
    }

    @Override
    public String getVersion() {
        return "24/03/2010";
    }

    @Override
    public String[] requiresValidators() {
        return new String[0];
    }

    Map<String, ONDEXConcept> concepts = new HashMap<String, ONDEXConcept>();

    Map<String, ONDEXRelation> relations = new HashMap<String, ONDEXRelation>();

    @Override
    public void start() throws Exception {
        EvidenceType evidence = graph.getMetaData().getEvidenceType("IMPD");
        if (evidence == null) throw new EvidenceTypeMissingException("IMPD");
        String cvId = (String) args.getUniqueValue(cvArg);
        DataSource dataSource = graph.getMetaData().getDataSource(cvId);
        if (dataSource == null) throw new DataSourceMissingException(cvId);
        Map<ConceptClass, Set<String>> ccPredicates = new HashMap<ConceptClass, Set<String>>();
        for (Object o : args.getObjectValueArray(ccArg)) {
            String[] pair = o.toString().split(",");
            String ccId = pair[0];
            ConceptClass cc = graph.getMetaData().getConceptClass(ccId);
            if (cc == null) throw new ConceptClassMissingException(ccId);
            if (!ccPredicates.containsKey(cc)) ccPredicates.put(cc, new HashSet<String>());
            ccPredicates.get(cc).add(pair[1]);
        }
        Map<RelationType, String> rtPredicate = new HashMap<RelationType, String>();
        for (Object o : args.getObjectValueArray(rtArg)) {
            String[] pair = o.toString().split(",");
            String rtId = pair[0];
            RelationType rt = graph.getMetaData().getRelationType(rtId);
            if (rt == null) throw new RelationTypeMissingException(rtId);
            rtPredicate.put(rt, pair[1]);
        }
        Map<String, Boolean> namePredicates = new HashMap<String, Boolean>();
        for (Object o : args.getObjectValueArray(nameArg)) {
            String[] pair = o.toString().split(",");
            String predicate = pair[1];
            namePredicates.put(predicate, Boolean.parseBoolean(pair[0]));
        }
        Map<AttributeName, String> anRelationPredicates = new HashMap<AttributeName, String>();
        for (Object o : args.getObjectValueArray(anRelationArg)) {
            String[] pair = o.toString().split(",");
            String anId = pair[0];
            AttributeName an = graph.getMetaData().getAttributeName(anId);
            if (an == null) throw new AttributeNameMissingException(anId);
            anRelationPredicates.put(an, pair[1]);
        }
        Map<AttributeName, String> anConceptPredicates = new HashMap<AttributeName, String>();
        for (Object o : args.getObjectValueArray(anConceptArg)) {
            String[] pair = o.toString().split(",");
            String anId = pair[0];
            AttributeName an = graph.getMetaData().getAttributeName(anId);
            if (an == null) throw new AttributeNameMissingException(anId);
            anConceptPredicates.put(an, pair[1]);
        }
        File file = new File((String) args.getUniqueValue(FileArgumentDefinition.INPUT_FILE));
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Model model = ModelFactory.createDefaultModel();
        RDFReader rdfreader = model.getReader("N3");
        rdfreader.read(model, reader, "");
        for (ConceptClass key : ccPredicates.keySet()) {
            parseConceptsPerConceptClass(model, key, ccPredicates.get(key), dataSource, evidence);
        }
        for (RelationType key : rtPredicate.keySet()) {
            parseRelationsPerRelationType(model, key, rtPredicate.get(key), evidence);
        }
        for (String key : namePredicates.keySet()) {
            parseConceptNames(model, key, namePredicates.get(key));
        }
        for (AttributeName an : anConceptPredicates.keySet()) {
            parseAttributeNamesConcepts(model, an, anConceptPredicates.get(an));
        }
        for (AttributeName an : anRelationPredicates.keySet()) {
            parseAttributeNamesRelations(model, an, anRelationPredicates.get(an));
        }
    }

    /**
	 * Associates statements as Attribute to relations.
	 * 
	 * @param model
	 *            JENA model to search
	 * @param an
	 *            AttributeName to use
	 * @param path
	 *            Path describing how to reach Attribute value
	 */
    private void parseAttributeNamesRelations(Model model, AttributeName attrname, String path) {
        Query query = QueryFactory.create(path, Syntax.syntaxARQ);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            Iterator<String> varNames = qs.varNames();
            Resource fromResource = qs.getResource(varNames.next());
            String fromUri = fromResource.getURI();
            if (fromUri == null) {
                fromUri = fromResource.getId().toString();
                System.err.println("fromUri is null in query solution " + qs + ". Using AnonId " + fromUri + " instead.");
            }
            Literal type = qs.getLiteral(varNames.next());
            Resource toResource = qs.getResource(varNames.next());
            String toUri = toResource.getURI();
            if (toUri == null) {
                toUri = toResource.getId().toString();
                System.err.println("toUri is null in query solution " + qs + ". Using AnonId " + toUri + " instead.");
            }
            String combined = fromUri + "\t" + type.getString() + "\t" + toUri;
            ONDEXRelation relation = relations.get(combined);
            if (relation == null) {
                System.err.println("relation not found for key " + combined);
                continue;
            }
            Literal valueLiteral = qs.getLiteral(varNames.next());
            Object value = valueLiteral.getValue();
            if (attrname.getDataType().isAssignableFrom(value.getClass())) {
                relation.createAttribute(attrname, value, false);
            } else {
                System.err.println("Trying to put wrong literal " + valueLiteral + " for AttributeName " + attrname.getDataType() + " compared to value " + value.getClass());
            }
        }
        qe.close();
    }

    /**
	 * Associates statements as Attribute to concepts.
	 * 
	 * @param model
	 *            JENA model to search
	 * @param attrname
	 *            AttributeName to use
	 * @param path
	 *            Path describing how to reach Attribute value
	 */
    private void parseAttributeNamesConcepts(Model model, AttributeName attrname, String path) {
        Query query = QueryFactory.create(path, Syntax.syntaxARQ);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            Iterator<String> varNames = qs.varNames();
            Resource conceptResource = qs.getResource(varNames.next());
            String uri = conceptResource.getURI();
            if (uri == null) {
                uri = conceptResource.getId().toString();
                System.err.println("concept uri is null in query solution " + qs + ". Using AnonId " + uri + " instead.");
            }
            ONDEXConcept concept = concepts.get(uri);
            if (concept == null) {
                System.err.println("concept not found in query solution " + qs);
                continue;
            }
            Literal valueLiteral = qs.getLiteral(varNames.next());
            Object value = valueLiteral.getValue();
            if (attrname.getDataType().isAssignableFrom(value.getClass())) {
                concept.createAttribute(attrname, value, false);
            } else {
                System.err.println("Trying to put wrong literal " + valueLiteral + " for AttributeName " + attrname.getDataType() + " compared to value " + value.getClass());
            }
        }
        qe.close();
    }

    /**
	 * Searches the model for predicates to be used as concept names.
	 * 
	 * @param model
	 *            JENA model to search
	 * @param uri
	 *            URI of the property constituting a concept name
	 * @param preferred
	 *            whether or not this concept name is preferred
	 */
    private void parseConceptNames(Model model, String uri, boolean preferred) {
        Property p = model.getProperty(uri);
        StmtIterator it = model.listStatements(null, p, (Resource) null);
        while (it.hasNext()) {
            Statement st = it.next();
            String fromUri = st.getSubject().getURI();
            if (fromUri == null) {
                fromUri = st.getSubject().getId().toString();
                System.err.println("fromUri is null in statement " + st + ". Using AnonId " + fromUri + " instead.");
            }
            ONDEXConcept concept = concepts.get(fromUri);
            if (concept == null) {
                System.err.println("concept not found in statement " + st);
                continue;
            }
            concept.createConceptName(st.getLiteral().getString(), preferred);
        }
    }

    /**
	 * Searches the model for statements with a predicate to be used as a
	 * relation type and constructs a relation.
	 * 
	 * @param model
	 *            JENA model to search
	 * @param ofType
	 *            RelationType for relations
	 * @param uri
	 *            URI of the property constituting a relation
	 * @param evidence
	 *            EvidenceType for relations
	 */
    private void parseRelationsPerRelationType(Model model, RelationType ofType, String uri, EvidenceType evidence) {
        Property p = model.getProperty(uri);
        StmtIterator it = model.listStatements(null, p, (Resource) null);
        while (it.hasNext()) {
            Statement st = it.next();
            String fromUri = st.getSubject().getURI();
            if (fromUri == null) {
                fromUri = st.getSubject().getId().toString();
                System.err.println("fromUri is null in statement " + st + ". Using AnonId " + fromUri + " instead.");
            }
            String toUri = st.getResource().getURI();
            if (toUri == null) {
                toUri = st.getResource().getId().toString();
                System.err.println("toUri is null in statement " + st + ". Using AnonId " + toUri + " instead.");
            }
            ONDEXConcept fromConcept = concepts.get(fromUri);
            if (fromConcept == null) {
                System.err.println("from concept not found in statement " + st);
                continue;
            }
            ONDEXConcept toConcept = concepts.get(toUri);
            if (toConcept == null) {
                System.err.println("to concept not found in statement " + st);
                continue;
            }
            ONDEXRelation r = graph.getFactory().createRelation(fromConcept, toConcept, ofType, evidence);
            relations.put(fromUri + "\t" + p.getURI() + "\t" + toUri, r);
        }
    }

    /**
	 * Searches the model for statements satisfying given predicate conditions
	 * and constructs an empty concept then.
	 * 
	 * @param model
	 *            JENA model to search
	 * @param ofType
	 *            ConceptClass for concepts
	 * @param predicates
	 *            predicates indicating ConceptClass
	 * @param elementOf
	 *            DataSource for concepts
	 * @param evidence
	 *            EvidenceType for concepts
	 */
    private void parseConceptsPerConceptClass(Model model, ConceptClass ofType, Set<String> predicates, DataSource elementOf, EvidenceType evidence) {
        Set<Resource> uniques = null;
        for (String uri : predicates) {
            Property p = model.getProperty(uri);
            if (uniques == null) uniques = model.listResourcesWithProperty(p).toSet(); else uniques.retainAll(model.listResourcesWithProperty(p).toSet());
        }
        for (Resource r : uniques) {
            String uri = r.getURI();
            if (uri != null) {
                ONDEXConcept c = graph.getFactory().createConcept(uri, elementOf, ofType, evidence);
                concepts.put(uri, c);
            } else {
                uri = r.getId().toString();
                System.err.println("No URI for Resource " + r + ". Using AnonId " + uri + " instead.");
                ONDEXConcept c = graph.getFactory().createConcept(uri, elementOf, ofType, evidence);
                concepts.put(uri, c);
            }
        }
    }
}
