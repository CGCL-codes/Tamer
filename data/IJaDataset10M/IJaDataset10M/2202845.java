package edu.columbia.concerns.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.core.runtime.IPath;
import edu.columbia.concerns.ConcernTagger;
import edu.columbia.concerns.model.IConcernListener;
import edu.columbia.concerns.util.ProblemManager;

/**
 * Class that provides connection to the HSQLDB database. All SQL queries are
 * run through this class.
 * 
 * @author vgarg
 * 
 */
public class ConcernRepository implements DBConstants {

    protected String databaseLocation;

    protected Connection con = null;

    private EnumMap<EdgeKind, Map<Integer, Set<Component>>> linkMap;

    private Map<String, Component> handleToComponentCache = new HashMap<String, Component>();

    private Map<Integer, List<Component>> componentToChildrenCache = new HashMap<Integer, List<Component>>();

    private Map<Integer, List<Concern>> concernToChildrenCache = new HashMap<Integer, List<Concern>>();

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Constructor. Checks whether the path is relative. If so, attaches the
	 * user home directory to the path.
	 * 
	 * @param databaseLocation
	 */
    private ConcernRepository(String databaseLocation) {
        this.databaseLocation = databaseLocation;
        this.linkMap = new EnumMap<EdgeKind, Map<Integer, Set<Component>>>(EdgeKind.class);
    }

    public static ConcernRepository openDatabase() {
        IPath workspacePluginDir = ConcernTagger.singleton().getStateLocation();
        return openDatabase(workspacePluginDir.append("db").toOSString(), true);
    }

    public static ConcernRepository openDatabase(String dirToSearch, boolean createIfNeeded) {
        String databaseLocation = resolveDatabaseDirectory(dirToSearch);
        if (databaseLocation != null) {
            ConcernRepository repository = new ConcernRepository(databaseLocation);
            repository.verifyComponentKinds();
            repository.verifyEdgeKinds();
            return repository;
        } else if (!createIfNeeded) {
            return null;
        } else {
            assert !doesDatabaseExist(dirToSearch);
            ConcernRepository repository = new ConcernRepository(dirToSearch);
            DBReset.resetDatabase(repository);
            repository.verifyComponentKinds();
            repository.verifyEdgeKinds();
            return repository;
        }
    }

