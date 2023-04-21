package net.sf.webdeco.decoration;

import java.util.Map;

/**
 * Classes that implement this interface are able to parse use case content and
 * split it up in parts and model data
 * 
 * @author ninan
 */
public interface ContentParser {

    /**
     * This method get�s called by a {@net.sf.webdeco.decoration.Decoration} class.
     * It has to parse the supplied content and extract relevant data from it. 
     * This data has to be put into the <code>partsMap</code> and the <code>modelMap</code>.
     * 
     * @param content This is the content that was returned from further down the 
     * filter chain
     * @param partsMap A map that will collect extracted parts from the supplied content.
     * This means chunks of the content that will be included into the final output untouched.
     * @param modelMap A map that will collect all non-string data that can be used in
     * the decoration.
     * @throws java.lang.Exception Any exception thrown while parsing the content
     */
    void parse(char[] content, Map<String, String> partsMap, Map<String, Object> modelMap) throws Exception;
}
