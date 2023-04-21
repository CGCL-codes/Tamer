package net.sourceforge.mapcraft;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

/**
 * This class keeps track of all the major actions used by the editor.
 * It loads the icons for the buttons and menu items, and should be
 * called when setting up toolbars and menus.
 */
public class Actions {

    private static Hashtable actions;

    private String path = "toolbar/";

    private ActionListener globalListener;

    public static final String FILE_NEW = "file.new";

    public static final String FILE_OPEN = "file.open";

    public static final String FILE_SAVE = "file.save";

    public static final String FILE_SAVEAS = "file.saveas";

    public static final String FILE_PRINT = "file.print";

    public static final String FILE_EXIT = "file.exit";

    public static final String FILE_CONNECT = "file.connect";

    public static final String VIEW_ZOOMIN = "view.zoomin";

    public static final String VIEW_ZOOMOUT = "view.zoomout";

    public static final String VIEW_USMALL = "view.usmall";

    public static final String VIEW_XXSMALL = "view.xxsmall";

    public static final String VIEW_XSMALL = "view.xsmall";

    public static final String VIEW_SMALL = "view.small";

    public static final String VIEW_MEDIUM = "view.medium";

    public static final String VIEW_LARGE = "view.large";

    public static final String VIEW_XLARGE = "view.xlarge";

    public static final String VIEW_XXLARGE = "view.xxlarges";

    public static final String VIEW_SHOWTERRAIN = "view.showterrain";

    public static final String VIEW_SHOWFEATURES = "view.showfeatures";

    public static final String VIEW_SHOWTHINGS = "view.things";

    public static final String VIEW_SHOWAREAS = "view.areas";

    public static final String VIEW_GRID = "view.grid";

    public static final String VIEW_LARGEGRID = "view.largegrid";

    public static final String EDIT_TERRAIN = "edit.terrain";

    public static final String EDIT_FEATURES = "edit.features";

    public static final String EDIT_THINGS = "edit.things";

    public static final String EDIT_RIVERS = "edit.rivers";

    public static final String EDIT_AREAS = "edit.areas";

    public static final String EDIT_ROADS = "edit.roads";

    public static final String EDIT_HIGHLIGHT = "edit.highlight";

    public static final String EDIT_SMALL = "edit.small";

    public static final String EDIT_MEDIUM = "edit.medium";

    public static final String EDIT_LARGE = "edit.large";

    public static final String EDIT_SELECT = "edit.select";

    public static final String EDIT_NEW = "edit.new";

    public static final String EDIT_EDIT = "edit.edit";

    public static final String EDIT_DELETE = "edit.delete";

    public static final String EDIT_INSERT = "edit.insert";

    public static final String TOOL_CROP = "tool.crop";

    public static final String TOOL_RESCALE = "tool.rescale";

    public static final String TOOL_RESIZE = "tool.resize";

    public static final String TOOL_MERGE = "tool.merge";

    public static final String TOOL_EDITAREAS = "tool.editareas";

    public class Actor extends AbstractAction {

        private ActionListener listener;

        public Actor(String name, String label) {
            putValue(Action.NAME, label);
            putValue(Action.ACTION_COMMAND_KEY, name);
        }

        public Actor(String name, String label, String image) {
            URL url = Actions.class.getResource("/" + image);
            ImageIcon icon = null;
            if (url != null) {
                icon = new ImageIcon(url);
            }
            putValue(Action.NAME, label);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.ACTION_COMMAND_KEY, name);
        }

        public void addListener(ActionListener listener) {
            this.listener = listener;
        }

        public void actionPerformed(ActionEvent e) {
            if (listener != null) {
                listener.actionPerformed(e);
            } else {
                globalListener.actionPerformed(e);
            }
        }
    }

    public Actions(ActionListener listener) {
        Action action;
        if (listener != null) {
            this.globalListener = listener;
        }
        if (actions == null) {
            actions = new Hashtable();
            add(FILE_NEW, "New map", "general/New");
            add(FILE_OPEN, "Open...", "general/Open");
            add(FILE_CONNECT, "Connect...", "general/Open");
            add(FILE_SAVE, "Save", "general/Save");
            add(FILE_SAVEAS, "Save As...", "general/SaveAs");
            add(FILE_EXIT, "Exit", "general/Stop");
            add(VIEW_ZOOMIN, "Zoom in", "general/ZoomIn");
            add(VIEW_ZOOMOUT, "Zoom out", "general/ZoomOut");
            addLocal(VIEW_SHOWTERRAIN, "Terrain palette", "terrain");
            addLocal(VIEW_SHOWTHINGS, "Thing palette", "things");
            addLocal(VIEW_SHOWFEATURES, "Features palette", "features");
            addLocal(VIEW_SHOWAREAS, "Areas palette", "terrain");
            addLocal(VIEW_GRID, "Toggle grid", "grid");
            addLocal(VIEW_LARGEGRID, "Toggle rulers", "biggrid");
            add(VIEW_XXSMALL, "XX-Small");
            add(VIEW_XSMALL, "X-Small");
            add(VIEW_SMALL, "Small");
            add(VIEW_MEDIUM, "Medium");
            add(VIEW_LARGE, "Large");
            add(VIEW_XLARGE, "X-Large");
            add(VIEW_XXLARGE, "XX-Large");
            addLocal(EDIT_TERRAIN, "Terrain brush", "terrain");
            addLocal(EDIT_FEATURES, "Features brush", "features");
            addLocal(EDIT_AREAS, "Area brush", "terrain");
            addLocal(EDIT_RIVERS, "Rivers brush", "rivers");
            addLocal(EDIT_THINGS, "Things brush", "things");
            addLocal(EDIT_ROADS, "Roads brush", "roads");
            addLocal(EDIT_HIGHLIGHT, "Highlight brush", "highlight");
            addLocal(EDIT_SMALL, "Small brush", "small-brush");
            addLocal(EDIT_MEDIUM, "Medium brush", "medium-brush");
            addLocal(EDIT_LARGE, "Large brush", "large-brush");
            addLocal(EDIT_SELECT, "Select", "select");
            addLocal(EDIT_NEW, "New", "new");
            addLocal(EDIT_INSERT, "Insert", "insert");
            addLocal(EDIT_EDIT, "Edit", "edit");
            addLocal(EDIT_DELETE, "Delete", "delete");
            addLocal(TOOL_CROP, "Crop...", "crop");
            addLocal(TOOL_RESCALE, "Rescale...", "rescale");
            addLocal(TOOL_RESIZE, "Resize...", "resize");
            addLocal(TOOL_MERGE, "Merge maps...", "merge");
            addLocal(TOOL_EDITAREAS, "Edit areas...", "editareas");
        }
    }

    private void add(String name, String label) {
        Action action = new Actor(name, label);
        actions.put(name, action);
    }

    private void add(String name, String label, String image) {
        Action action = new Actor(name, label, path + image + "24.gif");
        actions.put(name, action);
    }

    private void addLocal(String name, String label, String image) {
        Action action = new Actor(name, label, "mapcraft/" + image + ".png");
        actions.put(name, action);
    }

    public Action get(String name) {
        Actor actor = (Actor) actions.get(name);
        return (Action) actor;
    }

    public Action get(String name, ActionListener listener) {
        Actor actor = (Actor) actions.get(name);
        actor.addListener(listener);
        return (Action) actor;
    }
}
