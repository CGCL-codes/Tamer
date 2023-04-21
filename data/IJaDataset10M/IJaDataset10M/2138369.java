package org.codescale.eDependency.diagram.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

/**
 * @generated
 */
public class EDependencyNavigatorContentProvider implements ICommonContentProvider {

    /**
     * @generated
     */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * @generated
     */
    private Viewer myViewer;

    /**
     * @generated
     */
    private AdapterFactoryEditingDomain myEditingDomain;

    /**
     * @generated
     */
    private WorkspaceSynchronizer myWorkspaceSynchronizer;

    /**
     * @generated
     */
    private Runnable myViewerRefreshRunnable;

    /**
     * @generated
     */
    public EDependencyNavigatorContentProvider() {
        TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();
        myEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
        myEditingDomain.setResourceToReadOnlyMap(new HashMap() {

            public Object get(Object key) {
                if (!containsKey(key)) {
                    put(key, Boolean.TRUE);
                }
                return super.get(key);
            }
        });
        myViewerRefreshRunnable = new Runnable() {

            public void run() {
                if (myViewer != null) {
                    myViewer.refresh();
                }
            }
        };
        myWorkspaceSynchronizer = new WorkspaceSynchronizer(editingDomain, new WorkspaceSynchronizer.Delegate() {

            public void dispose() {
            }

            public boolean handleResourceChanged(final Resource resource) {
                for (Iterator it = myEditingDomain.getResourceSet().getResources().iterator(); it.hasNext(); ) {
                    Resource nextResource = (Resource) it.next();
                    nextResource.unload();
                }
                if (myViewer != null) {
                    myViewer.getControl().getDisplay().asyncExec(myViewerRefreshRunnable);
                }
                return true;
            }

            public boolean handleResourceDeleted(Resource resource) {
                for (Iterator it = myEditingDomain.getResourceSet().getResources().iterator(); it.hasNext(); ) {
                    Resource nextResource = (Resource) it.next();
                    nextResource.unload();
                }
                if (myViewer != null) {
                    myViewer.getControl().getDisplay().asyncExec(myViewerRefreshRunnable);
                }
                return true;
            }

            public boolean handleResourceMoved(Resource resource, final URI newURI) {
                for (Iterator it = myEditingDomain.getResourceSet().getResources().iterator(); it.hasNext(); ) {
                    Resource nextResource = (Resource) it.next();
                    nextResource.unload();
                }
                if (myViewer != null) {
                    myViewer.getControl().getDisplay().asyncExec(myViewerRefreshRunnable);
                }
                return true;
            }
        });
    }

    /**
     * @generated
     */
    public void dispose() {
        myWorkspaceSynchronizer.dispose();
        myWorkspaceSynchronizer = null;
        myViewerRefreshRunnable = null;
        for (Iterator it = myEditingDomain.getResourceSet().getResources().iterator(); it.hasNext(); ) {
            Resource resource = (Resource) it.next();
            resource.unload();
        }
        ((TransactionalEditingDomain) myEditingDomain).dispose();
        myEditingDomain = null;
    }

    /**
     * @generated
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        myViewer = viewer;
    }

    /**
     * @generated
     */
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    /**
     * @generated
     */
    public void restoreState(IMemento aMemento) {
    }

    /**
     * @generated
     */
    public void saveState(IMemento aMemento) {
    }

    /**
     * @generated
     */
    public void init(ICommonContentExtensionSite aConfig) {
    }

