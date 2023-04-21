package org.openscience.cdk.applications.taverna.aromaticity;

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

public class DetectHueckelAromaticityTest extends CDKTavernaTestCases {

    private CMLChemFile[] originalData = null;

    private List<CMLChemFile> inputList = null;

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    private String[] expectedDefaultResults = null;

    public DetectHueckelAromaticityTest() {
    }

    ;

    public static Test suite() {
        return new TestSuite(DetectHueckelAromaticityTest.class);
    }

    @SuppressWarnings("unchecked")
    public void test_DetectHueckelAromaticity() throws CDKException, Exception {
        DetectHueckelAromaticity test = new DetectHueckelAromaticity();
        inputList = new ArrayList<CMLChemFile>();
        originalData = CDKTavernaTestData.getCMLChemFile();
        for (int i = 0; i < originalData.length; i++) {
            inputList.add(originalData[i]);
        }
        expectedDefaultResults = CDKTavernaTestData.getAromaticityOfDefaultTestData();
        inputMap = new HashMap<String, DataThing>();
        inputMap.put("inputStructures", new DataThing(inputList));
        resultMap = test.execute(inputMap);
        List<String> isAromaticBooleanResult = (List<String>) ((DataThing) (resultMap.get("isAromaticBooleanResult"))).getDataObject();
        for (int i = 0; i < isAromaticBooleanResult.size(); i++) {
            assertEquals(true, expectedDefaultResults[i].equalsIgnoreCase((String) isAromaticBooleanResult.get(i)));
        }
    }
}
