package galaxiia.jeu.terrain;

import galaxiia.jeu.mur.InformateurMur;
import galaxiia.jeu.souffle.InformateurSouffle;
import galaxiia.jeu.unite.InformateurUnite;
import java.io.Serializable;

/**
 * Interface représentant le terrain de jeu.
 */
public interface InformateurTerrain extends Serializable {

    /**
	 * Précise la taille du terrain. La taille est représentée par un tableau dont
	 * le premier élément est la largeur et le deuxième la hauteur.
	 * 
	 * @return taille du terrain.
	 */
    public double[] taille();

    /**
	 * Donne les murs présents sur le terrain.
	 * 
	 * @return un tableau des murs présents.
	 */
    public InformateurMur[] tableauMurs();

    /**
	 * Précise les souffles présents sur le terrain.
	 * 
	 * @return un tableau des souffles présents sur le terrain.
	 */
    public InformateurSouffle[] tableauSouffles();

    /**
	 * Précise les unités présentes sur le terrain.
	 * 
	 * @return un tableau des unités présentes sur le terrain.
	 */
    public InformateurUnite[] tableauUnites();

    /**
	 * Récupère les informations sur l'unité ayant l'ID <code>id</code>.
	 * 
	 * @param id
	 *          ID de l'unité
	 * @return les informations sur l'unité d'ID <code>id</code>.
	 */
    public InformateurUnite unite(long id);

    public boolean[][] alliances();

    public InformateurSouffle souffle(long id);

    public double distanceBordCarte(double[] point);

    /**
	 * Récupère les informations sur le souffle ayant l'ID <code>id</code>.
	 * 
	 * @param id
	 *          ID du souffle
	 * @return les informations sur le souffle d'ID <code>id</code>.
	 */
    public InformateurTour informateurTour();
}
