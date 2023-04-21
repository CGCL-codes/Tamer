package netgest.bo.xwc.components.classic.charts;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.elements.BarChart.Bar;
import netgest.bo.runtime.EboContext;
import netgest.bo.system.Logger;
import netgest.bo.system.boApplication;
import netgest.bo.xwc.components.HTMLAttr;
import netgest.bo.xwc.components.HTMLTag;
import netgest.bo.xwc.components.classic.charts.configurations.IBarChartConfiguration;
import netgest.bo.xwc.components.classic.charts.datasets.SeriesDataSet;
import netgest.bo.xwc.components.classic.charts.datasets.SeriesDataSetSQL;
import netgest.bo.xwc.components.util.ComponentRenderUtils;
import netgest.bo.xwc.framework.XUIBindProperty;
import netgest.bo.xwc.framework.XUIRenderer;
import netgest.bo.xwc.framework.XUIRendererServlet;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.XUIResponseWriter;
import netgest.bo.xwc.framework.XUIScriptContext;
import netgest.bo.xwc.framework.XUIStateBindProperty;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * 
 * A BarChart component
 * 
 * @author Pedro Pereira
 * 
 * 
 */
public class BarChart extends XUIComponentBase implements netgest.bo.xwc.components.classic.charts.Chart {

    /**
	 * The Logger
	 */
    private static Logger logger = Logger.getLogger("netgest.bo.xcw.components.classic.charts.BarChart");

    /**
	 * The default height for a pie chart
	 */
    private static final int DEFAULT_HEIGHT = 300;

    /**
	 * The default width for a pie chart
	 */
    private static final int DEFAULT_WIDTH = 500;

    /**
	 * Orientation of the chart as horizontal
	 */
    private static final String CHART_ORIENTATION_HORIZONTAL = "horizontal";

    /**
	 * Orientation of the chart as vertical
	 */
    private static final String CHART_ORIENTATION_VERTICAL = "vertical";

    /**
	 * The type of chart that renders JFreeChart based charts
	 */
    private static final String TYPE_CHART_IMG = "IMG";

    /**
	 * The type of chart that renders Flash based charts
	 */
    private static final String TYPE_CHART_FLASH = "FLASH";

    /**
	 * The source of data for the chart
	 */
    private XUIStateBindProperty<SeriesDataSet> dataSet = new XUIStateBindProperty<SeriesDataSet>("dataSet", this, SeriesDataSet.class);

    /**
	 * The orientation of the bar char (horizontal / vertical
	 */
    private XUIBindProperty<String> orientation = new XUIBindProperty<String>("orientation", this, String.class, "vertical");

    /**
	 * Optional configurations for the 
	 */
    private XUIBindProperty<IBarChartConfiguration> configOptions = new XUIBindProperty<IBarChartConfiguration>("configOptions", this, IBarChartConfiguration.class);

    /**
	 * The width of the chart (rendered on the client)
	 */
    private XUIBindProperty<Integer> width = new XUIBindProperty<Integer>("width", this, Integer.class, "500");

    /**
	 * The height of the chart (rendered on the client)
	 */
    private XUIBindProperty<Integer> height = new XUIBindProperty<Integer>("height", this, Integer.class, "300");

    /**
	 * A label for the chart
	 */
    private XUIBindProperty<String> label = new XUIBindProperty<String>("label", this, String.class);

    /**
	 * The type of the chart Flash/ Static Image
	 */
    private XUIBindProperty<String> type = new XUIBindProperty<String>("type", this, String.class);

    /**
	 * The sql query to get the data from
	 */
    private XUIStateBindProperty<String> sql = new XUIStateBindProperty<String>("sql", this, String.class);

    /**
	 * The sql attribute where the column names are stored
	 */
    private XUIBindProperty<String> sqlAttColumn = new XUIBindProperty<String>("sqlAttColumn", this, String.class);

    /**
	 * The sql attribute where the series names are stored
	 */
    private XUIBindProperty<String> sqlAttSeries = new XUIBindProperty<String>("sqlAttSeries", this, String.class);

