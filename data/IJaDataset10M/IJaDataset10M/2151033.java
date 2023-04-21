package info.reflectionsofmind.connexion.fortress.core.common.board.geometry;

import info.reflectionsofmind.connexion.fortress.core.common.util.Loop;

public interface IGeometry {

    ILocation getInitialLocation();

    int getNumberOfDirections();

    Loop<IDirection> getDirections();
}
