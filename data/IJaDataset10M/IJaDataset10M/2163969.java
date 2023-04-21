package mecca.object;

import mecca.util.Logger;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Role {

    private String name;

    private String description;

    private Logger log;

    private String className = "mecca.object.Role";

    private boolean logger = false;

    public Role() {
        name = "";
        description = "";
        if (logger) log = new Logger(className);
    }

    /**
 * Setter method to set the String attribute, <i>name</i>.
 */
    public void setName(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
    }

    /**
 * Getter method to get the String attribute, <i>name</i>.
 */
    public String getName() {
        return name;
    }

    /**
 * Setter method to set the String attribute, <i>description</i>.
 */
    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    /**
 * Getter method to get the String attribute, <i>description</i>.
 */
    public String getDescription() {
        return description;
    }
}
