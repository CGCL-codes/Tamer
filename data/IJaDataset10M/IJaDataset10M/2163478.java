package org.gvsig.rastertools.properties.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.RasterPropertiesTocMenuEntry;
import org.gvsig.rastertools.statistics.StatisticsProcess;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
 * Este es el panel general de la ventana de propiedades. En el hemos metido
 * todas aquellas opciones que no sabemos en que seccion meterlas.
 *
 * @version 15/04/2008
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class GeneralPanel extends AbstractPanel implements ActionListener, INoDataPanel, IProcessActions {

    private static final long serialVersionUID = -4761218260868307869L;

    private FLayer fLayer = null;

    private JLabel labelNotShowLayer = null;

    private JLabel labelMinim = null;

    private JLabel labelMaxim = null;

    private JCheckBox checkBoxMinim = null;

    private JCheckBox checkBoxMaxim = null;

    private JFormattedTextField textFieldMinim = null;

    private JFormattedTextField textFieldMaxim = null;

    private NumberFormat doubleDisplayFormat = null;

    private NumberFormat doubleEditFormat = null;

    private double initMaxScale = -1;

    private double initMinScale = -1;

    private JButton calcButton = null;

    private JPanel scalePanel = null;

    private JPanel recalcStatsPanel = null;

    private NoDataPanel pNoDataPanel = null;

    private GridTransparency transparency = null;

    private JScrollPane jScrollPane = null;

    private JEditorPane jEditorPane = null;

    private final String bgColor0 = "\"#FEEDD6\"";

    private final String bgColor1 = "\"#EAEAEA\"";

    private final String bgColor3 = "\"#FBFFE1\"";

    private final String bgColor4 = "\"#D6D6D6\"";

    /**
	 * Booleano que est� a true cuando la fila a dibujar es par y a false cuando
	 * es impar.
	 */
    private boolean rowColor = true;

    /**
	 * Constructor del GeneralPanel
	 */
    public GeneralPanel() {
        super();
        setUpFormats();
        initialize();
        translate();
    }

    /**
	 * Create and set up number formats. These objects also parse numbers input by
	 * user.
	 */
    private void setUpFormats() {
        doubleDisplayFormat = NumberFormat.getNumberInstance();
        doubleDisplayFormat.setMinimumFractionDigits(0);
        doubleEditFormat = NumberFormat.getNumberInstance();
    }

    /**
	 * Asigna todos los textos del panel en su idioma correspondiente
	 */
    private void translate() {
        getJLabelNotShowLayer().setText(PluginServices.getText(this, "no_mostrar_la_capa_cuando_la_escala_sea") + ":");
        getJLabelMinim().setText("(" + PluginServices.getText(this, "escala_maxima") + ")");
        getJLabelMaxim().setText("(" + PluginServices.getText(this, "escala_minima") + ")");
        getJCheckBoxMinim().setText(PluginServices.getText(this, "mayor_de") + " 1:");
        getJCheckBoxMaxim().setText(PluginServices.getText(this, "menor_de") + " 1:");
        getScalePanel().setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "rango_de_escalas")));
        getRecalcStatsPanel().setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "stats")));
        setLabel(PluginServices.getText(this, "general"));
    }

    /**
	 * Construye el panel
	 */
    protected void initialize() {
        GridBagConstraints gridBagConstraints;
        setLayout(new GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(getScalePanel(), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(getNoDataPanel(), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(getRecalcStatsPanel(), gridBagConstraints);
        this.setPreferredSize(new Dimension(100, 80));
    }

    /**
	 * This method initializes TranspOpacitySliderPanel
	 * @return javax.swing.JPanel
	 */
    public NoDataPanel getNoDataPanel() {
        if (pNoDataPanel == null) {
            pNoDataPanel = new NoDataPanel(this);
            pNoDataPanel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "nodata"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            pNoDataPanel.setComboValueSetup(0);
        }
        return pNoDataPanel;
    }

    public JPanel getScalePanel() {
        if (scalePanel == null) {
            scalePanel = new JPanel();
            scalePanel.setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints;
            int y = 0;
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            scalePanel.add(getJLabelNotShowLayer(), gridBagConstraints);
            y++;
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y;
            gridBagConstraints.insets = new Insets(2, 2, 2, 0);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            scalePanel.add(getJCheckBoxMinim(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = y;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 0, 2, 2);
            scalePanel.add(getJTextFieldMinim(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = y;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            scalePanel.add(getJLabelMinim(), gridBagConstraints);
            y++;
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y;
            gridBagConstraints.insets = new Insets(2, 2, 2, 0);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            scalePanel.add(getJCheckBoxMaxim(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = y;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 0, 2, 2);
            scalePanel.add(getJTextFieldMaxim(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = y;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            scalePanel.add(getJLabelMaxim(), gridBagConstraints);
            y++;
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            JPanel emptyPanel = new JPanel();
            emptyPanel.setMinimumSize(new Dimension(0, 0));
            scalePanel.add(emptyPanel, gridBagConstraints);
        }
        return scalePanel;
    }

    public JPanel getRecalcStatsPanel() {
        if (recalcStatsPanel == null) {
            recalcStatsPanel = new JPanel();
            recalcStatsPanel.setLayout(new BorderLayout(5, 5));
            recalcStatsPanel.add(getJScrollPane(), BorderLayout.CENTER);
            recalcStatsPanel.add(getCalcButton(), BorderLayout.SOUTH);
        }
        return recalcStatsPanel;
    }

    /**
	 * Bot�n para recalcular las estadisticas.
	 * @return JButton
	 */
    public JButton getCalcButton() {
        if (calcButton == null) {
            calcButton = new JButton(RasterToolsUtil.getText(this, "recalc_stats"));
            calcButton.addActionListener(this);
        }
        return calcButton;
    }

    private JLabel getJLabelNotShowLayer() {
        if (labelNotShowLayer == null) {
            labelNotShowLayer = new JLabel();
        }
        return labelNotShowLayer;
    }

    private JFormattedTextField getJTextFieldMinim() {
        if (textFieldMinim == null) {
            textFieldMinim = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleEditFormat)));
            textFieldMinim.setEnabled(false);
            textFieldMinim.setBackground(getBackground());
        }
        return textFieldMinim;
    }

    private JFormattedTextField getJTextFieldMaxim() {
        if (textFieldMaxim == null) {
            textFieldMaxim = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleEditFormat)));
            textFieldMaxim.setEnabled(false);
            textFieldMaxim.setBackground(getBackground());
        }
        return textFieldMaxim;
    }

    private JCheckBox getJCheckBoxMinim() {
        if (checkBoxMinim == null) {
            checkBoxMinim = new JCheckBox();
            checkBoxMinim.addActionListener(this);
            checkBoxMinim.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            checkBoxMinim.setMargin(new java.awt.Insets(0, 0, 0, 0));
        }
        return checkBoxMinim;
    }

    private JLabel getJLabelMinim() {
        if (labelMinim == null) {
            labelMinim = new JLabel();
            labelMinim.setEnabled(false);
        }
        return labelMinim;
    }

    private JCheckBox getJCheckBoxMaxim() {
        if (checkBoxMaxim == null) {
            checkBoxMaxim = new JCheckBox();
            checkBoxMaxim.addActionListener(this);
            checkBoxMaxim.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            checkBoxMaxim.setMargin(new java.awt.Insets(0, 0, 0, 0));
        }
        return checkBoxMaxim;
    }

    private JLabel getJLabelMaxim() {
        if (labelMaxim == null) {
            labelMaxim = new JLabel();
            labelMaxim.setEnabled(false);
        }
        return labelMaxim;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getJCheckBoxMinim()) {
            boolean enabled = getJCheckBoxMinim().isSelected();
            getJLabelMinim().setEnabled(enabled);
            getJTextFieldMinim().setEnabled(enabled);
            getJTextFieldMinim().setBackground(enabled ? Color.white : getBackground());
        }
        if (e.getSource() == getCalcButton()) {
            RasterProcess process = new StatisticsProcess();
            process.addParam("layer", fLayer);
            process.addParam("force", new Boolean(true));
            process.setActions(this);
            process.start();
        }
        if (e.getSource() == getJCheckBoxMaxim()) {
            boolean enabled = getJCheckBoxMaxim().isSelected();
            getJLabelMaxim().setEnabled(enabled);
            getJTextFieldMaxim().setEnabled(enabled);
            getJTextFieldMaxim().setBackground(enabled ? Color.white : getBackground());
        }
    }

    /**
	 * Controla la alternatividad de colores en la tabla.
	 *
	 * @return Cadena con el color de la fila siguiente.
	 */
    private String getColor() {
        String color = (rowColor ? bgColor0 : bgColor1);
        rowColor = !rowColor;
        return color;
    }

    /**
	 * Obtiene una entrada de la tabla en formato HTML a partir de una propiedad,
	 * un valor y un color.
	 *
	 * @param prop
	 *          Nombre de la propiedad
	 * @param value
	 *          Valor
	 * @param color
	 *          Color
	 *
	 * @return Entrada HTML de la tabla
	 */
    private String setHTMLBasicProperty(String prop, String value) {
        String content = "<tr valign=\"top\">";
        if (prop != null) content += "<td bgcolor=" + bgColor4 + "align=\"right\" width=\"140\"><font face=\"Arial\" size=\"3\">" + prop + ":&nbsp;</font></td>";
        content += "<td bgcolor=" + getColor() + "align=\"left\"><font face=\"Arial\" size=\"3\">" + value + "</font></td>";
        content += "</tr>";
        return content;
    }

    /**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param title
	 *          Nombre del titulo
	 * @param colspan
	 *          Numero de celdas que ocupara el titulo
	 *
	 * @return Entrada HTML del titulo
	 */
    private String setHTMLTitleTable(String title, int colspan) {
        return "<tr valign=\"middle\" >" + "<td bgcolor=" + bgColor3 + " align=\"center\" colspan=\"" + colspan + "\"><font face=\"Arial\" size=\"3\"><b> " + title + "</b></font></td>" + "</tr>";
    }

    /**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param content
	 *          Codigo HTML de las filas que componen la tabla.
	 *
	 * @return Entrada HTML de la tabla completa
	 */
    private String setHTMLTable(String content) {
        return "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\">" + content + "</table>";
    }

    public void setReference(Object ref) {
        super.setReference(ref);
        if (!(ref instanceof FLayer)) return;
        FLayer lyr = (FLayer) ref;
        if (lyr instanceof IRasterProperties) {
            fLayer = lyr;
            getNoDataPanel().setLayer((IRasterProperties) lyr);
            transparency = ((IRasterProperties) lyr).getRenderTransparency();
            if (fLayer.getMaxScale() != -1) {
                initMaxScale = fLayer.getMaxScale();
                getJTextFieldMaxim().setValue(Double.valueOf(fLayer.getMaxScale()));
                getJCheckBoxMaxim().setSelected(true);
                getJLabelMaxim().setEnabled(true);
                getJTextFieldMaxim().setEnabled(true);
                getJTextFieldMaxim().setBackground(Color.WHITE);
            }
            if (fLayer.getMinScale() != -1) {
                initMinScale = fLayer.getMinScale();
                getJTextFieldMinim().setValue(Double.valueOf(fLayer.getMinScale()));
                getJCheckBoxMinim().setSelected(true);
                getJLabelMinim().setEnabled(true);
                getJTextFieldMinim().setEnabled(true);
                getJTextFieldMinim().setBackground(Color.WHITE);
            }
        }
        refreshHTMLStatistics();
        saveStatus();
        setValuesFromPanelToGridTransparency();
    }

    /**
	 * Refresca el HTML del cuadro de texto de las estadisticas de todas las capas
	 */
    private void refreshHTMLStatistics() {
        String html = "";
        DatasetListStatistics statistics = null;
        try {
            statistics = DatasetListStatistics.loadDatasetListStatistics(((FLyrRasterSE) fLayer).getDataSource());
            if (statistics == null) return;
        } catch (RmfSerializerException e) {
            return;
        }
        if (statistics.isCalculated()) {
            double[] max = statistics.getMax();
            double[] min = statistics.getMin();
            double[] variance = statistics.getVariance();
            double[] mean = statistics.getMean();
            if (((FLyrRasterSE) fLayer).getDataType()[0] == IBuffer.TYPE_BYTE) {
                max = statistics.getMaxByteUnsigned();
                min = statistics.getMinByteUnsigned();
            }
            for (int i = 0; i < max.length; i++) {
                html += setHTMLTitleTable(RasterToolsUtil.getText(this, "band") + " " + (i + 1), 2);
                html += setHTMLBasicProperty(RasterToolsUtil.getText(this, "minimo"), Double.valueOf(min[i]).toString());
                html += setHTMLBasicProperty(RasterToolsUtil.getText(this, "maximo"), Double.valueOf(max[i]).toString());
                html += setHTMLBasicProperty(RasterToolsUtil.getText(this, "media"), Double.valueOf(mean[i]).toString());
                html += setHTMLBasicProperty(RasterToolsUtil.getText(this, "varianza"), Double.valueOf(variance[i]).toString());
            }
            html = setHTMLTable(html);
        } else {
            html += setHTMLTitleTable("Estadisticas no calculadas", 2);
            html = setHTMLTable(html);
        }
        getJEditorPane().setText(html);
        getJEditorPane().setCaretPosition(0);
    }

    /**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJEditorPane());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jEditorPane
	 *
	 * @return javax.swing.JEditorPane
	 */
    private JEditorPane getJEditorPane() {
        if (jEditorPane == null) {
            jEditorPane = new JEditorPane();
            jEditorPane.setEditable(false);
            jEditorPane.setContentType("text/html");
        }
        return jEditorPane;
    }

    public void accept() {
        onlyApply();
    }

    /**
	 * Asigna los valores del panel a la transparencia de la capa.
	 */
    private void setValuesFromPanelToGridTransparency() {
        if (transparency == null) return;
        if (fLayer == null || ((FLyrRasterSE) fLayer).getDataSource() == null) return;
        ((FLyrRasterSE) fLayer).getDataSource().setNoDataValue(getNoDataPanel().getNoDataValue());
        ((FLyrRasterSE) fLayer).getDataSource().setNoDataEnabled(getNoDataPanel().getComboSetupIndex() != 0);
        transparency.setNoData(getNoDataPanel().getNoDataValue());
        transparency.activeNoData(getNoDataPanel().getComboSetupIndex() != 0);
        ((FLyrRasterSE) fLayer).applyNoData();
        ((FLyrRasterSE) fLayer).setNoDataType(getNoDataPanel().getComboSetupIndex());
        transparency.activeTransparency();
        fLayer.getMapContext().invalidate();
    }

    /**
	 * Aplica el estado del panel a la capa.
	 */
    public void onlyApply() {
        if (RasterPropertiesTocMenuEntry.enableEvents) setValuesFromPanelToGridTransparency();
        if (fLayer == null) return;
        double maxScale = -1;
        double minScale = -1;
        if (getJCheckBoxMaxim().isSelected() && getJTextFieldMaxim().getValue() != null) maxScale = ((Number) getJTextFieldMaxim().getValue()).doubleValue();
        if (getJCheckBoxMinim().isSelected() && getJTextFieldMinim().getValue() != null) minScale = ((Number) getJTextFieldMinim().getValue()).doubleValue();
        fLayer.setMaxScale(maxScale);
        fLayer.setMinScale(minScale);
    }

    public void apply() {
        onlyApply();
        saveStatus();
    }

    /**
	 * Guarda el estado actual del panel
	 */
    private void saveStatus() {
        getPanelGroup().getProperties().put("nodatavalue", new Double(getNoDataPanel().getNoDataValue()));
        getPanelGroup().getProperties().put("nodatatype", new Integer(((FLyrRasterSE) fLayer).getNoDataType()));
    }

    /**
	 * Restaura los valores de la capa conforme estaba al abrir el panel
	 */
    public void restoreStatus() {
        ((FLyrRasterSE) fLayer).setNoDataValue(((Double) getPanelGroup().getProperties().get("nodatavalue")).doubleValue());
        ((FLyrRasterSE) fLayer).setNoDataType(((Integer) getPanelGroup().getProperties().get("nodatatype")).intValue());
        fLayer.setMaxScale(initMaxScale);
        fLayer.setMinScale(initMinScale);
    }

    public void cancel() {
        restoreStatus();
    }

    /**
	 * Actualiza los valores del panel noData
	 * @param noDataPanel
	 */
    public void refreshValues(NoDataPanel noDataPanel) {
        if (fLayer == null) {
            noDataPanel.setEnabledComponents(false);
            return;
        }
        noDataPanel.setEnabledComponents(noDataPanel.getComboSetupIndex() == 2);
        switch(noDataPanel.getComboSetupIndex()) {
            case 0:
                noDataPanel.setNoDataValue(0);
                break;
            case 1:
                ((FLyrRasterSE) fLayer).getDataSource().resetNoDataValue();
                noDataPanel.setNoDataValue(((FLyrRasterSE) fLayer).getNoDataValue());
                break;
            case 2:
                noDataPanel.setNoDataValue(((Double) Configuration.getValue("nodata_value", new Double(RasterLibrary.defaultNoDataValue))).doubleValue());
                break;
        }
    }

    public void bandStateChanged(NoDataPanel noDataPanel, int newIndex) {
        refreshValues(noDataPanel);
    }

    public void sourceStateChanged(NoDataPanel noDataPanel, int newIndex) {
        refreshValues(noDataPanel);
    }

    public void end(Object param) {
        refreshHTMLStatistics();
    }

    public void selected() {
    }

    public void interrupted() {
    }
}
