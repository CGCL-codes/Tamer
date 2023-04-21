package com.msli.rcp.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.Saveable;
import com.msli.app.status.Status;
import com.msli.core.util.CoreUtils;
import com.msli.rcp.app.MsliApplicationService;
import com.msli.rcp.bench.PackageProps;

/**
 * Utilities related to cleanable resources.
 * @author jonb
 */
public class CleanableUtils {

    private CleanableUtils() {
    }

    /**
	 * Creates a new Saveable by wrapping a Cleanable. As with Cleanable, the
	 * resulting Saveable will have minimal GUI support.
	 * @param icon Shared exposed icon proxy. None if null.
	 * @param target Shared exposed wrapper target. Never null.
	 * @return Ceded saveable object. Never null.
	 */
    public static Saveable newSaveable(ImageDescriptor icon, Cleanable target) {
        return new CleanableWrapper(icon, target);
    }

    /**
	 * Creates a new Saveable by wrapping a ModelFileProxy. As with Cleanable,
	 * the resulting Saveable will have minimal GUI support.
	 * @param icon Shared exposed icon proxy. None if null.
	 * @param target Shared exposed wrapper target. Never null.
	 * @return Ceded saveable object. Never null.
	 */
    public static Saveable newSaveable(ImageDescriptor icon, ModelFileProxy<?> target) {
        return new ProxyWrapper(icon, target);
    }

    /**
	 * Returns the dirty cleanables found in a group of proxies.
	 * @return Ceded list of dirty cleanables. Never null.
	 */
    public static List<Cleanable> findDirty(Collection<? extends Cleanable> cleanables) {
        CoreUtils.assertNonNullArg(cleanables);
        List<Cleanable> dirties = new ArrayList<Cleanable>();
        for (Cleanable cleanable : cleanables) {
            if (cleanable.isDirty()) dirties.add(cleanable);
        }
        return dirties;
    }

    /**
	 * Returns the saveables corresponding to a group of cleanables.
	 * @return Ceded list of saveables. Never null.
	 */
    public static List<Saveable> toSaveables(Collection<? extends Cleanable> cleanables) {
        CoreUtils.assertNonNullArg(cleanables);
        List<Saveable> saveables = new ArrayList<Saveable>();
        for (Cleanable cleanable : cleanables) {
            saveables.add(cleanable.asSaveable());
        }
        return saveables;
    }

    /**
	 * Cleans a group of dirty cleanables, by either saving or reverting each
	 * one. If a cleanable is not dirty nothing happens. A dirty cleanable will
	 * be saved only if it is found in the saved group, otherwise it will be
	 * reverted.
	 * <p>
	 * If called at application shutdown, the client should call
	 * waitForCleanJobs() after this method to assure that the cleaning jobs
	 * have completed before proceeding.
	 * @param dirties Temp input group of shared exposed cleanables that are
	 * presumed to be dirty. Never null.
	 * @param saves Temp input group of cleanables in dirties to be saved. Never
	 * null. Ignored if missing from dirties.
	 */
    public static void doClean(Collection<? extends Cleanable> dirties, Collection<?> saves) {
        CoreUtils.assertNonNullArg(dirties);
        CoreUtils.assertNonNullArg(saves);
        for (Cleanable dirty : dirties) {
            if (!dirty.isDirty()) continue;
            if (saves.contains(dirty)) {
                doSaveJob(dirty);
            } else {
                doClearJob(dirty);
            }
        }
    }

    /**
	 * Builds and starts a job to save a given resource. The resource will be
	 * saved only if it is dirty when the job runs.
	 * @param cleanable Shared exposed cleanable. Never null.
	 */
    public static void doSaveJob(final Cleanable cleanable) {
        Job job = new CleanJob(PackageProps.SAVE_RESOURCE_JOB_NAME, cleanable) {

            @Override
            public void doClean(IProgressMonitor monitor, Cleanable cleanable) throws CoreException {
                cleanable.doSave(monitor);
            }

            @Override
            public void doFail(IProgressMonitor monitor, Cleanable cleanable, CoreException ex) {
                String message = NLS.bind(PackageProps.SAVE_RESOURCE_JOB_FAILED, cleanable.getResource().getFullPath());
                MsliApplicationService.reportStatus(Status.Type.ERROR, PackageProps.RESOURCE_PROBLEM_TITLE, message, ex);
            }
        };
        job.setRule(cleanable.getResource());
        job.schedule();
    }

    /**
	 * Builds and starts a job to clear a given resource. The resource will be
	 * cleared only if it is dirty when the job runs.
	 * @param cleanable Shared exposed cleanable. Never null.
	 */
    public static void doClearJob(final Cleanable cleanable) {
        Job job = new CleanJob(PackageProps.CLEAR_RESOURCE_JOB_NAME, cleanable) {

            @Override
            public void doClean(IProgressMonitor monitor, Cleanable saveable) throws CoreException {
                saveable.doRevert(monitor);
            }

            @Override
            public void doFail(IProgressMonitor monitor, Cleanable saveable, CoreException ex) {
                String message = NLS.bind(PackageProps.CLEAR_RESOURCE_JOB_FAILED, saveable.getResource().getFullPath());
                MsliApplicationService.reportStatus(Status.Type.ERROR, PackageProps.RESOURCE_PROBLEM_TITLE, message, ex);
            }
        };
        job.setRule(cleanable.getResource());
        job.schedule();
    }

