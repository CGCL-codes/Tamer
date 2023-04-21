package svn;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import util.BlueSVNLogger;

public class SVNUpdateEventHandler implements ISVNEventHandler {

    boolean _bConflict = false;

    public void handleEvent(SVNEvent event, double progress) {
        SVNEventAction action = event.getAction();
        String pathChangeType = " ";
        if (action == SVNEventAction.UPDATE_ADD) {
            pathChangeType = "A";
        } else if (action == SVNEventAction.UPDATE_DELETE) {
            pathChangeType = "D";
        } else if (action == SVNEventAction.UPDATE_UPDATE) {
            SVNStatusType contentsStatus = event.getContentsStatus();
            if (contentsStatus == SVNStatusType.CHANGED) {
                pathChangeType = "U";
            } else if (contentsStatus == SVNStatusType.CONFLICTED) {
                _bConflict = true;
                pathChangeType = "C";
            } else if (contentsStatus == SVNStatusType.MERGED) {
                pathChangeType = "G";
            }
        } else if (action == SVNEventAction.UPDATE_EXTERNAL) {
            BlueSVNLogger.getInstance().log("Fetching external item into '" + event.getFile().getAbsolutePath() + "'");
            BlueSVNLogger.getInstance().log("External at revision " + event.getRevision());
            return;
        } else if (action == SVNEventAction.UPDATE_COMPLETED) {
            BlueSVNLogger.getInstance().log("At revision " + event.getRevision());
            return;
        } else if (action == SVNEventAction.ADD) {
            BlueSVNLogger.getInstance().log("A     " + event.getPath());
            return;
        } else if (action == SVNEventAction.DELETE) {
            BlueSVNLogger.getInstance().log("D     " + event.getPath());
            return;
        } else if (action == SVNEventAction.LOCKED) {
            BlueSVNLogger.getInstance().log("L     " + event.getPath());
            return;
        } else if (action == SVNEventAction.LOCK_FAILED) {
            BlueSVNLogger.getInstance().log("failed to lock    " + event.getPath());
            return;
        }
        SVNStatusType propertiesStatus = event.getPropertiesStatus();
        String propertiesChangeType = " ";
        if (propertiesStatus == SVNStatusType.CHANGED) {
            propertiesChangeType = "U";
        } else if (propertiesStatus == SVNStatusType.CONFLICTED) {
            _bConflict = true;
            propertiesChangeType = "C";
        } else if (propertiesStatus == SVNStatusType.MERGED) {
            propertiesChangeType = "G";
        }
        String lockLabel = " ";
        SVNStatusType lockType = event.getLockStatus();
        if (lockType == SVNStatusType.LOCK_UNLOCKED) {
            lockLabel = "B";
        }
        BlueSVNLogger.getInstance().log(pathChangeType + propertiesChangeType + lockLabel + "       " + event.getPath());
    }

    public void checkCancelled() throws SVNCancelException {
        if (_bConflict) {
            _bConflict = false;
            throw new SVNCancelException();
        }
    }
}
