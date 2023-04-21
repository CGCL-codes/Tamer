package de.sonivis.tool.core.datamodel.networkloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.datamodel.Actor;
import de.sonivis.tool.core.datamodel.ContextRelation;
import de.sonivis.tool.core.datamodel.Edge;
import de.sonivis.tool.core.datamodel.Graph;
import de.sonivis.tool.core.datamodel.GraphItemProperty;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InteractionRelation;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.core.datamodel.dao.IInfoSpaceItemDAO;
import de.sonivis.tool.core.datamodel.dao.IInteractionRelationDAO;
import de.sonivis.tool.core.datamodel.dao.hibernate.InteractionRelationDAO;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;
import de.sonivis.tool.core.datamodel.proxy.IActor;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;
import de.sonivis.tool.core.datamodel.proxy.IFundamentalRelation;
import de.sonivis.tool.core.datamodel.proxy.IInfoSpaceItem;
import de.sonivis.tool.core.datamodel.proxy.IInteractionRelation;
import de.sonivis.tool.core.exception.NetworkLoaderException;

/**
 * Network loader for interaction relationships.
 * 
 * @author Janette Lehmann
 * @version $Revision: 1561 $, $Date: 2010-03-09 12:30:32 -0500 (Tue, 09 Mar 2010) $
 */
public class InteractionLoader {

    /**
	 * Logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionLoader.class);

    /**
	 * The node types as specified by the {@link NetworkType}.
	 */
    private final Set<Class<? extends IActor>> nodeTypes = new HashSet<Class<? extends IActor>>();

    /**
	 * The node grouping types as specified by the {@link NetworkType}.
	 */
    @SuppressWarnings("unchecked")
    private final Set<Class<? extends IInteractionRelation>> nodeGroupTypes = new HashSet<Class<? extends IInteractionRelation>>();

    /**
	 * The edge types as specified by the {@link NetworkType}.
	 */
    @SuppressWarnings("unchecked")
    private final Set<Class<? extends IInteractionRelation>> edgeTypes = new HashSet<Class<? extends IInteractionRelation>>();

    /**
	 * Collection that maps an {@link Actor} entity's serial identifier to the representing
	 * {@link Node}.
	 */
    private final Map<Long, Node> targetIndexedNodes = new HashMap<Long, Node>();

    /**
	 * Collection mapping a target's serial identifier to the {@link Edge} entities it is part of.
	 */
    private final Map<Long, Set<Edge>> targetIndexedEdges = new HashMap<Long, Set<Edge>>();

    /**
	 * Default constructor.
	 * 
	 * @throws NetworkLoaderException
	 *             if {@link #initialize()} throws {@link CannotConnectToDatabaseException}
	 */
    public InteractionLoader() {
    }

    public static final String INTERACTION_COUNT = "interactionCount";