    /**
	 * The sql attribute where the values
	 */
    private XUIBindProperty<String> sqlAttValues = new XUIBindProperty<String>("sqlAttValues", this, String.class);

    /**
	 * Label mapping for the columns
	 */
    private XUIBindProperty<Map<String, String>> columnLabelsMap = new XUIBindProperty<Map<String, String>>("columnLabelsMap", this, Map.class);

    /**
	 * Label mapping for the series
	 */
    private XUIBindProperty<Map<String, String>> seriesLabelsMap = new XUIBindProperty<Map<String, String>>("seriesLabelsMap", this, Map.class);

    /**
	 * The sql parameters for the query
	 */
    private XUIBindProperty<Object[]> sqlParameters = new XUIBindProperty<Object[]>("sqlParameters", this, Object[].class);

    public boolean wasStateChanged() {
        return super.wasStateChanged();
    }

    ;

    @Override
    public void initComponent() {
        super.initComponent();
        if (this.getType().equalsIgnoreCase(TYPE_CHART_FLASH)) {
            XUIRequestContext.getCurrentContext().getScriptContext().addInclude(XUIScriptContext.POSITION_HEADER, "openFlash", "js/swfobject.js");
        }
    }

    /**
	 * 
	 * Sets the expression from which the DataSet will be retrieved
	 * 
	 * @param dataSetExpr The expression that will be used to retrieve the data set
	 */
    public void setDataSet(String dataSetExpr) {
        this.dataSet.setExpressionText(dataSetExpr);
    }

    /**
	 * 
	 * Retrieves the expression that represents the source of the data set
	 * 
	 * @return The expression that will be used to retrieve the data set
	 */
    public SeriesDataSet getDataSet() {
        return this.dataSet.getEvaluatedValue();
    }

