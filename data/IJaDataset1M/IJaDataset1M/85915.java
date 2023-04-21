package org.jrobin.graph;

import org.jrobin.data.DataProcessor;

class CDef extends Source {

    private final String rpnExpression;

    CDef(String name, String rpnExpression) {
        super(name);
        this.rpnExpression = rpnExpression;
    }

    void requestData(DataProcessor dproc) {
        dproc.addDatasource(name, rpnExpression);
    }
}
