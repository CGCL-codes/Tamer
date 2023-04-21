package org.jazzteam.example.jms.servlet;

import java.util.HashMap;
import java.util.Map;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

public class StockMessageCreator implements MessageCreator {

    private static final Logger logger = LoggerFactory.getLogger(StockMessageCreator.class);

    private int MAX_DELTA_PERCENT = 1;

    private Map<Destination, Double> LAST_PRICES = new HashMap<Destination, Double>();

    private Destination stock;

    public StockMessageCreator(Destination stock) {
        this.stock = stock;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        Double value = LAST_PRICES.get(stock);
        if (value == null) {
            value = new Double(Math.random() * 100);
        }
        double oldPrice = value.doubleValue();
        value = new Double(mutatePrice(oldPrice));
        LAST_PRICES.put(stock, value);
        double price = value.doubleValue();
        double offer = price * 1.001;
        boolean up = (price > oldPrice);
        MapMessage message = session.createMapMessage();
        message.setString("stock", stock.toString());
        message.setDouble("price", price);
        message.setDouble("offer", offer);
        message.setBoolean("up", up);
        logger.info("Sending: " + ((ActiveMQMapMessage) message).getContentMap() + " on destination: " + stock);
        return message;
    }

    protected double mutatePrice(double price) {
        double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT) - MAX_DELTA_PERCENT;
        return price * (100 + percentChange) / 100;
    }
}
