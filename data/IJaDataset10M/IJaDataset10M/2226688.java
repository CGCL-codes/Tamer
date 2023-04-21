package tr.com.srdc.isurf.isu.reasoner;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.protegeowl.DefaultProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import tr.com.srdc.isurf.isu.config.Config;
import tr.com.srdc.isurf.isu.jess.JessFactRepository;
import tr.com.srdc.isurf.isu.ontologyGenerator.Utilities;

/**
 *
 * @author yildiray
 */
public class ReasonerClient {

    private static String ONTOLOGY_URL = "resources/UpperOntology.owl";

    private static String REASONER_URL = "http://localhost:8080";

    private DefaultProtegeOWLReasoner reasoner;

    private Vector<Vector<String>> equalities = new Vector();

    public ReasonerClient(String ontologyURL) {
        try {
            ONTOLOGY_URL = ontologyURL;
            REASONER_URL = Config.getProperty("reasoner.url");
            OWLModel model = ProtegeOWL.createJenaOWLModelFromURI(ONTOLOGY_URL);
            reasoner = new DefaultProtegeOWLReasoner(model);
            reasoner.setURL(REASONER_URL);
            equalities = new Vector();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isBIE(String name) {
        boolean result = true;
        if (name.equals("AggregateBusinessInformationEntity") || name.equals("AssociationBusinessInformationEntity") || name.equals("BasicBusinessInformationEntity") || name.equals("UBL.ABIE") || name.equals("UBL.ASBIE") || name.equals("UBL.BBIE") || name.equals("GS1.ABIE") || name.equals("GS1.ASBIE") || name.equals("GS1.BBIE") || name.equals("Component") || name.equals("ComponentRef") || name.equals("Field") || name.endsWith(".Type")) {
            result = false;
        }
        return result;
    }

    private boolean previouslyInserted(String source, String target) {
        boolean found = false;
        for (int i = 0; i < equalities.size(); i++) {
            Vector pair = equalities.elementAt(i);
            String first = (String) pair.elementAt(0);
            String second = (String) pair.elementAt(0);
            if ((first.equals(source) && second.equals(target)) || (first.equals(target) && second.equals(source))) {
                found = true;
            }
        }
        if (!found) {
            Vector pair = new Vector();
            pair.addElement(source);
            pair.addElement(target);
            equalities.addElement(pair);
        }
        return found;
    }

    public Hashtable<String, Hashtable<String, Double>[]> classifyTaxonomy(String filterString, String messageType) {
        Hashtable<String, Hashtable<String, Double>[]> result = new Hashtable();
        try {
            long t0 = System.currentTimeMillis();
            reasoner.classifyTaxonomy(null);
            long t1 = System.currentTimeMillis();
            System.out.println("\n\n\n $$$ Reasoner Time:" + (t1 - t0) + " msec...\n\n\n");
            OWLModel model = reasoner.getKnowledgeBase();
            Collection<OWLNamedClass> collection = model.getChangedInferredClasses();
            OWLNamedClass namedClass = null;
            JessFactRepository jessFactRepository = JessFactRepository.getInstance();
            jessFactRepository.initializeJessRules(Config.getProperty("output.path.for.rules") + messageType + "-Intermediate.clp");
            for (Iterator<OWLNamedClass> i = collection.iterator(); i.hasNext(); ) {
                namedClass = i.next();
                String name = namedClass.getLocalName();
                if (!name.startsWith(filterString)) {
                    continue;
                }
                if (!isBIE(name)) {
                    continue;
                }
                int index = name.indexOf("_");
                String prefix = null;
                if (index != -1) {
                    prefix = name.substring(0, index);
                }
                boolean printed = false;
                Collection<RDFSClass> inferredEquivalentClasses = namedClass.getInferredEquivalentClasses();
                Collection<RDFSClass> inferredSubClasses = namedClass.getInferredSubclasses();
                Collection<RDFSClass> inferredSuperClasses = namedClass.getInferredSuperclasses();
                RDFSClass temp = null;
                Hashtable<String, Double>[] array = new Hashtable[3];
                Hashtable<String, Double> tempHashtable = new Hashtable();
                for (Iterator<RDFSClass> j = inferredEquivalentClasses.iterator(); j.hasNext(); ) {
                    temp = j.next();
                    String tempName = temp.getLocalName();
                    if (!isBIE(tempName)) {
                        continue;
                    }
                    if (prefix != null && !tempName.startsWith(prefix)) {
                        if (!previouslyInserted(name, tempName)) {
                            jessFactRepository.notifyEqualsTo(name, tempName);
                        }
                        if (!printed) {
                            printed = true;
                        }
                        tempHashtable.put(tempName, Utilities.getSimilarity(name, tempName));
                    }
                }
                array[0] = tempHashtable;
                tempHashtable = new Hashtable();
                for (Iterator<RDFSClass> j = inferredSubClasses.iterator(); j.hasNext(); ) {
                    temp = j.next();
                    String tempName = temp.getLocalName();
                    if (!isBIE(tempName)) {
                        continue;
                    }
                    if (prefix != null && !tempName.startsWith(prefix)) {
                        jessFactRepository.notifySubClassOf(tempName, name);
                        if (!printed) {
                            printed = true;
                        }
                        tempHashtable.put(tempName, Utilities.getSimilarity(name, tempName));
                    }
                }
                array[1] = tempHashtable;
                tempHashtable = new Hashtable();
                for (Iterator<RDFSClass> j = inferredSuperClasses.iterator(); j.hasNext(); ) {
                    temp = j.next();
                    String tempName = temp.getLocalName();
                    if (!isBIE(tempName)) {
                        continue;
                    }
                    if (prefix != null && !tempName.startsWith(prefix)) {
                        jessFactRepository.notifySubClassOf(name, tempName);
                        if (!printed) {
                            printed = true;
                        }
                        tempHashtable.put(tempName, Utilities.getSimilarity(name, tempName));
                    }
                }
                array[2] = tempHashtable;
                result.put(name, array);
            }
            jessFactRepository.extractRepositoryToFile(Config.getProperty("output.path.for.rules") + messageType + ".clp", false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void main(String argv[]) {
        ReasonerClient rc = new ReasonerClient(argv[0]);
        rc.classifyTaxonomy(null, null);
    }
}
