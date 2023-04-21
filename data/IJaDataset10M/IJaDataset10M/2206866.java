package jmash;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import jmash.tableModel.NumberFormatter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author  Alessandro
 */
public class CorrezioneDensimetro extends javax.swing.JInternalFrame {

    /**
     *
     */
    private static final long serialVersionUID = 4455686160997506344L;

    GlassPanel glassPanel = new GlassPanel();

    GlassPanel glassPanel2 = new GlassPanel();

    /** Creates new form CorrezioneDensimetro */
    public CorrezioneDensimetro() {
        initComponents();
        setBorder(Utils.getDefaultBorder());
        this.jPanel1.add(this.glassPanel);
        this.jPanel4.add(this.glassPanel2);
        this.jPanel3.add(createDemoPanel(), BorderLayout.CENTER);
        pack();
        fldT.setModel(Main.getFromCache("CorrezioneDensimetro.T", 25.0), 0.0, 100.0, 1.0, "0.0", "CorrezioneDensimetro.T");
        fldTC.setModel(Main.getFromCache("CorrezioneDensimetro.TC", 20.0), 0.0, 100.0, 1.0, "0.0", "CorrezioneDensimetro.TC");
        fldG.setGravity(Main.getFromCache("CorrezioneDensimetro.G", 1.040));
        calc();
    }

