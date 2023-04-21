package vmp.gate.tbl.entity;

import vmp.gate.tbl.utils.*;
import junit.framework.*;
import gate.Annotation;
import gate.util.GateException;
import gnu.trove.TIntIntHashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class TemplateTest extends TestCase {

    public TemplateTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    /**
     * Test of createRules method, of class vmp.gate.tbl.utils.Template.
     */
    public void testCreateRules() throws GateException {
        System.out.println("createRules");
        SetUpHelper setUp = SetUpHelper.getInstance();
        List<TIntIntHashMap> vicinity = setUp.getVicinity();
        Template instance = new Template("pos_-1 chunk_0 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        Rule r = new Rule("RULE: pos_-1=DA chunk_0=B-ORG => chunk=B-PER");
        Set<Rule> expResult = new HashSet();
        expResult.add(r);
        Set<Rule> result = instance.createRules(vicinity);
        System.out.println("vecindad " + vicinity);
        assertEquals(expResult, result);
        instance = new Template("pos:[-1,1] chunk_0 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        expResult = new HashSet();
        r = new Rule("RULE: pos:[-1,1]=DA chunk_0=B-ORG => chunk=B-PER");
        expResult.add(r);
        r = new Rule("RULE: pos:[-1,1]=NP chunk_0=B-ORG => chunk=B-PER");
        expResult.add(r);
        result = instance.createRules(vicinity);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class vmp.gate.tbl.utils.Template.
     */
    public void testHashCode() {
        System.out.println("hashCode");
        Template t1 = new Template("pos_0 chunk_1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        Template t2 = new Template("pos_0 chunk_1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        assertTrue("No sirve el hashCode" + t1.hashCode() + "!=" + t2.hashCode(), t1.hashCode() == t2.hashCode());
        t1 = new Template("pos:[-1,2] chunk_1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        t2 = new Template("chunk_1 pos:[-1,2] => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        assertTrue("No sirve el hashCode", t1.hashCode() == t2.hashCode());
    }

    /**
     * Test of equals method, of class vmp.gate.tbl.utils.Template.
     */
    public void testEquals() {
        System.out.println("equals");
        List<Template> templateList = new ArrayList();
        Template t1 = new Template("pos_0 chunk_1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        Template t2 = new Template("pos_0 chunk_1 => chunk", vmp.gate.tbl.utils.Vocabulary.getVocabulary().indexOf("tchunk"));
        templateList.add(t1);
        assertTrue("No sirve el array", templateList.contains(t2));
        assertTrue("No sirve el equals", t1.equals(t2));
    }
}
