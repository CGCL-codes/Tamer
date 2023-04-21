package com.google.gwt.user.datepicker.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * A simple {@link MonthSelector} used for the default date picker. Not
 * extensible as we wish to evolve it freely over time.
 */
public final class DefaultMonthSelector extends MonthSelector {

    private PushButton backwards;

    private PushButton forwards;

    private Grid grid;

    /**
   * Constructor.
   */
    public DefaultMonthSelector() {
    }

    @Override
    protected void refresh() {
        String formattedMonth = getModel().formatCurrentMonth();
        grid.setText(0, 1, formattedMonth);
    }

    @Override
    protected void setup() {
        backwards = new PushButton();
        backwards.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addMonths(-1);
            }
        });
        backwards.getUpFace().setHTML("&laquo;");
        backwards.setStyleName(css().previousButton());
        forwards = new PushButton();
        forwards.getUpFace().setHTML("&raquo;");
        forwards.setStyleName(css().nextButton());
        forwards.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addMonths(+1);
            }
        });
        grid = new Grid(1, 3);
        grid.setWidget(0, 0, backwards);
        grid.setWidget(0, 2, forwards);
        CellFormatter formatter = grid.getCellFormatter();
        formatter.setStyleName(0, 1, css().month());
        formatter.setWidth(0, 0, "1");
        formatter.setWidth(0, 1, "100%");
        formatter.setWidth(0, 2, "1");
        grid.setStyleName(css().monthSelector());
        initWidget(grid);
    }
}
