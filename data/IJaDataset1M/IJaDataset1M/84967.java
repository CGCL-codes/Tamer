package examples.message.syncpingpong;

import anima.message.IBroker;
import anima.message.IMessage;
import anima.message.IMessageFactory;
import anima.message.ISourceMessage;
import anima.message.ISyncReceiver;

public class Ping implements ISourceMessage {

    private IBroker broker;

    private IMessageFactory factory;

    private ISyncReceiver thePong;

    public Ping(IMessageFactory factory, IBroker broker, ISyncReceiver thePong) {
        this.factory = factory;
        this.broker = broker;
        this.thePong = thePong;
    }

    public void start() {
        System.out.println("ping");
        IMessage message = factory.createMessage("ping", this);
        IMessage returnMessage = broker.dispatchSyncMessage(thePong, message);
        if (returnMessage != null && returnMessage.getLabel().equalsIgnoreCase("pong")) System.out.println("end");
    }
}