    public ConcernDomain getConcernDomain(String concernDomainName, IConcernListener changeListener) {
        try {
            ResultSet resultSet = executeQuery(GET_CONCERN_DOMAIN_BY_NAME, concernDomainName);
            if (resultSet.next()) {
                return new ConcernDomain(this, changeListener, resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    public List<ConcernDomain> getConcernDomains(IConcernListener changeListener) {
        List<ConcernDomain> concernDomains = new ArrayList<ConcernDomain>();
        try {
            ResultSet resultSet = executeQuery(GET_CONCERN_DOMAINS);
            while (resultSet.next()) {
                concernDomains.add(new ConcernDomain(this, changeListener, resultSet));
            }
        } catch (SQLException ex) {
            ProblemManager.reportException(ex);
        }
        return concernDomains;
    }

    public ConcernDomain createConcernDomain(String name, String description, String shortName, String kind, IConcernListener changeListener) {
        Concern rootConcern = getOrCreateRootConcern(name, changeListener);
        if (rootConcern == null) return null;
        List<Object> params = new ArrayList<Object>();
        params.add(rootConcern.getId());
        params.add(name);
        params.add(shortName);
        params.add(description);
        params.add(kind);
        try {
            PreparedStatement statement = createPreparedStatement(CONCERN_DOMAIN_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return getConcernDomain(name, changeListener);
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().indexOf("Violation of unique constraint") >= 0) {
                ProblemManager.reportException(e, "Failed to create concern domain. Concern domain '" + name + "' already exists.");
            } else {
                ProblemManager.reportException(e, "Failed to create concern domain '" + name + "'.");
            }
            return null;
        }
    }

    /**
	 * Updates the concern name.
	 * 
	 * @param oldName
	 * @param newName
	 */
    public void renameConcernDomain(String oldName, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_CONCERN_DOMAIN_NAME, newName, oldName);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }

    public Concern getConcern(String domainName, String concernName, IConcernListener changeListener) {
        try {
            ResultSet resultSet = executeQuery(GET_CONCERN_FROM_NAME, concernName);
            if (resultSet.next()) {
                return new Concern(this, changeListener, resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    public Concern getConcern(Integer concernId, IConcernListener changeListener) {
        try {
            ResultSet resultSet = executeQuery(GET_CONCERN_FROM_ID, concernId);
            if (resultSet.next()) {
                return new Concern(this, changeListener, resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    /**
	 * Gets the list of concerns from the concern table
	 */
    public List<Concern> getConcerns() {
        List<Concern> concerns = new ArrayList<Concern>();
        try {
            ResultSet resultSet = executeQuery(GET_CONCERNS);
            while (resultSet.next()) {
                concerns.add(new Concern(this, (IConcernListener) null, resultSet));
            }
        } catch (SQLException ex) {
            ProblemManager.reportException(ex);
        }
        return concerns;
    }

    private Concern getOrCreateRootConcern(String concernDomainName, IConcernListener changeListener) {
        String concernDomainRootConcernName = concernDomainName + "-" + DEFAULT_ROOT_CONCERN_NAME;
        Concern rootConcern = getConcern(concernDomainName, concernDomainRootConcernName, changeListener);
        if (rootConcern != null) return rootConcern;
        List<Concern> children = getConcerns();
        rootConcern = createConcern(changeListener, concernDomainRootConcernName, "", "", "");
        for (Concern childConcern : children) {
            if (childConcern.getParent() == null && !childConcern.isRoot()) {
                rootConcern.addChild(childConcern);
            }
        }
        return rootConcern;
    }

    public Concern getParentConcern(Concern child) {
        try {
            ResultSet resultSet = executeQuery(GET_PARENT_CONCERN, child.getId(), getEdgeKindId(EdgeKind.CONTAINS));
            if (resultSet.next()) {
                return new Concern(this, child.getChangeListener(), resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    public List<Concern> getChildConcerns(Concern concern) {
        List<Concern> children = concernToChildrenCache.get(concern.getId());
        if (children != null) return children;
        children = new ArrayList<Concern>();
        try {
            ResultSet resultSet = executeQuery(GET_CHILD_CONCERNS, concern.getId(), getEdgeKindId(EdgeKind.CONTAINS));
            while (resultSet.next()) {
                children.add(new Concern(this, concern.getChangeListener(), resultSet));
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
        List<Concern> prev = concernToChildrenCache.put(concern.getId(), children);
        assert prev == null;
        return children;
    }

    public void dumpConcernToChildrenCache() {
        for (Map.Entry<Integer, List<Concern>> entry : concernToChildrenCache.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println();
    }

    public boolean hasChildConcerns(Concern concern) {
        List<Concern> children = concernToChildrenCache.get(concern.getId());
        if (children != null) return !children.isEmpty();
        try {
            ResultSet resultSet = executeQuery(GET_CHILD_CONCERNS, concern.getId(), getEdgeKindId(EdgeKind.CONTAINS));
            return resultSet.next();
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return false;
        }
    }

    public boolean addChildConcern(Concern parent, Concern child) {
        concernToChildrenCache.put(parent.getId(), null);
        if (child.getParent() != null) {
            concernToChildrenCache.put(child.getParent().getId(), null);
        }
        assert !parent.equals(child);
        List<Object> params = new ArrayList<Object>();
        try {
            params.add(child.getId());
            params.add(this.getEdgeKindId(EdgeKind.CONTAINS));
            PreparedStatement statement = createPreparedStatement(REMOVE_CONCERN_EDGE_FOR_EDGE_KIND, params);
            statement.executeUpdate();
            statement.close();
            params.clear();
            params.add(parent.getId());
            params.add(child.getId());
            params.add(this.getEdgeKindId(EdgeKind.CONTAINS));
            statement = createPreparedStatement(CONCERN_EDGE_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return true;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }

    /**
	 * Add the concern to the database.
	 */
    public Concern createConcern(IConcernListener changeListener, String name, String shortName, String description, String color) {
        try {
            Integer id = getNextSequenceNumber("concern_id_seq", CONCERN_TABLE);
            List<Object> params = new ArrayList<Object>();
            params.add(id);
            params.add(name);
            params.add(shortName);
            params.add(description);
            params.add(color);
            PreparedStatement statement = createPreparedStatement(CONCERN_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return getConcern(id, changeListener);
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().indexOf("Violation of unique constraint") >= 0) {
                ProblemManager.reportException(e, "Failed to create concern. Concern '" + name + "' already exists.");
            } else {
                ProblemManager.reportException(e, "Failed to create concern '" + name + "'.");
            }
            return null;
        }
    }

    /**
	 * Removes a concern from concern table. If concern had linked components,
	 * those edges are removed. All parent/child relationships with other
	 * concerns are severed. If a concern domain is associated with the concern,
	 * it is removed as well.
	 */
    public int removeConcernAndChildren(Concern concern) {
        int numRemoved = 0;
        for (Concern child : concern.getChildren()) {
            numRemoved += removeConcernAndChildren(child);
        }
        try {
            if (concern.getParent() != null) concernToChildrenCache.remove(concern.getParent().getId());
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_CONCERN, concern.getId());
            statement.executeUpdate();
            statement.close();
            List<Object> params = new ArrayList<Object>();
            params.add(concern.getId());
            params.add(concern.getId());
            statement = createPreparedStatement(REMOVE_CONCERN_EDGE, params);
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_CONCERN, concern.getId());
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_CONCERN_DOMAIN, concern.getId());
            statement.executeUpdate();
            statement.close();
            con.commit();
            flushLinkCache(concern.getId());
            concernToChildrenCache.remove(concern.getId());
            numRemoved += 1;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
        return numRemoved;
    }

    /**
	 * Updates the concern name.
	 */
    public void renameConcern(int concernId, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_CONCERN_NAME, newName, concernId);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Adds a component to the database. If the domain is provided, adds the
	 * domain too.
	 * 
	 * @param name
	 * @param componentKind
	 * @param handle
	 * @param beginLine
	 * @param beginColumn
	 * @param endLine
	 * @param endColumn
	 * @param numLines
	 * @param componentDomain
	 * @return
	 */
    public Component createComponent(String name, ComponentKind componentKind, String handle, Integer beginLine, Integer beginColumn, Integer endLine, Integer endColumn, Integer numLines, ComponentDomain componentDomain) {
        PreparedStatement statement = null;
        Integer compSeqNum = null;
        try {
            compSeqNum = getNextSequenceNumber("COMPONENT_ID_SEQ", COMPONENT_TABLE);
            List<Object> params = new ArrayList<Object>();
            params.add(compSeqNum);
            params.add(name);
            params.add(getComponentKindId(componentKind));
            params.add(handle);
            params.add(beginLine);
            params.add(beginColumn);
            params.add(endLine);
            params.add(endColumn);
            params.add(numLines);
            statement = createPreparedStatement(COMPONENT_INSERT_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().startsWith("Violation of unique constraint")) {
                ProblemManager.reportException(e, "Program element handle '" + handle + "' is already present in database");
            } else {
                ProblemManager.reportException(e);
            }
            return null;
        }
        if (componentDomain == null) return getComponent(compSeqNum);
        assert compSeqNum != null;
        componentDomain.setId(compSeqNum);
        try {
            statement = createPreparedStatement(COMPONENT_DOMAIN_INSERT, componentDomain.getValuesAsList());
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return null;
        }
        return getComponent(compSeqNum);
    }

    public void renameComponent(Integer componentId, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_COMPONENT_NAME, newName, componentId);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }

    public void updateSourceRange(Integer componentId, Integer beginLine, Integer beginColumn, Integer endLine, Integer endColumn, Integer numLines) {
        List<Object> params = new ArrayList<Object>();
        params.add(beginLine);
        params.add(beginColumn);
        params.add(endLine);
        params.add(endColumn);
        params.add(numLines);
        params.add(componentId);
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_COMPONENT_SOURCE_RANGE, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Adds the component edge.
	 * 
	 * @param edge
	 * @throws SQLException
	 */
    public void connectComponents(Component from, Component to, EdgeKind edgeKind) {
        assert (edgeKind != null);
        List<Object> params = new ArrayList<Object>();
        params.add(from.getId());
        params.add(to.getId());
        params.add(getEdgeKindId(edgeKind));
        try {
            PreparedStatement statement = createPreparedStatement(COMPONENT_EDGE_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e, "Failed to create '" + edgeKind.name() + "' component edge " + "from component " + from.getId() + " to " + to.getId() + ".");
        }
    }

    /**
	 * Checks if an edge exists between two components
	 * 
	 * @param compFromId
	 * @param compToId
	 * @return
	 */
    public boolean isConnected(Component from, Component to) {
        try {
            ResultSet resultSet = executeQuery(DBConstants.CHECK_COMPONENT_EDGE_SQL, from.getId(), to.getId());
            boolean found = resultSet.next();
            resultSet.close();
            resultSet.getStatement().close();
            return found;
        } catch (SQLException e) {
            ProblemManager.reportException(e, true);
            return false;
        }
    }

    /**
	 * Gets the component associated with the component id.
	 * 
	 * @param componentId
	 * @return
	 * @throws SQLException
	 */
    public Component getComponent(Integer componentId) {
        try {
            Component component = null;
            ResultSet resultSet = executeQuery(GET_COMPONENT_BY_ID, componentId);
            if (resultSet.next()) {
                component = getOrCreateComponent(resultSet);
            }
            resultSet.close();
            resultSet.getStatement().close();
            return component;
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    private Component getOrCreateComponent(ResultSet resultSet) {
        String handle = Component.getHandleFromResultSet(resultSet);
        Component component = handleToComponentCache.get(handle);
        if (component == null) {
            component = new Component(this, resultSet);
            handleToComponentCache.put(handle, component);
        }
        return component;
    }

    /**
	 * Gets the component associated with the handle.
	 * 
	 * @param handle
	 * @return
	 * @throws SQLException
	 */
    public Component getComponent(String handle) {
        Component cache = handleToComponentCache.get(handle);
        if (cache != null) return cache;
        try {
            Component component = null;
            ResultSet resultSet = executeQuery(GET_COMPONENT_BY_HANDLE, handle);
            if (resultSet.next()) {
                component = getOrCreateComponent(resultSet);
            }
            resultSet.close();
            resultSet.getStatement().close();
            return component;
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    /**
	 * Return total number of components for a particular component kind
	 * 
	 * @param coKind
	 * @return
	 * @throws SQLException
	 */
    public List<Component> getComponents(ComponentKind componentKind) {
        List<Component> components = new ArrayList<Component>();
        try {
            ResultSet resultSet = executeQuery(GET_COMPONENTS_OF_KIND, getComponentKindId(componentKind));
            while (resultSet.next()) {
                Component component = getOrCreateComponent(resultSet);
                components.add(component);
            }
            return components;
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    /**
	 * Return total number of components for a particular component kind
	 * 
	 * @param coKind
	 * @return
	 * @throws SQLException
	 */
    public List<Component> getAllComponents() {
        List<Component> components = new ArrayList<Component>();
        try {
            ResultSet resultSet = executeQuery(GET_COMPONENTS);
            while (resultSet.next()) {
                Component component = getOrCreateComponent(resultSet);
                components.add(component);
            }
            return components;
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    /**
	 * Returns the list of child components for a component and their associated
	 * concern edges.
	 * 
	 * @param con
	 * @param componentId
	 * @param componentTypeId
	 * @param concernId
	 * @return
	 * @throws SQLException
	 */
    public List<Component> getChildComponents(Component component, boolean orderByStartLine) {
        List<Component> children = componentToChildrenCache.get(component.getId());
        if (children != null) return children;
        children = new ArrayList<Component>();
        try {
            ResultSet resultSet = getChildComponentResults(component.getId(), orderByStartLine);
            if (resultSet == null) return children;
            while (resultSet.next()) {
                Component child = getOrCreateComponent(resultSet);
                children.add(child);
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
        componentToChildrenCache.put(component.getId(), children);
        return children;
    }

    /**
	 * Returns the list of child components for a component and their associated
	 * concern edges.
	 * 
	 * @param con
	 * @param componentId
	 * @param componentTypeId
	 * @param concernId
	 * @return
	 * @throws SQLException
	 */
    public ResultSet getChildComponentResults(int componentId, boolean orderByStartLine) {
        try {
            return executeQuery((orderByStartLine ? DBConstants.GET_COMPONENT_CHILDREN_ORDERED : DBConstants.GET_COMPONENT_CHILDREN), componentId, getEdgeKindId(EdgeKind.CONTAINS));
        } catch (SQLException e) {
            ProblemManager.reportException(e);
            return null;
        }
    }

    /**
	 * Removes a concern from concern table. If concern had linked components,
	 * those edges are removed. All parent/child relationships with other
	 * concerns are severed. If a concern domain is associated with the concern,
	 * it is removed as well.
	 */
    public int removeComponentAndChildren(Component component) {
        this.linkMap = new EnumMap<EdgeKind, Map<Integer, Set<Component>>>(EdgeKind.class);
        this.handleToComponentCache.clear();
        this.componentToChildrenCache.clear();
        int numRemoved = 0;
        for (Component child : component.getChildren()) {
            numRemoved += removeComponentAndChildren(child);
        }
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_COMPONENT, component.getId());
            statement.executeUpdate();
            statement.close();
            List<Object> params = new ArrayList<Object>();
            params.add(component.getId());
            params.add(component.getId());
            statement = createPreparedStatement(REMOVE_COMPONENT_EDGE, params);
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_COMPONENT, component.getId());
            statement.executeUpdate();
            statement.close();
            con.commit();
            return 1;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return 0;
        }
    }

    /**
	 * @return true if a new link was created, otherwise false
	 */
    public boolean link(Concern concern, Component component, EdgeKind linkType) {
        if (isLinked(concern, component, linkType)) return false;
        try {
            List<Object> params = new ArrayList<Object>();
            params.add(concern.getId());
            params.add(component.getId());
            params.add(getEdgeKindId(linkType));
            PreparedStatement preparedStatement = createPreparedStatement(CONCERN_COMPONENT_EDGE_SQL, params);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return true;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }

    public boolean link(Concern concern, Component[] componentsToLink, EdgeKind linkType) {
        Collection<Component> alreadyLinkedComponents = getLinks(concern, linkType);
        if (alreadyLinkedComponents == null) alreadyLinkedComponents = new ArrayList<Component>(componentsToLink.length);
        try {
            int linkTypeId = getEdgeKindId(linkType);
            PreparedStatement statement = getConnection().prepareStatement(CONCERN_COMPONENT_EDGE_SQL);
            for (Component componentToLink : componentsToLink) {
                if (alreadyLinkedComponents.add(componentToLink)) {
                    statement.setInt(1, concern.getId());
                    statement.setInt(2, componentToLink.getId());
                    statement.setInt(3, linkTypeId);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            statement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return true;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }

    /**
	 * Remove a concern component edge
	 * 
	 * @param concernName
	 * @param componentHandle
	 */
    public boolean unlink(Concern concern, String componentHandle, EdgeKind linkType) {
        List<Object> params = new ArrayList<Object>();
        params.add(concern.getId());
        params.add(componentHandle);
        params.add(getEdgeKindId(linkType));
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_CONCERN_COMPONENT_EDGE, params);
            int numUnlinked = statement.executeUpdate();
            statement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return numUnlinked != 0;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }

    /**
	 * Remove a concern component edge
	 * 
	 * @param concernName
	 * @param componentHandle
	 */
    public int unlink(Concern concern, EdgeKind linkType) {
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_EDGE_KIND, concern.getId(), getEdgeKindId(linkType));
            int numUnlinked = statement.executeUpdate();
            statement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return numUnlinked;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return 0;
        }
    }

    /**
	 * Remove a concern component edge
	 * 
	 * @param concernName
	 * @param componentHandle
	 */
    public int unlink(EdgeKind linkType) {
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_EDGE_KIND, getEdgeKindId(linkType));
            int numUnlinked = statement.executeUpdate();
            statement.close();
            flushLinkCache(linkType);
            con.commit();
            return numUnlinked;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return 0;
        }
    }

    public boolean isLinked(Concern concern, EdgeKind linkType) {
        return !getLinksFromCache(concern.getId(), linkType).isEmpty();
    }

    public boolean isLinked(Concern concern, Component component, EdgeKind linkType) {
        return getLinksFromCache(concern.getId(), linkType).contains(component);
    }

    public Set<Component> getLinks(Concern concern, EdgeKind linkType) {
        return getLinksFromCache(concern.getId(), linkType);
    }

    public boolean isLinked(ConcernDomain concernDomain, Component component, EdgeKind linkType, boolean includeIgnoredConcerns) {
        List<Concern> linkedConcerns = getLinkedConcerns(concernDomain, component, linkType, null);
        if (linkedConcerns == null) return false;
        if (!includeIgnoredConcerns) {
            for (Concern concern : linkedConcerns) {
                if (!concern.isIgnore()) return true;
            }
            return false;
        } else {
            return linkedConcerns.size() > 0;
        }
    }

    public List<Concern> getLinkedConcerns(ConcernDomain concernDomain, Component component, EdgeKind linkType, IConcernListener changeListener) {
        List<Concern> linkedConcerns = null;
        try {
            ResultSet resultSet = executeQuery(GET_CONCERNS_FOR_COMPONENT, component.getId(), getEdgeKindId(linkType));
            while (resultSet.next()) {
                Concern candidateConcern = new Concern(this, changeListener, resultSet);
                if (candidateConcern.isInConcernDomain(concernDomain)) {
                    if (linkedConcerns == null) linkedConcerns = new ArrayList<Concern>();
                    linkedConcerns.add(candidateConcern);
                }
            }
            resultSet = null;
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
        return linkedConcerns;
    }

    public Set<Component> getLinksFromCache(Integer concernId, EdgeKind linkType) {
        Map<Integer, Set<Component>> linkMapForEdge = linkMap.get(linkType);
        if (linkMapForEdge == null) {
            linkMapForEdge = new HashMap<Integer, Set<Component>>();
            linkMap.put(linkType, linkMapForEdge);
        }
        Set<Component> linkedComponents = linkMapForEdge.get(concernId);
        if (linkedComponents != null) return linkedComponents;
        linkedComponents = new TreeSet<Component>();
        linkMapForEdge.put(concernId, linkedComponents);
        try {
            ResultSet resultSet = executeQuery(GET_COMPONENTS_FOR_CONCERN, concernId, getEdgeKindId(linkType));
            while (resultSet.next()) {
                Component component = getOrCreateComponent(resultSet);
                linkedComponents.add(component);
            }
        } catch (SQLException e) {
            ProblemManager.reportError("Failed to Access Concern Links", null, "Failed to retrieve components linked to concern '" + concernId + "'.\n" + e, true);
        }
        return linkedComponents;
    }

    public void flushLinkCache(Integer concernId, EdgeKind linkType) {
        Map<Integer, Set<Component>> linksForConcern = linkMap.get(linkType);
        if (linksForConcern != null) linksForConcern.put(concernId, null);
    }

    public void flushLinkCache(Integer concernId) {
        for (Map<Integer, Set<Component>> linksForConcern : linkMap.values()) {
            if (linksForConcern != null) linksForConcern.remove(concernId);
        }
    }

    public void flushLinkCache(EdgeKind linkType) {
        linkMap.put(linkType, null);
    }

    private static String resolveDatabaseDirectory(String dir) {
        if (doesDatabaseExist(dir)) return dir;
        String tryPath = dir + File.separator + "db";
        if (doesDatabaseExist(tryPath)) return tryPath;
        tryPath = dir + File.separator + ".metadata" + File.separator + ".plugins" + File.separator + "edu.columbia.concerns" + File.separator + "db";
        if (doesDatabaseExist(tryPath)) return tryPath;
        tryPath = dir + File.separator + "workspace" + File.separator + ".metadata" + File.separator + ".plugins" + File.separator + "edu.columbia.concerns" + File.separator + "db";
        if (doesDatabaseExist(tryPath)) return tryPath;
        return null;
    }

    private static boolean doesDatabaseExist(String path) {
        return new File(path + ".properties").exists();
    }

    /**
	 * Shuts down the database. HSQLDB requires this to commit changes when a
	 * database is closed.
	 */
    public void shutdown() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("SHUTDOWN");
        } catch (Exception e) {
            try {
                con.close();
                ProblemManager.reportException(e);
            } catch (SQLException e1) {
                ProblemManager.reportException(e);
                ProblemManager.reportException(e1);
            }
        }
    }

    public void resetDatabase() {
        DBReset.resetDatabase(this);
        this.linkMap = new EnumMap<EdgeKind, Map<Integer, Set<Component>>>(EdgeKind.class);
    }

    /**
	 * Execute a query statement. The list of values should match the number of
	 * parameters in the sql.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private ResultSet executeQuery(String sql) throws SQLException {
        return createPreparedStatement(sql).executeQuery();
    }

    private ResultSet executeQuery(String sql, Integer value) throws SQLException {
        return createPreparedStatement(sql, value).executeQuery();
    }

    private ResultSet executeQuery(String sql, Integer value1, Integer value2) throws SQLException {
        return createPreparedStatement(sql, value1, value2).executeQuery();
    }

    private ResultSet executeQuery(String sql, String value) throws SQLException {
        return createPreparedStatement(sql, value).executeQuery();
    }

    /**
	 * Create a prepared statement for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, List<Object> params) throws SQLException {
        assert params != null;
        assert sql != null;
        PreparedStatement pstmt;
        try {
            pstmt = getConnection().prepareStatement(sql);
        } catch (NullPointerException e) {
            ProblemManager.reportException(e, "Failed to execute SQL statement: '" + sql + "' with parameters: '" + params.toString() + "'");
            return null;
        }
        int i = 1;
        for (Object param : params) {
            if (param instanceof String) {
                pstmt.setString(i, (String) param);
            } else if (param instanceof Integer) {
                pstmt.setInt(i, (Integer) param);
            } else {
                assert false;
                pstmt.setObject(i, param);
            }
            ++i;
        }
        return pstmt;
    }

    /**
	 * Create a prepared statement for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, int value) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setInt(1, value);
        return pstmt;
    }

    /**
	 * Create a prepared statement for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, String value1, int value2) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setString(1, value1);
        pstmt.setInt(2, value2);
        return pstmt;
    }

    /**
	 * Create a prepared statement for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, int value1, int value2) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setInt(1, value1);
        pstmt.setInt(2, value2);
        return pstmt;
    }

    /**
	 * Create a prepared statement for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, String value) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setString(1, value);
        return pstmt;
    }

    /**
	 * Create a prepared statment for an sql and associated list of values.
	 * 
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql, String value1, String value2) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setString(1, value1);
        pstmt.setString(2, value2);
        return pstmt;
    }

    /**
	 * Create a prepared statement for an sql.
	 * 
	 * @param sql
	 * @return Prepared statement.
	 * @throws SQLException
	 */
    private PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    private void rollback() {
        try {
            if (con != null) con.rollback();
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Gets a connection to the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
    public synchronized Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection("jdbc:hsqldb:file:" + databaseLocation);
            con.setAutoCommit(false);
        }
        return con;
    }

    /**
	 * Sync Java enumeration with table
	 * 
	 * @throws SQLException
	 */
    private void verifyEdgeKinds() {
        try {
            ResultSet resultSet = executeQuery(GET_EDGE_KINDS);
            while (resultSet.next()) {
                String edgeKindName = resultSet.getString("name");
                int edgeKindId = resultSet.getInt("edge_kind_id");
                assert edgeKindId == EdgeKind.valueOfIgnoreCase(edgeKindName).ordinal() + 1;
            }
            for (EdgeKind kind : EdgeKind.values()) {
                resultSet = executeQuery(GET_EDGE_KIND, kind.ordinal() + 1);
                if (resultSet.next()) {
                    String edgeKindName = resultSet.getString("name");
                    int edgeKindId = resultSet.getInt("edge_kind_id");
                    assert edgeKindId == EdgeKind.valueOfIgnoreCase(edgeKindName).ordinal() + 1;
                } else {
                    List<Object> params = new ArrayList<Object>();
                    params.add(kind.ordinal() + 1);
                    params.add(kind.name());
                    PreparedStatement statement = createPreparedStatement(EDGE_KIND_SQL, params);
                    statement.executeUpdate();
                    statement.close();
                    con.commit();
                }
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Load component_kind table into map.
	 * 
	 * @throws SQLException
	 */
    private void verifyComponentKinds() {
        try {
            ResultSet resultSet = executeQuery("select * from component_kind");
            while (resultSet.next()) {
                String componentKindName = resultSet.getString("name");
                int componentKindId = resultSet.getInt("id");
                assert componentKindId == ComponentKind.valueOfIgnoreCase(componentKindName).ordinal() + 1;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
    }

    /**
	 * Gets the database value for a component kind
	 * 
	 * @param componentKindEnum
	 * @return
	 */
    public int getComponentKindId(ComponentKind componentKindEnum) {
        return componentKindEnum.ordinal() + 1;
    }

    /**
	 * Gets the database value for a edge kind
	 * 
	 * @param edgeKindEnum
	 * @return
	 */
    public int getEdgeKindId(EdgeKind edgeKindEnum) {
        return edgeKindEnum.ordinal() + 1;
    }

    /**
	 * Generates the next integer for the column in a table. It assumes we have
	 * a single connection. We should change this to use sequences.
	 * 
	 * @param con
	 * @param sequenceName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
    public Integer getNextSequenceNumber(String sequenceName, String tableName) {
        try {
            ResultSet resultSet = executeQuery("select max(" + sequenceName + ") from " + tableName);
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            ProblemManager.reportException(e);
        }
        ProblemManager.reportError("Failed to Create Item", "Unable to create sequence", tableName);
        return null;
    }
}
