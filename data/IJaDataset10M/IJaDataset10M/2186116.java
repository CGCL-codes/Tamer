package fr.fg.server.action.trade;

import java.util.List;
import java.util.Map;
import fr.fg.server.core.FleetTools;
import fr.fg.server.core.Update;
import fr.fg.server.core.UpdateTools;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Fleet;
import fr.fg.server.data.IllegalOperationException;
import fr.fg.server.data.Item;
import fr.fg.server.data.ItemContainer;
import fr.fg.server.data.Player;
import fr.fg.server.data.StellarObject;
import fr.fg.server.data.Tradecenter;
import fr.fg.server.events.GameEventsDispatcher;
import fr.fg.server.events.impl.AfterTradeEvent;
import fr.fg.server.servlet.Action;
import fr.fg.server.servlet.Session;

public class Change extends Action {

    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        int idFleet = (Integer) params.get("fleet");
        int resource = (Integer) params.get("resource");
        long count = (Long) params.get("count");
        boolean sell = count < 0;
        if (sell) count = -count;
        Fleet fleet = FleetTools.getFleetByIdWithChecks(idFleet, player.getId());
        List<StellarObject> objects = fleet.getArea().getObjects();
        int idTradecenter = -1;
        synchronized (objects) {
            for (StellarObject object : objects) if (object.getType().equals(StellarObject.TYPE_TRADECENTER) && object.getBounds().contains(fleet.getCurrentX(), fleet.getCurrentY())) {
                idTradecenter = object.getId();
                break;
            }
        }
        if (idTradecenter == -1) throw new IllegalOperationException("La flotte n'est pas " + "placée sur un centre de commerce.");
        Tradecenter tradecenter = DataAccess.getTradecenterById(idTradecenter);
        ItemContainer itemContainer = fleet.getItemContainer();
        if (sell && itemContainer.getResource(resource) < count) throw new IllegalOperationException("Vous n'avez pas suffisament de ressources à transférer.");
        double rateBefore = tradecenter.getRate(resource);
        double rateAfter = tradecenter.getRate(resource) + (sell ? -1 : 1) * count * tradecenter.getVariation();
        double rate = (rateAfter + rateBefore) / 2;
        double min = .005;
        double max = 9.52;
        if (rateAfter < min) {
            double coef = (rateBefore - min) / (rateBefore - rateAfter);
            rate = coef * (rateBefore - min) / 2 + (1 - coef) * min;
            rateAfter = min;
        } else if (rateAfter > max) {
            double coef = (max - rateBefore) / (rateAfter - rateBefore);
            rate = coef * (max - rateBefore) / 2 + (1 - coef) * max;
            rateAfter = max;
        } else {
            rate = (rateAfter + rateBefore) / 2;
        }
        rate *= (1 + (sell ? -tradecenter.getPlayerFees(player.getId()) : tradecenter.getPlayerFees(player.getId())));
        player = Player.updateCredits(player);
        long credits = (long) (sell ? Math.floor(rate * count) : Math.ceil(rate * count));
        if (!sell && player.getCredits() < credits) throw new IllegalOperationException("Vous n'avez pas suffisament de crédits.");
        if (!sell && itemContainer.getCompatibleOrFreePosition(Item.TYPE_RESOURCE, resource) == -1) throw new IllegalOperationException("La flotte n'a pas " + "d'emplacement de libre pour recevoir les ressources.");
        synchronized (player.getLock()) {
            player = DataAccess.getEditable(player);
            player.addCredits(sell ? credits : -credits);
            player.save();
        }
        if (sell) {
            for (int i = 0; i < itemContainer.getMaxItems(); i++) System.out.println(itemContainer.getItem(i).getType() + " " + itemContainer.getItem(i).getCount());
            synchronized (itemContainer.getLock()) {
                itemContainer = DataAccess.getEditable(itemContainer);
                itemContainer.addResource(-count, resource);
                itemContainer.save();
            }
            for (int i = 0; i < itemContainer.getMaxItems(); i++) System.out.println(itemContainer.getItem(i).getType() + " " + itemContainer.getItem(i).getCount());
        } else {
            double totalWeight = itemContainer.getTotalWeight();
            long newResources = count;
            if (fleet.getPayload() < totalWeight + count) newResources = (long) Math.floor(fleet.getPayload() - totalWeight);
            synchronized (itemContainer.getLock()) {
                itemContainer = DataAccess.getEditable(itemContainer);
                itemContainer.addResource(newResources, resource);
                itemContainer.save();
            }
        }
        synchronized (tradecenter.getLock()) {
            tradecenter = DataAccess.getEditable(tradecenter);
            tradecenter.setRate(rateAfter, resource);
            tradecenter.save();
        }
        GameEventsDispatcher.fireGameEvent(new AfterTradeEvent(fleet, sell, resource, count));
        return UpdateTools.formatUpdates(player, Update.getPlayerFleetUpdate(fleet.getId()), Update.getPlayerSystemsUpdate());
    }
}
