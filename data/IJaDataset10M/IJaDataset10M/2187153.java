package com.sunshulin.common.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sunshulin.service.GeneralCriteria;

public abstract class GeneralAction extends ActionSupport {

    private static final long serialVersionUID = -371809317698545233L;

    protected int page = 1;

    protected int rows = 10;

    protected String sort;

    protected String order;

    protected String param;

    public void out(String str) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(str);
        out.close();
    }

    /**
	 * 分页处理
	 * @param model
	 */
    public void page(GeneralCriteria model) {
        model.setCurrentPage((page - 1) * rows);
        model.setPage(page);
        model.setLine(rows);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
