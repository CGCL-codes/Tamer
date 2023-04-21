package com.ecs.etrade.bo.config;

import java.util.ArrayList;
import com.ecs.etrade.da.BankDetails;
import com.ecs.etrade.da.BankDetailsId;
import com.ecs.etrade.da.ContactDetails;

public interface BankManager {

    void createBank(BankDetails bankdetails, ContactDetails contactDetails) throws ConfigException;

    BankDetails getBankDetails(BankDetailsId id) throws ConfigException;

    ArrayList<BankDetails> getAllBanks() throws ConfigException;

    BankDetails getBankDetails(String bankName, String branchName) throws ConfigException;

    boolean updateBankDetails(BankDetails bankDetails) throws ConfigException;

    boolean deleteBankDetails(BankDetailsId bankDetailsId) throws ConfigException;
}