    private boolean flag = false;

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        fldT = new jmash.component.JTemperatureSpinner();
        fldTC = new jmash.component.JTemperatureSpinner();
        fldG = new jmash.component.JGravitySpinner();
        fldGC = new jmash.component.JGravitySpinner();
        jPanel4 = new javax.swing.JPanel();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setClosable(true);
        setIconifiable(true);
        setTitle("Correzione densimetro");
        jPanel1.setMaximumSize(new java.awt.Dimension(80, 400));
        jPanel1.setMinimumSize(new java.awt.Dimension(80, 400));
        jPanel1.setPreferredSize(new java.awt.Dimension(80, 400));
        jPanel1.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jLabel1.setText("T. lettura");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel2.add(jLabel1, gridBagConstraints);
        jLabel3.setText("Densità letta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel2.add(jLabel3, gridBagConstraints);
        jLabel4.setText("Densità corretta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel2.add(jLabel4, gridBagConstraints);
        jLabel2.setText("T. calibrazione");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanel2.add(jLabel2, gridBagConstraints);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jPanel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel5, gridBagConstraints);
        fldT.setPreferredSize(new java.awt.Dimension(120, 18));
        fldT.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fldTStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanel2.add(fldT, gridBagConstraints);
        fldTC.setPreferredSize(new java.awt.Dimension(120, 18));
        fldTC.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fldTCStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel2.add(fldTC, gridBagConstraints);
        fldG.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fldGStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanel2.add(fldG, gridBagConstraints);
        fldGC.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanel2.add(fldGC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel2, gridBagConstraints);
        jPanel4.setMaximumSize(new java.awt.Dimension(80, 400));
        jPanel4.setMinimumSize(new java.awt.Dimension(80, 400));
        jPanel4.setPreferredSize(new java.awt.Dimension(80, 400));
        jPanel4.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        getContentPane().add(jPanel4, gridBagConstraints);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 880) / 2, (screenSize.height - 730) / 2, 880, 730);
    }

    private void fldGStateChanged(javax.swing.event.ChangeEvent evt) {
        calc();
    }

    private void fldTCStateChanged(javax.swing.event.ChangeEvent evt) {
        calc();
    }

    private void fldTStateChanged(javax.swing.event.ChangeEvent evt) {
        calc();
    }

    private jmash.component.JGravitySpinner fldG;

    private jmash.component.JGravitySpinner fldGC;

    private jmash.component.JTemperatureSpinner fldT;

    private jmash.component.JTemperatureSpinner fldTC;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    XYPointerAnnotation pointer = null;

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries series1 = new XYSeries("");

    XYSeries series2 = new XYSeries("");

    XYSeries series3 = new XYSeries("");

    private void calc() {
        double t = Utils.C2F(fldT.getTemperature());
        Main.putIntoCache("CorrezioneDensimetro.T", fldT.getTemperature());
        Main.putIntoCache("CorrezioneDensimetro.TC", fldTC.getTemperature());
        Main.putIntoCache("CorrezioneDensimetro.G", fldG.getGravity());
        double T = t;
        double tc = Utils.C2F(fldTC.getTemperature());
        double ir = fldG.getGravity();
        double cr = ir * SG60(t) / SG60(tc);
        double CR = cr;
        fldGC.setGravity(cr);
        this.glassPanel.setDensita(cr);
        this.glassPanel2.setDensita(ir);
        this.series1.clear();
        this.series3.clear();
        this.series2.clear();
        this.series2.add(-20, (ir - 1) * 1000);
        this.series2.add(100, (ir - 1) * 1000);
        T = fldT.getTemperature();
        this.series3.add(T, 0);
        this.series3.add(T, 170);
        if (this.pointer == null) {
            this.pointer = new XYPointerAnnotation("", 0, 0, 5 * Math.PI / 4.0);
            this.chart.getXYPlot().addAnnotation(this.pointer);
            this.pointer.setTipRadius(2.0);
            this.pointer.setBaseRadius(120.0);
            this.pointer.setFont(new Font("SansSerif", Font.BOLD, 12));
            this.pointer.setPaint(Color.blue);
            this.pointer.setTextAnchor(TextAnchor.BOTTOM_CENTER);
        }
        int idx = 0;
        for (int C = (int) T - 25; C < (int) T + 25; C += 1) {
            t = Utils.C2F(C);
            cr = ir * SG60(t) / SG60(tc);
            this.series1.add(C, (cr - 1) * 1000);
        }
        this.pointer.setText("Lettura corretta = " + NumberFormatter.format03(CR));
        this.pointer.setX(T);
        this.pointer.setY((CR - 1) * 1000);
        this.chart.getXYPlot().getDomainAxis().setUpperBound(T + 20);
        this.chart.getXYPlot().getDomainAxis().setLowerBound(T - 20);
        this.chart.getXYPlot().getRangeAxis().setUpperBound((CR - 1) * 1000 + 20);
        this.chart.getXYPlot().getRangeAxis().setLowerBound((CR - 1) * 1000 - 20);
    }

    private double SG60(double t) {
        return 1.00130346 - 1.34722124E-4 * t + 2.04052596E-6 * t * t - 2.32820948E-9 * t * t * t;
    }

    public class GlassPanel extends JPanel {

        /**
	 *
	 */
        private static final long serialVersionUID = 2959297637060444493L;

        ImageIcon glassIcon = new ImageIcon(CorrezioneDensimetro.class.getResource("/jmash/images/densimetro.gif"));

        Image glass = this.glassIcon.getImage();

        ImageIcon glassIcon2 = new ImageIcon(CorrezioneDensimetro.class.getResource("/jmash/images/dens2.gif"));

        ImageIcon backIcon = new ImageIcon(CorrezioneDensimetro.class.getResource("/jmash/images/back.gif"));

        Image glass2 = this.glassIcon2.getImage();

        Image back = this.backIcon.getImage();

        BufferedImage dest;

        BufferedImage dest2;

        BufferedImage destColor;

        Insets insets;

        int X = 80, Y = 400;

        public GlassPanel() {
            this.dest = new BufferedImage(this.X, this.Y, BufferedImage.TYPE_INT_ARGB);
            this.dest2 = new BufferedImage(this.X, this.Y, BufferedImage.TYPE_INT_ARGB);
            this.destColor = new BufferedImage(this.X, this.Y, BufferedImage.TYPE_INT_ARGB);
            Graphics2D destG2 = this.dest2.createGraphics();
            Graphics2D destG = this.dest.createGraphics();
            destG.drawImage(this.glass, 0, 0, this);
            destG2.drawImage(this.glass2, 0, 0, this);
            this.setPreferredSize(new Dimension(this.X, this.Y));
            this.setMaximumSize(new Dimension(this.X, this.Y));
            this.setMinimumSize(new Dimension(this.X, this.Y));
            this.setSize(new Dimension(this.X, this.Y));
        }

        Color color = Main.treeColor.ebcToRgb(30.0);

        int step = 10;

        int DELTA = 100;

        @Override
        public void paint(Graphics g) {
            if (this.insets == null) {
                this.insets = getInsets();
            }
            g.translate(this.insets.left, this.insets.top);
            Graphics2D g2d = (Graphics2D) g;
            Graphics2D destG = this.dest.createGraphics();
            destG.setColor(Color.WHITE);
            destG.fillRect(0, 0, this.X, this.Y);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            destG.drawImage(this.back, 0, this.DELTA - this.step, this);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            destG.setColor(this.color);
            destG.fillRect(0, this.DELTA + 5, this.X, this.Y - this.DELTA);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            destG.drawImage(this.glass, 0, this.DELTA - this.step, this);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            destG.setColor(this.color);
            destG.fillRect(0, this.DELTA, this.X, this.Y - this.DELTA);
            destG.drawImage(this.dest2, 0, this.DELTA - this.step, this);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            destG.setColor(this.color);
            destG.fillRect(0, this.DELTA, this.X, this.Y - this.DELTA);
            destG.setColor(Color.BLACK);
            destG.drawRect(0, this.DELTA, this.X - 1, this.Y - this.DELTA - 1);
            destG.setColor(Color.BLACK);
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            destG.drawRect(0, 0, this.X - 1, this.Y - 1);
            g2d.drawImage(this.dest, 0, 0, this);
        }

        public void setDensita(double gg) {
            double g = (gg - 0.98) * 100;
            this.step = 20 + (int) (g * 11);
            this.repaint();
        }
    }

    public JPanel createDemoPanel() {
        JFreeChart lChart = createChart();
        this.dataset.addSeries(this.series1);
        this.dataset.addSeries(this.series2);
        this.dataset.addSeries(this.series3);
        int T = 0;
        for (int C = (int) T - 25; C < (int) T + 25; C += 1) {
            double t = Utils.C2F(C);
            double tc = 20, ir = 1;
            double cr = ir * SG60(t) / SG60(tc);
            this.series1.add(C, (cr - 1) * 1000);
        }
        this.chart.getXYPlot().setDataset(this.dataset);
        return new ChartPanel(lChart);
    }

    private JFreeChart chart = null;

    private JFreeChart createChart() {
        this.chart = createChart(this.dataset);
        return this.chart;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart("", "Temperatura", "Densità", null, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        chart.getXYPlot().getRenderer().setItemLabelsVisible(true);
        chart.getXYPlot().getRenderer().setItemLabelPaint(Color.black);
        final XYPlot plot = chart.getXYPlot();
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(4f));
        plot.getRenderer().setSeriesStroke(1, new BasicStroke(2f));
        plot.getRenderer().setSeriesVisibleInLegend(false);
        plot.setForegroundAlpha(0.75f);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        final ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        chart.getXYPlot().getDomainAxis().setUpperBound(100);
        chart.getXYPlot().getDomainAxis().setLowerBound(0);
        chart.getXYPlot().getRangeAxis().setUpperBound(166);
        chart.getXYPlot().getRangeAxis().setLowerBound(-10);
        chart.getXYPlot().getRenderer().setToolTipGenerator(new XYToolTipGenerator() {

            public String generateToolTip(XYDataset ds, int s, int i) {
                double d0 = ds.getXValue(s, i);
                double y0 = ds.getYValue(s, i);
                String str = "";
                str = "Letti a " + d0 + "�C sarebbero " + NumberFormatter.format03(y0 / 1000 + 1);
                return str;
            }
        });
        return chart;
    }
}
