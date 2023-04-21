package org.tripcom.query.util.loadKernelWithData;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.sail.memory.MemoryStore;
import org.tripcom.api.ws.client.RDFWrapper;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.Template;
import org.tripcom.integration.exception.TSAPIException;

public class LoadDCSchema {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        String space = "tsc://localhost:8080/profiumroot";
        String result;
        String query = "CONSTRUCT {?s ?p ?o } WHERE { ?s ?p ?o }";
        SpaceURI spaceURI = new SpaceURI(space);
        TSClient tsClient = null;
        try {
            tsClient = new TSClient("http", InetAddress.getByName(host), port);
        } catch (UnknownHostException e3) {
            e3.printStackTrace();
        }
        Repository repository = new SailRepository(new MemoryStore());
        RepositoryConnection con = null;
        GraphQueryResult graphResult = null;
        try {
            repository.initialize();
            try {
                con = repository.getConnection();
                RDFXMLWriter rdfWriter = new RDFXMLWriter(System.out);
                URL url = new URL("http://dublincore.org/2008/01/14/dcterms.rdf");
                con.add(url, null, RDFFormat.RDFXML);
                graphResult = con.prepareGraphQuery(QueryLanguage.SPARQL, "CONSTRUCT {?s ?p ?o } WHERE { ?s ?p ?o }").evaluate();
                while (graphResult.hasNext()) {
                    Statement st = graphResult.next();
                    try {
                        tsClient.out(st, spaceURI);
                    } catch (TSAPIException e2) {
                        e2.printStackTrace();
                    }
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            result = RDFWrapper.getRDFXMLStringFromSetofStatements(tsClient.rdmultiple(new Template(query), spaceURI, 10000));
            System.out.println(result);
        } catch (TSAPIException e) {
            e.printStackTrace();
        }
    }
}
