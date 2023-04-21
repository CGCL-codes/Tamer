package org.iqual.chaplin.example.dci.transfer;

import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * User: zslajchrt
 * Date: Mar 30, 2009
 * Time: 4:32:22 PM
 */
public class TransferUC {

    public static void doit(String sourceAccountNumber, String destinationAccountNumber, int amount, GUI gui) throws CannotPlayRoleException, InsufficientFundsException {
        Account srcAccount = $(sourceAccountNumber);
        Account destAccount = $(destinationAccountNumber);
        MoneySource source;
        try {
            source = $(srcAccount);
        } catch (ClassCastException e) {
            throw new CannotPlayRoleException(MoneySource.class, sourceAccountNumber, e);
        }
        MoneySink sink;
        try {
            sink = $(destAccount);
        } catch (ClassCastException e) {
            throw new CannotPlayRoleException(MoneySink.class, destinationAccountNumber, e);
        }
        source.transferMoneyTo(sink, amount);
        gui.message("Source balance:" + srcAccount.availableBalance());
        gui.message("Destination balance:" + destAccount.availableBalance());
    }
}
