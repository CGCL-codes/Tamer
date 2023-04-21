package com.worthsoln.patientview;

import java.util.ArrayList;
import java.util.Collection;
import com.worthsoln.database.StorableItem;

public class ResultDao extends StorableItem {

    private Result result;

    public ResultDao(Result result) {
        this.result = result;
    }

    public ResultDao() {
        super();
    }

    public String[] getColumnNames() {
        return new String[] { "timeStamp", "urea", "creat", "potassium", "calcium", "phosphate", "haemo", "wbc", "plat", "cni" };
    }

    public ArrayList getColumnParameters() {
        ArrayList params = new ArrayList();
        params.add(result.getTimeStamp());
        return params;
    }

    public Object getIdParameter() {
        return null;
    }

    public Class getTableMapper() {
        return Result.class;
    }

    public String getIdColumnName() {
        return "nhsno";
    }

    public String getTableName() {
        return "result";
    }

    public Collection getRetrieveListWhereClauseParameters() {
        ArrayList params = new ArrayList();
        return params;
    }

    public String getRetrieveListWhereClauseSql() {
        return "nhsno";
    }
}