    /**
     * @generated
     */
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IFile) {
            IFile file = (IFile) parentElement;
            URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
            Resource resource = myEditingDomain.getResourceSet().getResource(fileURI, true);
            Collection result = new ArrayList();
            result.addAll(createNavigatorItems(selectViewsByType(resource.getContents(), org.codescale.eDependency.diagram.edit.parts.WorkspaceEditPart.MODEL_ID), file, false));
            return result.toArray();
        }
        if (parentElement instanceof org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup) {
            org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup group = (org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup) parentElement;
            return group.getChildren();
        }
        if (parentElement instanceof org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) {
            org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem navigatorItem = (org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) parentElement;
            if (navigatorItem.isLeaf() || !isOwnView(navigatorItem.getView())) {
                return EMPTY_ARRAY;
            }
            return getViewChildren(navigatorItem.getView(), parentElement);
        }
        return EMPTY_ARRAY;
    }

    /**
     * @generated
     */
    private Object[] getViewChildren(View view, Object parentElement) {
        switch(org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getVisualID(view)) {
            case org.codescale.eDependency.diagram.edit.parts.WorkspaceEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup links = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Workspace_79_links, "icons/linksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.BundleEditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PluginEditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.FeatureEditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getDiagramLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    links.addChildren(createNavigatorItems(connectedViews, links, false));
                    connectedViews = getDiagramLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireFeatureEditPart.VISUAL_ID));
                    links.addChildren(createNavigatorItems(connectedViews, links, false));
                    if (!links.isEmpty()) {
                        result.add(links);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.BundleEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup incominglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Bundle_1001_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup outgoinglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Bundle_1001_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.BundlePackagesEditPart.VISUAL_ID));
                    connectedViews = getChildrenByType(connectedViews, org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PackageEditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.PluginEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup incominglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Plugin_1002_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup outgoinglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Plugin_1002_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PluginPackagesEditPart.VISUAL_ID));
                    connectedViews = getChildrenByType(connectedViews, org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.Package2EditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.FeatureEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup incominglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Feature_1003_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup outgoinglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Feature_1003_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.FeaturePluginsEditPart.VISUAL_ID));
                    connectedViews = getChildrenByType(connectedViews, org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.Bundle2EditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireFeatureEditPart.VISUAL_ID));
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireFeatureEditPart.VISUAL_ID));
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.Bundle2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup incominglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Bundle_2003_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup outgoinglinks = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_Bundle_2003_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.BundlePackages2EditPart.VISUAL_ID));
                    connectedViews = getChildrenByType(connectedViews, org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PackageEditPart.VISUAL_ID));
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID));
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.RequireBundleEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup target = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_RequireBundle_3001_target, "icons/linkTargetNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup source = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_RequireBundle_3001_source, "icons/linkSourceNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getLinksTargetByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.BundleEditPart.VISUAL_ID));
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PluginEditPart.VISUAL_ID));
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.Bundle2EditPart.VISUAL_ID));
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.BundleEditPart.VISUAL_ID));
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.PluginEditPart.VISUAL_ID));
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.Bundle2EditPart.VISUAL_ID));
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    if (!target.isEmpty()) {
                        result.add(target);
                    }
                    if (!source.isEmpty()) {
                        result.add(source);
                    }
                    return result.toArray();
                }
            case org.codescale.eDependency.diagram.edit.parts.RequireFeatureEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup target = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_RequireFeature_3002_target, "icons/linkTargetNavigatorGroup.gif", parentElement);
                    org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup source = new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup(org.codescale.eDependency.diagram.part.Messages.NavigatorGroupName_RequireFeature_3002_source, "icons/linkSourceNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getLinksTargetByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.FeatureEditPart.VISUAL_ID));
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getType(org.codescale.eDependency.diagram.edit.parts.FeatureEditPart.VISUAL_ID));
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    if (!target.isEmpty()) {
                        result.add(target);
                    }
                    if (!source.isEmpty()) {
                        result.add(source);
                    }
                    return result.toArray();
                }
        }
        return EMPTY_ARRAY;
    }

    /**
     * @generated
     */
    private Collection getLinksSourceByType(Collection edges, String type) {
        Collection result = new ArrayList();
        for (Iterator it = edges.iterator(); it.hasNext(); ) {
            Edge nextEdge = (Edge) it.next();
            View nextEdgeSource = nextEdge.getSource();
            if (type.equals(nextEdgeSource.getType()) && isOwnView(nextEdgeSource)) {
                result.add(nextEdgeSource);
            }
        }
        return result;
    }

    /**
     * @generated
     */
    private Collection getLinksTargetByType(Collection edges, String type) {
        Collection result = new ArrayList();
        for (Iterator it = edges.iterator(); it.hasNext(); ) {
            Edge nextEdge = (Edge) it.next();
            View nextEdgeTarget = nextEdge.getTarget();
            if (type.equals(nextEdgeTarget.getType()) && isOwnView(nextEdgeTarget)) {
                result.add(nextEdgeTarget);
            }
        }
        return result;
    }

    /**
     * @generated
     */
    private Collection getOutgoingLinksByType(Collection nodes, String type) {
        Collection result = new ArrayList();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getSourceEdges(), type));
        }
        return result;
    }

    /**
     * @generated
     */
    private Collection getIncomingLinksByType(Collection nodes, String type) {
        Collection result = new ArrayList();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getTargetEdges(), type));
        }
        return result;
    }

    /**
     * @generated
     */
    private Collection getChildrenByType(Collection nodes, String type) {
        Collection result = new ArrayList();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getChildren(), type));
        }
        return result;
    }

    /**
     * @generated
     */
    private Collection getDiagramLinksByType(Collection diagrams, String type) {
        Collection result = new ArrayList();
        for (Iterator it = diagrams.iterator(); it.hasNext(); ) {
            Diagram nextDiagram = (Diagram) it.next();
            result.addAll(selectViewsByType(nextDiagram.getEdges(), type));
        }
        return result;
    }

    /**
     * @generated NOT
     */
    private Collection selectViewsByType(Collection views, String type) {
        Collection result = new ArrayList();
        for (Iterator it = views.iterator(); it.hasNext(); ) {
            Object object = it.next();
            if (object instanceof View) {
                View nextView = (View) object;
                if (type.equals(nextView.getType()) && isOwnView(nextView)) {
                    result.add(nextView);
                }
            }
        }
        return result;
    }

    /**
     * @generated
     */
    private boolean isOwnView(View view) {
        return org.codescale.eDependency.diagram.edit.parts.WorkspaceEditPart.MODEL_ID.equals(org.codescale.eDependency.diagram.part.EDependencyVisualIDRegistry.getModelID(view));
    }

    /**
     * @generated
     */
    private Collection createNavigatorItems(Collection views, Object parent, boolean isLeafs) {
        Collection result = new ArrayList();
        for (Iterator it = views.iterator(); it.hasNext(); ) {
            result.add(new org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem((View) it.next(), parent, isLeafs));
        }
        return result;
    }

    /**
     * @generated
     */
    public Object getParent(Object element) {
        if (element instanceof org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem) {
            org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem abstractNavigatorItem = (org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem) element;
            return abstractNavigatorItem.getParent();
        }
        return null;
    }

    /**
     * @generated
     */
    public boolean hasChildren(Object element) {
        return element instanceof IFile || getChildren(element).length > 0;
    }
}
