package org.aiotrade.platform.core.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.aiotrade.math.timeseries.descriptor.AnalysisContents;
import org.aiotrade.charting.view.ChartViewContainer;
import org.aiotrade.charting.view.ChartingControllerFactory;
import org.aiotrade.platform.core.analysis.chartview.RealtimeChartViewContainer;
import org.aiotrade.charting.view.ChartingController;
import org.aiotrade.platform.core.sec.Sec;
import org.aiotrade.platform.core.sec.Ticker;
import org.aiotrade.platform.core.sec.TickerObserver;
import org.aiotrade.platform.core.sec.TickerSnapshot;
import org.aiotrade.platform.core.ui.panel.RealtimeBoardPanel.TrendSensitiveCellRenderer;
import org.aiotrade.charting.laf.LookFeel;

/**
 *
 * @author Caoyuan Deng
 */
public class RealtimeBoardPanel extends javax.swing.JPanel implements TickerObserver<TickerSnapshot> {

    private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    private RealtimeChartViewContainer viewContainer;

    private final Ticker previousTicker = new Ticker();

    private DefaultTableModel depthTableModel;

    private DefaultTableModel tickerTableModel;

    private Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);

    /**
     * Creates new form RealtimeBoardPanel
     */
    public RealtimeBoardPanel(Sec sec, AnalysisContents contents) {
        initComponents();
        lastPrice.setOpaque(true);
        dayChange.setOpaque(true);
        percent.setOpaque(true);
        tickerTableModel = (DefaultTableModel) tickerTable.getModel();
        depthTableModel = (DefaultTableModel) depthTable.getModel();
        TableColumnModel columeModel;
        columeModel = depthTable.getColumnModel();
        columeModel.getColumn(0).setMinWidth(12);
        columeModel.getColumn(1).setMinWidth(35);
        columeModel = tickerTable.getColumnModel();
        columeModel.getColumn(0).setMinWidth(22);
        columeModel.getColumn(1).setMinWidth(30);
        tickerTable.setDefaultRenderer(Object.class, new TrendSensitiveCellRenderer());
        ChartingController controller = ChartingControllerFactory.createInstance(sec.getTickerSer(), contents);
        viewContainer = controller.createChartViewContainer(RealtimeChartViewContainer.class, this);
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(viewContainer, BorderLayout.CENTER);
    }

    public void update(TickerSnapshot tickerSnapshot) {
        symbol.setText(tickerSnapshot.getSymbol());
        final Ticker snapshotTicker = tickerSnapshot.readTicker();
        int row = 0;
        depthTableModel.setValueAt("Ask", row, 0);
        depthTableModel.setValueAt(String.valueOf(snapshotTicker.get(Ticker.ASK_PRICE)), row, 1);
        depthTableModel.setValueAt(String.valueOf(snapshotTicker.get(Ticker.ASK_SIZE)), row, 2);
        row = 1;
        depthTableModel.setValueAt("Bid", row, 0);
        depthTableModel.setValueAt(String.valueOf(snapshotTicker.get(Ticker.BID_PRICE)), row, 1);
        depthTableModel.setValueAt(String.valueOf(snapshotTicker.get(Ticker.BID_SIZE)), row, 2);
        calendar.setTimeInMillis(snapshotTicker.getTime());
        time.setText(" " + sdf.format(calendar.getTime()));
        lastPrice.setText(" " + String.valueOf(snapshotTicker.get(Ticker.LAST_PRICE)));
        prevClose.setText(" " + String.valueOf(snapshotTicker.get(Ticker.PREV_CLOSE)));
        dayOpen.setText(" " + String.valueOf(snapshotTicker.get(Ticker.DAY_OPEN)));
        dayHigh.setText(" " + String.valueOf(snapshotTicker.get(Ticker.DAY_HIGH)));
        dayLow.setText(" " + String.valueOf(snapshotTicker.get(Ticker.DAY_LOW)));
        dayVolume.setText(" " + String.valueOf(snapshotTicker.get(Ticker.DAY_VOLUME)));
        dayChange.setText(" " + String.valueOf(snapshotTicker.get(Ticker.DAY_CHANGE)));
        percent.setText(" " + String.format("%+3.2f", snapshotTicker.getChangeInPercent()) + "%");
        Color bgColor = Color.YELLOW;
        if (snapshotTicker.get(Ticker.DAY_CHANGE) > 0) {
            bgColor = LookFeel.getCurrent().getPositiveBgColor();
        } else if (snapshotTicker.get(Ticker.DAY_CHANGE) < 0) {
            bgColor = LookFeel.getCurrent().getNegativeBgColor();
        } else {
            bgColor = Color.YELLOW;
        }
        setBackgroundImmediately(dayChange, bgColor);
        setBackgroundImmediately(percent, bgColor);
        if (snapshotTicker.isDayVolumeChanged(previousTicker)) {
            switch(snapshotTicker.compareLastCloseTo(previousTicker)) {
                case 1:
                    bgColor = LookFeel.getCurrent().getPositiveBgColor();
                    break;
                case 0:
                    bgColor = Color.YELLOW;
                    break;
                case -1:
                    bgColor = LookFeel.getCurrent().getNegativeBgColor();
                    break;
                default:
            }
            setBackgroundImmediately(lastPrice, bgColor);
            Object[] tickerRow = new Object[] { sdf.format(calendar.getTime()), String.format("%5.2f", snapshotTicker.get(Ticker.LAST_PRICE)), (int) (snapshotTicker.get(Ticker.DAY_VOLUME) - previousTicker.get(Ticker.DAY_VOLUME)) };
            tickerTableModel.insertRow(0, tickerRow);
        }
        previousTicker.copy(snapshotTicker);
    }

    /**
     * Need this as setBackgound() may not works immediately
     */
    private void setBackgroundImmediately(JComponent c, Color bgColor) {
        c.setBackground(bgColor);
        if (c.isShowing()) {
            c.paintImmediately(c.getBounds());
        }
    }

    private void showCell(JTable table, int row, int column) {
        Rectangle rect = table.getCellRect(row, column, true);
        table.scrollRectToVisible(rect);
        table.clearSelection();
        table.setRowSelectionInterval(row, row);
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
    }

    public class TrendSensitiveCellRenderer extends JLabel implements TableCellRenderer {

        public TrendSensitiveCellRenderer() {
            this.setForeground(Color.BLACK);
            this.setBackground(Color.WHITE);
            this.setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setForeground(Color.BLACK);
            this.setBackground(Color.WHITE);
            this.setText(null);
            if (value != null) {
                switch(column) {
                    case 0:
                        this.setHorizontalAlignment(JLabel.LEADING);
                        break;
                    case 1:
                        this.setHorizontalAlignment(JLabel.TRAILING);
                        if (row + 1 < table.getRowCount()) {
                            try {
                                float floatValue;
                                floatValue = NUMBER_FORMAT.parse(value.toString().trim()).floatValue();
                                Object prevValue = table.getValueAt(row + 1, column);
                                if (prevValue != null) {
                                    float prevFloatValue;
                                    prevFloatValue = NUMBER_FORMAT.parse(prevValue.toString().trim()).floatValue();
                                    if (floatValue > prevFloatValue) {
                                        this.setBackground(LookFeel.getCurrent().getPositiveBgColor());
                                    } else if (floatValue < prevFloatValue) {
                                        this.setBackground(LookFeel.getCurrent().getNegativeBgColor());
                                    } else {
                                        this.setBackground(Color.YELLOW);
                                    }
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case 2:
                        this.setHorizontalAlignment(JLabel.TRAILING);
                        break;
                }
                this.setText(value.toString());
            }
            return this;
        }
    }

    public ChartViewContainer getChartViewContainer() {
        return viewContainer;
    }

    private void test() {
        tickerTableModel.addRow(new Object[] { "00:01", "12334", "1" });
        tickerTableModel.addRow(new Object[] { "00:02", "12333", "1234" });
        tickerTableModel.addRow(new Object[] { "00:03", "12335", "12345" });
        tickerTableModel.addRow(new Object[] { "00:04", "12334", "123" });
        tickerTableModel.addRow(new Object[] { "00:05", "12334", "123" });
        showCell(tickerTable, tickerTable.getRowCount() - 1, 0);
    }

    private void initComponents() {
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dayChange = new javax.swing.JLabel();
        percent = new javax.swing.JLabel();
        dayVolume = new javax.swing.JLabel();
        dayLow = new javax.swing.JLabel();
        dayHigh = new javax.swing.JLabel();
        dayOpen = new javax.swing.JLabel();
        prevClose = new javax.swing.JLabel();
        lastPrice = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        depthTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tickerTable = new javax.swing.JTable();
        symbol = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        chartPanel = new javax.swing.JPanel();
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel3.setText("Last:");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel4.setText("Prev. cls:");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel5.setText("Open:");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel6.setText("High:");
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel7.setText("Low:");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel8.setText("Change:");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel9.setText("Percent:");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel10.setText("Volume:");
        dayChange.setFont(new java.awt.Font("Dialog", 0, 11));
        dayChange.setText("N/A");
        percent.setFont(new java.awt.Font("Dialog", 0, 11));
        percent.setText("N/A");
        dayVolume.setFont(new java.awt.Font("Dialog", 0, 11));
        dayVolume.setText("N/A");
        dayLow.setFont(new java.awt.Font("Dialog", 0, 11));
        dayLow.setText("N/A");
        dayHigh.setFont(new java.awt.Font("Dialog", 0, 11));
        dayHigh.setText("N/A");
        dayOpen.setFont(new java.awt.Font("Dialog", 0, 11));
        dayOpen.setText("N/A");
        prevClose.setFont(new java.awt.Font("Dialog", 0, 11));
        prevClose.setText("N/A");
        lastPrice.setFont(new java.awt.Font("Dialog", 0, 11));
        lastPrice.setText("N/A");
        depthTable.setFont(new java.awt.Font("Dialog", 0, 11));
        depthTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } }, new String[] { "Ask/Bid", "Price", "Size" }));
        jScrollPane3.setViewportView(depthTable);
        tickerTable.setFont(new java.awt.Font("DialogInput", 0, 11));
        tickerTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } }, new String[] { "Time", "Price", "Size" }) {

            boolean[] canEdit = new boolean[] { false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tickerTable.setShowHorizontalLines(false);
        tickerTable.setShowVerticalLines(false);
        jScrollPane4.setViewportView(tickerTable);
        symbol.setFont(new java.awt.Font("Dialog", 0, 11));
        symbol.setText("Symbol");
        time.setFont(new java.awt.Font("Dialog", 0, 11));
        time.setText("N/A");
        org.jdesktop.layout.GroupLayout chartPanelLayout = new org.jdesktop.layout.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(chartPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 267, Short.MAX_VALUE));
        chartPanelLayout.setVerticalGroup(chartPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 92, Short.MAX_VALUE));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, chartPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE).add(layout.createSequentialGroup().add(symbol, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(31, 31, 31)).add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE).add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(percent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).add(dayChange, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).add(lastPrice, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(prevClose, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel10).add(jLabel5).add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(27, 27, 27))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(dayVolume, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).add(dayLow, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).add(dayHigh, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).add(dayOpen, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).add(time, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(symbol).add(time)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel10).add(dayVolume).add(lastPrice)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(dayHigh).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(dayLow).add(jLabel7))).add(layout.createSequentialGroup().add(21, 21, 21).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel9).add(percent)))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(dayOpen)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(prevClose)))).add(layout.createSequentialGroup().add(43, 43, 43).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8).add(dayChange)))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chartPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JPanel chartPanel;

    private javax.swing.JLabel dayChange;

    private javax.swing.JLabel dayHigh;

    private javax.swing.JLabel dayLow;

    private javax.swing.JLabel dayOpen;

    private javax.swing.JLabel dayVolume;

    private javax.swing.JTable depthTable;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JLabel lastPrice;

    private javax.swing.JLabel percent;

    private javax.swing.JLabel prevClose;

    private javax.swing.JLabel symbol;

    private javax.swing.JTable tickerTable;

    private javax.swing.JLabel time;
}
