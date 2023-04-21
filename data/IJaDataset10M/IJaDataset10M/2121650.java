package com.data;

import com.game.Actions;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Master
 */
public class GameData {

    private static ArrayList<GameElement> _topLevelData = new ArrayList<GameElement>();

    public static ArrayList<GameElement> getTopLevelData() {
        return _topLevelData;
    }

    public static void init() {
        try {
            createElementTemplates();
            Actions.showMessage("XML Templates Created");
            loadGameData();
            Actions.showMessage("Game Data Loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createElementTemplates() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("elementTemplates.xml"));
            doc.getDocumentElement().normalize();
            for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
                int properFirstChildIndex = 0, properSecondChildIndex = 1;
                Node node = doc.getDocumentElement().getChildNodes().item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) node;
                    String name = ele.getAttribute("name");
                    String type = ele.getAttribute("type");
                    if (node.hasChildNodes() == false) {
                        GameElement template = new GameElement(name, type, new ArrayList<String>(), new String[] {});
                        GameElement.addTemplate(template);
                        continue;
                    }
                    Element firstChild = (Element) ele.getElementsByTagName("requiredAttributes").item(0), secondChild = (Element) ele.getElementsByTagName("events").item(0);
                    Node fn = node.getChildNodes().item(properFirstChildIndex);
                    while (fn.getNodeType() != Node.ELEMENT_NODE) {
                        properFirstChildIndex++;
                        fn = node.getChildNodes().item(properFirstChildIndex);
                    }
                    properSecondChildIndex = properFirstChildIndex;
                    firstChild = (Element) fn;
                    fn = node.getChildNodes().item(properSecondChildIndex);
                    while (fn.getNodeType() != Node.ELEMENT_NODE) {
                        properSecondChildIndex++;
                        fn = node.getChildNodes().item(properSecondChildIndex);
                    }
                    secondChild = (Element) fn;
                    NodeList requiredAttributes = firstChild.getElementsByTagName("requiredAttribute");
                    NodeList eventsList = secondChild.getElementsByTagName("event");
                    String[] attributes = new String[requiredAttributes.getLength()];
                    ArrayList<String> events = new ArrayList<String>();
                    for (int j = 0; j < attributes.length; j++) {
                        attributes[j] = requiredAttributes.item(j).getTextContent();
                    }
                    for (int j = 0; j < eventsList.getLength(); j++) {
                        events.add(eventsList.item(j).getTextContent());
                    }
                    GameElement template = new GameElement(name, type, events, attributes);
                    GameElement.addTemplate(template);
                }
            }
        } catch (Exception e) {
            System.out.println("An error has occured");
            e.printStackTrace();
        }
    }

    public static void loadGameData() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("towns.xml"));
            doc.getDocumentElement().normalize();
            for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
                Node node = doc.getDocumentElement().getChildNodes().item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                processElement(node, null);
            }
        } catch (Exception e) {
            System.out.println("An error has occured");
            e.printStackTrace();
        }
    }

    public static void processElement(Node nde, GameElement parent) {
        try {
            GameElement element;
            GameElement template = GameElement.getTemplate(nde.getNodeName());
            if (template.getType() != GameElement.DATA_ELEMENT && template.getType() != GameElement.EVENT_ELEMENT) element = GameElement.getNewInstance(nde.getNodeName(), nde, parent); else element = new GameElement(nde, parent);
            if (parent != null) {
                parent.addChild(element);
            } else {
                _topLevelData.add(element);
            }
            if (nde.hasChildNodes() == false) {
                return;
            }
            NodeList children = nde.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
                processElement(children.item(i), element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatStringForReflection(String str) {
        String newStr = "";
        for (int i = 0; i < str.length(); i++) {
            String c = str.substring(i, i + 1);
            if (Pattern.matches("[A-Z]", c)) {
                newStr += "_";
            }
            newStr += c;
        }
        newStr = newStr.toUpperCase();
        return newStr;
    }
}
