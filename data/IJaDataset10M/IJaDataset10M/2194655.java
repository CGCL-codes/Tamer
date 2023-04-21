package com.loribel.commons.util.string;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.util.STools;

/**
 * Class to make an action on a String.
 * To lower case.
 *
 * @author Gregory Borelli
 */
public class GB_SALowerCase extends GB_SAAbstract {

    /**
     * Constructor of GB_SALowerCase without parameter.
     */
    public GB_SALowerCase() {
        super();
    }

    /**
     * To lower case.
     * <p>
     *
     * @param a_string String - string sur laquelle l'action doit s'effectuer
     *
     * @return String
     */
    public String doActionStr(String a_string) {
        if (STools.isNull(a_string)) {
            return a_string;
        }
        return a_string.toLowerCase();
    }

    /**
     * Retourne la description de l'action.
     * <p>
     *
     * @return String
     */
    public String getDescription() {
        return "To lower case.";
    }

    /**
     * Retourne la description longue de l'action.
     * <p>
     *
     * @return List
     */
    public List getLongDescription() {
        List retour = new ArrayList();
        return retour;
    }

    /**
     * Retourne le nom de l'action.
     * <p>
     *
     * @return String
     */
    public String getName() {
        return "LowerCase";
    }
}
