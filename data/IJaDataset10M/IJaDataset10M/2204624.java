package org.openscience.cdk.applications.taverna.qsar.model.ART2AClassificator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamWriter;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.XScufl;
import org.jdom.Element;
import org.openscience.cdk.applications.art2aClassification.Art2aClassificator;
import org.openscience.cdk.applications.art2aClassification.FingerprintItem;
import org.openscience.cdk.applications.taverna.io.XMLFileIO;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorkerWithPorts;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKProcessor;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.XMLExtensible;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Class which implements a local worker for the cdk-taverna project. 
 * This worker uses an implementation of the ART2A Classificator to classify a given set of fingerprint items.
 * @author Thomas Kuhn
 *
 */
public class ART2AClassificatorWorker implements CDKLocalWorkerWithPorts, XMLExtensible {

    private static final String[] inputNames = new String[] { "fingerprintItemList" };

    private static final String[] outputNames = new String[] { "resultFileNames", "Comment" };

    /**
	 * Boolean which allows a scaling of the input fingerprint between 0 and 1 
	 */
    private boolean scaleFingerprintItemToInternalZeroOne = true;

    /**
	 * The number of classificator to calculate
	 */
    private int numberOfClassificators = 1;

    /**
	 * The upper limit for the vigilance parameter
	 */
    private double upperVigilanceLimit = 0.9;

    /**
	 * The lower limit for the vigilance parameter
	 */
    private double lowerVigilanceLimit = 0.1;

    /**
	 * The maximum classification time for one epoch
	 */
    private int maximumClassificationTime = 15;

    /**
	 * The flag is used to switch between deterministic random and random random.
	 * If mDeterministicRandom = true a seed of 1 is used.
	 * If mDeterministicRandom = false no seed is set explicitly
	 */
    private boolean deterministicRandom = true;

    /**
	 * This flag enables the switch between the two convergence criteria. 
	 * convergenceFlag = true means the clustering is convergent if the angels between two cluster has the required similarity between two epochs.
	 * convergenceFlag = false means the clustering is convergent if the number of cluster have the same composition as in the previous epoch.
	 */
    private boolean convergenceFlag = true;

    /**
	 * The required similarity for the use of the convergence flag.
	 * This is the required similarity used if the convergence flag is set to true.
	 */
    private double requiredSimilarity = 0.99;

    /**
	 * The name of the folder which should contains the result files
	 */
    private String folderName = "";

    private static final String EXTENSIONS = "extensions";

    private static final String ART2ACLASSIFICATOR = "ART2AClassificator";

    private static final String NUMBEROFCLASSIFICATORS = "numberofclassificators";

    private static final String UPPERVIGILANCELIMIT = "uppervigilancelimit";

    private static final String LOWERVIGILANCELIMIT = "lowervigilancelimit";

    private static final String SCALEFINGERPRINT = "scalefingerprint";

    private static final String MAXIMUMCLASSIFICATIONTIME = "maximumclassificationtime";

    private static final String REQUIREDSIMILARITY = "requiredsimilarity";

    private static final String CONVERGENCEFLAG = "convergenceflag";

    private static final String DETERMINISTICRANDOM = "deterministicrandom";

    private static final String ART2AFOLDERNAMES = "art2afoldernames";

    private static final String FOLDERNAME = "foldername";

    /**
	 * Get the number of classificators
	 * @return the numberOfClassificators
	 */
    public int getNumberOfClassificators() {
        return numberOfClassificators;
    }

    /**
	 * Set the number of classificators
	 * @param numberOfClassificators the numberOfClassificators to set
	 */
    public void setNumberOfClassificators(int numberOfClassificators) {
        this.numberOfClassificators = numberOfClassificators;
    }

    /**
	 * Get the upper vigilance limit parameter
	 * @return the upperVigilanceLimit
	 */
    public double getUpperVigilanceLimit() {
        return upperVigilanceLimit;
    }

    /**
	 * Set the upper vigilance limit parameter
	 * @param upperVigilanceLimit the upperVigilanceLimit to set
	 */
    public void setUpperVigilanceLimit(double upperVigilanceLimit) {
        this.upperVigilanceLimit = upperVigilanceLimit;
    }

    /**
	 * Get the lower vigilance limit parameter
	 * @return the lowerVigilanceLimit
	 */
    public double getLowerVigilanceLimit() {
        return lowerVigilanceLimit;
    }

    /**
	 * Set the lower vigilance limit parameter
	 * @param lowerVigilanceLimit the lowerVigilanceLimit to set
	 */
    public void setLowerVigilanceLimit(double lowerVigilanceLimit) {
        this.lowerVigilanceLimit = lowerVigilanceLimit;
    }

    /**
	 * Boolean which allows a scaling of the input fingerprint between 0 and 1 
	 * @return the scaleFingerprintItemToInternalZeroOne
	 */
    public boolean isScaleFingerprintItemToInternalZeroOne() {
        return scaleFingerprintItemToInternalZeroOne;
    }

