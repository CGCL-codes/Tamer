package net.sf.carmaker.orders.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.carmaker.orders.IOrder;
import net.sf.carmaker.orders.IOrderManager;

/**
 * @author uwe
 *
 */
public class FileOrderManager implements IOrderManager {

    Iterator<IOrder> orderIterator;

    List<IOrder> pastOrders;

    /**
	 * Construct an OrdeManager which reads its orders from a file
	 * @param inputOrderFile file name of a comma separated order file 
	 * @throws IOException 
	 */
    public FileOrderManager(String inputOrderFile) throws IOException {
        List<IOrder> orderList;
        String line;
        orderList = new ArrayList<IOrder>();
        InputStream orderStream = IOrderManager.class.getResourceAsStream(inputOrderFile);
        if (orderStream == null) {
            throw new IOException(inputOrderFile + " does not exist.");
        }
        InputStreamReader inputStreamReader = new InputStreamReader(orderStream);
        BufferedReader input = new BufferedReader(inputStreamReader);
        IOrder order = null;
        while ((line = input.readLine()) != null) {
            order = Order.createOrder(line);
            if (order == null) {
                break;
            }
            orderList.add(order);
        }
        orderIterator = orderList.iterator();
        pastOrders = new ArrayList<IOrder>();
    }

    public IOrder next() {
        IOrder currentOrder = orderIterator.next();
        pastOrders.add(currentOrder);
        return currentOrder;
    }

    public List<IOrder> getPastOrders() {
        return pastOrders;
    }

    public boolean hasNext() {
        return orderIterator.hasNext();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
