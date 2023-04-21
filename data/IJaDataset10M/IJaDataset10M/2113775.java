package org.privale.coreclients.depotclient;

import org.privale.utils.network.ReadComplete;
import org.privale.utils.network.Transaction;

public class CounterChallengeResponceReader implements ReadComplete {

    private CoreDepot Depot;

    private DepotQueueHandler Handler;

    public CounterChallengeResponceReader(CoreDepot d, DepotQueueHandler hand) {
        Depot = d;
        Handler = hand;
    }

    public void Complete(Transaction trans, Object readobj) {
        Long v = (Long) readobj;
        if (v != null) {
            Depot.CheckAnswerAndSendData(v, Handler);
        } else {
            Handler.Cancel();
        }
    }
}
