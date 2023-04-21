package org.svenk.svnhook;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.svenk.svnhook.connection.LookConnection;
import org.svenk.svnhook.runner.IFileBasedHookRunner;
import org.svenk.svnhook.runner.IHookRunner;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.admin.ISVNChangeEntryHandler;
import org.tmatesoft.svn.core.wc.admin.SVNChangeEntry;

public class RunnerControl implements ISVNChangeEntryHandler {

    private Logger logger;

    private LookConnection lookConn;

    private List<IFileBasedHookRunner> fileRunner;

    private String errorMessage;

    private HookKind hookKind;

    public RunnerControl(LookConnection conn) {
        logger = Logger.getLogger(Svnhook.class);
        lookConn = conn;
    }

    public boolean run(HookKind hookKind) {
        this.hookKind = hookKind;
        boolean flag = true;
        if (fileRunner != null && fileRunner.size() > 0) {
            try {
                lookConn.getChanged(this);
            } catch (SVNException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e);
                }
                if (errorMessage == null) {
                    String msg = e.getMessage();
                    errorMessage = msg;
                    if (logger.isDebugEnabled()) {
                        logger.debug(msg);
                    }
                }
                flag = false;
            }
        }
        return flag;
    }

    public void setRunner(List<IHookRunner> runner) {
        fileRunner = new ArrayList<IFileBasedHookRunner>();
        for (IHookRunner hookRunner : runner) {
            if (hookRunner instanceof IFileBasedHookRunner) {
                fileRunner.add((IFileBasedHookRunner) hookRunner);
            }
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void handleEntry(SVNChangeEntry entry) throws SVNException {
        if (entry.getType() != SVNChangeEntry.TYPE_DELETED && entry.getKind() == SVNNodeKind.FILE) {
            int size = fileRunner.size();
            byte data[] = lookConn.getFileContent(entry.getPath());
            for (int i = 0; i < size; i++) {
                IFileBasedHookRunner runner = fileRunner.get(0);
                if (runner.isRunnable() && runner.isUsable(hookKind)) {
                    if (!runner.changedFile(entry, data)) {
                        errorMessage = runner.getErrorMessage();
                        throw new SVNException(null);
                    }
                }
            }
        }
    }
}
