package org.openscience.cdk.applications.taverna.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CMLChemFile;
import org.openscience.cdk.applications.taverna.LocalWorkerCDK;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.applications.taverna.basicutilities.FileNameGenerator;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

public class CreatePNGWith2DStructures implements LocalWorkerCDK {

    private String[] inputNames = new String[] { "inputStructures" };

    private String[] outputNames = new String[] { "Comment" };

    public String[] inputNames() {
        return inputNames;
    }

    public String[] inputTypes() {
        return new String[] { LocalWorkerCDK.CMLChemFileList };
    }

    public String[] outputNames() {
        return outputNames;
    }

    public String[] outputTypes() {
        return new String[] { LocalWorkerCDK.STRING_ARRAY };
    }

    public Map execute(Map inputs) throws TaskExecutionException {
        FileNameGenerator fileNameGenerator = new FileNameGenerator();
        boolean tab = false;
        String ofor = "PNG";
        List inputList = null;
        List comment = new ArrayList();
        if (inputs.get(inputNames[0]) != null) {
            inputList = (List) ((DataThing) (inputs.get(inputNames[0]))).getDataObject();
        } else {
            return null;
        }
        try {
            for (Iterator iter = inputList.iterator(); iter.hasNext(); ) {
                CMLChemFile file = (CMLChemFile) iter.next();
                String fileName = fileNameGenerator.generateFileNameAndPathFromList((List<String>) file.getProperty(fileNameGenerator.FILENAME), "");
                List moleculeList = ChemFileManipulator.getAllAtomContainers(file);
                try {
                    Draw2DStructure.draw(moleculeList, tab, ofor, fileName);
                    comment.add("done");
                } catch (Exception e) {
                    comment.add("Error");
                }
            }
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        Map outputs = new HashMap();
        outputs.put(outputNames[0], new DataThing(comment));
        return outputs;
    }
}
