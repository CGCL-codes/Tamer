package org.germinus.telcoblocks.servicios.diagram.edit.policies;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.germinus.telcoblocks.TelcoblocksPackage;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.AnunciosEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.ClickToDialEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.DesvioEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.EnlaceEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.FacturacionEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.IMEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.LlamadasEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.PersonalizacionEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.SERVICIOSEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.ServicioGenEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.TelefonoEditPart;
import org.germinus.telcoblocks.servicios.diagram.edit.parts.WebEditPart;
import org.germinus.telcoblocks.servicios.diagram.part.TelcoblocksDiagramUpdater;
import org.germinus.telcoblocks.servicios.diagram.part.TelcoblocksLinkDescriptor;
import org.germinus.telcoblocks.servicios.diagram.part.TelcoblocksNodeDescriptor;
import org.germinus.telcoblocks.servicios.diagram.part.TelcoblocksVisualIDRegistry;

/**
 * @generated
 */
public class SERVICIOSCanonicalEditPolicy extends CanonicalConnectionEditPolicy {

    /**
	 * @generated
	 */
    Set myFeaturesToSynchronize;

    /**
	 * @generated
	 */
    protected List getSemanticChildrenList() {
        View viewObject = (View) getHost().getModel();
        List result = new LinkedList();
        for (Iterator it = TelcoblocksDiagramUpdater.getSERVICIOS_1000SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((TelcoblocksNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean shouldDeleteView(View view) {
        return true;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = TelcoblocksVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case ClickToDialEditPart.VISUAL_ID:
            case LlamadasEditPart.VISUAL_ID:
            case ServicioGenEditPart.VISUAL_ID:
            case PersonalizacionEditPart.VISUAL_ID:
            case IMEditPart.VISUAL_ID:
            case FacturacionEditPart.VISUAL_ID:
            case WebEditPart.VISUAL_ID:
            case DesvioEditPart.VISUAL_ID:
            case AnunciosEditPart.VISUAL_ID:
            case TelefonoEditPart.VISUAL_ID:
                if (!semanticChildren.contains(view.getElement())) {
                    return true;
                }
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected String getDefaultFactoryHint() {
        return null;
    }

    /**
	 * @generated
	 */
    protected Set getFeaturesToSynchronize() {
        if (myFeaturesToSynchronize == null) {
            myFeaturesToSynchronize = new HashSet();
            myFeaturesToSynchronize.add(TelcoblocksPackage.eINSTANCE.getSERVICIOS_Nodos());
        }
        return myFeaturesToSynchronize;
    }

    /**
	 * @generated
	 */
    protected List getSemanticConnectionsList() {
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    protected EObject getSourceElement(EObject relationship) {
        return null;
    }

    /**
	 * @generated
	 */
    protected EObject getTargetElement(EObject relationship) {
        return null;
    }

    /**
	 * @generated
	 */
    protected boolean shouldIncludeConnection(Edge connector, Collection children) {
        return false;
    }

    /**
	 * @generated
	 */
    protected void refreshSemantic() {
        List createdViews = new LinkedList();
        createdViews.addAll(refreshSemanticChildren());
        List createdConnectionViews = new LinkedList();
        createdConnectionViews.addAll(refreshSemanticConnections());
        createdConnectionViews.addAll(refreshConnections());
        if (createdViews.size() > 1) {
            DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host().getEditingDomain(), createdViews, host());
            executeCommand(new ICommandProxy(layoutCmd));
        }
        createdViews.addAll(createdConnectionViews);
        makeViewsImmutable(createdViews);
    }

    /**
	 * @generated
	 */
    private Diagram getDiagram() {
        return ((View) getHost().getModel()).getDiagram();
    }

    /**
	 * @generated
	 */
    private Collection refreshConnections() {
        Map domain2NotationMap = new HashMap();
        Collection linkDescriptors = collectAllLinks(getDiagram(), domain2NotationMap);
        Collection existingLinks = new LinkedList(getDiagram().getEdges());
        for (Iterator linksIterator = existingLinks.iterator(); linksIterator.hasNext(); ) {
            Edge nextDiagramLink = (Edge) linksIterator.next();
            int diagramLinkVisualID = TelcoblocksVisualIDRegistry.getVisualID(nextDiagramLink);
            if (diagramLinkVisualID == -1) {
                if (nextDiagramLink.getSource() != null && nextDiagramLink.getTarget() != null) {
                    linksIterator.remove();
                }
                continue;
            }
            EObject diagramLinkObject = nextDiagramLink.getElement();
            EObject diagramLinkSrc = nextDiagramLink.getSource().getElement();
            EObject diagramLinkDst = nextDiagramLink.getTarget().getElement();
            for (Iterator LinkDescriptorsIterator = linkDescriptors.iterator(); LinkDescriptorsIterator.hasNext(); ) {
                TelcoblocksLinkDescriptor nextLinkDescriptor = (TelcoblocksLinkDescriptor) LinkDescriptorsIterator.next();
                if (diagramLinkObject == nextLinkDescriptor.getModelElement() && diagramLinkSrc == nextLinkDescriptor.getSource() && diagramLinkDst == nextLinkDescriptor.getDestination() && diagramLinkVisualID == nextLinkDescriptor.getVisualID()) {
                    linksIterator.remove();
                    LinkDescriptorsIterator.remove();
                }
            }
        }
        deleteViews(existingLinks.iterator());
        return createConnections(linkDescriptors, domain2NotationMap);
    }

    /**
	 * @generated
	 */
    private Collection collectAllLinks(View view, Map domain2NotationMap) {
        if (!SERVICIOSEditPart.MODEL_ID.equals(TelcoblocksVisualIDRegistry.getModelID(view))) {
            return Collections.EMPTY_LIST;
        }
        Collection result = new LinkedList();
        switch(TelcoblocksVisualIDRegistry.getVisualID(view)) {
            case SERVICIOSEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getSERVICIOS_1000ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case ClickToDialEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getClickToDial_2001ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case LlamadasEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getLlamadas_2002ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case ServicioGenEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getServicioGen_2003ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case PersonalizacionEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getPersonalizacion_2004ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case IMEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getIM_2005ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case FacturacionEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getFacturacion_2006ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case WebEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getWeb_2007ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case DesvioEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getDesvio_2008ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case AnunciosEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getAnuncios_2009ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case TelefonoEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getTelefono_2010ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case EnlaceEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(TelcoblocksDiagramUpdater.getEnlace_4001ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
        }
        for (Iterator children = view.getChildren().iterator(); children.hasNext(); ) {
            result.addAll(collectAllLinks((View) children.next(), domain2NotationMap));
        }
        for (Iterator edges = view.getSourceEdges().iterator(); edges.hasNext(); ) {
            result.addAll(collectAllLinks((View) edges.next(), domain2NotationMap));
        }
        return result;
    }

    /**
	 * @generated
	 */
    private Collection createConnections(Collection linkDescriptors, Map domain2NotationMap) {
        List adapters = new LinkedList();
        for (Iterator linkDescriptorsIterator = linkDescriptors.iterator(); linkDescriptorsIterator.hasNext(); ) {
            final TelcoblocksLinkDescriptor nextLinkDescriptor = (TelcoblocksLinkDescriptor) linkDescriptorsIterator.next();
            EditPart sourceEditPart = getEditPart(nextLinkDescriptor.getSource(), domain2NotationMap);
            EditPart targetEditPart = getEditPart(nextLinkDescriptor.getDestination(), domain2NotationMap);
            if (sourceEditPart == null || targetEditPart == null) {
                continue;
            }
            CreateConnectionViewRequest.ConnectionViewDescriptor descriptor = new CreateConnectionViewRequest.ConnectionViewDescriptor(nextLinkDescriptor.getSemanticAdapter(), null, ViewUtil.APPEND, false, ((IGraphicalEditPart) getHost()).getDiagramPreferencesHint());
            CreateConnectionViewRequest ccr = new CreateConnectionViewRequest(descriptor);
            ccr.setType(RequestConstants.REQ_CONNECTION_START);
            ccr.setSourceEditPart(sourceEditPart);
            sourceEditPart.getCommand(ccr);
            ccr.setTargetEditPart(targetEditPart);
            ccr.setType(RequestConstants.REQ_CONNECTION_END);
            Command cmd = targetEditPart.getCommand(ccr);
            if (cmd != null && cmd.canExecute()) {
                executeCommand(cmd);
                IAdaptable viewAdapter = (IAdaptable) ccr.getNewObject();
                if (viewAdapter != null) {
                    adapters.add(viewAdapter);
                }
            }
        }
        return adapters;
    }

    /**
	 * @generated
	 */
    private EditPart getEditPart(EObject domainModelElement, Map domain2NotationMap) {
        View view = (View) domain2NotationMap.get(domainModelElement);
        if (view != null) {
            return (EditPart) getHost().getViewer().getEditPartRegistry().get(view);
        }
        return null;
    }
}
