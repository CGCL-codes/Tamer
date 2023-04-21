package jsynoptic.builtin.resources;

import simtools.ui.MenuResourceBundle;

/**
 * Default resources for LinesShape
 * 
 * French translation : This file uses the ISO 8859-1 encoding. If this encoding
 * is not the default for your platform, it must be compiled with the javac flag :
 * -encoding ISO8859_1 This is taken into account in the Ant build file:
 * build.xml See this file if you want to add another translation.
 */
public class LinesShapeResources_fr extends MenuResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = { { "ClickLeftMouseButtonToAddAPoint,RightMouseButtonToFinish", "Utiliser le bouton gauche de la souris pour ajouter un point, le droit pour terminer." }, { "CloseLine", "Fermer la forme" }, { "ConvertToPolygon", "Convertir en polygone" }, { "CouplesOfX,YPoints:", "Couples de points X,Y: " }, { "EditPoints", "Editer les points" }, { "Line", "Ligne" } };
}
