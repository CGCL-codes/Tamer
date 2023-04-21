package at.riemers.zero.widgets;

import at.riemers.zero.base.controller.ZeroView;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tobias
 */
public class TableClickEvent {

    private int column;

    private int row;

    private TableWidget component;

    private ZeroView view;

    private HttpServletRequest request;

    public TableClickEvent(TableWidget component, int column, int row, HttpServletRequest request, ZeroView view) {
        this.column = column;
        this.row = row;
        this.component = component;
        this.request = request;
        this.view = view;
    }

    public int getColumn() {
        return column;
    }

    public TableWidget getWidget() {
        return component;
    }

    public int getRow() {
        return row;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public ZeroView getView() {
        return view;
    }

    public void setView(ZeroView view) {
        this.view = view;
    }
}
