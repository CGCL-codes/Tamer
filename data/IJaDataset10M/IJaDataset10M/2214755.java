package org.boehn.kmlframework.todo.examples;

import java.io.IOException;
import org.boehn.kmlframework.coordinates.CartesianCoordinate;
import org.boehn.kmlframework.coordinates.EarthCoordinate;
import org.boehn.kmlframework.kml.Kml;
import org.boehn.kmlframework.kml.KmlException;
import org.boehn.kmlframework.todo.MapObject;
import org.boehn.kmlframework.todo.ModelObjectFactory;

public class ModelObjectFactoryExample {

    public static void main(String[] args) throws KmlException, IOException {
        try {
            Kml model = new Kml();
            ModelObjectFactory modelObjectFactory = new ModelObjectFactory("resources/symbols/symbols.xml");
            MapObject boat = modelObjectFactory.createMapObject("boat");
            boat.setLocation(new EarthCoordinate(59.8959, 10.6406));
            boat.setScale(new CartesianCoordinate(30, 150, 30));
            boat.setRotation(Math.toRadians(45d));
            boat.setLocalReferenceCoordinate(new CartesianCoordinate(2, 13, 0));
            System.out.println("The kml file was generated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
