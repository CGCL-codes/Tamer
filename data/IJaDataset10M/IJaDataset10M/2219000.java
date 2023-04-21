package behaviours;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author biba
 */
public class ObserverSubscribeBehaviourInitiatorData implements Serializable {

    HashMap<String, SubscriptionList> subscriptions;

    public ObserverSubscribeBehaviourInitiatorData(String agentID, SubscriptionDescription firstSubscription) throws InvalidSubscriptionException {
        this.subscriptions = new HashMap<String, SubscriptionList>();
        SubscriptionList fisrtSl = new SubscriptionList(agentID, firstSubscription);
        subscriptions.put(agentID, fisrtSl);
    }

    public void addSubscriptionDescription(String agentID, SubscriptionDescription sd) throws InvalidSubscriptionException {
        if (sd.getSubscriptionID() == null) {
            throw new InvalidSubscriptionException(null);
        } else if (subscriptions.containsKey(agentID)) {
            subscriptions.get(agentID).addSubscriptionDescription(sd);
        } else {
            subscriptions.put(agentID, new SubscriptionList(agentID, sd));
        }
    }

    public SubscriptionDescription removeSubscriptionDescription(String agentID, String subscriptionID) throws NoSuchSubscriptionDescription {
        if (!subscriptions.containsKey(agentID)) {
            throw new NoSuchSubscriptionDescription(subscriptionID);
        }
        return subscriptions.get(agentID).removeSubscriptionDescription(subscriptionID);
    }
}
