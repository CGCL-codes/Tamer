package quickfix.examples.banzai;

import java.util.HashMap;
import java.util.Map;

public class OrderTIF {

    private static Map<String, OrderTIF> known = new HashMap<String, OrderTIF>();

    public static final OrderTIF DAY = new OrderTIF("Day");

    public static final OrderTIF IOC = new OrderTIF("IOC");

    public static final OrderTIF OPG = new OrderTIF("OPG");

    public static final OrderTIF GTC = new OrderTIF("GTC");

    public static final OrderTIF GTX = new OrderTIF("GTX");

    private static OrderTIF[] array = { DAY, IOC, OPG, GTC, GTX };

    private String name;

    private OrderTIF(String name) {
        this.name = name;
        synchronized (OrderTIF.class) {
            known.put(name, this);
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static Object[] toArray() {
        return array;
    }

    public static OrderTIF parse(String type) throws IllegalArgumentException {
        OrderTIF result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException("OrderTIF:  " + type + " is unknown.");
        }
        return result;
    }
}
