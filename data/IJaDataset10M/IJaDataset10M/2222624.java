package org.fudaa.fudaa.commun.save;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import org.fudaa.ebli.calque.BCalqueSaverInterface;
import org.fudaa.ebli.commun.EbliUIProperties;

/**
 * Une classe contenant les propri�t�s de la vue calque. Faire tres attention lors de la modification de cette classe. NE PAS
 * CHANGER LE NOM CAR ENREGISTRER DANS DES FICHIERS DE PARAMETRES.<p>
 * Remarque : Des champs peuvent �tre ajout�s, mais non retir�s (ou db4o ne sait pas restaurer l'objet).
 * 
 * @author Fred Deniger
 * @version $Id: FilleVisuSaver.java,v 1.7.4.1 2008-04-01 07:20:53 bmarchan Exp $
 */
public final class FilleVisuSaver {

    /** Le nom de la fenetre */
    String name_;

    /** La localisation de la fenetre */
    Point ifLocation_;

    /** */
    Insets userInsets_;

    /** La dimension de la fenetre */
    Dimension ifDim_;

    /** Contient les infos calque de donn�es */
    BCalqueSaverInterface props_;

    /** Les infos calque d'infos */
    BCalqueSaverInterface cqInfosProps_;

    /** Le nom du calque selectionne */
    String selectedLayer_;

    /** Le min X viewport de la fenetre en espace reel */
    double ptMinX_;

    /** Le min Y viewport de la fenetre en espace reel */
    double ptMinY_;

    /** Le max X viewport de la fenetre en espace reel */
    double ptMaxX_ = -1;

    /** Le max Y viewport de la fenetre en espace reel */
    double ptMaxY_ = -1;

    /** Les propri�t�s pour le calque de l�gende */
    EbliUIProperties legendUI_;

    public FilleVisuSaver() {
    }
}
