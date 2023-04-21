package com.pub.ha;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import com.pub.ha.data.AdditiveDetails;
import com.pub.ha.data.AdditiveMngr;
import com.pub.ha.ui.UITools;
import com.pub.ha.view.FilterDBX;
import com.pub.ha.view.ListView;
import com.pub.ha.view.MainView;
import com.pub.ha.view.SpashView;
import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;

public class UIMidlet extends MIDlet implements ActionListener {

    private SpashView splash;

    private MainView main;

    private ListView list;

    private Command cmdList;

    private Command cmdFilter;

    private Command cmdBack;

    private Command cmdExit;

    private Command cmdAbout;

    public UIMidlet() {
        Display.init(this);
        Resources r = null;
        try {
            r = Resources.open("/LWUITtheme.res");
            String[] ts = r.getThemeResourceNames();
            if (com.pub.ha.Log.DBG) for (int i = 0; i < ts.length; i++) {
                System.out.println(" - " + ts[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        UIManager.getInstance().setThemeProps(r.getTheme("LWUITDefault"));
        if (com.pub.ha.Log.DBG) System.out.println("UIMidlet.UIMidlet() THME = " + Display.getInstance().getCurrent());
    }

    private void initCmds() {
        if (splash == null) splash = new SpashView();
        if (cmdList == null) try {
            cmdList = new Command("Lister", UITools.getImg("/drawable/list_all.png"));
            cmdFilter = new Command("Filtrer", UITools.getImg("/drawable/list_filter.png"));
            cmdBack = new Command("Retour", UITools.getImg("/drawable/list_back_home.png"));
            cmdExit = new Command("Quitter", UITools.getImg("/drawable/exit2.png"));
            cmdAbout = new Command("A propos", UITools.getImg("/drawable/about2.png"));
        } catch (Exception e) {
            if (com.pub.ha.Log.DBG) System.out.println("UIMidlet.UIMidlet() ERR 1 ");
            if (com.pub.ha.Log.DBG) e.printStackTrace();
        }
    }

    protected void startApp() throws MIDletStateChangeException {
        try {
            if (main == null) main = new MainView();
            initCmds();
            splash.show();
            if (com.pub.ha.Log.DBG) System.out.println(">> UIMidlet.startApp() W = " + main.getWidth() + " H =" + main.getHeight());
            if (com.pub.ha.Log.DBG) System.out.println(">> UIMidlet.startApp()  W.r  =" + com.pub.ha.ui.UITools.getRatio(true) + " H.r =" + UITools.getRatio(false));
            UITools.getRatio(true);
            if (list == null) list = new ListView(this);
            main.addCommand(cmdExit, 0);
            main.addCommand(cmdAbout, 1);
            main.addCommand(cmdList, 2);
            main.addCommandListener(UIMidlet.this);
            list.addCommand(cmdBack);
            list.addCommand(cmdFilter);
            list.addCommandListener(UIMidlet.this);
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    try {
                        AdditiveDetails.getInstance();
                        if (com.pub.ha.Log.DBG) System.out.println(">UIMidlet.startApp() LOADING DB ... ");
                        AdditiveMngr.getAdditiveMngr(UIMidlet.class.getResourceAsStream("/db.txt"), UIMidlet.this);
                        AdditiveMngr.getAdditiveMngr().getAllAdditives(null);
                        if (com.pub.ha.Log.DBG) System.out.println(">UIMidlet.startApp() LOADING DB DONE! ");
                    } catch (Exception e) {
                        if (com.pub.ha.Log.DBG) System.out.println("UIMidlet:  ERR LOADING DATA");
                        e.printStackTrace();
                    }
                    splash.setVisible(false);
                    main.show();
                    timer.cancel();
                }
            }, 100);
            if (com.pub.ha.Log.DBG) System.out.println("UIMidlet:  ALL DONE");
        } catch (Exception e) {
            if (com.pub.ha.Log.DBG) System.out.println("UIMidlet.UIMidlet() ERR 3 ");
            if (com.pub.ha.Log.DBG) e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getCommand() == cmdExit) {
            notifyDestroyed();
        } else if (evt.getCommand() == cmdList) {
            final FilterDBX fdbx = new FilterDBX();
            fdbx.show(new Runnable() {

                public void run() {
                    new Timer().schedule(new TimerTask() {

                        public void run() {
                            main.setTransitionInAnimator(CommonTransitions.createFade(500));
                            int s = fdbx.getChoice();
                            ListModel model = new DefaultListModel(AdditiveMngr.getAdditiveMngr().getAllAdditives(new AdditiveMngr.StatusFilter(s)));
                            list.setListModel(model);
                            list.show();
                            cancel();
                        }
                    }, 250);
                }
            });
        } else if (evt.getCommand() == cmdBack) {
            main.show();
        } else if (evt.getCommand() == cmdFilter) {
            final FilterDBX fdbx = new FilterDBX();
            fdbx.show(null);
            ListModel model = new DefaultListModel(AdditiveMngr.getAdditiveMngr().getAllAdditives(new AdditiveMngr.StatusFilter(fdbx.getChoice())));
            list.setListModel(model);
            list.layoutContainer();
            list.repaint();
        } else if (cmdAbout == evt.getCommand()) {
            Dialog.show("A propos", "E-Halal (J2ME)\nVersion:1.0\nDev: tyazid@gmail.com\n", Dialog.TYPE_INFO, UITools.getImg("/drawable/about2.png"), "Retour", null);
        } else {
            if (list.isOwnerList(evt.getSource())) {
                main.setAdditive(list.getSelectedItem());
                main.show();
            }
        }
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        try {
            AdditiveMngr.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void pauseApp() {
        try {
            AdditiveMngr.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (main != null) main.removeAllCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (list != null) list.removeAllCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
