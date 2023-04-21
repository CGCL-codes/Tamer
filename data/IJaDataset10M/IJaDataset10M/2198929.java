package com.peralex.utilities.ui.graphs.hopperHistogram.dualChannel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.peralex.utilities.locale.ILocaleListener;
import com.peralex.utilities.locale.PeralexLibsBundle;
import com.peralex.utilities.ui.graphs.graphBase.PixelUnitConverter;
import com.peralex.utilities.ui.graphs.hopperHistogram.IHopperHistogram;

/**
 * This class creates a new cHistogramDualChannel.
 *
 * @author Andre E
 */
public class HistogramDualChannel extends JPanel implements IHopperHistogram, ILocaleListener {

    /** The resource bundle used for multilingual support */
    private ResourceBundle textRes = PeralexLibsBundle.getResource();

    /**
   * The current HistogramDualChannelDrawSurface.
   */
    private final HistogramDualChannelDrawSurface oDrawSurface;

    /**
   * This is the format for the Coordinates.
   */
    private final DecimalFormat oCoordinatesFormat = new DecimalFormat("# ### ###.00");

    /**
	 * Flags to keep track of which fields have been set so.  This is for i18n.
	 */
    private boolean bTitleSet = false;

    private boolean bXAxisTextSet = false;

    private boolean bYAxisTextSet = false;

