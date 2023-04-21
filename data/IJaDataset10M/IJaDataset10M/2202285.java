package com.iselinlabs.tegf;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import com.iselinlabs.tegf.pojo.Activity;
import com.iselinlabs.tegf.pojo.AnimationData;
import com.iselinlabs.tegf.pojo.ClickableImage;
import com.iselinlabs.tegf.pojo.GameData;
import com.iselinlabs.tegf.pojo.GameObject;
import com.iselinlabs.tegf.pojo.Item;
import com.iselinlabs.tegf.pojo.MainMenu;
import com.iselinlabs.tegf.pojo.MainMenuElement;
import com.iselinlabs.tegf.pojo.Scene;

public class XmlParser extends DefaultHandler {

    String tempVal;

    GameData gameData = null;

    Scene scene = null;

    Activity activity = null;

    ClickableImage clickableImage = null;

    HashMap gameObjects = null;

    ArrayList<ClickableImage> clickableImages = null;

    ArrayList<com.iselinlabs.tegf.pojo.Override> overrides = null;

    com.iselinlabs.tegf.pojo.Override override = null;

    ArrayList<Item> items = null;

    Item item = null;

    AnimationData animation = null;

    MainMenu mainMenu = null;

    ArrayList<MainMenuElement> options = null;

    MainMenuElement option = null;

    HashMap animations = null;

    public XmlParser() {
        super();
        gameData = new GameData();
        gameObjects = new HashMap();
        items = new ArrayList<Item>();
        mainMenu = new MainMenu();
        options = new ArrayList<MainMenuElement>();
        animations = new HashMap();
    }

