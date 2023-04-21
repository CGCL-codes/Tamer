package org.hip.kernel.code.test;

import static org.junit.Assert.*;
import java.util.Vector;
import org.hip.kernel.code.AbstractCode;
import org.hip.kernel.code.CodeList;
import org.hip.kernel.code.CodeListFactory;
import org.hip.kernel.code.CodeNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author: Benno Luthiger
 */
public class CodeListTest {

    private static CodeList codeList;

    @BeforeClass
    public static void init() {
        codeList = new CodeListFactory().createCodeList("CodeTest", "de");
    }

    @Test
    public void testDo() {
        String[] lExpectedIDs = { "1", "4", "3", "2" };
        String lExpIDs = " 1234";
        String[] lExpectedLabels = { "Dies", "Test!", "ein", "ist" };
        String lExpLabels = " Dies ist ein Test!";
        int i = 0;
        String[] lActual = codeList.getElementIDs();
        assertEquals("length 1", lExpectedIDs.length, lActual.length);
        for (i = 0; i < lActual.length; i++) assertEquals("sorted ID " + i, lExpectedIDs[i], lActual[i]);
        lActual = codeList.getElementIDsUnsorted();
        assertEquals("length 2", lExpectedIDs.length, lActual.length);
        for (i = 0; i < lActual.length; i++) assertTrue("unsorted ID " + i, lExpIDs.indexOf(lActual[i]) > 0);
        lActual = codeList.getLabels();
        assertEquals("length 3", lExpectedLabels.length, lActual.length);
        for (i = 0; i < lActual.length; i++) assertEquals("sorted label " + i, lExpectedLabels[i], lActual[i]);
        lActual = codeList.getLabelsUnsorted();
        assertEquals("length 2", lExpectedIDs.length, lActual.length);
        for (i = 0; i < lActual.length; i++) assertTrue("unsorted label " + i, lExpLabels.indexOf(lActual[i]) > 0);
        try {
            codeList.existElementID("2");
        } catch (org.hip.kernel.code.CodeNotFoundException exc) {
            fail(exc.getMessage());
        }
        try {
            codeList.existElementID("0");
            fail("Code with elementID 0 doesn't exist!");
        } catch (org.hip.kernel.code.CodeNotFoundException exc) {
        }
    }

    @Test
    public void testGetElementIDByLabel() {
        try {
            assertEquals("getElementIDByLabel 1", "1", codeList.getElementIDByLabel("Dies"));
            assertEquals("getElementIDByLabel 2", "2", codeList.getElementIDByLabel("ist"));
            assertEquals("getElementIDByLabel 3", "3", codeList.getElementIDByLabel("ein"));
            assertEquals("getElementIDByLabel 4", "4", codeList.getElementIDByLabel("Test!"));
        } catch (org.hip.kernel.code.CodeNotFoundException exc) {
            fail(exc.getMessage());
        }
        try {
            codeList.getElementIDByLabel("Test");
            fail("Label 'Test' doesn't exist!");
        } catch (CodeNotFoundException exc) {
        }
        try {
            codeList.getElementIDByLabel("dies");
            fail("Label 'dies' doesn't exist!");
        } catch (CodeNotFoundException exc) {
        }
        try {
            codeList.getElementIDByLabel("Hallo");
            fail("Label 'Hallo' doesn't exist!");
        } catch (CodeNotFoundException exc) {
        }
    }

    @Test
    public void testGetLabel() {
        assertEquals("getLabel 1", "Dies", codeList.getLabel("1"));
        assertEquals("getLabel 2", "ist", codeList.getLabel("2"));
        assertEquals("getLabel 3", "ein", codeList.getLabel("3"));
        assertEquals("getLabel 4", "Test!", codeList.getLabel("4"));
        assertEquals("getLabel 5", null, codeList.getLabel("5"));
    }

    @Test
    public void testToString() {
        String lExpected1 = "1;4;3;2";
        String lExpected2 = "Dies;Test!;ein;ist";
        assertEquals("toString", lExpected1, codeList.toString());
        assertEquals("toLongString", lExpected2, codeList.toLongString());
        String lExpected3 = "<codeList> \n" + "<codeListItem id=\"1\" >Dies</codeListItem> \n" + "<codeListItem id=\"2\" selected=\"true\">ist</codeListItem> \n" + "<codeListItem id=\"3\" >ein</codeListItem> \n" + "<codeListItem id=\"4\" >Test!</codeListItem> \n" + "</codeList> \n";
        TestCode[] lSelected = new TestCode[1];
        lSelected[0] = new TestCode("2");
        assertEquals("toSelectionString", lExpected3, codeList.toSelectionString(lSelected));
        String lExpected4 = "<codeList> \n" + "<codeListItem id=\"1\" >Dies</codeListItem> \n" + "<codeListItem id=\"2\" selected=\"true\">ist</codeListItem> \n" + "<codeListItem id=\"3\" >ein</codeListItem> \n" + "<codeListItem id=\"4\" selected=\"true\">Test!</codeListItem> \n" + "</codeList> \n";
        lSelected = new TestCode[2];
        lSelected[0] = new TestCode("4");
        lSelected[1] = new TestCode("2");
        assertEquals("toSelectionString1", lExpected4, codeList.toSelectionString(lSelected));
        Vector<AbstractCode> lSelVector = new Vector<AbstractCode>();
        lSelVector.add(new TestCode("4"));
        lSelVector.add(new TestCode("2"));
        assertEquals("toSelectionString2", lExpected4, codeList.toSelectionString(lSelVector));
    }
}
