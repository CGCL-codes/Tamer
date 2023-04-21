package com.avian.foods.basefoods;

import com.avian.util.MilliTimeString;

/**
 * The contents of this food object is the "work package" that birds digest. StdFood
 * uses a String object as a work package, but other food objects could use other
 * objects for their contents. For example, the contents object could contain 2 fields,
 * f1 & f2 and the next food object would have a contents constructor that used the
 * 2 fields object to create a new contents object with 3 fields (f1, f2, & f3), etc.
 * This object also maintains a history of the events that happened to it (who ate it).
 *
 * @author nchamberlain
 */
public class StdFood implements Manna {

    private String contents;

    private String description;

    private String foodHistory;

    private String foodType;

    private MilliTimeString mts = new MilliTimeString();

    /**
     * Default constructor for this food, it sets a few reasonable values for
     * its variables.
     *
     */
    public StdFood() {
        contents = "";
        foodHistory = "";
        foodType = "Std";
        description = "Standard Generic Food";
    }

    /**
     * Constructor for this food that sets its variables to the params that were
     * passed in.
     *
     * @param newContents is the String to put into the contents field
     * @param newHistory is the String to put into the history field
     * @param newType is the String that represents the type food or the String
     * "FILE_OP" if the food type that was passed in from the Aviary equals "N/A"
     */
    public StdFood(String newContents, String newHistory, String newType) {
        contents = newContents;
        foodHistory = newHistory;
        description = "Standard Generic Food";
        if (newType.equalsIgnoreCase("N/A")) {
            newType = "FILE_OP";
        }
        foodType = newType;
    }

    /**
     * Constructor for this food that sets its variables to the params that were
     * passed in.
     *
     * @param newContents is the String to put into the contents field
     * @param newHistory is the String to put into the history field
     * @param newType is the String that represents the type food or the String
     * "FILE_OP" if the food type that was passed in from the Aviary equals "N/A"
     * @param newDescription is the String to put into the Description field
     */
    public StdFood(String newContents, String newHistory, String newType, String newDescription) {
        contents = newContents;
        foodHistory = newHistory;
        description = newDescription;
        if (newType.equalsIgnoreCase("N/A")) {
            newType = "FILE_OP";
        }
        foodType = newType;
    }

    public void setName(String newName) {
        foodType = newName;
    }

    public String getName() {
        return foodType;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the contents field, which is the work package that the birds digest.
     * @return String with the value in the contents field. If a food implemented
     * a different object as its contents, that type of object would be returned
     * and the calling bird would have to know what to do with that type of object.
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets the value of the contents object, in this case, as String.
     * @param contents is the String value that should be used for this food's content.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * Returns the String contained in the history field.
     * @return String with the contents of the foodHistory field.
     */
    public String getHistory() {
        return foodHistory;
    }

    /**
     * Sets this food object's foodHistory field with the String value passed as
     * a parameter. If a foodHistory existed, it is replaced by the one that is
     * passed as a parameter instead of adding to it's history. This method is not
     * used as often as addToFoodHistory.
     * @param history is the String that will be the new foodHistory.
     */
    public void setHistory(String history) {
        this.foodHistory = history;
    }

    /**
     * Adds a formatted event to the food's history. The formatting includes a
     * millisecond time, the food type, and the event that was passed in, all on
     * one line, with each piece separated by commas so it can be read as a .csv file.
     * @param historyEvent
     */
    public void addToFoodHistory(String historyEvent) {
        if (foodType.equalsIgnoreCase("N/A")) {
            foodType = "FILE_OP";
        }
        this.foodHistory = foodHistory + "\n" + mts.getShortMilliTimeString() + "," + foodType + "," + historyEvent;
    }

    /**
     * Returns the String name for the type of food that is this food object.
     * @return String name of the food object
     */
    public String getFoodType() {
        return foodType;
    }

    /**
     * Sets the type of food using the String that was passed as a parameter.
     * @param foodTypeIN is the String that is the name for the type of food.
     */
    public void setFoodType(String foodTypeIN) {
        if (foodTypeIN.equalsIgnoreCase("N/A")) {
            foodTypeIN = "FILE_OP";
        }
        this.foodType = foodTypeIN;
    }

    @Override
    public String toString() {
        return foodType + "," + contents + ",History=\n" + foodHistory;
    }
}
