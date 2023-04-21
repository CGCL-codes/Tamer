package com.od.jtimeseries.ui.visualizer.download;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.net.udp.TimeSeriesServerDictionary;
import com.od.jtimeseries.ui.visualizer.displaypattern.DisplayNameCalculator;
import com.od.jtimeseries.ui.visualizer.selector.SeriesSelectionPanel;
import com.od.jtimeseries.ui.util.ImageUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* Created by IntelliJ IDEA.
* User: nick
* Date: 24-May-2009
* Time: 22:29:22
*
* Create and show a dialog to download series
*/
public class ShowDownloadSeriesDialogAction extends AbstractAction {

    private SeriesSelectionPanel seriesSelectionPanel;

    private TimeSeriesContext contextToReceiveSeries;

    private TimeSeriesServerDictionary serverDictionary;

    private DisplayNameCalculator displayNameCalculator;

    private JComponent componentForDialogPositioning;

    public ShowDownloadSeriesDialogAction(SeriesSelectionPanel seriesSelectionPanel, TimeSeriesContext contextToReceiveSeries, TimeSeriesServerDictionary serverDictionary, DisplayNameCalculator displayNameCalculator, JComponent componentForDialogPositioning) {
        super("Download Series", ImageUtils.DOWNLOAD_16x16);
        this.seriesSelectionPanel = seriesSelectionPanel;
        this.contextToReceiveSeries = contextToReceiveSeries;
        this.serverDictionary = serverDictionary;
        this.displayNameCalculator = displayNameCalculator;
        this.componentForDialogPositioning = componentForDialogPositioning;
    }

    public void actionPerformed(ActionEvent e) {
        DownloadRemoteSeriesDialog d = new DownloadRemoteSeriesDialog(contextToReceiveSeries, serverDictionary, displayNameCalculator, componentForDialogPositioning);
        d.setVisible(true);
        d.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                seriesSelectionPanel.refresh();
            }
        });
    }
}
