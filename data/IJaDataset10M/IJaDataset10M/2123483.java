package job;

/**
* Repr�sente les diff�rents types d'options que le joueur peut modifier pour le jeu
* @author Laurent
*/
public interface UserOption {

    /** Retourne le niveau de départ */
    public int getInitLvl();

    /** Retourne le temps qu'on décremente a chaque niveau */
    public float getTimerPas();

    /** retourn la taille x en nombre de block du playground */
    public int getPlayGroundX();

    /** retourn la taille y en nombre de block du playground */
    public int getPlayGroundY();
}
