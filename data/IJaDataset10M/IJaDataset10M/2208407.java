package AGE;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class BufferedActorCollection implements AbstractBufferedActorCollection {

    private AbstractActorCollection actualCollection = null;

    private AbstractActorCollection addCollection = null;

    private AbstractActorCollection removeCollection = null;

    private boolean clearAll;

    public BufferedActorCollection() {
        actualCollection = new ActorCollection();
        addCollection = new ActorCollection();
        removeCollection = new ActorCollection();
        clearAll = false;
    }

    public void flush() {
        if (clearAll) {
            actualCollection.clear();
            addCollection.clear();
            removeCollection.clear();
            clearAll = false;
        } else {
            actualCollection.addAllActors(addCollection);
            addCollection.clear();
            actualCollection.removeAllActors(removeCollection);
            removeCollection.clear();
        }
    }

    public boolean isConsistent() {
        return (addCollection.isEmpty() && removeCollection.isEmpty());
    }

    public Collection<AbstractActor> getInternalCollection() {
        return actualCollection.getInternalCollection();
    }

    public void addActor(AbstractActor actor) {
        addCollection.addActor(actor);
    }

    public void addAllActors(AbstractActorCollection actorsToBeAdded) {
        addCollection.addAllActors(actorsToBeAdded);
    }

    public boolean containsActor(AbstractActor actor) {
        return actualCollection.containsActor(actor);
    }

    public void removeActor(AbstractActor actor) {
        removeCollection.addActor(actor);
    }

    public void removeAllActors(AbstractActorCollection actorsToBeRemoved) {
        removeCollection.addAllActors(actorsToBeRemoved);
    }

    public int size() {
        return actualCollection.size();
    }

    public boolean isEmpty() {
        return actualCollection.isEmpty();
    }

    /**
     * wird erst beim flush ausgeführt
     */
    public void clear() {
        clearAll = true;
    }

    public void visitAllActors(AbstractActorVisitor visitor) {
        actualCollection.visitAllActors(visitor);
    }

    public Iterator<AbstractActor> iterator() {
        return actualCollection.iterator();
    }
}
