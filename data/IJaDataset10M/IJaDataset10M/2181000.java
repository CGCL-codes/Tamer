package org.ice.config;

import javax.servlet.ServletContext;

/**
 * Interface for all configuration parser, which takes a source
 * object (e.g: a file or an key-value object), parse and store
 * result in the output
 * Developers can also provide their own implementation and register
 * them using <code>ice.config.parser</code> parameter in the <code>web.xml</code>
 * 
 * @author dungba
 */
public interface IConfigParser {

    /**
	 * Parses a source object and store the result in the output
	 * @param sc used for accessing the servlet's context 
	 * @param source the source object
	 * @param output the output used for storing parsed information
	 * @throws Exception
	 */
    public void parse(ServletContext sc, Object source, IConfigData output) throws Exception;
}
