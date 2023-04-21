package ise.plugin.svn.command;

import java.io.*;
import java.util.*;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import ise.plugin.svn.data.MergeData;
import ise.plugin.svn.data.MergeResults;
import org.gjt.sp.jedit.jEdit;

public class Merge {

    private boolean cancelled = false;

    /**
     * Fills a MergeResults based on the given MergeData.
     * @param data MergeData containing the information necessary to do a merge.
     */
    public MergeResults doMerge(MergeData data) throws CommandInitializationException, SVNException {
        SVNKit.setupLibrary();
        if (data.getOut() == null) {
            throw new CommandInitializationException("Invalid output stream.");
        }
        if (data.getErr() == null) {
            data.setErr(data.getOut());
        }
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager clientManager = SVNClientManager.newInstance(options, SVNWCUtil.createDefaultAuthenticationManager(data.getUsername(), data.getDecryptedPassword()));
        SVNDiffClient client = clientManager.getDiffClient();
        MergeResults results = new MergeResults();
        results.setDryRun(data.getDryRun());
        results.setCommandLineEquivalent(data.commandLineEquivalent());
        MergeEventProcessor handler = new MergeEventProcessor(data.getOut(), results);
        client.setEventHandler(handler);
        try {
            System.out.println(data.toString());
            SVNDepth depth = data.getRecursive() ? SVNDepth.INFINITY : SVNDepth.EMPTY;
            if (data.getFromFile() != null && data.getToFile() != null) {
                client.doMerge(data.getFromFile(), data.getStartRevision(), data.getToFile(), data.getEndRevision(), data.getDestinationFile(), depth, !data.getIgnoreAncestry(), data.getForce(), data.getDryRun(), false);
            } else if (data.getFromFile() != null && data.getToPath() != null) {
                client.doMerge(data.getFromFile(), data.getStartRevision(), SVNURL.parseURIDecoded(data.getToPath()), data.getEndRevision(), data.getDestinationFile(), depth, !data.getIgnoreAncestry(), data.getForce(), data.getDryRun(), false);
            } else if (data.getFromPath() != null && data.getToFile() != null) {
                client.doMerge(SVNURL.parseURIDecoded(data.getFromPath()), data.getStartRevision(), data.getToFile(), data.getEndRevision(), data.getDestinationFile(), depth, !data.getIgnoreAncestry(), data.getForce(), data.getDryRun(), false);
            } else if (data.getFromPath() != null && data.getToPath() != null) {
                client.doMerge(SVNURL.parseURIDecoded(data.getFromPath()), data.getStartRevision(), SVNURL.parseURIDecoded(data.getToPath()), data.getEndRevision(), data.getDestinationFile(), depth, !data.getIgnoreAncestry(), data.getForce(), data.getDryRun(), false);
            } else {
                String msg = data.checkValid();
                handler.getResults().setErrorMessage(msg == null ? jEdit.getProperty("ips.Merge_data_error.", "Merge data error.") : msg);
            }
        } catch (Exception ignored) {
        }
        return handler.getResults();
    }

    public void setCancelled(boolean c) {
        cancelled = c;
    }

    class MergeEventProcessor implements ISVNEventHandler {

        private final PrintStream out;

        private MergeResults results = null;

        public MergeEventProcessor(PrintStream out, MergeResults results) {
            this.out = out;
            this.results = results;
        }

        public MergeResults getResults() {
            return results;
        }

        public void handleEvent(SVNEvent event, double progress) {
            String filename = "";
            if (event.getFile() != null) {
                filename = SVNFormatUtil.formatPath(event.getFile());
            }
            if (event.getAction() == SVNEventAction.UPDATE_ADD) {
                if (event.getContentsStatus() == SVNStatusType.CONFLICTED) {
                    out.println("C    " + filename);
                    results.addConflicted(filename);
                } else {
                    out.println("A    " + filename);
                    results.addAdded(filename);
                }
            } else if (event.getAction() == SVNEventAction.UPDATE_DELETE) {
                out.println("D    " + filename);
                results.addDeleted(filename);
            } else if (event.getAction() == SVNEventAction.UPDATE_UPDATE) {
                StringBuffer sb = new StringBuffer();
                if (event.getNodeKind() != SVNNodeKind.DIR) {
                    if (event.getContentsStatus() == SVNStatusType.CHANGED) {
                        sb.append("U");
                        results.addUpdated(filename);
                    } else if (event.getContentsStatus() == SVNStatusType.CONFLICTED) {
                        sb.append("C");
                        results.addConflicted(filename);
                    } else if (event.getContentsStatus() == SVNStatusType.MERGED) {
                        sb.append("G");
                        results.addMerged(filename);
                    } else {
                        sb.append(" ");
                    }
                } else {
                    sb.append(' ');
                }
                if (event.getPropertiesStatus() == SVNStatusType.CHANGED) {
                    sb.append("U");
                    results.addUpdated(filename);
                } else if (event.getPropertiesStatus() == SVNStatusType.CONFLICTED) {
                    sb.append("C");
                    results.addConflicted(filename);
                } else if (event.getPropertiesStatus() == SVNStatusType.MERGED) {
                    sb.append("G");
                    results.addMerged(filename);
                } else {
                    sb.append(" ");
                }
                if (sb.toString().trim().length() > 0) {
                    out.println(sb.toString() + "  " + filename);
                }
            } else if (event.getAction() == SVNEventAction.ADD) {
                if (SVNProperty.isBinaryMimeType(event.getMimeType())) {
                    out.println("A  (bin)  " + filename);
                    results.addAdded(filename);
                } else {
                    out.println("A         " + filename);
                    results.addAdded(filename);
                }
            } else if (event.getAction() == SVNEventAction.DELETE) {
                out.println("D         " + filename);
                results.addDeleted(filename);
            } else if (event.getAction() == SVNEventAction.SKIP) {
                out.println("Skipped '" + filename + "'");
                results.addSkipped(filename);
            }
        }

        public void checkCancelled() throws SVNCancelException {
            if (Merge.this.cancelled) {
                results.setCancelled(true);
                throw new SVNCancelException();
            }
        }
    }
}
