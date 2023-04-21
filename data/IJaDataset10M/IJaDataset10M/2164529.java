package tafat.metamodel.behavior;

import java.util.ArrayList;
import tafat.engine.Behavior;
import tafat.engine.Console;
import tafat.engine.ModelObject;
import tafat.engine.Time;
import tafat.engine.Tools;
import tafat.engine.social.ActionMaker;
import tafat.engine.social.DecisionMaker;
import tafat.engine.social.OrderAndData;
import tafat.engine.timeout.TimeoutCyclic;
import tafat.metamodel.entity.ControlUnitFull;
import tafat.metamodel.entity.SmartMeterFull;

public class ControlUnitFullBehaviorPartialCentralized implements Behavior {

    private final double[] changingFactors = { 500, 1000, 2000, 5000, 10000, 20000 };

    private enum Actions {

        DECREASE, INCREASE, NOTHING
    }

    ;

    private final double TOLERANCE = 10000;

    private static ArrayList<ControlUnitFullBehaviorPartialCentralized> instanceList = new ArrayList<ControlUnitFullBehaviorPartialCentralized>();

    private ControlUnitFull controlUnit;

    private ActionMaker actionMaker;

    private DecisionMaker decisionMaker;

    public static Behavior newInstance() {
        ControlUnitFullBehaviorPartialCentralized behavior = new ControlUnitFullBehaviorPartialCentralized();
        instanceList.add(behavior);
        return behavior;
    }

    public void init(ModelObject target) {
        if (target instanceof ControlUnitFull) {
            controlUnit = (ControlUnitFull) target;
            createActionMaker();
            createDecisionMaker();
            actionMaker.createContext(controlUnit);
            new TimeoutCyclic(Time.getInstance().hour) {

                @Override
                public void action() {
                    int sign = Tools.randomInRangeInt(0, 1);
                    int change = Tools.randomInRangeInt(0, changingFactors.length - 1);
                    if (sign == 0) controlUnit.aimConsumption -= changingFactors[change]; else controlUnit.aimConsumption += changingFactors[change];
                }
            };
        } else Console.error(target.getFullPath() + " is not a ControlUnit");
    }

    private void createDecisionMaker() {
        decisionMaker = new DecisionMaker() {

            @Override
            public ArrayList<OrderAndData> checkActions(OrderAndData orderAndData) {
                ArrayList<OrderAndData> orders = new ArrayList<OrderAndData>();
                if ((controlUnit.aimConsumption - controlUnit.realConsumption) < 0) orders.add(new OrderAndData(Actions.DECREASE.ordinal(), null)); else if ((controlUnit.aimConsumption - controlUnit.realConsumption) > TOLERANCE) orders.add(new OrderAndData(Actions.INCREASE.ordinal(), null)); else orders.add(new OrderAndData(Actions.NOTHING.ordinal(), null));
                return orders;
            }

            @Override
            public void receiveMessage(Object data) {
            }
        };
    }

    private void createActionMaker() {
        actionMaker = new ActionMaker() {

            ControlUnitFull controlUnit;

            @Override
            public OrderAndData execute(OrderAndData orderAndData) {
                if (orderAndData.order == Actions.DECREASE.ordinal()) {
                    for (SmartMeterFull smartMeter : controlUnit.smartMeterList) smartMeter.mode = SmartMeterFull.Mode.DECREASE;
                    return new OrderAndData(orderAndData.order, null);
                }
                if (orderAndData.order == Actions.INCREASE.ordinal()) {
                    for (SmartMeterFull smartMeter : controlUnit.smartMeterList) smartMeter.mode = SmartMeterFull.Mode.INCREASE;
                    return new OrderAndData(orderAndData.order, null);
                }
                if (orderAndData.order == Actions.NOTHING.ordinal()) {
                    for (SmartMeterFull smartMeter : controlUnit.smartMeterList) smartMeter.mode = SmartMeterFull.Mode.OK;
                    return new OrderAndData(orderAndData.order, null);
                }
                return null;
            }

            @Override
            public void createContext(ModelObject object) {
                if (object instanceof ControlUnitFull) controlUnit = (ControlUnitFull) object; else Console.error(object.getFullPath() + " is not a ControlUnit");
            }

            @Override
            public void receiveMessage(Object data) {
            }
        };
    }

    @Override
    public void tickOn(Integer time) {
        controlUnit.realConsumption = getRealConsumption();
        ArrayList<OrderAndData> orders = decisionMaker.checkActions(null);
        if (orders != null) {
            for (OrderAndData order : orders) {
                OrderAndData orderAndDataOut = actionMaker.execute(order);
                decisionMaker.receiveMessage(orderAndDataOut);
            }
        }
    }

    private double getRealConsumption() {
        double realConsumption = 0;
        for (SmartMeterFull smartMeter : controlUnit.smartMeterList) realConsumption += smartMeter.powerBus.activePower;
        return realConsumption;
    }

    @Override
    public void tickOff(Integer time) {
    }

    public void terminate() {
    }

    public void loadAttribute(String name, String value) {
    }
}
