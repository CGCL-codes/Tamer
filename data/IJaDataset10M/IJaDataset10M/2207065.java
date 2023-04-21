package catchemrpg.persistency;

import catchemrpg.base.BaseVars;
import catchemrpg.gameobjects.MapMeta;
import java.util.Properties;

/**
 * Character file, saved in the gamedata.
 * @author Toby Pinder (Gigitrix)
 */
public class CharacterFileIO extends PropertiesFileIO {

    /**
     * Constructor. Currently featureless.
     */
    public CharacterFileIO() {
    }

    @Override
    public boolean loadProperties() {
        loadLocation = "/crpgdata/save/" + BaseVars.saveFile + "/char.properties";
        Boolean success = super.loadProperties();
        MapMeta.filePath = properties.getProperty("map$path", "catchemrpg/assets/maps/base");
        MapMeta.xPos = Integer.parseInt(properties.getProperty("map$xPos", "100"));
        MapMeta.yPos = Integer.parseInt(properties.getProperty("map$yPos", "100"));
        return success;
    }

    @Override
    public boolean saveProperties() {
        saveLocation = "/crpgdata/save/" + BaseVars.saveFile + "/char.properties";
        properties = new Properties();
        properties.setProperty("map$path", MapMeta.filePath);
        properties.setProperty("map$xPos", "" + MapMeta.xPos);
        properties.setProperty("map$yPos", "" + MapMeta.yPos);
        Boolean success = super.saveProperties("Character Data");
        return success;
    }
}
