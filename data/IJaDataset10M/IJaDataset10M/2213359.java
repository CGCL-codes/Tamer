package com.doculibre.intelligid.navigation;

import java.io.Serializable;
import java.util.List;

/**
 * Représente un noeud de navigation dans le système. 
 * 
 * La navigation se fait soit en fonction du plan de classification, soit en 
 * fonction des unités administratives.
 * 
 * @author Vincent Dussault
 */
public interface NoeudNavigation extends Serializable, Comparable<NoeudNavigation> {

    public static final String MODE_NAVIGATION_PLAN_CLASSIFICATION = "planClassification";

    public static final String MODE_NAVIGATION_UNITES_ADMINISTRATIVES = "unitesAdministratives";

    public static final String MODE_NAVIGATION_DOSSIERS_RECENTS = "dossiersRecents";

    public static final String MODE_NAVIGATION_DOCUMENTS_RECENTS = "documentsRecents";

    public static final String MODE_NAVIGATION_PROJETS = "projets";

    /**
	 * @return Id de l'objet représenté par ce noeud.
	 */
    Long getId();

    /**
	 * @return Titre pour l'objet représenté par ce noeud.
	 */
    String getTitre();

    /**
	 * @return Noeud correspondant à l'objet parent représenté par ce noeud.
	 */
    NoeudNavigation getNoeudParent();

    /**
	 * @return Sous-noeuds correspondant au sous-objets de l'objet représenté par ce noeud.
	 */
    List<NoeudNavigation> getSousNoeuds();

    /**
	 * Recharge le contenu déjà chargé du noeud.
	 */
    void reinitialiser();

    /**
	 * Indique qu'il doit être rechargé lors d'un prochain appel de méthode
	 */
    void detach();

    boolean hasSousNoeuds();
}
