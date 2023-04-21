package net.sf.i18nsanity.pt.service.request;

import java.util.List;

/**
 * @author Gary S. Weaver
 */
public interface PropertiesModifierRequest {

    public String getInputFile();

    public void setInputFile(String inputFile);

    public String getOutputFile();

    public void setOutputFile(String outputFile);

    public List getPropsToReplaceFiles();

    public void setPropsToReplaceFiles(List propsToReplaceFiles);
}
