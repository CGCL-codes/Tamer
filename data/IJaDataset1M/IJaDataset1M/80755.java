package org.nist.worldgen.t3d;

import java.io.*;

/**
 * Represents an AudioComponent used to output sounds from a dying victim...
 *
 * @author Stephen Carlson (NIST)
 * @version 4.0
 */
public class AudioComponent extends UTComponent {

    /**
	 * Creates a new audio component.
	 *
	 * @param name the object name
	 * @param staticName the name which matches the template for the real object
	 */
    public AudioComponent(final String name, final String staticName) {
        super(name, staticName);
    }

    public UTObject copyOf() {
        return copyCustom(new AudioComponent(getName(), getStaticName()));
    }

    protected void custom(PrintWriter out, int indent, int utCompatMode) {
    }
}