    /**
   * Creates new form cHistogramDualChannel.
   */
    public HistogramDualChannel() {
        initComponents();
        oDrawSurface = new HistogramDualChannelDrawSurface();
        oDrawSurface.setAxes(xAxis, yAxis);
        oDrawSurfacePanel.add(oDrawSurface, BorderLayout.CENTER);
        oDrawSurface.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        oDrawSurface.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                oXCoordinateValue.setText("-");
                oYCoordinateValue.setText("-");
            }
        });
        oDrawSurface.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                oXCoordinateValue.setText(oCoordinatesFormat.format(oDrawSurface.logXPixelToUnit(e.getX())));
                double unit = (PixelUnitConverter.pixelToUnit(false, e.getY(), 0, oDrawSurface.getHeight(), oDrawSurface.getYAxisMinimum(), oDrawSurface.getYAxisMaximum()));
                oYCoordinateValue.setText(oCoordinatesFormat.format(unit));
            }
        });
        Font titleFont = oTitleLabel.getFont();
        titleFont = titleFont.deriveFont(titleFont.getSize() * 1.2f);
        titleFont = titleFont.deriveFont(Font.BOLD);
        oTitleLabel.setFont(titleFont);
        PeralexLibsBundle.addLocaleListener(this);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        oDrawSurfacePanel = new javax.swing.JPanel();
        oYAxisPanel = new javax.swing.JPanel();
        yAxisLabel = new com.peralex.utilities.ui.graphs.graphBase.YAxisLabel();
        yAxis = new com.peralex.utilities.ui.graphs.axisscale.NumberAxisScale();
        xAxis = new com.peralex.utilities.ui.graphs.axisscale.NumberAxisScale();
        oXAxisLabel = new javax.swing.JLabel();
        oTopPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        oTitleLabel = new javax.swing.JLabel();
        oCoordinatesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        oXCoordinateValue = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        oYCoordinateValue = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        yAxisLabel1 = new com.peralex.utilities.ui.graphs.graphBase.YAxisLabel();
        oProbabilityFactorColorPanel = new com.peralex.utilities.ui.graphs.hopperHistogram.dualChannel.ProbabilityFactorColorPanel();
        setLayout(new java.awt.GridBagLayout());
        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        oDrawSurfacePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(oDrawSurfacePanel, gridBagConstraints);
        oYAxisPanel.setLayout(new java.awt.GridBagLayout());
        yAxisLabel.setText(textRes.getString("Y-Axis"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        oYAxisPanel.add(yAxisLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        oYAxisPanel.add(yAxis, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(oYAxisPanel, gridBagConstraints);
        xAxis.setOrientation(com.peralex.utilities.ui.graphs.axisscale.AbstractDefaultAxisScale.X_AXIS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(xAxis, gridBagConstraints);
        oXAxisLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        oXAxisLabel.setText(textRes.getString("X-Axis"));
        oXAxisLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(oXAxisLabel, gridBagConstraints);
        oTopPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        oTopPanel.add(jPanel2, gridBagConstraints);
        oTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        oTitleLabel.setText(textRes.getString("HopperHistogram.Title"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        oTopPanel.add(oTitleLabel, gridBagConstraints);
        oCoordinatesPanel.setLayout(new java.awt.GridBagLayout());
        oCoordinatesPanel.setBackground(new java.awt.Color(255, 255, 255));
        oCoordinatesPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setText("X:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 4);
        oCoordinatesPanel.add(jLabel1, gridBagConstraints);
        oXCoordinateValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        oXCoordinateValue.setText("# ### ###.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        oCoordinatesPanel.add(oXCoordinateValue, gridBagConstraints);
        jLabel3.setText("Y:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 4);
        oCoordinatesPanel.add(jLabel3, gridBagConstraints);
        oYCoordinateValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        oYCoordinateValue.setText("# ### ###.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        oCoordinatesPanel.add(oYCoordinateValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        oTopPanel.add(oCoordinatesPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(oTopPanel, gridBagConstraints);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        yAxisLabel1.setText(textRes.getString("HopperHistogram.Probability_factor"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(yAxisLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(oProbabilityFactorColorPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }

    /**
   * This method passes new data into the Histogram.
   *
   * @param asDimensionName the name of the dimension
   * @param asDimensionUnitName the name of units for the dimension
   * @param awNumPoints the number of points in the histogram for this dimension (ns)
   * @param afMinScaleValue minimum value for the scale of the dimension
   * @param afMaxScaleValue maximum value for the scale of the dimension
   * @param abIsLogarithmic is the dimension's scale logarithmic?
   * @param afHistogramData histogram data 
   */
    public synchronized void onDataReceived(String[] asDimensionName, String[] asDimensionUnitName, short[] awNumPoints, float[] afMinScaleValue, float[] afMaxScaleValue, boolean[] abIsLogarithmic, float[] afHistogramData) {
        String sXAxisText = asDimensionName[0] + " (" + asDimensionUnitName[0] + ")";
        sXAxisText = sXAxisText.substring(0, 1).toUpperCase() + sXAxisText.substring(1, sXAxisText.length());
        setXAxisText(sXAxisText);
        String sYAxisText = asDimensionName[1] + " (" + asDimensionUnitName[1] + ")";
        sYAxisText = sYAxisText.substring(0, 1).toUpperCase() + sYAxisText.substring(1, sYAxisText.length());
        setYAxisText(sYAxisText);
        if (getXAxisMaximum() != afMaxScaleValue[0] || getXAxisMinimum() != afMinScaleValue[0]) {
            setXAxisRange(afMinScaleValue[0], afMaxScaleValue[0]);
        }
        if (getXAxisMaximum() != afMaxScaleValue[0] || getXAxisMinimum() != afMinScaleValue[0]) {
            setXAxisRange(afMinScaleValue[0], afMaxScaleValue[0]);
        }
        if (getYAxisMaximum() != afMaxScaleValue[1] || getYAxisMinimum() != afMinScaleValue[1]) {
            setXAxisRange(afMinScaleValue[1], afMaxScaleValue[1]);
        }
        if (getYAxisMaximum() != afMaxScaleValue[1] || getYAxisMinimum() != afMinScaleValue[1]) {
            setYAxisRange(afMinScaleValue[1], afMaxScaleValue[1]);
        }
        if (abIsLogarithmic[0] != isXAxisLogarithmic()) {
            setXAxisLogarithmic(abIsLogarithmic[0]);
        }
        float[][] afNewHistogramData = new float[awNumPoints[1]][awNumPoints[0]];
        for (int i = 0; i < awNumPoints[1]; i++) {
            for (int a = 0; a < awNumPoints[0]; a++) {
                afNewHistogramData[i][a] = afHistogramData[a + (i * awNumPoints[0])];
            }
        }
        oDrawSurface.onDataReceived(afNewHistogramData);
    }

    /**
   * This methods sets the XAxisRange.
   *
   * @param dXAxisMinimum contains the minimum
   * @param dXAxisMaximum contains the maximum
   * @param dYAxisMinimum contains the minimum
   * @param dYAxisMaximum contains the maximum
   */
    public synchronized void setAxisRanges(double dXAxisMinimum, double dXAxisMaximum, double dYAxisMinimum, double dYAxisMaximum) {
        oDrawSurface.setAxisRanges(dXAxisMinimum, dXAxisMaximum, dYAxisMinimum, dYAxisMaximum);
    }

    /**
   * This methods sets the XAxisRange.
   *
   * @param dXAxisMinimum contains the minimum
   * @param dXAxisMaximum contains the maximum
   */
    public synchronized void setXAxisRange(double dXAxisMinimum, double dXAxisMaximum) {
        oDrawSurface.setXAxisRange(dXAxisMinimum, dXAxisMaximum);
    }

    /**
   * This methods sets the YAxisRange.
   *
   * @param dYAxisMinimum contains the minimum
   * @param dYAxisMaximum contains the maximum
   */
    public synchronized void setYAxisRange(double dYAxisMinimum, double dYAxisMaximum) {
        oDrawSurface.setYAxisRange(dYAxisMinimum, dYAxisMaximum);
    }

    /**
   * Get the XAxis Minimum.
   *
   * @return dMinX.
   */
    public double getXAxisMinimum() {
        return oDrawSurface.getXAxisMinimum();
    }

    /**
   * Get the XAxis Maximum.
   *
   * @return dMaxX.
   */
    public double getXAxisMaximum() {
        return oDrawSurface.getXAxisMaximum();
    }

    /**
   * Get the YAxis Minimum.
   *
   * @return dMinY.
   */
    public double getYAxisMinimum() {
        return oDrawSurface.getYAxisMinimum();
    }

    /**
   * Get the YAxis Maximum.
   *
   * @return dMaxY.
   */
    public double getYAxisMaximum() {
        return oDrawSurface.getYAxisMaximum();
    }

    /**
   * Set the Title of the Graph.
   *
   * @param sTitleText
   */
    public void setTitle(final String sTitleText) {
        bTitleSet = true;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                oTitleLabel.setText(" " + sTitleText + " ");
            }
        });
    }

    /**
   * Get the Title of the Graph.
   *
   * @return sTitleText
   */
    public String getTitle() {
        return oTitleLabel.getText();
    }

    /**
   * Set the Text of the X-Axis.
   *
   * @param sXAxisText
   */
    public void setXAxisText(final String sXAxisText) {
        bXAxisTextSet = true;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                oXAxisLabel.setText(" " + sXAxisText + " ");
            }
        });
    }

    /**
   * Get the Text of the X-Axis.
   *
   * @return XAxisTitle
   */
    public String getXAxisText() {
        return oXAxisLabel.getText();
    }

    /**
   * Set the Text of the Y-Axis.
   *
   * @param sYAxisText
   */
    public void setYAxisText(final String sYAxisText) {
        bYAxisTextSet = true;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                yAxisLabel.setText(" " + sYAxisText + " ");
            }
        });
    }

    /**
   * Get the Text of the Y-Axis.
   *
   * @return YAxisTitle
   */
    public String getYAxisText() {
        return yAxisLabel.getText();
    }

    /**
   * This method is used to Clear the graph.
   */
    public synchronized void clear() {
        oDrawSurface.clear();
    }

    /**
   * This method sets the Background color of this graph.
   *
   * @param oBackgroundColor contains the new color.
   */
    public void setBackgroundColor(Color oBackgroundColor) {
        oDrawSurface.setBackgroundColor(oBackgroundColor);
    }

    /**
   * This method sets the Grid Color of this graph.
   *
   * @param oGridColor contains the new color.
   */
    public void setGridColor(Color oGridColor) {
        oDrawSurface.setGridColor(oGridColor);
    }

    /**
   * This method sets whether the XAxis is Logarithmic or not.
   */
    public void setXAxisLogarithmic(boolean bXAxisLogarithmic) {
        oDrawSurface.setXAxisLogarithmic(bXAxisLogarithmic);
    }

    /**
   * This method returns whether the XAxis is Logarithmic or not.
   *
   * @return bXAxisLogarithmic.
   */
    public boolean isXAxisLogarithmic() {
        return oDrawSurface.isXAxisLogarithmic();
    }

    /**
   * This method is called when the locale has been changed. The listener should
	 * then update the visual components.
   */
    public void componentsLocaleChanged() {
        textRes = PeralexLibsBundle.getResource();
        if (!bTitleSet) {
            oTitleLabel.setText(textRes.getString("HopperHistogram.Title"));
        }
        if (!bXAxisTextSet) {
            oXAxisLabel.setText(textRes.getString("X-Axis"));
        }
        if (!bYAxisTextSet) {
            yAxisLabel.setText(textRes.getString("Y-Axis"));
        }
        yAxisLabel1.setText(textRes.getString("HopperHistogram.Probability_factor"));
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel oCoordinatesPanel;

    private javax.swing.JPanel oDrawSurfacePanel;

    private com.peralex.utilities.ui.graphs.hopperHistogram.dualChannel.ProbabilityFactorColorPanel oProbabilityFactorColorPanel;

    private javax.swing.JLabel oTitleLabel;

    private javax.swing.JPanel oTopPanel;

    private javax.swing.JLabel oXAxisLabel;

    private javax.swing.JLabel oXCoordinateValue;

    private javax.swing.JPanel oYAxisPanel;

    private javax.swing.JLabel oYCoordinateValue;

    private com.peralex.utilities.ui.graphs.axisscale.AbstractDefaultAxisScale xAxis;

    private com.peralex.utilities.ui.graphs.axisscale.AbstractDefaultAxisScale yAxis;

    private com.peralex.utilities.ui.graphs.graphBase.YAxisLabel yAxisLabel;

    private com.peralex.utilities.ui.graphs.graphBase.YAxisLabel yAxisLabel1;
}
