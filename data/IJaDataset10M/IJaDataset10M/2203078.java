package test.banking.transfer;

import test.banking.transfer.ExternalBank;
import test.banking.transfer.ExternalBankException;
import test.banking.transfer.InterbankService;

/**
 * Implement this using mockito
 * @author denny
 *
 */
public class TransferService {

    private InterbankService interbankService;

    public TransferService(InterbankService interbankService2) {
        this.interbankService = interbankService2;
    }

    public ExternalBank getExternalBank(int sortcode) throws ExternalBankException {
        ExternalBank bank = interbankService.getBank(sortcode);
        if (bank == null) {
            throw new ExternalBankException("Unable to find bank with sort code: " + sortcode);
        }
        return bank;
    }
}
