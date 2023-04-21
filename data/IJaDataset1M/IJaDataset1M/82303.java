package org.openscience.cdk.applications.taverna.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.jdom.Element;
import org.openscience.cdk.applications.taverna.CDKTavernaTestCases;
import org.openscience.cdk.exception.CDKException;

public class GetHistogramDistributionTest extends CDKTavernaTestCases {

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    public GetHistogramDistributionTest() {
    }

    public static Test suite() {
        return new TestSuite(GetHistogramDistributionTest.class);
    }

    public void test_GetDistributionXML() throws CDKException, Exception {
        GetHistogramDistribution worker = new GetHistogramDistribution();
        String header = "TestHeader";
        String fileName = "TestFile";
        String xAxix = "XTest";
        String yAxix = "YTest";
        worker.setDistributionHeader(header);
        worker.setFileName(fileName);
        worker.setXAxisDescription(xAxix);
        worker.setYAxisDescription(yAxix);
        Element ele = worker.provideXML();
        GetHistogramDistribution worker2 = new GetHistogramDistribution();
        worker2.consumeXML(ele);
        assertEquals(header, worker2.getDistributionHeader());
        assertEquals(fileName, worker2.getFileName());
        assertEquals(xAxix, worker2.getXAxisDescription());
        assertEquals(yAxix, worker2.getYAxisDescription());
    }

    public void test_GetRandomDistribution() throws CDKException, Exception {
        GetHistogramDistribution worker = new GetHistogramDistribution();
        List<Double> distributionValues = new ArrayList<Double>();
        worker.setNumberOfIntervals(200);
        Random random = new Random();
        int counter = 0;
        while (counter < 100) {
            distributionValues.add(random.nextDouble() * 100);
            counter++;
        }
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(worker.inputNames()[0], new DataThing(distributionValues));
        resultMap = worker.execute(inputMap);
        assertNotNull(resultMap);
    }

    public void test_GetRandomDistribution2() throws CDKException, Exception {
        GetHistogramDistribution worker = new GetHistogramDistribution();
        List<Double> distributionValues = new ArrayList<Double>();
        worker.setNumberOfIntervals(200);
        Random random = new Random();
        int counter = 0;
        while (counter < 10000) {
            distributionValues.add(random.nextGaussian() * 100);
            counter++;
        }
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(worker.inputNames()[0], new DataThing(distributionValues));
        resultMap = worker.execute(inputMap);
        assertNotNull(resultMap);
    }

    public void test_GetRandomDistribution3() throws CDKException, Exception {
        GetHistogramDistribution worker = new GetHistogramDistribution();
        List<Double> distributionValues = new ArrayList<Double>();
        worker.setAutomaticRange(false);
        worker.setEndPoint(100);
        worker.setStartingPoint(-0.5);
        worker.setInterval(1);
        worker.setFileName("interval");
        Random random = new Random();
        int counter = 0;
        while (counter < 10000) {
            distributionValues.add(random.nextDouble() * 100);
            counter++;
        }
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(worker.inputNames()[0], new DataThing(distributionValues));
        resultMap = worker.execute(inputMap);
        assertNotNull(resultMap);
    }
}
