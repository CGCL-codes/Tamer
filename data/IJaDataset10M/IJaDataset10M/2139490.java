package com.google.gwt.search.client.impl;

import com.google.gwt.search.client.ImageSearch;
import com.google.gwt.search.jsio.client.Constructor;
import com.google.gwt.search.jsio.client.JSOpaque;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * JSIO wrapper for GimageSearch.
 */
@Constructor("$wnd.GimageSearch")
public interface GimageSearch extends GSearch {

    GimageSearch IMPL = GWT.create(GimageSearch.class);

    JSOpaque RESULT_CLASS = new JSOpaque("$wnd.GimageSearch.RESULT_CLASS");

    @Constructor("$wnd.GimageSearch")
    JavaScriptObject construct();

    void setRestriction(ImageSearch obj, JSOpaque restrictionType);

    void setRestriction(ImageSearch obj, JSOpaque restrictionType, JSOpaque restrictionValue);

    void setSiteRestriction(ImageSearch obj, String site);
}
