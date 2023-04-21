package org.phylowidget.ui;

import java.util.HashMap;
import org.andrewberman.ui.UIEvent;
import org.andrewberman.ui.ifaces.UIListener;
import org.andrewberman.ui.menu.Menu;
import org.andrewberman.ui.menu.MenuItem;
import org.andrewberman.ui.unsorted.DelayedAction;
import org.andrewberman.ui.unsorted.JSCaller;
import org.andrewberman.ui.unsorted.Json;
import org.phylowidget.PWContext;
import org.phylowidget.PWPlatform;
import org.phylowidget.TreeManager;
import processing.core.PApplet;

public class Callback extends Menu implements UIListener {

    private PWContext pwc;

    private TreeManager tm;

    private PhyloContextMenu contextMenu;

    private TreeClipboard clipboard;

    private boolean addedClipboardListener;

    private boolean addedContextListener;

    private boolean addedTreeListener;

    private JSCaller caller;

    private DelayedAction action;

    public Callback(PApplet app) {
        super(app);
        caller = new JSCaller(app);
        callbackNeeded = false;
        action = new DelayedAction() {

            @Override
            protected void run() {
                fireJavascriptCallback();
            }
        };
    }

    private boolean callbackNeeded;

    @Override
    public synchronized void draw() {
        super.draw();
        loadUIStuff();
        if (callbackNeeded) {
            if (limitRate) {
                action.trigger(50);
            } else {
                fireJavascriptCallback();
            }
            callbackNeeded = false;
        }
    }

    private void loadUIStuff() {
        if (pwc == null) pwc = PWPlatform.getInstance().getThisAppContext();
        if (tm == null) {
            if (pwc != null) {
                tm = pwc.trees();
            }
        }
        if (tm != null && !addedTreeListener) {
            tm.addListener(this);
            addedTreeListener = true;
        }
        if (contextMenu == null) {
            if (pwc != null) {
                this.contextMenu = pwc.ui().contextMenu;
            }
        }
        if (contextMenu != null && !addedContextListener) {
            contextMenu.addListener(this);
            addedContextListener = true;
        }
        if (clipboard == null) {
            clipboard = pwc.ui().clipboard;
        }
        if (clipboard != null && !addedClipboardListener) {
            clipboard.addListener(this);
            addedClipboardListener = true;
        }
    }

    private boolean limitRate = true;

    public void setLimitRate(boolean limitRate) {
        this.limitRate = limitRate;
    }

    private boolean includeNodeInfo = false;

    public void setIncludeNodeInfo(boolean includeNodeInfo) {
        this.includeNodeInfo = includeNodeInfo;
    }

    private String callbackEvent;

    public void setEvent(String event) {
        this.callbackEvent = event;
    }

    private String callback;

    public void setCallback(String callback) {
        this.callback = callback;
    }

    private boolean isMyCallback(UIEvent e) {
        return isTreeCallback(e) || isGlowCallback(e) || isHoverCallback(e) || isOpenCallback(e) || isClipCallback(e);
    }

    private boolean isTreeCallback(UIEvent e) {
        return (e.getID() == TreeManager.TREE_CHANGE_EVENT && callbackEvent.toLowerCase().contains("tree"));
    }

    private boolean isGlowCallback(UIEvent e) {
        return (e.getID() == PhyloContextMenu.NODE_GLOW_EVENT && callbackEvent.toLowerCase().contains("glow"));
    }

    private boolean isHoverCallback(UIEvent e) {
        return (e.getID() == PhyloContextMenu.NODE_HOVER_EVENT && callbackEvent.toLowerCase().contains("hover"));
    }

    private boolean isOpenCallback(UIEvent e) {
        return (e.getID() == PhyloContextMenu.CONTEXT_OPEN_EVENT && callbackEvent.toLowerCase().contains("open"));
    }

    private boolean isClipCallback(UIEvent e) {
        return (e.getID() == TreeClipboard.CLIPBOARD_UPDATED && callbackEvent.toLowerCase().contains("clip"));
    }

    private void fireJavascriptCallback() {
        try {
            if (caller == null) return;
            if (callback == null) return;
            if (caller.reflectionWorking) {
                if (includeNodeInfo) {
                    HashMap<String, Object> map = pwc.ui().getHoveredNode().getNodeInfo();
                    caller.call(callback, Json.hashToJson(map));
                } else {
                    caller.call(callback);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MenuItem create(String label) {
        return null;
    }

    public void uiEvent(UIEvent e) {
        if (isMyCallback(e)) {
            callbackNeeded = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tm != null) tm.removeListener(this);
        if (contextMenu != null) contextMenu.removeListener(this);
    }
}
