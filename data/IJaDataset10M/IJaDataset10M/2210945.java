package com.leibict.ussd;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Dhanika
 */
public class Node {

    public static HashMap currentStates = new HashMap<Integer, String>();

    public static HashMap titles = new HashMap<String, String>();

    public static HashMap choices = new HashMap<String, String>();

    public static String title;

    public static String choiceList;

    public static void updateDatabase() {
        titles.put("MAIN_MENU", "Welcome to GeoAlert services.\nSelect the option.\n");
        titles.put("REGISTER", "You will be charged Rs.20 per month for this service.\nAre you sure you want to register?\n");
        titles.put("REGISTER_YES", "You are successfully registed!\n");
        titles.put("REGISTER_NO", "Thanks for using GeoAlert!\n");
        titles.put("SET_DEST", "Please enter your new destination.\n");
        titles.put("SET_DEST_CONFIRM", "Are you sure that you want to save the above destination:\n");
        titles.put("SET_DEST_CONFIRM_YES", "Destination successfully saved in your destination list.\n");
        titles.put("SAVED_DEST", "You haven't saved any destination yet.\n");
        titles.put("EXIT", "Thank you for using GeoAlert service!\n");
        titles.put("DYNAMIC", "Enter the first few letters of the Destination ");
        titles.put("INVALID", "Invalid input.\n");
        choices.put("MAIN_MENU", "1. Register\n2. Set a Destination\n3. Saved Destinations\n4. Select Destination\n5. Exit");
        choices.put("REGISTER", "1. Yes\n2. No");
        choices.put("REGISTER_YES", "1. Go to Main menu\n2. EXit");
        choices.put("SET_DEST_CONFIRM", "1. Yes\n2. No");
        choices.put("SET_DEST_CONFIRM_YES", "1. Go to Main menu\n2. EXit");
        choices.put("SAVED_DEST", "1. Go to Main menu\n2. EXit");
        choices.put("DYNAMIC", " ");
        choices.put("INVALID", "1. Try again\n2. Go to Main menu\n3. EXit");
    }

    public static String getResponse(int dialogId, String state) {
        updateDatabase();
        currentStates.put(dialogId, state);
        title = (String) titles.get(state);
        System.out.println("CurrentState: " + currentStates.get(dialogId) + ", Tile: " + title);
        try {
            choiceList = (String) choices.get(state);
            return title + choiceList;
        } catch (Error e) {
            return title;
        }
    }
}
