package gmusic.bo;

import java.util.ArrayList;
import java.util.List;

public class QueryResultBean {

    private List<List<String>> records;

    private List<String> columnList;

    public QueryResultBean() {
        records = new ArrayList<List<String>>();
    }

    public List<List<String>> getRecords() {
        return records;
    }

    public void setRecords(List<List<String>> records) {
        this.records = records;
    }

    public void addRecord(List<String> rec) {
        records.add(rec);
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }
}
