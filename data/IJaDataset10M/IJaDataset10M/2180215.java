package org.openremote.controller.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.openremote.controller.Constants;
import org.openremote.controller.ControllerConfiguration;

/**
 * This class is the data model for OpenRemote panel definitions. It provides bindings to an
 * XML model using W3C DOM API.  <p>
 *
 * It has been written to minimize API dependencies to enable reuse across various Java and
 * "Java-like" runtime environments, mainly targeting Java 6 SE and Android runtimes. The
 * dependencies are kept minimal in particular to ease the reuse and/or porting to Android
 * environments.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public class Panel {

    /**
   * XML 'panel' tag name.
   */
    public static final String XML_ELEMENT_PANEL = "panel";

    /**
   * XML 'id' attribute name in panel tag.
   */
    public static final String XML_PANEL_ATTRIBUTE_ID = "id";

    /**
   * XML 'name' attribute name in panel tag.
   */
    public static final String XML_PANEL_ATTRIBUTE_NAME = "name";

    /**
   * Common logger for XML parsing tasks.
   */
    private static final Logger xmlLog = Logger.getLogger(Constants.XML_PARSER_LOG_CATEGORY);

    public static List<Panel> getPanels(Document doc) {
        NodeList list = doc.getElementsByTagName(XML_ELEMENT_PANEL);
        List<Panel> panels = new ArrayList<Panel>();
        for (int panelIndex = 0; panelIndex < list.getLength(); ++panelIndex) {
            Node panel = list.item(panelIndex);
            try {
                String panelID = XMLMapping.getMandatoryAttributeValue(panel, XML_PANEL_ATTRIBUTE_ID);
                String panelName = XMLMapping.getMandatoryAttributeValue(panel, XML_PANEL_ATTRIBUTE_NAME);
                panels.add(new Panel(panelID, panelName));
            } catch (XMLSchemaException exception) {
                xmlLog.log(Level.SEVERE, "Skipping panel XML definition due to parsing error: " + exception.getMessage(), exception);
            }
        }
        return Collections.unmodifiableList(panels);
    }

    public static List<String> getPanelNames(Document doc) {
        List<Panel> panels = getPanels(doc);
        List<String> names = new ArrayList<String>(panels.size());
        for (Panel panel : panels) {
            String name = panel.getName();
            names.add(name);
        }
        return names;
    }

    public static List<String> getPanelIds(Document doc) {
        List<Panel> panels = getPanels(doc);
        List<String> ids = new ArrayList<String>(panels.size());
        for (Panel panel : panels) {
            String name = panel.getId();
            ids.add(name);
        }
        return ids;
    }

    public static String toPanelListXML(List<Panel> panels) {
        StringBuffer buffer = new StringBuffer(8000);
        buffer.append(XMLMapping.XML_DECLARATION_UTF8).append(ControllerConfiguration.LINE_SEPARATOR).append("<openremote>");
        for (Panel panel : panels) {
            buffer.append(XMLMapping.XML_DOCUMENT_INDENT).append("<panel id = \"").append(panel.getId()).append("\" name = \"").append(panel.getName()).append("\"/>").append(ControllerConfiguration.LINE_SEPARATOR);
        }
        buffer.append("</openremote>");
        return buffer.toString();
    }

    /**
   * Panel name.
   *
   * This maps to the "name" attribute of panel element in the XML model.
   *
   * @see #XML_ELEMENT_PANEL
   * @see #XML_PANEL_ATTRIBUTE_NAME
   */
    private String name = "<no name>";

    /**
   * Panel id.
   *
   * This maps to the 'id' attribute of panel element in the XML model.
   *
   * @see #XML_ELEMENT_PANEL
   * @see #XML_PANEL_ATTRIBUTE_ID
   */
    private String id = "<undefined>";

    private Panel(String panelID, String panelName) {
        this.id = panelID;
        this.name = panelName;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Panel)) return false;
        Panel p = (Panel) o;
        return p.id.equals(this.id) && p.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ name.hashCode();
    }
}
