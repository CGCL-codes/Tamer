package net.sourceforge.iwii.db.dev.ui.common;

import net.sourceforge.iwii.db.dev.ui.start.nodes.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.IBusinessObject;
import net.sourceforge.iwii.db.dev.bo.login.ServerConnectionBO;
import net.sourceforge.iwii.db.dev.bo.project.ProjectBO;
import net.sourceforge.iwii.db.dev.bo.project.ProjectPhaseBO;
import net.sourceforge.iwii.db.dev.bo.project.ProjectSubPhaseBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactVersionBO;
import net.sourceforge.iwii.db.dev.ui.main.nodes.ProjectArtifactNode;
import net.sourceforge.iwii.db.dev.ui.main.nodes.ProjectArtifactVersionNode;
import net.sourceforge.iwii.db.dev.ui.main.nodes.ProjectNode;
import net.sourceforge.iwii.db.dev.ui.main.nodes.ProjectPhaseNode;
import net.sourceforge.iwii.db.dev.ui.main.nodes.ProjectSubPhaseNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Class represents factory of nodes. Nodes are constructed based on specific business object.
 * 
 * @author Grzegorz 'Gregor736' Wolszcza
 * @version 1.00
 */
public class NodesFactory {

    /**
     * Class represents node map keys used in this factory
     */
    public static enum NodesFactoryKeys {

        ServerConnectionForLogin, ProjectForProjectExplorer, ProjectPhaseForProjectExplorer, ProjectSubPhaseForProjectExplorer, ProjectArtifactForProjectExplorer, ProjectArtifactVersionForProjectExplorer
    }

    private static final Logger logger = Logger.getLogger(NodesFactory.class.getName());

    private static NodesFactory instance = null;

    private Map<Class, Map<NodesFactoryKeys, Class>> factoryMap;

    /**
     * Default constructor. Initialize factory map.
     */
    private NodesFactory() {
        long start = System.currentTimeMillis();
        NodesFactory.logger.log(Level.INFO, "Initializating...");
        this.factoryMap = new HashMap<Class, Map<NodesFactoryKeys, Class>>();
        Map serverConnectionBOMap = new HashMap<NodesFactoryKeys, Class>();
        serverConnectionBOMap.put(NodesFactory.NodesFactoryKeys.ServerConnectionForLogin, ServerConnectionNode.class);
        this.factoryMap.put(ServerConnectionBO.class, serverConnectionBOMap);
        Map projectBOMap = new HashMap<NodesFactoryKeys, Class>();
        projectBOMap.put(NodesFactory.NodesFactoryKeys.ProjectForProjectExplorer, ProjectNode.class);
        this.factoryMap.put(ProjectBO.class, projectBOMap);
        Map projectPhaseBOMap = new HashMap<NodesFactoryKeys, Class>();
        projectPhaseBOMap.put(NodesFactory.NodesFactoryKeys.ProjectPhaseForProjectExplorer, ProjectPhaseNode.class);
        this.factoryMap.put(ProjectPhaseBO.class, projectPhaseBOMap);
        Map projectSubPhaseBOMap = new HashMap<NodesFactoryKeys, Class>();
        projectSubPhaseBOMap.put(NodesFactory.NodesFactoryKeys.ProjectSubPhaseForProjectExplorer, ProjectSubPhaseNode.class);
        this.factoryMap.put(ProjectSubPhaseBO.class, projectSubPhaseBOMap);
        Map projectArtifactBOMap = new HashMap<NodesFactoryKeys, Class>();
        projectArtifactBOMap.put(NodesFactory.NodesFactoryKeys.ProjectArtifactForProjectExplorer, ProjectArtifactNode.class);
        this.factoryMap.put(ProjectArtifactBO.class, projectArtifactBOMap);
        Map projectArtifactVersionBOMap = new HashMap<NodesFactoryKeys, Class>();
        projectArtifactVersionBOMap.put(NodesFactory.NodesFactoryKeys.ProjectArtifactVersionForProjectExplorer, ProjectArtifactVersionNode.class);
        this.factoryMap.put(ProjectArtifactVersionBO.class, projectArtifactVersionBOMap);
        NodesFactory.logger.log(Level.INFO, "Initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
    }

    /**
     * Gets factory instance.
     * @return factory instance
     */
    public static NodesFactory getInstance() {
        if (NodesFactory.instance == null) NodesFactory.instance = new NodesFactory();
        return NodesFactory.instance;
    }

    public Node createListRootNode(Collection<? extends IBusinessObject> businessObjects, java.util.Map<Class, NodesFactory.NodesFactoryKeys> configuration) {
        return new AbstractNode(new ListRoot(businessObjects, configuration));
    }

    public Node createTreeRootNode(Collection<? extends IBusinessObject> businessObjects, java.util.Map<Class, NodesFactory.NodesFactoryKeys> configuration) {
        return new AbstractNode(new TreeRoot(businessObjects, configuration));
    }

    /**
     * Creates simple node for given business object (simple node wrapps arround given business objects)
     * 
     * @param businessObject business object which is a base for node creation  
     * @param configuration represents type of right node to create
     * @return created node or null if creation was not possible
     */
    public Node createSimpleNodeForBO(IBusinessObject businessObject, NodesFactoryKeys configuration) {
        Map<NodesFactoryKeys, Class> nodeMap = this.factoryMap.get(businessObject.getClass());
        if (nodeMap == null) {
            return null;
        }
        Class nodeClass = nodeMap.get(configuration);
        if (nodeClass == null) {
            return null;
        }
        try {
            Constructor nodeConstructor = nodeClass.getConstructor(businessObject.getClass());
            return (Node) nodeConstructor.newInstance(businessObject);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
