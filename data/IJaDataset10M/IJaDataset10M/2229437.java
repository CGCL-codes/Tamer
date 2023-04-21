package de.mogwai.common.web.component.renderkit.html.action;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.component.action.PageRefreshComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Command button renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:46 $
 */
public class PageRefreshRenderer extends BaseCommandRenderer {

    private static final Logger LOGGER = new Logger(PageRefreshRenderer.class);

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {
        PageRefreshComponent theComponent = (PageRefreshComponent) aComponent;
        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        String theGUID = theUtilities.getCurrentGUID(aContext);
        if (theUtilities.isGUIDAlreadyUsed(aContext, theGUID)) {
            LOGGER.logDebug("Invoking action method for component " + theComponent + " as a page resubmit was caused");
            ActionEvent theActionEvent = new ActionEvent(theComponent);
            theComponent.queueEvent(theActionEvent);
        }
    }

    @Override
    protected String getDisabledClass() {
        return null;
    }

    @Override
    protected String getEnabledClass() {
        return null;
    }
}
