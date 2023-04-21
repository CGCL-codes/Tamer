package org.semtinel.plugins.thencer.datacollector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.semtinel.core.data.api.Annotation;
import org.semtinel.core.data.api.AnnotationSet;
import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.util.ProgressListener;
import org.semtinel.core.pubmed.PubmedSFS;

/**
 * This class enriches an existing annotationset with new annotations for every concept in a conceptscheme where
 * the number of existing annotations for one concept is less then 10
 * @author Robert
 *
 */
public class AnnotationEnricher {

    private ConceptScheme cs;

    private String name;

    private AnnotationSet as;

    private Stack<Concept> concepts;

    private Concept curcon;

    private Boolean ignore;

    private Integer num;

    private List<Annotation> annotations;

    private String year;

    private List<Long> hl;

    Logger log;

    PubmedSFS pub;

    private List<String> ignoreList;

    /**
     * Creates a new instanz of the class
     * @param name Name of the new RecordSource/Set/AnnotationSource/Set
     * @param cs ConceptScheme (Thesaurus)
     * @param as Existing AnnotationSet
     * @param ignore Ignore Top Concept
     * @param year Year the new data should be out of
     * @param ignoreConcept List of concepts which the enricher should ignore
     */
    public AnnotationEnricher(String name, ConceptScheme cs, AnnotationSet as, Boolean ignore, String year, String ignoreConcept) {
        this.cs = cs;
        this.name = name;
        this.as = as;
        this.ignore = ignore;
        this.year = year;
        this.ignoreList = new LinkedList<String>();
        log = Logger.getLogger(this.getClass());
        log.debug("AnnotationEnricher initialised: " + this.name + ", " + this.year);
        if (ignoreConcept.length() > 0) {
            String[] ignoreArray;
            ignoreArray = ignoreConcept.split(";");
            for (int i = 0; i < ignoreArray.length; i++) {
                String tmp = (String) ignoreArray[i].trim();
                this.ignoreList.add(tmp);
                log.debug("Ignoring concept: " + tmp);
            }
        }
        this.hl = new LinkedList<Long>();
        pub = new PubmedSFS("20", "", this.year, this.year, this.name);
    }

    /**
     * starts the fetching and storing of the new data
     * @return returns number of concepts which had less than 10 annotations
     */
    public void process() {
        start();
        Integer count = 0;
        log.debug("Starting ...");
        concepts = new Stack<Concept>();
        curcon = cs.getTopConcept();
        hl.add(curcon.getId());
        if (ignore) {
            log.debug("Ignoring top concept ...");
            for (Concept c : curcon.getNarrower()) {
                if (!(hl.contains(c.getId()))) {
                    log.debug("Adding concept " + c.getPrefLabel().getText());
                    concepts.push(c);
                    hl.add(c.getId());
                }
            }
        } else {
            log.debug("Adding top concept ...");
            concepts.push(curcon);
        }
        num = 1;
        while (!(concepts.empty())) {
            curcon = concepts.pop();
            if (!(ignoreList.contains(curcon.getPrefLabel().getText()))) {
                log.debug("Scanning concept " + curcon.getPrefLabel().getText());
                progress("Searching annotations for " + curcon.getPrefLabel().getText());
                if (!(as == null)) {
                    annotations = curcon.getAnnotations(as);
                }
                if ((as == null) || (annotations.size() < 10)) {
                    progress("Fetching new annotations from pubmed for " + curcon.getPrefLabel().getText());
                    try {
                        pub.search(curcon.getPrefLabel().getText());
                    } catch (Exception ex) {
                        log.debug("Error while searching ... skipping");
                    }
                    count++;
                }
                for (Concept c : curcon.getNarrower()) {
                    if (!(hl.contains(c.getId()))) {
                        log.debug("Adding concept " + c.getPrefLabel().getText() + ".");
                        concepts.push(c);
                        hl.add(c.getId());
                    }
                }
                log.debug("Actual idArray size: " + pub.getIdArray().size());
                if (pub.getIdArray().size() > 50) {
                    progress("Fetching data from pubmed ...");
                    pub.fetch(10);
                }
                log.debug("Actual recordList size: " + pub.getRecordList().size());
                if (pub.getRecordList().size() > 5000) {
                    progress("Storing data to db ...");
                    pub.store(cs, name + " " + num);
                    num++;
                }
            }
            log.debug("List contains " + hl.size() + " items.");
        }
        pub.fetch(10);
        pub.store(cs, name + " " + num);
        log.debug(count + " concepts had less than 10 annotations.");
        finish();
    }

    List<ProgressListener> progressListeners = new ArrayList<ProgressListener>();

    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

    int count = 0;

    int absCount = 0;

    private void progress(String message) {
        absCount++;
        if (count++ < 10) {
            return;
        }
        count = 0;
        for (ProgressListener listener : progressListeners) {
            listener.progress("" + absCount + ": " + message, -1);
        }
    }

    private void start() {
        for (ProgressListener listener : progressListeners) {
            listener.start(-1);
        }
    }

    private void finish() {
        for (ProgressListener listener : progressListeners) {
            listener.finish();
        }
    }
}
