package org.eclipse.wst.xml.security.core.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.File;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * <p>JUnit tests for the Utils class.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class UtilsTest {

    /** The XML document. */
    private Document xml = null;

    /** The selection in the XML document. */
    private Document selection = null;

    /** XML file containing different encryption and signature IDs. */
    private IFile idFile = null;

    /**
     * Set up method. Sets up the XML document used in these test cases.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        xml = Utils.parse(new File("resources/FirstSteps.xml"));
        IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(new File("resources/ids.xml"));
        FileStoreEditorInput input = new FileStoreEditorInput(fileStore);
        idFile = (IFile) input.getAdapter(IFile.class);
    }

    /**
     * Test for the XPath method to determine the XPath to a selected element and to a selected
     * element content.
     *
     * @throws Exception
     */
    @Test
    public void testGetUniqueXPathToNode() throws Exception {
        selection = Utils.parse("<Quantity>1</Quantity>");
        assertEquals("Invoice/InvoiceLine[2]/Quantity[1]", Utils.getUniqueXPathToNode(xml, selection));
        selection = Utils.parse("<PaymentMeans><PayeeFinancialAccount><ID>07044961</ID>" + "<Name>The Specialists Company</Name><AccountTypeCode>Credit</AccountTypeCode>" + "<FinancialInstitutionBranch><ID>776631</ID><Institution>LOYDGB852</Institution>" + "</FinancialInstitutionBranch></PayeeFinancialAccount></PaymentMeans>");
        assertEquals("Invoice/PaymentMeans[1]", Utils.getUniqueXPathToNode(xml, selection));
        selection = Utils.parse("<xmlsectempelement>458746</xmlsectempelement>");
        assertEquals("Invoice/BuyerParty[1]/ID[1]/text()", Utils.getUniqueXPathToNode(xml, selection));
    }

    /**
     * Test for the ID method to determine all IDs in the XML document based on the type signature
     * or encryption.
     */
    @Test
    public void testGetIds() throws Exception {
        String[] idsEncryption = { "Use first encryption id", "myEncryptionA", "myEncryptionB" };
        assertArrayEquals(idsEncryption, Utils.getIds(idFile, "encryption"));
        String[] idsSignature = { "Use first signature id", "mySignature" };
        assertArrayEquals(idsSignature, Utils.getIds(idFile, "signature"));
    }

    /**
     * Test for the ID method to determine all IDs in the XML document.
     */
    @Test
    public void testGetAllIds() throws Exception {
        String[] ids = { "myEncryptionA", "myEncryptionB", "mySignature" };
        assertArrayEquals(ids, Utils.getAllIds(idFile));
    }

    /**
     * Test for the XPath validation method to validate XPath expressions entered by the user.
     */
    @Test
    public void testValidateXPath() {
        assertEquals("attribute", Utils.validateXPath(xml, "Invoice/TotalAmount/@currencyID"));
        assertEquals("single", Utils.validateXPath(xml, "Invoice/InvoiceLine[1]/Item/BasePrice[@currencyID='�']"));
        assertEquals("multiple", Utils.validateXPath(xml, "Invoice/InvoiceLine"));
        assertEquals("none", Utils.validateXPath(xml, "Invoice/Line"));
    }

    /**
     * Test for signature and encryption id validation.
     */
    @Test
    public void testValidateId() {
        assertEquals(true, Utils.validateId("valid"));
        assertEquals(false, Utils.validateId("aa<324 "));
        assertEquals(false, Utils.validateId(">"));
        assertEquals(false, Utils.validateId("\""));
        assertEquals(false, Utils.validateId("'"));
        assertEquals(false, Utils.validateId("&"));
        assertEquals(false, Utils.validateId(" "));
        assertEquals(true, Utils.validateId("����!�$%/()"));
    }

    /**
     * Test for id uniqueness.
     */
    @Test
    public void testCheckIdUniqueness() {
        String[] ids = { "myID", "myid", "aaa", "bbb", "x", "���" };
        assertEquals(true, Utils.ensureIdIsUnique("aa", ids));
        assertEquals(true, Utils.ensureIdIsUnique("mySignatureID", ids));
        assertEquals(true, Utils.ensureIdIsUnique("����", ids));
        assertEquals(true, Utils.ensureIdIsUnique("myId", ids));
        assertEquals(false, Utils.ensureIdIsUnique("myid", ids));
        assertEquals(false, Utils.ensureIdIsUnique("���", ids));
        assertEquals(false, Utils.ensureIdIsUnique("x", ids));
        assertEquals(false, Utils.ensureIdIsUnique("bbb", ids));
    }
}
