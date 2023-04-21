package test.wssearch.junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.wssearch.DrawGraph;
import wssearch.Properties;
import wssearch.graph.ServiceGraph;
import wssearch.search.similaritygraph.SimilarityGraph;
import wssearch.search.VertexSearchResult;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

/**
 * JUnit-Test für Ähnlichkeitsgraphen.
 * Benutzt ServiceGraphJUnitTest um Dienstgraphen zu füllen, berechnet Ähnlichkeiten
 * und testet dann, ob Laden und Speichern funktioniert.
 * WICHTIG: Vorhandene XML-Dateien werden vor Test gelöscht, keine JUnit-Tests
 * durchführen wenn bereits echte Daten vorhanden sind!
 * @author Thorsten Theelen
 *
 */
public class SimilarityGraphJUnitTest {

    private Vector<VertexSearchResult> d0a, d1a, d2a, o0a, o1a, o2a, o3a, o4a;

    private String d0, d1, d2, o0, o1, o2, o3, o4;

    private boolean d0x, d1x, d2x;

    private SimilarityGraph smg;

    @Before
    public void setUp() throws Exception {
        ServiceGraphJUnitTest sgjut = new ServiceGraphJUnitTest();
        sgjut.setUp();
        setUp2();
    }

    public void setUp2() throws Exception {
        try {
            new File(Properties.similarityGraphFilename).delete();
        } catch (Exception e) {
        }
        ServiceGraph sg = ServiceGraph.getInstance();
        smg = SimilarityGraph.getInstance();
        smg.generateGraph();
        d0x = d1x = d2x = false;
        d0 = "S(" + sg.getNodeKeys("d0").get(0) + "):d0";
        d1 = "S(" + sg.getNodeKeys("d1").get(0) + "):d1";
        d2 = "S(" + sg.getNodeKeys("d2").get(0) + "):d2";
        Vector<String> ops = sg.getServiceOperations(d0);
        for (int i = 0; i < ops.size(); i++) {
            if (ops.get(i).endsWith("o0")) o0 = ops.get(i); else d0x = true;
        }
        ops = sg.getServiceOperations(d1);
        for (int i = 0; i < ops.size(); i++) {
            if (ops.get(i).endsWith("o1")) o1 = ops.get(i); else if (ops.get(i).endsWith("o2")) o2 = ops.get(i); else d1x = true;
        }
        ops = sg.getServiceOperations(d2);
        for (int i = 0; i < ops.size(); i++) {
            if (ops.get(i).endsWith("o3")) o3 = ops.get(i); else if (ops.get(i).endsWith("o2")) o4 = ops.get(i); else d2x = true;
        }
        d0a = smg.getSimilarNodes(d0);
        d1a = smg.getSimilarNodes(d1);
        d2a = smg.getSimilarNodes(d2);
        o0a = smg.getSimilarNodes(o0);
        o1a = smg.getSimilarNodes(o1);
        o2a = smg.getSimilarNodes(o2);
        o3a = smg.getSimilarNodes(o3);
        o4a = smg.getSimilarNodes(o4);
        smg.saveGraph();
        smg.reloadGraph();
    }

    @After
    public void tearDown() throws Exception {
        DrawGraph.draw(smg.getSimilarityGraph());
        Thread.sleep(2000000000);
    }

    @Test
    public final void testSimilarityGraph() {
        Assert.assertTrue("d0x", !d0x);
        Assert.assertTrue("d1x", !d1x);
        Assert.assertTrue("d2x", !d2x);
        Vector<VertexSearchResult> nXa = d0a;
        String node = d0;
        Vector<VertexSearchResult> nXb = smg.getSimilarNodes(node);
        Hashtable<String, Double> scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = d1a;
        node = d1;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = d2a;
        node = d2;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = o0a;
        node = o0;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = o1a;
        node = o1;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = o2a;
        node = o2;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = o3a;
        node = o3;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
        nXa = o4a;
        node = o4;
        nXb = smg.getSimilarNodes(node);
        scores = new Hashtable<String, Double>();
        System.out.println("\nSimilar Nodes for " + node);
        for (int i = 0; i < nXa.size(); i++) {
            VertexSearchResult result = nXa.get(i);
            scores.put(result.getName(), result.getScore());
        }
        for (int i = 0; i < nXb.size(); i++) {
            VertexSearchResult result = nXb.get(i);
            Assert.assertEquals(node + ", " + result.getName(), scores.get(result.getName()), result.getScore());
            System.out.println(result.getName() + "(" + result.getScore() + ")");
        }
    }
}
