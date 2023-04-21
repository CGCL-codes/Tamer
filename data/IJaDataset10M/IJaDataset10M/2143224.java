package org.vikamine.gui.subgroup;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.vikamine.app.BasicComponentManager;
import org.vikamine.app.VIKAMINE;
import org.vikamine.app.plugin.ShownPluginComponentAdapter;
import org.vikamine.gui.subgroup.debugger.DebuggerController;
import org.vikamine.gui.subgroup.editors.zoomtable.CommonZoomTablesController;
import org.vikamine.gui.util.docking.DefaultDockable;
import org.vikamine.gui.util.docking.DockPanel;
import org.vikamine.gui.util.docking.Dockable;
import org.vikamine.gui.util.docking.DockedComponent;
import de.d3web.ka.gui.KnowMELookAndFeel;
import de.d3web.ka.gui.ResourceBundleManager;
import de.d3web.ka.ui.UIManager;

/**
 * @author Atzmueller
 * 
 */
public class SDBasicComponentPanel extends JPanel {

    private static final long serialVersionUID = 7800850668031098167L;

    public static String SD_BASIC_PLUGIN_TITLE = VIKAMINE.I18N.getString("vikamine.basicSubgroupPlugin.pluginTitle");

    private static Icon SD_BASIC_PLUGIN_ICON = ResourceBundleManager.getInstance().getIcon(BasicComponentManager.class, "resources/images/SDPlugin.gif");

    private static String SD_BASIC_PLUGIN_MENUBAR_PATH = "views.standard.SubgroupEditor";

    static class DividerLocationSaver extends WindowAdapter {

        private JSplitPane splitPane;

        DividerLocationSaver(JSplitPane split) {
            splitPane = split;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            String key = KEY_DIVIDER_LOCATION + splitPane.getName();
            Preferences pref = Preferences.userNodeForPackage(getClass());
            pref.putInt(key, splitPane.getDividerLocation());
        }
    }

    private static final String KEY_DIVIDER_LOCATION = "dividerLocation_";

    private JSplitPane outerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    private JSplitPane innerMiddleSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private DockPanel currentSubgroupRelatedInformationDock = new DockPanel();

    private DockPanel currentSubgroupDock = new DockPanel();

    private DockedComponent overviewZoomDockedComponent;

    private DockedComponent sortedZoomDockedComponent;

    private static SDBasicComponentPanel instance;

    private boolean debuggerComponentsShown = false;

    private final DockPanel zoomTableDock;

    private boolean isSorted;

    private SDBasicComponentPanel() {
        zoomTableDock = new DockPanel();
        init();
        setBorder(KnowMELookAndFeel.getShadowBorder());
    }

    public static synchronized SDBasicComponentPanel getInstance() {
        if (instance == null) instance = new SDBasicComponentPanel();
        return instance;
    }

    private void init() {
        initGui();
        setLayout(new BorderLayout());
        add(outerSplitPane, BorderLayout.CENTER);
    }

    public void addDebuggerComponentsToSubgroupInformationPanel() {
        if (!areDebuggerComponentsShown()) {
            DebuggerController dbg = AllSubgroupPluginController.getInstance().getDebuggerController();
            String bpTitle = VIKAMINE.I18N.getString("vikamine.debugger.title.breakpoints");
            DefaultDockable dockable = new DefaultDockable(dbg.getBreakpointList().getComponent(), bpTitle, false);
            dockable.setRemoveableByClosing(false);
            currentSubgroupRelatedInformationDock.addDockable(dockable);
            String haltTitle = VIKAMINE.I18N.getString("vikamine.debugger.title.variables");
            dockable = new DefaultDockable(dbg.getVariablesView().getComponent(), haltTitle, false);
            dockable.setRemoveableByClosing(false);
            currentSubgroupRelatedInformationDock.addDockable(dockable);
            String debuggersTitle = VIKAMINE.I18N.getString("vikamine.debugger.title.debuggers");
            dockable = new DefaultDockable(dbg.getDebuggerList().getComponent(), debuggersTitle, false);
            dockable.setRemoveableByClosing(false);
            currentSubgroupRelatedInformationDock.addDockable(dockable);
            debuggerComponentsShown = true;
        }
    }

    public boolean areDebuggerComponentsShown() {
        return debuggerComponentsShown;
    }

