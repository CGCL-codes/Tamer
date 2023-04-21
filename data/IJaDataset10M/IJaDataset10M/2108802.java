package com.icesoft.faces.component.ext;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;

public class ColumnGroup extends UIPanel {

    public static final String COMPONENT_TYPE = "com.icesoft.faces.ColumnGroup";

    public static final String COMPONENT_FAMILY = "com.icesoft.faces.Column";

    private String renderedOnUserRole = null;

    public String getFamily() {
        return (COMPONENT_FAMILY);
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private transient Object values[];

    public void restoreState(FacesContext context, Object state) {
        values = (Object[]) state;
        super.restoreState(context, values[0]);
        renderedOnUserRole = (String) values[1];
    }

    public Object saveState(FacesContext context) {
        if (values == null) {
            values = new Object[2];
        }
        values[0] = super.saveState(context);
        values[1] = renderedOnUserRole;
        return values;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }
}
