package freelands;

import freelands.actor.npc.NpcBuilder;
import freelands.harvest.HarvestItem;
import freelands.item.ItemRecipe;
import freelands.maps.MapManager;
import freelands.spell.Spell;
import java.util.logging.Level;

public class Main {

    public static Preferences preferences;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args != null && args.length != 0) {
                preferences = new Preferences(args[0]);
            } else {
                preferences = new Preferences();
            }
            preferences.LOGGER.info("Freelands " + Preferences.VERSION + " start");
            HarvestItem.initHarvestablesItems(Main.preferences.HARVESTCONFIGFILE);
            ItemRecipe.createAllRecipes(Main.preferences.RECIPECONFIGFILES);
            Spell.createAllSpells(Main.preferences.SPELLCONFIGFILE);
            FreelandsThread.prepareThreads();
            preferences.mapManager = new MapManager(FreelandsThread.threads);
            NpcBuilder.buildNpcs();
            FreelandsThread.launchThreads();
            NonBlockingServer.startServer();
        } catch (Throwable e) {
            preferences.LOGGER.log(Level.SEVERE, "Unexpected error, please report bug :\n{0}", e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