    /**
	 * Loads the {@link Graph} that represents the network defined by {@link ENetworkType
	 * networkType} from specified {@link InfoSpace infoSpace}.
	 * <p>
	 * The returned {@link Graph} is based on the {@link InteractionRelation} entities persisted for
	 * the specified {@link InfoSpace infoSpace}. The sub-set of {@link InteractionRelation}
	 * entities to be taken into account is defined by {@link ENetworkType#getTypes()}. In general,
	 * any {@link IActor} entity that is either the source or the target of at least one relevant
	 * {@link InteractionRelation} entity is represented by a {@link Node} entity. If available each
	 * {@link Node} entity has the registration date of the represented entity attached as a
	 * {@link GraphItemProperty} named {@value #REGISTERED}.
	 * </p>
	 * <p>
	 * An {@link Edge} entity between two {@link Node} entities within the network will be
	 * established iff there exists at least one relevant {@link InteractionRelation} entity
	 * involving the {@link IActor} entities represented by the corresponding {@link Node} entities.
	 * Multiple occurrences of of {@link InteractionRelation} entities between the same {@link Node}
	 * entities will be represented as one {@link Edge} entity. A {@link List} of
	 * {@link InteractionRelation} entities represented by an {@link Edge} entity is attached as a
	 * {@link GraphItemProperty} named &quot;{@value #REPRESENTS}&quot;. The creation date of the
	 * oldest {@link IContentElement} related to the {@link InteractionRelation} entity's target
	 * {@link IActor} entity (if available) will be attached as a {@link GraphItemProperty} named
	 * {@value #ESTABLISHED}.
	 * </p>
	 * 
	 * @param networkType
	 *            The {@link NetworkType} of the {@link InteractionRelation} network to be loaded.
	 * @param infoSpace
	 *            The {@link InfoSpace} to load from.
	 * @throws NetworkLoaderException
	 *             if one of the arguments is <code>null</code>.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    protected final Graph loadNetwork(final NetworkType networkType, final InfoSpace infoSpace) throws CannotConnectToDatabaseException {
        if (networkType == null) {
            throw new NetworkLoaderException("No NetworkType given.");
        } else if (infoSpace == null) {
            throw new NetworkLoaderException("No InfoSpace selected!");
        }
        final Graph interactionGraph = new Graph(infoSpace, networkType.getName(), networkType.isDirected());
        this.initTypeSets(networkType);
        if (this.nodeGroupTypes == null || this.nodeGroupTypes.isEmpty()) {
            this.directWorkflow(networkType, interactionGraph);
        } else {
            final Map<IActor, List<IActor>> targetIndexedNodeGroups = this.queryForNodeGroups(infoSpace, networkType);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Number of target elements: " + targetIndexedNodeGroups.size());
            }
            int targetCount = 0;
            int targetWithoutRelationsCount = 0;
            final Iterator<IActor> targetIterator = targetIndexedNodeGroups.keySet().iterator();
            while (targetIterator.hasNext()) {
                final IActor target = targetIterator.next();
                ++targetCount;
                final List<IActor> currentSources = targetIndexedNodeGroups.get(target);
                if (currentSources == null || currentSources.isEmpty()) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Target [" + target.getSerialId() + "] \"" + target.getName() + "\" has no sources! This should not happen here, either the data source is erroneous or the query. Continuing with next...");
                    }
                    continue;
                } else {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Target #" + targetCount + ": \"" + target.getName() + "\" (" + currentSources.size() + " sources)");
                    }
                    this.processRelations(target, currentSources, interactionGraph, networkType);
                }
                int size = 0;
                if (this.targetIndexedEdges.containsKey(target.getSerialId())) {
                    size = this.targetIndexedEdges.get(target.getSerialId()).size();
                } else {
                    ++targetWithoutRelationsCount;
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("\tFound " + size + " distinct relations for this target.");
                }
                targetIterator.remove();
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Targets/nodes without any relations/edges: " + targetWithoutRelationsCount);
            }
        }
        long startEnhancing = 0;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Enhancing edges (with earliest creation date of the target's revision) ...");
            startEnhancing = System.currentTimeMillis();
        }
        this.enhanceEdges(infoSpace);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Edge enhancement done (in " + ((System.currentTimeMillis() - startEnhancing) / 1000) + "s).");
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Creating the interaction graph...");
        }
        interactionGraph.addGraphComponents(this.targetIndexedNodes.values());
        final Iterator<Set<Edge>> it = this.targetIndexedEdges.values().iterator();
        while (it.hasNext()) {
            interactionGraph.addGraphComponents(it.next());
            it.remove();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("InteractionLoader successfully loaded a graph named \"" + interactionGraph.getName() + "\" from InfoSpace \"" + infoSpace.getName() + "\"");
            LOGGER.info("The graph has " + interactionGraph.getNodes().size() + " nodes interconnected by " + interactionGraph.getEdges().size() + " edges.");
        }
        return interactionGraph;
    }

    /**
	 * Query for a subset of all node grouping relations.
	 * <p>
	 * All node grouping types specified in the {@link NetworkType} passed to this loader will be
	 * queried for in the selected {@link InfoSpace}. The returned result then maps the actual
	 * target entity of the relation to a list of source that share the same target.
	 * </p>
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} entity to operate on. May not be <code>null</code>.
	 * @param networkType
	 *            The {@link NetworkType} of the {@link Graph} entity to be created.
	 * @return A {@link Collection} that maps {@link IActor} entities representing the targets of
	 *         the retrieved relations to the {@link Collection} of {@link IActor} entities they are
	 *         the target of.
	 * @throws NetworkLoaderException
	 *             If the specified {@link InfoSpace} is <code>null</code>.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    private Map<IActor, List<IActor>> queryForNodeGroups(final InfoSpace infoSpace, final NetworkType networkType) throws CannotConnectToDatabaseException {
        if (infoSpace == null) {
            throw new NetworkLoaderException("No InfoSpace selected!");
        } else if (networkType == null) {
            throw new NetworkLoaderException("No NetworkType selected!");
        }
        final Map<IActor, List<IActor>> resultConverted = new Hashtable<IActor, List<IActor>>();
        if (this.nodeGroupTypes == null || this.nodeGroupTypes.isEmpty()) {
            return resultConverted;
        }
        List<IInteractionRelation<IActor, IActor>> resultDAO = null;
        resultDAO = new InteractionRelationDAO().findByType(infoSpace, this.nodeGroupTypes, IInteractionRelationDAO.INIT_TARGET);
        if (resultDAO != null && !resultDAO.isEmpty()) {
            final ListIterator<IInteractionRelation<IActor, IActor>> it = resultDAO.listIterator();
            while (it.hasNext()) {
                final IInteractionRelation<IActor, IActor> ia = it.next();
                if (!networkType.isPolymorphismImplicitForNodeGrouping()) {
                    if (!this.nodeGroupTypes.contains(ia.getType())) {
                        continue;
                    }
                }
                final IActor target = ia.getTarget();
                if (!this.isApplicableNodeType(networkType, target)) {
                    continue;
                }
                if (!resultConverted.containsKey(target)) {
                    final List<IActor> revList = new ArrayList<IActor>();
                    revList.add(ia.getSource());
                    resultConverted.put(target, revList);
                } else {
                    resultConverted.get(target).add(ia.getSource());
                }
            }
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Result of query for target sources is null.");
            }
        }
        return resultConverted;
    }

    /**
	 * Sets up local collections.
	 * <p>
	 * Sets up the the three sets of types {@link #nodeTypes}, {@link #nodeGroupTypes}, and
	 * {@link #edgeTypes} from the values provided by the {@link NetworkType}.
	 * </p>
	 * 
	 * @param networkType
	 *            The {@link NetworkType} of the {@link Graph} entity to be created.
	 * @throws NetworkLoaderException
	 *             If the specified {@link NetworkType} is null.
	 */
    @SuppressWarnings("unchecked")
    private void initTypeSets(final NetworkType networkType) {
        if (networkType == null) {
            throw new NetworkLoaderException("No NetworkType selected.");
        }
        for (final Class<? extends IInfoSpaceItem> clazz : networkType.getNodeTypes()) {
            if (IActor.class.isAssignableFrom(clazz)) {
                this.nodeTypes.add((Class<? extends IActor>) clazz);
            }
        }
        for (final Class<? extends IFundamentalRelation> clazz : networkType.getNodeGroupingRelation()) {
            if (IInteractionRelation.class.isAssignableFrom(clazz)) {
                this.nodeGroupTypes.add((Class<? extends IInteractionRelation>) clazz);
            }
        }
        for (final Class<? extends IInfoSpaceItem> clazz : networkType.getEdgeTypes()) {
            if (IInteractionRelation.class.isAssignableFrom(clazz)) {
                this.edgeTypes.add((Class<? extends IInteractionRelation>) clazz);
            }
        }
    }

