package unbbayes.prs.mebn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import unbbayes.prs.Edge;
import unbbayes.prs.Graph;
import unbbayes.prs.Node;
import unbbayes.prs.mebn.ssbn.OVInstance;
import unbbayes.util.NodeList;

/**
 * This class represents a MEBN Fragment. 
 * MEBN Fragments (MFrags) are the basic structure of any MEBN logic 
 * model. MFrags represent influences among clusters of related RVs 
 * and can portray repeated patters using ordinary variables as 
 * placeholders in to which entity identifiers can be substituted.  
 * 
 * @version 1.0 (26/11/06)
 */
public class MFrag implements Graph {

    private MultiEntityBayesianNetwork multiEntityBayesianNetwork;

    private List<ResidentNode> residentNodeList;

    private List<InputNode> inputNodeList;

    private List<OrdinaryVariable> ordinaryVariableList;

    /**
	 *  List of nodes that this MFrag has.
	 */
    private NodeList nodeList;

    /** 
	 * List of edges that this MFrag has.
	 */
    private ArrayList<Edge> edgeList;

    protected Map<String, Integer> nodeIndexes;

    private String name;

    private int ordinaryVariableNum = 1;

    private boolean isUsingDefaultCPT = false;

    /**
	 * Contructs a new MFrag with empty node's list.
	 * @param name The name of the MFrag.
	 */
    protected MFrag(String name, MultiEntityBayesianNetwork mebn) {
        this.multiEntityBayesianNetwork = mebn;
        residentNodeList = new ArrayList<ResidentNode>();
        inputNodeList = new ArrayList<InputNode>();
        ordinaryVariableList = new ArrayList<OrdinaryVariable>();
        setName(name);
        nodeList = new NodeList();
        edgeList = new ArrayList<Edge>();
        nodeIndexes = new HashMap<String, Integer>();
    }

    /**
	 * Set the MFrag's name.
	 * @argument name The MFrag's name.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get the MFrag's name.
	 * @return The MFrag's name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get the MEBN where this MFrag is inside
	 * @return the MEBN where this MFrag is inside
	 */
    public MultiEntityBayesianNetwork getMultiEntityBayesianNetwork() {
        return multiEntityBayesianNetwork;
    }

    /**
	 * Method responsible for deleting this MFrag but not its nodes and 
	 * variables. Only their relationship.
	 */
    public void delete() {
        nodeList.clear();
        for (MultiEntityNode node : residentNodeList) {
            node.removeFromMFrag();
        }
        residentNodeList = new ArrayList<ResidentNode>();
        for (MultiEntityNode node : inputNodeList) {
            node.removeFromMFrag();
        }
        inputNodeList = new ArrayList<InputNode>();
        for (OrdinaryVariable variable : ordinaryVariableList) {
            variable.removeFromMFrag();
        }
        ordinaryVariableList = new ArrayList<OrdinaryVariable>();
    }

    /**
	 * Method responsible for adding the given node in its node list. This list
	 * contains all resident and input nodes. Besides that, it contains all
	 * nodes that can be added in subclasses of this class. For instance, the
	 * DomainMFrag contains context nodes also.
	 * 
	 * @param node
	 *            The node to be added in the node list.
	 */
    public void addNode(Node node) {
        nodeList.add(node);
        multiEntityBayesianNetwork.addNode(node);
    }

    /**
	 * Method responsible for adding the given node in its resident node list.
	 * It is also responsible to add the same node to the node list.
	 * 
	 * @param residentNode
	 *            The node to be added in the resident node list.
	 */
    protected void addResidentNode(ResidentNode residentNode) {
        residentNodeList.add(residentNode);
        addNode(residentNode);
    }

    /**
	 * Method responsible for adding the given node in its input node list. It
	 * is also responsible to add the same node to the node list.
	 * 
	 * @param inputNode
	 *            The node to be added in the input node list.
	 */
    protected void addInputNode(InputNode inputNode) {
        inputNodeList.add(inputNode);
        addNode(inputNode);
    }

