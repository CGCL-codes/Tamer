package rothag.models.ressources;

import rothag.models.ModelBase;

/**
 * @version 0.1 
 * @author gulian.lorini
 */
public abstract class Ressource extends ModelBase {

    protected int indice;

    protected int[] listeValeurs;

    public Ressource() {
        this.indice = 0;
    }

    /**
     * @return Retourne le nombre de ressource après en avoir ajouté
     */
    public int addOneRessource() {
        try {
            return listeValeurs[++indice];
        } catch (Exception e) {
            return listeValeurs[--indice];
        }
    }

    /**
     * @return Retourne le nombre de ressource après en avoir ajouté
     */
    public int addRessource(int nbRessource) {
        try {
            int retour = listeValeurs[indice];
            for (int i = 0; i < nbRessource; i++) {
                retour = listeValeurs[++indice];
            }
            return retour;
        } catch (Exception e) {
            return listeValeurs[--indice];
        }
    }

    /**
     * @return Retourne le nombre de ressource après en avoir retiré une
     */
    public int deleteOneRessource() {
        try {
            return listeValeurs[--indice];
        } catch (Exception e) {
            return listeValeurs[++indice];
        }
    }

    /**
     * @return Retourne le nombre de ressource après en avoir retiré une
     */
    public int deleteRessource(int nbRessource) {
        try {
            int retour = listeValeurs[indice];
            for (int i = 0; i < nbRessource; i++) {
                retour = listeValeurs[--indice];
            }
            return retour;
        } catch (Exception e) {
            return listeValeurs[++indice];
        }
    }

    /**
     * @return Vide une ressource et retourne la valeur initial,
     * pour l'achat de developpement.
     */
    public int deleteAllRessources() {
        int value = this.getQuantite();
        this.indice = 0;
        return value;
    }

    /**
     * Get the value of indice
     * @return the value of indice
     */
    public int getQuantite() {
        return listeValeurs[indice];
    }

    /**
     * Retourne le nombre de ressource et non la quantité
     * @return Nombre de ressource
     */
    public int getIndice() {
        return indice;
    }

    /**
     * Retourne la liste des valeurs de la ressource
     * @return
     */
    public int[] getListeValeurs() {
        return listeValeurs;
    }
}
