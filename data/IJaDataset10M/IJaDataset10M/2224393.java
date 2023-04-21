package com.zzsoft.app.stock.tradeinformation;

import java.util.Date;

public interface TradeRcI {

    public abstract long getQuantity();

    public abstract void setQuantity(long quantity);

    public abstract Date getDate();

    public abstract void setDate(Date date);

    public abstract int getState();

    public abstract void setState(int state);
}
