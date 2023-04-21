package freemind.modes;

import java.util.EventListener;

/**
 * @author Dimitri Polivaev
 * 31.10.2005
 */
public interface NodeViewEventListener extends EventListener {

    /**
     */
    public void nodeViewCreated(NodeViewEvent nodeViewEvent);

    /**
     */
    public void nodeViewRemoved(NodeViewEvent nodeViewEvent);
}
