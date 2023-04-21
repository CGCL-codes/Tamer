package pl.otros.logview.gui.actions;

import pl.otros.logview.gui.*;
import pl.otros.logview.gui.table.TableColumns;
import pl.otros.logview.logging.GuiJulHandler;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public class ShowOlvLogs extends OtrosAction {

    private LogViewPanelWrapper logViewPanelWrapper;

    public ShowOlvLogs(OtrosApplication otrosApplication) {
        super(otrosApplication);
        putValue(NAME, "Show internal logs");
        putValue(SHORT_DESCRIPTION, "Show internal OLV logs");
        putValue(SMALL_ICON, Icons.LEVEL_INFO);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StatusObserver observer = getOtrosApplication().getStatusObserver();
        if (logViewPanelWrapper == null) {
            LogDataTableModel dataTableModel = new LogDataTableModel();
            dataTableModel.setDataLimit(10000);
            logViewPanelWrapper = new LogViewPanelWrapper("Olv logs", null, TableColumns.JUL_COLUMNS, dataTableModel, getOtrosApplication());
            logViewPanelWrapper.goToLiveMode();
            logViewPanelWrapper.addHierarchyListener(new HierarchyListener() {

                @Override
                public void hierarchyChanged(HierarchyEvent e) {
                    if (e.getChangeFlags() == 1 && e.getChanged().getParent() == null) {
                        GuiJulHandler.stop();
                    }
                }
            });
        }
        JTabbedPane tabbedPane = getOtrosApplication().getJTabbedPane();
        if (tabbedPane.indexOfComponent(logViewPanelWrapper) == -1) {
            int tabCount = tabbedPane.getTabCount();
            tabbedPane.addTab(null, Icons.WRENCH, logViewPanelWrapper);
            tabbedPane.setTabComponentAt(tabCount, new TabHeader(tabbedPane, "OLV internal Logs", Icons.LEVEL_INFO, "OLV internal logs"));
            tabbedPane.setSelectedIndex(tabCount);
        } else {
            tabbedPane.setSelectedComponent(logViewPanelWrapper);
        }
        GuiJulHandler.start(logViewPanelWrapper.getDataTableModel(), logViewPanelWrapper.getConfiguration());
    }
}
