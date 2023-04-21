package edu.sharif.ce.dml.common.data.trace.config;

import edu.sharif.ce.dml.common.data.trace.TraceWriter;
import edu.sharif.ce.dml.common.data.trace.filenamegenerator.FileNameGenerator;
import edu.sharif.ce.dml.common.parameters.data.StringDataParameter;
import edu.sharif.ce.dml.common.parameters.logic.Parameter;
import edu.sharif.ce.dml.common.parameters.logic.complex.ParameterableParameter;
import edu.sharif.ce.dml.common.parameters.logic.complex.SelectOneParameterable;
import edu.sharif.ce.dml.common.parameters.logic.parameterable.FileParameter;
import edu.sharif.ce.dml.common.parameters.logic.parameterable.ParameterableImplement;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Jun 11, 2008
 * Time: 2:12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TraceWriterConfig extends ParameterableImplement {

    protected SelectOneParameterable fileNameGenerator;

    protected ParameterableParameter outputFolder;

    protected SelectOneParameterable traceWriter;

    public TraceWriterConfig() {
        this.fileNameGenerator = new SelectOneParameterable(true);
        this.traceWriter = new SelectOneParameterable(false);
        outputFolder = new ParameterableParameter();
    }

    public void setParameters(Map<String, Parameter> parameters) {
        fileNameGenerator = (SelectOneParameterable) parameters.get("filenamegenerator");
        outputFolder = (ParameterableParameter) parameters.get("outputfolder");
        traceWriter = (SelectOneParameterable) parameters.get("tracewriter");
    }

    public Map<String, Parameter> getParameters() {
        Map<String, Parameter> parameters = new HashMap<String, Parameter>();
        parameters.put("filenamegenerator", fileNameGenerator);
        parameters.put("outputfolder", outputFolder);
        parameters.put("tracewriter", traceWriter);
        return parameters;
    }

    public void setPrefix(String s) {
        getFileGenerator().setPrefix(s);
    }

    public void setPostfix(String s) {
        getFileGenerator().setPostfix(s);
    }

    public String getPrefix() {
        return getFileGenerator().getPrefix();
    }

    public String getPostfix() {
        return getFileGenerator().getPostfix();
    }

    public File getNextFile() {
        File file = new File(((FileParameter) outputFolder.getValue()).getValue().getPath(), getFileGenerator().getNextFileName());
        if (!file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public TraceWriter getNextTraceWriter(Collection<StringDataParameter> parameters, String[] dataLabels) {
        TraceWriter traceWriter1 = (TraceWriter) traceWriter.getValue();
        traceWriter1.init(parameters, dataLabels, getNextFile().getPath());
        return traceWriter1;
    }

    private FileNameGenerator getFileGenerator() {
        assert fileNameGenerator.getValue() != null;
        return (FileNameGenerator) fileNameGenerator.getValue();
    }
}
