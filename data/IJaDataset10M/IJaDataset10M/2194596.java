package carrier;

public class Messenger {

    public void sendOrder(Deliverable order, Movable reference) {
        if (order instanceof TowerOrderType) {
            Order<TowerOrderType> msg = new Order<TowerOrderType>((TowerOrderType) order, reference);
            ControlTower.getInstance().addOrder(msg);
        } else if (order instanceof CrewOrderType) {
            Order<CrewOrderType> msg = new Order<CrewOrderType>((CrewOrderType) order, reference);
            CrewManager.getInstance().addOrder(msg);
        } else if (order instanceof FighterOrderType) {
            Order<FighterOrderType> msg = new Order<FighterOrderType>((FighterOrderType) order, reference);
            FighterManager.getInstance().addOrder(msg);
        }
    }
}
