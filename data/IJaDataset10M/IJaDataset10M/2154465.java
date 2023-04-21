package org.icefaces.application.showcase.view.bean;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.showcase.util.ContextUtilBean;
import org.icefaces.application.showcase.util.FacesUtils;
import org.icefaces.application.showcase.view.builder.ApplicationBuilder;
import org.icefaces.application.showcase.view.jaxb.*;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

/**
 * <p>Application controller is responsible for managing the presentation
 * layer action and actionListener functionality.  This bean is intended
 * to stay in Application scope and act on request and session beans as
 * needed. This class mainly handles site navigation processing via the tree
 * and common Tabset components.  Source and Document iFrame links are
 * also processed by this class.</p>
 * <p/>
 * <p>Each individual component showcase example has its own set of Beans which
 * are issolated from the main application and other examples.  Some examples
 * use a common mock service layer for retrieving Employee Objects.</p>
 *
 * @since 1.7
 */
public class ApplicationController implements Serializable {

    private static final Log logger = LogFactory.getLog(ApplicationController.class);

    /**
     * <p>Tree navigation events are processed by this method when a tree
     * leaf or folder is clicked on by the user.  This method assumes
     * that the navigation node in question passed the request param
     * "nodeId" in the request map.  The "nodeId" is a unique id assigned to
     * the node from the application meta data.  This ide is then used
     * to find the corresponding Node object from the ApplicationBuilder class</p>
     * <p/>
     * <p>A node is marked as selected and the previously selected node is
     * marked as unselected.  The nodes corresponding include may have
     * a tabSet state which will either be loaded or initialized.  This is
     * important to note as it allows the use of only one tabSetComponent
     * for the intire application.</p>
     *
     * @param event jsf action event
     */
    public void navigationEvent(ActionEvent event) {
        String nodeId = FacesUtils.getRequestParameter("nodeId");
        ApplicationBuilder applicationBuilder = (ApplicationBuilder) FacesUtils.getManagedBean(BeanNames.APPLICATION_BUILDER);
        Node node = applicationBuilder.getNode(nodeId);
        if (node != null && node.isLink()) {
            ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
            setTreeNodeSelectedState(applicationModel.getCurrentNode().getId(), false);
            applicationModel.setCurrentNode(node);
            setTreeNodeSelectedState(applicationModel.getCurrentNode().getId(), true);
            HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
            TabState tabState = tabStates.get(node);
            if (tabState == null) {
                tabState = new TabState();
                tabState.setTabIndex(TabState.DEMONSTRATION_TAB_INDEX);
                tabState.setDescriptionContent(getDefaultDocumentPath(node));
                tabState.setSourceContent(getDefaultSourcePath(node));
            }
            tabStates.put(node, tabState);
        } else if (node != null && !node.isLink()) {
            toggleTreeNodeExpandedState(node.getId());
        }
    }