    public GameData test() throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        XmlParser handler = new XmlParser();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader("edu_game.xml");
        System.out.println("Parsing XML file");
        xr.parse(new InputSource(r));
        return handler.getGameData();
    }

    public GameData getGameData() {
        return gameData;
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        if (qName.equalsIgnoreCase("game")) {
            gameData.setTitle(atts.getValue("title"));
            gameData.setDescription(atts.getValue("description"));
            gameData.setStartSceneId(parseLong(atts, "startSceneId"));
            gameData.setWidth(this.parseInteger(atts, "width"));
            gameData.setHeight(this.parseInteger(atts, "height"));
        } else if (qName.equalsIgnoreCase("mainmenu")) {
            mainMenu.setImage(atts.getValue("image"));
            mainMenu.setMusic(atts.getValue("music"));
            mainMenu.setFont(atts.getValue("font"));
        } else if (qName.equalsIgnoreCase("option")) {
            option = new MainMenuElement();
            option.setTitle(atts.getValue("value"));
            option.setType(parseLong(atts, "type"));
            option.setX(Integer.parseInt(atts.getValue("x")));
            option.setY(Integer.parseInt(atts.getValue("y")));
            option.setSound(atts.getValue("sound"));
            option.setAlign(atts.getValue("align"));
            option.setTitle(atts.getValue("title"));
        } else if (qName.equalsIgnoreCase("scene")) {
            scene = new Scene();
            scene.setId(parseLong(atts, "id"));
            scene.setTitle(atts.getValue("title"));
            scene.setType(GameObject.SCENE_OBJECT);
            scene.setMusic(atts.getValue("music"));
            scene.setSound(atts.getValue("sound"));
            scene.setImage(atts.getValue("image"));
        } else if (qName.equalsIgnoreCase("clickableimages")) {
            clickableImages = new ArrayList<ClickableImage>();
        } else if (qName.equalsIgnoreCase("clickableimage")) {
            clickableImage = new ClickableImage();
            clickableImage.setId(Long.parseLong(atts.getValue("id")));
            clickableImage.setImage(atts.getValue("image"));
            clickableImage.setLinkTo(parseLong(atts, "linkTo"));
            clickableImage.setX(Integer.parseInt(atts.getValue("x")));
            clickableImage.setY(Integer.parseInt(atts.getValue("y")));
            clickableImage.setLocked(parseBoolean(atts, "locked"));
            clickableImage.setTitle(atts.getValue("title"));
            clickableImage.setSound(atts.getValue("sound"));
            clickableImage.setImage(atts.getValue("image"));
            clickableImage.setRequiredItems(extractLongValues(atts.getValue("requiredItems")));
        } else if (qName.equalsIgnoreCase("activity")) {
            activity = new Activity();
            activity.setId(parseLong(atts, "id"));
            activity.setTitle(atts.getValue("title"));
            activity.setMode(parseLong(atts, "mode"));
            activity.setClassName(atts.getValue("className"));
            activity.setItems(this.extractLongValues(atts.getValue("items")));
            activity.setType(GameObject.ACTIVITY_OBJECT);
            activity.setLevel(parseLong(atts, "level"));
            activity.setCorrectItems(this.extractLongValues(atts.getValue("correctItems")));
        } else if (qName.equalsIgnoreCase("overrides")) {
            overrides = new ArrayList<com.iselinlabs.tegf.pojo.Override>();
        } else if (qName.equalsIgnoreCase("override")) {
            override = new com.iselinlabs.tegf.pojo.Override();
            override.setName(atts.getValue("name"));
            override.setValue(atts.getValue("value"));
        } else if (qName.equalsIgnoreCase("item")) {
            item = new Item();
            item.setId(parseLong(atts, "id"));
            item.setBuyable(parseBoolean(atts, "buyable"));
            item.setCost(Double.parseDouble(atts.getValue("cost")));
            item.setImage(atts.getValue("image"));
            item.setSound(atts.getValue("sound"));
            item.setTitle(atts.getValue("title"));
            item.setTalk(atts.getValue("talk"));
        } else if (qName.equalsIgnoreCase("animation")) {
            animation = new AnimationData();
            animation.setId(parseLong(atts, "id"));
            animation.setName(atts.getValue("name"));
            animation.setImage(atts.getValue("image"));
            animation.setXml(atts.getValue("xml"));
            animation.setMode(this.parseInteger(atts, "mode"));
            animation.setDuration(this.parseInteger(atts, "duration"));
        }
    }

    /**
     * Parse attribute value as Boolean
     * @param atts The attributes
     * @param attribute The attribute to parse to Boolean
     * @return The boolean value if valid or null if not
     */
    private Boolean parseBoolean(Attributes atts, String attribute) {
        String value = atts.getValue(attribute);
        if (value != null && value.length() > 0) {
            try {
                return Boolean.parseBoolean(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Parse attribute value as Long
     * @param atts The attributes
     * @param attribute The attribute to parse to Boolean
     * @return The long value if valid or null if not
     */
    private Long parseLong(Attributes atts, String attribute) {
        String value = atts.getValue(attribute);
        if (value != null && value.length() > 0) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Parse attribute value as Integer
     * @param atts The attributes
     * @param attribute The attribute to parse to Boolean
     * @return The integer value if valid or null if not
     */
    private Integer parseInteger(Attributes atts, String attribute) {
        String value = atts.getValue(attribute);
        if (value != null && value.length() > 0) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void endElement(String uri, String name, String qName) {
        if (qName.equalsIgnoreCase("scene")) {
            gameObjects.put(scene.getId(), scene);
            scene = null;
        } else if (qName.equalsIgnoreCase("clickableimages")) {
            scene.setClickableImages(clickableImages);
            clickableImages = null;
        } else if (qName.equalsIgnoreCase("clickableimage")) {
            clickableImages.add(clickableImage);
        } else if (qName.equalsIgnoreCase("activity")) {
            gameObjects.put(activity.getId(), activity);
            activity = null;
        } else if (qName.equalsIgnoreCase("overrides")) {
            activity.setOverrides(overrides);
            overrides = null;
        } else if (qName.equalsIgnoreCase("override")) {
            overrides.add(override);
            override = null;
        } else if (qName.equalsIgnoreCase("item")) {
            items.add(item);
            item = null;
        } else if (qName.equalsIgnoreCase("option")) {
            options.add(option);
            item = null;
        } else if (qName.equalsIgnoreCase("options")) {
            mainMenu.setOptions(options);
        } else if (qName.equalsIgnoreCase("game")) {
            gameData.setItems(items);
            gameData.setMainMenu(mainMenu);
            gameData.setGameObjects(gameObjects);
            gameData.setAnimations(animations);
        } else if (qName.equalsIgnoreCase("animation")) {
            animations.put(animation.getId(), animation);
            animation = null;
        }
    }

    /**
     * Extracts long values from a string
     * @param elements The long values string to extract from
     * @return An array containing the extracted long values 
     */
    private ArrayList<Long> extractLongValues(String elements) {
        ArrayList<Long> result = new ArrayList<Long>();
        if (elements != null && elements.length() > 0) {
            StringTokenizer st = new StringTokenizer(elements, ",");
            while (st.hasMoreTokens()) {
                String t = st.nextToken();
                result.add(Long.parseLong(t));
            }
        }
        return result;
    }

    public void characters(char ch[], int start, int length) {
        tempVal = new String(ch, start, length);
    }
}
