package de.banh.bibo.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MenuStructure {

    private final List<MenuStructureItem> menuItems = new ArrayList<MenuStructureItem>();

    private static final Logger logger = Logger.getLogger(MenuStructure.class.getName());

    public static final String MENU_STRUCTURE_FILE_PROPERTIES = "menu.structure.file";

    public static final String LOG_CONFIG_LOADING_MENU_STRUCT_FROM_PROPERTIES = "Log_Config_LoadingMenuStructFrom";

    public static final String LOG_INFO_LOADED_X_MENU_ITEMS = "Log_Info_LoadedXMenuItems";

    public MenuStructure() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            String filename = System.getProperty(MENU_STRUCTURE_FILE_PROPERTIES);
            if (filename == null) {
                filename = "MenuStructure.xml";
            }
            String[] p = new String[1];
            p[0] = filename;
            logger.config("Lade Menüstruktur aus XML-Datei");
            Document document = builder.parse(new File(filename));
            NodeList menuTagList = document.getElementsByTagName("menu");
            if (menuTagList.getLength() != 1) {
            }
            Node menuTag = menuTagList.item(0);
            final NodeList childNodes = menuTag.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                final MenuStructureItem menuItemFromNode = getMenuItemFromNode(childNodes.item(i));
                if (menuItemFromNode != null) getMenuItems().add(menuItemFromNode);
            }
            logger.info("Es wurden " + childNodes.getLength() + " Menüeintrag geladen.");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MenuStructureItem getMenuItemFromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        final Node namedItem = attributes.getNamedItem("text");
        String text = null;
        if (namedItem != null) {
            text = namedItem.getNodeValue();
        }
        if (text == null) {
            return null;
        }
        final Node linkItem = attributes.getNamedItem("link");
        String link = null;
        if (linkItem != null) {
            link = linkItem.getNodeValue();
        }
        List<MenuStructureItem> children = new ArrayList<MenuStructureItem>();
        final NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final MenuStructureItem menuItemFromNode = getMenuItemFromNode(childNodes.item(i));
            if (menuItemFromNode != null) children.add(menuItemFromNode);
        }
        return new MenuStructureItem(text, link, children);
    }

    /**
	 * @return the menuItems
	 */
    public List<MenuStructureItem> getMenuItems() {
        return menuItems;
    }
}
