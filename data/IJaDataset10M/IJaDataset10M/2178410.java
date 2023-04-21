package by.bsuir.picasso.client.service;

import com.google.gwt.core.client.GWT;

public class ServiceHelper {

    private final MapsDataServiceAsync mapsDataService = GWT.create(MapsDataService.class);

    private final MarkersDataServiceAsync markersDataService = GWT.create(MarkersDataService.class);

    private final PolyDataServiceAsync polyDataService = GWT.create(PolyDataService.class);

    private final LoginServiceAsync loginService = GWT.create(LoginService.class);

    public MapsDataServiceAsync getMapsDataService() {
        return mapsDataService;
    }

    public MarkersDataServiceAsync getMarkersDataService() {
        return markersDataService;
    }

    public LoginServiceAsync getLoginService() {
        return loginService;
    }

    public PolyDataServiceAsync getPolyDataService() {
        return polyDataService;
    }
}
