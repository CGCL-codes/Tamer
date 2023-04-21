package net.sourceforge.entrainer.mediator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class exists to decouple classes. Rather than having gui classes setting
 * values in utility or sound classes, the class that wishes to notify about
 * changes to values simply constructs a {@link ReceiverChangeEvent} object with
 * the appropriate parameters and uses a {@link Sender} object to notify all
 * {@link Receiver}'s of the event. Each implementation of a receiver will
 * determine which parameters are of interest and will take the appropriate
 * action.
 * 
 * @author burton
 * @see Sender
 * @see SenderAdapter
 * @see Receiver
 * @see ReceiverAdapter
 * @see ReceiverChangeEvent
 * @see MediatorConstants
 */
public class EntrainerMediator {

    private List<Sender> senders = new CopyOnWriteArrayList<Sender>();

    private List<Receiver> receivers = new CopyOnWriteArrayList<Receiver>();

    private static EntrainerMediator mediator = new EntrainerMediator();

    private Executor executor = Executors.newCachedThreadPool();

    /**
	 * Returns the only instance of this class.
	 */
    public static EntrainerMediator getInstance() {
        return mediator;
    }

    private EntrainerMediator() {
        super();
    }

    /**
	 * Adds a {@link Sender} to the list of senders.
	 * 
	 * @param sender
	 */
    public synchronized void addSender(Sender sender) {
        senders.add(sender);
    }

    /**
	 * Removes the specified {@link Sender} from the list.
	 * 
	 * @param sender
	 */
    public synchronized void removeSender(Sender sender) {
        senders.remove(sender);
    }

    /**
	 * Adds a {@link Receiver} to the list of receivers.
	 * 
	 * @param receiver
	 */
    public void addReceiver(Receiver receiver) {
        receivers.add(receiver);
    }

    public void addFirstReceiver(Receiver receiver) {
        receivers.add(0, receiver);
    }

    /**
	 * Removes the {@link Receiver} associated with the object which instantiated
	 * it.
	 * 
	 * @param source
	 */
    public void removeReceiver(Object source) {
        if (source != null) {
            for (int i = receivers.size() - 1; i >= 0; i--) {
                if (receivers.get(i).getSource().equals(source)) {
                    receivers.remove(i);
                }
            }
        }
    }

    void notifyReceivers(final ReceiverChangeEvent e) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                for (Receiver receiver : receivers) {
                    receiver.receiverChangeEventPerformed(e);
                }
            }
        });
    }
}
