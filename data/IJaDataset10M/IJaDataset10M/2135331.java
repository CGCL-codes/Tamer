package n3_project.helpers;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.ObjectFilter;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseConfiguration.AssertBehaviour;
import org.drools.RuleBaseConfiguration.SequentialAgenda;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/** (with hard-coded file names, was used once with Edson to debug an issue)
 * load and run facts and rules independently from EulerGUI;
 * using a different way of running Drools, with KnowledgeXXX
 * (made with Edson Tirelli) */
public class DroolsTest {

    @SuppressWarnings("unchecked")
    public static void loadFactsFromXMLFile(StatefulKnowledgeSession workingMemory) {
        File factsTriplesFile = new File("factsTriples.xml");
        try {
            InputStream inputStream = new FileInputStream(factsTriplesFile);
            XMLDecoder decoder = new XMLDecoder(inputStream);
            List<Triple> factsTriplesFromFile = (List<Triple>) decoder.readObject();
            for (Triple triple : factsTriplesFromFile) {
                workingMemory.insert(triple);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        loadSource(builder, "app_gui-rules.n3.drl");
        loadSource(builder, "java_projection-rules.n3.drl");
        loadSource(builder, "java_library-rules.n3.drl");
        loadSource(builder, "rpo-rules.n3.drl");
        loadSource(builder, "subform.n3.drl");
        loadSource(builder, "query.n3.drl");
        if (builder.hasErrors()) {
            System.out.println(builder.getErrors());
            System.exit(1);
        }
        RuleBaseConfiguration conf = new RuleBaseConfiguration();
        conf.setAssertBehaviour(AssertBehaviour.EQUALITY);
        conf.setSequentialAgenda(SequentialAgenda.SEQUENTIAL);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(conf);
        kbase.addKnowledgePackages(builder.getKnowledgePackages());
        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
        loadFactsFromXMLFile(session);
        System.out.println("before session.fireAllRules();");
        session.fireAllRules();
        System.out.println("after session.fireAllRules();");
        queryTripleResults(session);
        printAllTripleResults(session);
        System.out.println("DroolsTest.main()");
        session.dispose();
    }

    /** this supposes that the Drools source is in the classpath at the root level */
    private static void loadSource(KnowledgeBuilder builder, String drl) {
        builder.add(ResourceFactory.newInputStreamResource(DroolsFactsLoadStore.class.getResourceAsStream("/" + drl)), ResourceType.DRL);
        System.out.println("Compiled " + drl);
    }

    private static void printAllTripleResults(StatefulKnowledgeSession workingMemory) {
        ObjectFilter filter = new ObjectFilter() {

            @Override
            public boolean accept(Object arg0) {
                return arg0 instanceof TripleResult;
            }
        };
        Collection<?> coll = workingMemory.getObjects(filter);
        for (Object triple : coll) {
            System.out.print(triple);
        }
    }

    private static void queryTripleResults(StatefulKnowledgeSession workingMemory) {
        queryTripleResults(workingMemory, "<http://java.sun.com/class#javax_swing_JPanel>");
        queryTripleResults(workingMemory, "<http://java.sun.com/class#javax_swing_JLabel>");
        queryTripleResults(workingMemory, "<http://java.sun.com/class#javax_swing_JTextField>");
    }

    private static void queryTripleResults(StatefulKnowledgeSession workingMemory, String classURI) {
        ObjectFilter filter = new ObjectFilter() {

            @Override
            public boolean accept(Object arg0) {
                return arg0 instanceof TripleResult;
            }
        };
        Collection<?> coll = workingMemory.getObjects(filter);
        TreeSet<String> panelInstantiationsWishedResults = new TreeSet<String>();
        for (Object triple : coll) {
            if (triple instanceof TripleResult) {
                Triple t = (Triple) triple;
                if (classURI.equals(t.getObject())) {
                    System.out.print(t);
                    panelInstantiationsWishedResults.add(t.getSubject());
                }
            }
        }
        System.out.println("\npanelInstantiations Wished Results: " + panelInstantiationsWishedResults);
        TreeSet<String> panelInstantiationsActualResults = new TreeSet<String>();
        for (Object triple : coll) {
            if (triple instanceof TripleResult) {
                Triple t = (Triple) triple;
                if (classURI.equals(t.getObject()) && "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>".equals(t.getPredicate())) {
                    panelInstantiationsActualResults.add(t.getSubject());
                }
            }
        }
        System.out.println("\npanelInstantiations Actual Results: " + panelInstantiationsActualResults);
        System.out.println("\nSet difference : WishedResults - ActualResults : " + panelInstantiationsWishedResults.removeAll(panelInstantiationsActualResults) + "\n" + panelInstantiationsWishedResults);
    }
}
