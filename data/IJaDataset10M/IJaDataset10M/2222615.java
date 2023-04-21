package net.sf.ideoreport.engines.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import net.sf.ideoreport.api.configuration.ConfigType;
import net.sf.ideoreport.api.configuration.exception.ConfigurationException;
import net.sf.ideoreport.api.datastructure.containers.data.DataContainer;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.api.enginestructure.IEngine;
import net.sf.ideoreport.api.enginestructure.exception.EngineException;
import net.sf.ideoreport.common.config.DefaultConfigLoader;
import net.sf.ideoreport.common.config.IDataContainerConfig;
import net.sf.ideoreport.common.config.IEngineConfig;
import net.sf.ideoreport.xml.XMLDOMBuilder;
import net.sf.ideoreport.xml.XMLEngineConfigHelper;
import net.sf.ideoreport.xml.XmlXslUtils;

/**
 * @author jbeausseron
 * 
 * Un g�n�rateur de rapports XML.
 */
public class XMLEngine implements IEngine {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(XMLEngine.class);

    /** encodage des donn�es en sortie */
    public static final String XML_OUTPUT_ENCODING = "utf-8";

    /** Configuration du composant courant. */
    protected IEngineConfig config;

    /**
     * Liste de configurations utilis�es pour optimiser le chargement depuis le
     * fichier de configuration. A chaque couple resource - type de config, on
     * associe une EngineConfig correspondante.
     */
    protected static Hashtable configurations = new Hashtable();

    /**
     * Moteur de g�n�ration de rapport XMl
     */
    public XMLEngine() {
        super();
    }

    /**
     * Construit un g�n�rateur de graphiques et l'initialise � l'aide d'un
     * fichier de param�tres. Tente de r�cup�rer l'objet depuis une structuer
     * interne pour optimisation.
     * 
     * @param pConfigResourceName
     *            nom de la ressource qui contient les param�tres du graphique
     * @param pConfigType
     *            type de configuration utilis�.
     * @return l'objet r�cup�r� ou nouvellement cr��
     * @throws ConfigurationException
     *             en cas d'erreur lors de la configuration
     */
    public static XMLEngine getInstance(String pConfigResourceName, ConfigType pConfigType) throws ConfigurationException {
        return getInstance(pConfigResourceName, pConfigType, false);
    }

    /**
     * Construit un g�n�rateur de graphiques et l'initialise � l'aide d'un
     * fichier de param�tres. Tente de r�cup�rer l'objet depuis une structuer
     * interne pour optimisation.
     * 
     * @param pConfigResourceName
     *            nom de la ressource qui contient les param�tres du graphique
     * @param pConfigType
     *            type de configuration utilis�
     * @param forceCreation
     *            <code>true</code> pour forcer la cr�ation d'un nouvel objet
     *            � partir de la config demand�e, <code>false</code> sinon
     * @return l'objet r�cup�r� ou nouvellement cr��
     * @throws ConfigurationException
     *             en cas d'erreur lors de la configuration
     */
    public static XMLEngine getInstance(String pConfigResourceName, ConfigType pConfigType, boolean forceCreation) throws ConfigurationException {
        XMLEngine vEngine = new XMLEngine();
        if (forceCreation) {
            vEngine.reset(pConfigResourceName, pConfigType);
        }
        vEngine.loadConfiguration(pConfigResourceName, pConfigType);
        return vEngine;
    }

    /** @see net.sf.ideoreport.api.configuration.IConfigurable#reset() */
    public void reset() {
        configurations.clear();
    }

    /**
     * Supprime une configuration du cache.
     * 
     * @param pName
     *            le nom de la configuration
     * @param pConfigType
     *            le type de la configuration
     */
    public void reset(String pName, ConfigType pConfigType) {
        configurations.remove(pConfigType.getType() + "_" + pName);
    }

