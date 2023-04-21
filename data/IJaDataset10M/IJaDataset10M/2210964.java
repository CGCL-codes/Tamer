package it.hotel.model.price;

import java.math.BigDecimal;

/**
 * 
 *
 */
public class PricePeriod {

    private BigDecimal money;

    private String beginDate;

    private String finish;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
