package plugin.pretokens.test;

import org.junit.Before;
import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.SpellDescriptor;
import pcgen.cdom.graph.PCGraphGrantsEdge;
import pcgen.cdom.inst.CDOMLanguage;
import pcgen.cdom.inst.CDOMSpell;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.core.prereq.PrerequisiteTest;

public class PreSpellDescriptorTesterTest extends AbstractCDOMPreTestTestCase<CDOMSpell> {

    PreSpellDescriptorTester tester = new PreSpellDescriptorTester();

    private SpellDescriptor mindAffecting;

    private SpellDescriptor mind;

    private SpellDescriptor fear;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        mindAffecting = SpellDescriptor.getConstant("Mind-Affecting");
        mind = SpellDescriptor.getConstant("Mind");
        fear = SpellDescriptor.getConstant("Fear");
    }

    @Override
    public Class<CDOMSpell> getCDOMClass() {
        return CDOMSpell.class;
    }

    @Override
    public Class<? extends CDOMObject> getFalseClass() {
        return CDOMLanguage.class;
    }

    public String getKind() {
        return "SPELL.DESCRIPTOR";
    }

    public PrerequisiteTest getTest() {
        return tester;
    }

    public Prerequisite getSimplePrereq() {
        Prerequisite p;
        p = new Prerequisite();
        p.setKind(getKind());
        p.setKey("Fear");
        p.setOperand("1");
        p.setSubKey("1");
        p.setOperator(PrerequisiteOperator.GTEQ);
        return p;
    }

    public Prerequisite getLevelTwoPrereq() {
        Prerequisite p;
        p = new Prerequisite();
        p.setKind(getKind());
        p.setKey("Fear");
        p.setOperand("1");
        p.setSubKey("2");
        p.setOperator(PrerequisiteOperator.GTEQ);
        return p;
    }

    public Prerequisite getStartPrereq() {
        Prerequisite p;
        p = new Prerequisite();
        p.setKind(getKind());
        p.setKey("Mind-Affecting");
        p.setOperand("1");
        p.setSubKey("1");
        p.setOperator(PrerequisiteOperator.GTEQ);
        return p;
    }

    @Test
    public void testSimple() throws PrerequisiteException {
        Prerequisite prereq = getSimplePrereq();
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        CDOMObject spell = getObject("Wild Mage");
        PCGraphGrantsEdge edge = grantObject(spell);
        edge.setAssociation(AssociationKey.SPELL_LEVEL, Integer.valueOf(1));
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mindAffecting);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, fear);
        assertEquals(1, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mind);
        assertEquals(1, getTest().passesCDOM(prereq, pc));
    }

    @Test
    public void testLevelTwo() throws PrerequisiteException {
        Prerequisite prereq = getLevelTwoPrereq();
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        CDOMObject spell = getObject("Wild Mage");
        PCGraphGrantsEdge edge = grantObject(spell);
        edge.setAssociation(AssociationKey.SPELL_LEVEL, Integer.valueOf(1));
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mindAffecting);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, fear);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mind);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        edge.setAssociation(AssociationKey.SPELL_LEVEL, Integer.valueOf(2));
        assertEquals(1, getTest().passesCDOM(prereq, pc));
        edge.setAssociation(AssociationKey.SPELL_LEVEL, Integer.valueOf(3));
        assertEquals(1, getTest().passesCDOM(prereq, pc));
    }

    @Test
    public void testFalseObject() throws PrerequisiteException {
        Prerequisite prereq = getSimplePrereq();
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        CDOMObject lang = grantFalseObject("Winged Mage");
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        lang.addToListFor(ListKey.SPELL_DESCRIPTOR, mindAffecting);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        lang.addToListFor(ListKey.SPELL_DESCRIPTOR, fear);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        lang.addToListFor(ListKey.SPELL_DESCRIPTOR, mind);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
    }

    @Test
    public void testFalseStart() throws PrerequisiteException {
        Prerequisite prereq = getStartPrereq();
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        CDOMObject spell = getObject("Wild Mage");
        PCGraphGrantsEdge edge = grantObject(spell);
        edge.setAssociation(AssociationKey.SPELL_LEVEL, Integer.valueOf(3));
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, fear);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mind);
        assertEquals(0, getTest().passesCDOM(prereq, pc));
        spell.addToListFor(ListKey.SPELL_DESCRIPTOR, mindAffecting);
        assertEquals(1, getTest().passesCDOM(prereq, pc));
    }
}
