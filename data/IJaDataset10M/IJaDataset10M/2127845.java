package mw.server.model.cost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import mw.server.model.Card;

public class CostList implements Cost, Serializable, Iterable<Cost> {

    private static final long serialVersionUID = 1097911554205893784L;

    private ArrayList<Cost> costs = new ArrayList<Cost>();

    private int index = 0;

    public void addCost(Cost cost) {
        costs.add(cost);
    }

    public void updateCost(Cost cost) {
        Cost costToRemove = null;
        for (Cost c : costs) {
            if (c.getClass().isInstance(cost)) {
                costToRemove = c;
                break;
            }
        }
        if (costToRemove != null) {
            costs.remove(costToRemove);
        }
        costs.add(cost);
    }

    public boolean isPaid() {
        for (Cost cost : costs) {
            if (!cost.isPaid()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNextCost() {
        return index < costs.size() - 1;
    }

    public Cost getNextCost() {
        if (hasNextCost()) {
            index++;
            return costs.get(index);
        } else {
            return null;
        }
    }

    public Cost getCurrentCost() {
        return costs.get(index);
    }

    public void reset() {
        index = 0;
    }

    public boolean canPayForCard(Card card) {
        for (Cost cost : costs) {
            if (!cost.canPayForCard(card)) {
                return false;
            }
        }
        return true;
    }

    public void pay(Card card) {
    }

    @Override
    public Iterator<Cost> iterator() {
        return costs.iterator();
    }
}
