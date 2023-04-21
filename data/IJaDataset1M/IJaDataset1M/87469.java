package biz.axonsoftware;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 *
 * @author rimas
 */
public class Order implements Serializable, Cloneable {

    public static final int SELL = 1;

    public static final int BUY = 2;

    public static final int ALL = 0;

    public static final int MARKET = 1;

    public static final int STOP = 2;

    public static final int LIMIT = 3;

    public static final int NEW = 0;

    public static final int ORDER_PENDING = 1;

    public static final int ORDER_OPEN_CONFIRMED = 2;

    public static final int ORDER_CLOSE_CONFIRMED = 3;

    public static final int ORDER_CLOSE_PENDING = 4;

    public static final int ORDER_CONFIRMED = 5;

    public static final int ORDER_REJECTED = 6;

    public static final int ORDER_ORPHAN = 7;

    public static final int ORDER_CANCELED = 8;

    public static final int ORDER_REPLACED = 9;

    public static final int POSITION_CLOSE_CONFIRMED = 10;

    public static final int ORDER_CANCELED_INTERNALLY = 11;

    public static final int ORDER_RESTORED_FROM_GREY_POOL = 12;

    public static final int ORDER_CLOSE_REJECTED = 13;

    public static final int ORDER_REPLACE = 14;

    public static final int POSITION_CLOSING_INTERNALLY = 15;

    public static final int ORDER_CANCEL = 16;

    private int owner;

    private String externalOwner;

    private int side;

    private int type;

    private int status;

    private Date openDate;

    private Date closeDate;

    private long id;

    private long externalId = 0;

    private long previousId = 0;

    private long listId = 0;

    private long stopOrderId = 0;

    private long limitOrderId = 0;

    private long positionId = 0;

    private long closeOrderId = 0;

    private String symbol;

    private double size;

    private double openPrice;

    private double closePrice;

    private double pl;

    private double commissions;

    private double margin;

    private double marginFactor = 0.01;

    private double triggerPrice;

    private double stopPrice;

    private boolean lastPosition = false;

    private boolean forceOpen = false;

    private boolean forceClose = false;

    private boolean synthetic = false;

    private boolean movingToGreyPool = false;

    private boolean movingFromGreyPool = false;

    private boolean closeOrderSent = false;

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public boolean isMovingFromGreyPool() {
        return movingFromGreyPool;
    }

    public void setMovingFromGreyPool(boolean movingFromGreyPool) {
        this.movingFromGreyPool = movingFromGreyPool;
    }

    public boolean isMovingToGreyPool() {
        return movingToGreyPool;
    }

    public void setMovingToGreyPool(boolean movingToGreyPool) {
        this.movingToGreyPool = movingToGreyPool;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }

    private double limitPrice;

    public double getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(double triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public long getLimitOrderId() {
        return limitOrderId;
    }

    public void setLimitOrderId(long limitOrderId) {
        this.limitOrderId = limitOrderId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getStopOrderId() {
        return stopOrderId;
    }

    public void setStopOrderId(long stopOrderId) {
        this.stopOrderId = stopOrderId;
    }

    public long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(long previousId) {
        this.previousId = previousId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getPL() {
        return pl;
    }

    public void setPL(double pl) {
        this.pl = pl;
    }

    public double getCommissions() {
        return commissions;
    }

    public void setCommissions(double commissions) {
        this.commissions = commissions;
    }

    public double getMargin() {
        return marginFactor * size;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getMarginFactor() {
        return marginFactor;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getExternalOwner() {
        return externalOwner;
    }

    public void setExternalOwner(String externalOwner) {
        this.externalOwner = externalOwner;
    }

    public int getSide() {
        return side;
    }

    public String getSideAsString() {
        return (side == Order.SELL ? "S" : "B");
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getType() {
        return type;
    }

    public String getTypeAsString() {
        if (type == Order.MARKET) {
            return "Market";
        } else if (type == Order.STOP) {
            return "Stop";
        } else if (type == Order.LIMIT) {
            return "Limit";
        } else {
            return "Unknown type: " + type;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public boolean isForceOpen() {
        return forceOpen;
    }

    public void setForceOpen(boolean forceOpen) {
        this.forceOpen = forceOpen;
    }

    public boolean isLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(boolean lastPosition) {
        this.lastPosition = lastPosition;
    }

    public boolean isSynthetic() {
        return synthetic;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    public boolean isForceClose() {
        return forceClose;
    }

    public void setForceClose(boolean forceClose) {
        this.forceClose = forceClose;
    }

    public long getCloseOrderId() {
        return closeOrderId;
    }

    public void setCloseOrderId(long closeOrderId) {
        this.closeOrderId = closeOrderId;
    }

    public boolean isCloseOrderSent() {
        return closeOrderSent;
    }

    public void setCloseOrderSent(boolean closeOrderSent) {
        this.closeOrderSent = closeOrderSent;
    }

    public String toString() {
        DecimalFormat format = new DecimalFormat("#0.00");
        return "Order ID: " + id + "/" + externalId + " Side: " + (side == BUY ? "Buy" : "Sell") + " Owner: " + owner + " ExternalOwner: " + externalOwner + " Size: " + size + " Symbol: " + symbol + " Open price: " + openPrice + " Close price: " + closePrice + " PL: " + format.format(pl) + " Open date: " + openDate + " Type: " + type + " Status: " + status;
    }

    public Order clone() {
        try {
            Order order = (Order) super.clone();
            return order;
        } catch (CloneNotSupportedException cloneE) {
        }
        return null;
    }
}
