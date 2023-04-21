package edu.ucdavis.genomics.metabolomics.util.status.notify;

import java.util.List;

/**
 * is a notification system which allows to send notifications to several
 * notifiers
 * 
 * @author wohlgemuth
 */
public interface QueuedNotifier extends Notifier, PriotizedNotifier {

    /**
	 * adds a notifier with the given list of priorities
	 * 
	 * @param notifier
	 */
    public void addNotifier(Notifier notifier);

    /**
	 * removes a notifier
	 * 
	 * @param notifier
	 */
    public void removeNotifier(Notifier notifier);

    /**
	 * returns a list of all registered notifiers
	 * 
	 * @return
	 */
    public List<Notifier> getNotifiers();
}
