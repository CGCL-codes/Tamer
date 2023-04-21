package mimosa.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * A port reference describes a path of ports from a given entity.
 * Depending on the context, it may designate the port itself in a hierarchy, or the entities
 * accessible by the given path.
 *
 * @author Jean-Pierre Muller
 */
public class PortReference {

    private static final Pattern port = Pattern.compile("\\.");

    /** The entity from which the port path gets its meaning. */
    private Entity entity;

    /** The path as an ordered collection of ports */
    private ArrayList<Port> ports = new ArrayList<Port>();

    /**
	 * Creates a port reference given an entity and a name.
	 * @param entity The entity.
	 * @param name The name as a sequence of port names.
	 */
    public PortReference(Entity entity, String name) {
        this.entity = entity;
        parse(name);
    }

    /**
	 * Creates a port reference given an entity and a port.
	 * @param entity The entity.
	 * @param port The port of the entity.
	 */
    public PortReference(Entity entity, Port port) {
        this.entity = entity;
        ports.add(port);
    }

    /**
	 * Creates a port reference given an entity and a port.
	 * @param entity The entity.
	 * @param ports The ports path.
	 */
    public PortReference(Entity entity, Collection<Port> ports) {
        this.entity = entity;
        this.ports.addAll(ports);
    }

    /**
	 * Parses the port name into an ordered collection of ports.
	 * @param name The ports path name.
	 */
    private void parse(String name) {
        String[] portNames = port.split(name);
        for (int i = 0; i < portNames.length; i++) {
            if (portNames[i].equals("self")) ports.add(Port.SELF); else ports.add(new Port(portNames[i]));
        }
    }

    /**
	 * @return Returns the number of ports in the path.
	 */
    public int getNbPorts() {
        return ports.size();
    }

    /**
	 * @param i The index.
	 * @return Returns the i-th port or null.
	 */
    public Port getPort(int i) {
        if (i < ports.size()) return ports.get(i); else return null;
    }

    /**
	 * @return returns the entity.
	 */
    public Entity getEntity() {
        return entity;
    }

    /**
	 * @return Returns the number of ports.
	 */
    public int size() {
        return ports.size();
    }

    public String toString() {
        StringBuffer name = new StringBuffer();
        for (Port port : ports) name.append(port + ".");
        return "PortReference(" + entity + "," + name + ")";
    }

    /**
	 * Used for testing the functionality of the port reference.
	 * @param args
	 */
    public static void main(String[] args) {
        PortReference portName = new PortReference(null, "cell(2,3).truc.bidule(5)");
        for (int i = 0; i < portName.getNbPorts(); i++) {
            Port descr = portName.getPort(i);
            System.out.println("Name: " + descr.toString());
        }
    }
}
