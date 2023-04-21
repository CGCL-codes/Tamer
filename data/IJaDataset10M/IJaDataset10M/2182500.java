package net.sf.jtmt.concurrent.actorfoundry;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

/**
 * Index Actor.
 * @author Sujit Pal
 * @version $Revision: 4 $
 */
public class IndexActor extends Actor {

    private static final long serialVersionUID = -7939186176349943105L;

    private ActorName actorManager;

    private ActorName writeActor;

    public IndexActor(ActorName manager) throws RemoteCodeException {
        actorManager = manager;
    }

    @message
    public void index(String message) throws RemoteCodeException {
        String newMessage = message.replaceFirst("Downloaded ", "Indexed ");
        if (writeActor == null) {
            writeActor = create(WriteActor.class, actorManager);
        }
        send(writeActor, "write", newMessage);
    }

    @message
    public void stop() throws RemoteCodeException {
        send(writeActor, "stop");
        ActorManager.decrementLatch();
    }
}
