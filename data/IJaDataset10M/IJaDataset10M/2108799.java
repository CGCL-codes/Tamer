package org.ocera.orte;

import org.ocera.orte.types.*;

public class SubscriptionCallback {

    public void callback(RecvInfo info, MessageData message) {
        System.out.println(":j: ****** SubscriptionCallback.callback ******");
    }
}
