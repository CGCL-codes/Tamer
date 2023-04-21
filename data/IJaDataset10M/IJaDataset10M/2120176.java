package com.googlecode.g2re.html.google;

import com.googlecode.g2re.domain.DataColumn;
import com.googlecode.g2re.domain.JdbcQuery;
import com.googlecode.g2re.html.BoundElement;
import com.googlecode.g2re.html.HTMLBuilderArgs;
import com.googlecode.g2re.jdbc.DataSet;
import com.googlecode.g2re.util.DataSetUtil;

/**
 * <p>A widget to render one or more gauges using SVG or VML.
 * Widget is provided as part of the Google Visualization suite.
 * {@linkplain http://code.google.com/apis/visualization/documentation/gallery/gauge.html}</p>
 * @author Brad Rydzewski
 */
public class Gauge extends BoundElement {

    private float width = 400f;

    private float height = 120f;

    private DataColumn nameColumn;

    private DataColumn valueColumn;

    public DataColumn getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(DataColumn valueColumn) {
        this.valueColumn = valueColumn;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public DataColumn getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(DataColumn nameColumn) {
        this.nameColumn = nameColumn;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void build(HTMLBuilderArgs args) {
        args.getScriptFiles().add("http://www.google.com/jsapi");
        args.getGoogleModules().add("gauge");
        int nameColInt = getNameColumn().getOrder();
        int valueColInt = getValueColumn().getOrder();
        DataSet ds = args.getResults().get(this.getDataQuery().getName());
        ds = DataSetUtil.getFilteredDataSet(ds, getFilters());
        int seq = args.getSequence();
        args.getPreScript().append("  var data" + seq + " = new google.visualization.DataTable(); ");
        args.getPreScript().append("  data" + seq + ".addColumn('string', 'Label'); ");
        args.getPreScript().append("  data" + seq + ".addColumn('number', 'Value'); ");
        args.getPreScript().append("  data" + seq + ".addRows(1); ");
        Object[] tmpRow = (Object[]) ds.getRows().get(0);
        args.getPreScript().append("  data" + seq + ".setValue(0,0,'").append(tmpRow[nameColInt]).append("'); ");
        args.getPreScript().append("  data" + seq + ".setValue(0,1,").append(tmpRow[valueColInt]).append("); ");
        args.getPreScript().append("   var chart" + seq + " = new google.visualization.Gauge(document.getElementById('chart_div" + seq + "')); ");
        args.getPreScript().append("   chart" + seq + ".draw(data" + seq + ", {width: " + getWidth() + ", height:" + getHeight() + "}); ");
        args.getHtml().append("<div id='chart_div" + seq + "' style='width: " + getWidth() + "px; height: " + getHeight() + "px;'></div>");
    }
}
