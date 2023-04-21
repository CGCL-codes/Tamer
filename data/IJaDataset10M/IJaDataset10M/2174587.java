package org.bcholmes.jmicro.transfer.process;

import org.bcholmes.jmicro.transfer.service.Credentials;
import org.bcholmes.jmicro.transfer.service.FundsTransferAgency;
import org.bcholmes.jmicro.transfer.service.TransferInformation;

public interface ReceiveFundsGui extends Gui {

    public ProcessResult<FundsTransferAgency> selectAgency();

    public ProcessResult<Credentials> getCredentials();

    public ProcessResult<String> getConfirmationNumber();

    public ProcessResult<Object> displayTransferInformation(TransferInformation transferInformation);
}
