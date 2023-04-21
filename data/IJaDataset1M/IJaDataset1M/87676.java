package net.sf.carmaker.rules;

import net.sf.carmaker.orders.IOrder;
import net.sf.carmaker.orders.IOrderManager;

public interface IRuleManager {

    public boolean checkOrder(IOrderManager iOrderManager, IOrder thisOrder);
}
