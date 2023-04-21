package org.bcholmes.jmicro.transfer.service.impl;

import java.math.BigDecimal;
import org.bcholmes.jmicro.transfer.service.Credentials;
import org.bcholmes.jmicro.transfer.service.FundsTransferAgency;
import org.bcholmes.jmicro.transfer.service.FundsTransferService;
import org.bcholmes.jmicro.transfer.service.Money;
import org.bcholmes.jmicro.transfer.service.TransferInformation;

public class FundsTransferServiceImpl implements FundsTransferService {

    public FundsTransferAgency[] getAgencies() {
        return new FundsTransferAgency[] { FundsTransferAgency.CAM, FundsTransferAgency.MONEYGRAM, FundsTransferAgency.UNITRANSFER };
    }

    public String send() {
        return "ABCD-1234";
    }

    public void connect(FundsTransferAgency agency, Credentials credentials) {
    }

    public TransferInformation getTransferInformation(String confirmationNumber) {
        TransferInformation transferInformation = new TransferInformation();
        transferInformation.setName("Fred Flintstone");
        transferInformation.setTelephoneNumber("1234-5678");
        transferInformation.setAddress("123 Limestone Ave, Bedrock");
        transferInformation.setAmount(new Money(new BigDecimal("250.00"), "USD"));
        return transferInformation;
    }

    public void completeTransfer(TransferInformation transferInformation) {
    }
}