    /**
	 * Method responsible for removing the given node from its node list. This
	 * list contains all resident and input nodes. Besides that, it contains all
	 * nodes that can be added in subclasses of this class. For instance, the
	 * DomainMFrag contains context nodes also.
	 * 
	 * @param node
	 *            The node to be added in the node list.
	 */
    public void removeNode(Node node) {
        nodeList.remove(node);
        multiEntityBayesianNetwork.removeNode(node);
    }

    /**
	 * Method responsible for removing the given node from its resident node
	 * list. It is also responsible to remove the same node from the node list.
	 * 
	 * @param residentNode
	 *            The node to be removed from the resident node list.
	 */
    protected void removeResidentNode(ResidentNode residentNode) {
        residentNodeList.remove(residentNode);
        removeNode(residentNode);
    }

    /**
	 * Method responsible for removing the given node from its input node list.
	 * It is also responsible to remove the same node from the node list.
	 * 
	 * @param inputNode
	 *            The node to be removed from the input node list.
	 */
    protected void removeInputNode(InputNode inputNode) {
        inputNodeList.remove(inputNode);
        removeNode(inputNode);
    }

    /**
	 * Method responsible for adding the given ordinary variable in its ordinary variable list.
	 * 
	 * @param ordinaryVariable
	 *            The ordinary variable to be added in the ordinary variable list.
	 */
    public void addOrdinaryVariable(OrdinaryVariable ordinaryVariable) {
        ordinaryVariableList.add(ordinaryVariable);
        ordinaryVariableNum++;
    }

    public int getOrdinaryVariableNum() {
        return ordinaryVariableNum;
    }

    /**
	 * Method responsible for removing the given ordinary variable from its ordinary variable list.
	 * 
	 * @param ordinary variable
	 *            The ordinary variable to be removed from the ordinary variable list.
	 */
    public void removeOrdinaryVariable(OrdinaryVariable ordinaryVariable) {
        ordinaryVariableList.remove(ordinaryVariable);
    }

    /**
	 * Method responsible to tell if the ordinary variable list contains the given 
	 * ordinary variable.
	 * @param ordinaryVariable The ordinary variable to check.
	 * @return True if the list contais this ordinary variable and false otherwise.
	 */
    public boolean containsOrdinaryVariable(OrdinaryVariable ordinaryVariable) {
        return ordinaryVariableList.contains(ordinaryVariable);
    }

    /**
	 * Return the ordinary variable with the name, or null if don't exists. 
	 */
    public OrdinaryVariable getOrdinaryVariableByName(String name) {
        for (OrdinaryVariable test : ordinaryVariableList) {
            if (test.getName().equals(name)) {
                return test;
            }
        }
        return null;
    }

    /**
	 * Gets the node list. List of all nodes in this MFrag.
	 * 
	 * @return The list of all nodes in this MFrag.
	 */
    public NodeList getNodeList() {
        return nodeList;
    }

    /**
	 * Gets the ordinary variable list.
	 * 
	 * @return The list of all nodes in this MFrag.
	 */
    public List<OrdinaryVariable> getOrdinaryVariableList() {
        return ordinaryVariableList;
    }

    /**
	 * Method responsible to tell if the node list contains the given node.
	 * Note that this method depends on NodeList.contains(node) method and
	 * for now (September 10, 2007) it makes name comparision.
	 * @param node The node to check.
	 * @return True if the list contais this node and false otherwise.
	 */
    public boolean containsNode(Node node) {
        return nodeList.contains(node);
    }

    /**
	 *  Retorna os edgeList do grafo.
	 *
	 *@return    edgeList do grafo.
	 */
    public List<Edge> getEdges() {
        return edgeList;
    }

    /**
	 *  Retorna os n�s do grafo.
	 *
	 *@return    n�s do grafo.
	 * 
	 * @todo Eliminar esse metodo! eh utilizado na classe NetWindow
	 */
    public NodeList getNodes() {
        return this.getNodeList();
    }

    /**
	 *@return number of nodes of this MFrag (node of all types). 
	 */
    public int getNodeCount() {
        return nodeList.size();
    }

