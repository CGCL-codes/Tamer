package org.apache.myfaces.trinidadinternal.uinode.bind;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputText;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.data.BoundValue;

/**
 * BoundValue that returns the clientId of the component.
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class ClientIdBoundValue implements BoundValue {

    /**
   * Creates a ClientIdBoundValue.
   * @param component the UIComponent
   */
    public ClientIdBoundValue(UIComponent component) {
        this(component, false);
    }

    /**
   * Creates a ClientIdBoundValue.
   * @param component the UIComponent
   * @param onlyIfIdSet if true, ClientIdBoundValue will
   *   return null when "id" is null.
   */
    public ClientIdBoundValue(UIComponent component, boolean onlyIfIdSet) {
        if (component == null) throw new NullPointerException();
        _component = component;
        _onlyIfIdSet = onlyIfIdSet;
    }

    public Object getValue(UIXRenderingContext context) {
        if (_onlyIfIdSet) {
            String key = CoreOutputText.PARTIAL_TRIGGERS_KEY.getName();
            if (_component.getAttributes().get(key) == null) {
                String id = _component.getId();
                if (id == null) return null;
                if (id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) return null;
            }
        }
        FacesContext fContext = (context == null) ? FacesContext.getCurrentInstance() : context.getFacesContext();
        return _component.getClientId(fContext);
    }

    private final UIComponent _component;

    private final boolean _onlyIfIdSet;
}
