package hub.sam.mof.simulator.behaviour.diagram.navigator;

import hub.sam.mof.simulator.behaviour.diagram.part.M3ActionsVisualIDRegistry;
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
public class M3ActionsNavigatorContentProvider implements ICommonContentProvider {

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
    public M3ActionsNavigatorContentProvider() {
        TransactionalEditingDomain editingDomain = org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory.INSTANCE.createEditingDomain();
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
        myWorkspaceSynchronizer = new WorkspaceSynchronizer(editingDomain, new org.eclipse.emf.workspace.util.WorkspaceSynchronizer.Delegate() {

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
            result.addAll(createNavigatorItems(selectViewsByType(resource.getContents(), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MBehaviourEditPart.MODEL_ID), file, false));
            return result.toArray();
        }
        if (parentElement instanceof M3ActionsNavigatorGroup) {
            M3ActionsNavigatorGroup group = (M3ActionsNavigatorGroup) parentElement;
            return group.getChildren();
        }
        if (parentElement instanceof M3ActionsNavigatorItem) {
            M3ActionsNavigatorItem navigatorItem = (M3ActionsNavigatorItem) parentElement;
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
        switch(M3ActionsVisualIDRegistry.getVisualID(view)) {
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MBehaviourEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup links = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MBehaviour_1000_links, "icons/linksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNodeEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNodeEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNodeEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNodeEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNodeEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignActionEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateActionEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationActionEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryActionEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInputEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MOutputEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getDiagramLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    links.addChildren(createNavigatorItems(connectedViews, links, false));
                    if (!links.isEmpty()) {
                        result.add(links);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNodeEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInitialNode_2001_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInitialNode_2001_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNodeEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MFinalNode_2002_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MFinalNode_2002_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNodeEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MDecisionMergeNode_2003_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MDecisionMergeNode_2003_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNodeEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MForkJoinNode_2004_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MForkJoinNode_2004_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNodeEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MObjectNode_2005_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MObjectNode_2005_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignActionEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAssignAction_2006_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAssignAction_2006_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateActionEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MCreateAction_2007_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MCreateAction_2007_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationActionEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInvocationAction_2008_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInvocationAction_2008_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MIterateAction_2009_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MIterateAction_2009_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryActionEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MQueryAction_2010_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MQueryAction_2010_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInputEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInput_2011_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInput_2011_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MOutputEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MOutput_2012_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MOutput_2012_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAtomicGroup_2013_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAtomicGroup_2013_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MPin_3001_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MPin_3001_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAtomicGroup_3012_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAtomicGroup_3012_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupMAtomicGroupContentPaneCompartmentEditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInitialNode_3013_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInitialNode_3013_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MFinalNode_3014_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MFinalNode_3014_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MDecisionMergeNode_3015_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MDecisionMergeNode_3015_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MForkJoinNode_3016_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MForkJoinNode_3016_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MObjectNode_3017_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MObjectNode_3017_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MCreateAction_3008_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MCreateAction_3008_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAssignAction_3007_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MAssignAction_3007_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInvocationAction_3009_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MInvocationAction_3009_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MIterateAction_3021_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MIterateAction_3021_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionMIterateActionContentPaneCompartment2EditPart.VISUAL_ID);
                    connectedViews = getChildrenByType(connectedViews, hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup incominglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MQueryAction_3022_incominglinks, "icons/incomingLinksNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup outgoinglinks = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MQueryAction_3022_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getChildrenByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    result.addAll(createNavigatorItems(connectedViews, parentElement, false));
                    connectedViews = getIncomingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
                    connectedViews = getOutgoingLinksByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID);
                    outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
                    if (!incominglinks.isEmpty()) {
                        result.add(incominglinks);
                    }
                    if (!outgoinglinks.isEmpty()) {
                        result.add(outgoinglinks);
                    }
                    return result.toArray();
                }
            case hub.sam.mof.simulator.behaviour.diagram.edit.parts.MControlFlowEditPart.VISUAL_ID:
                {
                    Collection result = new ArrayList();
                    M3ActionsNavigatorGroup target = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MControlFlow_4001_target, "icons/linkTargetNavigatorGroup.gif", parentElement);
                    M3ActionsNavigatorGroup source = new M3ActionsNavigatorGroup(hub.sam.mof.simulator.behaviour.diagram.part.Messages.NavigatorGroupName_MControlFlow_4001_source, "icons/linkSourceNavigatorGroup.gif", parentElement);
                    Collection connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNodeEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNodeEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNodeEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNodeEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNodeEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignActionEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateActionEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationActionEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryActionEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInputEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MOutputEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksTargetByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
                    target.addChildren(createNavigatorItems(connectedViews, target, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNodeEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNodeEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNodeEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNodeEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNodeEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignActionEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateActionEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationActionEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateActionEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryActionEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInputEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MOutputEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroupEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MPinEditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAtomicGroup2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInitialNode2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MFinalNode2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MDecisionMergeNode2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MForkJoinNode2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MObjectNode2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MCreateAction2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MAssignAction2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MInvocationAction2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MIterateAction2EditPart.VISUAL_ID);
                    source.addChildren(createNavigatorItems(connectedViews, source, true));
                    connectedViews = getLinksSourceByType(Collections.singleton(view), hub.sam.mof.simulator.behaviour.diagram.edit.parts.MQueryAction2EditPart.VISUAL_ID);
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
    private Collection getLinksSourceByType(Collection edges, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
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
    private Collection getLinksTargetByType(Collection edges, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
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
    private Collection getOutgoingLinksByType(Collection nodes, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getSourceEdges(), type));
        }
        return result;
    }

    /**
	 * @generated
	 */
    private Collection getIncomingLinksByType(Collection nodes, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getTargetEdges(), type));
        }
        return result;
    }

