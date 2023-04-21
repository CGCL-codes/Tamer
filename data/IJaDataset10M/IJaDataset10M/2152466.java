package net.videgro.oma.web;

import javax.servlet.http.HttpServletRequest;
import net.videgro.oma.business.MemberFilter;

public class RequestParameters {

    private int functionId = 0;

    private String department = "testdepartment";

    private int columns = 4;

    private String format = "";

    private Boolean htmlheader = false;

    private String[] filterNames = { MemberFilter.FILTER_APPROVED };

    private String[] filterValues = { MemberFilter.PLACEHOLDER };

    public void parse(HttpServletRequest request) {
        if (request.getParameter("function") != null) functionId = Integer.parseInt(request.getParameter("function"));
        if (request.getParameter("department") != null) department = request.getParameter("department");
        if (request.getParameter("format") != null) format = request.getParameter("format");
        if (request.getParameter("columns") != null) columns = Integer.parseInt(request.getParameter("columns"));
        if (request.getParameter("htmlheader") != null) htmlheader = request.getParameter("htmlheader").equals("1");
        if (request.getParameter("filterName") != null) {
            filterNames[0] = request.getParameter("filterName");
            filterValues[0] = request.getParameter("filterValue");
        }
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Boolean getHtmlheader() {
        return htmlheader;
    }

    public void setHtmlheader(Boolean htmlheader) {
        this.htmlheader = htmlheader;
    }

    public String[] getFilterNames() {
        return filterNames;
    }

    public void setFilterNames(String[] filterNames) {
        this.filterNames = filterNames;
    }

    public String[] getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(String[] filterValues) {
        this.filterValues = filterValues;
    }

    public String getFormat() {
        return format;
    }
}
