package org.mazix.xml.impl.dom;

import static java.util.logging.Level.WARNING;
import static org.mazix.constants.GlobalConstants.BLANK_STRING;
import static org.mazix.constants.PlayerConstants.DEFAULT_PROGRESSION;
import static org.mazix.constants.XMLConstants.LEVEL_NUMBER_TAG;
import static org.mazix.constants.XMLConstants.NAME_TAG;
import static org.mazix.constants.XMLConstants.NO_NAMESPACE_SCHEMA;
import static org.mazix.constants.XMLConstants.PROFILES_TAG;
import static org.mazix.constants.XMLConstants.PROFILE_TAG;
import static org.mazix.constants.XMLConstants.SERIES_TAG;
import static org.mazix.constants.XMLConstants.W3C_INSTANCE;
import static org.mazix.constants.XMLConstants.W3C_XML_SCHEMA;
import static org.mazix.constants.log.ErrorConstants.NUMBER_FORMAT_ERROR;
import static org.mazix.constants.log.InfoConstants.BLANK_PROFILE_INFO;
import static org.mazix.constants.log.InfoConstants.BLANK_SERIES_NAME_INFO;
import static org.mazix.constants.log.InfoConstants.NOT_SUPPORTED_NODE_TYPE_INFO;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_LEVEL_PROGRESSION_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_XML_TAG_WARNING;
import static org.w3c.dom.Node.ELEMENT_NODE;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.mazix.kernel.Profile;
import org.mazix.log.LogUtils;
import org.mazix.xml.XMLProfiles;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The class which manages XML documents and parse XML files to fill profiles series with the DOM
 * implementation.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.5
 * @version 0.7
 */
class DOMProfiles extends DOMTools implements XMLProfiles {

    /** The class logger. */
    private static final Logger LOGGER = Logger.getLogger("org.mazix.xml.impl.dom.DOMProfiles");

    /**
     * Default constructor, sets document to an empty tree.
     * 
     * @throws ParserConfigurationException
     *             if the empty document can't be initialized
     * @since 0.5
     */
    public DOMProfiles() throws ParserConfigurationException {
        super();
    }

    /**
     * Reads the level progression from a series node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the series node containing a level progression attribute, can't be
     *            <code>null</code>. If the node doesn't contain a level progression attribute, the
     *            level progression will be empty.
     * @return a filled profile instance.
     * @since 0.5
     */
    private int readLevelNumberAttribute(final Node node) {
        assert node != null : "node is null";
        int levelProgression = DEFAULT_PROGRESSION;
        final Node levelNumberAttr = node.getAttributes().getNamedItem(LEVEL_NUMBER_TAG);
        try {
            if (levelNumberAttr != null && levelNumberAttr.getNodeValue() != null) {
                levelProgression = Integer.parseInt(levelNumberAttr.getNodeValue());
            } else {
                LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LEVEL_PROGRESSION_WARNING, levelNumberAttr));
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LEVEL_PROGRESSION_WARNING, levelNumberAttr));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            levelProgression = DEFAULT_PROGRESSION;
        }
        return levelProgression;
    }

    /**
     * Builds and fills profile instance from an profile node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the profile node filled by a XML game options tree, can't be <code>null</code>. If
     *            the node isn't an profile node, the profile instance will be empty.
     * @return a filled profile instance.
     * @since 0.5
     */
    private Profile readProfileNode(final Node node) {
        assert node != null : "node is null";
        String profileName = BLANK_STRING;
        final Node nameAttr = node.getAttributes().getNamedItem(NAME_TAG);
        if (nameAttr != null && nameAttr.getNodeValue() != null) {
            profileName = nameAttr.getNodeValue();
        } else {
            LOGGER.info(BLANK_PROFILE_INFO);
        }
        final Profile profile = new Profile(profileName);
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                final String seriesName = readSeriesNameAttribute(nl.item(i));
                final int levelProgression = readLevelNumberAttribute(nl.item(i));
                profile.addProgression(seriesName, levelProgression);
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
        return profile;
    }

    /**
     * @see org.mazix.xml.XMLProfiles#readProfiles(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Profile> readProfiles(final String pathname, final String schema) throws ParserConfigurationException, SAXException, IOException, JAXBException {
        assert pathname != null : "pathname is null";
        assert schema != null : "schema is null";
        assert getDocument() != null : "document is null";
        Map<String, Profile> profilesSeries = new HashMap<String, Profile>();
        readXml(pathname, schema);
        final NodeList nl = getDocument().getElementsByTagName(PROFILES_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            profilesSeries = readProfilesNode(nl.item(i));
        }
        return profilesSeries;
    }

    /**
     * Builds and fills a profile series {@code Map} from a profiles node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the profiles node filled by a XML profiles series tree, can't be <code>null</code>
     *            .
     * @return a filled {@code Map} which contains profiles.
     * @since 0.5
     */
    private Map<String, Profile> readProfilesNode(final Node node) {
        assert node != null : "node is null";
        final Map<String, Profile> profilesSeries = new HashMap<String, Profile>();
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                if (PROFILE_TAG.equals(nl.item(i).getNodeName())) {
                    final Profile tempProfile = readProfileNode(nl.item(i));
                    profilesSeries.put(tempProfile.getName(), tempProfile);
                } else {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_XML_TAG_WARNING, nl.item(i).getNodeName()));
                }
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
        return profilesSeries;
    }

    /**
     * Reads the series name from a series node. May throw a {@code NullPointerException} if {@code
     * node} is <code>null</code>.
     * 
     * @param node
     *            the series node containing a name attribute, can't be {@code null}. If the node
     *            doesn't contain a name attribute, the series name will be empty.
     * @return a filled profile instance.
     * @since 0.5
     */
    private String readSeriesNameAttribute(final Node node) {
        assert node != null : "node is null";
        String seriesName = BLANK_STRING;
        final Node nameAttr = node.getAttributes().getNamedItem(NAME_TAG);
        if (nameAttr != null && nameAttr.getNodeValue() != null) {
            seriesName = nameAttr.getNodeValue();
        } else {
            LOGGER.info(BLANK_SERIES_NAME_INFO);
        }
        return seriesName;
    }

    /**
     * @see org.mazix.xml.XMLProfiles#writeProfiles(java.util.Map, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void writeProfiles(final Map<String, Profile> profilesSeries, final String pathname, final String schema) throws ParserConfigurationException, TransformerException, JAXBException {
        assert pathname != null : "pathname is null";
        assert schema != null : "schema is null";
        assert profilesSeries != null : "profilesSeries is null";
        resetDocument();
        Element root;
        Element profile;
        Element series;
        root = getDocument().createElement(PROFILES_TAG);
        root.setAttributeNS(W3C_XML_SCHEMA + W3C_INSTANCE, NO_NAMESPACE_SCHEMA, schema);
        for (final Profile p : profilesSeries.values()) {
            profile = getDocument().createElement(PROFILE_TAG);
            profile.setAttribute(NAME_TAG, p.getName());
            for (final String seriesName : p.getProgressionKeys()) {
                final Integer levelProgression = p.getProgression(seriesName);
                series = getDocument().createElement(SERIES_TAG);
                series.setAttribute(NAME_TAG, seriesName);
                series.setAttribute(LEVEL_NUMBER_TAG, levelProgression.toString());
                profile.appendChild(series);
            }
            root.appendChild(profile);
        }
        getDocument().appendChild(root);
        writeXml(pathname);
    }
}
