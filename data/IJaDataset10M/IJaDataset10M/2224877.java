package unbbayes.prs;

import java.util.List;
import unbbayes.util.NodeList;

/** 
 * Interface for a graph building of Node's and Edge's
 */
public interface Graph {

    /**
		 *  Retorna os edgeList do grafo.
		 *
		 *@return    edgeList do grafo.
		 */
    public List<Edge> getEdges();

    /**
		 *  Retorna os n�s do grafo.
		 *
		 *@return    n�s do grafo.
		 * 
		 * @todo Eliminar esse metodo! eh utilizado na classe NetWindow
		 */
    public NodeList getNodes();

    /**
		 *  Returna o n�mero de vari�veis da rede.
		 *
		 *@return    n�mero de vari�veis da rede.
		 */
    public int getNodeCount();

    /**
		 *  Retira do grafo o arco especificado.
		 *
		 *@param  arco  arco a ser retirado.
		 */
    public void removeEdge(Edge arco);

    /**
		 *  Adiciona novo n� ao grafo.
		 *
		 *@param  no  n� a ser inserido.
		 */
    public void addNode(Node no);

    /**
		 *  Adiciona o arco � rede.
		 *
		 *@param  arco  arco a ser inserido.
		 */
    public void addEdge(Edge arco) throws Exception;

    /**
		 *  Remove n� do grafo.
		 *
		 *@param  elemento  no a ser removido.
		 */
    public void removeNode(Node elemento);

    /**
		 *  Verifica exist�ncia de determinado arco.
		 *
		 *@param  no1  n� origem.
		 *@param  no2  n� destino.
		 *@return      posi��o do arco no vetor ou -1 caso n�o exista tal arco.
		 */
    public int hasEdge(Node no1, Node no2);
}
