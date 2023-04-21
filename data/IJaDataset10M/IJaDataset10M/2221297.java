package com.shimari.fxtp;

import java.util.*;
import java.io.Serializable;

/**
 * An Trader wraps an User and adds to it the capability of 
 * submitting trading orders to the FXTP server. The primary difference 
 * is that an Trader has a password.
 */
public class Trader implements Serializable {

    private UserId userId;

    private final String password;

    private static final String[] NULL_STRING_ARRAY = new String[0];

    private transient FXTP fxtp;

    /** Construct a new Trader */
    public Trader(FXTP fxtpService, Integer uid, String fxPassword) throws FXTP_Exception {
        this(fxtpService, fxtpService.getUserId(uid), fxPassword);
    }

    /** Construct a new FXTP Trader */
    public Trader(FXTP fxtpService, UserId fxuser, String fxPassword) {
        this.userId = fxuser;
        this.password = fxPassword;
        this.fxtp = fxtpService;
    }

    /** set the FXTP object */
    public void setFXTP(FXTP fxtpService) {
        this.fxtp = fxtpService;
    }

    /** Semantic */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        try {
            Trader t = (Trader) o;
            return (userId.equals(t.userId));
        } catch (ClassCastException ce) {
            return false;
        }
    }

    /** Baed on userId */
    public int hashCode() {
        return userId.hashCode();
    }

    /**
    * Ordinarily the User we have is cached for up to 2 minutes. 
    * Calling this method will flush it from the cache.
    */
    public void refresh() {
        fxtp.refresh(userId);
    }

    /** Get the User this trader works with */
    public User getUser() throws FXTP_Exception {
        return fxtp.getUser(userId);
    }

    /** Get the UserId of this trader's user */
    public UserId getUserId() {
        return userId;
    }

    /** Get the integer ID of this trader's user */
    public Integer getId() {
        return userId.getId();
    }

    /** Debug string */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Trader(");
        buf.append(userId);
        buf.append(")");
        return buf.toString();
    }

    /** Cancel an order */
    public String cancel(Order order) throws FXTP_Exception {
        if (!order.getId().equals(getId())) {
            throw new FXTP_Exception(this + " cannot execute trades for  user " + order.getId());
        }
        return fxtp.cancelOrder(order, password);
    }

    /** Submit a new order */
    public String submit(Order order) throws FXTP_Exception {
        if (!order.getId().equals(getId())) {
            throw new FXTP_Exception(this + " cannot execute trades for  user " + order.getId());
        }
        return fxtp.submitOrder(order, password);
    }

    /**
    * Replace all our orders in the supplied book with the single new order
    * that is provided. If the new order is null they're all just cancelled. If 
    * the new order was already in the book it's left there, but all the 
    * other orders are cancelled.    
    */
    public String[] replaceAll(Book book, Order newOrder) throws FXTP_Exception {
        List results = new LinkedList();
        results.add(replace(book.getBook(), newOrder));
        return (String[]) results.toArray(NULL_STRING_ARRAY);
    }

    /**
    * Writing results to result list, replace all of the orders in the 
    * collection with the supplied order.
    */
    public String replace(Collection orders, Order newOrder) throws FXTP_Exception {
        Iterator i = orders.iterator();
        String result = "Nothing to do";
        while (i.hasNext()) {
            Order oldOrder = (Order) i.next();
            if (!getId().equals(oldOrder.getId())) {
                continue;
            }
            if (newOrder != null && newOrder.equals(oldOrder)) {
                result = "Maintaining order " + oldOrder.toShortString();
                newOrder = null;
            } else {
                cancel(oldOrder);
                result = "Cancelling order " + oldOrder.toShortString();
            }
        }
        if (newOrder != null) {
            submit(newOrder);
            result += " and submitting " + newOrder.toShortString();
        }
        return result;
    }

    /** cancel all order for symbol and submit an order to sell the 
     * remaining contracts that this trader holds
     */
    public String close(String symbol, double minCost) throws FXTP_Exception {
        fxtp.refresh(symbol);
        return close(fxtp.getClaim(symbol), minCost);
    }

    /** 
     * Determine our current position and issue whatever orders are 
     * necessary to close it.    
     */
    public String close(Claim claim, double minCost) throws FXTP_Exception {
        User user = getUser();
        Position pos = user.getPosition(claim.getSymbol());
        if (pos != null && pos.getQuantity() != 0) {
            Order closer = new Order(user.getId(), claim.getSymbol(), pos.getAbsoluteQuantity(), (pos.isLong() ? (int) Math.floor(Math.min(99, claim.getAverageAsk(user.getUserId(), minCost))) : (int) Math.ceil(Math.max(1, claim.getAverageBid(user.getUserId(), minCost)))), !pos.isLong());
            return replace(claim.getBook(), closer);
        } else {
            return "Position in " + claim.getSymbol() + " already closed";
        }
    }

    /**
      * Execute the market order
      */
    public String execute(MarketOrder mo) throws FXTP_Exception {
        Order bid = mo.getBid();
        Order ask = mo.getAsk();
        String r1 = null;
        String r2 = null;
        if (bid != null) {
            r1 = replace(mo.getClaim().getBids(), bid);
        }
        if (ask != null) {
            r2 = replace(mo.getClaim().getAsks(), ask);
        }
        refresh();
        if (r1 == null && r2 == null) return "Nothing to do"; else if (r1 == null) return r2; else if (r2 == null) return r1; else return r1 + "; " + r2;
    }
}
