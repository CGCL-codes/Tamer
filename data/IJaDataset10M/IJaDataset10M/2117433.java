package grammarscope.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Listenee
 * 
 * @author Bernard Bou
 */
public class Listenee<M> {

    /**
	 * Listeners
	 */
    protected List<IListener<M>> theListeners = new ArrayList<IListener<M>>();

    /**
	 * Add listener
	 * 
	 * @param thisListener
	 *            listener
	 */
    public void addListener(final IListener<M> thisListener) {
        if (thisListener == null) throw new IllegalArgumentException();
        this.theListeners.add(thisListener);
    }

    /**
	 * Remove listener
	 * 
	 * @param thisListener
	 *            listener
	 */
    public void removeListener(final IListener<M> thisListener) {
        if (thisListener == null) throw new IllegalArgumentException();
        this.theListeners.remove(thisListener);
    }

    /**
	 * Get listeners
	 * 
	 * @return listeners
	 */
    public List<IListener<M>> getListeners() {
        return this.theListeners;
    }

    /**
	 * Add listeners
	 * 
	 * @param theseListeners
	 *            listeners
	 */
    public void addListeners(final List<IListener<M>> theseListeners) {
        this.theListeners.addAll(theseListeners);
    }

    public void dumpListeners() {
        System.out.println("listeners");
        for (final IListener<M> thisListener : this.theListeners) {
            System.out.println(thisListener.getClass());
        }
    }
}
