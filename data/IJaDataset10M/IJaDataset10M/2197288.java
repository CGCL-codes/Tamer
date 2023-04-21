package org.privale.coreclients.depotclient;

import java.io.File;
import org.privale.utils.network.ReadComplete;
import org.privale.utils.network.Transaction;

public class TokenReader implements ReadComplete {

    private CoreDepot Depot;

    private DepotQueueHandler Handler;

    public TokenReader(CoreDepot dep, DepotQueueHandler hand) {
        Depot = dep;
        Handler = hand;
    }

    public void Complete(Transaction trans, Object readobj) {
        File f = (File) readobj;
        if (f != null) {
            Depot.SendChallenge(f, Handler);
        } else {
            Handler.Cancel();
        }
    }
}
