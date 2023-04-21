package phex.msghandling;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import phex.host.Host;
import phex.msg.InvalidMessageException;
import phex.msg.Message;

public class MessageSubscriberList<E extends Message> implements MessageSubscriber<E> {

    private List<MessageSubscriber<E>> subscriberList;

    MessageSubscriberList() {
        subscriberList = new CopyOnWriteArrayList<MessageSubscriber<E>>();
    }

    MessageSubscriberList(MessageSubscriber<E> subscriber1, MessageSubscriber<E> subscriber2) {
        this();
        addSubscribers(subscriber1, subscriber2);
    }

    public void onMessage(E message, Host sourceHost) throws InvalidMessageException {
        for (MessageSubscriber<E> messageSubscriber : subscriberList) {
            messageSubscriber.onMessage(message, sourceHost);
        }
    }

    public void addSubscriber(MessageSubscriber<E> subscriber) {
        subscriberList.add(subscriber);
    }

    public void addSubscribers(MessageSubscriber<E>... subscriberArr) {
        subscriberList.addAll(Arrays.asList(subscriberArr));
    }

    public void removeSubscriber(MessageSubscriber<E> subscriber) {
        subscriberList.remove(subscriber);
    }
}
