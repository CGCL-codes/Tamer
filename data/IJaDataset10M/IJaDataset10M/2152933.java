package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class MakeAllAvailableAction extends AbstractAction {

    ForegroundWindowProxy windowProxy;

    ProjectProxy projectProxy;

    GmaneFacade gmane;

    CommonService progress;

    public MakeAllAvailableAction(ProjectProxy project, ForegroundWindowProxy windowProxy, CommonService progress, GmaneFacade gmane) {
        super("Download missing emails from Gmane...");
        this.windowProxy = windowProxy;
        this.progress = progress;
        this.projectProxy = project;
        this.gmane = gmane;
        project.add(new VariableProxyListener<Project>() {

            public void setVariable(Project newValue) {
                setEnabled(newValue != null);
            }
        });
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
    }

    public void actionPerformed(ActionEvent arg0) {
        new Thread(new Runnable() {

            public void run() {
                gmane.makeAllAvailable(PrimaryDocument.getTreeWalker(projectProxy.getVariable().getPrimaryDocuments()), progress.getProgressBar("Making Documents Available"));
            }
        }).start();
    }
}

;
