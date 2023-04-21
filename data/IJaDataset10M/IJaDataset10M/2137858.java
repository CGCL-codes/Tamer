package org.blogtrader.platform.core.netbeans.options.colors;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.blogtrader.platform.core.analysis.chart.QuoteChart;
import org.blogtrader.platform.core.analysis.chartview.AnalysisChartViewContainer;
import org.blogtrader.platform.core.analysis.chartview.WithQuoteChart;
import org.blogtrader.platform.core.analysis.descriptor.Descriptors;
import org.blogtrader.platform.core.analysis.descriptor.QuoteDataSourceDescriptor;
import org.blogtrader.platform.core.analysis.descriptor.SourceType;
import org.blogtrader.platform.core.data.dataserver.AbstractQuoteDataServer;
import org.blogtrader.platform.core.option.CityLightsColorTheme;
import org.blogtrader.platform.core.option.AbstractColorTheme;
import org.blogtrader.platform.core.option.GrayColorTheme;
import org.blogtrader.platform.core.option.OptionsManager;
import org.blogtrader.platform.core.option.WhiteColorTheme;
import org.blogtrader.platform.core.persistence.PersistenceManager;
import org.blogtrader.platform.core.stock.Stock;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;

/**
 *
 * @author Caoyuan Deng
 */
public class ColorFontOptionsPanel extends javax.swing.JPanel {

    /** Creates new Panel */
    public ColorFontOptionsPanel() {
        initComponents();
        ComboBoxModel colorThemeModel = new DefaultComboBoxModel(new String[] { "City Lights", "Gray", "White" });
        colorThemeBox.setModel(colorThemeModel);
        ComboBoxModel quoteChartStyleModel = new DefaultComboBoxModel(QuoteChart.Style.values());
        quoteChartStyleBox.setModel(quoteChartStyleModel);
    }

    private AnalysisChartViewContainer previewContainer;

    private boolean previewPanelInited = false;

    public void initPreviewPanle() {
        String symbol = "Preview";
        QuoteDataSourceDescriptor quoteDataSourceDescriptor = new QuoteDataSourceDescriptor();
        AbstractQuoteDataServer previewDataServer = null;
        List<AbstractQuoteDataServer> dataServers = PersistenceManager.getDefalut().getAllAvailableQuoteDataServers();
        for (AbstractQuoteDataServer dataServer : dataServers) {
            if (dataServer.getName().toUpperCase().contains("CSV ASCII FILE")) {
                previewDataServer = dataServer;
            }
        }
        if (previewDataServer == null) {
            return;
        }
        quoteDataSourceDescriptor.sourceType = SourceType.Quote;
        quoteDataSourceDescriptor.setActive(true);
        quoteDataSourceDescriptor.className = previewDataServer.getName();
        quoteDataSourceDescriptor.sourceSymbol = symbol;
        quoteDataSourceDescriptor.dateFormat = previewDataServer.getDefaultDateFormat();
        FileObject previewFile = Repository.getDefault().getDefaultFileSystem().findResource("UserOptions/Template/preview.csv");
        if (previewFile != null) {
            try {
                InputStream is = previewFile.getInputStream();
                quoteDataSourceDescriptor.inputStream = is;
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ex);
            }
        }
        Descriptors descriptors = OptionsManager.getDefaultDescriptors();
        descriptors.addDataSourceDescriptor(quoteDataSourceDescriptor);
        Stock stock = new Stock(symbol, quoteDataSourceDescriptor);
        if (!stock.isTimeSeriesLoaded()) {
            stock.loadTimeSeries();
        }
        previewContainer = new AnalysisChartViewContainer(this, stock.getTimeSeries(), descriptors);
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(previewContainer, BorderLayout.CENTER);
        previewPanelInited = true;
    }

    private void refreshPreviewPanel() {
        if (previewPanelInited) {
            String colorThemeStr = (String) colorThemeBox.getSelectedItem();
            AbstractColorTheme colorTheme = null;
            if (colorThemeStr.equalsIgnoreCase("White")) {
                colorTheme = new WhiteColorTheme();
            } else if (colorThemeStr.equalsIgnoreCase("City Lights")) {
                colorTheme = new CityLightsColorTheme();
            } else {
                colorTheme = new GrayColorTheme();
            }
            OptionsManager.setColorTheme(colorTheme);
            boolean reversedPositiveNegativeColor = reverseColorBox.isSelected();
            OptionsManager.setReversedPositiveNegativeColor(reversedPositiveNegativeColor);
            QuoteChart.Style style = (QuoteChart.Style) quoteChartStyleBox.getSelectedItem();
            OptionsManager.setQuoteChartStyle(style);
            boolean antiAlias = antiAliasBox.isSelected();
            OptionsManager.setAntiAlias(antiAlias);
            ((WithQuoteChart) previewContainer.getMasterView()).switchQuoteChartStyle(style);
            previewContainer.repaint();
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        reverseColorBox = new javax.swing.JCheckBox();
        colorThemeBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        quoteChartStyleBox = new javax.swing.JComboBox();
        previewPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        antiAliasBox = new javax.swing.JCheckBox();
        jLabel1.setText("Color Theme:");
        reverseColorBox.setText("Reverse Positive / Negative Color");
        reverseColorBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reverseColorBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reverseColorBox.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                reverseColorBoxStateChanged(evt);
            }
        });
        colorThemeBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                colorThemeBoxItemStateChanged(evt);
            }
        });
        jLabel2.setText("Default Quote Chart Style:");
        quoteChartStyleBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                quoteChartStyleBoxItemStateChanged(evt);
            }
        });
        previewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Preview "));
        org.jdesktop.layout.GroupLayout previewPanelLayout = new org.jdesktop.layout.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(previewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 419, Short.MAX_VALUE));
        previewPanelLayout.setVerticalGroup(previewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 237, Short.MAX_VALUE));
        jLabel3.setText("Notice: You may need to re-open the views to take the full effect of new theme.");
        antiAliasBox.setText("Anti Alias");
        antiAliasBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        antiAliasBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        antiAliasBox.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                antiAliasBoxStateChanged(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(previewPanel).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(colorThemeBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(quoteChartStyleBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(reverseColorBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)).add(antiAliasBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(colorThemeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(reverseColorBox)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(quoteChartStyleBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(antiAliasBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(previewPanel).addContainerGap()));
    }

    private void antiAliasBoxStateChanged(javax.swing.event.ChangeEvent evt) {
        refreshPreviewPanel();
    }

    private void reverseColorBoxStateChanged(javax.swing.event.ChangeEvent evt) {
        refreshPreviewPanel();
    }

    private void quoteChartStyleBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        refreshPreviewPanel();
    }

    private void colorThemeBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        refreshPreviewPanel();
    }

    protected javax.swing.JCheckBox antiAliasBox;

    protected javax.swing.JComboBox colorThemeBox;

    protected javax.swing.JLabel jLabel1;

    protected javax.swing.JLabel jLabel2;

    protected javax.swing.JLabel jLabel3;

    protected javax.swing.JPanel previewPanel;

    protected javax.swing.JComboBox quoteChartStyleBox;

    protected javax.swing.JCheckBox reverseColorBox;
}
