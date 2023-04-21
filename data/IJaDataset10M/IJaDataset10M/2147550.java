package cs6330.p1.template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import relex.entity.DateEntityInfo;
import relex.entity.EntityInfo;
import relex.entity.LocationEntityInfo;
import relex.entity.MoneyEntityInfo;
import relex.entity.OrganizationEntityInfo;
import relex.entity.PersonEntityInfo;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

/**
 * This was mostly copied from the RelEx library to implement the minimum functionality we needed.
 * More info at: http://www.opencog.org/wiki/RelEx
 * 
 *  @author Andrew Bernard
 */
public class RelexEntityManager {

    private Corpus corpus = null;

    private Document doc = null;

    private ArrayList<EntityInfo> orderedEntityInfos;

    private SerialAnalyserController annieController;

    private static final String[] PR_NAMES = { "gate.creole.annotdelete.AnnotationDeletePR", "gate.creole.tokeniser.DefaultTokeniser", "gate.creole.gazetteer.DefaultGazetteer", "gate.creole.ANNIETransducer", "gate.creole.orthomatcher.OrthoMatcher" };

    protected RelexEntityManager() {
        orderedEntityInfos = new ArrayList<EntityInfo>();
    }

    /**
	 * Initialize the ANNIE system. This creates a "corpus pipeline"
	 * application that can be used to run sets of documents through
	 * the extraction system.
	 */
    protected void initAnnie() throws GateException {
        annieController = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController", Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE_" + Gate.genSym());
        for (int i = 0; i < PR_NAMES.length; i++) {
            FeatureMap params = Factory.newFeatureMap();
            ProcessingResource pr = (ProcessingResource) Factory.createResource(PR_NAMES[i], params);
            if (pr instanceof gate.creole.tokeniser.DefaultTokeniser) {
                System.out.println("Changing parameter of " + pr.getClass().getName());
            }
            annieController.add(pr);
        }
    }

    @SuppressWarnings("unchecked")
    protected AnnotationSet getAnnotations(String documentText) throws GateException {
        String fixed = documentText.replaceAll("Don't", "Do not").replaceAll("don't", "do not");
        doc = Factory.newDocument(fixed);
        corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
        corpus.add(doc);
        annieController.setCorpus(corpus);
        annieController.execute();
        return doc.getAnnotations();
    }

    /**
	 * Add the entity info to the list, inserting it in sorted order.
	 */
    protected void addEntity(EntityInfo ei) {
        int open = 0;
        int start = ei.getFirstCharIndex();
        int end = ei.getLastCharIndex();
        for (EntityInfo e : orderedEntityInfos) {
            int beg = e.getFirstCharIndex();
            if ((open <= start) && (end <= beg)) {
                int idx = orderedEntityInfos.indexOf(e);
                orderedEntityInfos.add(idx, ei);
                return;
            }
            open = e.getLastCharIndex();
            if (start < open) return;
        }
        orderedEntityInfos.add(ei);
    }

    protected List<EntityInfo> getEntityList(AnnotationSet annoset, String sentence) {
        ArrayList<EntityInfo> eInfos = new ArrayList<EntityInfo>();
        for (Iterator<Annotation> it = annoset.iterator(); it.hasNext(); ) {
            Annotation a = it.next();
            int start = a.getStartNode().getOffset().intValue();
            int end = a.getEndNode().getOffset().intValue();
            EntityInfo ei = null;
            String atype = a.getType();
            if (a.getType().equals(ANNIEConstants.PERSON_ANNOTATION_TYPE)) {
                String gender = (String) a.getFeatures().get(ANNIEConstants.PERSON_GENDER_FEATURE_NAME);
                PersonEntityInfo pei = new PersonEntityInfo(sentence, start, end);
                pei.setGender(gender);
                ei = pei;
            } else if (atype.equals(ANNIEConstants.ORGANIZATION_ANNOTATION_TYPE)) {
                ei = new OrganizationEntityInfo(sentence, start, end);
            } else if (atype.equals(ANNIEConstants.LOCATION_ANNOTATION_TYPE)) {
                ei = new LocationEntityInfo(sentence, start, end);
            } else if (atype.equals(ANNIEConstants.MONEY_ANNOTATION_TYPE)) {
                ei = new MoneyEntityInfo(sentence, start, end);
            } else if (atype.equals(ANNIEConstants.DATE_ANNOTATION_TYPE)) {
                ei = new DateEntityInfo(sentence, start, end);
            }
            if (ei != null) {
                for (int i = 0; i < eInfos.size(); i++) {
                    if (eInfos.get(i).getFirstCharIndex() > ei.getFirstCharIndex()) {
                        eInfos.add(i, ei);
                        ei = null;
                        break;
                    }
                }
                if (ei != null) eInfos.add(ei);
            }
        }
        return eInfos;
    }
}
