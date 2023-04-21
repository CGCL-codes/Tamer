package org.vtt.stylebase.controller;

import org.vtt.stylebase.gui.IViewHandler;
import org.vtt.stylebase.model.PatternAdmin;
import org.vtt.stylebase.system.Log;
import org.vtt.stylebase.system.ResourceAdmin;
import org.vtt.stylebase.system.SessionAdmin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class DatabaseViewController extends Controller {

    private IViewHandler view = null;

    private PatternAdmin modelAdmin = null;

    private ASearch search = null;

    private AFetch fetch = null;

    private ADownload download = null;

    private ALogin login = null;

    private AEdit edit = null;

    private ADelete delete = null;

    private AShowGuide guide = null;

    private AManageLock locking = null;

    private Action doubleClick = null;

    private Action relogin = null;

    private Action help = null;

    private Action editAttr = null;

    public DatabaseViewController(PatternAdmin model, IViewHandler view) {
        this.view = view;
        this.modelAdmin = model;
        this.makeActions();
        super.setRoot(this);
    }

    private void makeActions() {
        this.download = new ADownload(this);
        this.fetch = new AFetch(this);
        this.search = new ASearch(this);
        this.login = new ALogin(this);
        this.edit = new AEdit(this);
        this.delete = new ADelete(this);
        this.guide = new AShowGuide(this);
        this.locking = new AManageLock(this);
        this.editAttr = new AEditAttr(this);
        this.doubleClick = new Action() {

            public void run() {
                getAction(Controller.ACTION_GUIDE).run();
            }
        };
        this.help = new Action() {

            public void run() {
                PlatformUI.getWorkbench().getHelpSystem().displayHelp("org.vtt.stylebase.user_guide");
            }
        };
        this.help.setToolTipText("Help");
        this.help.setImageDescriptor(ResourceAdmin.getImageDescriptor(ResourceAdmin.IMG_HELP));
        this.relogin = new Action() {

            public void run() {
                if (SessionAdmin.getInstance().getUser() != null) {
                    if (SessionAdmin.getInstance().checkCurrentUser()) {
                        String msg = "You are successfully connected as user " + SessionAdmin.getInstance().getUser() + ". ";
                        msg = msg + "Would you like to login as another user or change your role?";
                        boolean reply = MessageDialog.openConfirm(getView().getShell(), "You are already connected", msg);
                        if (reply == true) SessionAdmin.getInstance().closeSession();
                    }
                }
                getAction(ACTION_LOGIN).run();
                getAction(Controller.ACTION_FETCH).run();
            }
        };
        this.relogin.setToolTipText("Login or change user");
        this.relogin.setImageDescriptor(ResourceAdmin.getImageDescriptor(ResourceAdmin.IMG_LOGIN));
    }

    public Shell getShell() {
        return this.view.getShell();
    }

    public Action getAction(int type) {
        switch(type) {
            case ACTION_DOWNLOAD:
                return this.download;
            case ACTION_FETCH:
                return this.fetch;
            case ACTION_SEARCH:
                return this.search;
            case ACTION_LOGIN:
                return this.login;
            case ACTION_EDIT:
                return this.edit;
            case ACTION_DELETE:
                return this.delete;
            case ACTION_GUIDE:
                return this.guide;
            case ACTION_LOCKING:
                return this.locking;
            case ACTION_DOUBLECLICK:
                return this.doubleClick;
            case ACTION_RELOGIN:
                return this.relogin;
            case ACTION_HELP:
                return this.help;
            case ACTION_EDITATTR:
                return this.editAttr;
            default:
                Log.logMsg(new String("DatabaseViewController:get: Unkonwn action type"));
                return null;
        }
    }

    IViewHandler getView() {
        return this.view;
    }

    PatternAdmin getModelAdmin() {
        return this.modelAdmin;
    }
}
