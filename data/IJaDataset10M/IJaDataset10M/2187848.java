package com.csaba.connector.dummy.model;

import java.util.Currency;
import com.csaba.connector.dummy.Codes;
import com.csaba.connector.model.Bank;
import com.csaba.connector.model.Country;

public class DummyBank extends Bank {

    private static final long serialVersionUID = -2583282543566555950L;

    private static final DummyBank instance = new DummyBank();

    public static DummyBank getInstance() {
        return instance;
    }

    public DummyBank() {
        super();
        setId("DUMMY");
        setName("Dummy Bank SA");
        setCountry(Country.HUNGARY);
        setBaseCurrency(Currency.getInstance("HUF"));
        setLargeIcon(Codes.class.getResource("/com/csaba/connector/dummy/image/dummy_logo.png"));
        setSmallIcon(Codes.class.getResource("/com/csaba/connector/dummy/image/dummy_logo_16.png"));
        setCallCenterURL("123");
        setInternetBankURL("http://csaba.googlecode.com");
        setMobileBankURL("http://start.bankdroid.info");
    }
}
