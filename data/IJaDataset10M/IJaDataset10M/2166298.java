package com.jeevaneo.naja.timeentries.popup.actions;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import com.jeevaneo.naja.Imputation;
import com.jeevaneo.naja.NajaFactory;
import com.jeevaneo.naja.NajaPackage;
import com.jeevaneo.naja.Person;
import com.jeevaneo.naja.Planification;
import com.jeevaneo.naja.Task;
import com.jeevaneo.naja.timeentries.TimeEntries;
import com.jeevaneo.naja.timeentries.TimeEntry;
import com.jeevaneo.naja.timeentries.TimeentriesPackage;

public class GenerateImputationsAction implements IObjectActionDelegate {

    private StructuredSelection selection = null;

    private EditingDomain editingDomain;

    private IWorkbenchPart part;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetEditor) {
        if (!(targetEditor instanceof IEditingDomainProvider)) {
            this.editingDomain = null;
            return;
        }
        this.editingDomain = ((IEditingDomainProvider) targetEditor).getEditingDomain();
        this.part = targetEditor;
    }

    @Override
    public void run(IAction action) {
        final boolean inferPlanifications = MessageDialog.openQuestion(part.getSite().getShell(), "Naja Time Entries", "Infer planifications?");
        final Set<TimeEntry> tys = new HashSet<TimeEntry>();
        for (Object o : selection.toList()) {
            if (o instanceof TimeEntries) {
                TimeEntries ties = (TimeEntries) o;
                for (TimeEntry ty : ties.getEntries()) {
                    tys.add(ty);
                }
            } else if (o instanceof TimeEntry) {
                tys.add((TimeEntry) o);
            }
        }
        Job job = new Job("Generate impuations") {

            @Override
            public IStatus run(IProgressMonitor monitor) {
                long start = System.currentTimeMillis();
                monitor.beginTask("Time entries - generate imputations", tys.size() * 1000);
                Set<Imputation> planificationNotFounds = new HashSet<Imputation>();
                for (TimeEntry ty : tys) {
                    if (monitor.isCanceled()) {
                        break;
                    }
                    recompute(ty, inferPlanifications, new SubProgressMonitor(monitor, 1000));
                    if (inferPlanifications && ty.getImputation() != null && ty.getImputation().getPlanification() == null) {
                        planificationNotFounds.add(ty.getImputation());
                    }
                    monitor.worked(1000);
                }
                monitor.done();
                for (Imputation imputation : planificationNotFounds) {
                    System.out.println("No planif found : " + imputation);
                    URI uri = EcoreUtil.getURI(imputation);
                    IFile file = deduceFile(uri);
                    try {
                        file.deleteMarkers("com.jeevaneo.naja.timeentries.editor.noPlanificationFound", true, IResource.DEPTH_ZERO);
                        IMarker marker = file.createMarker("com.jeevaneo.naja.timeentries.editor.noPlanificationFound");
                        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
                        marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_LOW);
                        marker.setAttribute(IMarker.MESSAGE, "No planification found!");
                        marker.setAttribute(EValidator.URI_ATTRIBUTE, uri.toString());
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
                long end = System.currentTimeMillis();
                String message = "Done in " + (end - start) + "ms.";
                System.out.println(message);
                monitor.setTaskName(message);
                return Status.OK_STATUS;
            }

            private static final String PLATFORM_SCHEME = "platform";

            private static final String FILE_SCHEME = "file";

            private static final String RESOURCE_SEGMENT = "resource";

            private IFile deduceFile(URI uri) {
                IFile file = null;
                if (PLATFORM_SCHEME.equals(uri.scheme()) && uri.segmentCount() > 1 && RESOURCE_SEGMENT.equals(uri.segment(0))) {
                    StringBuffer platformResourcePath = new StringBuffer();
                    for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
                        platformResourcePath.append('/');
                        platformResourcePath.append(URI.decode(uri.segment(j)));
                    }
                    file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));
                } else if (FILE_SCHEME.equals(uri.scheme())) {
                    StringBuffer fileResourcePath = new StringBuffer();
                    for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
                        fileResourcePath.append('/');
                        fileResourcePath.append(URI.decode(uri.segment(j)));
                    }
                    file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(fileResourcePath.toString()));
                }
                return file;
            }
        };
        job.schedule();
    }

    private static Date computeDate(int date) {
        int year = date / 10000;
        int month = date / 100 % 100 - 1;
        int day = date % 100;
        Date d = new GregorianCalendar(year, month, day).getTime();
        return d;
    }

    private void recompute(TimeEntry ty, final boolean inferPlanifications, IProgressMonitor monitor) {
        if (null == editingDomain) {
            return;
        }
        if (null != ty.getImputation()) {
            ty.getImputation().setPlanification(null);
            ty.getImputation().setResource(null);
            ty.getImputation().setTask(null);
            ty.setImputation(null);
        }
        Command command = new CreateChildCommand(editingDomain, ty, TimeentriesPackage.Literals.TIME_ENTRY__IMPUTATION, NajaFactory.eINSTANCE.createImputation(), Collections.emptyList()) {

            @Override
            public void execute() {
                super.execute();
                Imputation imputation = (Imputation) this.child;
                TimeEntry ty = (TimeEntry) this.owner;
                imputation.setComment(ty.getComment());
                imputation.setDate(computeDate(ty.getDay()));
                imputation.setLoad(ty.getLoad());
                imputation.setResource(ty.getResource());
                imputation.setTask(ty.getExternalId().getTask());
                ty.setImputation(imputation);
                if (inferPlanifications) {
                    Planification planification = inferPlanification(ty.getImputation());
                    if (null != planification) {
                        Command command2 = new SetCommand(editingDomain, ty.getImputation(), NajaPackage.Literals.IMPUTATION__PLANIFICATION, planification);
                        editingDomain.getCommandStack().execute(command2);
                    } else {
                        System.err.println("No planif found for TimeEntry " + ty);
                    }
                }
            }
        };
        editingDomain.getCommandStack().execute(command);
    }

    protected Planification inferPlanification(Imputation imputation) {
        for (Resource resource : imputation.eResource().getResourceSet().getResources()) {
            TreeIterator<EObject> it = resource.getAllContents();
            for (EObject o = null; it.hasNext(); o = it.next()) {
                if (o instanceof Planification) {
                    Planification planification = (Planification) o;
                    Person planifResource = planification.getResource();
                    if (null == planifResource) {
                        continue;
                    }
                    Task planifTask = planification.getTask();
                    if (null == planifTask) {
                        continue;
                    }
                    if (planifTask.equals(imputation.getTask()) && planifResource.equals(imputation.getResource()) && planification.getUnimputedLoad() >= imputation.getLoad()) {
                        return planification;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = (StructuredSelection) selection;
    }
}
