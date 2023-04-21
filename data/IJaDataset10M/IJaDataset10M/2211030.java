package org.mintr.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RTStockQuote {

    private String stockCode = "";

    private String lastUpdate = "NA";

    private String price = "NA";

    private String high = "NA";

    private String low = "NA";

    private String change = "NA";

    public RTStockQuote() {
    }

    public RTStockQuote(String code) {
        this.stockCode = code;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    @Override
    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("[");
        sbuilder.append(this.stockCode);
        sbuilder.append("] ");
        sbuilder.append(this.price);
        sbuilder.append(",");
        sbuilder.append(this.change);
        sbuilder.append(",");
        sbuilder.append(this.low);
        sbuilder.append("-");
        sbuilder.append(this.high);
        sbuilder.append(" (");
        sbuilder.append(this.lastUpdate);
        sbuilder.append(")");
        return sbuilder.toString();
    }
}
