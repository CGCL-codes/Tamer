package org.jecars.wfplugin;

import java.io.Reader;

/**
 *
 * @author weert
 */
public interface IWFP_Output {

    void setContents(final Reader pReader) throws WFP_Exception;

    void closeStream() throws WFP_Exception;
}
