package org.openscience.cdk.applications.taverna.io.iterative;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.registry.Registry;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Local Worker for the cdk-taverna plug-in which is used for the iterative file reader. 
 * This worker reads the next content of the file.
 * @author Thomas Kuhn
 */
public class IteratingFileReaderGetContent implements CDKLocalWorker {

    /**
	 * Name of the input ports of this Worker
	 */
    private String[] inputNames = new String[] { "readerID" };

    /**
	 * Name of the output ports of this Worker
	 */
    private String[] outputNames = new String[] { "content" };

    /**
	 * Get the names of the input ports of this Worker
	 * @return the names of the input ports
	 */
    public String[] inputNames() {
        return inputNames;
    }

    /**
	 * Get the names of the input port types of this Worker
	 * @return the names of the input port types
	 */
    public String[] inputTypes() {
        return new String[] { CDKLocalWorker.STRING };
    }

    /**
	 * Get the names of the output ports of this Worker
	 * @return the names of the output ports
	 */
    public String[] outputNames() {
        return outputNames;
    }

    /**
	 * Get the names of the output port types of this Worker
	 * @return the names of the output port types
	 */
    public String[] outputTypes() {
        return new String[] { CDKLocalWorker.STRING_ARRAY };
    }

    /**
	 * Executes this worker. 
	 * @param inputs
	 * 			input map of this worker
	 * @throws TaskExecutionException if an error during the work of the iterating file reader
	 * @return The result map of this worker
	 */
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        String readerID = null;
        List<String> content = null;
        if (inputs.get(inputNames[0]) != null) {
            readerID = (String) ((DataThing) (inputs.get(inputNames[0]))).getDataObject();
        } else {
            return null;
        }
        try {
            IteratingReader reader = (IteratingReader) Registry.getObject(UUID.fromString(readerID));
            content = reader.next();
        } catch (Exception e) {
            throw new TaskExecutionException("An error occur during the iterating file reader process! ", e);
        }
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        outputs.put(outputNames[0], new DataThing(content));
        return outputs;
    }
}
