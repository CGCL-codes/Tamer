package predict;

import junit.framework.TestCase;
import mem.OneView;
import mem.Hist;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.io.InputStreamReader;
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import predict.singletarget.*;
import utils.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: adenysenko
 * Date: 25/7/2008
 * Time: 18:15:26
 */
public class TestPredictor extends TestCase {

    public void testTimebased1() {
        LinearPredictor p = new LinearPredictor();
        p.add(new OneView().pt("a", "1"));
        p.add(new OneView().pt("a", "1"));
        assertEquals("1", p.predict().get("a"));
    }

    public void testFaTimebasedFastLearn3() {
        LinearPredictor p = new LinearPredictor();
        p.add(new OneView().pt("a", "1"));
        p.add(new OneView().pt("a", "0"));
        p.add(new OneView().pt("a", "1"));
        assertEquals("0", p.predict().get("a"));
    }

    public void testFaTimebased3() {
        String task = "110011>0";
        plainSeqProc(task);
    }

    public void testFaTimebased4() {
        String task = "0 1012 11 1012 01 10>12 0 101>2 0 10>12 101>2 11111 10>12 11";
        plainSeqProc(task);
    }

    public void testFaTimebased5() {
        String task = "0 101 22 101 00 10>1 00 10>1 10>1 11111 10>1 11";
        plainSeqProc(task);
    }

    public void test6Suggest() {
        HistSuggest sg = new HistSuggest();
        OneView v1 = addMulti(null, "A0", sg, "2");
        OneView v2 = addMulti(null, "B2", sg, "3");
        OneView v3 = addMulti(null, "A3", sg, "2");
        OneView v4 = addMulti(null, "B2", sg, "4");
        OneView v5 = addMulti(null, "C4", sg, "1");
        OneView v6 = addMulti(null, "A2", sg, "2");
        RuleCond r = sg.ruleByDecisionStump(Arrays.asList(v1, v2, v3, v4, v5, v6), null);
        assertEquals(r.toString(), "{a=A} neg {}");
    }

