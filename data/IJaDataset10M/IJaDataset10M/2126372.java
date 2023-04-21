package org.jcompany.control.tiles.adm;

import java.io.FileNotFoundException;
import javax.servlet.ServletContext;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.xmlDefinition.DefinitionsFactory;
import org.apache.struts.tiles.xmlDefinition.I18nFactorySet;
import org.apache.struts.tiles.xmlDefinition.XmlDefinitionsSet;

/**
 * jCompany 3.0 
 * @since jCompany 3.0
 * @version $Id: PlcI18nFactorySet.java,v 1.2 2006/05/17 20:38:13 rogerio_baldini Exp $
 */
public class PlcI18nFactorySet extends I18nFactorySet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Create default factory .
     * Create InstancesMapper for specified Locale.
     * If creation failes, use default mapper and log error message.
     * @param servletContext Current servlet context. Used to open file.
     * @return Created default definition factory.
     * @throws DefinitionsFactoryException If an error occur while creating factory.
     * @throws FileNotFoundException if factory can't be loaded from filenames.
     */
    protected DefinitionsFactory createDefaultFactory(ServletContext servletContext) throws DefinitionsFactoryException, FileNotFoundException {
        XmlDefinitionsSet rootXmlConfig = parseXmlFiles(servletContext, "", null);
        if (rootXmlConfig == null) {
            throw new FileNotFoundException();
        }
        rootXmlConfig.resolveInheritances();
        if (log.isDebugEnabled()) {
            log.debug(rootXmlConfig);
        }
        DefinitionsFactory factory = new PlcDefinitionsFactory(rootXmlConfig);
        if (log.isDebugEnabled()) {
            log.debug("factory loaded : " + factory);
        }
        return factory;
    }
}
