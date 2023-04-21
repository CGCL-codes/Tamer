package sjrd.tricktakinggame.rules;

import sjrd.tricktakinggame.game.*;

/**
 * Fournisseur de règles
 * @author sjrd
 */
public interface RulesProvider {

    /**
     * ID de ce fournisseur de règles
     * @return ID
     */
    public String getID();

    /**
     * Nom du jeu
     * @return Nom du jeu
     */
    public String getName();

    /**
     * Nombre minimum de joueurs
     * @return Nombre minimum de joueurs
     */
    public int getMinPlayerCount();

    /**
     * Nombre maximum de joueurs
     * @return Nombre maximum de joueurs
     */
    public int getMaxPlayerCount();

    /**
     * Crée un objet règles pour un nombre donné de joueurs
     * @param playerCount Nombre de joueurs
     * @return Règles
     */
    public Rules newRules(int playerCount);
}
