package com.game;

import com.Main;
import com.data.GameElement;
import com.data.elements.dynamic.Landmark;
import com.data.elements.Location;
import com.data.elements.dynamic.Room;
import com.data.elements.dynamic.Town;
import com.display.GameRenderer;
import com.display.GameText;

/**
 *
 * @author Master
 */
public class Actions {

    public static void moneyGain(int amount) {
        Main.getPlayerData().increaseMoney(amount);
    }

    public static void moneyLoss(int amount) {
        Main.getPlayerData().decreaseMoney(amount);
    }

    public static void itemGain(String item) {
        Main.getPlayerData().getInventory().add(item);
    }

    public static void itemLoss(String item) {
        Main.getPlayerData().getInventory().remove(item);
    }

    public static void allowAccess(Location location) {
        System.out.println("Access was allowed into " + location.getFullName());
        Main.getPlayerData().addAccessToLocation(location);
    }

    public static void locationChange(String town, String landmark, String room) {
        Town newTown = null;
        Landmark newLandmark = null;
        Room newRoom = null;
        newTown = Town.getTown(town);
        if (landmark.equals("") == false) {
            newLandmark = newTown.getLandmark(landmark);
            if (room.equals("") == false) newRoom = newLandmark.getRoom(room);
        }
        if (newTown != null && Main.getPlayerData().hasAccessTo(newTown) == false) {
            newTown.dispatchEvent(Main.getGraphics(), "deniedAccess");
            return;
        }
        if (newLandmark != null && Main.getPlayerData().hasAccessTo(newLandmark) == false) {
            newLandmark.dispatchEvent(Main.getGraphics(), "deniedAccess");
            return;
        }
        if (newRoom != null && Main.getPlayerData().hasAccessTo(newRoom) == false) {
            newRoom.dispatchEvent(Main.getGraphics(), "deniedAccess");
            return;
        }
        Main.getPlayerData().setTown(town);
        if (newLandmark != null) Main.getPlayerData().setLandmark(landmark);
        if (newRoom != null) Main.getPlayerData().setRoom(room);
        if (newRoom != null) newRoom.dispatchEvent(Main.getGraphics(), "access"); else if (newLandmark != null) newLandmark.dispatchEvent(Main.getGraphics(), "access"); else newTown.dispatchEvent(Main.getGraphics(), "access");
    }

    public static void locationChange(Location locationElement) {
        String[] split = locationElement.getFullName().split(".");
        locationChange(split);
    }

    public static void locationChange(String[] split) {
        if (split.length == 1) locationChange(split[0], "", ""); else if (split.length == 2) locationChange(split[0], split[1], ""); else if (split.length == 3) locationChange(split[0], split[1], split[2]);
    }

    public static void locationChange(String location) {
        Location playerLocation = Main.getPlayerData().getLocation();
        for (GameElement element : playerLocation.getChildren()) {
            if (Location.class.isInstance(element)) {
                Location locationElement = (Location) element;
                locationChange(locationElement);
                return;
            }
        }
        if (playerLocation.hasParent()) {
            if (Location.class.isInstance(playerLocation.getParent())) {
                Location locationElement = (Location) playerLocation.getParent();
                locationChange(locationElement);
            }
        }
    }

    public static void showMessage(String text) {
        GameRenderer.addGameText(new GameText(text));
    }
}
