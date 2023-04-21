package org.apache.myfaces.custom.focus;

import javax.faces.component.UIComponentBase;

/**
 * @author Rogerio Pereira Araujo (latest modification by $Author: rpa_rio $)
 * @version $Revision: 352 $ $Date: 2005-11-28 16:00:15 -0500 (Mon, 28 Nov 2005) $
 */
public class HtmlFocus extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.apache.myfaces.Focus";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Focus";

    public static final String COMPONENT_FAMILY = "javax.faces.Output";

    private String _for = null;

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public String getFor() {
        return _for;
    }
}
