package com.foursoft.fourever.export;

import com.foursoft.fourever.export.exception.ExportException;
import org.apache.commons.logging.Log;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * This class converts from one file to another.
 * 
 * 
 * @version $Revision: 1.5 $
 */
public interface Converter {

    /**
	 * Start transformation of a file according to a script and some properties.
	 * 
	 * @param in
	 *            the file to transform
	 * @param out
	 *            the output of the transformation
	 * @param res
	 *            the resource for the transformation (i.e. a script)
	 * @param p
	 *            a set of properties required for transformation
	 * @param log
	 *            the log to write messages to
	 * 
	 * @throws ExportException
	 *             if transformation was not successful
	 * @throws IOException
	 *             if a file couldn't be accessed
	 */
    public void transform(File in, File out, Resource res, Properties p, Log log) throws ExportException, IOException;
}
