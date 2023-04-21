package org.panopticode.java.supplement.complexian;

import org.apache.tools.ant.filters.StringInputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.panopticode.java.rdf.DefaultJavaRDFStore;
import org.panopticode.java.rdf.JavaRDFStore;
import org.panopticode.supplement.SupplementTestBase;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ComplexianSupplementTest extends SupplementTestBase {

    public void testLoadDataHappyPath() throws Exception {
        Document complexianDocument;
        Map<String, String> arguments;
        JavaRDFStore store;
        ComplexianSupplement supplement;
        String askQuery;
        String originalRDF;
        File workingDir;
        String complexianCCNXML;
        String complexianNpathXML;
        String rootDir = System.getProperty("java.io.tmpdir");
        if (!rootDir.endsWith(File.separator)) {
            rootDir += File.separator;
        }
        originalRDF = "<rdf:RDF xmlns:p='http://www.panopticode.org/ontologies/panopticode#'" + "         xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'" + "         xmlns:panopticode='http://www.panopticode.org/ontologies/panopticode#'" + "         xmlns:java='http://www.panopticode.org/ontologies/technology/java#'>" + "  <panopticode:Sample>" + "    <java:hasPackage>" + "      <java:Package>" + "        <java:hasFile>" + "          <panopticode:File>" + "            <panopticode:filePath>prod/src/org/panopticode/JavaOntology.java</panopticode:filePath>" + "            <java:hasType>" + "              <java:Class>" + "                <java:fullyQualifiedClassName>org.panopticode.java.JavaOntology</java:fullyQualifiedClassName>" + "                <java:hasMethod>" + "                  <java:Method>" + "                    <panopticode:startsAt>" + "                      <panopticode:FilePosition>" + "                        <panopticode:line rdf:datatype='http://www.w3.org/2001/XMLSchema#long'>17</panopticode:line>" + "                      </panopticode:FilePosition>" + "                    </panopticode:startsAt>" + "                  </java:Method>" + "                </java:hasMethod>" + "              </java:Class>" + "            </java:hasType>" + "          </panopticode:File>" + "        </java:hasFile>" + "        <java:hasFile>" + "          <panopticode:File>" + "            <panopticode:filePath>prod/src/org/panopticode/PanopticodeOntology.java</panopticode:filePath>" + "            <java:hasType>" + "              <java:Class>" + "                <java:fullyQualifiedClassName>org.panopticode.PanopticodeOntology</java:fullyQualifiedClassName>" + "                <java:hasMethod>" + "                  <java:Method>" + "                    <panopticode:startsAt>" + "                      <panopticode:FilePosition>" + "                        <panopticode:line rdf:datatype='http://www.w3.org/2001/XMLSchema#long'>22</panopticode:line>" + "                      </panopticode:FilePosition>" + "                    </panopticode:startsAt>" + "                  </java:Method>" + "                </java:hasMethod>" + "              </java:Class>" + "            </java:hasType>" + "          </panopticode:File>" + "        </java:hasFile>" + "      </java:Package>" + "    </java:hasPackage>" + "  </panopticode:Sample>" + "</rdf:RDF>";
        complexianCCNXML = "<complexian>" + "  <method file='" + rootDir + "prod/src/org/panopticode/JavaOntology.java' line='17' complexity='4' />" + "  <method file='" + rootDir + "prod/src/org/panopticode/PanopticodeOntology.java' line='22' complexity='2' />" + "</complexian>";
        complexianNpathXML = "<complexian>" + "  <method file='" + rootDir + "prod/src/org/panopticode/JavaOntology.java' line='17' complexity='3' />" + "  <method file='" + rootDir + "prod/src/org/panopticode/PanopticodeOntology.java' line='22' complexity='1' />" + "  <method file='" + rootDir + "prod/src/org/panopticode/DoesntExist.java' line='2' complexity='1' />" + "</complexian>";
        store = new DefaultJavaRDFStore(new StringInputStream(originalRDF));
        supplement = new ComplexianSupplement();
        workingDir = new File(rootDir);
        complexianDocument = DocumentHelper.parseText(complexianCCNXML);
        arguments = new HashMap<String, String>();
        arguments.put("type", "ccn");
        supplement.loadData(store, workingDir, complexianDocument, arguments);
        complexianDocument = DocumentHelper.parseText(complexianNpathXML);
        arguments = new HashMap<String, String>();
        arguments.put("type", "npath");
        supplement.loadData(store, workingDir, complexianDocument, arguments);
        askQuery = "PREFIX rdf:        <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX p:          <http://www.panopticode.org/ontologies/panopticode#> " + "PREFIX java:       <http://www.panopticode.org/ontologies/technology/java#> " + "PREFIX complexian: <http://www.panopticode.org/ontologies/supplement/complexian/1#> " + "ASK WHERE " + "{ " + "?method     rdf:type java:Method . " + "?method     p:startsAt ?position . " + "?position   p:line 17 . " + "?method complexian:cyclomaticComplexity 4 . " + "?method complexian:nPath 3 " + "}";
        assertRDFAsk(store, askQuery);
        askQuery = "PREFIX rdf:        <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX p:          <http://www.panopticode.org/ontologies/panopticode#> " + "PREFIX java:       <http://www.panopticode.org/ontologies/technology/java#> " + "PREFIX complexian: <http://www.panopticode.org/ontologies/supplement/complexian/1#> " + "ASK WHERE " + "{ " + "?method     rdf:type java:Method . " + "?method     p:startsAt ?position . " + "?position   p:line 22 . " + "?method complexian:cyclomaticComplexity 2 . " + "?method complexian:nPath 1 " + "}";
        assertRDFAsk(store, askQuery);
    }
}
