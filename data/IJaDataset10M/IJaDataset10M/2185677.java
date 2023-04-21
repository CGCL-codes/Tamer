package ArianneUtil;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import org.jfree.chart.*;
import org.jfree.chart.annotations.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.time.*;
import org.jfree.ui.*;
import org.jfree.chart.title.LegendTitle;

/**
 * ChartShape � il bean utilizzato da ChartShape, che � la classe derivata da Shapes che implementa le regole di
 * visualizzazione dei trend dinamici
 * <p>
 *
 * @author      Andrea Annibali
 * @version     1.0
 */
public class ChartShapeBean extends ChartPanel {

    private TimeSeries curve[];

    private Hashtable curves;

    private java.sql.Connection conn;

    private String chartTitle = "";

    private int chartTitleSize = 14;

    private int chartTitleStyle = Font.ITALIC;

    private String chartTitleFontName = "Times";

    private Font titleFont = new Font(chartTitleFontName, chartTitleStyle, chartTitleSize);

    private int titleColor = Color.black.getRGB();

    private QueryPlot queryPlotList[];

    private Color bckColor = null;

    private Color brdColor = null;

    private Color fontColor = null;

    private Font font = JFreeChart.DEFAULT_TITLE_FONT;

    private DataGenerator dgTimer;

    private StandardXYItemRenderer renderer;

    public JFreeChart chart;

    private int historyCount = 100;

    int xOrg, yOrg;

    private JPopupMenu popup = null;

    private boolean specifyTimeStamp = false, autoScale = true, showLegend = true, vOrientation = false;

    private String yRangeLabel = "Values";

    private String xRangeLabel = "Time";

    private Shapes fatherShape = null;

    private ImageIcon bckImg = null;

    private float alpha = 0.1f;

    private double minYRange = Double.MAX_VALUE;

    private double maxYRange = Double.MIN_VALUE;

    private LegendTitle legend;

