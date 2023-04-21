package com.od.jtimeseries.ui.visualizer.download.panel;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.ui.visualizer.selector.SeriesSelectionPanel;
import com.od.jtimeseries.ui.visualizer.selector.table.ColumnSettings;
import com.od.jtimeseries.ui.visualizer.selector.table.FixedColumns;
import com.od.jtimeseries.ui.util.ImageUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 24-May-2009
 * Time: 22:33:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectRemoteSeriesPanel extends AbstractDownloadWizardPanel {

    private SeriesSelectionPanel seriesSelectionPanel;

    public SelectRemoteSeriesPanel(WizardPanelListener panelListener, TimeSeriesContext timeSeriesContext) {
        super(panelListener);
        Box titlePanel = createTitlePanel("Select series to import (" + timeSeriesContext.findAllTimeSeries().getNumberOfMatches() + " series found)");
        JComponent seriesSelector = createSeriesSelector(timeSeriesContext);
        Box buttonPanel = createButtonPanel();
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(seriesSelector, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Box createButtonPanel() {
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        JButton addButton = new JButton("Add Selected Series");
        addButton.setIcon(ImageUtils.DOWNLOAD_16x16);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getPanelListener().seriesSelected(seriesSelectionPanel.getSelectedTimeSeries());
            }
        });
        buttonBox.add(addButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setIcon(ImageUtils.CANCEL_16x16);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getPanelListener().downloadCancelled();
            }
        });
        buttonBox.add(cancelButton);
        return buttonBox;
    }

    private JComponent createSeriesSelector(TimeSeriesContext timeSeriesContext) {
        seriesSelectionPanel = new SeriesSelectionPanel(timeSeriesContext);
        java.util.List<ColumnSettings> defaultColumnSettings = getDefaultColumnSettings();
        seriesSelectionPanel.setColumns(defaultColumnSettings);
        seriesSelectionPanel.addAllDynamicColumns();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(seriesSelectionPanel);
        splitPane.setRightComponent(seriesSelectionPanel.getSelectionList());
        splitPane.setDividerLocation((PANEL_WIDTH * 2) / 3);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.7d);
        return splitPane;
    }

    private List<ColumnSettings> getDefaultColumnSettings() {
        List<ColumnSettings> columns = new ArrayList<ColumnSettings>();
        FixedColumns.addFixedColumn(columns, FixedColumns.Selected);
        FixedColumns.addFixedColumn(columns, FixedColumns.DisplayName);
        return columns;
    }
}
