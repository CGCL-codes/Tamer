package org.codescale.eDependency.diagram.part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codescale.eDependency.Bundle;
import org.codescale.eDependency.diagram.edit.parts.Bundle2EditPart;
import org.codescale.eDependency.diagram.edit.parts.BundleEditPart;
import org.codescale.eDependency.diagram.edit.parts.BundlePackages2EditPart;
import org.codescale.eDependency.diagram.edit.parts.BundlePackagesEditPart;
import org.codescale.eDependency.diagram.edit.parts.FeatureEditPart;
import org.codescale.eDependency.diagram.edit.parts.FeaturePluginsEditPart;
import org.codescale.eDependency.diagram.edit.parts.PluginEditPart;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @generated
 */
public class EDependencyDiagramEditorUtil {

    /**
     * @generated
     */
    public static class LazyElement2ViewMap {

        /**
	 * @generated
	 */
        static Map buildElement2ViewMap(View parentView, Map element2ViewMap, Set elements) {
            if (elements.size() == element2ViewMap.size()) {
                return element2ViewMap;
            }
            if (parentView.isSetElement() && !element2ViewMap.containsKey(parentView.getElement()) && elements.contains(parentView.getElement())) {
                element2ViewMap.put(parentView.getElement(), parentView);
                if (elements.size() == element2ViewMap.size()) {
                    return element2ViewMap;
                }
            }
            for (Iterator it = parentView.getChildren().iterator(); it.hasNext(); ) {
                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
                if (elements.size() == element2ViewMap.size()) {
                    return element2ViewMap;
                }
            }
            for (Iterator it = parentView.getSourceEdges().iterator(); it.hasNext(); ) {
                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
                if (elements.size() == element2ViewMap.size()) {
                    return element2ViewMap;
                }
            }
            for (Iterator it = parentView.getSourceEdges().iterator(); it.hasNext(); ) {
                buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
                if (elements.size() == element2ViewMap.size()) {
                    return element2ViewMap;
                }
            }
            return element2ViewMap;
        }

        /**
	 * @generated
	 */
        public final List editPartTmpHolder = new ArrayList();

        /**
	 * @generated
	 */
        private Map element2ViewMap;

        /**
	 * @generated
	 */
        private final Set elementSet;

        /**
	 * @generated
	 */
        private final View scope;

        /**
	 * @generated
	 */
        public LazyElement2ViewMap(View scope, Set elements) {
            this.scope = scope;
            elementSet = elements;
        }

        /**
	 * @generated
	 */
        public final Map getElement2ViewMap() {
            if (element2ViewMap == null) {
                element2ViewMap = new HashMap();
                for (Iterator it = elementSet.iterator(); it.hasNext(); ) {
                    EObject element = (EObject) it.next();
                    if (element instanceof View) {
                        View view = (View) element;
                        if (view.getDiagram() == scope.getDiagram()) {
                            element2ViewMap.put(element, element);
                        }
                    }
                }
                buildElement2ViewMap(scope, element2ViewMap, elementSet);
            }
            return element2ViewMap;
        }
    }

