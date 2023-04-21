package org.tigr.cloe.controller.openAssembly;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.SwingWorker;
import org.tigr.Facade.AssemblyFacadeFactory;
import org.tigr.Facade.IAssemblyFacade;
import org.tigr.cloe.controller.Controller;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAO;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAOException;
import org.tigr.cloe.utils.AssemblyDBRange;
import org.tigr.cloe.utils.Range;
import org.tigr.common.Application;
import org.tigr.seq.display.CancelDialog.ICancellableOperation;
import org.tigr.seq.tdb.exceptions.OutOfRangeException;
import org.tigr.seq.util.CancelHelper;

/**
 * Fetch and Open an Assembly from the Datastore
 * while inside a background thread.
 * this will fetch the assembly specified by
 * the <code>OpenAssemblyParameters</code> given.
 * When the thread completes (may be due to error or user
 * cancellation) this class will notify
 * all <code>OpenAssemblyListeners</code> listening on
 * the  given <code>OpenAssemblyModel</code>.
 * @author dkatzel
 *
 *
 */
public class OpenAssemblyWorker extends SwingWorker<IAssemblyFacade, Void> implements ICancellableOperation, PropertyChangeListener {

    private static int openAttempts = 0;

    private final OpenAssemblyModel openAssemblyModel;

    private final OpenAssemblyParameters openAssemblyParameters;

    /**
     * Constructor.
     * @param openAssemblyModel the openAssemblyModel where the answer
     * will be set and who has listeners listening on it.
     * @param openAssemblyParameters the paramters to specify which assembly
     * and which parts to open.
     */
    public OpenAssemblyWorker(final OpenAssemblyModel openAssemblyModel, final OpenAssemblyParameters openAssemblyParameters) {
        this.openAssemblyModel = openAssemblyModel;
        this.openAssemblyParameters = openAssemblyParameters;
    }

    @Override
    protected IAssemblyFacade doInBackground() throws Exception {
        int assemblyID = openAssemblyParameters.getAssemblyID().intValue();
        String project = openAssemblyParameters.getProject();
        boolean isReadOnly = openAssemblyParameters.isReadOnly();
        boolean prefetchTraces = openAssemblyParameters.isPrefetchTrace();
        System.out.println("prefetch =" + prefetchTraces);
        Range ungappedConstraintRange = calculateOpenAssemblyRange(assemblyID, project);
        CheckCancel();
        setProgress(0);
        DAO dao = Application.getDatastore().getDao();
        dao.beginTransaction();
        IAssemblyFacade assembly = null;
        PropertyChangeSupport pcs = null;
        try {
            pcs = new PropertyChangeSupport(this);
            pcs.addPropertyChangeListener(this);
            assembly = AssemblyFacadeFactory.getInstance().createAssemblyFacade(project, assemblyID, ungappedConstraintRange.getLeft(), ungappedConstraintRange.getRight(), null, isReadOnly, prefetchTraces, pcs);
            CheckCancel();
            if (assembly == null) {
                throw new Exception("Error: could not open Assembly from dataStore");
            }
            setProgress(50);
            CheckCancel();
            if (!assembly.isAssemblyCurrent()) {
                if (!isReadOnly) {
                    String message;
                    if (assembly.containsTrashReads()) {
                        message = "This Assembly ( " + assembly.getDatastoreAssemblyID() + " ) " + "contains Trash Reads, can not open it in editable mode.";
                    } else {
                        message = "Error, not allowed to open an non-current assembly in editable mode";
                    }
                    throw new Exception(message);
                }
                if (!Controller.getInstance().getView().promptUserYesNoQuestion("Open non-current assembly?", "Are you sure you want to open an non-current assembly in read-only mode?")) {
                    throw new Exception("User cancelled operation");
                }
            }
            setProgress(75);
            CheckCancel();
            assembly.validateExhaustively();
            dao.commitTransaction();
        } catch (Exception e) {
            if (pcs != null) {
                pcs.removePropertyChangeListener(this);
                pcs = null;
            }
            if (assembly != null) {
                assembly.close();
            }
            dao.rollbackTransaction();
            throw e;
        }
        setProgress(100);
        return assembly;
    }

    protected void done() {
        try {
            openAssemblyModel.setOpenAssemblyException(null);
            openAssemblyModel.setAssembly(get());
        } catch (Exception e) {
            openAssemblyModel.setOpenAssemblyException(e);
            openAssemblyModel.setAssembly(null);
        }
        openAttempts++;
        openAssemblyModel.openAttempt.setValue(new Integer(openAttempts));
    }

    public void cancel() {
        if (!isDone()) {
            Application.getDatastore().getDao().cancel();
        }
    }

    public String getCancelInProgressText() {
        return "Cancelling Open Assembly...";
    }

    public String getLabel() {
        return "Opening Assembly";
    }

    public String getTitle() {
        return "Open Assembly";
    }

    public void start() {
        execute();
    }

    private Range calculateOpenAssemblyRange(int assemblyID, String project) throws OutOfRangeException, DAOException {
        int startConstraint = -1, endConstraint = -1;
        if (openAssemblyParameters.getStartConstraint() != null || openAssemblyParameters.getEndConstraint() != null) {
            if (openAssemblyParameters.getStartConstraint() != null) {
                startConstraint = openAssemblyParameters.getStartConstraint().intValue();
            }
            if (openAssemblyParameters.getEndConstraint() != null) {
                endConstraint = openAssemblyParameters.getEndConstraint().intValue();
            }
            if (openAssemblyParameters.isGappedCoordinates()) {
                return AssemblyDBRange.calculateUngappedAssemblyRange(project, assemblyID, startConstraint, endConstraint);
            }
        }
        return new Range(startConstraint, endConstraint);
    }

    private void CheckCancel() throws Exception {
        if (CancelHelper.isCancelled()) {
            throw new Exception("User cancelled operation");
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Integer downloadedPercent = (Integer) evt.getNewValue();
        this.setProgress(downloadedPercent);
    }
}
