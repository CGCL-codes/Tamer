package net.sf.i18nsanity.pt.service.request;

import java.util.List;

/**
 * @author Gary S. Weaver
 */
public class PropertiesModifierRequestImpl implements PropertiesModifierRequest {

    private String inputFile;

    private String outputFile;

    private List propsToReplaceFiles;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public List getPropsToReplaceFiles() {
        return propsToReplaceFiles;
    }

    public void setPropsToReplaceFiles(List propsToReplaceFiles) {
        this.propsToReplaceFiles = propsToReplaceFiles;
    }
}
