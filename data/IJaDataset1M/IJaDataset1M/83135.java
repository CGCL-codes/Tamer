package db.nodedb;

import java.io.IOException;
import db.HighLevelInputStream;
import db.HighLevelOutputStream;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 *
 * The representation of a node for the node database.
 */
public class DbNode {

    /**
	 * The number of bytes used to store a node.
	 */
    public static final int NODESIZE = 24;

    private long id;

    private double lon;

    private double lat;

    /**
	 * Create a node
	 * 
	 * @param id the if
	 * @param lon the longitude
	 * @param lat the latitude
	 */
    public DbNode(long id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    /**
	 * Write this node to an output stream
	 * 
	 * @param hlos the output stream to write to
	 * @throws IOException on writing failure.
	 */
    public void write(HighLevelOutputStream hlos) throws IOException {
        hlos.writeLong(getId());
        hlos.writeDouble(getLat());
        hlos.writeDouble(getLon());
    }

    /**
	 * Read this node from an input stream
	 * 
	 * @param hlis the stream to read from
	 * @return the read node
	 * @throws IOException on reading failure.
	 */
    public static DbNode read(HighLevelInputStream hlis) throws IOException {
        long id = hlis.readLong();
        double lat = hlis.readDouble();
        double lon = hlis.readDouble();
        return new DbNode(id, lon, lat);
    }

    /**
	 * @return this node's id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * @return this node's longitude.
	 */
    public double getLon() {
        return lon;
    }

    /**
	 * @return this node's latitude.
	 */
    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return id + ": " + lon + "," + lat;
    }
}
