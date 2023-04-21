package org.apache.myfaces.examples.colspanexample;

import java.util.ArrayList;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

public class CellLineBean {

    public DataModel getCells() {
        ArrayList a = new ArrayList();
        a.add(new Cell("1"));
        a.add(new Cell("2"));
        a.add(new Cell("3"));
        a.add(new Cell("4"));
        a.add(new Cell("5"));
        DataModel testModel = new ListDataModel();
        testModel.setWrappedData(a);
        return testModel;
    }
}
