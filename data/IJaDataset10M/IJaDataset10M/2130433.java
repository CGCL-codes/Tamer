package ch.hsr.orm.model.diagram.part;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.action.IDisposableAction;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.openarchitectureware.adapter.gmf2.runtime.OAWStatus;
import org.openarchitectureware.check.CheckFacade;
import org.openarchitectureware.expression.ExecutionContext;
import org.openarchitectureware.expression.ExecutionContextImpl;
import org.openarchitectureware.type.emf.EmfMetaModel;
import org.openarchitectureware.workflow.issues.Issue;
import org.openarchitectureware.workflow.issues.Issues;
import org.openarchitectureware.workflow.issues.IssuesImpl;
import ch.hsr.orm.model.ModelPackage;
import ch.hsr.orm.model.diagram.providers.OrmmetaMarkerNavigationProvider;
import ch.hsr.orm.model.diagram.providers.OrmmetaValidationProvider;

/**
 * @generated
 */
public class ValidateAction extends Action implements IDisposableAction {

    /**
	 * @generated
	 */
    public static final String VALIDATE_ACTION_KEY = "validateAction";

    /**
	 * @generated
	 */
    private IWorkbenchPartDescriptor workbenchPartDescriptor;

    /**
	 * @generated
	 */
    private static ExecutionContextImpl ctx;

    /**
	 * @generated
	 */
    public ValidateAction(IWorkbenchPartDescriptor workbenchPartDescriptor) {
        setId(VALIDATE_ACTION_KEY);
        setText(Messages.ValidateActionMessage);
        this.workbenchPartDescriptor = workbenchPartDescriptor;
    }

