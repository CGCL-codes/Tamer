package uk.ac.ncl.cs.instantsoap.esciencetool.cline.xmlParser;

import uk.ac.ncl.cs.instantsoap.esciencetool.cline.Request;
import uk.ac.ncl.cs.instantsoap.JobExecutionException;
import java.io.InputStream;

/**
 * Author: Cheng-Yang(Louis) Tang
 * Date: 26-Oct-2006
 * Time: 08:57:47
 */
public interface RequestExtractor {

    Request extractRequest(InputStream inputStream) throws JobExecutionException;

    RequestExtractor copy();
}
