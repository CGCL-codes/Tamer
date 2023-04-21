package siteWeb;

import java.util.List;
import javax.ejb.Remote;

/**
 * 
 * @author aitelhaj
 *
 */
@Remote
public interface Catalogue {

    /**
	 * Crée un article et l'enregistre en base
	 * @param referenceArt
	 * @param nomArt
	 * @param prixArt
	 * @param descriptionArt
	 * @param categorieArticle
	 * @return
	 */
    public ItemBean creerArticle(int quantite, String nomArt, double prixArt, String descriptionArt, String categorieArticle);

    /**
	 * Supprime de la base l'article dont la référence est passée en paramètre
	 * @param ref
	 */
    public void supprimerArticle(int ref);

    public void incrementerStock(int refProduit);

    public void decrementerStock(int refProduit);

    public void updateStock(int refProduit, int quantite);

    /**
	 * Modifie les attributs d'un article(sauf la référence)
	 * @param a
	 */
    public void modifierArticle(ItemBean a);

    /**
	 * Récupère l'article dont le nom est passé en paramètre
	 * @param nom
	 * @return Article
	 */
    public ItemBean getArticleByNom(String nom);

    /**
	 * Récupère l'article dont la référence est passée en paramètre
	 * @param ref
	 * @return Article
	 */
    public ItemBean getArticleByRef(int ref);

    /**
	 * Récupere la liste de tous les articles
	 * @return List
	 */
    public List<ItemBean> getAllArticles();
}
