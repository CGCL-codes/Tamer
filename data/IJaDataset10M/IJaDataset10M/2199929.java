package org.apache.commons.chain.web.portlet;

import java.util.Locale;
import javax.portlet.PortletRequest;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.web.AbstractGetLocaleCommand;

/**
 * <p>Concrete implementation of {@link AbstractGetLocaleCommand} for
 * the Portlet API.</p>
 */
public class PortletGetLocaleCommand extends AbstractGetLocaleCommand {

    /**
     * <p>Retrieve and return the <code>Locale</code> for this request.</p>
     *
     * @param context The {@link Context} we are operating on.
     * @return The Locale for the request.
     */
    protected Locale getLocale(Context context) {
        PortletRequest request = (PortletRequest) context.get("request");
        return (request.getLocale());
    }
}
