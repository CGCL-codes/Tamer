package com.softaspects.jsf.component.table;

import com.softaspects.jsf.component.base.WGFComponentBase;

/**
 * CellEdit Component
 */
public class BaseCellEdit extends WGFComponentBase {

    public static final String COMPONENT_TYPE = "com.softaspects.jsf.component.table.CellEdit";

    public static final String RENDERER_TYPE = "com.softaspects.jsf.renderer.table.CellEditRenderer";

    public String getComponentTypeName() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }

    public Class[] getPossibleChildComponents() {
        return new Class[] {};
    }
}