    /**
	 * Boolean which allows a scaling of the input fingerprint between 0 and 1 
	 * @param scaleFingerprintItemToInternalZeroOne the scaleFingerprintItemToInternalZeroOne to set
	 */
    public void setScaleFingerprintItemToInternalZeroOne(boolean scaleFingerprintItemToInternalZeroOne) {
        this.scaleFingerprintItemToInternalZeroOne = scaleFingerprintItemToInternalZeroOne;
    }

    /**
	 * Get the maximum classification time.
	 * @return the maximumClassificationTime
	 */
    public int getMaximumClassificationTime() {
        return maximumClassificationTime;
    }

    /**
	 * Set the maximum classification time
	 * @param maximumClassificationTime the maximumClassificationTime to set
	 */
    public void setMaximumClassificationTime(int maximumClassificationTime) {
        this.maximumClassificationTime = maximumClassificationTime;
    }

    /**
	 * Get the convergence flag.
	 * @return the convergenceFlag
	 */
    public boolean isConvergenceFlag() {
        return convergenceFlag;
    }

    /**
	 * Set the convergence flag.
	 * @param convergenceFlag the convergenceFlag to set
	 */
    public void setConvergenceFlag(boolean convergenceFlag) {
        this.convergenceFlag = convergenceFlag;
    }

    /**
	 * Get the required similarity.
	 * @return the requiredSimilarity
	 */
    public double getRequiredSimilarity() {
        return requiredSimilarity;
    }

    /**
	 * Set the required similarity
	 * @param requiredSimilarity the requiredSimilarity to set
	 */
    public void setRequiredSimilarity(double requiredSimilarity) {
        this.requiredSimilarity = requiredSimilarity;
    }

    /**
	 * @return the folderName
	 */
    public String getFolderName() {
        return folderName;
    }

    /**
	 * @param folderName the folderName to set
	 */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String[] inputNames() {
        return inputNames;
    }

    public String[] inputTypes() {
        return new String[] { CDKLocalWorker.FINGERPRINTITEMLIST };
    }

    public String[] outputNames() {
        return outputNames;
    }

    public String[] outputTypes() {
        return new String[] { CDKLocalWorker.STRING_ARRAY, CDKLocalWorker.STRING_ARRAY };
    }

