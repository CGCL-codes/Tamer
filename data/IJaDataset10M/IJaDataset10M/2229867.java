package ggc.db.datalayer;

import ggc.db.hibernate.MealGroupH;

public class MealGroup extends MealGroupH {

    public boolean debug = false;

    public static final int MEAL_GROUP_MEALS = 1;

    public static final int MEAL_GROUP_NUTRITION = 2;

    public MealGroup() {
        this.setId(0);
    }

    public String getShortDescription() {
        return "";
    }

    @Override
    public String toString() {
        return this.getShortDescription();
    }

    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName() {
        return "Food Group";
    }

    /**
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public boolean isDebugMode() {
        return debug;
    }

    /**
     * getAction - returns action that should be done on object
     *    0 = no action
     *    1 = add action
     *    2 = edit action
     *    3 = delete action
     *    This is used mainly for objects, contained in lists and dialogs, used for 
     *    processing by higher classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction() {
        return 0;
    }
}