    /**
	 * Waits for the save jobs associated with a group of saveables to complete.
	 * This may open a progress dialog with the option to cancel.
	 * @param dirties Temp input group of shared exposed saveables that are
	 * presumed to be dirty. Never null.
	 * @return True if the jobs were successfully completed. False if the user
	 * chose to cancel.
	 */
    public static boolean waitForCleanJobs(final Collection<? extends Cleanable> dirties) {
        try {
            MsliApplicationService.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InterruptedException {
                    Job.getJobManager().join(new CleanJobFamily(dirties), monitor);
                }
            });
        } catch (InvocationTargetException ex) {
            MsliApplicationService.reportStatus(Status.Type.ERROR, null, "Should never happen!", ex);
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }

    /**
	 * Equality is based on the Saveable type and that of the target.
	 */
    private static class CleanableWrapper extends Saveable {

        /**
		 * Creates an instance.
		 * @param icon Shared exposed icon proxy. None if null.
		 * @param target Shared exposed wrapper target.  Never null.
		 */
        public CleanableWrapper(ImageDescriptor icon, Cleanable target) {
            CoreUtils.assertNonNullArg(target);
            _icon = icon;
            _target = target;
        }

        @Override
        public void doSave(IProgressMonitor monitor) throws CoreException {
            _target.doSave(monitor);
        }

        @Override
        public ImageDescriptor getImageDescriptor() {
            return _icon;
        }

        @Override
        public String getName() {
            return _target.getResource().getName();
        }

        @Override
        public String getToolTipText() {
            return _target.getResource().getFullPath().toString();
        }

        @Override
        public boolean isDirty() {
            return _target.isDirty();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof Saveable)) return false;
            return _target.equals(object);
        }

        @Override
        public int hashCode() {
            return _target.hashCode();
        }

        private ImageDescriptor _icon;

        private Cleanable _target;
    }

    ;

    /**
	 * Equality is based on the Saveable type and that of the target.
	 */
    private static class ProxyWrapper extends Saveable {

        /**
		 * Creates an instance.
		 * @param icon Shared exposed icon proxy. None if null.
		 * @param target Shared exposed wrapper target.  Never null.
		 */
        public ProxyWrapper(ImageDescriptor icon, ModelFileProxy<?> target) {
            CoreUtils.assertNonNullArg(target);
            _icon = icon;
            _target = target;
        }

        @Override
        public void doSave(IProgressMonitor monitor) throws CoreException {
            _target.getReporter().setProgress(monitor);
            _target.saveModel();
        }

        @Override
        public ImageDescriptor getImageDescriptor() {
            return _icon;
        }

        @Override
        public String getName() {
            return _target.getResource().getName();
        }

        @Override
        public String getToolTipText() {
            return _target.getResource().getFullPath().toString();
        }

        @Override
        public boolean isDirty() {
            return _target.isModelDirty();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof Saveable)) return false;
            return _target.equals(object);
        }

        @Override
        public int hashCode() {
            return Saveable.class.hashCode() + _target.hashCode();
        }

        private ImageDescriptor _icon;

        private ModelFileProxy<?> _target;
    }

    ;

    /**
	 * A WorkspaceJob that cleans (saves or clears) a target saveable.
	 */
    private abstract static class CleanJob extends WorkspaceJob {

        /**
		 * Creates an instance.
		 * @param jobName Internal name of the job. Never null.
		 * @param cleanable Shared exposed target cleanable. Never null.
		 */
        public CleanJob(String jobName, Cleanable cleanable) {
            super(jobName);
            CoreUtils.assertNonNullArg(jobName);
            CoreUtils.assertNonNullArg(cleanable);
            _cleanable = cleanable;
        }

        /**
		 * Called by the system if the target saveable is dirty and it is
		 * time to clean it (save or clear).
		 * @param monitor Shared exposed progress monitor. Never null.
		 * @param cleanable Shared exposed target cleanable. Never null.
		 */
        protected abstract void doClean(IProgressMonitor monitor, Cleanable cleanable) throws CoreException;

        /**
		 * Called by the system if the clean operation failed.
		 * @param monitor Shared exposed progress monitor. Never null.
		 * @param cleanable Shared exposed target cleanable. Never null.
		 * @param ex Shared exposed exception. Never null.
		 */
        protected abstract void doFail(IProgressMonitor monitor, Cleanable cleanable, CoreException ex);

        @Override
        public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
            try {
                if (_cleanable.isDirty()) {
                    doClean(monitor, _cleanable);
                }
            } catch (CoreException ex) {
                doFail(monitor, _cleanable, ex);
            }
            return org.eclipse.core.runtime.Status.OK_STATUS;
        }

        @Override
        public boolean belongsTo(Object family) {
            if (family instanceof CleanJobFamily) {
                return ((CleanJobFamily) family).contains(_cleanable);
            }
            return false;
        }

        private Cleanable _cleanable;
    }

    /**
	 * Marker type for cleanables being cleaned (saved or cleared) via jobs.
	 */
    private static class CleanJobFamily extends HashSet<Cleanable> {

        private static final long serialVersionUID = 1L;

        public CleanJobFamily(Collection<? extends Cleanable> cleanables) {
            super(cleanables);
        }
    }
}
