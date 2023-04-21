package org.nextframework.view.chart.googletools;

import java.awt.Color;
import java.beans.PropertyEditor;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.nextframework.exception.NextException;
import org.nextframework.util.Util;
import org.nextframework.view.chart.ChartData;
import org.nextframework.view.chart.ChartRow;
import org.nextframework.view.chart.ChartStyle;
import org.nextframework.view.chart.ChartType;
import org.nextframework.view.chart.ChartStyle.LegendPosition;
import org.nextframework.view.js.JavascriptReferenciable;
import org.nextframework.view.js.api.GoogleVisualization;
import org.nextframework.view.js.api.Html;
import org.nextframework.view.js.builder.JavascriptBuilder;

public class GoogleToolsChartBuilder extends JavascriptBuilder implements GoogleVisualization, Html {

    static java.util.Map<ChartType, Class<? extends Chart>> chartMapping;

    static {
        chartMapping = new HashMap<ChartType, Class<? extends Chart>>();
        chartMapping.put(ChartType.AREA, AreaChart.class);
        chartMapping.put(ChartType.SCATTER, ScatterChart.class);
        chartMapping.put(ChartType.LINE, LineChart.class);
        chartMapping.put(ChartType.CURVED_LINE, CurvedLineChart.class);
        chartMapping.put(ChartType.PIE, PieChart.class);
        chartMapping.put(ChartType.BAR, BarChart.class);
        chartMapping.put(ChartType.COLUMN, ColumnChart.class);
        chartMapping.put(ChartType.COMBO, ComboChart.class);
    }

    private org.nextframework.view.chart.Chart chart;

    public GoogleToolsChartBuilder(org.nextframework.view.chart.Chart chart) {
        this.chart = chart;
    }

    private DataTable transformChartDataToDataTable() {
        DataTable data = new DataTable();
        ChartData chartData = chart.getData();
        List<ChartRow> rows = chartData.getData();
        if (rows.size() > 0 && chart.getStyle().isContinuous()) {
            data.addColumn(getTypeFor(rows.get(0).getGroup()), chartData.getGroupTitle());
        } else {
            data.addColumn("string", chartData.getGroupTitle());
        }
        for (Comparable<?> serie : chartData.getSeries()) {
            data.addColumn("number", getSerieString(serie));
        }
        data.addRows(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            ChartRow chartRow = rows.get(i);
            if (chart.getStyle().isContinuous()) {
                data.setValue(i, 0, chartRow.getGroup());
            } else {
                data.setValue(i, 0, getGroupString(chartRow.getGroup()));
            }
            DecimalFormat format = new DecimalFormat("0.##", new DecimalFormatSymbols(Locale.US));
            for (int j = 0; j < chartRow.getValues().length; j++) {
                Number value = chartRow.getValues()[j];
                data.setValue(i, j + 1, new StringBuilder(format.format(value)));
            }
        }
        return data;
    }

    protected Comparable<String> getGroupString(Object group) {
        PropertyEditor formatter = this.chart.getStyle().getGroupFormatter();
        formatter.setValue(group);
        return formatter.getAsText();
    }

    private String getTypeFor(Object group) {
        if (group instanceof Date || group instanceof Calendar) {
            return "datetime";
        } else if (group instanceof Number) {
            return "number";
        }
        chart.getStyle().setContinuous(false);
        return "string";
    }

    private String getSerieString(Comparable<?> serie) {
        PropertyEditor formatter = this.chart.getStyle().getSeriesFormatter();
        formatter.setValue(serie);
        return formatter.getAsText();
    }

