package org.fudaa.ebli.calque.edition;

import java.awt.Graphics2D;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ebli.calque.ZCalqueAffichageDonneesInterface;
import org.fudaa.ebli.calque.ZCalqueGeometry.SelectionMode;
import org.fudaa.ebli.geometrie.GrObjet;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.trace.TraceIcon;

/**
 * Une interface a implementer pour tout calque editable. Un calque editable ne peut contenir qu'un modele editable.
 * @author Fred Deniger
 * @version $Id: ZCalqueEditable.java,v 1.5.8.4 2008-03-26 16:46:43 bmarchan Exp $
 */
public interface ZCalqueEditable extends ZCalqueAffichageDonneesInterface {

    /**
   * @param _g2d le graphics support
   * @param _dx le dx ecran
   * @param _dy le dy ecran
   * @param _ic l'icone a utiliser
   */
    void paintDeplacement(Graphics2D _g2d, int _dx, int _dy, TraceIcon _ic);

    /**
   * @param _cmd le receveur de commande
   * @param _ui l'interface utilisateur
   * @return true si commande reussie
   */
    boolean removeSelectedObjects(CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * @param _reelDx l'ecart reel demande en x
   * @param _reelDy l'ecart reel demande en y
   * @param _reelDz l'ecran reel demand� en z.
   * @param _cmd le receveur de commande
   * @param _ui l'interface utilisateur
   * @return true is commande reussie
   */
    boolean moveSelectedObjects(double _reelDx, double _reelDy, double _reelDz, CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * Rotation des objets selectionn�s. Les objets sont transform�s autour d'une origine unique ou de leur barycentre.
   * 
   * @param _angRad L'angle de rotation en radians.
   * @param _xreel0 La coordonn�e X du pt d'origine.
   * @param _yreel0 La coordonn�e Y du pt d'origine.
   * @param _cmd le receveur de commande
   * @param _ui l'interface utilisateur
   * @return true is commande reussie
   */
    boolean rotateSelectedObjects(double _angRad, double _xreel0, double _yreel0, CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * Duplique les objets s�lectionn�s avec un d�calage suivant X et Y.
   * @param _cmd le receveur de commande
   * @param _ui l'interface utilisateur
   * @return true is commande reussie
   */
    public boolean copySelectedObjects(CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * Scinde une geometrie.
   * @param _cmd le receveur de commande
   * @param _ui l'interface utilisateur
   * @return true is commande reussie
   */
    public boolean splitSelectedObject(CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * Joint 2 g�ometries.
   * @param _cmd La pile de commandes.
   * @param _ui l'interface utilisateur
   * @return true si les objets ont �t� joints.
   */
    public boolean joinSelectedObjects(final CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * Retourne si la forme d'un certain type peut �tre ajout�e.
   * @param _typeForme Le type de forme
   * @return true si ce type de forme peut �tre ajout�.
   */
    boolean canAddForme(int _typeForme);

    /**
   * @param _o l'objet geometrique
   * @param _deforme la forme
   * @param _cmd le receveur de commandes
   * @param _ui l'interface utilisateur
   * @param _data les donn�es associ�es
   * @return true si ajout�e
   */
    boolean addForme(GrObjet _o, int _deforme, CtuluCommandContainer _cmd, CtuluUI _ui, ZEditionAttributesDataI _data);

    /**
   * Ajoute un sommet a l'objet selectionn�. Si le nombre d'objets selectionn�s est diff�rent de 1,
   * le point n'est pas ajout�.
   *  
   * @param _ptReel le point 3D r�el de l'atome a ajouter
   * @param _cmd le receveur de command
   * @param _ui l'interface utilisateur
   * @return true si atome ajoute
   */
    boolean addAtome(GrPoint _ptReel, CtuluCommandContainer _cmd, CtuluUI _ui);

    /**
   * @return true si le mode atomique est autorise
   */
    boolean canUseAtomicMode();

    /**
   * @return true si le mode segment est autorise
   */
    boolean canUseSegmentMode();

    /**
   * @return le mode de selection.
   */
    SelectionMode getSelectionMode();

    /**
   * @param mode le nouveau mode
   * @return true si changement
   */
    boolean setSelectionMode(SelectionMode mode);

    /**
   * @return true si en mode atomique
   */
    boolean isAtomicMode();

    /**
   * @return le modele editable
   */
    ZModeleEditable getModelEditable();

    /**
   * @return L'editeur, notamment pour l'edition de la g�om�trie s�lectionn�e.
   */
    ZEditorInterface getEditor();

    /**
   * D�finit l'editeur.
   * @param _editor L'editeur, notamment pour l'edition de la g�om�trie s�lectionn�e.
   */
    void setEditor(final ZEditorInterface _editor);
}