    public ChartShapeBean(Shapes fs, Hashtable cvs, java.sql.Connection c, int x, int y, Dimension minSize, Dimension prefSize, Color brdC, Color bckC, String ct, String yrl, String xrl, String ctfn, int ctsty, int ctsz, int ctc, Font fnt, Color fntColor, int hc, boolean st, boolean as, JPopupMenu jp, int msec, ImageIcon bi, float a, double minYR, double maxYR, boolean sl, boolean vOri) {
        super(null);
        setDoubleBuffered(false);
        try {
            fatherShape = fs;
            curves = cvs;
            conn = c;
            bckColor = bckC;
            brdColor = brdC;
            font = fnt;
            historyCount = hc;
            dgTimer = new DataGenerator(msec);
            setMinimumSize(minSize);
            setMaximumSize(prefSize);
            setPreferredSize(prefSize);
            setSize(prefSize);
            setLocation(x, y);
            setChartTitle(ct);
            setChartTitleSize(ctsz);
            setChartTitleStyle(ctsty);
            setChartTitleFontName(ctfn);
            setChartTitleColor(ctc);
            setFontColor(fntColor);
            setYRangeLabel(yrl);
            setXRangeLabel(xrl);
            setSpecifyTimeStamp(st);
            setAutoScale(as);
            setBckImg(bi);
            setAlpha(a);
            setMinYRange(minYR);
            setMaxYRange(maxYR);
            xOrg = x;
            yOrg = y;
            popup = jp;
            setShowLegend(sl);
            setOrientationVertical(vOri);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setLegend(LegendTitle lt) {
        legend = lt;
    }

    public LegendTitle getLegend() {
        return legend;
    }

    public void setShowLegend(boolean b) {
        showLegend = b;
        if (chart != null) {
            if (!showLegend) {
                setLegend(chart.getLegend());
                chart.removeLegend();
            } else {
                if (chart.getLegend() == null) chart.addLegend(getLegend());
            }
        }
        validate();
    }

    public void setOrientationVertical(boolean b) {
        vOrientation = b;
        if (chart != null && chart.getXYPlot() != null) chart.getXYPlot().setOrientation(b ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
    }

    public boolean getOrientationVertical() {
        return vOrientation;
    }

    public boolean getShowLegend() {
        return showLegend;
    }

    public void setAlpha(float a) {
        alpha = a;
        if (chart != null && chart.getPlot() != null) {
            chart.setBackgroundImageAlpha(alpha);
            chart.getPlot().setBackgroundAlpha(alpha);
        }
    }

    public void setMinYRange(double m) {
        minYRange = m;
        if (chart != null && chart.getXYPlot() != null && minYRange < maxYRange && !getAutoScale()) chart.getXYPlot().getDomainAxis().setRange(minYRange, maxYRange);
    }

    public double getMinYRange() {
        return minYRange;
    }

    public void setMaxYRange(double m) {
        maxYRange = m;
        if (chart != null && chart.getXYPlot() != null && minYRange < maxYRange && !getAutoScale()) chart.getXYPlot().getDomainAxis().setRange(minYRange, maxYRange);
    }

    public double getMaxYRange() {
        return maxYRange;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setBckImg(ImageIcon bi) {
        bckImg = bi;
        if (chart != null && bckImg != null) {
            chart.setBackgroundImage(bckImg.getImage());
        }
    }

    public ImageIcon getBckImg() {
        return bckImg;
    }

    public void setAutoScale(boolean as) {
        autoScale = as;
    }

    public boolean getAutoScale() {
        return autoScale;
    }

    public void setYRangeLabel(String yrl) {
        yRangeLabel = yrl;
        if (getChart() != null) getChart().getXYPlot().getRangeAxis().setLabel(getYRangeLabel());
    }

    public String getYRangeLabel() {
        return yRangeLabel;
    }

    public void setXRangeLabel(String xrl) {
        xRangeLabel = xrl;
        if (getChart() != null) getChart().getXYPlot().getDomainAxis().setLabel(getXRangeLabel());
    }

    public String getXRangeLabel() {
        return xRangeLabel;
    }

    public void setSpecifyTimeStamp(boolean st) {
        specifyTimeStamp = st;
    }

    public boolean getSpecifyTimeStamp() {
        return specifyTimeStamp;
    }

    /**
   * Restituisce true se il timer � in esecuzione
   * @return true se il timer � running
   */
    public boolean isRunning() {
        return dgTimer.isRunning();
    }

    /**
   * Avvia il timer
   */
    public void start() {
        dgTimer.start();
    }

    /**
   * Arresta il timer
   */
    public void stop() {
        dgTimer.stop();
    }

    /**
   * Inizializzazione dei componenti grafici
   *
   * @throws java.lang.Exception
   */
    private void jbInit() throws Exception {
        QueryPlot[] qp = new QueryPlot[curves.size()];
        int i = 0;
        int j = 0;
        int nTs = 0;
        for (Enumeration e = curves.keys(); e.hasMoreElements(); ) {
            Object element = e.nextElement();
            int id = (new Integer("" + element)).intValue();
            if (!((QueryPlot) curves.get(element)).getColumns().elementAt(0).equals("")) {
                qp[j] = (QueryPlot) curves.get(element);
                nTs += getNumCol(qp[j]);
                j++;
                i++;
            }
        }
        queryPlotList = new QueryPlot[j];
        System.arraycopy(qp, 0, queryPlotList, 0, j);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        if (nTs > 0) {
            curve = new TimeSeries[nTs];
            nTs = 0;
            for (j = 0; j < queryPlotList.length; j++) {
                String curveName = (String) queryPlotList[j].getColumns().elementAt(0);
                for (i = 0; i < getNumCol(queryPlotList[j]); i++) {
                    if (!curveName.equals("")) {
                        this.curve[nTs] = new TimeSeries(curveName, Millisecond.class);
                        this.curve[nTs].setMaximumItemCount(historyCount);
                        dataset.addSeries(curve[nTs]);
                        nTs++;
                    }
                }
            }
        }
        if (getSpecifyTimeStamp()) {
            for (int k = 0; k < queryPlotList.length; k++) {
                String queryId = fatherShape.getQueryId(k);
                if (fatherShape.getDataProvider() != null && fatherShape.getDataProvider().isRegisteredQuery(queryId)) fatherShape.getDataProvider().removeQuery(queryId);
            }
        }
        DateAxis domain = new DateAxis(getXRangeLabel());
        NumberAxis range = new NumberAxis(getYRangeLabel());
        range.setAutoRange(getAutoScale());
        range.setAutoRangeIncludesZero(false);
        if (!getAutoScale()) {
            range.setRange(minYRange, maxYRange);
        }
        renderer = new StandardXYItemRenderer();
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.green);
        renderer.setPlotImages(false);
        Stroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        renderer.setBaseStroke(bs);
        Stroke bs1 = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Stroke bs2 = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        renderer.setSeriesStroke(0, bs1);
        renderer.setSeriesStroke(1, bs2);
        XYPlot xyplot = new XYPlot(dataset, domain, range, renderer);
        xyplot.setBackgroundPaint(Color.black);
        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        domain.setTickLabelsVisible(true);
        domain.setVerticalTickLabels(true);
        DateTickUnit unit = new DateTickUnit(DateTickUnit.HOUR, 2);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        domain.setTickUnit(unit);
        domain.setDateFormatOverride(formatter);
        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart = new JFreeChart(getChartTitle(), font, xyplot, true);
        chart.setBackgroundPaint(bckColor);
        chart.setBorderVisible(true);
        chart.setBorderPaint(brdColor);
        chart.getPlot().setBackgroundPaint(bckColor);
        setAlpha(alpha);
        chart.getXYPlot().setOrientation(getOrientationVertical() ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
        if (bckImg != null) {
            setBckImg(bckImg);
        }
        setChartFont(font);
        setFontColor(fontColor);
        chart.getTitle().setPaint(new Color(titleColor));
        chart.getTitle().setFont(titleFont);
        setChart(chart);
        if (popup != null) setPopupMenu(popup);
        this.setDoubleBuffered(false);
        validate();
    }

    public ImageIcon getBackgroundImage() {
        return bckImg;
    }

    public void annotateMax(int idx) {
        TimeSeriesDataItem max = getMax(idx);
        int val = (int) Math.round(max.getValue().doubleValue());
        long maxTimeStamp = max.getPeriod().getLastMillisecond();
        setAnnotation(maxTimeStamp, val, "Fabbisogno max odierno: ");
    }

    public void setAnnotation(long maxTimeStamp, int val, String label) {
        final CircleDrawer cd = new CircleDrawer(Color.yellow, new BasicStroke(1.0f), null);
        final XYAnnotation bestBid = new XYDrawableAnnotation(maxTimeStamp, val, 11, 11, cd);
        chart.getXYPlot().addAnnotation(bestBid);
        XYPointerAnnotation pointer = null;
        double minXRange = chart.getXYPlot().getDomainAxis().getRange().getLowerBound();
        double maxXRange = chart.getXYPlot().getDomainAxis().getRange().getUpperBound();
        if (val < (maxYRange + minYRange) / 2) {
            if (maxTimeStamp > (maxXRange + minXRange) / 2) {
                pointer = new XYPointerAnnotation(label + val, maxTimeStamp, val, 5 * Math.PI / 4.0);
                pointer.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
            } else {
                pointer = new XYPointerAnnotation(label + val, maxTimeStamp, val, 7 * Math.PI / 4.0);
                pointer.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT);
            }
        } else {
            if (maxTimeStamp > (maxXRange + minXRange) / 2) {
                pointer = new XYPointerAnnotation(label + val, maxTimeStamp, val, 3 * Math.PI / 4.0);
                pointer.setTextAnchor(TextAnchor.TOP_RIGHT);
            } else {
                pointer = new XYPointerAnnotation(label + val, maxTimeStamp, val, Math.PI / 4.0);
                pointer.setTextAnchor(TextAnchor.TOP_LEFT);
            }
            pointer.setBaseRadius(35.0);
            pointer.setTipRadius(10.0);
            pointer.setFont(new Font("SansSerif", Font.BOLD, 14));
        }
        pointer.setPaint(Color.yellow);
        pointer.setArrowPaint(Color.yellow);
        chart.getXYPlot().addAnnotation(pointer);
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(Font f) {
        titleFont = f;
        if (getChart() != null && getChart().getTitle() != null) getChart().getTitle().setFont(titleFont);
    }

    public int getChartTitleColor() {
        return titleColor;
    }

    public void setChartTitleColor(int c) {
        titleColor = c;
        if (getChart() != null && getChart().getTitle() != null) getChart().getTitle().setPaint(new Color(c));
    }

    public String getChartTitleFontName() {
        return chartTitleFontName;
    }

    public void setChartTitleFontName(String fn) {
        chartTitleFontName = fn;
        setTitleFont(new Font(getChartTitleFontName(), getChartTitleStyle(), getChartTitleSize()));
    }

    public int getChartTitleSize() {
        return chartTitleSize;
    }

    public void setChartTitleSize(int cts) {
        chartTitleSize = cts;
        setTitleFont(new Font(getChartTitleFontName(), getChartTitleStyle(), getChartTitleSize()));
    }

    public int getChartTitleStyle() {
        return chartTitleStyle;
    }

    public void setChartTitleStyle(int sty) {
        chartTitleStyle = sty;
        setTitleFont(new Font(getChartTitleFontName(), getChartTitleStyle(), getChartTitleSize()));
    }

    public Color getFontColor() {
        if (chart != null && chart.getTitle() != null) {
            fontColor = (Color) chart.getXYPlot().getDomainAxis().getTickLabelPaint();
            return fontColor;
        }
        return null;
    }

    public void setFontColor(Color c) {
        if (c != null && chart != null) {
            fontColor = c;
            chart.getXYPlot().getDomainAxis().setTickLabelPaint(c);
            chart.getXYPlot().getDomainAxis().setLabelPaint(c);
            chart.getXYPlot().getRangeAxis().setTickLabelPaint(c);
            chart.getXYPlot().getRangeAxis().setLabelPaint(c);
        }
    }

    /**
   * Restituisce il font dei testi visualizzati nel grafico. Poich� titolo,
   * sottotitolo, etichette degli assi, etc. hanno lo stesso font,
   * viene restituito il font ottenuto dal titolo
   * @return il Font del titolo
   */
    public Font getChartFont() {
        if (chart != null && chart.getTitle() != null) {
            font = chart.getXYPlot().getDomainAxis().getLabelFont();
            return font;
        }
        return null;
    }

    public void setChartFont(Font f) {
        if (chart != null && chart.getTitle() != null) {
            font = f;
            chart.getXYPlot().getDomainAxis().setTickLabelFont(f);
            chart.getXYPlot().getDomainAxis().setLabelFont(f);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(f);
            chart.getXYPlot().getRangeAxis().setLabelFont(f);
        }
    }

    /**
   * Imposta la visualizzazione o meno dei simboli sulla curva
   * @param ds il booleano che imposta la visualizzazione
   */
    public void setDisplayShapes(boolean ds) {
        renderer.setPlotImages(ds);
    }

    public boolean getDisplayShapes() {
        return renderer.getPlotImages();
    }

    /**
   * Restituisce il numero di colonne della query data in ingresso
   * @param q la stringa contenente la query di cui calcolare il numero di colonne
   * @return il numero di colonne
   */
    public int getNumCol(String q) {
        int res = -1;
        try {
            if (conn != null && !conn.isClosed()) {
                Statement s = conn.createStatement();
                ResultSet r = s.executeQuery(q);
                res = r.getMetaData().getColumnCount();
                r.close();
                s.close();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        return (getSpecifyTimeStamp() ? res - 1 : res);
    }

    public int getNumCol(QueryPlot qp) {
        int res = -1;
        if (qp.getNumCol() < 0) {
            try {
                if (conn != null && !conn.isClosed()) {
                    Statement s = conn.createStatement();
                    ResultSet r = s.executeQuery(qp.getQuery());
                    res = r.getMetaData().getColumnCount();
                    qp.setNumCol(res);
                    r.close();
                    s.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        } else {
            res = qp.getNumCol();
        }
        return (getSpecifyTimeStamp() ? res - 1 : res);
    }

    /**
   * Imposta l'Hashtable contenente le curve
   * @param cvs Hashtable
   */
    public void setCurves(Hashtable cvs) {
        curves = cvs;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Disegna il componente
   * @param g l'oggetto Graphics del componente
   */
    public void paint(Graphics g) {
        super.paint(g);
    }

    /**
   * Disegna il componente
   * @param g l'oggetto Graphics del componente
   */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void modifyHistoryCount(int nsec) {
        int nTs = 0;
        for (int j = 0; j < queryPlotList.length; j++) {
            for (int i = 0; i < getNumCol(queryPlotList[j]); i++) {
                this.curve[nTs].setMaximumItemAge(nsec);
                nTs++;
            }
        }
    }

    /**
   * Imposta il colore del bordo
   * @param c l'oggetto Color che rappresenta il colore del bordo
   */
    public void setBorderColor(Color c) {
        this.getChart().getPlot().setOutlinePaint(c);
        repaint();
    }

    /**
   * Imposta il titolo del grafico
   * @param s String
   */
    public void setChartTitle(String s) {
        chartTitle = s;
        if (s != null && chart != null) {
            chart.setTitle(s);
        }
    }

    /**
   * Restituisce il titolo del grafico
   * @return un oggetto di tipo String contenente il titolo del grafico
   */
    public String getChartTitle() {
        if (chart != null) chartTitle = chart.getTitle().getText();
        return chartTitle;
    }

    public Color getBorderColor() {
        return (Color) getChart().getPlot().getOutlinePaint();
    }

    public Color getFillColor() {
        return (Color) getChart().getBackgroundPaint();
    }

    /**
   * Imposta il colore di riempimento
   * @param c l'oggetto Color che rappresenta il colore di riempimento
   */
    public void setFillColor(Color c) {
        chart.setBackgroundPaint(c);
        chart.setBorderPaint(c);
        chart.getPlot().setBackgroundPaint(c);
        repaint();
    }

    public Color getShapeBorderColor() {
        return (Color) getChart().getPlot().getOutlinePaint();
    }

    public Color getShapeFillColor() {
        return (Color) getChart().getPlot().getBackgroundPaint();
    }

    public TimeSeriesDataItem getMax(int idx) {
        TimeSeriesDataItem maxItem = null;
        for (int j = 0; j < curve[idx].getItemCount(); j++) {
            TimeSeriesDataItem item = curve[idx].getDataItem(j);
            if (maxItem == null) maxItem = item;
            if (item.getValue().doubleValue() >= maxItem.getValue().doubleValue()) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    private boolean addCurveObservation(int idx, double y) {
        boolean changes = false;
        Millisecond msec = new Millisecond();
        try {
            if (curve.length > idx) {
                curve[idx].add(msec, y);
                changes = true;
            } else {
                if (curve[idx].getDataItem(msec).getValue().doubleValue() != y) {
                    curve[idx].addOrUpdate(msec, y);
                    changes = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return changes;
    }

    private boolean addCurveObservation(int idx, double y, long timeStamp) {
        boolean changes = false;
        try {
            Millisecond startTime = new Millisecond(new java.util.Date(timeStamp * 1000));
            if (curve[idx].getDataItem(startTime) == null || curve[idx].getDataItem(startTime).getValue().doubleValue() != y) {
                curve[idx].addOrUpdate(startTime, y);
                changes = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return changes;
    }

    /**
   * Imposta la connessione al DB MySQL
   * @param c l'oggetto Connection che rappresenta la connessione al DB MySQL
   */
    public void setConnection(java.sql.Connection c) {
        this.conn = c;
    }

    public void startDataGenerator(int msec) {
        this.new DataGenerator(msec).start();
    }

    /**
   * The data generator.
   */
    class DataGenerator extends Timer implements ActionListener {

        Statement s = null;

        /**
     * Constructor.
     */
        DataGenerator(int msec) {
            super(msec, null);
            addActionListener(this);
        }

        /**
     * Adds a new free/total memory reading to the dataset.
     *
     * @param event  the action event.
     */
        public void actionPerformed(ActionEvent event) {
            if (getSpecifyTimeStamp()) directRetrieve(); else delegatedRetrieve();
        }

        void delegatedRetrieve() {
            double res = 0;
            long timeStamp = 0;
            int nColTot = 0;
            int numCol = 0;
            for (int i = 0; i < queryPlotList.length; i++) {
                if (!((String) queryPlotList[i].getColumns().elementAt(0)).equals("")) {
                    String queryId = fatherShape.getQueryId(i);
                    if (fatherShape.getDataProvider() == null) {
                        JOptionPane.showMessageDialog(null, "Non e'  presente alcun Data Provider", "Attenzione!", JOptionPane.ERROR_MESSAGE);
                        fatherShape.getFatherPanel().stopTimerToggle();
                    } else {
                        numCol = getNumCol(fatherShape.getDataProvider().getQuery(queryId));
                        for (int j = 0; j < numCol; j++) {
                            if (!getSpecifyTimeStamp()) {
                                Object retrVal = fatherShape.getDataProvider().getVal(queryId, j + 1);
                                if (retrVal != null) {
                                    if (retrVal instanceof Integer) res = (double) ((Integer) retrVal).intValue(); else if (retrVal instanceof Float) try {
                                        res = ((Float) retrVal).floatValue();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    } else try {
                                        res = ((Double) retrVal).doubleValue();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    addCurveObservation(nColTot + j, res);
                                }
                            } else {
                                try {
                                    Object retrTime = fatherShape.getDataProvider().getVal(queryId, j + 1);
                                    if (retrTime instanceof Long) timeStamp = ((Long) retrTime).longValue(); else timeStamp = (new Long("" + fatherShape.getDataProvider().getVal(queryId, j + 1))).longValue();
                                    Object retrVal = fatherShape.getDataProvider().getVal(queryId, j + 2);
                                    if (retrVal instanceof Float) res = (double) ((Float) retrVal).floatValue(); else if (retrVal instanceof Integer) res = (double) ((Integer) retrVal).intValue(); else if (retrVal instanceof Long) res = (double) ((Long) retrVal).longValue(); else if (retrVal instanceof Double) res = ((Double) retrVal).doubleValue();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                addCurveObservation(i, res, timeStamp);
                            }
                        }
                    }
                }
                nColTot += numCol;
            }
        }

        public ResultSet executeStatementQuery(Statement s, String q) throws SQLException {
            ResultSet res = null;
            int failureCount = 100;
            long elapsed = 0;
            boolean retrieveSuccess = false;
            while (!retrieveSuccess) {
                try {
                    res = s.executeQuery(q);
                    retrieveSuccess = true;
                } catch (SQLException exsql) {
                    retrieveSuccess = false;
                    failureCount--;
                    exsql.printStackTrace();
                    System.out.println("Il retrieve dei dati ha fallito per la query: " + q);
                    if (failureCount <= 0) throw exsql;
                }
            }
            return res;
        }

        void directRetrieve() {
            boolean curveChanged = false;
            double res = 0;
            long timeStamp = 0;
            ResultSet r = null;
            for (int i = 0; i < queryPlotList.length; i++) {
                try {
                    if (!conn.isClosed()) {
                        s = conn.createStatement();
                        if (s != null && !conn.isClosed() && queryPlotList[i] != null && queryPlotList[i].getQuery() != null) {
                            r = executeStatementQuery(s, queryPlotList[i].getQuery());
                            int numCol = (getSpecifyTimeStamp() ? r.getMetaData().getColumnCount() - 1 : r.getMetaData().getColumnCount());
                            while (!conn.isClosed() && r.next()) {
                                for (int j = 0; j < numCol; j++) {
                                    if (!getSpecifyTimeStamp()) {
                                        res = r.getDouble(j + 1);
                                        curveChanged |= addCurveObservation(j, res);
                                    } else {
                                        timeStamp = r.getLong(j + 1);
                                        res = r.getDouble(j + 2);
                                        curveChanged |= addCurveObservation(j, res, timeStamp);
                                    }
                                }
                            }
                            r.close();
                            s.close();
                        }
                    }
                } catch (SQLException sqlex) {
                    sqlex.printStackTrace();
                    String errMsg = null;
                    if (sqlex.getMessage().startsWith("Column Index out of range")) errMsg = "Rincontrollare il numero di parametri specificati nella " + "query di definizione della curva " + queryPlotList[i].getQuery(); else errMsg = sqlex.getMessage();
                    JOptionPane.showMessageDialog(null, errMsg, "Attenzione!", JOptionPane.ERROR_MESSAGE);
                    fatherShape.getFatherPanel().stopTimerToggle();
                }
            }
            if (curveChanged) {
                chart.getXYPlot().clearAnnotations();
                annotateMax(0);
            }
        }
    }
}