    /**
     * @see net.sf.ideoreport.api.enginestructure.IEngine#process(net.sf.ideoreport.api.datastructure.containers.data.DataContainer,
     *      java.io.OutputStream,
     *      net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues)
     */
    public boolean process(DataContainer pDataContainer, OutputStream pOut, IParameterValues pReportParameterValues) throws EngineException {
        try {
            IDataContainerConfig vDataContainerConfig = this.config.getDataContainer(pDataContainer.getName());
            if (vDataContainerConfig == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("no configuration for data container [" + pDataContainer.getName() + "]");
                }
            }
            XMLEngineConfigHelper vConfigHelper = new XMLEngineConfigHelper(vDataContainerConfig);
            String vPrefix = vConfigHelper.getNamespace();
            String vPrefixURL = vConfigHelper.getNamespaceURL();
            Document vDoc = XMLDOMBuilder.createXmlDocument(pDataContainer, pReportParameterValues, vPrefix, vPrefixURL, vConfigHelper);
            String vXslURL = vConfigHelper.getAssociatedXSL();
            if (StringUtils.isEmpty(vXslURL)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("no XSL transformation do proceed");
                }
                XMLDOMBuilder.writeXMLDocumentToStream(vDoc, new PrintStream(pOut));
            } else {
                ByteArrayOutputStream vXmlContent = new ByteArrayOutputStream();
                XMLDOMBuilder.writeXMLDocumentToStream(vDoc, new PrintStream(new BufferedOutputStream(vXmlContent)));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("XSL transformation has to be done : xsl = [" + vXslURL + "]...");
                }
                XmlXslUtils vXSLTransformer = new XmlXslUtils();
                vXSLTransformer.setParameters(pReportParameterValues);
                vXSLTransformer.transformXmlXslFromContentAndPath(new ByteArrayInputStream(vXmlContent.toByteArray()), vXslURL, pOut);
            }
        } catch (Exception e) {
            LOGGER.error("process(DataContainer, OutputStream, IParameterValues)" + e, e);
            throw new EngineException("error while generating report", e);
        }
        return true;
    }

    /**
     * 
     * @see net.sf.ideoreport.api.configuration.IConfigurable#loadConfiguration(java.lang.String, net.sf.ideoreport.api.configuration.ConfigType)
     */
    public void loadConfiguration(String pName, ConfigType pType) throws ConfigurationException {
        this.loadConfiguration(pName, pType, false);
    }

    /**
     * @see net.sf.ideoreport.api.configuration.IConfigurable#loadConfiguration(java.lang.String, net.sf.ideoreport.api.configuration.ConfigType, boolean)
     * 
     */
    public void loadConfiguration(String pName, ConfigType pType, boolean pForceReload) throws ConfigurationException {
        String vConfigCacheName = getConfigurationCacheName(pType, pName);
        IEngineConfig vConfiguration = (IEngineConfig) configurations.get(vConfigCacheName);
        if (vConfiguration != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("configuration [" + pName + "] found in cache");
            }
            if (pForceReload) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("configuration [" + pName + "] removed from cache");
                }
                vConfiguration = null;
                configurations.remove(vConfigCacheName);
            } else {
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("configuration [" + pName + "] NOT found in cache");
            }
        }
        if (vConfiguration == null) {
            vConfiguration = (IEngineConfig) DefaultConfigLoader.loadEngineConfig(pName, pType);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("loading configuration (pName = " + pName + ", pType = " + pType.getType() + ", config = " + vConfiguration + ")");
            }
            if (vConfiguration == null) {
                throw new ConfigurationException("configuration not found [" + pName + "] !");
            }
            configurations.put(vConfigCacheName, vConfiguration);
        } else {
        }
        this.config = vConfiguration;
    }

    /** @see net.sf.ideoreport.api.configuration.IConfigurable#loadConfiguration(java.lang.String) */
    public void loadConfiguration(String pName) throws ConfigurationException {
        loadConfiguration(pName, ConfigType.CONFIG_TYPE_XML);
    }

    /**
     * Renvoie la cl� utilis�e pour identifier une configuration donn�e dans le
     * gestionnaire de cache.
     * 
     * @param pType
     *            type de configuration
     * @param pName
     *            nom de la configuration
     * @return la cl� g�n�r�e.
     */
    private String getConfigurationCacheName(ConfigType pType, String pName) {
        String vRetour = pType.getType() + "_" + pName;
        return vRetour;
    }
}
