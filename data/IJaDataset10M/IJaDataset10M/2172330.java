package com.loribel.commons.util.string;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.util.STools;

/**
 * Class to make an action on a String.
 * <p>
 * Remplace les tags texte avec parenth�ses par de vrais tags HTML.
 * <p>
 *
 * @author Gregory Borelli
 */
public class GB_SATxtTagToHtmlTag extends GB_SAAbstract {

    /**
     * Constructor of GB_SATxtTagToHtmlTag without parameter.
     */
    public GB_SATxtTagToHtmlTag() {
        super();
    }

    /**
     * Remplace les tags texte avec parenth�ses par de vrais tags HTML.
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
        GB_StringReplaceList l_list = new GB_StringReplaceListHtmlTagToTxtTag();
        l_list.reverse();
        return l_list.doActionStr(a_string);
    }

    /**
     * Retourne la description de l'action.
     * <p>
     *
     * @return String
     */
    public String getDescription() {
        return "Remplace les tags texte avec parenth�ses par de vrais tags HTML.";
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
        return "TxtTagToHtmlTag";
    }
}
