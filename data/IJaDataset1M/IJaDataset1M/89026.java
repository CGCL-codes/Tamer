package org.jdbc4olap.xmla;

import java.util.List;

public class QueryFilterOperand {

    private QueryColumn col;

    private List<String> valList;

    public QueryColumn getCol() {
        return col;
    }

    public void setCol(QueryColumn col) {
        this.col = col;
        this.valList = null;
    }

    public List<String> getValList() {
        return valList;
    }

    public void setValList(List<String> valList) {
        this.valList = valList;
        this.col = null;
    }
}
