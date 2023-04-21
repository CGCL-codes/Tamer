package de.ibis.permoto.gui.modelling.ui;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;

/**
 * <p>
 * Title: PerMoToEdge connection structure
 * </p>
 * <p>
 * Description: This class is used to connect two elements into PerMoToGraph. It
 * is designed to store keys of source and target stations that are used when
 * deleting or copying a connection
 * </p>
 * @author Christian Markl
 * @author Oliver H�hn
 */
public class PerMoToEdge extends DefaultEdge implements Edge {

    /** The serialVersionUID. */
    private static final long serialVersionUID = 5247727398545752216L;

    /** The key of the source. */
    private String sourceKey;

    /** The key of the target. */
    private String targetKey;

    /**
	 * Constructor.
	 */
    public PerMoToEdge() {
        super("");
    }

    /**
	 * Constructor.
	 * @param o The object
	 */
    public PerMoToEdge(final Object o) {
        super(o);
    }

    /**
	 * Creates a new PerMoToEdge connecting source to target.
	 * @param sK key of source station
	 * @param tK key of target station
	 */
    public PerMoToEdge(final String sK, final String tK, Object userObject) {
        this(userObject);
        this.sourceKey = sK;
        this.targetKey = tK;
    }

    /**
	 * Gets source station search key.
	 * @return source key
	 */
    public final String getSourceKey() {
        return this.sourceKey;
    }

    /**
	 * Gets target station search key.
	 * @return target key
	 */
    public final String getTargetKey() {
        return this.targetKey;
    }
}
