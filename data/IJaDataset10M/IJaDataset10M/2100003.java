package cescs.cescsmodel.diagram.edit.policies;

import java.io.IOException;
import java.util.Iterator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.HintedDiagramLinkStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import cescs.cescsmodel.diagram.edit.parts.ModelRootEditPart;
import cescs.cescsmodel.diagram.part.CescsmodelDiagramEditor;
import cescs.cescsmodel.diagram.part.CescsmodelDiagramEditorPlugin;
import cescs.cescsmodel.diagram.part.CescsmodelDiagramEditorUtil;
import cescs.cescsmodel.diagram.part.Messages;

/**
 * @generated
 */
public class OpenDiagramEditPolicy extends OpenEditPolicy {

    /**
	 * @generated
	 */
    protected Command getOpenCommand(Request request) {
        EditPart targetEditPart = getTargetEditPart(request);
        if (false == targetEditPart.getModel() instanceof View) {
            return null;
        }
        View view = (View) targetEditPart.getModel();
        Style link = view.getStyle(NotationPackage.eINSTANCE.getHintedDiagramLinkStyle());
        if (false == link instanceof HintedDiagramLinkStyle) {
            return null;
        }
        return new ICommandProxy(new OpenDiagramCommand((HintedDiagramLinkStyle) link));
    }

    /**
	 * @generated
	 */
    private static class OpenDiagramCommand extends AbstractTransactionalCommand {

        /**
		 * @generated
		 */
        private final HintedDiagramLinkStyle diagramFacet;

        /**
		 * @generated
		 */
        OpenDiagramCommand(HintedDiagramLinkStyle linkStyle) {
            super(TransactionUtil.getEditingDomain(linkStyle), Messages.CommandName_OpenDiagram, null);
            diagramFacet = linkStyle;
        }

        /**
		 * @generated
		 */
        protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
            try {
                Diagram diagram = getDiagramToOpen();
                if (diagram == null) {
                    diagram = intializeNewDiagram();
                }
                URI uri = EcoreUtil.getURI(diagram);
                String editorName = uri.lastSegment() + "#" + diagram.eResource().getContents().indexOf(diagram);
                IEditorInput editorInput = new URIEditorInput(uri, editorName);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                page.openEditor(editorInput, getEditorID());
                return CommandResult.newOKCommandResult();
            } catch (Exception ex) {
                throw new ExecutionException("Can't open diagram", ex);
            }
        }

        /**
		 * @generated
		 */
        protected Diagram getDiagramToOpen() {
            return diagramFacet.getDiagramLink();
        }

        /**
		 * @generated
		 */
        protected Diagram intializeNewDiagram() throws ExecutionException {
            Diagram d = ViewService.createDiagram(getDiagramDomainElement(), getDiagramKind(), getPreferencesHint());
            if (d == null) {
                throw new ExecutionException("Can't create diagram of '" + getDiagramKind() + "' kind");
            }
            diagramFacet.setDiagramLink(d);
            assert diagramFacet.eResource() != null;
            diagramFacet.eResource().getContents().add(d);
            EObject container = diagramFacet.eContainer();
            while (container instanceof View) {
                ((View) container).persist();
                container = container.eContainer();
            }
            try {
                for (Iterator it = diagramFacet.eResource().getResourceSet().getResources().iterator(); it.hasNext(); ) {
                    Resource nextResource = (Resource) it.next();
                    if (nextResource.isLoaded() && !getEditingDomain().isReadOnly(nextResource)) {
                        nextResource.save(CescsmodelDiagramEditorUtil.getSaveOptions());
                    }
                }
            } catch (IOException ex) {
                throw new ExecutionException("Can't create diagram of '" + getDiagramKind() + "' kind", ex);
            }
            return d;
        }

        /**
		 * @generated
		 */
        protected EObject getDiagramDomainElement() {
            return ((View) diagramFacet.eContainer()).getElement();
        }

        /**
		 * @generated
		 */
        protected PreferencesHint getPreferencesHint() {
            return CescsmodelDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
        }

        /**
		 * @generated
		 */
        protected String getDiagramKind() {
            return ModelRootEditPart.MODEL_ID;
        }

        /**
		 * @generated
		 */
        protected String getEditorID() {
            return CescsmodelDiagramEditor.ID;
        }
    }
}