    /**
	 * @generated
	 */
    public void run() {
        IWorkbenchPart workbenchPart = workbenchPartDescriptor.getPartPage().getActivePart();
        if (workbenchPart instanceof IDiagramWorkbenchPart) {
            final IDiagramWorkbenchPart part = (IDiagramWorkbenchPart) workbenchPart;
            try {
                new WorkspaceModifyDelegatingOperation(new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) throws InterruptedException, InvocationTargetException {
                        runValidation(part.getDiagramEditPart(), part.getDiagram());
                    }
                }).run(new NullProgressMonitor());
            } catch (Exception e) {
                OrmmetaDiagramEditorPlugin.getInstance().logError("Validation action failed", e);
            }
        }
    }

    /**
	 * @generated
	 */
    public static void runValidation(View view) {
        try {
            if (OrmmetaDiagramEditorUtil.openDiagram(view.eResource())) {
                IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                if (editorPart instanceof IDiagramWorkbenchPart) {
                    runValidation(((IDiagramWorkbenchPart) editorPart).getDiagramEditPart(), view);
                } else {
                    runNonUIValidation(view);
                }
            }
        } catch (Exception e) {
            OrmmetaDiagramEditorPlugin.getInstance().logError("Validation action failed", e);
        }
    }

    /**
	 * @generated
	 */
    public static void runNonUIValidation(View view) {
        DiagramEditPart diagramEditPart = OffscreenEditPartFactory.getInstance().createDiagramEditPart(view.getDiagram());
        runValidation(diagramEditPart, view);
    }

    /**
	 * @generated
	 */
    public static void runValidation(DiagramEditPart diagramEditPart, View view) {
        final DiagramEditPart fpart = diagramEditPart;
        final View fview = view;
        OrmmetaValidationProvider.runWithConstraints(view, new Runnable() {

            public void run() {
                validate(fpart, fview);
            }
        });
    }

    /**
	 * @generated
	 */
    private static Diagnostic runEMFValidator(View target) {
        if (target.isSetElement() && target.getElement() != null) {
            return new Diagnostician() {

                public String getObjectLabel(EObject eObject) {
                    return EMFCoreUtil.getQualifiedName(eObject, true);
                }
            }.validate(target.getElement());
        }
        return Diagnostic.OK_INSTANCE;
    }

    /**
	 * @generated
	 */
    private static void validate(DiagramEditPart diagramEditPart, View view) {
        IFile target = view.eResource() != null ? WorkspaceSynchronizer.getFile(view.eResource()) : null;
        if (target != null) {
            OrmmetaMarkerNavigationProvider.deleteMarkers(target);
        }
        Diagnostic diagnostic = runEMFValidator(view);
        createMarkers(target, diagnostic, diagramEditPart);
        IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
        validator.setIncludeLiveConstraints(true);
        if (view.isSetElement() && view.getElement() != null) {
            IStatus status = validator.validate(view.getElement());
            createMarkers(target, status, diagramEditPart);
        }
        validateOAWChecks(view, diagramEditPart);
    }

    /**
	 * @generated
	 */
    private static void validateOAWChecks(View target, DiagramEditPart diagramEditPart) {
        try {
            Issues issues = new IssuesImpl();
            EObject targetElement = target.getElement();
            List<EObject> l = new ArrayList<EObject>();
            TreeIterator<EObject> allContents = targetElement.eAllContents();
            while (allContents.hasNext()) {
                l.add((EObject) allContents.next());
            }
            l.add(targetElement);
            InputStream stream = null;
            String chkFile = null;
            chkFile = "//checks/naming.chk";
            stream = Platform.getBundle("ch.hsr.orm").getResource(chkFile).openStream();
            CheckFacade.checkAll(chkFile, stream, l, getExecutionContext(targetElement), issues);
            stream.close();
            chkFile = "//checks/persistable.chk";
            stream = Platform.getBundle("ch.hsr.orm").getResource(chkFile).openStream();
            CheckFacade.checkAll(chkFile, stream, l, getExecutionContext(targetElement), issues);
            stream.close();
            chkFile = "//checks/relation.chk";
            stream = Platform.getBundle("ch.hsr.orm").getResource(chkFile).openStream();
            CheckFacade.checkAll(chkFile, stream, l, getExecutionContext(targetElement), issues);
            stream.close();
            chkFile = "//checks/inheritance.chk";
            stream = Platform.getBundle("ch.hsr.orm").getResource(chkFile).openStream();
            CheckFacade.checkAll(chkFile, stream, l, getExecutionContext(targetElement), issues);
            stream.close();
            IFile targetFile = target.eResource() != null ? WorkspaceSynchronizer.getFile(target.eResource()) : null;
            if (issues.hasErrors()) {
                for (Issue i : issues.getErrors()) {
                    Status error = new OAWStatus((EObject) i.getElement(), IStatus.ERROR, OrmmetaDiagramEditorPlugin.ID, i.getMessage());
                    createMarkers(targetFile, error, diagramEditPart);
                }
            }
            if (issues.getWarnings().length != 0) {
                for (Issue i : issues.getWarnings()) {
                    Status error = new OAWStatus((EObject) i.getElement(), IStatus.WARNING, OrmmetaDiagramEditorPlugin.ID, i.getMessage());
                    createMarkers(targetFile, error, diagramEditPart);
                }
            }
        } catch (Exception e) {
            OrmmetaDiagramEditorPlugin.getInstance().logError(e.getMessage(), e);
        }
    }

    /**
	 * @generated
	 */
    private static ExecutionContext getExecutionContext(EObject target) {
        if (ctx == null) {
            ctx = new ExecutionContextImpl();
            ctx.registerMetaModel(new EmfMetaModel(ModelPackage.eINSTANCE));
        }
        return ctx;
    }

    /**
	 * @generated
	 */
    private static void createMarkers(IFile target, IStatus validationStatus, DiagramEditPart diagramEditPart) {
        if (validationStatus.isOK()) {
            return;
        }
        final IStatus rootStatus = validationStatus;
        List allStatuses = new ArrayList();
        OrmmetaDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new OrmmetaDiagramEditorUtil.LazyElement2ViewMap(diagramEditPart.getDiagramView(), collectTargetElements(rootStatus, new HashSet(), allStatuses));
        for (Iterator it = allStatuses.iterator(); it.hasNext(); ) {
            IConstraintStatus nextStatus = (IConstraintStatus) it.next();
            View view = OrmmetaDiagramEditorUtil.findView(diagramEditPart, nextStatus.getTarget(), element2ViewMap);
            addMarker(diagramEditPart.getViewer(), target, view.eResource().getURIFragment(view), EMFCoreUtil.getQualifiedName(nextStatus.getTarget(), true), nextStatus.getMessage(), nextStatus.getSeverity());
        }
    }

    /**
	 * @generated
	 */
    private static void createMarkers(IFile target, Diagnostic emfValidationStatus, DiagramEditPart diagramEditPart) {
        if (emfValidationStatus.getSeverity() == Diagnostic.OK) {
            return;
        }
        final Diagnostic rootStatus = emfValidationStatus;
        List allDiagnostics = new ArrayList();
        OrmmetaDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new OrmmetaDiagramEditorUtil.LazyElement2ViewMap(diagramEditPart.getDiagramView(), collectTargetElements(rootStatus, new HashSet(), allDiagnostics));
        for (Iterator it = emfValidationStatus.getChildren().iterator(); it.hasNext(); ) {
            Diagnostic nextDiagnostic = (Diagnostic) it.next();
            List data = nextDiagnostic.getData();
            if (data != null && !data.isEmpty() && data.get(0) instanceof EObject) {
                EObject element = (EObject) data.get(0);
                View view = OrmmetaDiagramEditorUtil.findView(diagramEditPart, element, element2ViewMap);
                addMarker(diagramEditPart.getViewer(), target, view.eResource().getURIFragment(view), EMFCoreUtil.getQualifiedName(element, true), nextDiagnostic.getMessage(), diagnosticToStatusSeverity(nextDiagnostic.getSeverity()));
            }
        }
    }

    /**
	 * @generated
	 */
    private static void addMarker(EditPartViewer viewer, IFile target, String elementId, String location, String message, int statusSeverity) {
        if (target == null) {
            return;
        }
        OrmmetaMarkerNavigationProvider.addMarker(target, elementId, location, message, statusSeverity);
    }

    /**
	 * @generated
	 */
    private static int diagnosticToStatusSeverity(int diagnosticSeverity) {
        if (diagnosticSeverity == Diagnostic.OK) {
            return IStatus.OK;
        } else if (diagnosticSeverity == Diagnostic.INFO) {
            return IStatus.INFO;
        } else if (diagnosticSeverity == Diagnostic.WARNING) {
            return IStatus.WARNING;
        } else if (diagnosticSeverity == Diagnostic.ERROR || diagnosticSeverity == Diagnostic.CANCEL) {
            return IStatus.ERROR;
        }
        return IStatus.INFO;
    }

    /**
	 * @generated
	 */
    private static Set collectTargetElements(IStatus status, Set targetElementCollector, List allConstraintStatuses) {
        if (status instanceof IConstraintStatus) {
            targetElementCollector.add(((IConstraintStatus) status).getTarget());
            allConstraintStatuses.add(status);
        }
        if (status.isMultiStatus()) {
            IStatus[] children = status.getChildren();
            for (int i = 0; i < children.length; i++) {
                collectTargetElements(children[i], targetElementCollector, allConstraintStatuses);
            }
        }
        return targetElementCollector;
    }

    /**
	 * @generated
	 */
    private static Set collectTargetElements(Diagnostic diagnostic, Set targetElementCollector, List allDiagnostics) {
        List data = diagnostic.getData();
        EObject target = null;
        if (data != null && !data.isEmpty() && data.get(0) instanceof EObject) {
            target = (EObject) data.get(0);
            targetElementCollector.add(target);
            allDiagnostics.add(diagnostic);
        }
        if (diagnostic.getChildren() != null && !diagnostic.getChildren().isEmpty()) {
            for (Iterator it = diagnostic.getChildren().iterator(); it.hasNext(); ) {
                collectTargetElements((Diagnostic) it.next(), targetElementCollector, allDiagnostics);
            }
        }
        return targetElementCollector;
    }

    /**
	 * @generated
	 */
    private boolean isDisposed = false;

    /**
	 * @generated
	 */
    private Job j;

    /**
	 * @generated
	 */
    public void init() {
        j = new ValidateJob("executing oAW Checks", this);
        j.schedule();
    }

    /**
	 * @generated
	 */
    public void dispose() {
        j.cancel();
        isDisposed = true;
    }

    /**
	 * @generated
	 */
    public boolean isDisposed() {
        return isDisposed;
    }

    /**
	 * @generated
	 */
    private final class ValidateJob extends Job {

        /**
		 * @generated
		 */
        private final ValidateAction validateAction;

        /**
		 * @generated
		 */
        private ValidateJob(String name, ValidateAction validateAction) {
            super(name);
            this.validateAction = validateAction;
        }

        /**
		 * @generated
		 */
        protected IStatus run(IProgressMonitor monitor) {
            if (!monitor.isCanceled()) {
                validateAction.run();
                schedule(1000);
            }
            return Status.OK_STATUS;
        }
    }
}
