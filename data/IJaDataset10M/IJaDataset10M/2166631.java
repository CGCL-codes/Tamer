package Generic.Graph;

import java.util.List;
import java.util.Vector;

/**
 * Classe d'un chemin dans un graphe allant d'un noeud de d�part � un noeud
 * d'arriv� compos� d'arcs � suivre.
 * 
 * @param <Node>
 *            type des noeuds du graphe.
 * @param <Link>
 *            type des arcs du graphe.
 */
public class Path<Node extends INode<Node, Link>, Link extends ILink<Link, Node>> {

    /**
	 * Liste des arcs � suivre dans l'ordre
	 */
    private final List<Link> linkList = new Vector<Link>();

    /**
	 * Co�t total du chemin
	 */
    private int iCost = 0;

    /**
	 * @return premier noeud du chemin, null si le chemin est vide
	 */
    private Node getStartNode() {
        if (this.linkList.size() <= 0) {
            return null;
        }
        return this.linkList.get(0).getStartNode();
    }

    /**
	 * @return dernier noeud du chemin, null si le chemin est vide
	 */
    private Node getEndNode() {
        if (this.linkList.size() <= 0) {
            return null;
        }
        return this.linkList.get(this.linkList.size() - 1).getEndNode();
    }

    /**
	 * @return co�t total du chemin
	 */
    public int getCost() {
        return this.iCost;
    }

    /**
	 * @return arcs composant le chemin
	 */
    public List<Link> getLinks() {
        return this.linkList;
    }

    /**
	 * Supprime tous les arcs composant le chemin
	 */
    public void clearLinkList() {
        this.linkList.clear();
    }

    /**
	 * Concat�nation d'un chemin, on ajoute les arcs le composant ainsi que son
	 * co�t de parcours. Le premier noeud du chemin � concat�ner doit �tre le
	 * m�me que le dernier de ce chemin pour assurer la continuit�. Le chemin
	 * n'est pas concat�n� s'il est vide ou si son co�t est infini
	 * 
	 * @param path
	 *            chemin que l'on va concat�ner
	 */
    public void add(Path<Node, Link> path) {
        assert (path != null);
        assert (path != this);
        if (path.getLinks().size() <= 0) {
            return;
        }
        if (path.getCost() == Integer.MAX_VALUE) {
            return;
        }
        assert (!path.getStartNode().equals(this.getEndNode()));
        this.linkList.addAll(path.getLinks());
        this.iCost += path.getCost();
    }

    /**
	 * Cr�ation du chemin � partir d'un noeud de d�part et allant jusqu'a un
	 * noeud d'arriv�. Le chemin pr�c�dent est effac�. La construction du chemin
	 * est assur�e en utilisant l'arc parent de chaque noeud, il s'agit de l'arc
	 * utilis� lors de la recherche du chemin pour atteindre ce noeud. une fois
	 * la construction achev�e, si le noeud de d�part ne correspond pas � celui
	 * pass� en param�tre le chemin est effac� car non valide.
	 * 
	 * @param startNode
	 *            noeud de d�part du chemin
	 * @param endNode
	 *            noeud d'arriv� du chemin
	 */
    public void createPath(Node startNode, Node endNode) {
        this.clearLinkList();
        if (endNode == null) {
            return;
        }
        Link link = endNode.getParent();
        while (link != null) {
            assert (!this.linkList.contains(link));
            this.linkList.add(0, link);
            if (link.getStartNode().equals(startNode)) {
                break;
            }
            link = link.getStartNode().getParent();
        }
        if (this.linkList.size() > 0 && this.linkList.get(0).getStartNode() != startNode) {
            this.clearLinkList();
        }
        if (this.linkList.size() <= 0) {
            if (startNode == endNode) {
                this.iCost = 0;
            } else {
                this.iCost = Integer.MAX_VALUE;
            }
        } else {
            this.iCost = endNode.getCost();
        }
    }

    /**
	 * Indique si le chemin est vide.
	 * 
	 * @return true s'il est vide, false sinon
	 */
    public boolean isEmpty() {
        return this.linkList.isEmpty();
    }

    @Override
    public String toString() {
        return this.getStartNode() + " -> " + this.getEndNode() + " : " + this.getCost();
    }
}
