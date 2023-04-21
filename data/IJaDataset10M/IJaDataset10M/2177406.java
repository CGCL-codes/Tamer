package org.semtinel.plugins.thencer.owl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.helpers.StatementCollector;
import org.openrdf.rio.rdfxml.RDFXMLParserFactory;
import org.semtinel.core.data.api.CoreManager;
import org.semtinel.core.data.api.SchemeInserter;
import org.semtinel.core.util.ProgressListener;

/**
 * Imports an RDF/OWL XML file into a conceptscheme
 * @author Robert
 */
public class RDFImporter {

    Logger log;

    GraphImpl g;

    /**
     * Initializes the RDFImporter
     */
    public RDFImporter() {
        g = new GraphImpl();
    }

    List<ProgressListener> progressListeners = new ArrayList<ProgressListener>();

    /**
     * Adds a Progresslisterner to RDFImporter
     * @param listener ProgressListener
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

    int count = 0;

    int absCount = 0;

    private void progress(String message, long progress) {
        absCount++;
        if (count++ < 10) {
            return;
        }
        count = 0;
        for (ProgressListener listener : progressListeners) {
            listener.progress("" + absCount + ": " + message, (int) (progress));
        }
    }

    private void start(int max) {
        for (ProgressListener listener : progressListeners) {
            listener.start(max);
        }
    }

    private void finish() {
        for (ProgressListener listener : progressListeners) {
            listener.finish();
        }
    }

    /**
     * Parse the XML file and collects all statments and writes them into the DB
     * @param file Inputfile
     * @param name Name of the ConceptScheme
     * @param baseURI Base URI of the Inputfile
     */
    public void process(String file, String name, String baseURI) {
        log = Logger.getLogger(this.getClass());
        log.debug("Start importing RDF ...");
        StatementCollector handler = new StatementCollector();
        try {
            RDFXMLParserFactory factory = new RDFXMLParserFactory();
            RDFParser parser = factory.getParser();
            parser.setRDFHandler(handler);
            parser.setVerifyData(true);
            parser.setStopAtFirstError(false);
            parser.parse(new InputStreamReader(new FileInputStream(file)), baseURI);
            FileOutputStream fout = new FileOutputStream("statements.txt");
            Writer out = new OutputStreamWriter(fout);
            BufferedWriter buf = new BufferedWriter(out);
            g.addAll(handler.getStatements());
            URIImpl pred_typ = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            URIImpl obj_class = new URIImpl("http://www.w3.org/2002/07/owl#Class");
            URIImpl pred_hasdef = new URIImpl("http://www.geneontology.org/formats/oboInOwl#hasDefinition");
            URIImpl pred_label = new URIImpl("http://www.w3.org/2000/01/rdf-schema#label");
            URIImpl pred_hassyn = new URIImpl("http://www.geneontology.org/formats/oboInOwl#hasRelatedSynonym");
            URIImpl pred_hassub = new URIImpl("http://www.w3.org/2000/01/rdf-schema#subClassOf");
            Iterator<Statement> iter2;
            Iterator<Statement> iter;
            String prefix_class = new String("http://human.owl");
            Hashtable<String, OwlConcept> concepts = new Hashtable<String, OwlConcept>();
            iter = g.match(null, pred_typ, obj_class, (Resource) null);
            while (iter.hasNext()) {
                Statement state = iter.next();
                if (state.getSubject().toString().contains(prefix_class)) {
                    concepts.put(cutPrefix(state.getSubject().toString(), prefix_class), new OwlConcept(state.getSubject().toString()));
                    log.debug("Found concept: " + cutPrefix(state.getSubject().toString(), prefix_class));
                }
            }
            log.debug(concepts.size() + " concepts found.");
            start(concepts.size());
            Integer len = 0;
            for (String s : concepts.keySet()) {
                OwlConcept concept = concepts.get(s);
                len += 1;
                progress("Fetching data: " + concept.getUri().toString(), len);
                iter = g.match(new URIImpl(prefix_class + "/" + s), pred_label, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (concept.getPrefLabel() == null) {
                        concept.setPrefLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("Label found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    } else {
                        concept.addAltLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("AltLabel found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "#" + s), pred_label, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (concept.getPrefLabel() == null) {
                        concept.setPrefLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("Label found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    } else {
                        concept.addAltLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("AltLabel found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "/" + s), pred_hassyn, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                        iter2 = g.match((BNodeImpl) state.getObject(), pred_label, null, (Resource) null);
                        while (iter2.hasNext()) {
                            Statement state2 = iter2.next();
                            concept.addAltLabel(cutType(state2.getObject().toString(), "^^"));
                            log.debug("AltLabel found for Statement " + s + ": " + cutType(state2.getObject().toString(), "^^"));
                        }
                    } else {
                        concept.addAltLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("AltLabel found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "#" + s), pred_hassyn, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                        iter2 = g.match((BNodeImpl) state.getObject(), pred_label, null, (Resource) null);
                        while (iter2.hasNext()) {
                            Statement state2 = iter2.next();
                            concept.addAltLabel(cutType(state2.getObject().toString(), "^^"));
                            log.debug("AltLabel found for Statement " + s + ": " + cutType(state2.getObject().toString(), "^^"));
                        }
                    } else {
                        concept.addAltLabel(cutType(state.getObject().toString(), "^^"));
                        log.debug("AltLabel found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "/" + s), pred_hassub, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                    } else {
                        concept.addParent(cutPrefix(state.getObject().toString(), prefix_class));
                        log.debug("Parent found for Statement " + s + ": " + cutPrefix(state.getObject().toString(), prefix_class));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "#" + s), pred_hassub, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                    } else {
                        concept.addParent(cutPrefix(state.getObject().toString(), prefix_class));
                        log.debug("Parent found for Statement " + s + ": " + cutPrefix(state.getObject().toString(), prefix_class));
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "/" + s), pred_hasdef, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                        iter2 = g.match((BNodeImpl) state.getObject(), pred_label, null, (Resource) null);
                        while (iter2.hasNext()) {
                            Statement state2 = iter2.next();
                            if (concept.getScopeNote() == null) {
                                concept.setScopeNote(cutType(state2.getObject().toString(), "^^"));
                                log.debug("Definition found for Statement " + s + ": " + cutType(state2.getObject().toString(), "^^"));
                            }
                        }
                    } else {
                        if (concept.getScopeNote() == null) {
                            concept.setScopeNote(cutType(state.getObject().toString(), "^^"));
                            log.debug("Definition found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                        }
                    }
                }
                iter = g.match(new URIImpl(prefix_class + "#" + s), pred_hasdef, null, (Resource) null);
                while (iter.hasNext()) {
                    Statement state = iter.next();
                    if (state.getObject().toString().contains("_:node")) {
                        iter2 = g.match((BNodeImpl) state.getObject(), pred_label, null, (Resource) null);
                        while (iter2.hasNext()) {
                            Statement state2 = iter2.next();
                            if (concept.getScopeNote() == null) {
                                concept.setScopeNote(cutType(state2.getObject().toString(), "^^"));
                                log.debug("Definition found for Statement " + s + ": " + cutType(state2.getObject().toString(), "^^"));
                            }
                        }
                    } else {
                        if (concept.getScopeNote() == null) {
                            concept.setScopeNote(cutType(state.getObject().toString(), "^^"));
                            log.debug("Definition found for Statement " + s + ": " + cutType(state.getObject().toString(), "^^"));
                        }
                    }
                }
            }
            CoreManager cm = Lookup.getDefault().lookup(CoreManager.class);
            SchemeInserter schemeInserter = cm.createSchemeInserter("OWL " + name + "(" + new Date() + ")", "en");
            Long schemeID = schemeInserter.getConceptScheme().getTopConcept().getId();
            Map<String, Long> ids = new Hashtable<String, Long>();
            List<String> nonparent = new ArrayList<String>();
            for (String s : concepts.keySet()) {
                len += 1;
                log.debug("Insertion of concept: " + s);
                Long id = schemeInserter.insertConcept(s, concepts.get(s).getPrefLabel(), concepts.get(s).getAltLabels(), concepts.get(s).getHiddenLabels(), concepts.get(s).getScopeNote());
                ids.put(s, id);
                if (concepts.get(s).getParents().isEmpty()) {
                    nonparent.add(s);
                }
            }
            for (String s : concepts.keySet()) {
                len += 1;
                OwlConcept concept = concepts.get(s);
                for (String p : concept.getParents()) {
                    log.debug("Parent: " + ids.get(p) + " Child: " + ids.get(s));
                    schemeInserter.insertParentChild(ids.get(p), ids.get(s));
                }
            }
            for (String s : nonparent) {
                log.debug("Root: " + schemeID + " Child: " + ids.get(s));
                schemeInserter.insertParentChild(schemeID, ids.get(s));
            }
            finish();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (RDFParseException ex) {
            Exceptions.printStackTrace(ex);
        } catch (RDFHandlerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private String cutPrefix(String input, String prefix) {
        String output = "";
        output = input.substring(prefix.length() + 1);
        return output;
    }

    private String cutType(String input, String sep) {
        String output = "";
        if (input.contains(sep)) {
            output = input.substring(0, input.indexOf(sep));
        } else {
            output = input;
        }
        output = output.replace("\"", "");
        return output;
    }
}
