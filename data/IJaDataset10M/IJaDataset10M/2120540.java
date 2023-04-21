package com.lovebugs.xgrid.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lovebugs.OgnlContext;
import com.lovebugs.UIBean;

public class ColumnAction extends UIBean {

    private String id;

    private String icon;

    private String tooltip;

    private String winWidth = "600";

    private String winHeight = "500";

    private String url;

    private String handler;

    private String map;

    private String label;

    private boolean ajax = false;

    private String target = null;

    private boolean showline;

    public ColumnAction(OgnlContext ognlContext, HttpServletRequest req, HttpServletResponse resp) {
        super(ognlContext, req, resp);
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public boolean isShowline() {
        return showline;
    }

    public void setShowline(boolean showline) {
        this.showline = showline;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWinHeight() {
        return winHeight;
    }

    public void setWinHeight(String winHeight) {
        this.winHeight = winHeight;
    }

    public String getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(String winWidth) {
        this.winWidth = winWidth;
    }
}
