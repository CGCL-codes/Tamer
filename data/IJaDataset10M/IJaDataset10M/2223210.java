package games.virtualworldgame;

import games.GameCallback;
import games.defaultgame.DefaultGameLoader;
import games.script.LuaScript;
import games.script.LuaScriptConstructor;
import games.script.ScriptException;
import games.virtualworldgame.objects.ItemType;
import games.virtualworldgame.objects.Location;
import games.virtualworldgame.objects.ScriptedItemType;
import games.virtualworldgame.objects.ScriptedLocation;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.luaj.vm.LuaErrorException;

public class VirtualWorldGameLoader extends DefaultGameLoader {

    private static final Logger logger = Logger.getLogger("system.loading");

    private static final String DEFAULT_TRIGGER_FILTER_PATTERN = ".*";

    private static final boolean DEFAULT_LOAD_DEFAULT_TRIGGERS = true;

    @Override
    public VirtualWorldGame loadGame(File gameDirectory, Map<Object, Object> setup) {
        VirtualWorldGame game = new VirtualWorldGame();
        VirtualWorldGameConfig config = new VirtualWorldGameConfig(new VirtualWorldGameCallback(game));
        config.loadSettingsFrom(gameDirectory, setup);
        config.addDefaultLuaGlobals(gameDirectory);
        config.addDefaultTriggerFiles(gameDirectory);
        config.addDefaultPropertyFiles(gameDirectory);
        config.addDefaultLocationFiles(gameDirectory);
        config.addDefaultItemTypeFiles(gameDirectory);
        this.initGame(game, config);
        return game;
    }

    protected void initGame(VirtualWorldGame game, VirtualWorldGameConfig config) {
        VirtualWorldGameCallback callback = config.getCallback();
        Set<ItemType> types = this.loadItemTypes(config.getItemTypeFiles(), callback);
        game.setItemTypes(types);
        logger.info("Item types:");
        for (ItemType type : types) logger.info(type.getName());
        Set<Location> locations = this.loadLocations(config.getLocationFiles(), callback);
        game.setLocations(locations);
        String starttingLocation = config.getStartingLocation();
        game.setStartingLocation(starttingLocation);
        File innerGameDirectory = config.getInnerGameDirectory();
        if (innerGameDirectory != null && innerGameDirectory.exists()) {
            config.addDefaultLuaGlobals(innerGameDirectory);
            config.addDefaultPropertyFiles(innerGameDirectory);
            Boolean loadInnerTriggers = config.getLoadInnerTriggers();
            if (loadInnerTriggers == null) loadInnerTriggers = DEFAULT_LOAD_DEFAULT_TRIGGERS;
            if (loadInnerTriggers) {
                List<File> innerTriggerFiles = config.getTriggerFiles(innerGameDirectory);
                String triggerFilterPatternProperty = config.getTriggerFilterPattern();
                if (triggerFilterPatternProperty == null || triggerFilterPatternProperty.isEmpty()) triggerFilterPatternProperty = DEFAULT_TRIGGER_FILTER_PATTERN;
                String[] triggerFilterPatterns = triggerFilterPatternProperty.split("; *");
                for (File triggerFile : innerTriggerFiles) for (String triggerFilterPattern : triggerFilterPatterns) if (triggerFile.getName().matches(triggerFilterPattern)) config.addTriggerFile(triggerFile);
            }
            super.initGame(game, config);
        }
    }

    private Set<Location> loadLocations(List<File> locationFiles, GameCallback callback) {
        Set<Location> locations = new HashSet<Location>();
        for (File locationFile : locationFiles) {
            try {
                LuaScript script = LuaScriptConstructor.createLuaScript(locationFile);
                ScriptedLocation location = new ScriptedLocation(callback, script);
                location.init(callback);
                locations.add(location);
            } catch (ScriptException e) {
                logger.log(Level.SEVERE, "Error while loading location " + locationFile.getName(), e);
            }
        }
        logger.info(locations.size() + " locations loaded");
        return locations;
    }

    private Set<ItemType> loadItemTypes(List<File> itemTypeFiles, VirtualWorldGameCallback callback) {
        Set<ItemType> types = new HashSet<ItemType>();
        for (File itemTypeFile : itemTypeFiles) {
            try {
                LuaScript script = LuaScriptConstructor.createLuaScript(itemTypeFile);
                ItemType type = new ScriptedItemType(itemTypeFile.getName().replace(".lua", ""), script);
                types.add(type);
            } catch (LuaErrorException e) {
                logger.log(Level.SEVERE, "Error while loading location " + itemTypeFile.getName(), e);
            }
        }
        logger.info(types.size() + " item types loaded");
        return types;
    }
}
