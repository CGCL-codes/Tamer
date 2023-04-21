package net.sf.orcc.network;

import net.sf.orcc.ir.Port;

/**
 * This class defines a vertex in an XDF network. A vertex is either an input
 * port, an output port, or an instance.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class Vertex {

    /**
	 * This class defines a vertex type.
	 * 
	 * @author Matthieu Wipliez
	 * 
	 */
    private enum Type {

        INPUT_PORT, INSTANCE, OUTPUT_PORT
    }

    ;

    /**
	 * the contents of this vertex. Can only be an Instance or a Port.
	 */
    private Object contents;

    /**
	 * the type of this vertex. One of INSTANCE, INPUT or OUTPUT.
	 */
    private Type type;

    /**
	 * Creates a new vertex whose contents will be the given instance.
	 * 
	 * @param instance
	 *            an instance
	 */
    public Vertex(Instance instance) {
        type = Type.INSTANCE;
        contents = instance;
    }

    /**
	 * Creates a new vertex whose contents will be the given port.
	 * 
	 * @param port
	 *            a port
	 */
    public Vertex(String kind, Port port) {
        if (kind.equals("Input")) {
            type = Type.INPUT_PORT;
        } else {
            type = Type.OUTPUT_PORT;
        }
        contents = port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex vertex = (Vertex) obj;
            return contents == vertex.contents;
        } else {
            return false;
        }
    }

    /**
	 * Returns the instance contained in this vertex.
	 * 
	 * @return the instance contained in this vertex.
	 */
    public Instance getInstance() {
        if (isInstance()) {
            return (Instance) contents;
        } else {
            return null;
        }
    }

    /**
	 * Returns the port contained in this vertex.
	 * 
	 * @return the port contained in this vertex.
	 */
    public Port getPort() {
        if (isPort()) {
            return (Port) contents;
        } else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }

    /**
	 * Returns <code>true</code> if this vertex contains an instance, and
	 * <code>false</code> otherwise. This method must be called to ensure a
	 * vertex is an instance before calling {@link #getInstance()}.
	 * 
	 * @return <code>true</code> if this vertex contains an instance, and
	 *         <code>false</code> otherwise
	 */
    public boolean isInstance() {
        return (type == Type.INSTANCE);
    }

    /**
	 * Returns <code>true</code> if this vertex contains a port, and
	 * <code>false</code> otherwise. This method must be called to ensure a
	 * vertex is a port before calling {@link #getPort()}.
	 * 
	 * @return <code>true</code> if this vertex contains a port, and
	 *         <code>false</code> otherwise
	 */
    public boolean isPort() {
        return (type != Type.INSTANCE);
    }

    @Override
    public String toString() {
        return type + " " + contents;
    }
}
