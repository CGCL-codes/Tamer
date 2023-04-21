package fr.jussieu.gla.wasa.monitor.gui.event;

import fr.jussieu.gla.wasa.monitor.model.EngineNode;
import fr.jussieu.gla.wasa.monitor.gui.event.NodeCreatedEvent;

/**
 * Posted when an {@link EngineNode}
 * @author Laurent Caillette
 * @version $Revision: 1.2 $ $Date: 2002/03/22 19:06:29 $
 */
public class EngineNodeCreatedEvent extends NodeCreatedEvent {

    public EngineNodeCreatedEvent(EngineNode source) {
        super(source);
    }

    public EngineNode getEngineNodeSource() {
        return (EngineNode) getSource();
    }
}
