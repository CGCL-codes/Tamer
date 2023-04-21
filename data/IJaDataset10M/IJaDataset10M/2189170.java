package org.sgodden.echo.ext20.peers;

import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;
import nextapp.echo.webcontainer.sync.component.LabelPeer;
import org.sgodden.echo.ext20.ApplicationWaitIndicator;

public class ApplicationWaitIndicatorPeer extends LabelPeer {

    public ApplicationWaitIndicatorPeer() {
        super();
    }

    public String getClientComponentType(boolean shortType) {
        return shortType ? "E2AWI" : "Ext20ApplicationWaitIndicator";
    }

    /**
     * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getComponentClass()
     */
    @SuppressWarnings("unchecked")
    public Class getComponentClass() {
        return ApplicationWaitIndicator.class;
    }

    /**
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#init(Context)
     */
    @Override
    public void init(Context context, Component c) {
        super.init(context, c);
    }
}
