package ise.plugin.svn.pv;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import projectviewer.vpt.VPTNode;
import ise.plugin.svn.gui.OutputPanel;
import ise.plugin.svn.SVNPlugin;
import ise.plugin.svn.command.Status;
import ise.plugin.svn.data.SVNData;
import ise.plugin.svn.data.StatusData;
import ise.plugin.svn.gui.StatusResultsPanel;
import ise.plugin.svn.library.GUIUtils;
import ise.plugin.svn.library.swingworker.*;
import ise.plugin.svn.io.ConsolePrintStream;
import org.tmatesoft.svn.core.wc.SVNStatus;

/**
 * Collects status of working copy files from PV tree.
 */
public class StatusAction extends NodeActor {

    public void actionPerformed(ActionEvent ae) {
        if (nodes != null && nodes.size() > 0) {
            final SVNData cd = new SVNData();
            List<String> paths = new ArrayList<String>();
            boolean has_directory = false;
            for (VPTNode node : nodes) {
                if (node != null && node.getNodePath() != null) {
                    paths.add(node.getNodePath());
                    if (node.isDirectory()) {
                        has_directory = true;
                    }
                }
            }
            cd.setPaths(paths);
            if (has_directory) {
                int answer = JOptionPane.showConfirmDialog(view, "One or more of the items selected is a directory.\nWould you like to see status for subdirectories and files?", "Show Child Status?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                cd.setRecursive(JOptionPane.YES_OPTION == answer);
            }
            if (username != null && password != null) {
                cd.setUsername(username);
                cd.setPassword(password);
            }
            cd.setRemote(true);
            cd.setOut(new ConsolePrintStream(view));
            view.getDockableWindowManager().showDockableWindow("subversion");
            final OutputPanel output_panel = SVNPlugin.getOutputPanel(view);
            output_panel.showConsole();
            Logger logger = output_panel.getLogger();
            logger.log(Level.INFO, "Gathering status ...");
            for (Handler handler : logger.getHandlers()) {
                handler.flush();
            }
            class Runner extends SwingWorker<StatusData, Object> {

                @Override
                public StatusData doInBackground() {
                    try {
                        Status status = new Status();
                        return status.getStatus(cd);
                    } catch (Exception e) {
                        cd.getOut().printError(e.getMessage());
                    } finally {
                        cd.getOut().close();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        JPanel panel = new StatusResultsPanel(get(), view, username, password);
                        output_panel.addTab("Status", panel);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            (new Runner()).execute();
        }
    }
}
