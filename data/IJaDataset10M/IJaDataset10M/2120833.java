package com.xpresso.utils.maps;

public interface MapDataProvider {

    public Coordinate getCoordinatesFromAddress(Address a) throws MapException;

    public Coordinate[] getCoordinatesForPath(Address origin, Address destination) throws MapException;

    public Tile[] getTitlesForPath(Coordinate[] coordinates, int zoom) throws MapException;
}
