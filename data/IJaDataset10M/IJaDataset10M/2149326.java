package com.google.checkout.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;
import org.w3c.dom.Element;
import com.google.checkout.checkout.Item;
import com.google.checkout.util.Utils;

public class NewOrderNotificationTest extends TestCase {

    String notificationXml;

    public NewOrderNotificationTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        notificationXml = inputStreamAsString(this.getClass().getResourceAsStream("/com/google/checkout/notification/new-order-notification.xml"));
    }

    public void testNotification() throws Exception {
        NewOrderNotification n = new NewOrderNotification(notificationXml);
        assertEquals(n.getGoogleOrderNo(), "841171949013218");
        assertEquals(n.getSerialNumber(), "85f54628-538a-44fc-8605-ae62364f6c71");
        assertEquals(n.getTimestamp(), Utils.parseDate("2006-03-17T12:20:46.137Z"));
        assertEquals(n.getAdjustmentTotal(), 13.06f, 0f);
        assertEquals(n.getBuyerId(), 419797746651146l);
        assertEquals(n.getOrderTotal(), 226.06f, 0f);
        assertEquals(n.getTotalTax(), 15.06f, 0f);
        Address bill = n.getBuyerBillingAddress();
        assertEquals(bill.getAddress1(), "1250 Shoreline Blvd");
        assertEquals(bill.getAddress2(), "");
        assertEquals(bill.getCity(), "Mountain View");
        assertEquals(bill.getCompanyName(), "");
        assertEquals(bill.getContactName(), "Knikki Beckwell");
        assertEquals(bill.getCountryCode(), "US");
        assertEquals(bill.getFax(), "");
        assertEquals(bill.getPhone(), "");
        assertEquals(bill.getPostalCode(), "94043");
        assertEquals(bill.getRegion(), "CA");
        assertEquals(bill.getEmail(), "knbw@gmail.com");
        Address ship = n.getBuyerBillingAddress();
        assertEquals(ship.getAddress1(), "1250 Shoreline Blvd");
        assertEquals(ship.getAddress2(), "");
        assertEquals(ship.getCity(), "Mountain View");
        assertEquals(ship.getCompanyName(), "");
        assertEquals(ship.getContactName(), "Knikki Beckwell");
        assertEquals(ship.getCountryCode(), "US");
        assertEquals(ship.getFax(), "");
        assertEquals(ship.getPhone(), "");
        assertEquals(ship.getPostalCode(), "94043");
        assertEquals(ship.getRegion(), "CA");
        assertEquals(ship.getEmail(), "knbw@gmail.com");
        assertEquals(n.getCartExpiration(), Utils.parseDate("2006-12-31T23:59:59"));
        assertEquals(n.getFinancialOrderState(), FinancialOrderState.CHARGEABLE);
        assertEquals(n.getFulfillmentOrderState(), FulfillmentOrderState.NEW);
        assertEquals(n.getGoogleOrderNo(), "841171949013218");
        Collection items = n.getItems();
        assertEquals(items.size(), 2);
        Iterator itemIt = items.iterator();
        Item i1 = (Item) itemIt.next();
        assertEquals(i1.getItemDescription(), "A pack of highly nutritious dried food for emergency - store in your garage for up to one year!!");
        assertEquals(i1.getItemName(), "Dry Food Pack AA1453");
        assertEquals(i1.getMerchantItemId(), "");
        assertEquals(i1.getMerchantPrivateItemData().length, 0);
        assertEquals(i1.getQuantity(), 1);
        assertEquals(i1.getUnitPriceAmount(), 35.00f, 0f);
        assertEquals(i1.getUnitPriceCurrency(), "USD");
        assertEquals(i1.getTaxTableSelector(), "food");
        Item i2 = (Item) itemIt.next();
        assertEquals(i2.getItemDescription(), "Portable MP3 player - stores 500 songs, easy-to-use interface, color display");
        assertEquals(i2.getItemName(), "MegaSound 2GB MP3 Player");
        assertEquals(i2.getMerchantItemId(), "");
        Element[] pd = i2.getMerchantPrivateItemData();
        assertEquals(pd.length, 1);
        assertEquals(pd[0].getNodeName(), "my-data");
        assertEquals(i2.getQuantity(), 1);
        assertEquals(i2.getUnitPriceAmount(), 178.00f, 0f);
        assertEquals(i2.getUnitPriceCurrency(), "USD");
        assertEquals(i2.getTaxTableSelector(), "");
        assertEquals(n.getMerchantCodes().size(), 2);
        assertEquals(n.getMerchantPrivateDataNodes(), null);
        assertEquals(n.getOrderCurrencyCode(), "USD");
        Shipping s = n.getShipping();
        assertTrue(s instanceof MerchantCalculatedShippingAdjustment);
        assertEquals(s.getShippingCost(), 13.00f, 0f);
        assertEquals(s.getShippingName(), "SuperShip");
    }

    private String inputStreamAsString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }
}
