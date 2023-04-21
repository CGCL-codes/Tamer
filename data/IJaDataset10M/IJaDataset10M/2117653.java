package net.sf.jtmt.concurrent.actorsguild;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Message;

/**
 * Download Actor.
 * @author Sujit Pal
 * @version $Revision: 4 $
 */
public class DownloadActor extends Actor {

    public IndexActor indexActor;

    @Initializer
    public AsyncResult<DownloadActor> init(IndexActor indexActor) {
        this.indexActor = indexActor;
        return result(this);
    }

    @Message
    public AsyncResult<Void> download(int id, String payload) {
        String newPayload = payload.replaceFirst("Requested ", "Downloaded ");
        ActorManager.log(newPayload);
        return indexActor.index(id, newPayload);
    }
}
