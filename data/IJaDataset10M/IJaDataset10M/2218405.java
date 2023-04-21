package org.vardb.web.json;

import org.vardb.analysis.IAnalysis;
import org.vardb.util.CDateHelper;

public class CAnalysisJson extends CAbstractJson {

    protected String id;

    protected String dtype;

    protected String date;

    public CAnalysisJson(IAnalysis analysis) {
        this.id = analysis.getId();
        this.dtype = analysis.getDtype().name();
        this.date = CDateHelper.format(analysis.getDate(), CDateHelper.DATETIME_PATTERN);
    }
}
