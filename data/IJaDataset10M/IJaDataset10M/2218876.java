package com.loribel.commons.business;

import java.util.List;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.loribel.commons.GB_FwkInitializer;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.exception.GB_BOException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_XmlTools;

/**
 * Test GB_BOXmlTools.
 *
 * @author Gregory Borelli
 */
public class GB_BOXmlToolsTest extends TestCase {

    /**
     * Returns a list of BO for tests.
     */
    private List getBosForTest() {
        List retour = GB_BOTestTools.getBOsGenericForTest();
        return retour;
    }

    protected void setUp() throws Exception {
        super.setUp();
        GB_FwkInitializer.initAll();
    }

    public void test_cloneWithXml() throws GB_BOException {
        List l_bos = getBosForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            test_cloneWithXml("" + i, l_bo);
        }
    }

    private void test_cloneWithXml(String a_index, GB_SimpleBusinessObject a_bo) throws GB_BOException {
        GB_SimpleBusinessObject l_bo = GB_BOXmlTools.cloneWithXML(a_bo);
        GB_BOTestTools.assertEqualsBO(a_index, a_bo, l_bo);
    }

    public void test_toNodefromNode() throws GB_BOException {
        List l_bos = getBosForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            test_toNodefromNode("" + i, l_bo);
        }
    }

    private void test_toNodefromNode(String a_index, GB_SimpleBusinessObject a_bo) throws GB_BOException {
        Document l_doc = GB_XmlTools.newDocument();
        Element l_node = GB_BOXmlTools.toNode(a_bo, l_doc);
        assertNotNull(a_index + " - Node null", l_node);
        GB_SimpleBusinessObject l_bo = GB_BOXmlTools.fromNode(l_node);
        assertNotNull(a_index + " - BO null", l_bo);
        GB_BOTestTools.assertEqualsBO(a_index, a_bo, l_bo);
    }

    public void test_toStringXml() throws GB_BOException {
        List l_bos = getBosForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            test_toStringXml("" + i, l_bo);
        }
    }

    private void test_toStringXml(String a_index, GB_SimpleBusinessObject a_bo) throws GB_BOException {
        test_toStringXml(a_index + ".ISO", a_bo, ENCODING.ISO_8859_1);
        test_toStringXml(a_index + ".UTF", a_bo, ENCODING.UTF8);
    }

    private void test_toStringXml(String a_index, GB_SimpleBusinessObject a_bo, String a_encoding) throws GB_BOException {
        String l_xml = GB_BOXmlTools.toStringXml(a_bo, a_encoding, true);
        assertNotNull(l_xml);
        l_xml = GB_BOXmlTools.toStringXml(a_bo, a_encoding, false);
        assertNotNull(l_xml);
    }

    public void test_updateFromNode() throws GB_BOException {
        List l_bos = getBosForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            test_updateFromNode("" + i, l_bo);
        }
    }

    /**
     * TODO ajouter le cas property Map et property BO une fois le cas g�r�
     */
    private void test_updateFromNode(String a_index, GB_SimpleBusinessObject a_bo) throws GB_BOException {
        GB_BOTools.removeAllMapValues((GB_SimpleBusinessObjectSet) a_bo);
        GB_BOTools.removeAllComplexValues((GB_SimpleBusinessObjectSet) a_bo);
        Document l_doc = GB_XmlTools.newDocument();
        Element l_node = GB_BOXmlTools.toNode(a_bo, l_doc);
        assertNotNull(a_index + " - Node null", l_node);
        String l_boName = a_bo.getBOName();
        GB_SimpleBusinessObjectSet l_bo = (GB_SimpleBusinessObjectSet) GB_BOTools.newBO(l_boName);
        GB_BOXmlTools.updateFromNode(l_bo, l_node);
        GB_BOTestTools.assertEqualsBO(a_index, a_bo, l_bo);
    }
}
