package com.icteam.fiji.command.corporate.geographicArea;

import com.icteam.fiji.model.TipAreaGeogr;
import com.icteam.fiji.support.appl.DefaultSearchCriterium;

/**
 * Created by IntelliJ IDEA. User: s.ghezzi Date: 7-dic-2007 Time: 17.53.53
 */
public class SearchAreaGeogrCriterium extends DefaultSearchCriterium {

    private static final long serialVersionUID = 1L;

    private String areaGeogr = null;

    private TipAreaGeogr tipAreaGeogr = null;

    private Boolean searchFromStartOnly = Boolean.FALSE;

    public String getAreaGeogr() {
        return areaGeogr;
    }

    public void setAreaGeogr(String areaGeogr) {
        this.areaGeogr = areaGeogr;
    }

    public TipAreaGeogr getTipAreaGeogr() {
        return tipAreaGeogr;
    }

    public void setTipAreaGeogr(TipAreaGeogr tipAreaGeogr) {
        this.tipAreaGeogr = tipAreaGeogr;
    }

    public Boolean getSearchFromStartOnly() {
        return searchFromStartOnly;
    }

    public void setSearchFromStartOnly(Boolean searchFromStartOnly) {
        this.searchFromStartOnly = searchFromStartOnly;
    }
}
