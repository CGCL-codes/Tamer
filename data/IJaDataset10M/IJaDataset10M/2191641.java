package org.tripcom.api.ws.client;

import java.util.List;

public class SubscriptionCallBack {

    private TSClient client;

    private String subscription;

    public SubscriptionCallBack(String subscription, TSClient client, List<String> results) {
        this.client = client;
        this.subscription = subscription;
        startSubsubsciption(results);
    }

    private void startSubsubsciption(final List<String> results) {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    String s = client.subscriptionEndpoint(subscription);
                    while (s.equals("Timeout")) {
                        s = client.subscriptionEndpoint(subscription);
                    }
                    results.add(s);
                }
            }
        }).start();
    }
}
