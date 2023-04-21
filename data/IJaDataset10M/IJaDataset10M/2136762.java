package org.nist.worldgen.t3d;

import java.io.*;
import java.util.*;

/**
 * When no native representation is available for a contained object, this object steps in to
 * maintain perfect fidelity with the original world.
 *
 * @author Stephen Carlson
 * @version 4.0
 */
public class DefaultComponent extends UTComponent {

    protected final List<UTObject> objects;

    protected final String type;

    /**
	 * Creates a new default object with the specified name and static name.
	 *
	 * @param type the object type to represent
	 * @param name the object's real name (aka ObjName)
	 * @param staticName the object's static, inherited name (aka Name)
	 */
    public DefaultComponent(final String type, final String name, final String staticName) {
        super(name, staticName);
        objects = new ArrayList<UTObject>(8);
        this.type = type;
    }

    public void addComponent(UTObject object) {
        objects.add(object);
    }

    public UTObject copyOf() {
        return copyCustom(new DefaultComponent(getType(), getName(), getStaticName()));
    }

    protected void custom(PrintWriter out, int indent, int utCompatMode) {
        for (UTObject sc : objects) sc.toUnrealText(out, indent + 1, 0);
    }

    public String getType() {
        return type;
    }
}
