package com.od.jtimeseries.ui.download.panel;

import com.od.jtimeseries.ui.timeseries.UIPropertiesTimeSeries;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 25-May-2009
 * Time: 00:08:07
 * To change this template use File | Settings | File Templates.
 */
public class AbstractDownloadWizardPanel extends JPanel {

    private WizardPanelListener panelListener;

    public static final int PANEL_WIDTH = 800;

    public static final int PANEL_HEIGHT = 800;

    public AbstractDownloadWizardPanel(WizardPanelListener panelListener) {
        this.panelListener = panelListener;
    }

    protected Box createTitlePanel(String title) {
        Box labelPanel = Box.createHorizontalBox();
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(14f).deriveFont(Font.BOLD));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());
        return labelPanel;
    }

    public WizardPanelListener getPanelListener() {
        return panelListener;
    }

    public static interface WizardPanelListener {

        void seriesLoaded();

        void seriesSelected(List<? extends UIPropertiesTimeSeries> selectedTimeSeries);

        void downloadCancelled();
    }
}