    /**
     * <p>The method captures the selected tab state of the currently selected
     * node content.  We only use one Tabset for this application so this
     * method is responsible for making sure that each nodes content state
     * is persisted.</p>
     *
     * @param tabChangeEvent used to set the tab focus.
     * @throws javax.faces.event.AbortProcessingException
     *          An exception that may
     *          be thrown by event listeners to terminate the processing of the current event.
     */
    public void processTabChange(TabChangeEvent tabChangeEvent) throws AbortProcessingException {
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Node currentNode = applicationModel.getCurrentNode();
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);
        tabState.setTabIndex(tabChangeEvent.getNewTabIndex());
    }

    /**
     * <p>Each example has a source code tab which has links to JSPX
     * and Java Beans associated with the example.  The selection of source
     * code is handle by the method {@link #viewSourceEvent(javax.faces.event.ActionEvent)}
     * .  This method takes the selected source code and generates a
     * valid iFrame tag to which is used to load the source code.  The iFrame
     * url points to a Servlet which does the work of sending back the source
     * code in plain text. </p>
     *
     * @return iFrame tag and src which points to currently selected source
     *         code content.  If no source content is selected an iframe with an empty
     *         source link is returned.
     */
    public String getCurrentSource() {
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Node currentNode = applicationModel.getCurrentNode();
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);
        if (tabState.getSourceContent() != null) {
            return ContextUtilBean.generateSourceCodeIFrame(tabState.getSourceContent());
        }
        return ContextUtilBean.generateIFrame("");
    }

    /**
     * <p>Each example has a documentation tab which has links to documentation
     * and tutorials associated with the example.  The selection of document
     * code is handle by the method {@link #viewIncludeEvent(javax.faces.event.ActionEvent)}
     * .  This method takes the selected document and generates a
     * valid iFrame tag to which is used to load the documentation.  The iFrame
     * url points to a Servlet which does the work of sending back the
     * documentation in plain text. </p>
     *
     * @return iFrame tag and src which points to currently selected
     *         documentation.  If no source content is selected an iframe with
     *         an empty source link is returned.
     */
    public String getCurrentDocumentSource() {
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Node currentNode = applicationModel.getCurrentNode();
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);
        if (tabState.getDescriptionContent() != null) {
            return ContextUtilBean.generateIFrameWithContextPath(tabState.getDescriptionContent());
        }
        return ContextUtilBean.generateIFrame("");
    }

    /**
     * <p>Loads the source code specified by the request parameters includePath.
     * The parameters includePath is used to generate the full file path
     * needed by the SourceCodeServlet. The path generated by this method
     * is stored in the session TabState object. </p>
     *
     * @param event jsf action event.
     */
    public void viewSourceEvent(ActionEvent event) {
        String includePath = FacesUtils.getRequestParameter("includePath");
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Node currentNode = applicationModel.getCurrentNode();
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);
        tabState.setSourceContent(includePath);
        tabStates.put(currentNode, tabState);
    }

    /**
     * <p>Loads the documentation specified by the request parameters includePath.
     * The parameters includePath is used to generate the full file path
     * needed by the SourceCodeServlet. The path generated by this method
     * is stored in the session TabState object. </p>
     *
     * @param event jsf action event.
     */
    public void viewIncludeEvent(ActionEvent event) {
        String includePath = FacesUtils.getRequestParameter("includePath");
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Node currentNode = applicationModel.getCurrentNode();
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);
        tabState.setDescriptionContent(includePath);
        tabStates.put(currentNode, tabState);
    }

    /**
     * <p>Gets the ContextDescriptor object associated with the currently
     * selected node.  The ContextDescriptor class contains example,
     * documentation and source code information derived from the schema
     * data.</p>
     *
     * @return ContentDescriptor associated withthe selected node.
     */
    public ContentDescriptor getCurrentContextDescriptor() {
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        ApplicationBuilder applicationBuilder = (ApplicationBuilder) FacesUtils.getManagedBean(BeanNames.APPLICATION_BUILDER);
        Node currentNode = applicationModel.getCurrentNode();
        return applicationBuilder.getContextDescriptor(currentNode);
    }

    /**
     * Utility method to assign the selected state of a tree node.
     *
     * @param nodeId nodeId of the node to set the selected state on.
     * @param value  desired selection state of node.
     */
    private void setTreeNodeSelectedState(String nodeId, boolean value) {
        DefaultMutableTreeNode defaultNode = findTreeNode(nodeId);
        if (defaultNode != null) {
            NavigationTreeNode node = (NavigationTreeNode) defaultNode.getUserObject();
            node.setSelected(value);
        }
    }

    /**
     * Utility method to toggle the selected state of the specified node.
     *
     * @param nodeId nodeId of the the node to toggle the expanded state of.
     */
    private void toggleTreeNodeExpandedState(String nodeId) {
        DefaultMutableTreeNode defaultNode = findTreeNode(nodeId);
        if (defaultNode != null) {
            NavigationTreeNode node = (NavigationTreeNode) defaultNode.getUserObject();
            node.setExpanded(!node.isExpanded());
        }
    }

    /**
     * Utility method to find a tree node by its ID.
     *
     * @param nodeId node Id of node to to find in tree.
     * @return node specified by ID or null of no node of that ID is found.
     */
    private DefaultMutableTreeNode findTreeNode(String nodeId) {
        ApplicationSessionModel applicationModel = (ApplicationSessionModel) FacesUtils.getManagedBean(BeanNames.APPLICATION_MODEL);
        Collection<DefaultTreeModel> trees = applicationModel.getNavigationTrees().values();
        DefaultMutableTreeNode node;
        DefaultMutableTreeNode rootNode;
        for (DefaultTreeModel treeModel : trees) {
            rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
            Enumeration nodes = rootNode.depthFirstEnumeration();
            while (nodes.hasMoreElements()) {
                node = (DefaultMutableTreeNode) nodes.nextElement();
                NavigationTreeNode tmp = (NavigationTreeNode) node.getUserObject();
                if (tmp.getNodeId() != null && tmp.getNodeId().equals(nodeId)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Uitlity method to find the first available document reference that
     * can be used at the default content.
     *
     * @param node node to search child documentation resources for a the
     *             first available documentation link.
     * @return first available documentation link or an empty String if none
     *         are found.
     */
    private String getDefaultDocumentPath(Node node) {
        if (node.getContentDescriptor().getDocumentation().getDocuments() != null) {
            List<ReferenceType> references = node.getContentDescriptor().getDocumentation().getDocuments().getResourceReference();
            if (references != null && references.get(0) != null && references.get(0).getResourceRef() != null) {
                return references.get(0).getResourceRef().getPath();
            }
        }
        if (node.getContentDescriptor().getDocumentation().getTlds() != null) {
            List<ReferenceType> references = node.getContentDescriptor().getDocumentation().getTlds().getResourceReference();
            if (references != null && references.get(0) != null && references.get(0).getResourceRef() != null) {
                return references.get(0).getResourceRef().getPath();
            }
        }
        return "";
    }

    /**
     * Uitlity method to find the first available source reference that
     * can be used at the default content.
     *
     * @param node node to search child documentation resources for a the
     *             first available source link.
     * @return first available source link or an empty String if none
     *         are found.
     */
    private String getDefaultSourcePath(Node node) {
        if (node.getContentDescriptor().getSourceCode().getBeans() != null) {
            List<ReferenceType> references = node.getContentDescriptor().getSourceCode().getBeans().getResourceReference();
            if (references != null && references.get(0) != null && references.get(0).getResourceRef() != null) {
                return references.get(0).getResourceRef().getPath();
            }
        }
        if (node.getContentDescriptor().getSourceCode().getJspxPages() != null) {
            List<ReferenceType> references = node.getContentDescriptor().getSourceCode().getJspxPages().getResourceReference();
            if (references != null && references.get(0) != null && references.get(0).getResourceRef() != null) {
                return references.get(0).getResourceRef().getPath();
            }
        }
        return "";
    }
}
