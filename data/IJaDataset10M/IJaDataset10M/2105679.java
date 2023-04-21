package org.apache.batik.swing.svg;

/**
 * This interface represents a listener to the LinkActivationEvent events.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: LinkActivationListener.java,v 1.1 2005/11/21 09:51:34 dev Exp $
 */
public interface LinkActivationListener {

    /**
     * Called when a link was activated.
     */
    void linkActivated(LinkActivationEvent e);
}
