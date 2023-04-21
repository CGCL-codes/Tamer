package org.openscience.cdk.applications.taverna.qsar.model.weka;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.XScufl;
import org.jdom.Element;
import org.openscience.cdk.applications.art2aClassification.Art2aClassificator;
import org.openscience.cdk.applications.art2aClassification.FingerprintItem;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorkerWithPorts;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKProcessor;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.XMLExtensible;
import org.openscience.cdk.libio.weka.Weka;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.filters.Filter;

/**
 * Class which implements a local worker for the cdk-taverna project. 
 * This worker uses an implementation of the WEKA SimpleKmeans clusterer to classify a given set of fingerprint items.
 * @author Thomas Kuhn
 *
 */
public class SimpleKMeansWorker implements CDKLocalWorkerWithPorts, XMLExtensible {

    private static final String[] inputNames = new String[] { "fingerprintItemList", "descriptorNames", "options" };

    private static final String[] outputNames = new String[] { "resultFileNames", "Comment" };

    /**
	 * Boolean which allows a scaling of the input fingerprint between 0 and 1 
	 */
    private boolean scaleFingerprintItemToInternalZeroOne = true;

    /**
	 * Remove the identifier from the arff construction of the fingerprint item list
	 */
    private boolean removeIdentifier = true;

    /**
	 * Use the range of cluster instead of only a single cluster value
	 */
    private boolean useClusterRange = false;

    /**
	 * The range for the number of cluster to build
	 */
    private int rangeFrom = 0;

    /**
	 * The range for the number of cluster to build
	 */
    private int rangeTo = 0;

    /**
	 * The name of the folder which should contains the result files
	 */
    private String folderName = "";

    private static final String EXTENSIONS = "extensions";

    private static final String SIMPLEKMEANSCLUSTERER = "SimpleKMeansClusterer";

    private static final String SCALEFINGERPRINT = "scalefingerprint";

    private static final String REMOVEIDENTIFIER = "removeIdentifier";

    private static final String USECLUSTERRANGE = "useclusterrange";

    private static final String RANGEFROM = "clusterrangeFrom";

    private static final String RANGETO = "clusterrangeTo";

    private static final String RESULTFOLDERNAMES = "simplekmeansfoldername";

    private static final String FOLDERNAME = "foldername";

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
	 * @return the removeIdentifier
	 */
    public boolean isRemoveIdentifier() {
        return removeIdentifier;
    }

    /**
	 * @param removeIdentifier the removeIdentifier to set
	 */
    public void setRemoveIdentifier(boolean removeIdentifier) {
        this.removeIdentifier = removeIdentifier;
    }

    /**
	 * @return the useClusterRange
	 */
    public boolean isUseClusterRange() {
        return useClusterRange;
    }

    /**
	 * @param useClusterRange the useClusterRange to set
	 */
    public void setUseClusterRange(boolean useClusterRange) {
        this.useClusterRange = useClusterRange;
    }

    /**
	 * @return the rangeFrom
	 */
    public int getRangeFrom() {
        return rangeFrom;
    }

