package visugraph.graph;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import visugraph.plugin.UserPlugin;
import visugraph.util.Factory;

/**
 * <p>Implémentation par défaut des forêts reposant sur la classe SimpleGraph.</p>
 *
 * <p>Plutôt que de vérifier à chaque ajout d'arête que le graphe est bien connexe, cette classe maintient à jour "les niveaux" des noeuds,
 * c'est à dire la profondeur à laquelle ils se situent du noeud parent. Quelques complexités changent, notamment :</p>
 * <ul>
 *	<li>Ajout/Suppression d'une arête : O(N) où N nombre de noeuds de la composante connexe.</li>
 *	<li>Calcul de la profondeur d'un noeud : O(1)</li>
 *	<li>Retourne le noeud racine d'un noeud : O(1)</li>
 *	<li>Le reste est identique</li>
 * </ul>
 *
 * <p>La racine et la profondeur d'un noeud peut changer à tout moment suite à l'ajout
 * ou à la suppression d'autres noeuds ou d'arêtes. L'ordre est cependant garanti lorsque vous
 * ajoutez les noeuds en partant de la racine vers les feuilles.</p>
 *
 */
@UserPlugin("Arborescence")
public class TreeGraph<N, E> extends AbstractGraph<N, E> implements Tree<N, E> {

    /**
	 * Construit une nouvelle forêt en allouant de l'espace pour un certain nombre de noeuds.
	 * Attention seul l'espace mémoire est alloué ! Aucun noeud n'est créé !
	 *
	 * @param nbNodesToAllocate nombre de noeuds dont on souhaite allouer la mémoire.
	 * @param directed true si le graphe doit être orienté.
	 *
	 * @throws IllegalArgumentException si le nombre de noeuds est négatif
	 */
    public TreeGraph(int nbNodesToAllocate, boolean directed) {
        super(nbNodesToAllocate, directed, false);
    }

    /**
	 * Construit une nouvelle forêt.
	 *
	 * @param directed true si le graphe doit être orienté.
	 */
    public TreeGraph(boolean directed) {
        this(100, directed);
    }

    /**
	 * {@inheritDoc}
	 */
    protected void preRemoveEdge(E edge) {
        super.preRemoveEdge(edge);
        N source = this.getEdgeInfos(edge).source;
        N target = this.getEdgeInfos(edge).target;
        this.setDepth(target, source, 0);
        this.setRoot(target, source, target);
        this.setDepth(source, target, 0);
        this.setRoot(source, target, source);
    }

    /**
	 * {@inheritDoc}
	 */
    protected void postAddEdge(E edge) {
        super.postAddEdge(edge);
        N source = this.getEdgeInfos(edge).source;
        N target = this.getEdgeInfos(edge).target;
        this.setDepth(target, source, this.getDepth(source) + 1);
        this.setRoot(target, source, this.getRoot(source));
    }

    /**
	 * {@inheritDoc}
	 * Vous ne pouvez ajouter des arêtes qu'entre deux noeuds de racines différentes.
	 * Les racines et profondeurs des noeuds changent suite à l'appel de cette méthode.
	 *
	 * @throws StructuralException si le graphe formé suite à l'ajout de l'arête n'est pas acyclique.
	 */
    protected void assertAddEdge(E edge, N source, N target) {
        super.assertAddEdge(edge, source, target);
        if (this.getRoot(source).equals(this.getRoot(target))) {
            throw new StructuralException("Le graphe formé doit être acyclique et bien orienté !");
        }
    }

    /**
	 * {@inheritDoc}
	 * Complexité : O(1).
	 */
    public final int getDepth(N node) {
        this.assertNode(node);
        return this.getNodeInfos(node).depth;
    }

    /**
	 * {@inheritDoc}
	 * Complexité : O(1).
	 */
    public final N getRoot(N node) {
        this.assertNode(node);
        return this.getNodeInfos(node).root;
    }

