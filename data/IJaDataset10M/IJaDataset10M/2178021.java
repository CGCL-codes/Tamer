package pl.edu.pjwstk.mteam.pubsub.operation;

import pl.edu.pjwstk.mteam.pubsub.core.Event;
import pl.edu.pjwstk.mteam.pubsub.core.Operation;
import pl.edu.pjwstk.mteam.pubsub.core.PubSubConstants;
import pl.edu.pjwstk.mteam.pubsub.core.Subscriber;

/**
 * Class representing subscribe operation.
 * 
 * @author Paulina Adamska s3529@pjwstk.edu.pl
 */
public class SubscribeOperation extends Operation {

    /**
	 * Creates new subscribe operation. 
	 * @param id Topic id.
	 * @param user Subscriber performing this operation. It is irrelevant when
	 * 			   method is used for creating operation rule.
	 * @param e Event, this operation is associated with. For 'subscribe' 
	 * 			it will be {@link PubSubConstants#EVENT_ALL} as this operation can't be divided
	 * 		    into several events.
	 */
    public SubscribeOperation(String id, Subscriber user, Event e) {
        super(PubSubConstants.OPERATION_SUBSCRIBE, id, user, e);
    }
}
