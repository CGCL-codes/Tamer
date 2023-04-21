package org.xaware.server.engine.instruction.bizcomps.file.write;

import org.xaware.server.engine.instruction.InstructionTestHelper;
import org.xaware.testing.util.BaseBdpTestCase;

/**
 * This class provides tests for the FileBizCompInt instruction (aka File BizComponent) when the request_type in the
 * BizDriver is write. For each positive scenario, it writes data to the file output.txt.
 * 
 * @author jtarnowski
 */
public class TestFileWrite extends BaseBdpTestCase {

    /** Our dataFolder */
    private static final String dataFolder = "data/org/xaware/server/engine/instruction/bizcomps/file/write/";

    InstructionTestHelper testHelper = null;

    /**
     * Constructor
     * 
     * @param arg0 -
     *            String
     */
    public TestFileWrite(String arg0) {
        super(arg0);
    }

    /**
     * Get our data folder must be implemented
     * 
     * @return String
     */
    @Override
    public String getDataFolder() {
        return dataFolder;
    }

    /**
     * Test simple write of delimited file
     */
    public void testFileWriteDelimited() {
        clearInputParams();
        setInputXmlFileName("contactList.xml");
        setBizDocFileName("delimitedWrite.xbd");
        setExpectedOutputFileName("write_Expected.xml");
        getTestHelper().setTestMethodName("testFileWriteDelimited");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
        getTestHelper().evaluateTextFileContents(dataFolder + "output.txt", dataFolder + "delimitedWrite_Expected.txt");
    }

    /**
     * Test write of delimited file with switch Attribute and Element
     */
    public void testFileWriteDelimitedSwitch() {
        clearInputParams();
        setInputXmlFileName("contactList.xml");
        setBizDocFileName("delimitedSwitchWrite.xbd");
        setExpectedOutputFileName("write_Expected.xml");
        getTestHelper().setTestMethodName("testFileWriteDelimitedSwitch");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
        getTestHelper().evaluateTextFileContents(dataFolder + "output.txt", dataFolder + "delimitedSwitchWrite_Expected.txt");
    }

    /**
     * Test simple write of fixed-length field file
     */
    public void testFileWriteFixed() {
        clearInputParams();
        setInputXmlFileName("contactList.xml");
        setBizDocFileName("fixedLengthWrite.xbd");
        setExpectedOutputFileName("write_Expected.xml");
        getTestHelper().setTestMethodName("testFileWriteFixed");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
        getTestHelper().evaluateTextFileContents(dataFolder + "output.txt", dataFolder + "fixedLengthWrite_Expected.txt");
    }

    /**
     * Test simple write of text file
     */
    public void testFileWriteText() {
        clearInputParams();
        setInputXmlFileName("contactList.xml");
        setBizDocFileName("textWrite.xbd");
        setExpectedOutputFileName("write_Expected.xml");
        getTestHelper().setTestMethodName("testFileWriteText");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
        getTestHelper().evaluateTextFileContents(dataFolder + "output.txt", dataFolder + "textWrite_Expected.txt");
    }

    /**
     * Test simple write of XML file - acts the same as testFileWriteText()
     */
    public void testFileWriteXml() {
        clearInputParams();
        setInputXmlFileName("contactList.xml");
        setBizDocFileName("xmlWrite.xbd");
        setExpectedOutputFileName("write_Expected.xml");
        getTestHelper().setTestMethodName("testFileWriteXml");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
        getTestHelper().evaluateTextFileContents(dataFolder + "output.txt", dataFolder + "xmlWrite_Expected.txt");
    }
}
