package net.sourceforge.sqlexplorer.sessiontree.model.utility;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.sessiontree.model.SessionTreeNode;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class DictionaryLoader extends Job {

    private SessionTreeNode _sessionNode;

    private static final String ID = "net.sourceforge.sqlexplorer";

    /**
     * Hidden constructor.
     */
    private DictionaryLoader() {
        super(null);
    }

    /**
     * Default constructor,
     */
    public DictionaryLoader(SessionTreeNode sessionNode) {
        super(Messages.getString("Progress.Dictionary.Title"));
        _sessionNode = sessionNode;
    }

    /**
     * Load dictionary in background process.
     * 
     */
    protected IStatus run(IProgressMonitor monitor) {
        Dictionary dictionary = _sessionNode.getDictionary();
        monitor.setTaskName(Messages.getString("Progress.Dictionary.Scanning"));
        try {
            boolean isLoaded = dictionary.restore(_sessionNode.getRoot(), monitor);
            if (!isLoaded) {
                dictionary.load(_sessionNode.getRoot(), monitor);
                monitor.done();
            }
        } catch (InterruptedException ie) {
            return new Status(IStatus.CANCEL, ID, IStatus.CANCEL, Messages.getString("Progress.Dictionary.Cancelled"), null);
        } catch (Exception e) {
            return new Status(IStatus.ERROR, ID, IStatus.CANCEL, Messages.getString("Progress.Dictionary.Error"), e);
        } finally {
            monitor.done();
        }
        return new Status(IStatus.OK, ID, IStatus.OK, "tested ok ", null);
    }
}
