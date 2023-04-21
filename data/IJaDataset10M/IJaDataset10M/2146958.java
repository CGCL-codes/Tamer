package com.michaelbelyakov1967.resources.projects.GPS.client;

import java.sql.Timestamp;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBaseable extends RemoteService {

    enum Action {

        SINGLE, ALL, REPORT
    }

    ;

    String[][] getVehicles();

    Marker[] getMarkers();

    boolean prepareMarkers(String start, String end, int[] ids, Action action);

    boolean getReport();

    boolean login(String login, String password);
}
