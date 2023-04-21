package com.odal.petstore.domain;

import com.odal.petstore.persistence.gen.bean.OrderPoBean;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gennady Krizhevsky
 */
public class Order extends OrderPoBean {

    public Order() {
    }

    public Order(long orderid) {
        super(orderid);
    }

    public void initOrder(Account account, Cart cart) {
        setUserId(account.getUserName());
        setOrderDate(new Date());
        setShipToFirstName(account.getFirstName());
        setShipToLastName(account.getLastName());
        setShipAddress1(account.getAddress1());
        setShipAddress2(account.getAddress2());
        setShipCity(account.getCity());
        setShipState(account.getState());
        setShipZip(account.getZip());
        setShipCountry(account.getCountry());
        setBillToFirstName(account.getFirstName());
        setBillToLastName(account.getLastName());
        setBillAddress1(account.getAddress1());
        setBillAddress2(account.getAddress2());
        setBillCity(account.getCity());
        setBillState(account.getState());
        setBillZip(account.getZip());
        setBillCountry(account.getCountry());
        setTotalPrice(cart.getSubTotal());
        setCreditCard("999 9999 9999 9999");
        setExpiryDate("12/03");
        setCardType("Visa");
        setCourier("UPS");
        setLocale("CA");
        setStatus("P");
        Iterator i = cart.getAllCartItems();
        while (i.hasNext()) {
            CartItem cartItem = (CartItem) i.next();
            addLineItem(cartItem);
        }
    }

    public void addLineItem(CartItem cartItem) {
        List lineItems = getLineItems();
        LineItem lineItem = new LineItem(lineItems.size() + 1, cartItem);
        addLineItem(lineItem);
    }
}