    /**
	 * @param rangeFrom the rangeFrom to set
	 */
    public void setRangeFrom(int rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    /**
	 * @return the rangeTo
	 */
    public int getRangeTo() {
        return rangeTo;
    }

    /**
	 * @param rangeTo the rangeTo to set
	 */
    public void setRangeTo(int rangeTo) {
        this.rangeTo = rangeTo;
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
        return new String[] { CDKLocalWorker.FINGERPRINTITEMLIST, CDKLocalWorker.STRING_ARRAY, CDKLocalWorker.STRING_ARRAY };
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
	 * This worker uses an implementation of the WEKA SimpleKmeans clusterer to classify a given set of fingerprint items..
	 */
    @SuppressWarnings("unchecked")
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        List<FingerprintItem> fingerprintItemList = null;
        List<String> comment = new ArrayList<String>();
        List<String> descriptorNames = null;
        List<String> options = null;
        List<String> resultFileNames = new ArrayList<String>(2);
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        if (inputs.get(inputNames[0]) != null) {
            fingerprintItemList = (List<FingerprintItem>) ((DataThing) (inputs.get(inputNames[0]))).getDataObject();
        }
        if (inputs.get(inputNames[1]) != null) {
            descriptorNames = (List<String>) ((DataThing) (inputs.get(inputNames[1]))).getDataObject();
        }
        if (inputs.get(inputNames[2]) != null) {
            options = (List<String>) ((DataThing) (inputs.get(inputNames[2]))).getDataObject();
        }
        if (fingerprintItemList == null || fingerprintItemList.isEmpty()) {
            comment.add("no entry in list");
            outputs.put(outputNames[0], new DataThing(comment));
            return outputs;
        }
        if (descriptorNames == null) {
            descriptorNames = new ArrayList<String>(fingerprintItemList.get(0).fingerprintVector.length);
            for (int i = 0; i < fingerprintItemList.get(0).fingerprintVector.length; i++) {
                descriptorNames.add("Descriptor_" + String.valueOf(i));
            }
        }
        try {
            System.out.println("SimpleKmeans_1 Start");
            File modelFile;
            File arffFile;
            FingerprintItem[] itemArray = fingerprintItemList.toArray(new FingerprintItem[0]);
            System.out.println("SimpleKmeans_2 Scalefingerprint");
            Art2aClassificator.scaleFingerprintVectorComponentsToIntervalZeroOne(itemArray);
            Double[][] fingerprintData = new Double[itemArray.length][];
            for (int i = 0; i < fingerprintData.length; i++) {
                fingerprintData[i] = new Double[itemArray[i].fingerprintVector.length];
                for (int j = 0; j < fingerprintData[i].length; j++) {
                    fingerprintData[i][j] = new Double(itemArray[i].fingerprintVector[j]);
                }
            }
            String[] optionArray = null;
            if (options != null) {
                optionArray = options.toArray(new String[0]);
            }
            String[] descriptorArray = new String[descriptorNames.size()];
            descriptorArray = descriptorNames.toArray(descriptorArray);
            int[] typAttribut = new int[descriptorArray.length];
            for (int i = 0; i < typAttribut.length; i++) {
                typAttribut[i] = Weka.NUMERIC;
            }
            SimpleKMeans kMeans;
            System.out.println("SimpleKmeans_3 CreateInstance");
            Instances instances = WekaTools.createInstancesFromFingerprintArray(itemArray, descriptorArray);
            System.out.println("SimpleKmeans_4 SaveInstance");
            arffFile = File.createTempFile("KMeans_Instances", ".txt");
            BufferedWriter twriter = new BufferedWriter(new FileWriter(arffFile));
            twriter.write(instances.toString());
            twriter.flush();
            twriter.close();
            resultFileNames.add(arffFile.getAbsolutePath());
            if (this.removeIdentifier) {
                instances = Filter.useFilter(instances, WekaTools.getIDRemover(instances));
            }
            if (this.useClusterRange) {
                StringBuffer buffer;
                optionArray = new String[2];
                optionArray[0] = "-N";
                for (int i = this.rangeFrom; i < this.rangeTo; i++) {
                    kMeans = new SimpleKMeans();
                    optionArray[1] = String.valueOf(i);
                    kMeans.setOptions(optionArray);
                    System.out.println("SimpleKmeans_5 BuildCluster: Number of Cluster:" + i);
                    kMeans.buildClusterer(instances);
                    System.out.println("SimpleKmeans_6 SaveModel");
                    buffer = new StringBuffer();
                    buffer.append("KMean_clusterer_with");
                    buffer.append(i);
                    buffer.append("cluster");
                    modelFile = File.createTempFile(buffer.toString(), ".model");
                    resultFileNames.add(serializeObjectToFile(modelFile, kMeans));
                }
            } else {
                kMeans = new SimpleKMeans();
                if (optionArray != null) {
                    kMeans.setOptions(optionArray);
                }
                System.out.println("SimpleKmeans_5 BuildCluster");
                kMeans.buildClusterer(instances);
                System.out.println("SimpleKmeans_6 SaveModel");
                modelFile = File.createTempFile("KMean_clusterer", ".model");
                resultFileNames.add(serializeObjectToFile(modelFile, kMeans));
            }
            System.out.println("SimpleKmeans_7 Done");
            outputs.put(outputNames[0], new DataThing(resultFileNames));
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        outputs.put(outputNames[1], new DataThing(comment));
        return outputs;
    }

    public void consumeXML(Element element) {
        Element ele = element.getChild(SIMPLEKMEANSCLUSTERER, XScufl.XScuflNS);
        if (ele == null) {
            return;
        }
        this.scaleFingerprintItemToInternalZeroOne = Boolean.valueOf(ele.getAttributeValue(SCALEFINGERPRINT));
        this.removeIdentifier = Boolean.valueOf(ele.getAttributeValue(REMOVEIDENTIFIER));
        this.useClusterRange = Boolean.valueOf(ele.getAttributeValue(USECLUSTERRANGE));
        this.rangeFrom = Integer.valueOf(ele.getAttributeValue(RANGEFROM));
        this.rangeTo = Integer.valueOf(ele.getAttributeValue(RANGETO));
        ele = null;
        ele = element.getChild(RESULTFOLDERNAMES, XScufl.XScuflNS);
        if (ele == null) {
            return;
        }
        this.folderName = String.valueOf(ele.getAttributeValue(FOLDERNAME));
    }

    public Element provideXML() {
        Element extensions = new Element(EXTENSIONS, XScufl.XScuflNS);
        Element insertElement = new Element(SIMPLEKMEANSCLUSTERER, XScufl.XScuflNS);
        insertElement.setAttribute(SCALEFINGERPRINT, String.valueOf(this.scaleFingerprintItemToInternalZeroOne));
        insertElement.setAttribute(REMOVEIDENTIFIER, String.valueOf(this.removeIdentifier));
        insertElement.setAttribute(USECLUSTERRANGE, String.valueOf(this.useClusterRange));
        insertElement.setAttribute(RANGEFROM, String.valueOf(this.rangeFrom));
        insertElement.setAttribute(RANGETO, String.valueOf(this.rangeTo));
        extensions.addContent(insertElement);
        Element fileName = new Element(RESULTFOLDERNAMES, XScufl.XScuflNS);
        fileName.setAttribute(FOLDERNAME, this.folderName);
        extensions.addContent(fileName);
        return extensions;
    }

    /**
	 * Serialize a given object to a file
	 * @param outputFile
	 * 				File which contains the serialized object
	 * @param object
	 * 				Object to serialize
	 * @return
	 * 				The absolute path of the created file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private String serializeObjectToFile(File outputFile, Object object) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        oos.writeObject(object);
        oos.flush();
        oos.close();
        return outputFile.getAbsolutePath();
    }
}
