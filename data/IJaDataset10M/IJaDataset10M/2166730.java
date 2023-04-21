package net.sf.ideoreport.common.config.alerters;

import java.util.List;

/**
 * Contient une liste de d�finitions d'alerteurs
 * 
 * @author jbeausseron
 */
public interface IAlertersDefinition {

    /**
	 * Ajoute une d�finition d'alerteur
	 * @param pAlerterDefinition la d�finition de l'alerteur
	 */
    public abstract void addAlerterDefinition(IAlerterDefinition pAlerterDefinition);

    /**
	 * @return Returns the alertersDefinitions.
	 */
    public abstract List getAlertersDefinitions();

    /**
     * Renvoie la d�finition d'un alerteur en fonction de son ID
     * @param pId id de l'alerteur recherch�
     * @return la d�finition de l'alerteur, ou null si non trouv�e
     */
    public IAlerterDefinition getAlerterDefinition(String pId);
}
