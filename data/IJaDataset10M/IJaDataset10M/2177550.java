package cbr2.service;

import java.util.Set;

public interface BrokerService {

    public ImageGrant getToken(String image);

    public ImageGrant getToken(String image, Set subscriptions);

    public Subscription getSubscription();
}
