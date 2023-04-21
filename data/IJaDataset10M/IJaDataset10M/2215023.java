package org.mobicents.javax.media.mscontrol.container;

/**
 *
 * @author kulikov
 */
public interface LinkListener {

    public void joined(Link link);

    public void unjoined(Link link);
}
