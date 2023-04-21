package ise.plugin.svn.action;

import ise.plugin.svn.gui.OutputPanel;
import ise.plugin.svn.SVNPlugin;
import ise.plugin.svn.command.Add;
import ise.plugin.svn.command.BrowseRepository;
import ise.plugin.svn.command.Info;
import ise.plugin.svn.data.DiffData;
import ise.plugin.svn.data.AddResults;
import ise.plugin.svn.gui.DiffDialog;
import ise.plugin.svn.gui.AddResultsPanel;
import ise.plugin.svn.gui.SVNInfoPanel;
import ise.plugin.svn.io.ConsolePrintStream;
import ise.plugin.svn.library.GUIUtils;
import ise.plugin.svn.library.swingworker.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.EditPane;
import jdiff.DualDiff;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNInfo;

/**
 * ActionListener to perform sort of an svn diff.  While subversion can do a diff,
 * I'm delegating to the JDiff plugin to create and display the diff.
 * This is not dependent on ProjectViewer.
 */
public class DiffAction implements ActionListener {

    private DiffDialog dialog = null;

    private View view = null;

    private String path = null;

    private String revision1 = null;

    private String revision2 = null;

    private String username = null;

    private String password = null;

    /**
     * @param view the View in which to display results
     * @param path the name of a local file to be diffed.  A dialog will be shown
     * to let the user pick the revision to diff against.
     * @param username the username for the svn repository
     * @param password the password for the username
     */
    public DiffAction(View view, String path, String username, String password) {
        if (view == null) throw new IllegalArgumentException("view may not be null");
        if (path == null || path.length() == 0) throw new IllegalArgumentException("path may not be null");
        this.view = view;
        this.path = path;
        this.username = username;
        this.password = password;
    }

    public DiffAction(View view, String path, String revision1, String revision2, String username, String password) {
        if (view == null) throw new IllegalArgumentException("view may not be null");
        if (path == null || path.length() == 0) throw new IllegalArgumentException("path may not be null");
        this.view = view;
        this.path = path;
        this.revision1 = revision1;
        this.revision2 = revision2;
        this.username = username;
        this.password = password;
    }

    public void actionPerformed(ActionEvent ae) {
        if (path != null && path.length() > 0) {
            final DiffData data;
            if (revision1 == null) {
                dialog = new DiffDialog(view, path);
                GUIUtils.center(view, dialog);
                dialog.setVisible(true);
                data = dialog.getData();
                if (data == null) {
                    return;
                }
            } else {
                if (revision2 == null) {
                    return;
                }
                data = new DiffData();
                data.addPath(path);
                data.setRevision1(SVNRevision.parse(revision1));
                data.setRevision2(SVNRevision.parse(revision2));
            }
            if (username != null && password != null) {
                data.setUsername(username);
                data.setPassword(password);
            }
            data.setOut(new ConsolePrintStream(view));
            view.getDockableWindowManager().showDockableWindow("subversion");
            final OutputPanel panel = SVNPlugin.getOutputPanel(view);
            panel.showConsole();
            Logger logger = panel.getLogger();
            logger.log(Level.INFO, "Preparing to diff ...");
            for (Handler handler : logger.getHandlers()) {
                handler.flush();
            }
            class Runner extends SwingWorker<SVNInfo, Object> {

                @Override
                public SVNInfo doInBackground() {
                    try {
                        Info info = new Info();
                        List<SVNInfo> infos = info.getInfo(data);
                        if (infos.size() > 0) {
                            return infos.get(0);
                        }
                        return null;
                    } catch (Exception e) {
                        data.getOut().printError(e.getMessage());
                    } finally {
                        data.getOut().close();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        SVNInfo info = get();
                        SVNURL url = info.getRepositoryRootURL();
                        String svn_path = info.getPath();
                        BrowseRepository br = new BrowseRepository();
                        File remote1 = br.getFile(url.toString(), svn_path, data.getRevision1().getNumber(), data.getUsername(), data.getPassword());
                        File remote2 = null;
                        if (data.getRevision2() != null) {
                            remote2 = br.getFile(url.toString(), svn_path, data.getRevision2().getNumber(), data.getUsername(), data.getPassword());
                        }
                        if (remote1 == null || remote2 == null) {
                            JOptionPane.showMessageDialog(view, "Unable to fetch contents for comparison.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if ((remote1 != null && remote1.isDirectory()) || (remote2 != null && remote2.isDirectory())) {
                            JOptionPane.showMessageDialog(view, "Unable to compare directories.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        view.unsplit();
                        DualDiff.toggleFor(view);
                        EditPane[] editPanes = view.getEditPanes();
                        if (remote2 != null) {
                            editPanes[0].setBuffer(jEdit.openFile(view, remote2.getAbsolutePath()));
                        } else {
                            editPanes[0].setBuffer(jEdit.openFile(view, path));
                        }
                        editPanes[1].setBuffer(jEdit.openFile(view, remote1.getAbsolutePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            (new Runner()).execute();
        }
    }
}
