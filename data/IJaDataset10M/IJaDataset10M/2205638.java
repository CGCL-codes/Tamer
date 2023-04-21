package org.tripcom.dam.utils.ontology;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;

public class JenaUtils {

    /**
 * Loads an ontology model and gets the inferred information provided by a reasoner,
 * adding the inferred information to the original ontology model.
 * @param ontologySource the file stream where the ontology is defined
 * @param reasoner the reasoner used to infer the information from the model
 * @return a model composed by the original one and the inferred information
 */
    public Model loadModel(FileInputStream ontologySource, Reasoner reasoner) {
        InfModel ontologyModel = ModelFactory.createInfModel(reasoner, loadModel(ontologySource));
        Model inferredModel = ontologyModel.getDeductionsModel();
        ontologyModel.add(inferredModel);
        return ontologyModel;
    }

    /**
 * Loads an ontology model from a file source
 * @param ontologySource the file stream where the ontology is defined
 * @return the ontology model
 */
    public Model loadModel(FileInputStream ontologySource) {
        Model originalModel = ModelFactory.createOntologyModel();
        originalModel.read(ontologySource, null);
        return originalModel;
    }

    /**
 * Executes a SPARQL query against a given ontology model
 * @param query The String format of the SPARQL query. It must be obviously correct according to SPARQL specification
 * @param ontology The ontology model used to make the queries against
 * @return The list of results that matched the executed query 
 */
    public ResultSet execQuery(String query, Model ontology) {
        Query SPARQLquery = QueryFactory.create(query);
        QueryExecution queryEngine = QueryExecutionFactory.create(SPARQLquery, ontology);
        ResultSet result = queryEngine.execSelect();
        return result;
    }

    /**
 * Writes an ontology model to a file
 * @param filePath The path of the file where the ontology will be written
 * @param ontology The ontology to be exported
 * @throws IOException Reports any problem with the file creation or writing
 */
    public void writeModel(String filePath, Model ontology) throws IOException {
        ontology.write(new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "ISO-8859-1")));
    }
}
