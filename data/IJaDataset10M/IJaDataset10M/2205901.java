package org.xmlcml.cml.element.main;

import static org.xmlcml.cml.base.CMLXOMTestUtils.assertEqualsCanonically;
import static org.xmlcml.cml.element.main.AbstractTestBase.SIMPLE_RESOURCE;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLXOMTestUtils;
import org.xmlcml.cml.element.CMLArg;
import org.xmlcml.cml.element.CMLAtom;
import org.xmlcml.cml.element.CMLBond;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.euclid.Util;
import org.xmlcml.euclid.test.StringTestBase;

/**
 * test CMLArg.
 * 
 * @author pm286
 * 
 */
public class CMLArgTest {

    /**
	 * Test method for 'org.xmlcml.cml.element.CMLArg.CMLArg(String, int)'
	 */
    @Test
    public void testCMLArgStringInt() {
        CMLArg arg = new CMLArg("foo", 23);
        Assert.assertEquals("arg int", "foo", arg.getName());
        Assert.assertEquals("arg int", 23, arg.getInteger());
    }

    /**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLArg.substituteNameByValue(CMLElement)'
	 * 
	 * @exception Exception
	 */
    @Test
    public void testSubstituteNameByValueCMLElement() throws Exception {
        String cmlS = "" + "<cml " + CMLConstants.CML_XMLNS + " id='a_i_'>" + "  <molecule id='mol_i_' title='orig mol'>" + "    <arg name='i' substitute='.//@*'>" + "      <scalar dataType='xsd:integer'>42</scalar>" + "    </arg>" + "    <atomArray>" + "      <atom id='m_i__a1'/>" + "      <atom id='m_i__a2'/>" + "    </atomArray>" + "  </molecule>" + "</cml>" + "";
        CMLCml cml = (CMLCml) CMLXOMTestUtils.parseValidString(cmlS);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("child count", 1, cml.getChildElements().size());
        CMLMolecule mol = (CMLMolecule) cml.getChildCMLElements(CMLMolecule.TAG).get(0);
        Assert.assertEquals("mol id", "mol_i_", mol.getId());
        List<CMLAtom> atoms = mol.getAtoms();
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m_i__a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m_i__a2", atoms.get(1).getId());
        CMLArg.substituteNameByValue(mol);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("mol id", "mol42", mol.getId());
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m42_a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m42_a2", atoms.get(1).getId());
        URL url = Util.getResource(SIMPLE_RESOURCE + File.separator + "arg1.xml");
        CMLCml arg1Cml = null;
        InputStream in = null;
        try {
            in = url.openStream();
            arg1Cml = (CMLCml) new CMLBuilder().build(in).getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("EXC" + e);
        } finally {
            in.close();
        }
        CMLArg.substituteNameByValue(arg1Cml);
        CMLMolecule arg1Mol = (CMLMolecule) arg1Cml.getChildCMLElements(CMLMolecule.TAG).get(0);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("mol id", "oh42", arg1Mol.getId());
        atoms = arg1Mol.getAtoms();
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "oh42_a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "oh42_r1", atoms.get(1).getId());
        List<CMLBond> bonds = arg1Mol.getBonds();
        Assert.assertEquals("bond count", 2, atoms.size());
        Assert.assertEquals("bond id", "b_oh42_a1_oh42_r1", bonds.get(0).getId());
        StringTestBase.assertEquals("bond atomRefs2", new String[] { "oh42_a1", "oh42_r1" }, bonds.get(0).getAtomRefs2());
    }

    /**
	 * dewisott
	 */
    @Test
    public void testAddArg() {
        CMLList list = new CMLList();
        CMLArg arg = new CMLArg("a1", 1);
        CMLArg.addArg(list, arg, 0);
        arg = new CMLArg("a2", 2);
        CMLArg.addArg(list, arg, 0);
        String s = "<list xmlns='http://www.xml-cml.org/schema'>" + "<arg name='a2'>" + "<scalar dataType='xsd:integer'>2</scalar>" + "</arg>" + "<arg name='a1'>" + "<scalar dataType='xsd:integer'>1</scalar>" + "</arg>" + "</list>";
        assertEqualsCanonically("add arg", CMLXOMTestUtils.parseValidString(s), list, true);
        try {
            arg = new CMLArg("a1", 2);
            CMLArg.addArg(list, arg, 0);
            Assert.fail("should throw duplicate arg");
        } catch (RuntimeException e) {
        }
    }

    /**
	 * Test method for 'org.xmlcml.cml.element.CMLArg.eval(List<CMLArg>)'
	 * 
	 * @exception Exception
	 */
    @Test
    public void testEval() throws Exception {
        String cmlS = "" + "<cml " + CMLConstants.CML_XMLNS + " id='a_i_'>" + "  <molecule id='mol_i_' title='orig mol'>" + "    <arg name='i' substitute='.//@*'>" + "      <scalar dataType='xsd:integer'>42</scalar>" + "    </arg>" + "    <arg name='j' substitute='.//@*' eval='_i_+100'>" + "      <scalar dataType='xsd:integer'/>" + "    </arg>" + "    <atomArray>" + "      <atom id='m_j__a1'/>" + "      <atom id='m_j__a2'/>" + "    </atomArray>" + "  </molecule>" + "</cml>" + "";
        CMLCml cml = (CMLCml) CMLXOMTestUtils.parseValidString(cmlS);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("child count", 1, cml.getChildElements().size());
        CMLMolecule mol = (CMLMolecule) cml.getChildCMLElements(CMLMolecule.TAG).get(0);
        Assert.assertEquals("mol id", "mol_i_", mol.getId());
        List<CMLAtom> atoms = mol.getAtoms();
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m_j__a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m_j__a2", atoms.get(1).getId());
        CMLArg.substituteNameByValue(mol);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("mol id", "mol42", mol.getId());
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m142_a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m142_a2", atoms.get(1).getId());
    }

    /**
	 * Test method for 'org.xmlcml.cml.element.CMLArg.getInteger()'
	 */
    @Test
    public void testGetInteger() {
        CMLArg arg = new CMLArg("foo", 23);
        Integer i = arg.getInteger();
        Assert.assertEquals("int", 23, i.intValue());
    }
}
