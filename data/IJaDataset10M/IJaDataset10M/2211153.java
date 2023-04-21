package work;

import java.io.InputStream;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class ProcessOwlFile {

    private OntModel model;

    public void run() {
        readFile();
        queryOntology();
    }

    public void readFile() {
        model = ModelFactory.createOntologyModel();
        InputStream in = FileManager.get().open("./ontologies/imageSearchOntology.owl");
        model.read(in, "");
    }

    protected void queryOntology() {
        String queryString = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX owl:<http://www.w3.org/2002/07/owl#>" + "SELECT ?class " + "WHERE { " + "?class rdf:type owl:Class " + " }";
        String queryString2 = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX owl:<http://www.w3.org/2002/07/owl#>" + "SELECT ?classA ?classB " + "WHERE { " + "?classA rdf:type owl:Class " + "{ " + "?classB rdf:type owl:Class " + "} " + "{ " + "?classA rdfs:subClassOf ?classB " + "} " + "}";
        String queryString3 = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX owl:<http://www.w3.org/2002/07/owl#>" + "SELECT ?class ?property " + "WHERE { " + "{ " + "?property rdf:type owl:FunctionalProperty ." + "} " + "UNION " + "{ " + "?class rdf:type owl:Class ." + "} " + " }";
        String queryString4 = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX owl:<http://www.w3.org/2002/07/owl#>" + "PREFIX ucd: <http://www.srg.ucd.ie/photo_ontologies.owl#>" + "SELECT ?property ?class " + "WHERE { " + "{ " + "?property rdf:type owl:FunctionalProperty . " + "} " + "UNION " + "{ " + "?class owl:Class ucd:Country . " + "} " + "} ";
        String queryStringTest = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "SELECT ?type " + "WHERE { " + "?x rdf:type ?type " + "}";
        Query query = QueryFactory.create(queryString4);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        for (; results.hasNext(); ) {
            QuerySolution solution = results.nextSolution();
            System.out.println("solution: " + solution.toString());
        }
        qe.close();
    }

    public static void main(String[] args) {
        new ProcessOwlFile().run();
    }
}
