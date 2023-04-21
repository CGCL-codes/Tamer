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
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * TestSuite that runs a test for the Get number of cluster test
 *
 * @author Thomas Kuhn
 */
public class GetART2AClassificationResultAsPDFTest extends CDKTavernaTestCases {

    /**
	 * Constructor
	 */
    public GetART2AClassificationResultAsPDFTest() {
    }

    /**
	 * A unit test suite for JUnit
	 *
	 * @return The test suite
	 */
    public static Test suite() {
        return new TestSuite(GetART2AClassificationResultAsPDFTest.class);
    }

    @SuppressWarnings("unchecked")
    public void testGetNumberOfCluster() {
        Map<String, DataThing> inputMap;
        List<String> fileNames;
        String path = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + "data" + File.separator + "ART2A" + File.separator + "ART2a_Result_comp.gz";
        GetART2AClassificationResultAsPDF worker = new GetART2AClassificationResultAsPDF();
        inputMap = new HashMap<String, DataThing>();
        fileNames = new ArrayList<String>();
        fileNames.add(path);
        inputMap.put(worker.inputNames()[0], new DataThing(fileNames));
        try {
            Map<String, DataThing> result = worker.execute(inputMap);
            List<String> resultList = (List<String>) ((DataThing) (result.get(worker.outputNames()[0]))).getDataObject();
            ;
            assertNotNull(resultList);
            assertEquals(1, resultList.size());
        } catch (TaskExecutionException e) {
            assertFalse(true);
        }
    }
}
