package asa.controller.item;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import asa.model.item.Food;
import asa.model.item.Item;
import asa.model.item.Toy;
import asa.util.communication.Communicator;
import asa.util.resourcemanager.Resource;
import asa.util.resourcemanager.ResourceManager;
import asa.util.xml.Preprocessor;
import asa.view.components.JImage;

public class ItemController {

    private static ArrayList<Food> foodList;

    private static ArrayList<Toy> toyList;

    private static int buttonCount = 8;

    public static void loadItems() {
        foodList = new ArrayList<Food>();
        toyList = new ArrayList<Toy>();
        Hashtable<String, String> data = new Hashtable<String, String>();
        try {
            parseXML((Communicator.sendGetData(new URL(Resource.HOST + "getitems"), data)));
        } catch (Exception e) {
            System.err.println("ERROR in [ItemController.loadItems]");
        }
    }

    private static void parseXML(String xmlString) {
        Document doc = Preprocessor.processXML(xmlString);
        NodeList list = doc.getElementsByTagName("item");
        for (int i = 0; i < list.getLength(); i++) {
            Element itemElement = (Element) list.item(i);
            String itemDescription = itemElement.getElementsByTagName("description").item(0).getTextContent();
            if (isFood(itemDescription)) {
                Food f = new Food();
                f.setID(Integer.parseInt(itemElement.getElementsByTagName("id").item(0).getTextContent()));
                f.setName(itemElement.getElementsByTagName("name").item(0).getTextContent());
                f.setDescription(itemElement.getElementsByTagName("description").item(0).getTextContent());
                f.setCost(Integer.parseInt(itemElement.getElementsByTagName("cost").item(0).getTextContent().trim()));
                f.setEffect(Integer.parseInt(itemElement.getElementsByTagName("effect").item(0).getTextContent().trim()));
                f.setItemType(Resource.FOOD);
                f.setImagePath(itemElement.getElementsByTagName("imgpath").item(0).getTextContent().trim());
                foodList.add(f);
            }
            if (isToy(itemDescription)) {
                Toy t = new Toy();
                t.setID(Integer.parseInt(itemElement.getElementsByTagName("id").item(0).getTextContent()));
                t.setName(itemElement.getElementsByTagName("name").item(0).getTextContent());
                t.setDescription(itemElement.getElementsByTagName("description").item(0).getTextContent());
                t.setCost(Integer.parseInt(itemElement.getElementsByTagName("cost").item(0).getTextContent().trim()));
                t.setEffect(Integer.parseInt(itemElement.getElementsByTagName("effect").item(0).getTextContent().trim()));
                t.setItemType(Resource.TOY);
                t.setImagePath(itemElement.getElementsByTagName("imgpath").item(0).getTextContent().trim());
                toyList.add(t);
            }
        }
        if (foodList.size() < (buttonCount - 1) || toyList.size() < (buttonCount - 1)) {
            if (foodList.size() < (buttonCount - 1)) {
                for (int i = foodList.size(); i < 8; i++) {
                    Food fakeFood = new Food();
                    fakeFood.setName("Fake");
                    fakeFood.setCost(0);
                    fakeFood.setDescription("to come...");
                    fakeFood.setEffect(0);
                    fakeFood.setItemType("Food");
                    fakeFood.setImagePath("fakeBtn.png");
                    foodList.add(fakeFood);
                }
            }
            if (toyList.size() < (buttonCount - 1)) {
                for (int i = toyList.size(); i < 8; i++) {
                    Toy fakeToy = new Toy();
                    fakeToy.setName("Fake");
                    fakeToy.setCost(0);
                    fakeToy.setDescription("to come...");
                    fakeToy.setEffect(0);
                    fakeToy.setItemType("Toy");
                    fakeToy.setImagePath("fakeBtn.png");
                    toyList.add(fakeToy);
                }
            }
        }
    }

    public static ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        ItemController.foodList = foodList;
    }

    public static ArrayList<Toy> getToyList() {
        return toyList;
    }

    public void setToyList(ArrayList<Toy> toyList) {
        ItemController.toyList = toyList;
    }

    public static Item getItemById(int i) {
        try {
            for (Food f : foodList) {
                if (f.getID() == i) {
                    Food fItem = new Food();
                    fItem.setCost(f.getCost());
                    fItem.setDescription(f.getDescription());
                    fItem.setEffect(f.getEffect());
                    fItem.setID(f.getID());
                    fItem.setItemType(f.getItemType());
                    fItem.setName(f.getName());
                    fItem.setItemCount(0);
                    fItem.setImagePath(f.getImagePath());
                    return fItem;
                }
            }
            for (Toy t : toyList) {
                if (t.getID() == i) {
                    Toy tItem = new Toy();
                    tItem.setCost(t.getCost());
                    tItem.setDescription(t.getDescription());
                    tItem.setEffect(t.getEffect());
                    tItem.setID(t.getID());
                    tItem.setItemType(t.getItemType());
                    tItem.setName(t.getName());
                    tItem.setItemCount(0);
                    tItem.setImagePath(t.getImagePath());
                    return tItem;
                }
            }
        } catch (Exception e) {
            System.err.println("ItemController.getItemById(int i): Es konnte leider kein Item mit der entsprechenden ID gefunden werden.");
        }
        return null;
    }

    public static boolean isFood(String description) {
        try {
            if (description.equals("Gem�se") || description.equals("Fleisch") || description.equals("Naschzeug")) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("ERROR while ItemController.isFood(String description)");
        }
        return false;
    }

    public static boolean isToy(String description) {
        try {
            if (description.equals("Spielzeug") || description.equals("Puppe") || description.equals("Stofftier")) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("ERROR while ItemController.isFood(String description)");
        }
        return false;
    }

    public static JImage loadItemImage(Item item) {
        JImage image = null;
        Image img = null;
        String prePath = "";
        if (isFood(item.getDescription())) {
            prePath = "image/shopIcons/food/";
        } else if (isToy(item.getDescription())) {
            prePath = "image/shopIcons/toys/";
        } else if (item.getName().equals("Fake")) {
            prePath = "image/shopIcons/";
        }
        try {
            img = ResourceManager.loadImage(prePath + item.getImagePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = new JImage(img, JImage.ScaleMode.BOTH, JImage.Anchor.MIDDLE, true);
        return image;
    }
}
