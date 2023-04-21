package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.Date;

public class BFMarketData implements Serializable {

    private int marketId;

    private String marketName;

    private String marketType;

    private String marketStatus;

    private Date eventDate;

    private String menuPath;

    private String eventHierarchy;

    private String betDelay;

    private int exchangeId;

    private String countryCode;

    private Date lastRefresh;

    private int numberOfRunners;

    private int numberOfWinners;

    private double totalAmountMatched;

    private boolean bsbMarket;

    private boolean turningInPlay;

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getEventHierarchy() {
        return eventHierarchy;
    }

    public void setEventHierarchy(String eventHierarchy) {
        this.eventHierarchy = eventHierarchy;
    }

    public String getBetDelay() {
        return betDelay;
    }

    public void setBetDelay(String betDelay) {
        this.betDelay = betDelay;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getNumberOfRunners() {
        return numberOfRunners;
    }

    public void setNumberOfRunners(int numberOfRunners) {
        this.numberOfRunners = numberOfRunners;
    }

    public int getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }

    public double getTotalAmountMatched() {
        return totalAmountMatched;
    }

    public void setTotalAmountMatched(double totalAmountMatched) {
        this.totalAmountMatched = totalAmountMatched;
    }

    public boolean isBsbMarket() {
        return bsbMarket;
    }

    public void setBsbMarket(boolean bsbMarket) {
        this.bsbMarket = bsbMarket;
    }

    public boolean isTurningInPlay() {
        return turningInPlay;
    }

    public void setTurningInPlay(boolean turningInPlay) {
        this.turningInPlay = turningInPlay;
    }

    public String getEventName() {
        String[] split = this.menuPath.split("\\\\");
        return split[split.length - 1];
    }

    @Override
    public String toString() {
        return marketName + ":" + menuPath + ":" + eventDate + ":" + marketType + ":" + marketStatus + ":" + numberOfRunners + ":" + numberOfWinners + ":" + totalAmountMatched;
    }
}
