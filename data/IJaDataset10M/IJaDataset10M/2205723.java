package visugraph.graph;

/**
 * Exception qui indique qu'un graphe comporte des arêtes/arcs de poids négatifs.
 * Cette situation rend impossible l'exécution de certains algorithmes.
 */
public class NegativeEdgeException extends GraphException {

    private static final long serialVersionUID = 0x42;

    public NegativeEdgeException() {
        super();
    }

    public NegativeEdgeException(String message) {
        super(message);
    }
}
