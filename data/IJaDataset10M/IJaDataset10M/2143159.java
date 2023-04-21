package com.be.bo;

import java.sql.Date;
import java.util.Vector;
import com.be.vo.QueryParameterVO;

public class QueryParameterContainer {

    private Vector<QueryParameterVO> paramV = new Vector<QueryParameterVO>();

    private long templateID;

    public QueryParameterContainer(long templateID) {
        this.templateID = templateID;
    }

    public void addParameter(long templateID, String paramName, String paramType, String text) {
        this.templateID = templateID;
        addParameter(paramName, paramType, text);
    }

    public void addParameter(String paramName, String paramType, String text) {
        QueryParameterVO vo = new QueryParameterVO(templateID, paramName, paramType);
        vo.setTextParam(text);
        paramV.add(vo);
    }

    public void addParameter(long templateID, String paramName, String paramType, Date date) {
        this.templateID = templateID;
        addParameter(paramName, paramType, date);
    }

    public void addParameter(String paramName, String paramType, Date date) {
        QueryParameterVO vo = new QueryParameterVO(templateID, paramName, paramType);
        vo.setDateParam(date);
        paramV.add(vo);
    }

    public QueryParameterVO[] getParameterArray() {
        QueryParameterVO[] list = new QueryParameterVO[paramV.size()];
        for (int i = 0; i < paramV.size(); i++) {
            list[i] = (QueryParameterVO) paramV.elementAt(i);
        }
        return list;
    }
}
