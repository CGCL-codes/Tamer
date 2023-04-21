package org.zkoss.zkdemo;

import java.util.ArrayList;
import org.zkoss.zktest.model.EmployeeService2;
import org.zkoss.zul.Grid;
import org.zkoss.zul.SimpleListModel;

public class MyGrid2 extends Grid {

    protected static EmployeeService2 provider;

    public ArrayList getEmployees() {
        return provider.getEmployees();
    }

    public SimpleListModel getMyModel() {
        return new SimpleListModel(getEmployees());
    }

    public MyRowRenderer getMyRenderer() {
        return new MyRowRenderer();
    }

    public void onCreate() {
        setModel(getMyModel());
        setRowRenderer(getMyRenderer());
    }

    static {
        provider = new EmployeeService2();
    }
}
