package org.openscience.cdk.applications.taverna.io.iterative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.XScufl;
import org.jdom.Attribute;
import org.jdom.Element;
import org.openscience.cdk.applications.taverna.registry.Registry;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorkerWithPorts;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKProcessor;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.XMLExtensible;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Class which implements a local worker for the cdk-taverna plugin which provides the 
 * possibility to read text files and adds an
 * action to the processor menu which allows the choosing of the a file from within a filechooser
 * 
 * @author Thomas Kuhn
 */
public class IteratingFileReader implements CDKLocalWorker, CDKLocalWorkerWithPorts, XMLExtensible {

    /**
	 * String array which contains the names for the inputs of the worker
	 */
    private static final String[] INPUTNAMES = new String[] { "fileName" };

    /**
	 * String array which contains the names for the output of the worker
	 */
    private static final String[] OUTPUTNAMES = new String[] { "readerID", "comment" };

    /**
	 * String contains the chosen filename and path
	 */
    private String fileName = "";

    /**
	 * String which contains xml extension tag for saving the chosen file name to the workflow
	 */
    private static final String EXTENSIONS = "extensions";

    /**
	 * String which contains xml extension tag for saving the chosen file name to the workflow
	 */
    private static final String FILEREADER = "filereader";

    /**
	 * String which contains xml extension tag for saving the chosen file name to the workflow
	 */
    private static final String FILENAME = "filename";

    /**
	 * Returns the current file name
	 * @return filename
	 */
    public String getFileName() {
        return this.fileName;
    }

    /**
	 * Sets the file name
	 * @param fileName
	 * 			fileName
	 */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
	 * Returns the workers input names
	 * @return 	Names of the worker inputs
	 */
    public String[] inputNames() {
        return INPUTNAMES;
    }

    /**
	 * Returns the workers input types
	 * @return Returns the workers input types
	 */
    public String[] inputTypes() {
        return new String[] { CDKLocalWorker.STRING_ARRAY };
    }

    /**
	 * Returns the workers output names
	 * @return 	Names of the worker output
	 */
    public String[] outputNames() {
        return OUTPUTNAMES;
    }

    /**
	 * Returns the workers output types
	 * @return Returns the workers output types
	 */
    public String[] outputTypes() {
        return new String[] { CDKLocalWorker.STRING, CDKLocalWorker.STRING_ARRAY };
    }

    /**
	 * Returns a list of InputPort from this worker
	 * @param processor
	 * 			Processor for which the List of InputPorts should be returned
	 * @return Returns a list of InputPort from this worker
	 * @throws DuplicatePortNameException
	 * 			Two ports with identical names
	 * @throws PortCreationException
	 * 			Fail to create the new port
	 */
    public List<InputPort> inputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<InputPort> input = new ArrayList<InputPort>();
        InputPort inPort;
        for (int i = 0; i < INPUTNAMES.length; i++) {
            inPort = new InputPort(processor, INPUTNAMES[i]);
            inPort.setSyntacticType(inputTypes()[i]);
            input.add(inPort);
        }
        return input;
    }

    /**
	 * Returns a list of OutputPorts from this worker
	 * @param processor
	 * 			Processor for which the List of OutputPorts should be returned
	 * @return Returns a list of OutputPorts from this worker
	 * @throws DuplicatePortNameException
	 * 			Two ports with identical names
	 * @throws PortCreationException
	 * 			Fail to create the new port
	 */
    public List<OutputPort> outputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<OutputPort> output = new ArrayList<OutputPort>();
        OutputPort outPort;
        for (int i = 0; i < OUTPUTNAMES.length; i++) {
            outPort = new OutputPort(processor, OUTPUTNAMES[i]);
            outPort.setSyntacticType(outputTypes()[i]);
            output.add(outPort);
        }
        return output;
    }

    /**
	 * Method which executes this worker
	 * @param inputs
	 * 			Map which contains the input values
	 * @return Returns a map which contains the results
	 * @throws TaskExecutionException
	 * 			Execution of the worker cause an exception
	 */
    @SuppressWarnings("unchecked")
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        List<String> inputList = new ArrayList<String>(10);
        List<String> comment = new ArrayList<String>(10);
        String readerID = null;
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        if (inputs.get(INPUTNAMES[0]) != null) {
            inputList = (List<String>) ((DataThing) (inputs.get(INPUTNAMES[0]))).getDataObject();
        }
        if (this.fileName != null && this.fileName.length() != 0) {
            inputList.add(this.fileName);
        }
        if (inputList.isEmpty()) {
            comment.add("no entry in list");
            outputs.put(OUTPUTNAMES[0], new DataThing(comment));
            return outputs;
        }
        try {
            IteratingReader reader;
            UUID id;
            for (String file : inputList) {
                reader = new IteratingReader(new java.io.FileReader(file));
                id = UUID.randomUUID();
                Registry.addObjectToRegistry(id, reader);
                readerID = id.toString();
            }
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        outputs.put(OUTPUTNAMES[0], new DataThing(readerID));
        outputs.put(OUTPUTNAMES[1], new DataThing(comment));
        return outputs;
    }

    /**
	 * Method which gets a xml element and saves the information to this processor
	 * @param element
	 * 			XML element which contains the information for this worker
	 */
    public void consumeXML(Element element) {
        Element fileName = element.getChild(FILEREADER, XScufl.XScuflNS);
        if (fileName == null) {
            return;
        }
        Attribute completeFileName = fileName.getAttribute(FILENAME, XScufl.XScuflNS);
        if (completeFileName == null) {
            return;
        }
        try {
            this.fileName = completeFileName.getValue();
        } catch (Exception ex) {
        }
    }

    /**
	 * Method which provides a XML element to save the worker with its current status
	 * @return Returns a XML element
	 */
    public Element provideXML() {
        Element extensions = new Element(EXTENSIONS, XScufl.XScuflNS);
        Element fileNameList = new Element(FILEREADER, XScufl.XScuflNS);
        if (this.fileName == null || this.fileName.length() == 0) {
            this.fileName = "";
        }
        fileNameList.setAttribute(FILENAME, this.fileName, XScufl.XScuflNS);
        extensions.addContent(fileNameList);
        return extensions;
    }
}
