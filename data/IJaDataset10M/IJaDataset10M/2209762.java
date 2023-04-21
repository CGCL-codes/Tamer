package org.openscience.cdk.applications.taverna.test.qsar.descriptors.atomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CMLChemFile;
import org.openscience.cdk.applications.taverna.basicutilities.FileNameGenerator;
import org.openscience.cdk.applications.taverna.io.CDKIOFileWriter;
import org.openscience.cdk.applications.taverna.qsar.descriptors.atomic.PartialTChargePEOE;
import org.openscience.cdk.applications.taverna.test.CDKTavernaTestCase;
import org.openscience.cdk.applications.taverna.test.CDKTavernaTests;
import org.openscience.cdk.applications.taverna.test.TavernaTestData;
import org.openscience.cdk.exception.CDKException;

/**
 * Class with contains JUnit-Tests for the CDK-Taverna Project
 * @author Thomas
 *
 */
public class PartialTChargePEOETest extends CDKTavernaTestCase {

    private CMLChemFile[] originalData = null;

    private List inputList = null;

    private Map inputMap = null;

    private Map resultMap = null;

    private List resultListCalculatedStructures = null;

    private List resultListNOTCalculatedStructures = null;

    private List DescriptorValue = null;

    private String descriptorName = "PartialTChargePEOE";

    public PartialTChargePEOETest() {
    }

    public static Test suite() {
        return new TestSuite(PartialTChargePEOETest.class);
    }

    public void test_LocalWorkerPartialTChargePEOE() throws CDKException, Exception {
        PartialTChargePEOE test = new PartialTChargePEOE();
        inputList = new ArrayList();
        originalData = TavernaTestData.getCMLChemFile();
        for (int i = 0; i < originalData.length; i++) {
            inputList.add(originalData[i]);
        }
        inputMap = new HashMap();
        inputMap.put(test.inputNames()[0], new DataThing(inputList));
        resultMap = test.execute(inputMap);
        resultListCalculatedStructures = (List) ((DataThing) (resultMap.get(test.outputNames()[0]))).getDataObject();
        resultListNOTCalculatedStructures = (List) ((DataThing) (resultMap.get(test.outputNames()[1]))).getDataObject();
        boolean notCalculatedResults = false;
        if (resultListNOTCalculatedStructures.size() != 0) {
            notCalculatedResults = true;
            CDKIOFileWriter.writeListOfCMLChemFilesToFile(resultListNOTCalculatedStructures, descriptorName + "Problem.txt", TavernaTestData.getPathForWritingFilesOfUnitTests());
        }
        if (resultListCalculatedStructures.size() != 0 && CDKTavernaTests.writeResultsToFile) {
            CDKIOFileWriter.writeListOfCMLChemFilesToFile(resultListCalculatedStructures, descriptorName + ".txt", TavernaTestData.getPathForWritingFilesOfUnitTests());
        }
        boolean hasFileNameAnError = false;
        if (resultListCalculatedStructures.size() != 0) {
            for (Iterator iter = resultListCalculatedStructures.iterator(); iter.hasNext(); ) {
                CMLChemFile cmlChemFile = (CMLChemFile) iter.next();
                List<String> fileName = (List<String>) cmlChemFile.getProperty(FileNameGenerator.FILENAME);
                if (!((String) fileName.get(0)).equalsIgnoreCase("JunitTesting")) {
                    hasFileNameAnError = true;
                }
                if (!((String) fileName.get(fileName.size() - 1)).equalsIgnoreCase("PartialTChargePEOE_Descriptor")) {
                    hasFileNameAnError = true;
                }
            }
        }
        assertEquals(false, hasFileNameAnError);
        assertEquals(false, notCalculatedResults);
    }
}
