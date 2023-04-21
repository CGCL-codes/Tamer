package common.Location;

import globals.GlobalObjects;
import globals.ObjectFactory;
import globals.ObjectType;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.graphics.ImageData;
import physics.GameObject;
import physics.Planet;
import common.IParse;
import common.Position3D;

public class GOLocationsMap implements IParse {

    private class Location {

        public int index;

        public Position3D position;
    }

    String mapFileName = null;

    ImageData mapImage = null;

    String planetName = null;

    Planet planet;

    ArrayList<Location> locations = new ArrayList<Location>();

    HashMap<Integer, String> indexNameMap = new HashMap<Integer, String>();

    private String name;

    public void commit() {
        mapImage = new ImageData(mapFileName);
        planet = (Planet) GlobalObjects.getGameObject(planetName);
        readMap();
        addObjectsToPlanet();
    }

    private void readMap() {
        for (int x = 0; x < mapImage.width; x++) {
            for (int y = 0; y < mapImage.height; y++) {
                createLocation(mapImage.getPixel(x, y), (double) x / (mapImage.width - 1), (double) y / (mapImage.height - 1));
            }
        }
    }

    private void createLocation(int data, double x, double y) {
        int index = data & 0xff;
        if (index == 0 || index == 255) return;
        Position3D position = new Position3D();
        position.getCenter().y += 1;
        position.rotate(0, 0, -Math.PI * y);
        position.rotate(0, -Math.PI * 2 * x, 0);
        Location location = new Location();
        location.index = index;
        location.position = position;
        locations.add(location);
    }

    private void addObjectsToPlanet() {
        for (Location location : locations) {
            int index = location.index;
            Position3D position = location.position;
            String name = indexNameMap.get(index);
            if (name == null) {
                continue;
            }
            GameObject go = ObjectFactory.createGameObject(ObjectType.valueOf(name));
            if (go == null) {
                continue;
            }
            go.setPlanet(planet);
            go.getCenter().copy(planet.getCenter());
            go.getCenter().add(position.getCenter());
            planet.pinpointToPlanet(go);
        }
    }

    public void setParameter(String name, String[] params) {
        if (name.equals("filename")) {
            mapFileName = params[0];
        }
        if (name.equals("index")) {
            int index = Integer.parseInt(params[0]);
            String objectName = params[1];
            indexNameMap.put(index, objectName);
        }
        if (name.equals("planet")) {
            planetName = params[0];
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
