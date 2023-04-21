package org.s3b.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.s3b.index.index.RDF2GoLoader;
import org.s3b.index.index.ResourceIndexer;
import org.s3b.index.index.SparqlQuery;
import org.s3b.search.api.SemanticQueryExpansionDelegate;
import org.s3b.search.resource.MidResource;

/**
 * 
 * @author Lukasz Porwol
 *
 */
public class Test extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public Test() {
        super();
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        SemanticQueryExpansionDelegate sqed = new SemanticQueryExpansionDelegate();
        PrintWriter writer;
        String query = "Web browsing data";
        String type = "index";
        Model md = RDF2GoLoader.getInstance().getModel();
        md.open();
        URI cos = new URIImpl("http://localhost:8080/jeromedl/resource/ZE0r2Zz7");
        ClosableIterator<Statement> rows3 = md.findStatements(cos, null, null);
        while (rows3.hasNext()) {
            md.removeStatement(rows3.next());
        }
        md.addStatement(cos, md.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), md.createURI("http://www.jeromedl.org/structure#Book"));
        md.addStatement(cos, md.createURI("http://www.marcont.org/ontology#hasDomain"), "data");
        md.addStatement(cos, md.createURI("http://www.marcont.org/ontology#hasKeyword"), "limousine");
        md.addStatement(cos, md.createURI("http://www.jeromedl.org/structure#abstract"), "limousine");
        ClosableIterator<Statement> rows2 = md.findStatements(null, null, null);
        while (rows2.hasNext()) {
            System.out.println(rows2.next());
        }
        rows2.close();
        md.dump();
        md.commit();
        md.close();
        if (type.equals("index")) {
            ResourceIndexer ri = ResourceIndexer.getInstance();
            URI uri = new URIImpl("http://localhost:8080/jeromedl/resource/ZTTr2Zz6");
            String label = "nowy doc1";
            Map<String, String[]> others = new HashMap<String, String[]>();
            String[] tab = { " kot ma ale nose lol car limousine magic" };
            others.put("abstract", tab);
            String content = uri.toString() + label + tab;
            ri.addToIndex(uri, label, content, others);
            uri = new URIImpl("http://localhost:8080/jeromedl/resource/ZTTr2Zz8");
            label = "nowy doc2";
            others = new HashMap<String, String[]>();
            String[] tab21 = { " kot ma ale nose lol limousine magic" };
            others.put("abstract", tab21);
            String[] tab22 = { "limousine" };
            others.put("keywords", tab22);
            String[] tab23 = { "Adam Gzella" };
            others.put("authors", tab23);
            String[] tab24 = { "limousine" };
            others.put("domains", tab24);
            content = uri.toString();
            ri.addToIndex(uri, label, content, others);
            uri = new URIImpl("http://localhost:8080/jeromedl/resource/ZTTr2Zz9");
            label = "nowy doc2";
            others = new HashMap<String, String[]>();
            String[] tab31 = { " kot ma ale nose lol brougham magic" };
            others.put("abstract", tab31);
            String[] tab32 = { "brougham" };
            others.put("keywords", tab32);
            String[] tab33 = { "Adam Gzella" };
            others.put("authors", tab33);
            String[] tab34 = { "limousine" };
            others.put("domains", tab34);
            content = uri.toString();
            ri.addToIndex(uri, label, content, others);
            uri = new URIImpl("http://localhost:8080/jeromedl/resource/ZTTr2Zx1");
            label = "nowy doc2";
            others = new HashMap<String, String[]>();
            String[] tab41 = { " kot ma ale nose lol sedan magic" };
            others.put("abstract", tab41);
            String[] tab42 = { "sedan" };
            others.put("keywords", tab42);
            String[] tab43 = { "Adam Gzella" };
            others.put("authors", tab43);
            String[] tab44 = { "limousine" };
            others.put("domains", tab44);
            content = uri.toString();
            ri.addToIndex(uri, label, content, others);
        }
        MidResource mresource = null;
        if (type.equals("sqe")) {
            Map<URI, Double> result = sqed.doSemanticSearch("mailto:admin@foafrealm.org", query, false);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }
}
