package poo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

/***********************************************************
* @author FBA
* @since 2008/05/1
* This interface defines the requirements for implementing 
* graph models
***********************************************************/
public interface IGraphModel {

    /***********************************************************
	* @return the bottom-right node (the most southeastern one)
	***********************************************************/
    public INode getBottomRight();

    /***********************************************************
	 * @return the top-left node (the most northwestern one)
	 ***********************************************************/
    public INode getTopLeft();

    /***********************************************************
	 * @param bottomRight bottom-right node to set
	 ***********************************************************/
    public void setBottomRight(INode bottomRight);

    /***********************************************************
	 * @param topLeft the top-left node to set
	 ***********************************************************/
    public void setTopLeft(INode topLeft);

    /***********************************************************
	 * @param aNode the node to be added
	 * @return true if the node was added.
	***********************************************************/
    public boolean addNode(INode aNode);

    /***********************************************************
	* @param aConnection the connection to be added
	* @return true if the connection was added.
	***********************************************************/
    public boolean addConnection(IConnection aConnection);

    /***********************************************************
	* @return the iterable collection of all nodes in the model 
	***********************************************************/
    public Iterable<INode> getNodes();

    /***********************************************************
	* @return the iterable collection of all connections in the model 
	***********************************************************/
    public Iterable<IConnection> getConnections();

    /***********************************************************
	 *  This method removes all references to a given node
	 * @param aNode the node to be deleted from the model
	***********************************************************/
    public void removeNode(INode aNode);

    /***********************************************************
	* @param aConnection the unidirectional connection to be removed
	* @return true if the connection was removed from the model
	***********************************************************/
    public boolean removeConnection(IConnection aConnection);

    /***********************************************************
	* Removes all nodes from the model. Notice that this will also
	* remove all conections from the model automatically.
	***********************************************************/
    public void removeAllNodes();

    /***********************************************************
	* Removes all connections from the model. Notice that removing
	* connections does not cause the removal of nodes.
	***********************************************************/
    public void removeAllConnections();

    /***********************************************************
	* @param source the node from which we want to get the shortest path
	* @param target the node to which we want to get the shortest path 
	* @return an ordered collection of nodes corresponding to a minimum 
	* sized path (in terms of number of hops) between the source node
	* and the target node. This method uses Breadth First search.
	***********************************************************/
    public Iterable<INode> shortestPath(INode source, INode target);

    /***********************************************************
	* @param source the node from which we want to get the longest path
	* @param target the node to which we want to get the longest path 
	* @return an ordered collection of nodes corresponding to a maximum 
	* sized path (in terms of number of hops) between the source node
	* and the target node. This method uses Depth First search.
	***********************************************************/
    public Iterable<INode> longestPath(INode source, INode target);

    /***********************************************************
	* @param source the node from which we want to get the shortest path
	* @param target the node to which we want to get the shortest path 
	* @return an ordered collection of nodes corresponding to a minimum 
	* sized path (in terms of number of meters) between the source node
	* and the target node. This method uses Depth First search.
	***********************************************************/
    public Iterable<INode> shortestPathMeters(INode source, INode target);

    /***********************************************************
	* @param source the node from which we want to get the longest path
	* @param target the node to which we want to get the longest path 
	* @return an ordered collection of nodes corresponding to a maximum 
	* sized path (in terms of number of meters) between the source node
	* and the target node. This method uses Depth First search.
	***********************************************************/
    public Iterable<INode> longestPathMeters(INode source, INode target);

    /***********************************************************
	 * @param source the starting node
	 * @param endNode the ending node
	 * @return an iterable collection of iterators; each iterator allows 
	 * transversing a possible path between the source and target nodes; 
	 * this method uses Depth First Search
	***********************************************************/
    public Iterable<Iterator<INode>> allpaths(INode source, INode target);

    /***********************************************************
	* This method calculates the minimum map that allows the representation
	* of all recorded nodes and sets the top-left and bottom-right variables
	* accordingly.
	***********************************************************/
    public void setLimits();

    /***********************************************************
	* This method allows reading a model file with a format described
	* elsewhere (see the Magellan problem description). Notice that the
	* header line can have two different formats.
	* @param in the input scanner
	* @return true if the input data was fully read.
	***********************************************************/
    public boolean loadMapInfo(Scanner in);

    /***********************************************************
	* This method allows writing a model file with a format described
	* elsewhere (see the Magellan problem description).
	* @param out the print writer to write the output to
	* @return true if the output data was fully written.
	***********************************************************/
    public boolean saveMapInfo(PrintWriter out);

    public boolean readModel(String filename);

    public boolean writeModel(String filename);
}
