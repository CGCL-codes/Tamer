package org.openscience.cdk.applications.taverna.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CDKTavernaTestCases;
import org.openscience.cdk.applications.taverna.io.ReadMDLSDFileAsDatabaseInput;
import org.openscience.cdk.exception.CDKException;

public class ExtractIDFromMolFileTest extends CDKTavernaTestCases {

    private List<String> inputList = null;

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    public ExtractIDFromMolFileTest() {
    }

    public static Test suite() {
        return new TestSuite(ExtractIDFromMolFileTest.class);
    }

    @SuppressWarnings("unchecked")
    public void test_RemoveMOLFilesWithoutStructureFromList() throws CDKException, Exception {
        ReadMDLSDFileAsDatabaseInput test = new ReadMDLSDFileAsDatabaseInput();
        inputList = new ArrayList<String>();
        String path = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + "data" + File.separator + "sdf" + File.separator + "SDFileDemoWithoutStructures.sdf";
        inputList.add(path);
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(test.inputNames()[0], new DataThing(inputList));
        resultMap = test.execute(inputMap);
        inputList = null;
        inputList = (List<String>) ((DataThing) resultMap.get(test.outputNames()[0])).getDataObject();
        assertNotNull(inputList);
        ExtractIDFromMolFile extractor = new ExtractIDFromMolFile();
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(extractor.inputNames()[0], new DataThing(inputList));
        resultMap = extractor.execute(inputMap);
        List<String> listOfMolecules = (List<String>) ((DataThing) resultMap.get(extractor.outputNames()[0])).getDataObject();
        assertNotNull(listOfMolecules);
        assertEquals(4, listOfMolecules.size());
        List<String> listOfIDs = (List<String>) ((DataThing) resultMap.get(extractor.outputNames()[1])).getDataObject();
        assertNotNull(listOfIDs);
        assertEquals(4, listOfIDs.size());
        for (int i = 0; i < listOfMolecules.size(); i++) {
            assertTrue(listOfMolecules.get(i).contains(listOfIDs.get(i)));
        }
    }

    @SuppressWarnings("unchecked")
    public void test_RemoveMOLFilesWithoutStructureFromList2() throws CDKException, Exception {
        ReadMDLSDFileAsDatabaseInput test = new ReadMDLSDFileAsDatabaseInput();
        inputList = new ArrayList<String>();
        String path = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + "data" + File.separator + "sdf" + File.separator + "starliteMols.sdf";
        inputList.add(path);
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(test.inputNames()[0], new DataThing(inputList));
        resultMap = test.execute(inputMap);
        inputList = null;
        inputList = (List<String>) ((DataThing) resultMap.get(test.outputNames()[0])).getDataObject();
        assertNotNull(inputList);
        ExtractIDFromMolFile extractor = new ExtractIDFromMolFile();
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(extractor.inputNames()[0], new DataThing(inputList));
        resultMap = extractor.execute(inputMap);
        List<String> listOfMolecules = (List<String>) ((DataThing) resultMap.get(extractor.outputNames()[0])).getDataObject();
        assertNotNull(listOfMolecules);
        assertEquals(3, listOfMolecules.size());
        List<String> listOfIDs = (List<String>) ((DataThing) resultMap.get(extractor.outputNames()[1])).getDataObject();
        assertNotNull(listOfIDs);
        assertEquals(3, listOfIDs.size());
        for (int i = 0; i < listOfMolecules.size(); i++) {
            assertTrue(listOfMolecules.get(i).contains(listOfIDs.get(i)));
        }
    }
}
