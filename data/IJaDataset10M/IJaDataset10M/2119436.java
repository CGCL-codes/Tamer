package algoritms.fifo;

import java.util.LinkedList;

public class Exchange {

    private LinkedList<StockImpl> stockQueue = new LinkedList<StockImpl>();

    public void buyStocks(int count, Double price) {
        for (int i = 0; i < count; i++) {
            StockImpl s = new StockImpl(price);
            stockQueue.add(s);
        }
    }

    public void saleStocks(int count, Double price, VisitorExchange v) throws Exception {
        for (int i = 0; i < count; i++) {
            if (null == stockQueue || stockQueue.size() < count) {
                throw new Exception("error");
            }
            StockImpl s = stockQueue.removeFirst();
            s.setSalePrice(price);
            s.accept(v);
        }
    }

    public static void main(String[] argv) {
        Exchange e = new Exchange();
        VisitorExchangeImpl v = new VisitorExchangeImpl();
        e.buyStocks(100, new Double(20));
        e.buyStocks(20, new Double(24));
        e.buyStocks(200, new Double(36));
        try {
            e.saleStocks(150, new Double(30), v);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(v.getResult());
        System.out.println(e.stockQueue.size());
    }
}