    private void initGui() {
        AllSubgroupPluginController.getInstance().initialize();
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(innerMiddleSplitPane, BorderLayout.CENTER);
        middlePanel.add(AllSubgroupPluginController.getInstance().getSGQualityBar().getComponent(), BorderLayout.SOUTH);
        outerSplitPane.setTopComponent(middlePanel);
        innerMiddleSplitPane.setLeftComponent(currentSubgroupDock);
        innerMiddleSplitPane.setRightComponent(currentSubgroupRelatedInformationDock);
        String title = VIKAMINE.I18N.getString("vikamine.currentSG.title");
        DefaultDockable dockable = new DefaultDockable(AllSubgroupPluginController.getInstance().getSubgroupTreeController().getSubgroupComponent(), title, false);
        dockable.setRemoveableByClosing(false);
        currentSubgroupDock.addDockable(dockable);
        String statTitle = VIKAMINE.I18N.getString("vikamine.currentSG.statPanel.title");
        dockable = new DefaultDockable(AllSubgroupPluginController.getInstance().getSubgroupStatPanelController().getComponent(), statTitle, false);
        dockable.setRemoveableByClosing(false);
        currentSubgroupRelatedInformationDock.addDockable(dockable);
        String overviewTitle = VIKAMINE.I18N.getString("vikamine.zoomtable.title.overview");
        dockable = new DefaultDockable(AllSubgroupPluginController.getInstance().getZoomController().getOverviewZoomController().getZoomComponent(), overviewTitle, false);
        dockable.setRemoveableByClosing(false);
        overviewZoomDockedComponent = new DockedComponent(dockable);
        zoomTableDock.addDockable(overviewZoomDockedComponent);
        String sortedTitle = VIKAMINE.I18N.getString("vikamine.zoomtable.title.sorted");
        dockable = new DefaultDockable(AllSubgroupPluginController.getInstance().getZoomController().getSortedZoomController().getZoomComponent(), sortedTitle, false);
        sortedZoomDockedComponent = new DockedComponent(dockable);
        dockable.setRemoveableByClosing(false);
        isSorted = false;
        outerSplitPane.setBottomComponent(zoomTableDock);
        outerSplitPane.setOneTouchExpandable(true);
        outerSplitPane.setName(VIKAMINE.I18N.getString("vikamine.basicSubgroupPlugin.sdTab"));
        innerMiddleSplitPane.setName("innerMiddleSplitPane");
        if (UIManager.getInstance().getApplicationMainFrame() != null) {
            UIManager.getInstance().getApplicationMainFrame().addWindowListener(new DividerLocationSaver(outerSplitPane));
            UIManager.getInstance().getApplicationMainFrame().addWindowListener(new DividerLocationSaver(innerMiddleSplitPane));
        }
        outerSplitPane.setResizeWeight(0.66);
        innerMiddleSplitPane.setResizeWeight(0.33);
        outerSplitPane.setDividerSize(10);
        innerMiddleSplitPane.setDividerSize(10);
        outerSplitPane.setContinuousLayout(true);
        innerMiddleSplitPane.setContinuousLayout(true);
        loadDividerLocation(outerSplitPane, 300);
        loadDividerLocation(innerMiddleSplitPane, 300);
    }

    public void toogleSortedZoomTable() {
        if (isSorted) {
            try {
                zoomTableDock.removeDockable(sortedZoomDockedComponent);
            } catch (ArrayIndexOutOfBoundsException e) {
                return;
            }
        } else {
            zoomTableDock.addDockable(sortedZoomDockedComponent);
            zoomTableDock.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    int index = pane.getSelectedIndex();
                    if (index > 0) {
                        DockedComponent docked = zoomTableDock.getModel().getElementAt(index);
                        if ((docked == overviewZoomDockedComponent) || (docked == sortedZoomDockedComponent)) {
                            if (docked == overviewZoomDockedComponent) {
                                AllSubgroupPluginController.getInstance().getZoomController().activateTreeModelOnChangeEvent(CommonZoomTablesController.OVERVIEW_ZOOM_CONTROLLER_INDEX);
                            } else {
                                AllSubgroupPluginController.getInstance().getZoomController().activateTreeModelOnChangeEvent(CommonZoomTablesController.SORTED_ZOOM_CONTROLLER_INDEX);
                            }
                        }
                    }
                }
            });
        }
        isSorted = !isSorted;
    }

    public boolean isSortedZoomTable() {
        return isSorted;
    }

    /**
     * @param outerSplitPane2
     */
    private void loadDividerLocation(JSplitPane split, int defaultPos) {
        Preferences pref = Preferences.userNodeForPackage(getClass());
        String key = KEY_DIVIDER_LOCATION + split.getName();
        int pos = pref.getInt(key, defaultPos);
        split.setDividerLocation(pos);
    }

    private DockPanel getCurrentSubgroupRelatedInformationDock() {
        return currentSubgroupRelatedInformationDock;
    }

    public void addMiddleRightDockable(Dockable newDockable) {
        if (newDockable != null) {
            getCurrentSubgroupRelatedInformationDock().addDockable(newDockable);
        }
    }

    public ShownPluginComponentAdapter createShownPluginComponentAdapter() {
        return new ShownPluginComponentAdapter(this, SD_BASIC_PLUGIN_TITLE, SD_BASIC_PLUGIN_TITLE, SD_BASIC_PLUGIN_ICON, SD_BASIC_PLUGIN_MENUBAR_PATH);
    }
}
