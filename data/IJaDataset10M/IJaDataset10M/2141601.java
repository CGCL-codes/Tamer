package vmp.gate.tbl.utils;

import vmp.gate.tbl.control.AnnotationManager;
import vmp.gate.tbl.entity.Rule;
import vmp.gate.tbl.entity.Template;
import vmp.gate.tbl.entity.Predicate;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Resource;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gnu.trove.TIntIntHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import vmp.gate.tbl.DocumentConverter;
import vmp.gate.tbl.control.IndexedDocumentAnnotationManager;

/**
 *
 * @author jesus
 */
public class SetUpHelper {

    private static SetUpHelper setUpHelper = null;

    private Map<Predicate, Set<Rule>> rules;

    private List<Template> templatesList;

    private Document doc;

    private AnnotationManager<TIntIntHashMap> annotationManager;

    private List<TIntIntHashMap> vicinity;

    /** Creates a new instance of SetUpHelper */
    public SetUpHelper() {
        try {
            Gate.init();
        } catch (GateException ex) {
            System.out.println("Error on initializing gate " + ex);
        }
        generateListOfRules();
        generateTemplatesList();
        generateAnnotationManager();
        generateVicinity();
    }

    public static SetUpHelper getInstance() {
        if (setUpHelper == null) setUpHelper = new SetUpHelper();
        return setUpHelper;
    }

    private void generateListOfRules() {
        Rule r;
        rules = new HashMap();
        try {
            r = new Rule("RULE: chunk_0=B-ORG pos_-1=DA => chunk=B");
            Set<Rule> rs = new HashSet();
            rs.add(r);
            rules.put(r.getPredicate(), rs);
            r = new Rule("RULE: chunk_0=B-ORG pos_-1=DA => chunk=I");
            rs.add(r);
            r = new Rule("RULE: chunk_0=B-ORG pos_-1=DA => chunk=O");
            rs.add(r);
            r = new Rule("RULE: chunk_1=B-ORG pos_0=NP => chunk=B");
            rs = new HashSet();
            rs.add(r);
            rules.put(r.getPredicate(), rs);
            r = new Rule("RULE: chunk_1=B-ORG pos_0=NP => chunk=I");
            rs.add(r);
            r = new Rule("RULE: chunk_1=B-ORG pos_0=NP => chunk=O");
            rs.add(r);
            r = new Rule("RULE: chunk_1=B-ORG pos_0=NP => chunk=B-LOC");
            rs.add(r);
        } catch (GateException ex) {
            ex.printStackTrace();
        }
    }

    private void generateVicinity() {
        Iterator<TIntIntHashMap> amIt = annotationManager.iterator();
        if (amIt.hasNext()) amIt.next();
        if (amIt.hasNext()) amIt.next();
        vicinity = annotationManager.vicinity();
    }

    private void generateDocument() {
        FeatureMap params = Factory.newFeatureMap();
        String home = System.getProperty("basedir");
        System.out.println(home);
        params.put("sourceUrl", "file:/" + home + "/resources/test/SimpleTestFile.txt");
        params.put("encoding", "utf8");
        params.put("format", "word pos gaze ort chunk tchunk");
        Resource res;
        DocumentConverter docConverter;
        try {
            res = Factory.createResource("vmp.gate.tbl.DocumentConverter", params);
            docConverter = (DocumentConverter) res;
            doc = (Document) gate.Gate.getCreoleRegister().getLrInstances("gate.corpora.DocumentImpl").get(0);
        } catch (ResourceInstantiationException ex) {
            ex.printStackTrace();
        }
    }

    private void generateAnnotationManager() {
        generateDocument();
        annotationManager = new IndexedDocumentAnnotationManager(doc);
        annotationManager.setSizeOfVicinity(1);
    }

    private void generateTemplatesList() {
        templatesList = new ArrayList();
        templatesList.add(new Template("chunk_0 pos_-1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk")));
        templatesList.add(new Template("chunk_1 pos_0 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk")));
        templatesList.add(new Template("chunk_0 chunk_-1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk")));
    }

    public Map<Predicate, Set<Rule>> getRules() {
        return rules;
    }

    public List<Template> getTemplatesList() {
        return templatesList;
    }

    public Document getDoc() {
        return doc;
    }

    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    public List<TIntIntHashMap> getVicinity() {
        return vicinity;
    }

    private void initializeGate() throws GateException {
        Gate.init();
    }
}