    /**
     * Store model element in the resource. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    private static void attachModelToResource(org.codescale.eDependency.Workspace model, Resource resource) {
        resource.getContents().add(model);
    }

    public static Command changeFillColor(IGraphicalEditPart editPart, RGB rgb) {
        ChangePropertyValueRequest request = new ChangePropertyValueRequest(DiagramUIMessages.PropertyDescriptorFactory_FillColor, Properties.ID_FILLCOLOR, FigureUtilities.RGBToInteger(rgb));
        return editPart.getCommand(request);
    }

    public static Command changeLineColor(IGraphicalEditPart editPart, RGB rgb) {
        ChangePropertyValueRequest request = new ChangePropertyValueRequest(DiagramUIMessages.PropertyDescriptorFactory_LineColor, Properties.ID_LINECOLOR, FigureUtilities.RGBToInteger(rgb));
        return editPart.getCommand(request);
    }

    public static Command collapse(IGraphicalEditPart editPart) {
        ChangePropertyValueRequest request = new ChangePropertyValueRequest(DiagramUIMessages.PropertyDescriptorFactory_CollapseCompartment, Properties.ID_COLLAPSED, true);
        return editPart.getCommand(request);
    }

    /**
     * This method should be called within a workspace modify operation since it
     * creates resources.
     * 
     * @generated
     */
    public static Resource createDiagram(URI diagramURI, IProgressMonitor progressMonitor) {
        TransactionalEditingDomain editingDomain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();
        progressMonitor.beginTask(org.codescale.eDependency.diagram.part.Messages.EDependencyDiagramEditorUtil_CreateDiagramProgressTask, 3);
        final Resource diagramResource = editingDomain.getResourceSet().createResource(diagramURI);
        final String diagramName = diagramURI.lastSegment();
        AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain, org.codescale.eDependency.diagram.part.Messages.EDependencyDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {

            @Override
            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                org.codescale.eDependency.Workspace model = createInitialModel();
                attachModelToResource(model, diagramResource);
                Diagram diagram = ViewService.createDiagram(model, org.codescale.eDependency.diagram.edit.parts.WorkspaceEditPart.MODEL_ID, org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
                if (diagram != null) {
                    diagramResource.getContents().add(diagram);
                    diagram.setName(diagramName);
                    diagram.setElement(model);
                }
                try {
                    diagramResource.save(org.codescale.eDependency.diagram.part.EDependencyDiagramEditorUtil.getSaveOptions());
                } catch (IOException e) {
                    org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);
                }
                return CommandResult.newOKCommandResult();
            }
        };
        try {
            OperationHistoryFactory.getOperationHistory().execute(command, new SubProgressMonitor(progressMonitor, 1), null);
        } catch (ExecutionException e) {
            org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);
        }
        setCharset(WorkspaceSynchronizer.getFile(diagramResource));
        return diagramResource;
    }

    /**
     * This method should be called within a workspace modify operation since it
     * creates resources.
     * 
     * @generated NOT
     */
    public static Resource createDiagram(URI diagramURI, final org.codescale.eDependency.Workspace workspaceModel, IProgressMonitor progressMonitor) {
        TransactionalEditingDomain editingDomain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();
        progressMonitor.beginTask(org.codescale.eDependency.diagram.part.Messages.EDependencyDiagramEditorUtil_CreateDiagramProgressTask, 3);
        final Resource diagramResource = editingDomain.getResourceSet().createResource(diagramURI);
        final String diagramName = diagramURI.lastSegment();
        AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain, org.codescale.eDependency.diagram.part.Messages.EDependencyDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {

            @Override
            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                org.codescale.eDependency.Workspace model = null;
                if (workspaceModel == null) {
                    model = createInitialModel();
                } else {
                    model = workspaceModel;
                }
                attachModelToResource(model, diagramResource);
                Diagram diagram = ViewService.createDiagram(model, org.codescale.eDependency.diagram.edit.parts.WorkspaceEditPart.MODEL_ID, org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
                if (diagram != null) {
                    diagramResource.getContents().add(diagram);
                    diagram.setName(diagramName);
                    diagram.setElement(model);
                }
                try {
                    diagramResource.save(org.codescale.eDependency.diagram.part.EDependencyDiagramEditorUtil.getSaveOptions());
                } catch (IOException e) {
                    org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);
                }
                return CommandResult.newOKCommandResult();
            }
        };
        try {
            OperationHistoryFactory.getOperationHistory().execute(command, new SubProgressMonitor(progressMonitor, 1), null);
        } catch (ExecutionException e) {
            org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);
        }
        setCharset(WorkspaceSynchronizer.getFile(diagramResource));
        return diagramResource;
    }

    /**
     * Create a new instance of domain element associated with canvas. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static org.codescale.eDependency.Workspace createInitialModel() {
        return org.codescale.eDependency.EDependencyFactory.eINSTANCE.createWorkspace();
    }

    /**
     * @generated
     */
    private static int findElementsInDiagramByID(DiagramEditPart diagramPart, EObject element, List editPartCollector) {
        IDiagramGraphicalViewer viewer = (IDiagramGraphicalViewer) diagramPart.getViewer();
        final int intialNumOfEditParts = editPartCollector.size();
        if (element instanceof View) {
            EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(element);
            if (editPart != null) {
                editPartCollector.add(editPart);
                return 1;
            }
        }
        String elementID = EMFCoreUtil.getProxyID(element);
        List associatedParts = viewer.findEditPartsForElement(elementID, IGraphicalEditPart.class);
        for (Iterator editPartIt = associatedParts.iterator(); editPartIt.hasNext(); ) {
            EditPart nextPart = (EditPart) editPartIt.next();
            EditPart parentPart = nextPart.getParent();
            while ((parentPart != null) && !associatedParts.contains(parentPart)) {
                parentPart = parentPart.getParent();
            }
            if (parentPart == null) {
                editPartCollector.add(nextPart);
            }
        }
        if (intialNumOfEditParts == editPartCollector.size()) {
            if (!associatedParts.isEmpty()) {
                editPartCollector.add(associatedParts.iterator().next());
            } else {
                if (element.eContainer() != null) {
                    return findElementsInDiagramByID(diagramPart, element.eContainer(), editPartCollector);
                }
            }
        }
        return editPartCollector.size() - intialNumOfEditParts;
    }

    /**
     * @generated
     */
    public static View findView(DiagramEditPart diagramEditPart, EObject targetElement, LazyElement2ViewMap lazyElement2ViewMap) {
        boolean hasStructuralURI = false;
        if (targetElement.eResource() instanceof XMLResource) {
            hasStructuralURI = ((XMLResource) targetElement.eResource()).getID(targetElement) == null;
        }
        View view = null;
        if (hasStructuralURI && !lazyElement2ViewMap.getElement2ViewMap().isEmpty()) {
            view = (View) lazyElement2ViewMap.getElement2ViewMap().get(targetElement);
        } else if (findElementsInDiagramByID(diagramEditPart, targetElement, lazyElement2ViewMap.editPartTmpHolder) > 0) {
            EditPart editPart = (EditPart) lazyElement2ViewMap.editPartTmpHolder.get(0);
            lazyElement2ViewMap.editPartTmpHolder.clear();
            view = editPart.getModel() instanceof View ? (View) editPart.getModel() : null;
        }
        return (view == null) ? diagramEditPart.getDiagramView() : view;
    }

    /**
     * @generated
     */
    public static Map getSaveOptions() {
        Map saveOptions = new HashMap();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8");
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
    }

    /**
     * @generated
     */
    public static String getUniqueFileName(IPath containerFullPath, String fileName, String extension) {
        if (containerFullPath == null) {
            containerFullPath = new Path("");
        }
        if ((fileName == null) || (fileName.trim().length() == 0)) {
            fileName = "default";
        }
        IPath filePath = containerFullPath.append(fileName);
        if ((extension != null) && !extension.equals(filePath.getFileExtension())) {
            filePath = filePath.addFileExtension(extension);
        }
        extension = filePath.getFileExtension();
        fileName = filePath.removeFileExtension().lastSegment();
        int i = 1;
        while (ResourcesPlugin.getWorkspace().getRoot().exists(filePath)) {
            i++;
            filePath = containerFullPath.append(fileName + i);
            if (extension != null) {
                filePath = filePath.addFileExtension(extension);
            }
        }
        return filePath.lastSegment();
    }

    /**
     * @generated NOT
     */
    public static IEditorPart openDiagram(Resource diagram) throws PartInitException {
        String path = diagram.getURI().toPlatformString(true);
        IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
        if (workspaceResource instanceof IFile) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IEditorPart openEditor = page.openEditor(new FileEditorInput((IFile) workspaceResource), org.codescale.eDependency.diagram.part.EDependencyDiagramEditor.ID);
            return openEditor;
        }
        return null;
    }

    public static void prepareVisual(IEditorPart editorPart) {
        if (editorPart == null) {
            return;
        }
        EDependencyDiagramEditor eDependencyDiagramEditor = (EDependencyDiagramEditor) editorPart;
        List<Command> commands = new ArrayList<Command>();
        List editParts = eDependencyDiagramEditor.getDiagramEditPart().getChildren();
        for (Object editPart : editParts) {
            if (editPart instanceof FeatureEditPart) {
                FeatureEditPart featureEditPart = (FeatureEditPart) editPart;
                IGraphicalEditPart featurePluginsEditPart = featureEditPart.getChildBySemanticHint(Integer.toString(FeaturePluginsEditPart.VISUAL_ID));
                commands.addAll(prepareVisualFeature(featurePluginsEditPart));
            }
            if (editPart instanceof BundleEditPart) {
                IGraphicalEditPart packagesEditPart = ((BundleEditPart) editPart).getChildBySemanticHint(Integer.toString(BundlePackagesEditPart.VISUAL_ID));
                commands.add(collapse(packagesEditPart));
            }
        }
        for (Command command : commands) {
            if (command.canExecute()) {
                ((DiagramEditDomain) eDependencyDiagramEditor.getDiagramEditDomain()).getCommandStack().execute(command);
            }
        }
    }

    private static Collection<? extends Command> prepareVisualFeature(IGraphicalEditPart featurePluginsEditPart) {
        List<Command> commands = new ArrayList<Command>();
        boolean containsBundleWithBuddyPolicy = false;
        for (Object obj : featurePluginsEditPart.getChildren()) {
            IGraphicalEditPart bundleEditPart = null;
            if ((obj instanceof Bundle2EditPart) || (obj instanceof PluginEditPart)) {
                bundleEditPart = (IGraphicalEditPart) obj;
            }
            if ((bundleEditPart != null) && (bundleEditPart.getModel() instanceof View)) {
                View view = (View) bundleEditPart.getModel();
                if (view.getElement() instanceof Bundle) {
                    Bundle eBundle = (Bundle) view.getElement();
                    if (eBundle.getEclipseBuddyPolicy() != null) {
                        commands.add(changeFillColor(bundleEditPart, new RGB(227, 164, 156)));
                        containsBundleWithBuddyPolicy |= true;
                    }
                    IGraphicalEditPart packagesEditPart = bundleEditPart.getChildBySemanticHint(Integer.toString(BundlePackages2EditPart.VISUAL_ID));
                    commands.add(collapse(packagesEditPart));
                }
            }
        }
        if (!containsBundleWithBuddyPolicy) {
            commands.add(collapse(featurePluginsEditPart));
        }
        return commands;
    }

    /**
     * Runs the wizard in a dialog.
     * 
     * @generated
     */
    public static void runWizard(Shell shell, Wizard wizard, String settingsKey) {
        IDialogSettings pluginDialogSettings = org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().getDialogSettings();
        IDialogSettings wizardDialogSettings = pluginDialogSettings.getSection(settingsKey);
        if (wizardDialogSettings == null) {
            wizardDialogSettings = pluginDialogSettings.addNewSection(settingsKey);
        }
        wizard.setDialogSettings(wizardDialogSettings);
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        dialog.getShell().setSize(Math.max(500, dialog.getShell().getSize().x), 500);
        dialog.open();
    }

    /**
     * @generated
     */
    public static void selectElementsInDiagram(IDiagramWorkbenchPart diagramPart, List editParts) {
        diagramPart.getDiagramGraphicalViewer().deselectAll();
        EditPart firstPrimary = null;
        for (Iterator it = editParts.iterator(); it.hasNext(); ) {
            EditPart nextPart = (EditPart) it.next();
            diagramPart.getDiagramGraphicalViewer().appendSelection(nextPart);
            if ((firstPrimary == null) && (nextPart instanceof IPrimaryEditPart)) {
                firstPrimary = nextPart;
            }
        }
        if (!editParts.isEmpty()) {
            diagramPart.getDiagramGraphicalViewer().reveal(firstPrimary != null ? firstPrimary : (EditPart) editParts.get(0));
        }
    }

    /**
     * @generated
     */
    public static void setCharset(IFile file) {
        if (file == null) {
            return;
        }
        try {
            file.setCharset("UTF-8", new NullProgressMonitor());
        } catch (CoreException e) {
            org.codescale.eDependency.diagram.part.EDependencyDiagramEditorPlugin.getInstance().logError("Unable to set charset for file " + file.getFullPath(), e);
        }
    }
}
