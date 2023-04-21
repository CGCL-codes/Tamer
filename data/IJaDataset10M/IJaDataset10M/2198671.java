package gov.ca.bdo.modeling.dsm2.map.client;

import gov.ca.bdo.modeling.dsm2.map.client.service.DSM2InputService;
import gov.ca.bdo.modeling.dsm2.map.client.service.DSM2InputServiceAsync;
import gov.ca.bdo.modeling.dsm2.map.client.service.LoginService;
import gov.ca.bdo.modeling.dsm2.map.client.service.LoginServiceAsync;
import gov.ca.bdo.modeling.dsm2.map.client.service.UserProfileService;
import gov.ca.bdo.modeling.dsm2.map.client.service.UserProfileServiceAsync;
import gov.ca.modeling.maps.elevation.client.service.BathymetryDataService;
import gov.ca.modeling.maps.elevation.client.service.BathymetryDataServiceAsync;
import gov.ca.modeling.maps.elevation.client.service.DEMDataService;
import gov.ca.modeling.maps.elevation.client.service.DEMDataServiceAsync;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.logging.client.FirebugLogHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MainEntryPoint implements EntryPoint {

    public void onModuleLoad() {
        Logger logger = Logger.getLogger("");
        String version = "1.0";
        Handler[] handlers = logger.getHandlers();
        if (handlers != null) {
            for (Handler h : handlers) {
                logger.removeHandler(h);
            }
        }
        logger.addHandler(new FirebugLogHandler());
        logger.setLevel(Level.FINE);
        logger.fine("onModuleLoad");
        LoginServiceAsync loginService = GWT.create(LoginService.class);
        DSM2InputServiceAsync rpcService = GWT.create(DSM2InputService.class);
        UserProfileServiceAsync userProfileService = GWT.create(UserProfileService.class);
        BathymetryDataServiceAsync bathymetryService = GWT.create(BathymetryDataService.class);
        DEMDataServiceAsync demService = GWT.create(DEMDataService.class);
        SimpleEventBus eventBus = new SimpleEventBus();
        final AppController appViewer = new AppController(loginService, rpcService, userProfileService, bathymetryService, demService, eventBus);
        appViewer.go(RootLayoutPanel.get());
        logger.fine("onModuleLoad end");
    }
}
