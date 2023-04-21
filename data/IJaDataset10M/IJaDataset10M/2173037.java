package org.openscience.cdk.applications.taverna.isomorphism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CDKTavernaTestCases;
import org.openscience.cdk.applications.taverna.CDKTavernaTestData;
import org.openscience.cdk.applications.taverna.CMLChemFile;
import org.openscience.cdk.exception.CDKException;

public class IsomorphismTesterTest extends CDKTavernaTestCases {

    private CMLChemFile[] originalData = null;

    private List<CMLChemFile> inputList = null;

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    public IsomorphismTesterTest() {
    }

    ;

    public static Test suite() {
        return new TestSuite(IsomorphismTesterTest.class);
    }

    @SuppressWarnings("unchecked")
    public void test_IsomorphismTester() throws CDKException, Exception {
        IsomorphismTester test = new IsomorphismTester();
        inputList = new ArrayList<CMLChemFile>();
        originalData = CDKTavernaTestData.getCMLChemFile();
        for (int i = 0; i < originalData.length; i++) {
            inputList.add(originalData[i]);
        }
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(test.inputNames()[0], new DataThing(inputList));
        inputMap.put(test.inputNames()[1], new DataThing(inputList.subList(0, 1)));
        resultMap = test.execute(inputMap);
        List<String> resultList = (List<String>) ((DataThing) (resultMap.get(test.outputNames()[0]))).getDataObject();
        List<String> databaseIDList = (List<String>) ((DataThing) (resultMap.get(test.outputNames()[1]))).getDataObject();
        assertNotNull(resultList);
        assertEquals(resultList.get(0), "true");
        assertEquals(resultList.size(), 10);
        for (int i = 1; i < resultList.size(); i++) {
            assertEquals(resultList.get(i), "false");
        }
        assertNotNull(databaseIDList);
        assertEquals(1, databaseIDList.size());
    }
}