    /**
	 * Create a {@link Node} entity from an {@link IActor} entity.
	 * <p>
	 * The method queries {@link #targetIndexedNodes} if an entry for this {@link IActor} entity
	 * already exists and will return it on success. Otherwise a new {@link Node} entity will be
	 * created. For this case, the specified {@link IActor} entity is supposed to be initialized.
	 * Otherwise a {@link LazyInitializationException} will be thrown.
	 * </p>
	 * <p>
	 * A newly created {@link Node} entity that is created and returned has the {@link IActor}
	 * entity's name as a label and references the entity in its {@link Node#getRepresents()
	 * represents field}. If the {@link IActor} entity's registration date is available (i.e., not
	 * <code>null</code>) the {@link Node} entity is attached a property named &quot;
	 * {@value #REGISTERED}&quot; containing that information.
	 * </p>
	 * <p>
	 * A newly created {@link Node} entity will be part of {@link #interactionGraph} and will be
	 * added to {@link #targetIndexedNodes} .
	 * </p>
	 * 
	 * @param actor
	 *            An initialized {@link IActor} entity.
	 * @param graph
	 *            The {@link Graph} entity the {@link Node} instance will belong to.
	 * @return A {@link Node} representing the {@link IActor} entity in the
	 *         {@link #interactionGraph}.
	 */
    private Node createNode(final IActor actor, final Graph graph) {
        if (actor == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The specified IActor instance is null. Cannot create Node instance.");
            }
            return null;
        } else if (graph == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The specified Graph instance is null. Cannot create Node instance.");
            }
            return null;
        }
        if (this.targetIndexedNodes.containsKey(actor.getSerialId())) {
            return this.targetIndexedNodes.get(actor.getSerialId());
        } else {
            Node node = null;
            node = new Node(graph, actor, actor.getName());
            if (actor.getRegistered() != null) {
                node.addProperty(new GraphItemProperty<Date>(node, NetworkLoaderConstants.REGISTERED, actor.getRegistered()));
            }
            this.targetIndexedNodes.put(actor.getSerialId(), node);
            return node;
        }
    }

    /**
	 * Calculates the {@link Date} when each {@link Edge} entity was established.
	 * <p>
	 * Each {@link Edge} created by {@link #loadNetwork(NetworkType, InfoSpace)} is expected to have
	 * a {@link GraphItemProperty} named {@link #REPRESENTS} attached. From this {@link List} of
	 * {@link InteractionRelation} entities this method finds the establishment date of the
	 * represented relation. This date will be attached as a special {@link GraphItemProperty} named
	 * &quot; {@value #ESTABLISHED}&quot; to each {@link Edge} entity.
	 * </p>
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} to operate upon.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    @SuppressWarnings("unchecked")
    private void enhanceEdges(final InfoSpace infoSpace) throws CannotConnectToDatabaseException {
        if (infoSpace == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified InfoSpace entity is null.");
            }
            return;
        }
        if (this.targetIndexedEdges == null || this.targetIndexedEdges.isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to enhance Edge entities - Graph instance has no Edges.");
            }
            return;
        }
        Session s = null;
        Transaction tx = null;
        try {
            s = ModelManager.getInstance().getCurrentSession();
            Object result = null;
            for (final Long id : this.targetIndexedEdges.keySet()) {
                for (final Edge edge : this.targetIndexedEdges.get(id)) {
                    final List<Serializable> represents = (ArrayList<Serializable>) edge.getProperty(NetworkLoaderConstants.REPRESENTS).getValue();
                    if (represents == null || represents.isEmpty()) {
                        continue;
                    }
                    tx = s.beginTransaction();
                    final Criteria crit = s.createCriteria(InteractionRelation.class);
                    crit.add(Restrictions.eq(IInfoSpaceItemDAO.INFOSPACE_FIELD, infoSpace));
                    crit.add(Restrictions.in("serialId", represents));
                    result = crit.setProjection(Projections.min(IInteractionRelationDAO.ESTABLISHED_FIELD)).uniqueResult();
                    s.flush();
                    tx.commit();
                    if (result != null) {
                        edge.addProperty(new GraphItemProperty<Date>(edge, NetworkLoaderConstants.ESTABLISHED, (Date) result));
                    }
                }
            }
        } catch (final HibernateException he) {
            if (tx != null) {
                tx.rollback();
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Failed to enhance edges - query failed, transaction was rolled back.");
                }
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Failed to enhance edges - could not establish transaction.");
                }
            }
            throw he;
        } finally {
            s.close();
        }
    }

    /**
	 * Direct work-flow
	 * <p>
	 * The, s.c., direct workflow is executed when there is no node grouping type provided by the
	 * {@link NetworkType}.
	 * </p>
	 * 
	 * @param networkType
	 *            The {@link INetworkType} of the {@link Graph} entity to be created.
	 * @param graph
	 *            The {@link Graph} to operate upon.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available
	 */
    private void directWorkflow(final NetworkType networkType, final Graph graph) throws CannotConnectToDatabaseException {
        if (graph == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Non-null Graph instance required for direct work-flow.");
            }
            return;
        }
        long startIALoading = 0;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Loading InteractionRelation entities...");
            startIALoading = System.currentTimeMillis();
        }
        final List<IInteractionRelation<IActor, IActor>> iaList = new InteractionRelationDAO().findByType(graph.getInfoSpace(), this.edgeTypes, IInteractionRelationDAO.INIT_ALL);
        if (iaList == null || iaList.isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Could not create network - no suitable items for representation found in persistence store.");
            }
            return;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Loaded " + iaList.size() + " InteractionRelation entities (in " + ((System.currentTimeMillis() - startIALoading) / 1000) + "s).");
        }
        final Iterator<IInteractionRelation<IActor, IActor>> iaIt = iaList.iterator();
        while (iaIt.hasNext()) {
            final IInteractionRelation<IActor, IActor> interaction = iaIt.next();
            if (!this.isApplicableNodeType(networkType, interaction.getSource()) || !this.isApplicableNodeType(networkType, interaction.getTarget())) {
                continue;
            }
            final Node sourceNode = createNode(interaction.getSource(), graph);
            final Node targetNode = createNode(interaction.getTarget(), graph);
            this.handleEdge(graph, sourceNode, targetNode, interaction);
        }
    }

    /**
	 * Process the relations for a {@link IActor target}.
	 * <p>
	 * All {@link Node} entities produced by this method will be stored to
	 * {@link #targetIndexedNodes}. All {@link Edge} entities are passed to
	 * {@link #targetIndexedEdges}.
	 * </p>
	 * 
	 * @param target
	 *            The initialized {@link IActor} entity to do the processing for.
	 * @param sources
	 *            A {@link List} of possibly uninitialized {@link IActor} entities representing the
	 *            versions of the specified <em>target</em>.
	 * @param graph
	 *            The {@link Graph} entity to work with.
	 * @param networkType
	 *            The {@link NetworkType} the graph represents.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available
	 */
    private void processRelations(final IActor target, final List<IActor> sources, final Graph graph, final NetworkType networkType) throws CannotConnectToDatabaseException {
        if (target == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified target argument is null - returning");
            }
            return;
        } else if (sources == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified sources argument is null - returning");
            }
            return;
        } else if (sources.isEmpty()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified sources argument is empty - returning");
            }
            return;
        } else if (graph == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified Graph instance is null.");
            }
            return;
        } else if (networkType == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified NetworkType instance is null.");
            }
            return;
        } else if (!this.isApplicableNodeType(networkType, target)) {
            return;
        }
        final List<IInteractionRelation<IActor, IActor>> iaList = new InteractionRelationDAO().findTargets(sources, this.edgeTypes, IInteractionRelationDAO.INIT_ALL);
        sources.clear();
        Node sourceNode = null;
        if (!this.targetIndexedNodes.containsKey(target.getSerialId())) {
            sourceNode = this.createNode(target, graph);
            this.targetIndexedNodes.put(target.getSerialId(), sourceNode);
        } else {
            sourceNode = this.targetIndexedNodes.get(target.getSerialId());
        }
        if (sourceNode == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to create or find source node.");
            }
            return;
        }
        for (final IInteractionRelation<IActor, IActor> relation : iaList) {
            if (!networkType.isPolymorphismImplicitForEdges()) {
                if (!this.edgeTypes.contains(relation.getType())) {
                    continue;
                }
            }
            if (!this.isApplicableNodeType(networkType, relation.getTarget())) {
                continue;
            }
            Node targetNode = null;
            if (!this.targetIndexedNodes.containsKey(relation.getTarget().getSerialId())) {
                targetNode = this.createNode(relation.getTarget(), graph);
                this.targetIndexedNodes.put(relation.getTarget().getSerialId(), targetNode);
            } else {
                targetNode = this.targetIndexedNodes.get(relation.getTarget().getSerialId());
            }
            if (targetNode == null) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Failed to create or find target node.");
                }
                continue;
            }
            this.handleEdge(graph, sourceNode, targetNode, relation);
        }
    }

    /**
	 * Create an new {@link Edge} entity.
	 * <p>
	 * A directed {@link Edge} entity is created that points form the specified <em>source</em>
	 * {@link Node} entity to the specified <em>target</em> {@link Node} entity. If the specified
	 * {@link ContextRelation link} is not null the {@link Edge} entity will be added a {@link Set}
	 * of {@link Date} as a {@link GraphItemProperty} entity named
	 * {@link NetworkLoaderConstants#ACTIVE_DATES} that contains the creation {@link Date} of the
	 * <em>link</em>'s source. Furthermore, the type of the <em>link</em> is added as a
	 * {@link GraphItemProperty} entity named {@link NetworkLoaderConstants#TYPE}.
	 * </p>
	 * 
	 * @param graph
	 *            The {@link Graph} entity the {@link Edge} instance is part of.
	 * @param source
	 *            The source {@link Node} entity of the {@link Edge} instance.
	 * @param target
	 *            The target {@link Node} entity of the {@link Edge} instance.
	 * @param link
	 *            The {@link InteractionRelation} entity this {@link Edge} entity is based upon.
	 * @return A new {@link Edge} entity.
	 */
    @SuppressWarnings("unchecked")
    private Edge createEdge(final Graph graph, final Node source, final Node target, final IInteractionRelation<IActor, IActor> relation) {
        if (graph == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The specified Graph instance is null. Cannot create Edge.");
            }
            return null;
        } else if (source == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The specified source Node instance is null. Cannot create Edge.");
            }
            return null;
        } else if (target == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("The specified target Node instance is null. Cannot create Edge.");
            }
            return null;
        }
        final Edge linkEdge = new Edge(graph, source, target, graph.isDirected());
        linkEdge.addProperty(new GraphItemProperty<Class<?>>(NetworkLoaderConstants.TYPE, relation.getType()));
        final ArrayList<Serializable> represents = new ArrayList<Serializable>();
        represents.add(relation.getSerialId());
        linkEdge.addProperty(new GraphItemProperty<ArrayList>(NetworkLoaderConstants.REPRESENTS, represents));
        return linkEdge;
    }

    /**
	 * Takes care of nearly anything about an {@link Edge} to be established between two
	 * {@link Node} entities.
	 * 
	 * @param graph
	 *            The {@link Graph} to operate on. May not be <code>null</code>.
	 * @param sourceNode
	 *            The {@link Node} entity that is the source of the {@link Edge} instance to be
	 *            created. May not be <code>null</code>.
	 * @param targetNode
	 *            The {@link Node} entity that is the target of the {@link Edge} instance to be
	 *            created. May not be <code>null</code>.
	 * @param relation
	 *            The {@link IInteractionRelation} entity that the {@link Edge} instance that is to
	 *            be created represents. May not be <code>null</code>.
	 */
    @SuppressWarnings("unchecked")
    private void handleEdge(final Graph graph, final Node sourceNode, final Node targetNode, final IInteractionRelation<IActor, IActor> relation) {
        if (graph == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Graph entity for Edge instance to be created is null.");
            }
            return;
        } else if (sourceNode == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Source Node entity for Edge instance to be created is null.");
            }
            return;
        } else if (targetNode == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Target Node entity for Edge instance to be created is null.");
            }
            return;
        } else if (relation == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("IInteractionRelation entity to create Edge instance from is null.");
            }
            return;
        }
        if (!this.targetIndexedEdges.containsKey(relation.getTarget().getSerialId())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\tTarget has no edges yet.");
            }
            final Edge linkEdge = this.createEdge(graph, sourceNode, targetNode, relation);
            final Set<Edge> edges = new HashSet<Edge>();
            edges.add(linkEdge);
            this.targetIndexedEdges.put(relation.getTarget().getSerialId(), edges);
        } else {
            final Set<Edge> estabEdges = this.targetIndexedEdges.get(relation.getTarget().getSerialId());
            boolean edgeExists = false;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\tTarget already has " + estabEdges.size() + " edges.");
            }
            for (final Edge estabEdge : estabEdges) {
                if (estabEdge.getSource().equals(sourceNode) && estabEdge.getTarget().equals(targetNode) && estabEdge.getProperty(NetworkLoaderConstants.TYPE).getValue().equals(relation.getType())) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("\t\tFound matching edge.");
                    }
                    edgeExists = true;
                    if (estabEdge.hasProperty(NetworkLoaderConstants.REPRESENTS)) {
                        ((List<Serializable>) estabEdge.getProperty(NetworkLoaderConstants.REPRESENTS).getValue()).add(relation.getSerialId());
                    }
                }
                break;
            }
            if (!edgeExists) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("\t\tFound NO match - creating new edge.");
                }
                final Edge linkEdge = this.createEdge(graph, sourceNode, targetNode, relation);
                this.targetIndexedEdges.get(relation.getTarget().getSerialId()).add(linkEdge);
            }
        }
    }

    /**
	 * Helper to determine if the specified <em>item</em>'s type is supported by the node type
	 * provided by the specified {@link NetworkType}.
	 * 
	 * @param nt
	 *            The {@link NetworkType} to check against.
	 * @param item
	 *            The {@link IActor} entity to check.
	 * @return <code>true</code> if the given {@link NetworkType} supports the specified item's
	 *         type.
	 */
    private boolean isApplicableNodeType(final NetworkType nt, final IActor item) {
        if (nt == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Specified NetworkType instance is null.");
            }
            return false;
        } else if (nt.isPolymorphismImplicitForNodes()) {
            for (final Class<? extends IActor> clazz : this.nodeTypes) {
                if (clazz.isAssignableFrom(item.getClass())) {
                    return true;
                }
            }
        } else {
            if (this.nodeTypes.contains(item.getClass())) {
                return true;
            }
        }
        return false;
    }
}
