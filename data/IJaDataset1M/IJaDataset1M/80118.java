package com.googlecode.httl.support;

import java.io.IOException;
import java.util.List;
import com.googlecode.httl.Resource;

/**
 * Loader. (SPI, Singleton, ThreadSafe)
 * 
 * @see com.googlecode.httl.Engine#setLoader(Loader)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public interface Loader {

    /**
     * list names.
     * 
     * @return names.
     */
    List<String> list() throws IOException;

    /**
	 * Load template source.
	 * 
	 * @param name - Template name
	 * @param encoding - Template encoding
	 * @return Template source
	 */
    Resource load(String name, String encoding) throws IOException;
}