    /**
	 * 
	 * Retrieves the expression that represents the optional configuration options
	 * of this pie chart
	 * 
	 * @return The expression used to retrieve the configuration options
	 */
    public IBarChartConfiguration getConfigOptions() {
        return this.configOptions.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the Configuration Options expression
	 * 
	 * @param configExpr The configuration options expression string
	 */
    public void setConfigOptions(String configExpr) {
        this.configOptions.setExpressionText(configExpr);
    }

    /**
	 * 
	 * Sets the expression for inout data SQL
	 * 
	 * @param newSqlExpr The SQL expression
	 */
    public void setSql(String newSqlExpr) {
        this.sql.setExpressionText(newSqlExpr);
    }

    /**
	 * 
	 * Return the SQL expression to retrieve input data
	 * 
	 * @return The SQL expression to retrieve to the data base
	 */
    public String getSql() {
        return this.sql.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the expression for the SQL Column Attribute
	 * 
	 * @param newSqlColumnExpr The SQL attribute name for the column of the chart
	 */
    public void setSqlAttColumn(String newSqlColumnExpr) {
        this.sqlAttColumn.setExpressionText(newSqlColumnExpr);
    }

    /**
	 * 
	 * Return the SQL attribute which contains the category values
	 * 
	 * @return The SQL attribute which contains the category values
	 */
    public String getSqlAttColumn() {
        return this.sqlAttColumn.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the expression for the SQL Series Attribute
	 * 
	 * @param newSqlColumnExpr The SQL attribute name for the Series of the chart
	 */
    public void setSqlAttSeries(String newSqlSeriesExpr) {
        this.sqlAttSeries.setExpressionText(newSqlSeriesExpr);
    }

    /**
	 * 
	 * Return the SQL attribute which contains the Series values
	 * 
	 * @return The SQL attribute which contains the Series values
	 */
    public String getSqlAttSeries() {
        return this.sqlAttSeries.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the name of the values attribute in the SQL expression
	 * 
	 * @param newSqlAttValuesExpr The name of the values attribute
	 */
    public void setSqlAttValues(String newSqlAttValuesExpr) {
        this.sqlAttValues.setExpressionText(newSqlAttValuesExpr);
    }

    /**
	 * 
	 * Get the name of the sql attribute for the values
	 * 
	 * @return 
	 */
    public String getSqlAttValues() {
        return this.sqlAttValues.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the expression for the label of the chart
	 * 
	 * @param newLabelExpr The label expression
	 */
    public void setLabel(String newLabelExpr) {
        this.label.setExpressionText(newLabelExpr);
    }

    /**
	 * 
	 * Return the label for the chart
	 * 
	 * @return The label to show with the chart
	 */
    public String getLabel() {
        return this.label.getEvaluatedValue();
    }

    /**
	 * 
	 * Returns the orientation of this 
	 * 
	 * @return
	 */
    public String getOrientation() {
        return this.orientation.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the orientation expression 
	 * 
	 * @return The orientation expression
	 */
    public void setOrientation(String orientationExpr) {
        this.orientation.setExpressionText(orientationExpr);
    }

    /**
	 * 
	 * Sets the type of the chart (Image Based or Flash based)
	 * 
	 * @param newTypeExpr The type expression
	 */
    public void setType(String newTypeExpr) {
        this.type.setExpressionText(newTypeExpr);
    }

    /**
	 * 
	 * Returns the type of the chart
	 * 
	 * @return A string with the value <code>IMG</code> or <code>FLASH</code>
	 * 
	 */
    public String getType() {
        return this.type.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the with (in pixels) of the PieChart
	 * 
	 * @param width The value for the width of the chart (in pixels) 
	 */
    public void setWidth(String newWidthExpr) {
        this.width.setExpressionText(newWidthExpr);
    }

    /**
	 * 
	 * Retrieves the width (in pixels) of the chart
	 * 
	 * @return The number of pixels for the width of the chart
	 */
    public Integer getWidth() {
        return this.width.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the height of the chart (in pixels)
	 * 
	 * @param newHeight The number of pixels in height of the chart
	 */
    public void setHeight(String newHeightExpr) {
        this.height.setExpressionText(newHeightExpr);
    }

    /**
	 * 
	 * Get the height of the chart (in pixels)
	 * 
	 * @return The number of pixels for the height of the chart
	 */
    public Integer getHeight() {
        return this.height.getEvaluatedValue();
    }

    /**
	 * 
	 * Retrieve the labels map
	 * 
	 * @return a map between original column labels and their formating
	 */
    public Map<String, String> getColumnLabelsMap() {
        return columnLabelsMap.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the labels map
	 * 
	 * @param labelsExpr
	 * 
	 */
    public void setColumnLabelsMap(String labelsExpr) {
        this.columnLabelsMap.setExpressionText(labelsExpr);
    }

    /**
	 * 
	 * Retrieve the labels map for series
	 * 
	 * @return a map between original series labels and their formating
	 */
    public Map<String, String> getSeriesLabelsMap() {
        return seriesLabelsMap.getEvaluatedValue();
    }

    /**
	 * 
	 * Sets the labels map for series
	 * 
	 * @param labelsExpr
	 * 
	 */
    public void setSeriesLabelsMap(String labelsExpr) {
        this.seriesLabelsMap.setExpressionText(labelsExpr);
    }

    /**
	 * 
	 * Retrieves the array of parameters for the sql query
	 * 
	 * @return
	 */
    public Object[] getSqlParameters() {
        Object[] params = sqlParameters.getEvaluatedValue();
        if (params != null) return params;
        return new Object[0];
    }

    /**
	 * 
	 * Sets the parameters for the sql query
	 * 
	 * @param sqlParamExpr
	 */
    public void setSqlParameters(String sqlParamExpr) {
        this.sqlParameters.setExpressionText(sqlParamExpr);
    }

    @Override
    public void preRender() {
    }

    @Override
    public Object saveState() {
        return super.saveState();
    }

    public DefaultCategoryDataset fillDataSet() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        java.sql.Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet srs = null;
        Map<String, String> columnMappings = getColumnLabelsMap();
        Map<String, String> seriesMappings = getSeriesLabelsMap();
        try {
            Object[] parameters = getSqlParameters();
            EboContext ctx = boApplication.currentContext().getEboContext();
            conn = ctx.getConnectionData();
            stmt = conn.prepareStatement(getSql(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) stmt.setObject(paramIndex + 1, parameters[paramIndex]);
            srs = stmt.executeQuery();
            while (srs.next()) {
                String column = ChartUtils.getLabelOrReplacement(srs.getString(getSqlAttColumn()), columnMappings);
                String series = ChartUtils.getLabelOrReplacement(srs.getString(getSqlAttSeries()), seriesMappings);
                float value = srs.getFloat(getSqlAttValues());
                data.setValue(value, series, column);
            }
        } catch (Exception e) {
            logger.severe(e.getMessage(), e);
        } finally {
            try {
                if (srs != null) srs.close();
            } catch (Exception e) {
                logger.warn("Could not close resultset", e);
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                logger.warn("Could not close statement", e);
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                logger.warn("Could not close connection", e);
            }
        }
        return data;
    }

    /**
	 * 
	 * Renders the LineChart as an image to the given output stream
	 * 
	 * @param out The output stream to write the chart to
	 * @param force If the charts type is not image, forces the chart to be rendered as image
	 */
    public void outputChartAsImageToStream(OutputStream out, boolean force) {
        if (getType().equalsIgnoreCase(TYPE_CHART_IMG) || force) {
            String xAxisLabel = "";
            String yAxisLabel = "";
            DefaultCategoryDataset data = new DefaultCategoryDataset();
            Map<String, String> columnMappings = getColumnLabelsMap();
            Map<String, String> seriesMappings = getSeriesLabelsMap();
            if (getSql() != null) {
                data = fillDataSet();
            } else {
                SeriesDataSet setMine = (SeriesDataSet) getDataSet();
                Iterator<String> it = setMine.getColumnKeys().iterator();
                while (it.hasNext()) {
                    String currColum = (String) it.next();
                    Iterator<String> itRows = setMine.getSeriesKeys().iterator();
                    while (itRows.hasNext()) {
                        String currSeries = (String) itRows.next();
                        Number val = setMine.getValue(currSeries, currColum);
                        data.addValue(val, ChartUtils.getLabelOrReplacement(currSeries, seriesMappings), ChartUtils.getLabelOrReplacement(currColum, columnMappings));
                    }
                }
                xAxisLabel = setMine.getXAxisLabel();
                yAxisLabel = setMine.getYAxisLabel();
            }
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            if (getOrientation() != null) {
                if (getOrientation().equalsIgnoreCase(CHART_ORIENTATION_HORIZONTAL)) orientation = PlotOrientation.HORIZONTAL;
                if (getOrientation().equalsIgnoreCase(CHART_ORIENTATION_VERTICAL)) orientation = PlotOrientation.VERTICAL;
            }
            JFreeChart chart = ChartFactory.createBarChart(getLabel(), xAxisLabel, yAxisLabel, data, orientation, true, true, false);
            chart.setBackgroundPaint(Color.WHITE);
            chart.setBorderVisible(false);
            CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
            plot.getRangeAxis().setUpperMargin(0.1);
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setItemMargin(0.0);
            plot.setRangeGridlinePaint(Color.BLACK);
            plot.setBackgroundPaint(Color.white);
            CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.00"));
            renderer.setBaseItemLabelGenerator(generator);
            renderer.setBaseItemLabelsVisible(true);
            Integer width = getWidth();
            if (width == null) width = DEFAULT_WIDTH;
            Integer height = getHeight();
            if (height == null) height = DEFAULT_HEIGHT;
            if (getConfigOptions() != null) {
                IBarChartConfiguration chartConfigurations = (IBarChartConfiguration) getConfigOptions();
                if (chartConfigurations != null) {
                    if (!chartConfigurations.showChartTitle()) chart.setTitle("");
                    if (chartConfigurations.getTooltipString() != null) {
                        String expression = chartConfigurations.getTooltipString();
                        expression = expression.replace("$val", "{2}");
                        CategoryItemRenderer rendererLabel = plot.getRenderer();
                        CategoryItemLabelGenerator generatorCustom = new StandardCategoryItemLabelGenerator(expression, new DecimalFormat("0.00"));
                        rendererLabel.setBaseItemLabelGenerator(generatorCustom);
                    }
                    if (chartConfigurations.getColours() != null) {
                        Color[] colors = chartConfigurations.getColours();
                        BarRenderer rendererColors = (BarRenderer) plot.getRenderer();
                        int pos = 0;
                        for (Color p : colors) {
                            rendererColors.setSeriesPaint(pos, p);
                            pos++;
                        }
                    }
                    if (chartConfigurations.getBackgroundColour() != null) {
                        chart.setBackgroundPaint(chartConfigurations.getBackgroundColour());
                    }
                }
            }
            try {
                ChartUtilities.writeChartAsPNG(out, chart, width, height);
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    public static class XEOHTMLRenderer extends XUIRenderer implements XUIRendererServlet {

        @Override
        public void service(ServletRequest oRequest, ServletResponse oResponse, XUIComponentBase oComp) throws IOException {
            BarChart component = (BarChart) oComp;
            OutputStream out = oResponse.getOutputStream();
            if (component.getType().equalsIgnoreCase(TYPE_CHART_IMG)) {
                oResponse.setContentType("image/png");
                component.outputChartAsImageToStream(out, false);
            } else if (component.getType().equalsIgnoreCase(TYPE_CHART_FLASH)) {
                Chart c = new Chart();
                IBarChartConfiguration chartConfigurations = null;
                Map<String, String> columnMapping = component.getColumnLabelsMap();
                Map<String, String> seriesMapping = component.getSeriesLabelsMap();
                if (component.getConfigOptions() != null) chartConfigurations = (IBarChartConfiguration) component.getConfigOptions();
                c.setBackgroundColour("#FFFFFF");
                c.setTitle(new Text(component.getLabel()));
                HashSet<String> labelsUsed = new HashSet<String>();
                XAxis xAx = new XAxis();
                SeriesDataSet setMine;
                if (component.getSql() != null) {
                    String sqlExpression = component.getSql();
                    String attColumn = component.getSqlAttColumn();
                    String attSeries = component.getSqlAttSeries();
                    String attValues = component.getSqlAttValues();
                    EboContext context = boApplication.currentContext().getEboContext();
                    setMine = new SeriesDataSetSQL(context, sqlExpression, attColumn, attSeries, attValues, component.getSqlParameters());
                } else {
                    setMine = (SeriesDataSet) component.getDataSet();
                }
                Iterator<String> it = setMine.getSeriesKeys().iterator();
                Color[] mapOfColors = ChartUtils.DEFAULT_COLORS;
                if (chartConfigurations != null) if (chartConfigurations.getColours() != null) mapOfColors = chartConfigurations.getColours();
                int colorCounter = 0;
                if (component.getOrientation().equalsIgnoreCase(CHART_ORIENTATION_VERTICAL)) {
                    while (it.hasNext()) {
                        jofc2.model.elements.BarChart currChart = new jofc2.model.elements.BarChart(jofc2.model.elements.BarChart.Style.GLASS);
                        Color currentColor = null;
                        if (colorCounter <= mapOfColors.length - 1) currentColor = mapOfColors[colorCounter]; else currentColor = ChartUtils.getRandomDarkColor();
                        String rgb = ChartUtils.ColorToRGB(currentColor);
                        String currentSeries = (String) it.next();
                        currChart.setText(ChartUtils.getLabelOrReplacement(currentSeries, seriesMapping));
                        currChart.setColour(rgb);
                        Iterator<String> itColum = setMine.getColumnKeys().iterator();
                        while (itColum.hasNext()) {
                            String currentColumn = (String) itColum.next();
                            Number val = setMine.getValue(currentSeries, currentColumn);
                            Bar barra = new Bar(val, rgb);
                            if (!labelsUsed.contains(currentColumn)) {
                                labelsUsed.add(currentColumn);
                                xAx.addLabels(ChartUtils.getLabelOrReplacement(currentColumn, columnMapping));
                            }
                            currChart.addBars(barra);
                        }
                        colorCounter++;
                        if (chartConfigurations != null && chartConfigurations.getTooltipString() != null) {
                            String expression = chartConfigurations.getTooltipString();
                            expression = expression.replace("$val", "#val#");
                            currChart.setTooltip(expression);
                        }
                        c.addElements(currChart);
                    }
                    if (chartConfigurations != null) {
                        if (chartConfigurations.getBackgroundColour() != null) {
                            Color color = chartConfigurations.getBackgroundColour();
                            String rgb = Integer.toHexString(color.getRGB());
                            rgb = rgb.substring(2, rgb.length());
                            c.setBackgroundColour(rgb);
                        }
                        if (!chartConfigurations.showChartTitle()) {
                            c.setTitle(null);
                        }
                    }
                    c.setXAxis(xAx);
                    c.computeYAxisRange(10);
                } else {
                }
                out.write(c.toString().getBytes());
                out.close();
            }
        }

        @Override
        public void encodeBegin(XUIComponentBase component) throws IOException {
            XUIResponseWriter w = getResponseWriter();
            BarChart oComp = (BarChart) component;
            if (oComp.getType().equalsIgnoreCase(TYPE_CHART_FLASH)) {
                StringBuilder b = new StringBuilder();
                String url = ComponentRenderUtils.getCompleteServletURL(getRequestContext(), component.getClientId());
                url = URLEncoder.encode(url, "UTF-8");
                int width = oComp.getWidth();
                int height = oComp.getHeight();
                b.append("  var flashvars = {};" + "  var params = { wmode: \"transparent\" };" + "var attributes = {}; ");
                b.append("swfobject.embedSWF(\"open-flash-chart.swf\", " + "\"" + component.getClientId() + "\", " + "\"" + width + "\", " + "\"" + height + "\", " + "\"9.0.0\",\"expressInstall.swf\", " + "{\"data-file\": \"" + url + "\"},flashvars,params,attributes);");
                w.getScriptContext().add(XUIScriptContext.POSITION_FOOTER, component.getClientId(), b);
                String clientId = oComp.getClientId();
                String reloadChart = ChartUtils.getReloadChartJSFunction(clientId, url, width, height, getRequestContext());
                w.getScriptContext().add(XUIScriptContext.POSITION_FOOTER, "reloadChart", reloadChart);
                w.startElement(HTMLTag.DIV, oComp);
                w.writeAttribute(HTMLAttr.ID, component.getClientId(), null);
                w.endElement(HTMLTag.DIV);
            } else if (oComp.getType().equalsIgnoreCase(TYPE_CHART_IMG)) {
                String url = ComponentRenderUtils.getServletURL(getRequestContext(), component.getClientId());
                url += "&ts=" + System.currentTimeMillis();
                w.startElement(HTMLTag.DIV, oComp);
                w.writeAttribute(HTMLAttr.ID, component.getClientId(), null);
                w.startElement(HTMLTag.IMG, oComp);
                w.writeAttribute(HTMLAttr.SRC, url, null);
                w.writeAttribute(HTMLAttr.WIDTH, oComp.getWidth(), null);
                w.writeAttribute(HTMLAttr.HEIGHT, oComp.getHeight(), null);
                w.endElement(HTMLTag.IMG);
                w.endElement(HTMLTag.DIV);
            }
        }

        @Override
        public void encodeEnd(XUIComponentBase component) throws IOException {
        }

        @Override
        public void decode(XUIComponentBase component) {
            super.decode(component);
        }
    }
}
