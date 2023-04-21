package org.openscience.cdk.applications.taverna.qsar.model.R2.LinearRegressionModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scuflworkers.java.LocalWorker;
import org.openscience.cdk.applications.taverna.CDKTavernaConfig;
import org.openscience.cdk.applications.taverna.LocalWorkerCDK;
import org.openscience.cdk.applications.taverna.io.CDKIOFileWriter;
import org.openscience.cdk.qsar.model.R2.LinearRegressionModel;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Local worker that loads a LinearRegressionModel from a given filename and
 * path To use this Linear regression model you have to installed R and the JRI
 * package. See RModel within the cdk.qsar.model.R2 for more details
 * 
 * @author Thomas Kuhn
 */
public class LRM_LoadModel implements LocalWorker {

    private String[] inputNames = new String[] { "FileName", "Path" };

    private String[] outputNames = new String[] { "ModelFileNameAndPath", "Comment" };

    public String[] inputNames() {
        return inputNames;
    }

    public String[] inputTypes() {
        return new String[] { LocalWorkerCDK.STRING, LocalWorkerCDK.STRING };
    }

    public String[] outputNames() {
        return outputNames;
    }

    public String[] outputTypes() {
        return new String[] { LocalWorkerCDK.STRING, LocalWorkerCDK.STRING };
    }

    public Map execute(Map inputs) throws TaskExecutionException {
        String comment = "";
        String path = null;
        String fileName = null;
        if (inputs.get(inputNames[1]) != null) {
            path = (String) ((DataThing) inputs.get(inputNames[1])).getDataObject();
        } else {
            path = CDKTavernaConfig.getPathOfFileWriting() + File.separator + "RModel";
        }
        if (inputs.get(inputNames[0]) != null) {
            fileName = (String) ((DataThing) inputs.get(inputNames[0])).getDataObject();
        } else {
            comment += "No fileName; ";
        }
        Map outputs = new HashMap();
        try {
            LinearRegressionModel lrm = new LinearRegressionModel();
            String fileNameAndPath = path + File.separator + fileName;
            fileNameAndPath = CDKIOFileWriter.getAbsolutPath(fileNameAndPath);
            lrm.loadModel(fileNameAndPath);
            comment += "Model loaded; ";
            outputs.put(outputNames[0], new DataThing(fileNameAndPath));
        } catch (Exception e) {
            comment = "Error: " + e;
        }
        outputs.put(outputNames[1], new DataThing(comment));
        return outputs;
    }
}
