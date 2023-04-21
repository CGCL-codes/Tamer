package com.idna.trace.utils;

import com.idna.trace.model.rr.xsd.Datasource;

public class UtilMethods {

    public static Datasource copyDatasourceShallow(Datasource datasource) {
        Datasource rtn = new Datasource();
        rtn.setAltText(datasource.getAltText());
        rtn.setColour(datasource.getColour());
        rtn.setDatasourceId(datasource.getDatasourceId());
        rtn.setDatasourceName(datasource.getDatasourceName());
        rtn.setDreamDatasource(datasource.getDreamDatasource());
        rtn.setIconText(datasource.getIconText());
        rtn.setIsBasic(datasource.getIsBasic());
        rtn.setIsChecked(datasource.getIsChecked());
        rtn.setIsDisplayed(datasource.getIsDisplayed());
        rtn.setIsGroup(datasource.getIsGroup());
        rtn.setScope(datasource.getScope());
        return rtn;
    }
}
