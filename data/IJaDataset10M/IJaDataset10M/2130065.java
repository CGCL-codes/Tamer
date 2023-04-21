package org.debellor.weka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import org.debellor.core.Cell;
import org.debellor.core.Parameters;
import org.debellor.core.Sample;
import org.debellor.core.Sample.SampleType;
import org.debellor.core.parameters.ParametersInfo;
import weka.core.Instance;
import weka.core.Instances;

/**
 * <p>Parameters:</p>
 * <ul>
 * <li><i>filename</i>: directory path to the ARFF file
 * <li><i>fileURL</i>: URL of the ARFF file. 
 * 		Can be given instead of a <i>filename</i>,
 * 		e.g. to read from a class resource file:<br>
 *          <code> arffReader.set("fileURL", myClass.class.getResource("...").toString()); </code>
 * <li><i>decisionAttr</i>: index of the attribute to become {@link Sample#decision}. Indices start from 1.
 * 		Special values "last" and "-1" denote the last attribute.
 * 		If not specified or empty string, all attributes will fall into {@link Sample#data}.
 * </ul>
 * 
 * <p>Instead of specifying a 'filename' or 'fileURL' the user may call 
 * {@link #setInputStream(InputStream)} to manually
 * provide an instance of <code>java.io.InputStream</code> that 
 * should be used to read the ARFF file.
 * This enables reading from file that is not accessible in regular way,
 * e.g., is stored in memory.
 * This also gives the user more control over how the file is accessed and when,
 * as the InputStream instance may be prepared in a special way by the user. 
 * </p>
 * 
 * @author Marcin Wojnarski
 *
 */
public class ArffReader extends Cell {

    private static final ParametersInfo availParams = new ParametersInfo("filename", null, "directory path to the ARFF file", "fileURL", null, "URL of the ARFF file. Can be given instead of a filename, " + "e.g. to read from a class resource file: " + "arffReader.set(\"fileURL\", myClass.class.getResource(\"...\").toString());", "decisionAttr", "", "index of the attribute to become Sample.decision. Indices start from 1. " + "Special values \"last\" and \"-1\" denote the last attribute. " + "If not specified or empty string, all attributes will fall into Sample.data.");

    private InputStream inputStreamFromUser;

    private Reader reader;

    private SampleType type;

    private Instances instances;

    private boolean finished;

    public ArffReader() {
        super(false);
        setAvailableParams(availParams);
    }

    /**
	 * Sets the instance of <code>java.io.InputStream</code> that will be used
	 * by this ArffReader to read the contents of an ARFF file.
	 * ArffReader will use this input stream instead of opening
	 * the file by itself.
	 * This method has priority over parameters 'filename' and 'fileURL'.
	 * It has effect only on the nearest call to open-next...-close.
	 */
    public void setInputStream(InputStream inputStream) {
        inputStreamFromUser = inputStream;
    }

    @Override
    protected SampleType onOpen() throws Exception {
        Parameters param = parameters;
        if (inputStreamFromUser != null) {
            if (!(inputStreamFromUser instanceof BufferedInputStream)) inputStreamFromUser = new BufferedInputStream(inputStreamFromUser);
            reader = new InputStreamReader(inputStreamFromUser);
            inputStreamFromUser = null;
        } else if (param.exists("filename")) reader = new BufferedReader(new FileReader(param.get("filename"))); else if (param.exists("fileURL")) reader = new InputStreamReader(new BufferedInputStream(new URL(param.get("fileURL")).openStream())); else throw new Exception("Neither 'filename' nor 'fileURL' parameter was defined for ArffReader");
        instances = new Instances(reader, 1);
        int decisionAttrIndex = -2;
        try {
            if (param.get("decisionAttr").trim().equalsIgnoreCase("last")) decisionAttrIndex = -1; else decisionAttrIndex = param.getAsInt("decisionAttr");
            if (decisionAttrIndex == -1) decisionAttrIndex = instances.numAttributes() - 1;
        } catch (Exception e) {
        }
        if ((decisionAttrIndex >= 0) && (decisionAttrIndex < instances.numAttributes())) instances.setClassIndex(decisionAttrIndex);
        type = DataConverter.sampleTypeFrom(instances);
        finished = false;
        return type;
    }

    @Override
    protected Sample onNext() throws Exception {
        if (finished) return null;
        if (!instances.readInstance(reader)) {
            finished = true;
            return null;
        }
        Instance instance = instances.instance(0);
        Sample s = DataConverter.sampleFrom(instance, type);
        instances.delete(0);
        return s;
    }

    @Override
    protected void onClose() throws Exception {
        reader.close();
        reader = null;
        instances = null;
        type = null;
    }
}
