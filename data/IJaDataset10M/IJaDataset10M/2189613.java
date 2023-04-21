package org.openscience.cdk.applications.taverna.qsar.model.weka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.art2aClassification.FingerprintItem;
import org.openscience.cdk.applications.taverna.CDKTavernaTestCases;
import org.openscience.cdk.applications.taverna.CDKTavernaTestData;
import org.openscience.cdk.applications.taverna.database.pgchem.DBConnector;
import org.openscience.cdk.applications.taverna.database.pgchem.GetQSARVectorFromDB;
import org.openscience.cdk.applications.taverna.qsar.model.ART2AClassificator.tools.GenerateRandomCentroidVectors;

/**
 * TestSuite that runs a test for the GenerateKmeanCluster
 *
 * @author Thomas Kuhn
 */
@SuppressWarnings("unchecked")
public class SimpleKMeansWorkerTest extends CDKTavernaTestCases {

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    /**
	 * Constructor of the GenerateKmeanClusterTest
	 */
    public SimpleKMeansWorkerTest() {
    }

    /**
	 * A unit test suite for JUnit
	 *
	 * @return The test suite
	 */
    public static Test suite() {
        return new TestSuite(SimpleKMeansWorkerTest.class);
    }

    public void test_SimpleKMeansWorkerCluster_Default() throws Exception {
        SimpleKMeansWorker worker = new SimpleKMeansWorker();
        List<String> options = new ArrayList<String>();
        options.add("-N");
        options.add("10");
        GenerateRandomCentroidVectors generateVector = new GenerateRandomCentroidVectors();
        inputMap = new HashMap<String, DataThing>();
        String numberOfVectorsPerClass = "10";
        String numberOfClasses = "10";
        String vectorDimension = "15";
        String vectorLength = "15";
        inputMap.put(generateVector.inputNames()[0], new DataThing(numberOfClasses));
        inputMap.put(generateVector.inputNames()[1], new DataThing(numberOfVectorsPerClass));
        inputMap.put(generateVector.inputNames()[2], new DataThing(vectorDimension));
        inputMap.put(generateVector.inputNames()[3], new DataThing(vectorLength));
        Map<String, DataThing> result = generateVector.execute(inputMap);
        List<FingerprintItem> fingerprintVectorList = (List<FingerprintItem>) ((DataThing) (result.get(generateVector.outputNames()[0]))).getDataObject();
        List<String> descriptorNames = (List<String>) ((DataThing) (result.get(generateVector.outputNames()[1]))).getDataObject();
        inputMap.clear();
        inputMap.put(worker.inputNames()[0], new DataThing(fingerprintVectorList));
        inputMap.put(worker.inputNames()[1], new DataThing(descriptorNames));
        inputMap.put(worker.inputNames()[2], new DataThing(options));
        result = worker.execute(inputMap);
        List<String> fileNames = (List<String>) ((DataThing) (result.get(worker.outputNames()[0]))).getDataObject();
        assertNotNull(fileNames);
        assertEquals(2, fileNames.size());
    }

    public void test_SimpleKMeansWorkerCluster_QSAR() throws Exception {
        String sqlStatement = "SELECT * FROM qsarresults WHERE moleculeid < 10 ";
        DBConnector connector = CDKTavernaTestData.getDatabaseConnector(sqlStatement);
        GetQSARVectorFromDB worker = new GetQSARVectorFromDB();
        worker.setDbConnector(connector);
        worker.setCleanFingerprintVector(true);
        inputMap = new HashMap<String, DataThing>();
        resultMap = worker.execute(inputMap);
        List<FingerprintItem> fingerprintItemList = (List<FingerprintItem>) ((DataThing) resultMap.get(worker.outputNames()[0])).getDataObject();
        List<String> usableColumnNames = (List<String>) ((DataThing) resultMap.get(worker.outputNames()[2])).getDataObject();
        SimpleKMeansWorker clusterworker = new SimpleKMeansWorker();
        List<String> options = new ArrayList<String>();
        options.add("-N");
        options.add("30");
        inputMap.clear();
        inputMap.put(clusterworker.inputNames()[0], new DataThing(fingerprintItemList));
        inputMap.put(clusterworker.inputNames()[1], new DataThing(usableColumnNames));
        inputMap.put(clusterworker.inputNames()[2], new DataThing(options));
        resultMap = clusterworker.execute(inputMap);
        List<String> fileNames = (List<String>) ((DataThing) (resultMap.get(clusterworker.outputNames()[0]))).getDataObject();
        assertNotNull(fileNames);
        assertEquals(2, fileNames.size());
    }

    public void test_SimpleKMeansWorkerCluster_Range() throws Exception {
        SimpleKMeansWorker worker = new SimpleKMeansWorker();
        worker.setUseClusterRange(true);
        worker.setRangeFrom(5);
        worker.setRangeTo(7);
        GenerateRandomCentroidVectors generateVector = new GenerateRandomCentroidVectors();
        inputMap = new HashMap<String, DataThing>();
        String numberOfVectorsPerClass = "10";
        String numberOfClasses = "10";
        String vectorDimension = "15";
        String vectorLength = "15";
        inputMap.put(generateVector.inputNames()[0], new DataThing(numberOfClasses));
        inputMap.put(generateVector.inputNames()[1], new DataThing(numberOfVectorsPerClass));
        inputMap.put(generateVector.inputNames()[2], new DataThing(vectorDimension));
        inputMap.put(generateVector.inputNames()[3], new DataThing(vectorLength));
        Map<String, DataThing> result = generateVector.execute(inputMap);
        List<FingerprintItem> fingerprintVectorList = (List<FingerprintItem>) ((DataThing) (result.get(generateVector.outputNames()[0]))).getDataObject();
        List<String> descriptorNames = (List<String>) ((DataThing) (result.get(generateVector.outputNames()[1]))).getDataObject();
        inputMap.clear();
        inputMap.put(worker.inputNames()[0], new DataThing(fingerprintVectorList));
        inputMap.put(worker.inputNames()[1], new DataThing(descriptorNames));
        result = worker.execute(inputMap);
        List<String> fileNames = (List<String>) ((DataThing) (result.get(worker.outputNames()[0]))).getDataObject();
        assertNotNull(fileNames);
        assertEquals(3, fileNames.size());
    }
}