    private void createAndDrawChart(final String id, DataTable data) {
        Chart chart = instanciateChart(this.chart.getStyle().getChartType(), id);
        ChartStyle style = this.chart.getStyle();
        Map chartParameters = map("width", style.getWidth(), "height", this.chart.getStyle().getHeight());
        Map hAxis = map();
        if (Util.strings.isNotEmpty(this.chart.getData().getGroupTitle())) {
            hAxis.putProperty("title", this.chart.getData().getGroupTitle());
            hAxis.putProperty("titleTextStyle", map("fontName", "arial"));
        }
        if (this.chart.getStyle().isContinuous() && this.chart.getStyle().getGroupPattern() != null) {
            hAxis.putProperty("format", this.chart.getStyle().getGroupPattern());
        }
        if (this.chart.getStyle().getGroupMinValue() != null && this.chart.getStyle().getGroupMaxValue() != null) {
            hAxis.putProperty("viewWindowMode", "explicit");
            hAxis.putProperty("viewWindow", map("max", this.chart.getStyle().getGroupMaxValue(), "min", this.chart.getStyle().getGroupMinValue()));
        }
        Map vAxis = map();
        if (Util.strings.isNotEmpty(this.chart.getData().getSeriesTitle())) {
            vAxis.putProperty("title", this.chart.getData().getSeriesTitle());
            vAxis.putProperty("titleTextStyle", map("fontName", "Arial"));
        }
        vAxis.putProperty("viewWindow", map("min", 0));
        if (style.getPieSliceTextStyle() != null) {
            Map textStyle = map();
            if (style.getPieSliceTextStyle().getColor() != null) {
                textStyle.putProperty("color", getHexColor(style.getPieSliceTextStyle().getColor()));
            }
            if (style.getPieSliceTextStyle().getFontName() != null) {
                textStyle.putProperty("fontName", style.getPieSliceTextStyle().getFontName());
            }
            if (style.getPieSliceTextStyle().getFontSize() != null) {
                textStyle.putProperty("fontSize", style.getPieSliceTextStyle().getFontSize());
            }
            chartParameters.putProperty("pieSliceTextStyle", textStyle);
        }
        if (style.getLegendPosition() != LegendPosition.DEFAULT) {
            chartParameters.putProperty("legend", style.getLegendPosition().toString().toLowerCase());
        }
        if (style.is3d()) {
            chartParameters.putProperty("is3D", true);
        }
        Color[] customColors = this.chart.getStyle().getColors();
        if (customColors != null) {
            Array array = array();
            for (int i = 0; i < customColors.length; i++) {
                Color color = customColors[i];
                array.add(getHexColor(color));
            }
            chartParameters.putProperty("colors", array);
        }
        if (this.chart.getTitle() != null) {
            chartParameters.putProperty("title", GoogleToolsChartBuilder.this.chart.getTitle());
        }
        chartParameters.putProperty("hAxis", hAxis);
        chartParameters.putProperty("vAxis", vAxis);
        if (this.chart.getStyle().getLeftPadding() != null) {
            chartParameters.putProperty("chartArea", map("left", this.chart.getStyle().getLeftPadding()));
        }
        if (this.chart.getStyle().getChartType() == ChartType.COMBO) {
            String styleType = getComboDescription(this.chart.getComboDefaultChartType());
            chartParameters.putProperty("seriesType", styleType);
            Comparable<?>[] series = this.chart.getData().getSeries();
            Map seriesTypeMap = map();
            for (int i = 0; i < series.length; i++) {
                ChartType comboSerieType = this.chart.getComboSerieType(i);
                if (comboSerieType != null) {
                    seriesTypeMap.putProperty(String.valueOf(i), map("type", getComboDescription(comboSerieType)));
                }
            }
            chartParameters.putProperty("series", seriesTypeMap);
        }
        chartParameters.putProperty("backgroundColor", "none");
        if (this.chart.getStyle().getPointSize() != null) {
            chartParameters.putProperty("pointSize", this.chart.getStyle().getPointSize());
        }
        if (this.chart.getStyle().getLineWidth() != null) {
            chartParameters.putProperty("lineWidth", this.chart.getStyle().getLineWidth());
        }
        chart.draw(data, chartParameters);
    }

    private String getComboDescription(ChartType comboDefaultChartType) {
        switch(comboDefaultChartType) {
            case LINE:
                return "line";
            case COLUMN:
                return "bars";
            case AREA:
                return "area";
        }
        throw new NextException("ChartType " + comboDefaultChartType + " not allowed for combo charts. Only LINE, COLUMN and AREA are permited.");
    }

    private String getHexColor(Color color) {
        return "#" + toHexString(color.getRed()) + toHexString(color.getGreen()) + toHexString(color.getBlue());
    }

    private String toHexString(int color) {
        String hexString = Integer.toHexString(color);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    private Chart instanciateChart(ChartType chartType, String id) {
        Class<? extends Chart> class1 = chartMapping.get(chartType);
        if (class1 == null) {
            throw new NextException("N�o � poss�vel renderizar o gr�fico do tipo especificado " + chartType);
        }
        try {
            return class1.getConstructor(JavascriptReferenciable.class).newInstance(document.getElementById(id));
        } catch (Exception e) {
            throw new NextException("N�o foi poss�vel instanciar o tipo de gr�fico especificado. " + class1, e);
        }
    }

    @Override
    public void build() {
        final String id = this.chart.getId();
        if (id == null) {
            throw new RuntimeException("O id do gr�fico � null, n�o foi poss�vel gerar gr�fico");
        }
        google.load("visualization", "1", map("packages", array("corechart")));
        Function function = new Function(new JavascriptBuilder() {

            public void build() {
                DataTable data = transformChartDataToDataTable();
                createAndDrawChart(id, data);
            }
        });
        google.setOnLoadCallback(function);
    }
}
