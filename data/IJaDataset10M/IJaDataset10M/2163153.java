package de.sonivis.tool.core.datamodel.dao;

import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.datamodel.Edge;
import de.sonivis.tool.core.datamodel.Graph;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;

/**
 * Requirements for DAO implementations for an {@link Edge}.
 * <p>
 * TODO: Methods {@link #findAllDirected(Graph)} and {@link #findAllUndirected(Graph)} should have
 * the Integer flag. Create the appropriate methods and deprecate the current ones.
 * </p>
 * 
 * @author Andreas Erber
 * @version $Revision: 1416 $, $Date: 2010-01-28 06:46:22 -0500 (Thu, 28 Jan 2010) $
 */
public interface IEdgeDAO extends IGenericDAO<Edge>, IGraphItemDAO {

    /**
	 * Name of the source field of an {@link Edge}.
	 */
    String SOURCE_FIELD = "source";

    /**
	 * Name of the target field of an {@link Edge}.
	 */
    String TARGET_FIELD = "target";

    /**
	 * Name of the directed field of an {@link Edge}.
	 */
    String DIRECTED_FIELD = "directed";

    /**
	 * Flag to eagerly load the source {@link Node} of the {@link Edge}.
	 */
    Integer INIT_SOURCE = 1;

    /**
	 * Flag to eagerly load the target {@link Node} of the {@link Edge}.
	 */
    Integer INIT_TARGET = 2;

    /**
	 * Flag to eagerly load source and target {@link Node}s of the {@link Edge}.
	 */
    Integer INIT_ALL = 3;

    /**
	 * Retrieves all directed {@link Edge}s in the specified {@link InfoSpace}.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @return An {@link EventList} of directed {@link Edge}s.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 * @see #findAllUndirected()
	 */
    EventList<? extends Edge> findAllDirected(Graph g) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findAllDirected(Long)}
	 * 
	 * @return contained data type
	 */
    Class<? extends Edge> findAllDirectedType();

    /**
	 * Retrieves all undirected {@link Edge}s in the specified {@link InfoSpace}.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @return An {@link EventList} of undirected {@link Edge}s.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 * @see #findAllDirected()
	 */
    EventList<? extends Edge> findAllUndirected(Graph g) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findAllUndirected(Long)}
	 * 
	 * @return contained data type
	 */
    Class<? extends Edge> findAllUndirectedType();

    /**
	 * Retrieves all {@link Edge}s that contain the given {@link Node node}.
	 * 
	 * @param node
	 *            The {@link Node} whose {@link Edge}s are to be found.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of {@link Edge}s that contain the specified {@link Node}.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<? extends Edge> findNeighbors(Node node, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findNeighbors(Long)}
	 * 
	 * @return contained data type
	 */
    Class<? extends Edge> findNeighborsType();

    /**
	 * Retrieves all directed {@link Edge}s that share the target {@link Node}. In other words, gets
	 * a collection of {@link Edge}s that link to the specified {@link Node} in one step.
	 * 
	 * @param target
	 *            Target {@link Node}.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of {@link Edge}s that share the same target {@link Node}.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 * @see #findSuccessors(Node,Integer)
	 */
    EventList<? extends Edge> findPredecessors(Node target, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findPredecessors(Long)}
	 * 
	 * @return contained data type
	 */
    Class<? extends Edge> findPredecessorsType();

    /**
	 * Retrieves all directed {@link Edge}s that share the specified source {@link Node}. In other
	 * words, gets a collection of {@link Edge}s that link to the specified {@link Node} in one
	 * step.
	 * 
	 * @param source
	 *            Source {@link Node}.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of {@link Edge}s that share the same source {@link Node}.
	 * @see #findPredecessors(Node,Integer)
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<? extends Edge> findSuccessors(Node source, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findSuccessors(Long)}
	 * 
	 * @return contained data type
	 */
    Class<? extends Edge> findSuccessorsType();
}