    /**
	 * @generated
	 */
    private Collection getChildrenByType(Collection nodes, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            View nextNode = (View) it.next();
            result.addAll(selectViewsByType(nextNode.getChildren(), type));
        }
        return result;
    }

    /**
	 * @generated
	 */
    private Collection getDiagramLinksByType(Collection diagrams, int visualID) {
        Collection result = new ArrayList();
        String type = M3ActionsVisualIDRegistry.getType(visualID);
        for (Iterator it = diagrams.iterator(); it.hasNext(); ) {
            Diagram nextDiagram = (Diagram) it.next();
            result.addAll(selectViewsByType(nextDiagram.getEdges(), type));
        }
        return result;
    }

    /**
	 * @generated
	 */
    private Collection selectViewsByType(Collection views, String type) {
        Collection result = new ArrayList();
        for (Iterator it = views.iterator(); it.hasNext(); ) {
            View nextView = (View) it.next();
            if (type.equals(nextView.getType()) && isOwnView(nextView)) {
                result.add(nextView);
            }
        }
        return result;
    }

    /**
	 * @generated
	 */
    private boolean isOwnView(View view) {
        return hub.sam.mof.simulator.behaviour.diagram.edit.parts.MBehaviourEditPart.MODEL_ID.equals(M3ActionsVisualIDRegistry.getModelID(view));
    }

    /**
	 * @generated
	 */
    private Collection createNavigatorItems(Collection views, Object parent, boolean isLeafs) {
        Collection result = new ArrayList();
        for (Iterator it = views.iterator(); it.hasNext(); ) {
            result.add(new M3ActionsNavigatorItem((View) it.next(), parent, isLeafs));
        }
        return result;
    }

    /**
	 * @generated
	 */
    public Object getParent(Object element) {
        if (element instanceof M3ActionsAbstractNavigatorItem) {
            M3ActionsAbstractNavigatorItem abstractNavigatorItem = (M3ActionsAbstractNavigatorItem) element;
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