    /**
	 *  Remove the edge between two nodes and remove the relations
	 *  between the nodes.  
	 *
	 *  @param  arco
	 */
    public void removeEdge(Edge arco) {
        Node origin = arco.getOriginNode();
        Node destination = arco.getDestinationNode();
        origin.getChildren().remove(arco.getDestinationNode());
        destination.getParents().remove(arco.getOriginNode());
        edgeList.remove(arco);
        if (origin instanceof DomainResidentNode) {
            ((DomainResidentNode) origin).removeResidentNodeChildList((DomainResidentNode) destination);
        } else {
            ((GenerativeInputNode) origin).removeResidentNodeChild((DomainResidentNode) destination);
        }
    }

    /**
	 *  Retira do grafo o arco especificado.
	 *
	 *@param  arco  arco a ser retirado.
	 */
    public void removeEdgeByNodes(Node origin, Node destination) {
        for (Edge edge : edgeList) {
            if (edge.getOriginNode() == origin) {
                if (edge.getDestinationNode() == destination) {
                    removeEdge(edge);
                    return;
                }
            }
        }
    }

    /**
	 *  Add a edge in the graph. 
     *  @param edge
	*/
    public void addEdge(Edge edge) throws Exception {
        Node origin = edge.getOriginNode();
        Node destination = edge.getDestinationNode();
        origin.getChildren().add(edge.getDestinationNode());
        destination.getParents().add(edge.getOriginNode());
        edgeList.add(edge);
    }

    /**
	 *  Verifica exist�ncia de determinado arco.
	 *
	 *@param  no1  n� origem.
	 *@param  no2  n� destino.
	 *@return      posi��o do arco no vetor ou -1 caso n�o exista tal arco.
	 */
    public int hasEdge(Node no1, Node no2) {
        if (no1 == no2) {
            return 1;
        }
        int sizeArcos = edgeList.size();
        Edge auxA;
        for (int i = 0; i < sizeArcos; i++) {
            auxA = (Edge) edgeList.get(i);
            if ((auxA.getOriginNode() == no1) && (auxA.getDestinationNode() == no2) || (auxA.getOriginNode() == no2) && (auxA.getDestinationNode() == no1)) {
                return i;
            }
        }
        return -1;
    }

    public List<InputNode> getInputNodeList() {
        return inputNodeList;
    }

    public void setInputNodeList(List<InputNode> inputNodeList) {
        this.inputNodeList = inputNodeList;
    }

    public List<ResidentNode> getResidentNodeList() {
        return residentNodeList;
    }

    public void setResidentNodeList(List<ResidentNode> residentNodeList) {
        this.residentNodeList = residentNodeList;
    }

    public void setOrdinaryVariableNum(int ordinaryVariableNum) {
        this.ordinaryVariableNum = ordinaryVariableNum;
    }

    /**
	 * When creating SSBN and making a query, this method tells us whether someone has reported this MFrag as having
	 * a failed context node (thus we should use default CPT for every). This value should be cleared every time we
	 * start a new SSBN creation.
	 * @return true if every Random Variable inside this MFrag should use default CPT (there were failing context node).
	 * Returns false elsewise.
	 * @see MFrag.setAsUsingDefaultCPT()
	 * TODO refactor. Move this method to DomainMFrag, since an input mfrag would never be concious
	 * about default CPTs
	 */
    public boolean isUsingDefaultCPT() {
        return isUsingDefaultCPT;
    }

    /**
	 * By setting this to true, lets a "note" informing other classes accessing this class that there were some
	 * context node failing and every Resident Node inside this MFrag must use default distribution By setting this to false,
	 * clears that information. We should set this to false everytime we start a new SSBN query.
	 * @param isUsingDefaultCPT: value to set
	 * TODO refactor. Move this method to DomainMFrag, since an input mfrag would never be concious
	 * about default CPTs
	 */
    public void setAsUsingDefaultCPT(boolean isUsingDefaultCPT) {
        this.isUsingDefaultCPT = isUsingDefaultCPT;
    }

    /**
	 * checks if there are any node with a particular name inside this mfrag.
	 * @param name: the name of a node
	 * @return The first Node found. Null elsewise.
	 */
    public Node containsNode(String name) {
        NodeList list = this.getNodeList();
        Node node = null;
        for (int i = 0; i < list.size(); i++) {
            node = list.get(i);
            if (node.getName().compareToIgnoreCase(name) == 0) {
                return node;
            }
        }
        return null;
    }
}
