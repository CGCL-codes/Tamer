package org.openscience.cdk.applications.taverna.qsar.model.ART2AClassificator.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CDKTavernaTestCases;
import org.openscience.cdk.applications.taverna.qsar.model.ART2AClassificator.tools.GetInterAngleBetweenClassesAsCSV;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * TestSuite that runs a test for the GetInterAngleBetweenClassesAsCSV 
 *
 * @author Thomas Kuhn
 */
public class GetInterAngleBetweenClassesAsCSVTest extends CDKTavernaTestCases {

    /**
	 * Constructor
	 */
    public GetInterAngleBetweenClassesAsCSVTest() {
    }

    /**
	 * A unit test suite for JUnit
	 *
	 * @return The test suite
	 */
    public static Test suite() {
        return new TestSuite(GetInterAngleBetweenClassesAsCSVTest.class);
    }

    /**
	 * This test loads a stored ART2A classification result and extracts the interangle from the classification and stores the result as list of strings
	 */
    @SuppressWarnings("unchecked")
    public void testGetInterAngleBetweenClassesAsCSV() {
        Map<String, DataThing> inputMap;
        List<String> fileNames;
        String path = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + "data" + File.separator + "ART2A" + File.separator + "ART2a_Result_comp.gz";
        GetInterAngleBetweenClassesAsCSV interAngleWorker = new GetInterAngleBetweenClassesAsCSV();
        inputMap = new HashMap<String, DataThing>();
        fileNames = new ArrayList<String>();
        fileNames.add(path);
        inputMap.put(interAngleWorker.inputNames()[0], new DataThing(fileNames));
        try {
            Map<String, DataThing> result = interAngleWorker.execute(inputMap);
            List<String> resultList = (List<String>) ((DataThing) (result.get(interAngleWorker.outputNames()[0]))).getDataObject();
            ;
            assertNotNull(resultList);
            assertEquals(13, resultList.size());
            String resultFile = "FileName; ." + File.separator + "target" + File.separator + "test-classes" + File.separator + "data" + File.separator + "ART2A" + File.separator + "ART2a_Result_comp.gz";
            assertEquals(resultFile, resultList.get(0));
            assertEquals("Vigilance parameter; 0.8", resultList.get(1));
            assertEquals("3;;;;0;", resultList.get(12));
        } catch (TaskExecutionException e) {
            assertFalse(true);
        }
    }
}
