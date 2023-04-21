package visugraph.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import visugraph.data.Attribute;
import visugraph.graph.Graph;

/**
 * <p>Interface spécifiant les méthodes pour importer
 * des graphes depuis des fichiers.</p>
 * 
 * <p>L'importeur utilisera son propre type de graphe indépendamment du fichier.
 * L'utilisateur devra donc s'assurer que le graphe d'arrivée supporte le format utilisé par le fichier.</p>
 */
public interface GraphImporter<N, E> {

    /**
	 * Charge un graphe depuis un fichier.
	 * 
	 * @param filename Chemin vers le fichier contenant le graphe
	 * @throws IOException si un erreur d'écriture survient.
	 */
    Graph<N, E> load(String filename) throws IOException;

    /**
	 * Charge un graphe en utilisant un flux.
	 * @param stream flux à utiliser pour charger le graphe.
	 * @throws IOException si une erreur d'écriture survient.
	 */
    Graph<N, E> load(InputStream stream) throws IOException;

    /**
	  * Retourne la liste des attributs sur les noeuds chargés
	  * lors du dernier appel à load(). Si aucun appel à load n'a eu
	  * lieu ou si l'importeur ne gère pas les attributs, une liste
	  * vide est retournée.
	  */
    List<Attribute<N, ?>> getNodeAttributes();

    /**
	  * Retourne la liste des attributs sur les arêtes chargés
	  * lors du dernier appel à load(). Si aucun appel à load n'a eu
	  * lieu ou si l'importeur ne gère pas les attributs, une liste
	  * vide est retournée.
	  */
    List<Attribute<E, ?>> getEdgeAttributes();

    /**
	 * Indique si la conversion est avec ou sans perte.
	 * Dans une conversion avec perte, certaines informations sont perdues.
	 * Cela ne peut arriver que si l'importeur ne supporte pas toutes les fonctionnalités du format.
	 */
    boolean isLossless();
}
