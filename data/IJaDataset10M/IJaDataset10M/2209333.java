package gr.konstant.powder.sempp.kb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.kb.jena.JenaOWL;
import gr.konstant.transonto.kb.jena.Vocabulary;
import gr.konstant.powder.sempp.SemPP;

public class PowderOWL extends JenaOWL {

    public PowderOWL(URL powderLocation) throws BackendException, UnsupportedFeatureException {
        super(uriCreate(powderLocation));
        this.jenaModel = new RegexRDFExt(powderLocation);
    }

    public PowderOWL(String powderFilePathname) throws BackendException, UnsupportedFeatureException {
        super(uriCreate(powderFilePathname));
        this.jenaModel = new RegexRDFExt(powderFilePathname);
    }

    @Override
    protected RegexRDFExt getJenaModel() {
        Model m = super.getJenaModel();
        assert m instanceof RegexRDFExt;
        return (RegexRDFExt) m;
    }

    public void add(PowderOWL more) {
        getJenaModel().add(more.getJenaModel());
    }

    private Model mkAnswerModel() {
        return null;
    }

    private boolean assertResults(ResultSet results, Resource ansSubj, Model ans) {
        boolean described = false;
        while (results.hasNext()) {
            QuerySolution binding = results.nextSolution();
            Property p = ans.createProperty(((Resource) binding.get("p")).getURI());
            if (p.getNameSpace().compareTo(RegexRDFExt.nsWDRS) == 0) {
                if (p.getLocalName().compareTo("tag") == 0) {
                    RDFNode o = binding.get("o").inModel(ans);
                    ansSubj.addProperty(p, o);
                    described = true;
                }
            } else if (p.getNameSpace().compareTo(Vocabulary.NS_RDF) == 0) {
                if (p.getLocalName().compareTo("type") == 0) {
                    Resource o = ans.createResource(((Resource) binding.get("o")).getURI());
                    if (o.getNameSpace() != null && (o.getNameSpace().compareTo(Vocabulary.NS_RDF) != 0) && (o.getNameSpace().compareTo(Vocabulary.NS_RDFS) != 0) && (o.getNameSpace().compareTo(Vocabulary.NS_OWL) != 0) && (o.getNameSpace().compareTo(getJenaModel().nsThisDoc) != 0)) {
                        Statement stmt = ans.createStatement(ansSubj, p, o);
                        ans.add(stmt);
                        described = true;
                    }
                }
            } else if (p.getNameSpace().compareTo(Vocabulary.NS_RDFS) == 0) {
            } else if (p.getNameSpace().compareTo(Vocabulary.NS_OWL) == 0) {
            } else {
                Statement stmt = ans.createStatement(ansSubj, p, binding.get("o"));
                ans.add(stmt);
                described = true;
            }
        }
        return described;
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a single URI.
	 * @param String the URI to be described
	 * @return Model RDF description of the URI 
	 */
    public Model describe(String url, boolean attribution) {
        return describe(Collections.singleton(url), attribution);
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a set of URIs.
	 * @param Set<String> the URIs to be described
	 * @param boolean if true, include in the answer the information in the attribution block of the POWDER documents used to build this model.
	 * @return Model RDF description of the URIs 
	 */
    public Model describe(Set<String> urls, boolean attribution) {
        com.hp.hpl.jena.rdf.model.RDFNode nullnode = null;
        RegexRDFExt m = new RegexRDFExt();
        m.add(getJenaModel());
        OntClass thing = m.createClass(Vocabulary.NS_OWL + "Thing");
        Set<Individual> subjects = new HashSet<Individual>(urls.size());
        Iterator<String> i = urls.iterator();
        while (i.hasNext()) {
            String u = i.next();
            Individual hereSubj = m.createIndividual(u, thing);
            subjects.add(hereSubj);
        }
        m.assertMatchesRegEx();
        if (SemPP.debug) {
            m.writeAll(System.err);
        }
        Model ans = ModelFactory.createDefaultModel();
        ans.setNsPrefixes(getJenaModel().getNsPrefixMap());
        ans.setNsPrefix("tron", "http://konstant.gr/transonto/apps#");
        Property ansRdftype = ans.createProperty(Vocabulary.NS_RDF + "type");
        Resource ansOnto = ans.createResource(Vocabulary.NS_OWL + "Ontology");
        Property ansIssuedby = ans.createProperty(RegexRDFExt.nsWDRS + "issuedby");
        Resource me = ans.createResource("http://konstant.gr/transonto/apps#sempp");
        Resource pp = ans.createResource(RegexRDFExt.nsWDRS + "Processor");
        {
            Statement stmt = ans.createStatement(me, ansRdftype, pp);
            ans.add(stmt);
        }
        {
            Property ansProp = ans.createProperty("http://konstant.gr/transonto/apps#version");
            RDFNode ansObj = ans.createLiteral("0.4");
            Statement stmt = ans.createStatement(me, ansProp, ansObj);
            ans.add(stmt);
        }
        {
            Property ansProp = ans.createProperty(Vocabulary.NS_RDFS + "seeAlso");
            Resource ansObj = ans.createResource("http://konstant.gr/transonto");
            Statement stmt = ans.createStatement(me, ansProp, ansObj);
            ans.add(stmt);
        }
        if (attribution) {
            StmtIterator stmts = getJenaModel().listStatements(null, ansIssuedby, nullnode);
            Property ansUsing = ans.createProperty("http://konstant.gr/transonto/apps#using");
            while (stmts.hasNext()) {
                Statement stmt = stmts.nextStatement();
                Resource hereDoc = stmt.getSubject();
                Resource ansDoc = ans.createResource(hereDoc.getURI());
                ansDoc.addProperty(ansRdftype, ansOnto);
                StmtIterator stmts2 = hereDoc.listProperties();
                int numIssuedBy = 0;
                while (stmts2.hasNext()) {
                    Statement stmt2 = stmts2.nextStatement();
                    String pName = stmt2.getPredicate().getURI();
                    Property p = ans.createProperty(pName);
                    if (pName.compareTo(RegexRDFExt.nsWDRS + "issuedby") == 0) {
                        ++numIssuedBy;
                    }
                    if (p.getNameSpace().compareTo(RegexRDFExt.nsWDRS) == 0) {
                        RDFNode o = stmt2.getObject().inModel(ans);
                        Statement ansStmt = ans.createStatement(ansDoc, p, o);
                        ans.add(ansStmt);
                    } else if (p.getNameSpace().compareTo(Vocabulary.NS_RDFS) == 0) {
                        if (p.getLocalName().compareTo("seeAlso") == 0) {
                            RDFNode o = stmt2.getObject().inModel(ans);
                            Statement ansStmt = ans.createStatement(ansDoc, p, o);
                            ans.add(ansStmt);
                        }
                    }
                }
                assert numIssuedBy == 1;
                Statement ansAttrStmt = ans.createStatement(me, ansUsing, ansDoc);
                ans.add(ansAttrStmt);
            }
        }
        Iterator<Individual> hereSubjs = subjects.iterator();
        Property ymRE = m.createProperty(RegexRDFExt.nsWDRS + "matchesregex");
        Property nmRE = m.createProperty(RegexRDFExt.nsWDRS + "notmatchesregex");
        while (hereSubjs.hasNext()) {
            m.rebind();
            Resource hereSubj = (Resource) hereSubjs.next();
            Resource ansSubj = (Resource) hereSubj.inModel(ans).as(Resource.class);
            String subj = ansSubj.getURI();
            boolean described = false;
            StmtIterator matchStmts = m.listStatements(hereSubj, ymRE, nullnode);
            HashSet<String> yesRE = new HashSet<String>();
            while (matchStmts.hasNext()) {
                String re = matchStmts.nextStatement().getLiteral().getString();
                yesRE.add(re);
                if (SemPP.debug) {
                    System.err.println(subj + " matchesRE " + re);
                }
            }
            matchStmts = m.listStatements(hereSubj, nmRE, nullnode);
            while (matchStmts.hasNext()) {
                String re = matchStmts.nextStatement().getLiteral().getString();
                if (yesRE.contains(re)) {
                    ans = ModelFactory.createDefaultModel();
                    ans.setNsPrefixes(getJenaModel().getNsPrefixMap());
                    ans.setNsPrefix("tron", "http://konstant.gr/transonto/apps#");
                    ans.setNsPrefix("wdrs", RegexRDFExt.nsWDRS);
                    Property errorcode = ans.createProperty(RegexRDFExt.nsWDRS + "err_code");
                    Property errormsg = ans.createProperty(RegexRDFExt.nsWDRS + "data_error");
                    StmtIterator stmts = getJenaModel().listStatements(null, ansIssuedby, nullnode);
                    while (stmts.hasNext()) {
                        Statement stmt = stmts.nextStatement();
                        Resource hereDoc = stmt.getSubject();
                        Resource ansDoc = (Resource) hereDoc.inModel(ans).as(Resource.class);
                        stmt = ans.createLiteralStatement(ansDoc, errorcode, 101);
                        ans.add(stmt);
                        stmt = ans.createLiteralStatement(ansDoc, errormsg, "Description Resource refers to a descriptor set that is out of scope");
                        ans.add(stmt);
                    }
                    return ans;
                }
            }
            String queryStr = "SELECT ?p ?o WHERE { <" + subj + "> ?p ?o }";
            Query query = QueryFactory.create(queryStr);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            ResultSet results = qexec.execSelect();
            described = assertResults(results, ansSubj, ans) || described;
            queryStr = "SELECT ?ds ?p ?o " + "WHERE { <" + subj + "> <" + Vocabulary.NS_RDF + "type> ?ds . " + "?ds ?p ?o . " + "} ";
            query = QueryFactory.create(queryStr);
            qexec = QueryExecutionFactory.create(query, m);
            results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution binding = results.nextSolution();
                Property p = ans.createProperty(((Resource) binding.get("p")).getURI());
                Statement stmt = ans.createStatement(ansSubj, p, binding.get("o"));
                if (p.getNameSpace().compareTo(Vocabulary.NS_RDFS) == 0) {
                    if ((p.getLocalName().compareTo("seeAlso") == 0) || (p.getLocalName().compareTo("comment") == 0) || (p.getLocalName().compareTo("label") == 0)) {
                        ans.add(stmt);
                        described = true;
                    }
                } else if ((p.getNameSpace().compareTo(Vocabulary.NS_RDF) == 0) || (p.getNameSpace().compareTo(Vocabulary.NS_OWL) == 0)) {
                } else {
                    ans.add(stmt);
                    described = true;
                }
            }
            Property p;
            Statement stmt;
            if (described) {
                StmtIterator stmts = getJenaModel().listStatements(null, ansIssuedby, nullnode);
                stmts.next();
                if (!stmts.hasNext()) {
                    Resource ansPowderDoc = ans.createResource(getJenaModel().baseThisDoc);
                    p = ans.createProperty(Vocabulary.NS_RDF + "type");
                    Resource ansPowderDocType = ans.createResource(RegexRDFExt.nsWDRS + "Document");
                    stmt = ans.createStatement(ansPowderDoc, p, ansPowderDocType);
                    ans.add(stmt);
                    p = ans.createProperty(RegexRDFExt.nsWDRS + "describedby");
                    stmt = ans.createStatement(ansSubj, p, ansPowderDoc);
                    ans.add(stmt);
                }
                if (attribution) {
                    p = ans.createProperty("http://konstant.gr/transonto/apps#describedby");
                    stmt = ans.createStatement(ansSubj, p, me);
                    ans.add(stmt);
                }
            } else {
                p = ans.createProperty(RegexRDFExt.nsWDRS + "notknownto");
                stmt = ans.createStatement(ansSubj, p, me);
                ans.add(stmt);
            }
        }
        m.close();
        return ans;
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a URI.
	 * @param String the URI to be described
	 * @param boolean if true, include in the answer the information in the attribution block of the POWDER documents used to build this model.
	 * @param String write the answer to this file
	 * @return Model RDF description of the URI 
	 */
    public Model describe(String url, boolean attribution, String outFileName) throws BackendException {
        return describe(Collections.singleton(url), outFileName);
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a set of URIs.
	 * @param Set<String> the URIs to be described
	 * @param boolean if true, include in the answer the information in the attribution block of the POWDER documents used to build this model.
	 * @param String write the answer to this file
	 * @return Model RDF description of the URIs 
	 */
    public Model describe(Set<String> urls, boolean attribution, String outFileName) throws BackendException {
        Model ans = describe(urls, attribution);
        if (outFileName != null) {
            File baseFile = new File(outFileName);
            try {
                baseFile.createNewFile();
            } catch (IOException ex) {
                throw new BackendException(ex);
            }
            OutputStream os;
            try {
                os = new FileOutputStream(baseFile);
            } catch (FileNotFoundException ex) {
                throw new BackendException(ex);
            }
            RDFWriter rdfWriter = ans.getWriter("RDF/XML-ABBREV");
            rdfWriter.setProperty("attributeQuoteChar", "\"");
            rdfWriter.setProperty("tab", "3");
            rdfWriter.setProperty("relativeURIs", "same-document");
            rdfWriter.write(ans, os, "");
        }
        return ans;
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a URI. Does not include attribution information.
	 * @param String the URI to be described
	 * @return Model RDF description of the URI 
	 */
    public Model describe(String url) {
        return describe(url, false);
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a set of URIs. Does not include attribution information.
	 * @param Set<String> the URIs to be described
	 * @return Model RDF description of the URIs 
	 */
    public Model describe(Set<String> urls) {
        return describe(urls, false);
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a URI. Does not include attribution information.
	 * @param String the URI to be described
	 * @param String write the answer to this file
	 * @return Model RDF description of the URI 
	 */
    public Model describe(String url, String outFileName) throws BackendException {
        return describe(url, false, outFileName);
    }

    /**
	 * The basic POWDER Processor functionality: given this model,
	 * describe a set of URIs. Does not include attribution information.
	 * @param Set<String> the URIs to be described
	 * @param String write the answer to this file
	 * @return Model RDF description of the URIs 
	 */
    public Model describe(Set<String> urls, String outFileName) throws BackendException {
        return describe(urls, false, outFileName);
    }
}
