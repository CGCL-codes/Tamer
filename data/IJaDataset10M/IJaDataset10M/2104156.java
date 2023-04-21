package com.ynhenc.gis.web;

import com.ynhenc.comm.GisComLib;

public class MapSession extends GisComLib {

    public Request getReequest() {
        return this.request;
    }

    public long getLastAccessedTime() {
        Request request = this.getReequest();
        if (request != null) {
            Session session = request.getSession();
            if (session != null) {
                long lat = session.getLastAccessedTime();
                return lat;
            }
        }
        return -1;
    }

    public double getNo() {
        return no;
    }

    private MapSession(double no, Request request) {
        super();
        this.no = no;
        this.request = request;
    }

    private double no;

    private Request request;

    public static MapSession getMapSession(double no, Request req) {
        MapSession mapSession = mapSessionHash.get(no);
        if (mapSession == null) {
            mapSession = new MapSession(no, req);
            mapSessionHash.put(no, mapSession);
        }
        return mapSession;
    }

    public static MapSession nullMapSesion = new MapSession(-1, null);

    private static MapSessionHash mapSessionHash = new MapSessionHash();
}
