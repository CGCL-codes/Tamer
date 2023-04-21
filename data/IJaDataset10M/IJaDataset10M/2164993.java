package com.loribel.commons.util.string;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.util.STools;

/**
 * Class to make an action on a String.
 * <p>
 * Remplace les tags HTML par des tags texte avec parenth�ses.
 * <p>
 *
 * @author Gregory Borelli
 */
public class GB_SAHtmlTagToTxtTag extends GB_SAAbstract {

    /**
     * Constructor of GB_SAHtmlTagToTxtTag without parameter.
     */
    public GB_SAHtmlTagToTxtTag() {
        super();
    }

    /**
     * Remplace les tags HTML par des tags texte avec parenth�ses.
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
        return l_list.doActionStr(a_string);
    }

    /**
     * Retourne la description de l'action.
     * <p>
     *
     * @return String
     */
    public String getDescription() {
        return "Remplace les tags HTML par des tags texte avec parenth�ses.";
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
        return "HtmlTagToTxtTag";
    }
}
