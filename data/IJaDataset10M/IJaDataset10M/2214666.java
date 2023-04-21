package com.globant.google.mendoza;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.globant.google.mendoza.malbec.BuyerInformation;
import com.globant.google.mendoza.malbec.SeleniumBuyerRobot;
import com.globant.google.mendoza.malbec.XmlCartEncoder;
import com.globant.google.mendoza.malbec.schema._2.CheckoutShoppingCart;

public final class SeleniumBuyerRobotMock extends SeleniumBuyerRobot {

    public SeleniumBuyerRobotMock(final String buyerUrl, final String url, final XmlCartEncoder theEncoder, final String theMerchantKey, final String theFirefoxBinPath, final String seleniumServerHost, final int seleniumServerPort) {
        super(buyerUrl, url, theEncoder, theMerchantKey, theFirefoxBinPath, seleniumServerHost, seleniumServerPort);
        System.out.println("SeleniumBuyerRobotMock.SeleniumBuyerRobotMock()");
    }

    public String placeOrder() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("cart", MendozaTestConstants.cart);
        CartSender.send(MendozaTestConstants.googleCheckoutMockURL, parameters);
        System.out.println("returning google order number: " + MendozaTestConstants.googleCheckoutOrder);
        return MendozaTestConstants.googleCheckoutOrder;
    }

    public String placeOrder(final CheckoutShoppingCart cart, final String email, final String password) {
        return placeOrder();
    }

    public String placeOrder(final CheckoutShoppingCart cart, final String email, final String password, final List<BuyerInformation> buyerInformation) {
        return placeOrder();
    }

    public String placeOrder(final CheckoutShoppingCart cart, final String email, final String password, final List<BuyerInformation> buyerInformation, final List<String> ccOptions) {
        return placeOrder();
    }

    public String placeOrder(final String cart, final String email, final String password) {
        return placeOrder();
    }

    public String placeOrder(final String cart, final String email, final String password, final List<BuyerInformation> buyerInformation) {
        return placeOrder();
    }

    public String placeOrder(final String cart, final String email, final String password, final List<BuyerInformation> buyerInformation, final List<String> ccOptions) {
        return placeOrder();
    }
}