    public List<InputPort> inputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<InputPort> input = new ArrayList<InputPort>();
        InputPort inPort;
        for (int i = 0; i < inputNames.length; i++) {
            inPort = new InputPort(processor, inputNames[i]);
            inPort.setSyntacticType(inputTypes()[i]);
            input.add(inPort);
        }
        return input;
    }

    public List<OutputPort> outputPorts(CDKProcessor processor) throws DuplicatePortNameException, PortCreationException {
        List<OutputPort> output = new ArrayList<OutputPort>();
        OutputPort outPort;
        for (int i = 0; i < outputNames.length; i++) {
            outPort = new OutputPort(processor, outputNames[i]);
            outPort.setSyntacticType(outputTypes()[i]);
            output.add(outPort);
        }
        return output;
    }

    /**
	 * Executes this worker which uses an implementation of the ART2A Classificator to classify a given set of fingerprint items.
	 */
    @SuppressWarnings("unchecked")
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        List<FingerprintItem> fingerprintItemList = null;
        List<String> comment = new ArrayList<String>();
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        if (inputs.get(inputNames[0]) != null) {
            fingerprintItemList = (List<FingerprintItem>) ((DataThing) (inputs.get(inputNames[0]))).getDataObject();
        }
        if (fingerprintItemList == null || fingerprintItemList.isEmpty()) {
            comment.add("no entry in list");
            outputs.put(outputNames[0], new DataThing(comment));
            return outputs;
        }
        try {
            FingerprintItem[] fingerprintItemArray = new FingerprintItem[fingerprintItemList.size()];
            fingerprintItemList.toArray(fingerprintItemArray);
            if (this.numberOfClassificators < 1) throw new IllegalArgumentException("The number of clusters must be greater 0.");
            if (this.upperVigilanceLimit < 0 || this.upperVigilanceLimit > 1 || this.upperVigilanceLimit <= this.lowerVigilanceLimit) throw new IllegalArgumentException("The upper limit of the vigilance parameter is out of boundaries.");
            if (this.lowerVigilanceLimit < 0 || this.lowerVigilanceLimit > 1) throw new IllegalArgumentException("The lower limit of the vigilance parameter is out of boundaries.");
            double[] vigilanceParameters = new double[this.numberOfClassificators];
            double interval;
            if (this.numberOfClassificators == 1) interval = 0; else interval = (this.upperVigilanceLimit - this.lowerVigilanceLimit) / (this.numberOfClassificators - 1);
            for (int i = 0; i < this.numberOfClassificators; i++) {
                vigilanceParameters[i] = Math.round((this.lowerVigilanceLimit + i * interval) * 100) / 100.0;
            }
            if (this.scaleFingerprintItemToInternalZeroOne) {
                Art2aClassificator.scaleFingerprintVectorComponentsToIntervalZeroOne(fingerprintItemArray);
            }
            File tempFile;
            XMLStreamWriter writer;
            Art2aClassificator myART;
            XMLFileIO xmlFileIO = new XMLFileIO();
            List<String> fileNames = new ArrayList<String>(this.numberOfClassificators);
            for (int i = 0; i < this.numberOfClassificators; i++) {
                myART = new Art2aClassificator(fingerprintItemArray, vigilanceParameters[i]);
                myART.setMaximumClassificationTime(maximumClassificationTime);
                myART.setConvergenceFlag(this.convergenceFlag);
                myART.setRequiredSimilarity(this.requiredSimilarity);
                myART.setDeterministicRandom(this.deterministicRandom);
                myART.classify();
                if (this.folderName == null || this.folderName.length() == 0) {
                    tempFile = File.createTempFile("ART2a_Result" + String.valueOf(i) + "of" + String.valueOf(this.numberOfClassificators) + "Classifications", ".gz");
                } else {
                    tempFile = File.createTempFile("ART2a_Result" + String.valueOf(i) + "of" + String.valueOf(this.numberOfClassificators) + "Classifications", ".gz", new File(this.folderName));
                }
                writer = xmlFileIO.getXMLStreamWriterWithCompression(tempFile);
                writer.writeStartDocument();
                myART.saveResultToXmlWriter(writer);
                writer.writeEndDocument();
                writer.close();
                fileNames.add(tempFile.getAbsolutePath());
                xmlFileIO.closeXMLStreamWriter();
            }
            outputs.put(outputNames[0], new DataThing(fileNames));
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        outputs.put(outputNames[1], new DataThing(comment));
        return outputs;
    }

    public void consumeXML(Element element) {
        Element ele = element.getChild(ART2ACLASSIFICATOR, XScufl.XScuflNS);
        if (ele == null) {
            return;
        }
        this.scaleFingerprintItemToInternalZeroOne = Boolean.valueOf(ele.getAttributeValue(SCALEFINGERPRINT));
        this.lowerVigilanceLimit = Double.valueOf(ele.getAttributeValue(LOWERVIGILANCELIMIT));
        this.upperVigilanceLimit = Double.valueOf(ele.getAttributeValue(UPPERVIGILANCELIMIT));
        this.numberOfClassificators = Integer.valueOf(ele.getAttributeValue(NUMBEROFCLASSIFICATORS));
        this.maximumClassificationTime = Integer.valueOf(ele.getAttributeValue(MAXIMUMCLASSIFICATIONTIME));
        this.convergenceFlag = Boolean.valueOf(ele.getAttributeValue(CONVERGENCEFLAG));
        this.requiredSimilarity = Double.valueOf(ele.getAttributeValue(REQUIREDSIMILARITY));
        this.deterministicRandom = Boolean.valueOf(ele.getAttributeValue(DETERMINISTICRANDOM));
        ele = null;
        ele = element.getChild(ART2AFOLDERNAMES, XScufl.XScuflNS);
        if (ele == null) {
            return;
        }
        this.folderName = String.valueOf(ele.getAttributeValue(FOLDERNAME));
    }

    public Element provideXML() {
        Element extensions = new Element(EXTENSIONS, XScufl.XScuflNS);
        Element insertElement = new Element(ART2ACLASSIFICATOR, XScufl.XScuflNS);
        insertElement.setAttribute(SCALEFINGERPRINT, String.valueOf(this.scaleFingerprintItemToInternalZeroOne));
        insertElement.setAttribute(UPPERVIGILANCELIMIT, String.valueOf(this.upperVigilanceLimit));
        insertElement.setAttribute(LOWERVIGILANCELIMIT, String.valueOf(this.lowerVigilanceLimit));
        insertElement.setAttribute(NUMBEROFCLASSIFICATORS, String.valueOf(this.numberOfClassificators));
        insertElement.setAttribute(MAXIMUMCLASSIFICATIONTIME, String.valueOf(this.maximumClassificationTime));
        insertElement.setAttribute(CONVERGENCEFLAG, String.valueOf(this.convergenceFlag));
        insertElement.setAttribute(REQUIREDSIMILARITY, String.valueOf(this.requiredSimilarity));
        insertElement.setAttribute(DETERMINISTICRANDOM, String.valueOf(this.deterministicRandom));
        extensions.addContent(insertElement);
        Element fileName = new Element(ART2AFOLDERNAMES, XScufl.XScuflNS);
        fileName.setAttribute(FOLDERNAME, this.folderName);
        extensions.addContent(fileName);
        return extensions;
    }
}
