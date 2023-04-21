package fr.fg.server.action.structure;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import fr.fg.server.core.StructureTools;
import fr.fg.server.core.Update;
import fr.fg.server.core.UpdateTools;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.GameConstants;
import fr.fg.server.data.IllegalOperationException;
import fr.fg.server.data.Player;
import fr.fg.server.data.StorehouseResources;
import fr.fg.server.data.Structure;
import fr.fg.server.data.StructureModule;
import fr.fg.server.servlet.Action;
import fr.fg.server.servlet.Session;

public class UpgradeModule extends Action {

    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        long idStructure = (Long) params.get("structure");
        int type = (Integer) params.get("type");
        Structure structure = StructureTools.getStructureByIdWithChecks(idStructure, player.getId());
        if (!structure.isValidModule(type)) throw new IllegalOperationException("Module incompatible avec la structure.");
        if (!structure.isActivated()) throw new IllegalOperationException("La structure est désactivée.");
        if (structure.getModuleLevel(type) >= StructureModule.getMaxLevel(type)) throw new IllegalOperationException("Le module a été amélioré au niveau maximum.");
        int[] structureCost = Structure.getBaseCost(structure.getType());
        int baseCost = 0;
        for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) baseCost += structureCost[i];
        long[] cost = structure.getModuleCost(type, structure.getModuleLevel(type) + 1);
        StorehouseResources storehouseResources = structure.getArea().getStorehouseResourcesByPlayer(player.getId());
        long[] playerResources = new long[GameConstants.RESOURCES_COUNT];
        if (storehouseResources != null) {
            for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) playerResources[i] = storehouseResources.getResource(i);
        }
        for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) if (playerResources[i] < cost[i]) throw new IllegalOperationException("Vous n'avez pas " + "suffisament de ressources pour pouvoir améliorer le " + "module.");
        player = Player.updateCredits(player);
        if (player.getCredits() < cost[4]) throw new IllegalOperationException("Vous n'avez pas " + "suffisament de crédits pour pouvoir améliorer le " + "module.");
        int maxHullBefore = structure.getMaxHull();
        if (storehouseResources != null) {
            synchronized (storehouseResources.getLock()) {
                storehouseResources = DataAccess.getEditable(storehouseResources);
                for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) storehouseResources.addResource(-cost[i], i);
                storehouseResources.save();
            }
        }
        synchronized (player.getLock()) {
            player = DataAccess.getEditable(player);
            player.addCredits(-cost[4]);
            player.save();
        }
        List<StructureModule> modules = new ArrayList<StructureModule>(structure.getModules());
        boolean found = false;
        for (StructureModule module : modules) {
            if (module.getType() == type) {
                synchronized (module.getLock()) {
                    module = DataAccess.getEditable(module);
                    module.setLevel(module.getLevel() + 1);
                    module.save();
                }
                found = true;
                break;
            }
        }
        if (!found) {
            StructureModule module = new StructureModule(structure.getId(), type, 1);
            module.save();
        }
        synchronized (structure) {
            structure = DataAccess.getEditable(structure);
            structure.updateModulesCache();
            structure.setHull(structure.getHull() + structure.getMaxHull() - maxHullBefore);
            structure.save();
        }
        UpdateTools.queueAreaUpdate(structure.getIdArea(), player.getId(), false, new Point(structure.getX(), structure.getY()));
        List<Update> updates = new ArrayList<Update>();
        updates.add(Update.getAreaUpdate());
        updates.add(Update.getPlayerSystemsUpdate());
        if (structure.getType() == Structure.TYPE_GENERATOR) updates.add(Update.getPlayerGeneratorsUpdate());
        return UpdateTools.formatUpdates(player, updates);
    }
}
