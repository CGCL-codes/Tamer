package tm.configuration;

import java.util.*;
import tm.utilities.Assert;

/**
 * Couplets are basically just (name, pair) strings for saving configuration
    information in text readable/editable format. 
 *
 * @author mpbl
 */
public class Couplet {

    public String name;

    public String value;

    public String leadingText = null;

    public boolean isActive = false;

    Couplet(XMLParamTag param) {
        if (param != null) name = param.getAttName();
        value = param.getText();
        isActive = true;
    }

    Couplet(String name) {
        this.name = name;
    }

    public String toString() {
        String rValue = "";
        if (leadingText != null) rValue = leadingText + "\n";
        rValue += name + ": " + value;
        return rValue;
    }
}
