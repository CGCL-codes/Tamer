package realtimetrading.exchage;

import java.math.BigDecimal;
import realtimetrading.trader.Trader;

public class Contract {

    Long size;

    BigDecimal price;

    Trader Buyer;

    Trader Seller;

    public Contract(Trader Buyer, Trader Seller, BigDecimal price, Long size) {
        this.Buyer = Buyer;
        this.Seller = Seller;
        this.price = price;
        this.size = size;
    }

    public String toString() {
        return "[" + Buyer + " , " + Seller + " , " + price + " , " + size;
    }

    public Trader getBuyer() {
        return Buyer;
    }

    public void setBuyer(Trader buyer) {
        Buyer = buyer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Trader getSeller() {
        return Seller;
    }

    public void setSeller(Trader seller) {
        Seller = seller;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
