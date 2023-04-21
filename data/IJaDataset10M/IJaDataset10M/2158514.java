package net.sourceforge.nattable.example.pricing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PricingDataBean {

    private String batsTicker;

    private double bid;

    private double bidYield;

    private double ask;

    private double askYield;

    private double bidOverAsk;

    private double bidOverAskP;

    private double bidSpread;

    private double askSpread;

    private String bidAskType;

    private double closingPrice;

    private double closingYield;

    private double closingSpread;

    private double priceChange;

    private double yieldChange;

    private double spreadChange;

    private double basisPointValue;

    private double modDuration;

    private double convexity;

    private String nativeTradingGroup;

    private double tgPosition;

    private double tgPL;

    private double tgClosingPL;

    private double tgCostOfInventory;

    private double tgAverageCost;

    private double tgUnrealizedPL;

    private double tgNetPL;

    private double idnBid;

    private double idnBidYield;

    private double idnBidSize;

    private double idnBidSpread;

    private double idnAskYield;

    private double tdPosition;

    private double tdTradingPL;

    private double tdClosingPL;

    private double tdCostOfInventory;

    private double tdAvgCost;

    private double tdUnrealizedPL;

    private double tdNetPL;

    private String pricingSource;

    private String comments;

    private String alias;

    private String baseIssue;

    private String pricingModel;

    private String securityType;

    private String errorMessage;

    private int errorSeverity;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    public String getBatsTicker() {
        return batsTicker;
    }

    public void setBatsTicker(String batsTicker) {
        this.batsTicker = batsTicker;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getBidYield() {
        return bidYield;
    }

    public void setBidYield(double bidYield) {
        this.bidYield = bidYield;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getAskYield() {
        return askYield;
    }

    public void setAskYield(double askYield) {
        this.askYield = askYield;
    }

    public double getBidOverAsk() {
        return bidOverAsk;
    }

    public void setBidOverAsk(double bidOverAsk) {
        this.bidOverAsk = bidOverAsk;
    }

    public String getBidAskType() {
        return bidAskType;
    }

    public void setBidAskType(String bidkAskType) {
        this.bidAskType = bidkAskType;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public double getClosingYield() {
        return closingYield;
    }

    public void setClosingYield(double closingYield) {
        this.closingYield = closingYield;
    }

    public double getClosingSpread() {
        return closingSpread;
    }

    public void setClosingSpread(double closingSpread) {
        this.closingSpread = closingSpread;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getYieldChange() {
        return yieldChange;
    }

    public void setYieldChange(double yieldChange) {
        this.yieldChange = yieldChange;
    }

    public double getSpreadChange() {
        return spreadChange;
    }

    public void setSpreadChange(double spreadChange) {
        this.spreadChange = spreadChange;
    }

    public double getBasisPointValue() {
        return basisPointValue;
    }

    public void setBasisPointValue(double basisPointValue) {
        this.basisPointValue = basisPointValue;
    }

    public double getModDuration() {
        return modDuration;
    }

    public void setModDuration(double modDuration) {
        this.modDuration = modDuration;
    }

    public double getConvexity() {
        return convexity;
    }

    public void setConvexity(double convexity) {
        this.convexity = convexity;
    }

    public String getNativeTradingGroup() {
        return nativeTradingGroup;
    }

    public void setNativeTradingGroup(String nativeTradingGroup) {
        this.nativeTradingGroup = nativeTradingGroup;
    }

    public double getTgPosition() {
        return tgPosition;
    }

    public void setTgPosition(double tgPosition) {
        this.tgPosition = tgPosition;
    }

    public double getTgPL() {
        return tgPL;
    }

    public void setTgPL(double tgPL) {
        this.tgPL = tgPL;
    }

    public double getTgClosingPL() {
        return tgClosingPL;
    }

    public void setTgClosingPL(double tgClosingPL) {
        this.tgClosingPL = tgClosingPL;
    }

    public double getTgCostOfInventory() {
        return tgCostOfInventory;
    }

    public void setTgCostOfInventory(double tgCostOfInventory) {
        this.tgCostOfInventory = tgCostOfInventory;
    }

    public double getTgAverageCost() {
        return tgAverageCost;
    }

    public void setTgAverageCost(double tgAverageCost) {
        this.tgAverageCost = tgAverageCost;
    }

    public double getTgUnrealizedPL() {
        return tgUnrealizedPL;
    }

    public void setTgUnrealizedPL(double tgUnrealizedPL) {
        this.tgUnrealizedPL = tgUnrealizedPL;
    }

    public double getTgNetPL() {
        return tgNetPL;
    }

    public void setTgNetPL(double tgNetPL) {
        this.tgNetPL = tgNetPL;
    }

    public double getIdnBid() {
        return idnBid;
    }

    public void setIdnBid(double idnBid) {
        this.idnBid = idnBid;
    }

    public double getIdnBidYield() {
        return idnBidYield;
    }

    public void setIdnBidYield(double idnBidYield) {
        this.idnBidYield = idnBidYield;
    }

    public double getIdnBidSize() {
        return idnBidSize;
    }

    public void setIdnBidSize(double idnBidSize) {
        this.idnBidSize = idnBidSize;
    }

    public double getIdnBidSpread() {
        return idnBidSpread;
    }

    public void setIdnBidSpread(double idnBidSpread) {
        this.idnBidSpread = idnBidSpread;
    }

    public double getIdnAskYield() {
        return idnAskYield;
    }

    public void setIdnAskYield(double idnAskYield) {
        this.idnAskYield = idnAskYield;
    }

    public double getTdPosition() {
        return tdPosition;
    }

    public void setTdPosition(double tdPosition) {
        this.tdPosition = tdPosition;
    }

    public double getTdTradingPL() {
        return tdTradingPL;
    }

    public void setTdTradingPL(double tdTradingPL) {
        this.tdTradingPL = tdTradingPL;
    }

    public double getTdClosingPL() {
        return tdClosingPL;
    }

    public void setTdClosingPL(double tdClosingPL) {
        this.tdClosingPL = tdClosingPL;
    }

    public double getTdCostOfInventory() {
        return tdCostOfInventory;
    }

    public void setTdCostOfInventory(double tdCostOfInventory) {
        this.tdCostOfInventory = tdCostOfInventory;
    }

    public double getTdAvgCost() {
        return tdAvgCost;
    }

    public void setTdAvgCost(double tdAvgCost) {
        this.tdAvgCost = tdAvgCost;
    }

    public double getTdUnrealizedPL() {
        return tdUnrealizedPL;
    }

    public void setTdUnrealizedPL(double tdUnrealizedPL) {
        this.tdUnrealizedPL = tdUnrealizedPL;
    }

    public double getTdNetPL() {
        return tdNetPL;
    }

    public void setTdNetPL(double tdNetPL) {
        this.tdNetPL = tdNetPL;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBaseIssue() {
        return baseIssue;
    }

    public void setBaseIssue(String baseIssue) {
        this.baseIssue = baseIssue;
    }

    public String getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(String pricingModel) {
        this.pricingModel = pricingModel;
    }

    public PropertyChangeSupport getSupport() {
        return support;
    }

    public String getPricingSource() {
        return pricingSource;
    }

    public void setPricingSource(String pricingSource) {
        this.pricingSource = pricingSource;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(int errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public double getBidSpread() {
        return bidSpread;
    }

    public void setBidSpread(double bidSpread) {
        this.bidSpread = bidSpread;
    }

    public double getAskSpread() {
        return askSpread;
    }

    public void setAskSpread(double askSpread) {
        this.askSpread = askSpread;
    }

    public double getBidOverAskP() {
        return bidOverAskP;
    }

    public void setBidOverAskP(double bidOverAskP) {
        this.bidOverAskP = bidOverAskP;
    }
}
