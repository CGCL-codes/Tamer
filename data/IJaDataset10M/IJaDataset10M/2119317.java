package com.silrais.jdog.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.silrais.jdog.biz.domain.QueryResultDataSet;
import com.silrais.toolkit.dataset.SimpleDataSet;
import com.silrais.toolkit.dataset.SimpleRow;
import com.silrais.toolkit.util.SQLFilter;
import com.silrais.toolkit.util.SimpleMap;
import com.silrais.toolkit.util.SimpleUtil;

public class ExecuteQueryController extends JDOGBaseActionController {

    public static final String RS_AXN_NEXT = "nxt";

    public static final String RS_AXN_PREV = "prv";

    public static final String RS_AXN_SORT = "srt";

    public static final String RS_AXN_FILTER = "flr";

    public static final String RS_AXN_CH_DISP_MODE = "cdm";

    protected int maxRecordCountPerPage = 25;

    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) {
        String query = null;
        String action = getStrParamB4Attr(req, "axn");
        String qryName = getStrParamB4Attr(req, "qryname");
        String qryType = getStrParamB4Attr(req, "qrytype");
        if (SimpleUtil.isnotnull(action) && action.equalsIgnoreCase("exec")) {
            query = getStrParamB4Attr(req, "qry");
        } else {
            query = (String) req.getSession().getAttribute("qry");
        }
        if (SimpleUtil.isnull(query)) {
            SimpleMap map = new SimpleMap();
            map.put("0", "Enter the query");
            req.setAttribute("err", map);
            return new ModelAndView("error");
        }
        boolean refreshData = true;
        boolean isNewFilter = false;
        String dsaxn = getStrParam(req, "dsaxn");
        String sortCol = getStrParam(req, "scolidx");
        String sortOdr = getStrParam(req, "sodr");
        String dsid = getStrParam(req, "dsid");
        int curFirstRowId = getIntParam(req, "cfrid");
        if (curFirstRowId < 1) {
            curFirstRowId = 1;
        }
        if (RS_AXN_NEXT.equalsIgnoreCase(dsaxn)) {
            curFirstRowId += maxRecordCountPerPage;
        } else if (RS_AXN_PREV.equalsIgnoreCase(dsaxn)) {
            curFirstRowId -= maxRecordCountPerPage;
        } else if (RS_AXN_SORT.equalsIgnoreCase(dsaxn)) {
            curFirstRowId = 1;
        } else if (RS_AXN_FILTER.equalsIgnoreCase(dsaxn)) {
            isNewFilter = true;
        } else if (RS_AXN_CH_DISP_MODE.equalsIgnoreCase(dsaxn)) {
            refreshData = false;
        }
        SQLFilter filter = new SQLFilter();
        if (!SimpleUtil.isnull(sortCol)) {
            filter.setOrderByClauseList(new String[] { sortCol });
        }
        if (!SimpleUtil.isnull(sortOdr)) {
            filter.setSortOrder(sortOdr);
        }
        filter.setBeginRowId(curFirstRowId);
        filter.setMaxRowCount(getMaxRecordCountPerPage());
        QueryResultDataSet dataSet = null;
        if (refreshData) {
            dataSet = jdogService.executeQuery(query, filter);
        } else {
            dataSet = (QueryResultDataSet) req.getSession().getAttribute(dsid);
        }
        if (dataSet.getErrorCode() == null) {
            req.getSession().setAttribute(dataSet.getId(), dataSet);
            if ("exec".equals(action) && "usrqry".equals(qryType)) {
                SimpleDataSet histDataSet = (SimpleDataSet) req.getSession().getAttribute("qryhist");
                if (histDataSet == null) {
                    histDataSet = new SimpleDataSet();
                }
                SimpleRow tmpRow = new SimpleRow();
                tmpRow.add(query.trim());
                tmpRow.add(dataSet.getQryExecTime());
                tmpRow.add(dataSet.getTimeTaken());
                histDataSet.addDataRow(tmpRow);
                req.getSession().setAttribute("qryhist", histDataSet);
            }
            req.getSession().setAttribute("qry", query.trim());
            Map model = new HashMap();
            model.put("TableData", dataSet);
            model.put("cfrid", curFirstRowId);
            model.put("dsid", dataSet.getId());
            model.put("qryname", qryName);
            model.put("mxrecperpage", getMaxRecordCountPerPage());
            model.put("flr.whereClauseMap", filter.getWhereClauseMap());
            return new ModelAndView("querydataview", "model", model);
        } else {
            SimpleMap map = new SimpleMap();
            map.put("0", dataSet.getErrorCode());
            req.setAttribute("err", map);
            return new ModelAndView("error");
        }
    }

    public void setMaxRecordCountPerPage(int mrcpp) {
        this.maxRecordCountPerPage = mrcpp;
    }

    public int getMaxRecordCountPerPage() {
        return this.maxRecordCountPerPage;
    }
}
