package org.apache.batik.util.gui.resource;

import javax.swing.JComponent;

/**
 * This interface must be implemented by actions which need
 * to have an access to their associated component(s)
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: JComponentModifier.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public interface JComponentModifier {

    /**
     * Gives a reference to a component to this object
     * @param comp the component associed with this object
     */
    void addJComponent(JComponent comp);
}
