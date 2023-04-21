package org.jfor.jfor.converter;

import org.jfor.jfor.rtflib.rtfdoc.IRtfOptions;
import org.xml.sax.XMLReader;

public interface IConverterOption extends IRtfOptions {

    /**
	 * Gets the log channel.
	 *
	 * If m_log not set, the standard output stream writer will set.
	 *
	 * @return Log channel
	 */
    public ConverterLogChannel getLog();

    /**
	 * Gets the standard XML reader.
	 * 'Xerces.jar' is needed.
	 * Can be overridden to use a different parser.
	 */
    public XMLReader getXmlReader() throws Exception;
}
