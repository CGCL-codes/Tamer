package jsynoptic.ui.resources;

import simtools.ui.StringsResourceBundle;

/**
 * Parametrable messages for JSynoptic
 * 
 * @author Nicolas Brodu
 * 
 * @version 1.0 1999
 */
public class SourceTreeMessages_fr extends StringsResourceBundle {

    public String[][] getContents() {
        return contents;
    }

    static final String[][] contents = { { "noSelection", "Veuillez s�lectionner une source de donn�es ou un objet dans le panneau de gauche." }, { "sourceName", "Nom: ", null }, { "sourceId", "ID: ", null }, { "sourceComment", "Commentaire: ", null }, { "sourceUnit", "Unit�: ", null }, { "noInfo", "Aucune information disponible pour cette s�lection." }, { "sourceInfo", "Informations sur la source de donn�es" }, { "sourceLinkedId", "ID de la source li�e: ", null }, { "displayLength", "Nombre de digits affich�s : ", null }, { "displayFormat", "Format d'affichage :  ", null } };
}
