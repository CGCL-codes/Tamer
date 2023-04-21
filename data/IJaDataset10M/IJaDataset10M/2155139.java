package com.codemonster.surinam.examples.published;

import com.codemonster.surinam.export.meta.Contract;

/**
 * 
 */
@Contract(author = "Sam Provencher", organization = "http://surinam.sourceforge.net", publicationDate = "2010/02/12", description = "This example contract defines a bank service that only has one user and maintains state" + " so there is no need for db access on the back end. The point is to show how injected configuration" + " can be used to set up a service")
public interface BankAccountService_v1_0 {

    public String getName();

    public float getMaxWithdrawalAmount();

    public void deposit(double amount);

    public void withdraw(double amount);

    public double getCheckingRate();

    public double getBalance();

    public int getLastCheckNumber();

    public long getCurrentATMCount();
}