    public void test6b() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "A0");
        p.printRules("b");
        addMulti(p, "B2");
        p.printRules("b");
        addMulti(p, "A3");
        p.printRules("b");
        addMulti(p, "B2");
        p.printRules("b");
        addMulti(p, "C4");
        p.printRules("b");
        addMulti(p, "A4");
        p.printRules("b");
        addMulti(p, "C2");
        p.printRules("b");
        addMulti(p, "A1");
        p.printRules("b");
        assertEquals("2", p.predict().get("b"));
    }

    public void test6() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "A0");
        p.printRules("b");
        addMulti(p, "B2");
        p.printRules("b");
        addMulti(p, "A3");
        p.printRules("b");
        addMulti(p, "B2");
        p.printRules("b");
        addMulti(p, "C4");
        p.printRules("b");
        addMulti(p, "A1");
        p.printRules("b");
        assertEquals("2", p.predict().get("b"));
    }

    public void test7() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "A0");
        addMulti(p, "B1");
        addMulti(p, "C0");
        addMulti(p, "B2");
        addMulti(p, "A0");
        addMulti(p, "B1");
        addMulti(p, "C5");
        addMulti(p, "B3");
        addMulti(p, "B1");
        assertEquals("C", p.predict().get("a"));
    }

    public void test8() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk1");
        addMulti(p, "xAp0");
        addMulti(p, "xAp1");
        addMulti(p, "zAk1");
        addMulti(p, "zBk0");
        addMulti(p, "xAk0");
        assertEquals(null, p.predict().get("d"));
        addMulti(p, "xBk0");
        assertEquals("0", p.predict().get("d"));
        addMulti(p, "xAv0");
        assertEquals("1", p.predict().get("d"));
    }

    public void test8disambig() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk1");
        addMulti(p, "yAp0");
        addMulti(p, "xAp1");
        addMulti(p, "zAk1");
        addMulti(p, "zBk0");
        addMulti(p, "xAk0");
        assertEquals(null, p.predict().get("d"));
    }

    public void test8disambig2() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk1");
        addMulti(p, "yAp0");
        addMulti(p, "xAp1");
        addMulti(p, "mAk1");
        addMulti(p, "zBk0");
        addMulti(p, "xAk0");
        assertEquals("0", p.predict().get("d"));
    }

    public void test8WrongWideOnAdd() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1q");
        addMulti(p, "zAk1q");
        addMulti(p, "yAp0q");
        addMulti(p, "xAp1q");
        addMulti(p, "vAk1q");
        addMulti(p, "pBk0q");
        addMulti(p, "xAk0q");
        assertEquals("0", p.predict().get("d"));
        addMulti(p, "xAk0s");
        assertEquals(null, p.predict().get("d"));
    }

    public void testRefSame1() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk4");
        addMulti(p, "xAp4");
        addMulti(p, "xAp1");
        addMulti(p, "zAk3");
        addMulti(p, "zBk3");
        addMulti(p, "mCt3");
        addMulti(p, "mCt0");
        addMulti(p, "mCk5");
        assertEquals(null, p.predict().get("d"));
    }

    public void testRefSame1b() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk4");
        addMulti(p, "xAp4");
        addMulti(p, "xAp1");
        addMulti(p, "yAk3");
        addMulti(p, "zBk3");
        addMulti(p, "mCt3");
        addMulti(p, "mCt0");
        addMulti(p, "mCk5");
        assertEquals("5", p.predict().get("d"));
    }

    public void testRefSame2() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "zAn1");
        addMulti(p, "zAk4");
        addMulti(p, "xAp4");
        addMulti(p, "xAp1");
        addMulti(p, "rAk3");
        addMulti(p, "zBk3");
        addMulti(p, "mCt3");
        addMulti(p, "mCt0");
        addMulti(p, "mCk5");
        assertEquals("5", p.predict().get("d"));
    }

    public void testDoubtExtraWidening9() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "1Abz0");
        addMulti(p, "2Njq1");
        addMulti(p, "1Mjq0");
        addMulti(p, "2Abw0");
        addMulti(p, "1Nhq1");
        addMulti(p, "2Yhp0");
        addMulti(p, "1Nbp0");
        addMulti(p, "2Avk0");
        Object pred = p.predict().get("e");
        assertEquals(null, pred);
    }

    public void testDoubtExtraWidening9b() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "1Abz0");
        addMulti(p, "2Njq1");
        addMulti(p, "1Mjq0");
        addMulti(p, "2Abw0");
        addMulti(p, "1Nhq1");
        addMulti(p, "2Yhp0");
        addMulti(p, "1Nbp0");
        addMulti(p, "2Abk0");
        Object pred = p.predict().get("e");
        assertEquals("1", pred);
    }

    public void testLongTime() {
        LinearPredictor p = new LinearPredictor();
        addMulti(p, "1Abz0");
        addMulti(p, "2Njq1");
        for (int i = 0; i < 100; i++) {
            addMulti(p, "1Mjq0");
        }
        addMulti(p, "2Abw0");
        addMulti(p, "1Nhq1");
        addMulti(p, "2Yhp0");
        addMulti(p, "1Nbp0");
        addMulti(p, "2Abk0");
        Object pred = p.predict().get("e");
        assertEquals("1", pred);
    }

    public void testFaFastLearn1() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        assertEquals("1", p.predict().get("$"));
    }

    public void test10() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        assertEquals("0", p.predict().get("$"));
    }

    public void test10b() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=BLACK, r=WHITE, ff=BLACK, $=0, fr=BLACK, l=BLACK}");
        assertEquals(null, p.predict().get("$"));
    }

    public void test10c() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=WHITE, ff=BLACK, $=0, fr=BLACK, l=BLACK}");
        assertEquals("1", p.predict().get("$"));
    }

    public void testTree1a() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, fl=WHITE, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        List<String> cmds = Arrays.asList("L", "R", "N", "Fa", "Fb", "E", "Ep");
        CmdPredictionTree tree = new PredictionTreeBuilder(p.getPredictor(), cmds, 2).build(p.getLast());
        assertNull(tree.findPositiveResultOrSmacks());
    }

    public void testTree1() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, fl=WHITE, r=WHITE, ff=BLACK, $=0, fr=BLACK, l=BLACK}");
        List<String> cmds = Arrays.asList("L", "R", "N", "Fa", "Fb", "E", "Ep");
        CmdPredictionTree tree = new PredictionTreeBuilder(p.getPredictor(), cmds, 2).build(p.getLast());
        assertNotNull(tree.findPositiveResultOrSmacks());
    }

    public void test11() {
        LinearPredictor p = new LinearPredictor();
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}");
        addMultiMap(p, "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=1, fr=BLACK, l=WHITE}");
        addMultiMap(p, "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=1, fr=BLACK, l=BLACK}");
        addMultiMap(p, "{f=ORANGE, !=E, fl=WHITE, r=WHITE, ff=BLACK, $=1, fr=BLACK, l=BLACK}");
        assertEquals("0", p.predict().get("$"));
    }

    public void testTooManyRules1() throws Exception {
        buildPredictor("testTooManyRules1");
    }

    public void testFaFastLearn2() {
        LinearPredictor p = new LinearPredictor();
        addMultiMapAll(p, "{f=YELLOW, !=Ep, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}", "{f=ORANGE, !=E, fl=GRAY, r=YELLOW, ff=BLACK, $=0, fr=WHITE, l=GRAY}", "{f=WHITE, !=R, fl=GRAY, r=YELLOW, ff=BLACK, $=1, fr=WHITE, l=GRAY}", "{f=YELLOW, !=Ep, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}", "{f=ORANGE, !=E, fl=WHITE, r=BLACK, ff=BLACK, $=0, fr=YELLOW, l=WHITE}", "{f=WHITE, !=Fb, fl=WHITE, r=BLACK, ff=BLACK, $=1, fr=YELLOW, l=WHITE}", "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}", "{f=YELLOW, !=L, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}", "{f=BLACK, !=R, fl=BLACK, r=YELLOW, ff=BLACK, $=0, fr=BLACK, l=WHITE}", "{f=YELLOW, !=Ep, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}", "{f=ORANGE, !=E, fl=BLACK, r=WHITE, ff=WHITE, $=0, fr=BLACK, l=BLACK}");
        assertEquals("1", p.predict().get("$"));
    }

    public void testRuleGeneralization() throws Exception {
        SensorHist sensor = new SensorHist("E");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testRuleGeneralization");
    }

    public void testAreaStrip() throws Exception {
        SensorHist sensor = new SensorHist("E");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testAreaStrip");
        Utils.breakPoint();
    }

    public void testAreaIsle() throws Exception {
        SensorHist sensor = new SensorHist("E");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testAreaIsle");
        Utils.breakPoint();
    }

    public void testArea2Islands() throws Exception {
        SensorHist sensor = new SensorHist("E");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testArea2Islands");
        Utils.breakPoint();
    }

    public void testArea2Strips() throws Exception {
        SensorHist sensor = new SensorHist("E");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testArea2Strips");
        Utils.breakPoint();
    }

    public void testFaRulesFor2Categories() throws Exception {
        SensorHist sensor = new SensorHist("$");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testRulesFor2Categories");
    }

    public void testFaResultEqPrevNull() throws Exception {
        SensorHist sensor = new SensorHist("f");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.CMD_KEY));
        examplesForSensorHist(sensor, "testResultEqPrevNull");
    }

    public void testFaResultEqPrevNullTree() throws Exception {
        LinearPredictor p = buildPredictor("testResultEqPrevNullTree");
        CmdPredictionTree tree = new PredictionTreeBuilder(p.p, Arrays.asList("L", "R", "N", "Fb", "A1", "A2F", "A2B", "B1", "B2"), 4).build(p.last);
        assertTrue(tree.branchOnCommand("N").noopDetected());
    }

    private LinearPredictor buildPredictor(String tag) throws SAXException, IOException, ParserConfigurationException {
        LinearPredictor p = new LinearPredictor();
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getResourceAsStream("data.xml"));
        String txt = d.getElementsByTagName(tag).item(0).getTextContent();
        for (String s : txt.split("\n")) {
            s = s.trim();
            if (s.length() == 0) {
                continue;
            }
            addMultiMap(p, s);
        }
        return p;
    }

    public void testReorderedOverDecisionStump() throws Exception {
        SensorHist sensor = new SensorHist("$");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testReorderedOverDecisionStump");
    }

    public void testFrScatteredRules() throws Exception {
        SensorHist sensor = new SensorHist("fr");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testFrScatteredRules");
    }

    public void testMoveRef() throws Exception {
        SensorHist sensor = new SensorHist("mmm");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testMoveRef");
        Utils.breakPoint();
    }

    public void testMoveRef2() throws Exception {
        SensorHist sensor = new SensorHist("mmm");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testMoveRef2");
        Utils.breakPoint();
    }

    public void testSimple2atrr() throws Exception {
        SensorHist sensor = new SensorHist("$");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testSimple2atrr");
    }

    public void testPredictRepeated() throws Exception {
        SensorHist sensor = new SensorHist("fr");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testPredictRepeated");
    }

    public void testPredictRepeated2() throws Exception {
        SensorHist sensor = new SensorHist("fr");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testPredictRepeated2");
    }

    public void testFaRecencyPredictRepeated3() throws Exception {
        SensorHist sensor = new SensorHist("fr");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHist(sensor, "testRecencyPredictRepeated3");
    }

    public void testRecencySpread() throws Exception {
        SensorHist sensor = new SensorHist("f");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHistFile(sensor, "testRecencySpread.properties");
    }

    public void testNmisPred_fl() throws Exception {
        SensorHist sensor = new SensorHist("fl");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHistFile(sensor, "testNmisPred_fl.properties");
    }

    public void testNmisPred_rr() throws Exception {
        SensorHist sensor = new SensorHist("rr");
        sensor.setSkippedViewKeys(Collections.singleton(Hist.RES_KEY));
        examplesForSensorHistFile(sensor, "testNmisPred_rr.properties");
    }

    private void examplesForSensorHist(SensorHist sensor, String xmlElem) throws Exception {
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getResourceAsStream("data.xml"));
        String txt = d.getElementsByTagName(xmlElem).item(0).getTextContent();
        examplesForSensorHistStr(sensor, txt);
    }

    private void examplesForSensorHistFile(SensorHist sensor, String file) throws Exception {
        String txt = Utils.readAll(new InputStreamReader(getClass().getResourceAsStream(file), "utf-8"));
        examplesForSensorHistStr(sensor, txt);
    }

    private void examplesForSensorHistStr(SensorHist sensor, String txt) {
        OneView vprev = null;
        for (String s : txt.split("\n")) {
            s = s.trim();
            if (s.length() == 0) {
                continue;
            }
            boolean quest = false;
            if (s.startsWith("?")) {
                quest = true;
                s = s.substring(1);
            }
            String key = "-";
            int posBr = s.indexOf("{");
            if (posBr > 0) {
                key = s.substring(0, posBr);
                s = s.substring(posBr);
            }
            OneView v = mkOneView(s);
            v.prev = vprev;
            vprev = v;
            if (quest) {
                boolean acc = sensor.valAcceptedByRules(v, key);
                Object wekaKey = sensor.predictWithWeka(v);
                PredictionResult pred1 = sensor.predictState(v, sensor.getViewToValStatic());
                Object pred = pred1.val(sensor.getSensorName());
                assertTrue("key=" + key + " wekaKey=" + wekaKey + " pred=" + pred, acc);
            } else {
                sensor.addAsCurrent(key, v);
            }
        }
    }

    void addMultiMapAll(LinearPredictor p, String... views) {
        for (String v : views) {
            addMultiMap(p, v);
        }
    }

    void addMultiMap(LinearPredictor p, String view) {
        OneView v = mkOneView(view);
        p.add(v);
    }

    private OneView mkOneView(String view) {
        OneView v = new OneView();
        String[] elems = view.split("[{}, ]+");
        for (String e : elems) {
            if (e.length() > 0) {
                String[] pair = e.split("=");
                v.pt(pair[0], pair[1]);
            }
        }
        return v;
    }

    void addMulti(LinearPredictor p, String view) {
        OneView v = new OneView();
        for (int i = 0; i < view.length(); i++) {
            v.pt("" + (char) ('a' + i), "" + new Character(view.charAt(i)));
        }
        p.add(v);
    }

    OneView addMulti(OneView prev, String view, HistSuggest analyzer, Object res) {
        OneView v = new OneView();
        for (int i = 0; i < view.length(); i++) {
            v.pt("" + (char) ('a' + i), "" + new Character(view.charAt(i)));
        }
        v.chain(prev);
        analyzer.addAsCurrent(res, v);
        return v;
    }

    private void plainSeqProc(String task) {
        LinearPredictor p = new LinearPredictor();
        StringBuilder hist = new StringBuilder();
        StringBuilder compleated = new StringBuilder();
        for (int i = 0; i < task.length(); i++) {
            char c = task.charAt(i);
            compleated.append(c);
            if (c == ' ') {
                continue;
            }
            if (c == '>') {
                assertEquals("hist=" + hist, "" + task.charAt(i + 1), p.predict().get("a"));
            } else {
                p.add(new OneView().pt("a", "" + c));
                hist.append(c);
            }
        }
    }
}
