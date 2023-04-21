package org.vosao.business.imex;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.vosao.dao.DaoTaskException;

/**
 * @author Alexander Oleynik
 */
public interface PluginExporter {

    String createPluginsXML();

    void readPlugins(Element PluginsElement) throws DaoTaskException;

    /**
	 * Read and import data from _plugins.xml file.
	 * @param xml - _plugins.xml file content.
	 * @throws DocumentException
	 * @throws DaoTaskException
	 */
    void readPluginsFile(String xml) throws DocumentException, DaoTaskException;
}
