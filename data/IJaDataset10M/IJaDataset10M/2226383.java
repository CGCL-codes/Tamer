package net.sf.telamon.renderkit;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;
import net.sf.telamon.ajax.UIAjaxUpdate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.RendererUtils;

/**
 * Since <code>UIAjaxRegion</code> dont render itself ( only set apropriate event to parent component )
 * only <code>decode()</code> method implemented.
 * In case of Ajax Request caused by event for a given component
 * send action event.
 * @author shura (latest modification by $Author: alexandrsmirnov $)
 * @version $Revision: 1.1 $ $Date: 2005/10/24 18:36:46 $
 *
 */
public class AjaxUpdateRenderer extends Renderer {

    private static final Log log = LogFactory.getLog(AjaxUpdateRenderer.class);

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIAjaxUpdate.class);
        String clientId = uiComponent.getClientId(facesContext);
        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        if (paramMap.containsKey(clientId)) {
            if (log.isDebugEnabled()) {
                log.debug("Have request parameter for AjaxUpdate component " + clientId);
            }
            {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        }
    }

    public boolean getRendersChildren() {
        return false;
    }
}
