package com.dfruits.queries.ui;

import java.util.ArrayList;
import java.util.List;

public class Exporter {

    private List<Var> varsToExport = new ArrayList<Var>();

    public void addVar(Var var) {
        varsToExport.add(var);
    }

    public List<Var> getVarsToExport() {
        return varsToExport;
    }
}
