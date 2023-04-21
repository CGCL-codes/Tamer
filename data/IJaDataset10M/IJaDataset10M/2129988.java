package ise.plugin.svn.action;

import ise.plugin.svn.gui.OutputPanel;
import ise.plugin.svn.SVNPlugin;
import ise.plugin.svn.command.Merge;
import ise.plugin.svn.data.MergeData;
import ise.plugin.svn.data.MergeResults;
import ise.plugin.svn.data.SVNData;
import ise.plugin.svn.gui.MergeResultsPanel;
import ise.plugin.svn.io.ConsolePrintStream;
import common.swingworker.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JPanel;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

/**
 * Action to do an svn merge.
 */
public class MergeAction extends SVNAction {

    private MergeData data = null;

    public MergeAction(View view, MergeData data) {
        super(view, jEdit.getProperty("ips.Merge", "Merge"));
        if (data == null) throw new IllegalArgumentException("data may not be null");
        this.data = data;
        setUsername(data.getUsername());
        setPassword(data.getPassword());
    }

    public void actionPerformed(ActionEvent ae) {
        if (getUsername() == null) {
            verifyLogin(data.getFromFile() == null ? "" : data.getFromFile().getAbsolutePath());
            if (isCanceled()) {
                return;
            }
            data.setUsername(getUsername());
            data.setPassword(getPassword());
        }
        data.setOut(new ConsolePrintStream(getView()));
        getView().getDockableWindowManager().showDockableWindow("subversion");
        final OutputPanel panel = SVNPlugin.getOutputPanel(getView());
        panel.showConsole();
        Logger logger = panel.getLogger();
        logger.log(Level.INFO, jEdit.getProperty("ips.Merging...", "Merging..."));
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
        }
        class Runner extends SwingWorker<MergeResults, Object> {

            Merge merge = new Merge();

            @Override
            public MergeResults doInBackground() {
                try {
                    return merge.doMerge(data);
                } catch (Exception e) {
                    data.getOut().printError(e.getMessage());
                } finally {
                    data.getOut().close();
                }
                return null;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean cancelled = super.cancel(mayInterruptIfRunning);
                merge.setCancelled(cancelled);
                if (cancelled) {
                    data.getOut().printError("Stopped 'Merge' action.");
                    data.getOut().close();
                } else {
                    data.getOut().printError("Unable to stop 'Merge' action.");
                }
                return cancelled;
            }

            @Override
            protected void done() {
                if (isCancelled()) {
                    return;
                }
                try {
                    MergeResults results = get();
                    if (results == null) {
                        return;
                    }
                    JPanel results_panel = new MergeResultsPanel(results);
                    panel.addTab(jEdit.getProperty("ips.Merge", "Merge"), results_panel);
                    if (!results.isDryRun()) {
                        SVNData cd = new SVNData();
                        cd.addPath(data.getDestinationFile().getAbsolutePath());
                        cd.setRemote(false);
                        cd.setRecursive(data.getRecursive());
                        cd.setOut(data.getOut());
                        cd.setErr(data.getErr());
                        cd.setUsername(data.getUsername());
                        cd.setPassword(data.getPassword());
                        StatusAction sa = new StatusAction(MergeAction.this.getView(), cd);
                        sa.setTabTitle(jEdit.getProperty("ips.Merge", "Merge"));
                        sa.actionPerformed(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Runner runner = new Runner();
        panel.addWorker("Log", runner);
        runner.execute();
    }
}
