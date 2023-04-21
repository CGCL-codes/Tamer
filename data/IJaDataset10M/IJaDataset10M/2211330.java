package openjsip.proxy;

import org.apache.log4j.Logger;
import javax.sip.ClientTransaction;
import javax.sip.TransactionState;
import javax.sip.SipProvider;
import javax.sip.SipException;
import javax.sip.message.Request;
import java.util.TimerTask;

public class TimerCTask extends TimerTask {

    private ClientTransaction clientTransaction;

    private SipProvider sipProvider;

    private Logger log;

    private Proxy proxy;

    public TimerCTask(ClientTransaction clientTransaction, SipProvider sipProvider, Proxy proxy, Logger logger) {
        this.clientTransaction = clientTransaction;
        this.sipProvider = sipProvider;
        this.proxy = proxy;
        this.log = logger;
    }

    public void run() {
        try {
            if (log.isTraceEnabled()) log.trace("Timer C for CT " + clientTransaction + " fired.");
            if (clientTransaction.getState().getValue() == TransactionState._PROCEEDING) {
                Request cancelRequest = clientTransaction.createCancel();
                ClientTransaction cancelTransaction = sipProvider.getNewClientTransaction(cancelRequest);
                proxy.getSnmpAssistant().decrementSnmpInteger(Proxy.SNMP_OID_NUM_CLIENT_TRANSACTIONS);
                cancelTransaction.sendRequest();
                if (log.isTraceEnabled()) log.trace("Timer C dispatched CANCEL request for CT " + clientTransaction);
            } else if (log.isTraceEnabled()) log.trace("Timer C associated CT is not in PROCEEDING state: " + clientTransaction.getState() + ". Do nothing.");
        } catch (SipException ex) {
            if (log.isDebugEnabled()) log.debug("Exception raised while processing Timer C action: " + ex.getMessage());
            if (log.isTraceEnabled()) log.trace("Exception raised while processing Timer C action: " + ex.getMessage(), ex);
        }
    }
}
