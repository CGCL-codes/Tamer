package de.mpiwg.vspace.generation.itext.core.services;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import de.mpiwg.vspace.common.constants.VSpaceExtensions;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.diagram.part.ExhibitionDiagramEditor;
import de.mpiwg.vspace.diagram.patch.MyDiagramDocumentEditor;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.generation.itext.core.Activator;
import de.mpiwg.vspace.generation.itext.core.Constants;
import de.mpiwg.vspace.metamodel.Exhibition;
import de.mpiwg.vspace.util.PropertyHandler;
import de.mpiwg.vspace.util.PropertyHandlerRegistry;

public abstract class AbstractGenerationService {

    private MyDiagramDocumentEditor dde = null;

    private Resource resource = null;

    private Resource diagramResource;

    public AbstractGenerationService() {
        IEditorPart activeDde = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        TransactionalEditingDomain editingDomain = ProjectObservable.INSTANCE.getEditingDomain();
        if (editingDomain == null) return;
        ResourceSet resourceSet = editingDomain.getResourceSet();
        if (resourceSet == null) return;
        diagramResource = null;
        if (resourceSet.getResources() != null) {
            for (Resource res : resourceSet.getResources()) {
                if (res.getURI().fileExtension().equals(VSpaceExtensions.DIAGRAM_EXTENSION)) diagramResource = res;
            }
        }
        if (diagramResource == null) return;
        if (activeDde instanceof ExhibitionDiagramEditor) {
            dde = (ExhibitionDiagramEditor) activeDde;
        } else {
            if (!openEditor(PlatformUI.getWorkbench(), diagramResource.getURI())) return;
        }
        TransactionalEditingDomain editingDomain2 = dde.getEditingDomain();
        ResourceSet resourceSet2 = editingDomain2.getResourceSet();
        if (resourceSet2.getResources() != null) {
            for (Resource res : resourceSet2.getResources()) {
                if (res.getURI().fileExtension().equals(VSpaceExtensions.MODEL_EXTENSION)) resource = res;
            }
        }
    }

    public void runGeneration() {
        PropertyHandler propHandler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILENAME);
        if (resource == null) {
            MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OK | SWT.ICON_ERROR);
            messageBox.setText(propHandler.getProperty("_msgbox_title_diagram_closed"));
            messageBox.setMessage(propHandler.getProperty("_msgbox_text_diagram_closed"));
            messageBox.open();
            return;
        }
        EList<EObject> resourceChildren = resource.getContents();
        Exhibition exhibition = null;
        if (resourceChildren != null) {
            for (Object child : resourceChildren) {
                if (child instanceof Exhibition) exhibition = (Exhibition) child;
            }
        }
        if (exhibition == null) return;
        final Exhibition exhibitionFinal = exhibition;
        EObject eo = diagramResource.getContents().get(0);
        if (!(eo instanceof Diagram)) return;
        final Diagram diagram = (Diagram) eo;
        ITextGenerator generator = getGenerator();
        generator.generate(exhibitionFinal, diagram);
    }

    protected abstract ITextGenerator getGenerator();

    private boolean openEditor(IWorkbench workbench, URI fileURI) {
        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = workbenchWindow.getActivePage();
        IEditorDescriptor editorDescriptor = workbench.getEditorRegistry().getDefaultEditor(fileURI.toFileString());
        if (editorDescriptor == null) {
            return false;
        } else {
            dde = (MyDiagramDocumentEditor) page.findEditor(new URIEditorInput(fileURI));
            final IDocumentProvider provider = dde.getDocumentProvider();
            final IEditorInput input = dde.getEditorInput();
            if (!provider.isDeleted(input)) {
                try {
                    provider.synchronize(input);
                } catch (CoreException e) {
                    ExceptionHandlingService.INSTANCE.handleException(e);
                }
            } else return false;
        }
        return true;
    }
}
