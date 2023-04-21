package visugraph.graph;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Cette classe ne fait qu'implémenter les méthodes concernant les listeners.
 * Les classes héritées devront appeler <code>fireGraphEvent(), fireNodeEvent()
 * ou fireEdgeEvent()</code> lorsqu'aura lieu un ajout ou une suppression de noeuds et
 * d'arêtes. Les listeners sont déclenchés dans l'ordre dans lequel ils ont été ajoutés.</p>
 *
 * <p>Cette classe contient également différentes petites méthodes qui peuvent aider
 * à l'implémentation de l'interface <code>Graph</code>.</p>
 */
public abstract class ListenableGraph<N, E> implements Graph<N, E> {

    private final Set<GraphListener<N, E>> listeners;

    private final List<GraphListener<N, E>> copyListeners;

    /**
	 * Initialisation...
	 */
    protected ListenableGraph() {
        this.listeners = new LinkedHashSet<GraphListener<N, E>>();
        this.copyListeners = new ArrayList<GraphListener<N, E>>();
    }

    public void addGraphListener(GraphListener<N, E> listener) {
        this.listeners.add(listener);
    }

    public void removeGraphListener(GraphListener<N, E> listener) {
        this.listeners.remove(listener);
    }

    /**
	 * Lance une UnknownNodeException si le noeud n'est pas dans le graphe.
	 *
	 * @param node noeud sur lequel se porte l'assertion.
	 * @throws UnknownNodeException si le noeud n'est pas dans le graphe.
	 */
    protected void assertNode(N node) {
        if (!this.containsNode(node)) {
            throw new UnknownNodeException("Le noeud indiqué n'existe pas dans le graphe");
        }
    }

    /**
	 * Lance une UnknownEdgeException si l'arête n'est pas dans le graphe.
	 * @param edge arête sur laquelle se porte l'assertion.
	 * @throws UnknownEdgeException si l'arête n'est pas dans le graphe.
	 */
    protected void assertEdge(E edge) {
        if (!this.containsEdge(edge)) {
            throw new UnknownEdgeException("L'arête indiquée n'existe pas dans le graphe");
        }
    }

    /**
	 * Teste si l'objet passé en paramêtre est null.
	 * @param obj objet à tester
	 * @throws IllegalArgumentException si l'objet est nulle.
	 */
    protected void assertNonNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Vous ne pouvez ajouter d'élément null au graphe.");
        }
    }

    /**
	 * Déclenche les listeners sur un évènement et un noeud particulier.
	 * @param type type d'évènement à déclencher.
	 * @param node noeud concerné par l'évènement.
	 */
    protected void fireNodeEvent(EventType type, N node) {
        this.fireGraphEvent(type, node, null);
    }

    /**
	 * Déclenche les listeners sur un évènement et une arête particuliere.
	 * @param type type d'évènement à déclencher.
	 * @param edge arête concernée par l'évènement.
	 */
    protected void fireEdgeEvent(EventType type, E edge) {
        this.fireGraphEvent(type, null, edge);
    }

    /**
	 * Déclenche les listeners sur un évènement général survenant sur le graphe.
	 * Exemple : NODES_CLEARED ou EDGES_CLEARED.
	 * @param type type d'évènement à déclencher.
	 */
    protected void fireGraphEvent(EventType type) {
        this.fireGraphEvent(type, null, null);
    }

    /**
	 * Déclenche les listeners sur un évènement particulier.
	 * @param type type d'évènement à déclencher
	 * @param node noeud concerné par l'évènement ou null si aucun.
	 * @param edge arête concernée par l'évènement ou null si aucune.
	 */
    private void fireGraphEvent(EventType type, N node, E edge) {
        if (this.listeners.size() > 0) {
            this.copyListeners.clear();
            this.copyListeners.addAll(this.listeners);
            for (GraphListener<N, E> oneListener : this.copyListeners) {
                switch(type) {
                    case NODE_ADDED:
                        oneListener.nodeAdded(this, node);
                        break;
                    case NODE_REMOVED:
                        oneListener.nodeRemoved(this, node);
                        break;
                    case NODE_REMOVING:
                        oneListener.nodeIsRemoving(this, node);
                        break;
                    case NODES_CLEARED:
                        oneListener.nodesCleared(this);
                        break;
                    case EDGE_ADDED:
                        oneListener.edgeAdded(this, edge);
                        break;
                    case EDGE_REMOVED:
                        oneListener.edgeRemoved(this, edge);
                        break;
                    case EDGE_REMOVING:
                        oneListener.edgeIsRemoving(this, edge);
                        break;
                    case EDGES_CLEARED:
                        oneListener.edgesCleared(this);
                        break;
                    default:
                }
            }
        }
    }

    /**
	 * Enum listant les différents types d'évènements utilisés par les méthodes fire.
	 */
    protected enum EventType {

        NODE_ADDED, NODE_REMOVED, NODE_REMOVING, NODES_CLEARED, EDGE_ADDED, EDGE_REMOVED, EDGE_REMOVING, EDGES_CLEARED
    }
}
