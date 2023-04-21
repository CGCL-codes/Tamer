package gate.creole.namematch;

import java.util.*;
import java.io.*;
import junit.framework.*;
import java.net.*;
import gate.*;
import gate.util.*;
import gate.corpora.*;

public class TestNamematch extends TestCase {

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** Construction */
    public TestNamematch(String name) {
        super(name);
    }

    /** Fixture set up */
    public void setUp() {
    }

    /** Test suite routine for the test runner */
    public static Test suite() {
        return new TestSuite(TestNamematch.class);
    }

    /** test the namematcher */
    public void testNamematch() throws Exception {
        Document doc = Factory.newDocument(new URL(TestDocument.getTestServerName() + "tests/matcher.txt"));
        Set annotationTypes = new HashSet();
        annotationTypes.add("Organization");
        annotationTypes.add("Person");
        annotationTypes.add("Location");
        FeatureMap params = Factory.newFeatureMap();
        params.put("annotationTypes", annotationTypes);
        params.put("organizationType", "Organization");
        params.put("personType", "Person");
        Namematch namematch = (Namematch) Factory.createResource("gate.creole.namematch.Namematch", params);
        AnnotationSet annotSetAS = doc.getAnnotations("AnnotationSetAS");
        Integer newId;
        FeatureMap fm = Factory.newFeatureMap();
        try {
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(257), new Long(274), "unknown", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "person");
            fm.put("country", "USA");
            annotSetAS.add(new Long(275), new Long(293), "Person", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(294), new Long(306), "unknown", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(307), new Long(326), "unknown", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "person");
            fm.put("country", "USA");
            annotSetAS.add(new Long(327), new Long(338), "Person", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(339), new Long(349), "Organization", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(350), new Long(365), "Organization", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(386), new Long(389), "Organization", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(390), new Long(394), "unknown", fm);
            fm = Factory.newFeatureMap();
            fm.put("token", "org");
            fm.put("country", "USA");
            annotSetAS.add(new Long(395), new Long(422), "Organization", fm);
        } catch (InvalidOffsetException ioe) {
            ioe.printStackTrace();
        }
        namematch.setDocument(doc);
        namematch.setAnnotationSetName("AnnotationSetAS");
        namematch.setExtLists(new Boolean(false));
        namematch.run();
        namematch.check();
        List matches = namematch.getMatchesDocument();
        if (matches != null) assert (matches.toString().equals("[[9, 7], [8, 6], [5, 3, 0], [4, 2]]"));
        AnnotationSet annotSet = doc.getAnnotations("AnnotationSetAS");
        FeatureMap fm1;
        Annotation annot;
        Iterator i;
        Long offsetStartAnnot = new Long(257);
        Long offsetEndAnnot = new Long(274);
        String annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(0));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("3") || value.equals("5"));
                }
            }
        }
        offsetStartAnnot = new Long(294);
        offsetEndAnnot = new Long(306);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(2));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("4"));
                }
            }
        }
        offsetStartAnnot = new Long(307);
        offsetEndAnnot = new Long(326);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(3));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("0") || value.equals("5"));
                }
            }
        }
        offsetStartAnnot = new Long(327);
        offsetEndAnnot = new Long(338);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(4));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("2"));
                }
            }
        }
        offsetStartAnnot = new Long(339);
        offsetEndAnnot = new Long(349);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(5));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("0") || value.equals("3"));
                }
            }
        }
        offsetStartAnnot = new Long(350);
        offsetEndAnnot = new Long(365);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(6));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("8"));
                }
            }
        }
        offsetStartAnnot = new Long(386);
        offsetEndAnnot = new Long(389);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(7));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("9"));
                }
            }
        }
        offsetStartAnnot = new Long(395);
        offsetEndAnnot = new Long(422);
        annotString = doc.getContent().getContent(offsetStartAnnot, offsetEndAnnot).toString();
        annot = annotSet.get(new Integer(9));
        fm1 = annot.getFeatures();
        i = fm1.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            if (type == "matches") {
                List list = (List) fm1.get(type);
                for (int j = 0; j < list.size(); j++) {
                    String value = (String) list.get(j);
                    assert (value.equals("7"));
                }
            }
        }
    }

    public static void main(String[] args) {
        TestNamematch test = new TestNamematch("");
        try {
            Gate.init();
            test.testNamematch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
