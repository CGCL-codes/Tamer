package com.wrupple.muba.catalogs.client.module.services.logic.impl;

import java.util.List;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayBoolean;
import com.google.gwt.core.client.JsArrayString;
import com.wrupple.muba.catalogs.domain.CatalogEntry;
import com.wrupple.vegetate.client.util.JsArrayList;
import com.wrupple.vegetate.domain.CatalogResponseContract;

public final class JsCatalogResponseContract extends JavaScriptObject implements CatalogResponseContract {

    protected JsCatalogResponseContract() {
    }

    @Override
    public native String getError();

    @Override
    public native List<CatalogEntry> getResponse();

    public native JsArray<JavaScriptObject> getResponseAsJSOList();

    public native JsArrayBoolean getResponseAsBooleanList();

    @Override
    public List<String> getWarnings() {
        return JsArrayList.arrayAsListOfString(getWarningsArray());
    }

    public native JsArrayString getWarningsArray();
}
