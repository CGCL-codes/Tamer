package org.nist.worldgen.t3d;

import java.io.PrintWriter;

/**
 * Represents a static mesh's specifying component.
 *
 * @author Stephen Carlson (NIST)
 * @version 4.0
 */
public class StaticMeshComponent extends PrimitiveComponent {

    protected UTReference staticMesh;

    /**
	 * Creates a new static mesh component.
	 *
	 * @param name the component name
	 * @param staticName the object's inherited name
	 */
    public StaticMeshComponent(final String name, final String staticName) {
        super(name, staticName);
        staticMesh = null;
    }

    public void additionalReferences(ReferenceList list) {
        super.additionalReferences(list);
        list.addReference(staticMesh);
    }

    public UTObject copyOf() {
        final StaticMeshComponent smc = new StaticMeshComponent(getName(), getStaticName());
        smc.setStaticMesh(getStaticMesh());
        return copyCustom(smc);
    }

    /**
	 * Output any custom attributes of this actor.
	 *
	 * @param out the print writer where the text will be sent
	 * @param indent how many levels to indent
	 * @param utCompatMode the compatibility mode used in the output
	 */
    protected void custom(PrintWriter out, int indent, int utCompatMode) {
        UTUtils.putAttribute(out, indent, "StaticMesh", UTUtils.nullAsNone(getStaticMesh()));
    }

    protected boolean customPutAttribute(String key, String value) {
        boolean accept = super.customPutAttribute(key, value);
        if (!accept && key.equalsIgnoreCase("StaticMesh")) {
            setStaticMesh(UTUtils.parseReference(value));
            accept = true;
        }
        return accept;
    }

    /**
	 * Gets the component's static mesh.
	 *
	 * @return a reference to the component's static mesh
	 */
    public UTReference getStaticMesh() {
        return staticMesh;
    }

    /**
	 * Changes the static mesh used.
	 *
	 * @param staticMesh a reference to the new static mesh
	 */
    public void setStaticMesh(final UTReference staticMesh) {
        this.staticMesh = staticMesh;
    }

    public String toString() {
        return toString("name", getName(), "parent", getParent().getName(), "mesh", getStaticMesh());
    }
}
