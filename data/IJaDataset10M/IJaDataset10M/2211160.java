package uk.ac.ox.oucs.oxpoints.gaboto.beans;

import uk.ac.ox.oucs.oxpoints.gaboto.beans.SpaceConfiguration;

/**
 * Gaboto generated bean.
 * @see net.sf.gaboto.generation.GabotoGenerator
 */
public class Cabaret extends SpaceConfiguration {

    @Override
    public String getType() {
        return "http://purl.org/openorg/space-configuration/Cabaret";
    }

    public String toString() {
        return this.capacity + ", " + this.comment;
    }
}
