package org.fudaa.fudaa.modeleur.modeleur1d.model;

import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.gis.GISZoneCollectionLigneBrisee;
import org.fudaa.ebli.calque.edition.ZModeleLigneBriseeEditable;

/**
 * Une interface permettant d'acc�der aux informations du bief selectionn�.
 * 
 * @author Emmanuel MARTIN
 * @version $Id: BiefContainerI.java 6476 2011-10-04 08:07:21Z bmarchan $
 */
public interface BiefContainerI extends ProfileSetI {

    /** Retourne vrai si l'index pass� en param�tre correspond � un profil. */
    public boolean hasProfil(int _idxProfil);

    /** Retourne vrai si un axe hydraulique existe */
    public boolean hasAxeHydraulique();

    /** Retourne la valeur de d�calage de l'axe hydraulique. */
    public double getDecalageAxeHydraulique();

    /** Ajout d'un nouveau listener. */
    public void addBiefContainerListener(BiefContainerListener _listener);

    /** Supprime le listener. */
    public void removeBiefContainerListener(BiefContainerListener _listener);

    /** Retourne les zones contenant les g�om�tries. */
    public GISZoneCollectionLigneBrisee[] getZones();

    /** Retourne la zone contenant l'axe hydraulique. */
    public GISZoneCollectionLigneBrisee getZoneAxeHydraulique();

    /** Retourne la zone contenant les profils. */
    public GISZoneCollectionLigneBrisee getZoneProfils();

    /** Retourne le model contenant les profils. */
    public ZModeleLigneBriseeEditable getModelProfils();

    /** Retourne la zone contenant les rives. */
    public GISZoneCollectionLigneBrisee getZoneRives();

    /** Retourne la zone contenant les limites de stockages. */
    public GISZoneCollectionLigneBrisee getZoneLimitesStockages();

    /** Retourne la zone contenant les lignes directrices. */
    public GISZoneCollectionLigneBrisee getZoneLignesDirectrices();

    /** Retourne le nom du profil choisi par sont index. */
    public String getNomProfil(int _idxProfil);

    /** Renomme le profil. */
    public void renameProfil(int _idxProfil, String _newName, CtuluCommandContainer _cmd);
}
