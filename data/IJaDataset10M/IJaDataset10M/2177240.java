package com.lovebugs.xgrid.components;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lovebugs.OgnlContext;
import com.lovebugs.UIBean;
import com.lovebugs.xgrid.components.Column;
import com.lovebugs.xgrid.components.TopBar;

public class Grid extends UIBean {

    private String name;

    private String url;

    private boolean checkbox = true;

    private boolean bbar = false;

    private String apply;

    private String width;

    private String checkonly;

    private boolean autoload = true;

    private boolean columnLines = true;

    private String pagec = "15";

    private String view;

    private String gtpl;

    private String gsortinfo;

    private String gfield;

    private String gtotalfield;

    private List<Column> columns = new ArrayList<Column>();

    private List<TopBar> topbars = new ArrayList<TopBar>();

    public Grid(OgnlContext ognlContext, HttpServletRequest req, HttpServletResponse resp) {
        super(ognlContext, req, resp);
    }

    public void addComponent(UIBean subComponent) {
        if (subComponent instanceof Column) {
            columns.add((Column) subComponent);
        } else if (subComponent instanceof TopBar) {
            topbars.add((TopBar) subComponent);
        }
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public boolean getAutoload() {
        return autoload;
    }

    public void setAutoload(boolean autoload) {
        this.autoload = autoload;
    }

    public boolean getBbar() {
        return bbar;
    }

    public void setBbar(boolean bbar) {
        this.bbar = bbar;
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getCheckonly() {
        return checkonly;
    }

    public void setCheckonly(String checkonly) {
        this.checkonly = checkonly;
    }

    public String getGfield() {
        return gfield;
    }

    public void setGfield(String gfield) {
        this.gfield = gfield;
    }

    public String getGsortinfo() {
        return gsortinfo;
    }

    public void setGsortinfo(String gsortinfo) {
        this.gsortinfo = gsortinfo;
    }

    public String getGtotalfield() {
        return gtotalfield;
    }

    public void setGtotalfield(String gtotalfield) {
        this.gtotalfield = gtotalfield;
    }

    public String getGtpl() {
        return gtpl;
    }

    public void setGtpl(String gtpl) {
        this.gtpl = gtpl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPagec() {
        return pagec;
    }

    public void setPagec(String pagec) {
        this.pagec = pagec;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getTemplate() {
        return "grid";
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<TopBar> getTopbars() {
        return topbars;
    }

    public void setTopbars(List<TopBar> topbars) {
        this.topbars = topbars;
    }

    public boolean getColumnLines() {
        return columnLines;
    }

    public void setColumnLines(boolean columnLines) {
        this.columnLines = columnLines;
    }
}
