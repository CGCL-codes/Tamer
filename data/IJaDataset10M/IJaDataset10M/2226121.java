package org.opennms.gwt.web.ui.asset.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * EntryPoint of asset module. Following GWT MVP design.
 */
public class AssetPage implements EntryPoint {

    private AssetServiceAsync m_assetServiceAsync;

    public void onModuleLoad() {
        AssetServiceAsync rpcService = getAssetService();
        HandlerManager eventBus = new HandlerManager(null);
        AppController appViewer = new AppController(rpcService, eventBus);
        if (RootPanel.get("opennms-assetNodePage") != null) {
            appViewer.go(RootPanel.get("opennms-assetNodePage"));
        }
    }

    private AssetServiceAsync getAssetService() {
        if (m_assetServiceAsync == null) {
            String serviceEntryPoint = GWT.getHostPageBaseURL() + "assetService.gwt";
            final AssetServiceAsync svc = (AssetServiceAsync) GWT.create(AssetService.class);
            ServiceDefTarget endpoint = (ServiceDefTarget) svc;
            endpoint.setServiceEntryPoint(serviceEntryPoint);
            m_assetServiceAsync = svc;
        }
        return m_assetServiceAsync;
    }
}
