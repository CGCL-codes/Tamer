package org.fpdev.apps.web;

import java.rmi.*;

/**
 * Remote interface for the main Five Points RMI Server. Servlets access these
 * methods when responding to web-based request.
 * 
 * @author demory
 */
public interface FPServer extends Remote {

    public String get5PHome() throws RemoteException;

    public String resolveLocationXML(String loc) throws RemoteException;

    public String findNearbyLocations(double x, double y, double r) throws RemoteException;

    public String batchCoordResolve(String coords) throws RemoteException;

    public String planTrip(short scenID, int triptype, String start, String end, int hour, int min, int ampm, String dayStr, String deparrStr, String optmodeStr, double nmspeed, double nmradius, double typefact, double topfact) throws RemoteException;

    public int[] getTopoGraph(String linkStr) throws RemoteException;

    public int[] getTopoGraphFromID(int id, int height) throws RemoteException;

    public String getGMapOverlay(String linkStr) throws RemoteException;

    public String getGMapOverlayFromID(int id) throws RemoteException;

    public String getRouteGMapOverlay(String fullID) throws RemoteException;
}
