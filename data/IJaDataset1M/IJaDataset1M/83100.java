package net.sf.jdiskcatalog.api;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Analyses a given file.
 *
 * @author Przemek Więch <pwiech@losthive.org>
 * @version $Id$
 */
public interface FileAnalyser {

    /**
	 * Analyses a given file.
	 * @param file  file to be analysed
	 * @param node  contains information about this file that was gathered up to now.
	 * @return  map containing attributes to be added to the file information.
	 */
    public Map<String, Object> analyse(File file, Node node) throws IOException;
}
