package fr.fg.server.action.allies;

import java.util.Map;
import fr.fg.server.core.Update;
import fr.fg.server.core.UpdateTools;
import fr.fg.server.data.Ally;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.IllegalOperationException;
import fr.fg.server.data.Player;
import fr.fg.server.servlet.Action;
import fr.fg.server.servlet.Session;

public class SetTerritoryColor extends Action {

    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        int color = (Integer) params.get("color");
        long update = (Long) params.get("update");
        Ally ally = player.getAlly();
        if (ally == null) throw new IllegalOperationException("Vous n'avez pas d'alliance.");
        if (player.getAllyRank() != ally.getLeaderRank()) throw new IllegalOperationException("Seul le leader peut " + "modifier la couleur du territoire.");
        synchronized (ally.getLock()) {
            ally = DataAccess.getEditable(ally);
            ally.setColor(color);
            ally.save();
        }
        return UpdateTools.formatUpdates(player, Update.getAllyUpdate(update), Update.getInformationUpdate("Le changement de couleur sera effectif dans 1 heure."));
    }
}
