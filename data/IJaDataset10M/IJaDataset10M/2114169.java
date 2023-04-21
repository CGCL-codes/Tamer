package quickfix.examples.ordermatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OrderMatcher {

    private HashMap<String, Market> markets = new HashMap<String, Market>();

    private Market getMarket(String symbol) {
        Market m = markets.get(symbol);
        if (m == null) {
            m = new Market();
            markets.put(symbol, m);
        }
        return m;
    }

    public boolean insert(Order order) {
        return getMarket(order.getSymbol()).insert(order);
    }

    public void match(String symbol, ArrayList<Order> orders) {
        getMarket(symbol).match(symbol, orders);
    }

    public Order find(String symbol, char side, String id) {
        return getMarket(symbol).find(symbol, side, id);
    }

    public void erase(Order order) {
        getMarket(order.getSymbol()).erase(order);
    }

    public void display() {
        for (Iterator<String> iter = markets.keySet().iterator(); iter.hasNext(); ) {
            String symbol = iter.next();
            System.out.println("MARKET: " + symbol);
            display(symbol);
        }
    }

    public void display(String symbol) {
        getMarket(symbol).display();
    }
}
