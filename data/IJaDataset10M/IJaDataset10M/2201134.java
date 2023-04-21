package com.safi.workshop.navigator;

import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import com.safi.db.server.config.SafiServer;
import com.safi.server.plugin.SafiServerPlugin;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;

public class ServerResourcesDecorator extends org.eclipse.jface.viewers.LabelProvider implements ILightweightLabelDecorator {

    private static final String SAFI_SERVER_CONNECTED_ICON = "icons/decorators/safiserver_connected.gif";

    private static final String SAFI_SERVER_DISCONNECTED_ICON = "icons/decorators/safiserver_disconnected.gif";

    private static final String DECORATOR_ID = "com.safi.workshop.navigator.ServerResourcesDecorator";

    public static ServerResourcesDecorator getServerResourcesDecorator() {
        IDecoratorManager decoratorManager = AsteriskDiagramEditorPlugin.getDefault().getWorkbench().getDecoratorManager();
        if (decoratorManager.getEnabled(DECORATOR_ID)) {
            ServerResourcesDecorator dec = (ServerResourcesDecorator) decoratorManager.getBaseLabelProvider(DECORATOR_ID);
            if (dec == null) {
            }
        }
        return null;
    }

    public ServerResourcesDecorator() {
        System.err.println("makin serverresources");
    }

    @Override
    public void decorate(Object element, IDecoration decoration) {
        System.err.println("Tryin to dec " + element + " of typ e " + element.getClass());
        if (element instanceof SafiServer) {
            decorateSafiServer((SafiServer) element, decoration);
        }
    }

    private void decorateSafiServer(SafiServer server, IDecoration decoration) {
        System.err.println("Decorating safiserver " + server);
        {
            if (!SafiServerPlugin.getDefault().isConnected()) {
                decoration.addOverlay(getImageDescriptor(SAFI_SERVER_DISCONNECTED_ICON), IDecoration.BOTTOM_LEFT);
            } else {
                decoration.addOverlay(getImageDescriptor(SAFI_SERVER_CONNECTED_ICON), IDecoration.BOTTOM_LEFT);
            }
        }
    }

    private ImageDescriptor getImageDescriptor(String path) {
        return AsteriskDiagramEditorPlugin.getBundledImageDescriptor(path);
    }

    public void refresh(List resourcesToBeUpdated) {
        fireLabelEvent(new LabelProviderChangedEvent(this, resourcesToBeUpdated.toArray()));
    }

    public void refresh() {
        fireLabelEvent(new LabelProviderChangedEvent(this));
    }

    private void fireLabelEvent(final LabelProviderChangedEvent event) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                fireLabelProviderChanged(event);
            }
        });
    }

    public static void updateServerResourcesDecorators() {
        try {
            ServerResourcesDecorator dec = getServerResourcesDecorator();
            if (dec != null) dec.refresh();
        } catch (Exception e) {
            AsteriskDiagramEditorPlugin.getInstance().logError("Couldn't refresh Server Resources decorators", e);
        }
    }
}
