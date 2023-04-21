package com.trechner.gsmmodem4j.examples;

import com.trechner.kgsmmodem4j.GSMModem;
import com.trechner.kgsmmodem4j.driver.GSMModemDriver;
import com.trechner.kgsmmodem4j.driver.GenericDriver;
import org.apache.log4j.Logger;
import com.trechner.kgsmmodem4j.event.GSMModemListener;
import com.trechner.kgsmmodem4j.sms.SMS;
import java.util.List;

/**
 * Simple terminal.
 *
 * @author Karl Tiller <karl@t-rechner.com>
 *         Copyright (c) 2006-2009 T-Rechner Ltd.
 */
public class GetSMSList implements GSMModemListener {

    public static final Logger log = Logger.getLogger("main");

    public GetSMSList() throws Exception {
        GSMModem modem = null;
        try {
            modem = new GSMModem(GenericDriver.class, "COM7", 9600);
            modem.addListener(this);
            modem.open();
            Thread.sleep(60000);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        } finally {
            if (modem != null) {
                modem.close();
            }
        }
    }

    public void online(GSMModem modem) {
        log.info("ONLINE");
    }

    public void offline(GSMModem modem) {
        log.info("OFFLINE");
    }

    public boolean init(GSMModem modem) {
        try {
            modem.setSmsFormat(GSMModemDriver.SMSFORMAT_TEXT);
            List<SMS> sms = modem.getSmsList(GSMModemDriver.SMSLIST_ALL);
            log.info("SMS list: " + sms);
        } catch (Exception e) {
            log.error(e);
        }
        return true;
    }

    public void ring(GSMModem modem) {
    }

    public void noCarrier(GSMModem modem, String telNum) {
    }

    public void noDialTone(GSMModem modem, String telNum) {
    }

    public void noAnswer(GSMModem modem, String telNum) {
    }

    public void connect(GSMModem modem, String telNum) {
    }

    public void busy(GSMModem modem, String telNum) {
    }

    public void smsReceived(GSMModem modem, SMS sms) {
    }

    public void smsDelivered(GSMModem modem, int refNum, String telNum, boolean deliveryStatus) {
    }

    public void connected(GSMModem modem, String telNum) {
    }

    public void disconnected(GSMModem modem, String telNum) {
    }

    public void dataReceived(GSMModem modem, String telNum, String message) {
    }

    public static void main(String[] args) throws Exception {
        new GetSMSList();
    }
}
