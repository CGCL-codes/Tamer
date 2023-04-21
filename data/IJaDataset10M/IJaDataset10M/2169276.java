package org.opu.db_vdumper.ui;

import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.Map;
import javax.swing.JFrame;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.util.Direction;
import org.opu.db_vdumper.actions.db.DbAction;
import org.opu.db_vdumper.ui.component.area.model.CanvesModel;

/**
 *  Main View is one for whole application, and has some controls to control
 * idw-docking window settings.
 * 
 * @author yura
 */
public interface MainView extends View {

    public static final String TREE_TABLE_SCEMA_VIEW = "Tree";

    public static final String GRAPH_TABLE_SCEMA_VIEW = "Graph";

    public static final String SQL_QUERY_EDITOR_VIEW = "Editor";

    public static final String DB_CONNECTION_PROP_VIEW = "DbProp";

    public static final String SQL_REQUEST_DYNAMIC_VIEW = "Request";

    public static final String SQL_REQUEST_PROPERTIES_DYNAMIC_VIEW = "RqstProp";

    /**
     * Sets the docking windows theme.
     *
     * @param theme the docking windows theme
     */
    public void setTheme(DockingWindowsTheme theme);

    /**
     *  Return current view theme
     *
     * @return  current view theme
     */
    public DockingWindowsTheme getTheme();

    /**
     *  Set enable/disable of close button on tabs views
     *
     * @param enable
     */
    public void setCloseButtonEnable(boolean enable);

    /**
     *  Return is close button enable (visible)
     *
     * @return is close button enable (visible)
     */
    public boolean isCloseButtonEnable();

    /**
     *  Set Freeze layout on main panel
     *
     * @param freeze
     */
    public void setFreezeLayout(boolean freeze);

    /**
     *  Return is freeze layout on main panel
     *
     * @return  is freeze layout on main panel
     */
    public boolean isFreezeLayout();

    /**
     *  Enable/disable the window bar
     * 
     * @param direction what bar enabled/disabled
     * @param enable what to do
     */
    public void setToogleEnabled(Direction direction, boolean enable);

    /**
     *  Return true if the window bar enable
     *
     * @return true if the window bar enable
     */
    public boolean isToogleEnabled(Direction direction);

    /**
     *  replace enabeling of th window bar (enable=>disable, disable=>enale)
     *
     * @param direction  what bar enabled/disabled
     */
    public void replaceToogleEnabled(Direction direction);

    /**
     * Update view menu items and dynamic view map: added/remodve view from
     * panel, depends of "added" value
     *
     * @param window the window in which to search for views
     * @param added if true the window was added
     */
    public void updateViews(DockingWindow window, boolean added);

    /**
     *  Set table views model (for graph view, tree view)
     *
     * @param model table views model
     */
    public void setCanvesModel(CanvesModel model);

    public void setDbProp(Map<String, String> properties);

    /**
     * 
     * <ul><b>Restore view:</b>
     *  <li>{@link #TREE_TABLE_SCEMA_VIEW}</li>
     *  <li>{@link #GRAPH_TABLE_SCEMA_VIEW}</li>
     *  <li>{@link #SQL_QUERY_EDITOR_VIEW}</li>
     *  <li>{@link #DB_CONNECTION_PROP_VIEW}</li>
     * </ul>
     * @param view
     */
    public void restore(String viewId);

    public void addWindowListener(WindowListener wl);

    public TextResource getTextResource();

    public void setDbActions(Collection<DbAction> actions);

    public JFrame asFrame();
}
