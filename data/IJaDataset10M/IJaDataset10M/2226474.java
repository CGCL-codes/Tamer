package fr.fg.server.action.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import fr.fg.server.core.FleetTools;
import fr.fg.server.core.Update;
import fr.fg.server.core.UpdateTools;
import fr.fg.server.data.Area;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Fleet;
import fr.fg.server.data.GameConstants;
import fr.fg.server.data.IllegalOperationException;
import fr.fg.server.data.ItemContainer;
import fr.fg.server.data.Player;
import fr.fg.server.data.Skill;
import fr.fg.server.data.Structure;
import fr.fg.server.i18n.Messages;
import fr.fg.server.servlet.Action;
import fr.fg.server.servlet.Session;
import fr.fg.server.util.Utilities;

public class BuildStructure extends Action {

    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        int type = (Integer) params.get("type");
        Fleet fleet = FleetTools.getFleetByIdWithChecks((Integer) params.get("fleet"), player.getId());
        if (fleet.getSkillLevel(Skill.SKILL_ENGINEER) == -1) throw new IllegalOperationException("La flotte n'a pas " + "la compétence ingénieur.");
        if (fleet.getMovement() == 0) throw new IllegalOperationException("La flotte n'a pas " + "suffisament de mouvement pour pouvoir construire.");
        int[] cost = Structure.getBaseCost(type);
        ItemContainer itemContainer = fleet.getItemContainer();
        for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) if (itemContainer.getResource(i) < cost[i]) throw new IllegalOperationException("La flotte n'a pas " + "suffisament de ressources pour pouvoir construire la " + "structure.");
        player = Player.updateCredits(player);
        if (player.getCredits() < cost[4]) throw new IllegalOperationException("Vous n'avez pas " + "suffisament de crédits pour pouvoir construire la " + "structure.");
        Area area = fleet.getArea();
        if (type == Structure.TYPE_GENERATOR) {
            List<Structure> playerStructures = player.getStructures();
            int generatorsCount = 0;
            synchronized (playerStructures) {
                for (Structure playerStructure : playerStructures) {
                    if (playerStructure.getType() == Structure.TYPE_GENERATOR) generatorsCount++;
                }
            }
            if (generatorsCount >= 1 + player.getLevel() / 4) throw new IllegalOperationException("La limite de " + "générateurs a été atteinte. Détruisez d'autres " + "générateurs pour pouvoir en construire un nouveau.");
        }
        int fleetX = fleet.getCurrentX();
        int fleetY = fleet.getCurrentY();
        area.checkValidStructureLocation(type, fleetX, fleetY);
        long idEnergySupplierStructure;
        int requiredEnergy = Structure.getEnergyConsumption(type);
        if (requiredEnergy > 0) {
            Structure energySupplierStructure = area.getEnergySupplierStructure(player, requiredEnergy, fleetX, fleetY);
            if (energySupplierStructure == null) throw new IllegalOperationException("Aucune source " + "d'énergie ne peut alimenter la structure.");
            idEnergySupplierStructure = energySupplierStructure.getId();
        } else {
            idEnergySupplierStructure = 0;
        }
        synchronized (fleet.getLock()) {
            fleet = DataAccess.getEditable(fleet);
            fleet.doAction(Fleet.ACTION_BUILD_STRUCTURE, Utilities.now() + Skill.SKILL_ENGINEER_MOVEMENT_RELOAD);
            fleet.addXp(Structure.getXp(type));
            fleet.save();
        }
        synchronized (itemContainer.getLock()) {
            itemContainer = DataAccess.getEditable(itemContainer);
            for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) itemContainer.addResource(-cost[i], i);
            itemContainer.save();
        }
        synchronized (player.getLock()) {
            player = DataAccess.getEditable(player);
            player.addCredits(-cost[4]);
            player.save();
        }
        String name = Messages.getString("structure" + type);
        if (name.length() > 20) name = name.substring(0, 20);
        Structure structure = new Structure(type, name, Structure.getDefaultHull(type), fleetX, fleetY, idEnergySupplierStructure, player.getId(), fleet.getIdCurrentArea());
        structure.save();
        if (idEnergySupplierStructure == 0) {
            synchronized (structure.getLock()) {
                structure = DataAccess.getEditable(structure);
                structure.setIdEnergySupplierStructure(structure.getId());
                structure.save();
            }
        }
        UpdateTools.queueAreaUpdate(fleet.getIdCurrentArea(), player.getId());
        List<Update> updates = new ArrayList<Update>();
        updates.add(Update.getAreaUpdate());
        updates.add(Update.getPlayerFleetUpdate(fleet.getId()));
        if (structure.getType() == Structure.TYPE_GENERATOR) updates.add(Update.getPlayerGeneratorsUpdate());
        return UpdateTools.formatUpdates(player, updates);
    }
}
