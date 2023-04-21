package com.epicsagaonline.bukkit.ChallengeMaps;

import java.util.HashMap;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.epicsagaonline.bukkit.ChallengeMaps.DAL.Current;
import com.epicsagaonline.bukkit.ChallengeMaps.DAL.GameStateData;
import com.epicsagaonline.bukkit.ChallengeMaps.DAL.MapData;
import com.epicsagaonline.bukkit.ChallengeMaps.DAL.MapEntranceData;
import com.epicsagaonline.bukkit.ChallengeMaps.commands.CM;
import com.epicsagaonline.bukkit.ChallengeMaps.commands.CommandHandler;
import com.epicsagaonline.bukkit.ChallengeMaps.listeners.BlockEvents;
import com.epicsagaonline.bukkit.ChallengeMaps.listeners.EntityEvents;
import com.epicsagaonline.bukkit.ChallengeMaps.listeners.InventoryEvents;
import com.epicsagaonline.bukkit.ChallengeMaps.listeners.PlayerEvents;
import com.epicsagaonline.bukkit.ChallengeMaps.objects.Map;

public class ChallengeMaps extends JavaPlugin {

    private final BlockEvents blockListener = new BlockEvents(this);

    private final PlayerEvents playerListener = new PlayerEvents(this);

    private final EntityEvents entityListener = new EntityEvents(this);

    private InventoryEvents inventoryListener;

    private ProcessObjectives processObjectives = new ProcessObjectives();

    private MonitorEnvironment monitorEnvironment = new MonitorEnvironment();

    private HashMap<String, CommandHandler> handlers = new HashMap<String, CommandHandler>();

    private static final String[] CM_COMMANDS = { "cm", "challengemaps", "leave" };

    private static CommandHandler cmCommandHandler = new CM();

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        try {
            Current.Plugin = this;
            PluginManager pm = getServer().getPluginManager();
            pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.SIGN_CHANGE, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BURN, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_FADE, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_FORM, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_SPREAD, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.LEAVES_DECAY, this.blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);
            pm.registerEvent(Event.Type.PLAYER_LOGIN, this.playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_PRELOGIN, this.playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Highest, this);
            pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListener, Event.Priority.Normal, this);
            registerCommands();
            MapData.LoadMaps();
            MapEntranceData.LoadMapEntrances();
            for (World world : this.getServer().getWorlds()) {
                for (Player player : world.getPlayers()) {
                    String worldName = world.getName().replace("_" + player.getName(), "");
                    Map map = Current.Maps.get(worldName.toLowerCase());
                    if (map != null) {
                        Current.GameWorlds.add(world.getName());
                        GameStateData.Load(map, player, false);
                    }
                }
            }
            setupSpout(pm);
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, processObjectives, 100, 50);
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, monitorEnvironment, 100, 50);
            System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
        } catch (Throwable e) {
            System.out.println("[" + pdfFile.getName() + "]" + " error starting: " + e.getMessage() + " Disabling plugin");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        processObjectives = null;
        monitorEnvironment = null;
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled.");
    }

    private void registerCommands() {
        for (String cmd : CM_COMMANDS) {
            registerCommand(cmd, cmCommandHandler);
        }
    }

    public void registerCommand(String command, CommandHandler handler) {
        handlers.put(command.toLowerCase(), handler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        CommandHandler handler = handlers.get(commandLabel.toLowerCase());
        if (handler == null) {
            return true;
        }
        return handler.onCommand(commandLabel, sender, args);
    }

    private void EnablePlugin(String pluginName, String pluginType) {
        Plugin plg;
        plg = this.getServer().getPluginManager().getPlugin(pluginName);
        if (plg != null) {
            if (!plg.isEnabled()) {
                try {
                    Log.Write("Detected " + pluginType + " Plugin > " + pluginName + " > Enabling...");
                    this.getServer().getPluginManager().enablePlugin(plg);
                } catch (Exception e) {
                    Log.Write(e.getMessage());
                }
            }
        }
    }

    private void setupSpout(PluginManager pm) {
        Plugin test = this.getServer().getPluginManager().getPlugin("Spout");
        if (test != null) {
            EnablePlugin("Spout", "Spout");
            this.inventoryListener = new InventoryEvents(this);
            pm.registerEvent(Event.Type.CUSTOM_EVENT, this.inventoryListener, Event.Priority.Normal, this);
            Log.Write("Spout Integration Enabled.");
        } else {
            Log.Write("Spout plugin not detected, unable to enable Spout integration.");
        }
    }
}
