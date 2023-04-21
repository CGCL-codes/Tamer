package phworld.mapeditor;

import java.util.*;
import dsc.netgame.*;
import phworld.*;

/**
   EditorMapLoader extends phworld MapLoader that 
   loads given mapfile and parses it. 
*/
class EditorMapLoader extends phworld.MapLoader {

    private String mapFile;

    private int xSize, ySize;

    private Vector planetVector;

    public EditorMapLoader(String mapFile) {
        super(null, mapFile, GlobalDefines.maxPlayers);
        if (!isMapValid()) {
            System.err.println("Unable to load map. Giving up.\n");
            System.exit(1);
        }
        xSize = getMapXSize();
        ySize = getMapYSize();
    }

    protected void placePlanet(Location location, String name, String type, int owner, String resources) {
        PlanetEditorPlanet pl;
        if (!name.equals("")) {
            if (name.startsWith("\"") && name.lastIndexOf('\"') == name.length() - 1) {
                name = name.substring(1, name.length() - 1);
            }
        }
        if (resources.equals("-")) {
            resources = "-/-/-";
        }
        pl = new PlanetEditorPlanet(location.getX(), location.getY(), type, owner, resources, name);
        if (planetVector == null) planetVector = new Vector();
        planetVector.addElement(pl);
    }

    public Vector getPlanetVector() {
        return planetVector;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }
}
