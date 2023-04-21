package net.sf.magicmap.client.model.location;

import java.util.EventListener;

/**
 * <p>
 * Class NodeGraphListener ZUSAMMENFASSUNG
 * </p>
 * <p>
 * DETAILS
 * </p>
 *
 * @author Jan Friderici
 *         Date: 21.01.2008
 *         Time: 09:06:43
 */
public interface NodeGraphListener extends EventListener {

    /**
     *
     * @param edge
     */
    void edgeAdded(MagicGraphEvent edge);

    /**
     *
     * @param edge
     */
    void edgeRemoved(MagicGraphEvent edge);
}
