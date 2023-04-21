package org.openscience.cdk.applications.taverna.qsar.descriptors.molecular;

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
import org.openscience.cdk.applications.taverna.basicutilities.CMLChemFileWrapper;
import org.openscience.cdk.applications.taverna.basicutilities.FileNameGenerator;
import org.openscience.cdk.applications.taverna.io.CDKIOFileWriter;
import org.openscience.cdk.exception.CDKException;

/**
 * Class with contains JUnit-Tests for the CDK-Taverna Project
 * @author Thomas Kuhn
 *
 */
public class RuleOfFiveTest extends CDKTavernaTestCases {

    private CMLChemFile[] originalData = null;

    private List<CMLChemFile> inputList = null;

    private Map<String, DataThing> inputMap = null;

    private Map<String, DataThing> resultMap = null;

    private List<CMLChemFile> resultListMatchedStructures = null;

    private List<CMLChemFile> resultListOtherStructures = null;

    private String descriptorName = "RuleOfFive_Descriptor";

    public RuleOfFiveTest() {
    }

    public static Test suite() {
        return new TestSuite(RuleOfFiveTest.class);
    }

    @SuppressWarnings("unchecked")
    public void test_LocalWorkerRuleOfFive() throws CDKException, Exception {
        RuleOfFiveFilter test = new RuleOfFiveFilter();
        inputList = new ArrayList<CMLChemFile>();
        originalData = CDKTavernaTestData.getCMLChemFile();
        for (int i = 0; i < originalData.length; i++) {
            inputList.add(originalData[i]);
        }
        inputMap = new HashMap<String, DataThing>();
        inputMap.put(test.inputNames()[0], new DataThing(inputList));
        resultMap = test.execute(inputMap);
        resultListMatchedStructures = CMLChemFileWrapper.getListOfCMLChemfileFromDataThing(resultMap.get(test.outputNames()[0]));
        resultListOtherStructures = CMLChemFileWrapper.getListOfCMLChemfileFromDataThing(resultMap.get(test.outputNames()[1]));
        boolean notCalculatedResults = false;
        if (resultListOtherStructures.size() != 3) {
            notCalculatedResults = true;
            CDKIOFileWriter.writeListOfCMLChemFilesToFile(resultListOtherStructures, "Error_" + descriptorName + ".txt", CDKTavernaTestData.getPathForWritingFilesOfUnitTests(false));
        }
        boolean hasFileNameAnError = false;
        if (resultListMatchedStructures.size() != 0) {
            for (CMLChemFile cmlChemFile : this.resultListMatchedStructures) {
                List<String> fileName = (List<String>) cmlChemFile.getProperty(FileNameGenerator.FILENAME);
                if (!((String) fileName.get(0)).equalsIgnoreCase("JunitTesting")) {
                    hasFileNameAnError = true;
                }
                if (!((String) fileName.get(fileName.size() - 1)).equalsIgnoreCase("RuleOfFive_Filter_Matched")) {
                    hasFileNameAnError = true;
                }
            }
        }
        assertFalse(hasFileNameAnError);
        assertFalse(notCalculatedResults);
    }
}
