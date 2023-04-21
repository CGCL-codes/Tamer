package org.mmt.gui.recording;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.mmt.core.DataRecorder;

public class RecordingPanel extends JPanel implements ChangeListener, ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private DecimalFormat formatter = new DecimalFormat("#0.##");

    private Set<String> selectedCharts = new HashSet<String>();

    private String servergroup;

    private JToggleButton record = new JToggleButton("Record charts data");

    private JToggleButton recordProcessList = new JToggleButton("Record also processlists");

    private JLabel chartDataDiskUsage = new JLabel();

    private JLabel processlistDataDiskUsage = new JLabel();

    private JLabel totalDataDiskUsage = new JLabel();

    private JButton refreshStatistics = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/refresh_small.png")));

    private JButton deleteFiles = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/delete_small.png")));

    private JButton chooseChartsToPlot = new JButton("Choose data to plot", new ImageIcon(ClassLoader.getSystemResource("resources/charts.gif")));

    private JButton refreshPlottedData = new JButton("Refresh plotted charts", new ImageIcon(ClassLoader.getSystemResource("resources/refresh_charts.png")));

    private ShowRecordedPanel plottedChart;

    private JPanel loadingPanel = new JPanel();

    public RecordingPanel(String servergroup) {
        this.servergroup = servergroup;
        formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(getLocale()));
        loadingPanel.setLayout(new GridBagLayout());
        JPanel loadingContainer = new JPanel();
        loadingContainer.setLayout(new BoxLayout(loadingContainer, BoxLayout.Y_AXIS));
        loadingContainer.add(new JLabel("Plotting requested data, please wait..."));
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        loadingContainer.add(bar);
        loadingPanel.add(loadingContainer);
        record.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/play.png")));
        record.addChangeListener(this);
        recordProcessList.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/play.png")));
        recordProcessList.setVisible(false);
        recordProcessList.addChangeListener(this);
        JPanel controls = new JPanel();
        controls.add(record);
        controls.add(recordProcessList);
        controls.setBorder(BorderFactory.createTitledBorder("Chart data recording settings."));
        JPanel statistics = new JPanel();
        refreshDiskUsageStatistics();
        statistics.setLayout(new GridLayout(3, 2));
        statistics.add(new JLabel("Chart data: "));
        statistics.add(chartDataDiskUsage);
        statistics.add(new JLabel("Processlists: "));
        statistics.add(processlistDataDiskUsage);
        statistics.add(new JLabel("Total data: "));
        statistics.add(totalDataDiskUsage);
        refreshStatistics.setToolTipText("Refresh data usage statistics");
        deleteFiles.setToolTipText("Delete data recorded");
        refreshStatistics.addActionListener(this);
        deleteFiles.addActionListener(this);
        JPanel statisticsControl = new JPanel();
        statisticsControl.add(refreshStatistics);
        statisticsControl.add(deleteFiles);
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BorderLayout());
        statisticsPanel.add(statistics, BorderLayout.CENTER);
        statisticsPanel.add(statisticsControl, BorderLayout.SOUTH);
        statisticsPanel.setBorder(BorderFactory.createTitledBorder("Disk usage statistics"));
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(controls, BorderLayout.CENTER);
        northPanel.add(statisticsPanel, BorderLayout.EAST);
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        chooseChartsToPlot.addActionListener(this);
        refreshPlottedData.addActionListener(this);
        refreshPlottedData.setEnabled(false);
        JPanel buttons = new JPanel();
        buttons.add(chooseChartsToPlot);
        buttons.add(refreshPlottedData);
        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        south.add(buttons, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
    }

    private void refreshDiskUsageStatistics() {
        long chart = DataRecorder.getInstance().getChartsDataDiskUsageStatistics();
        long processlist = DataRecorder.getInstance().getProcessListsDiskUsageStatistics();
        long total = chart + processlist;
        chartDataDiskUsage.setText("<html><b>" + formatFileSize(chart) + "</b></html>");
        processlistDataDiskUsage.setText("<html><b>" + formatFileSize(processlist) + "</b></html>");
        totalDataDiskUsage.setText("<html><b>" + formatFileSize(total) + "</b></html>");
    }

    private String formatFileSize(long size) {
        String[] suffix = new String[] { "byte", "Kb", "Mb", "Gb", "Tb" };
        int index = 0;
        double tempSize = size;
        while (tempSize > 1024 && index < suffix.length) {
            tempSize /= 1024;
            index++;
        }
        return formatter.format(tempSize) + " " + suffix[index];
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == record) {
            if (record.isSelected()) {
                record.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/stop.png")));
                record.setText("Stop recording chart data");
                DataRecorder.getInstance().startRecording();
                recordProcessList.setVisible(true);
            } else {
                record.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/play.png")));
                record.setText("Record charts data");
                DataRecorder.getInstance().stopRecording();
                recordProcessList.setVisible(false);
            }
        } else if (e.getSource() == recordProcessList) {
            if (recordProcessList.isSelected()) {
                recordProcessList.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/stop.png")));
                recordProcessList.setText("Stop recording also processlists");
                DataRecorder.getInstance().setRecordProcessLists(true);
            } else {
                recordProcessList.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/play.png")));
                recordProcessList.setText("Record also processlists");
                DataRecorder.getInstance().setRecordProcessLists(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshStatistics) {
            refreshDiskUsageStatistics();
        } else if (e.getSource() == deleteFiles) {
            int res = JOptionPane.showConfirmDialog(this, "This action will delete all the data recorded for this server group.\nDo you want to continue?", "Pay attenction!", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                DataRecorder.getInstance().deleteRecordedData();
                refreshDiskUsageStatistics();
            }
        } else if (e.getSource() == chooseChartsToPlot) {
            String[] availableCharts = DataRecorder.getInstance().getAvailableChartData();
            if (availableCharts == null) {
                JOptionPane.showMessageDialog(this, "No chart data available!\nRecord data first!", "Attention!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ChartCoosingDialog dialog = new ChartCoosingDialog(availableCharts, selectedCharts);
            dialog.setVisible(true);
            if (!dialog.hasCancelled()) {
                selectedCharts = dialog.getSelectedCharts();
                if (selectedCharts.size() > 0) drawPlot(); else JOptionPane.showMessageDialog(this, "No chart selected, no data plotted!");
            }
        } else if (e.getSource() == refreshPlottedData) {
            if (selectedCharts.size() > 0) drawPlot();
        }
    }

    private void drawPlot() {
        chooseChartsToPlot.setEnabled(false);
        refreshPlottedData.setEnabled(false);
        if (plottedChart != null) remove(plottedChart);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private ShowRecordedPanel panel;

            @Override
            protected Void doInBackground() throws Exception {
                String[] files = new String[selectedCharts.size()];
                int index = 0;
                for (String chart : selectedCharts) {
                    files[index] = chart + ".rrd";
                    index++;
                }
                panel = new ShowRecordedPanel(files, servergroup);
                return null;
            }

            @Override
            protected void done() {
                remove(loadingPanel);
                plottedChart = panel;
                add(plottedChart, BorderLayout.CENTER);
                revalidate();
                repaint();
                chooseChartsToPlot.setEnabled(true);
                refreshPlottedData.setEnabled(true);
            }
        };
        worker.execute();
    }
}