    /**
	 * {@inheritDoc}
	 * Complexité : O(N).
	 */
    public final void setRoot(N node) {
        this.assertNode(node);
        TreeNodeInfos rootInfos = this.getNodeInfos(node);
        if (rootInfos.depth > 0) {
            rootInfos.depth = 0;
            rootInfos.root = node;
            Iterator<E> itEdges = this.edgesIterator(node);
            while (itEdges.hasNext()) {
                E edge = itEdges.next();
                N opposite = this.getOpposite(edge, node);
                this.setDepth(opposite, node, 1);
                this.setRoot(opposite, node, node);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 * Complexité : O(N).
	 */
    public final List<N> rootNodes() {
        List<N> roots = new LinkedList<N>();
        for (N node : this) {
            if (this.getDepth(node) == 0) {
                roots.add(node);
            }
        }
        return roots;
    }

    /**
	 * {@inheritDoc}
	 * Complexité : O(N).
	 */
    public final int height() {
        int depth = 0;
        for (N node : this) {
            int nodeDepth = this.getNodeInfos(node).depth;
            if (nodeDepth > depth) {
                depth = nodeDepth;
            }
        }
        return depth;
    }

    /**
	 * Indique si le noeud donné est une racine.
	 * @throws UnknownNodeException si le noeud n'est pas dans le graphe.
	 */
    public boolean isRoot(N node) {
        this.assertNode(node);
        TreeNodeInfos rootInfos = this.getNodeInfos(node);
        return rootInfos.depth == 0;
    }

    /**
	 * Indique si le noeud donné est une feuille.
	 * @throws UnknownNodeException si le noeud n'est pas dans le graphe.
	 */
    public boolean isLeaf(N node) {
        this.assertNode(node);
        TreeNodeInfos rootInfos = this.getNodeInfos(node);
        Iterator<E> itEdges = this.edgesIterator(node);
        return this.degree(node) == 1 && this.getOpposite(itEdges.next(), node).equals(rootInfos.root);
    }

    /**
	 * Change la prodondeur d'un noeud ainsi que de tous ses successeurs.
	 */
    private void setDepth(N node, N precNode, int depth) {
        this.getNodeInfos(node).depth = depth;
        Iterator<E> itEdges = this.edgesIterator(node);
        while (itEdges.hasNext()) {
            N childNode = this.getOpposite(itEdges.next(), node);
            if (!childNode.equals(precNode)) {
                this.setDepth(childNode, node, depth + 1);
            }
        }
    }

    /**
	 * Change la racine d'un noeud et de tous ses successeurs.
	 */
    private void setRoot(N node, N precNode, N root) {
        this.getNodeInfos(node).root = root;
        Iterator<E> itEdges = this.edgesIterator(node);
        while (itEdges.hasNext()) {
            N childNode = this.getOpposite(itEdges.next(), node);
            if (!childNode.equals(precNode)) {
                this.setRoot(childNode, node, root);
            }
        }
    }

    /**
	 * Surcharge le factory sur les infos.
	 */
    protected TreeNodeInfos createNodeInfos(N node) {
        return new TreeNodeInfos(node);
    }

    /**
	 * Petite méthode qui évite de passer par un cast.
	 * Utilise la covariance des types de Java 1.5
	 */
    protected TreeNodeInfos getNodeInfos(N node) {
        return (TreeNodeInfos) super.getNodeInfos(node);
    }

    /**
	 * Ajoute des informations sur les noeuds comme son noeud racine
	 * ou sa profondeur au sommet.
	 */
    private class TreeNodeInfos extends NodeInfos {

        public N root;

        public int depth;

        public TreeNodeInfos(N node) {
            super();
            this.root = node;
        }
    }

    /**
	 * Retourne une usine pour créer des arborescences et des forêts.
	 */
    public static <Ns, Es> Factory<TreeGraph<Ns, Es>> factory(boolean directed) {
        return new GraphFactory<Ns, Es>(directed);
    }

    private static class GraphFactory<Ns, Es> implements Factory<TreeGraph<Ns, Es>> {

        private boolean directed;

        public GraphFactory(boolean directed) {
            this.directed = directed;
        }

        public TreeGraph<Ns, Es> create() {
            return new TreeGraph<Ns, Es>(this.directed);
        }
    }
}
