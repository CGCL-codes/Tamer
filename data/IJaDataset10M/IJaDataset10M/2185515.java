package com.cronopista.lightpacker.steps;

import java.util.Iterator;
import java.util.List;
import com.cronopista.lightpacker.Main;
import com.cronopista.lightpacker.GUI.InstallingPanel;
import com.cronopista.lightpacker.actions.Action;

/**
 * @author Eduardo Rodr�guez
 * 
 */
public class ShowProgressStep extends Step implements Runnable {

    private InstallingPanel panel;

    private boolean detailsOpened;

    private boolean showDetailsButton = true;

    private boolean preselectedActions = true;

    private String finishedCode = "installing.finished.title";

    private boolean moveForward;

    public void execute() {
        if (!executed) {
            if (!isSilent()) {
                if (panel == null) {
                    List packs = Main.getInstance().getPackagesToInstall();
                    if (!preselectedActions) {
                        packs = actions;
                    }
                    panel = new InstallingPanel(getPanelData(), detailsOpened, showDetailsButton, packs);
                }
                Installer.getInstance().getMainFrame().setPanel(panel);
            }
        }
        new Thread(this).start();
    }

    public void run() {
        List packs = Main.getInstance().getPackagesToInstall();
        if (!preselectedActions) packs = actions;
        for (Iterator iterator = packs.iterator(); iterator.hasNext(); ) {
            Action action = (Action) iterator.next();
            if ((action.isRequired() || action.isSelected()) && action.canExecute() && action.getErrorTolerance() > Installer.getInstance().getCurrentErrorLevel() && Main.getInstance().getOs().canExecuteAction(action)) {
                if (panel != null) panel.addMessage(action.getName(), true);
                int res = action.execute(panel);
                if (res > Installer.getInstance().getCurrentErrorLevel()) {
                    Installer.getInstance().setCurrentErrorLevel(res);
                }
                if (res > Action.OK) {
                    if (panel != null) panel.addMessage(action.getErrorMessage(), false);
                    List actions = action.getErrorActions();
                    for (Iterator iterator2 = actions.iterator(); iterator2.hasNext(); ) {
                        Action a = (Action) iterator2.next();
                        a.execute(null);
                    }
                }
            }
        }
        if (panel != null) panel.addMessage(Main.getInstance().translate(finishedCode), true);
        if (preselectedActions) super.execute();
        if (panel != null) panel.enableNext();
        if (moveForward) Installer.getInstance().moveForward();
    }

    public boolean isDetailsOpened() {
        return detailsOpened;
    }

    public void setDetailsOpened(boolean detailsOpened) {
        this.detailsOpened = detailsOpened;
    }

    public boolean isShowDetailsButton() {
        return showDetailsButton;
    }

    public void setShowDetailsButton(boolean showDetailsButton) {
        this.showDetailsButton = showDetailsButton;
    }

    public void setFinishedCode(String finishedCode) {
        this.finishedCode = finishedCode;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    public void setPreselectedActions(boolean preselectedActions) {
        this.preselectedActions = preselectedActions;
    }
}
