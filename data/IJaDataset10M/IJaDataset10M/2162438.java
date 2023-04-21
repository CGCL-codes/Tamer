package gov.ca.bdo.modeling.dsm2.map.client.map;

import gov.ca.bdo.modeling.dsm2.map.client.model.RegularTimeSeries;
import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.AnnotatedLegendPosition;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.Options;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine.ScaleType;

public class OutputPanel extends Composite {

    private final FlowPanel container;

    private final String name;

    public OutputPanel(String name, String outputVariables, OutputMarkerDataManager manager) {
        this.name = name;
        String[] variables = null;
        if (outputVariables != null) {
            variables = outputVariables.split(",");
        }
        manager.getRegularTimeSeries(name, variables, this);
        container = new FlowPanel();
        initWidget(container);
    }

    @SuppressWarnings("deprecation")
    public void displayData(List<RegularTimeSeries> list) {
        if ((list == null) || (list.size() == 0)) {
            displayEmptyDataNotice();
            return;
        }
        Options options = Options.create();
        options.setDisplayAnnotations(false);
        options.setScaleColumns(0, 1);
        options.setScaleType(ScaleType.ALLMAXIMIZE);
        DataTable table = DataTable.create();
        table.addColumn(ColumnType.DATE, "Date");
        int maxrows = 0;
        for (RegularTimeSeries regularTimeSeries : list) {
            table.addColumn(ColumnType.NUMBER, regularTimeSeries.getType());
            maxrows = Math.max(maxrows, regularTimeSeries.getData().length);
        }
        table.addRows(maxrows);
        RegularTimeSeries timeSeries = list.get(0);
        Date date = timeSeries.getStartTime();
        for (int i = 0; i < maxrows; i++) {
            table.setValue(i, 0, date);
            date = new Date(date.getTime());
            date.setDate(date.getDate() + 1);
        }
        for (int i = 0; i < list.size(); i++) {
            RegularTimeSeries regularTimeSeries = list.get(i);
            double[] data = regularTimeSeries.getData();
            for (int j = 0; j < data.length; j++) {
                table.setValue(j, i + 1, data[j]);
            }
        }
        int width = Math.max(500, container.getOffsetWidth());
        int height = (int) Math.round(width / 1.618);
        AnnotatedTimeLine chart = new AnnotatedTimeLine(table, options, width + "px", height + "px");
        options.setLegendPosition(AnnotatedLegendPosition.SAME_ROW);
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.add(new HTML("<h3>Location: " + name + "</h3>"));
        container.add(chart);
    }

    public void displayError(Throwable caught) {
        container.clear();
        String errorMessage = "Could not retrieve data. Error message says " + caught.getMessage();
        HTMLPanel htmlPanel = new HTMLPanel(errorMessage);
        container.add(htmlPanel);
        String possibleCause = "Please check your connection.";
        container.add(new HTML(possibleCause));
    }

    public void displayEmptyDataNotice() {
        container.clear();
        container.add(new HTMLPanel("<h3 style='color:red'>No data available</h3>"));
    }
}
