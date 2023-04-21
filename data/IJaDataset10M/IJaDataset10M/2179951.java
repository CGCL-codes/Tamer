package de.hpi.eworld.model.db.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.IndexColumn;
import de.hpi.eworld.observer.NotificationType;
import de.hpi.eworld.observer.ObserverNotification;

/**
 * @author
 */
@Entity
@Table(name = "ways")
@SequenceGenerator(name = "SEQ_WAY", sequenceName = "ways_id_seq")
public class WayModel extends ModelElement implements Serializable {

    /**
	 * An ID for Database purposes.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WAY")
    public int id;

    /**
	 * 
	 */
    private static final long serialVersionUID = 6274902182356835551L;

    /**
	 * The description for this way.
	 */
    private String description;

    /**
	 * A list of all forward edges.
	 */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentWay")
    @IndexColumn(name = "indexinway", base = 0)
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.PERSIST })
    private List<EdgeModel> forwardEdges;

    /**
	 * A list of all backward edges.
	 */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentWay")
    @IndexColumn(name = "indexinway", base = 0)
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.PERSIST })
    private List<EdgeModel> backwardEdges;

    /**
	 * A reference of the first forward edge.
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firstforwardedge")
    private EdgeModel firstForwardEdge = null;

    /**
	 * A reference of the first backward edge.
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firstbackwardedge")
    private EdgeModel firstBackwardEdge = null;

    /**
	 * A reference of the last forward edge.
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lastforwardedge")
    private EdgeModel lastForwardEdge = null;

    /**
	 * A reference of the last backward edge.
	 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lastbackwardedge")
    private EdgeModel lastBackwardEdge = null;

    /**
	 * Creates a way with the given name/description.
	 * @param description The textual representation of the way, e.g. a street name.
	 * @author Christian Holz
	 */
    public WayModel(String description) {
        this.description = description;
        forwardEdges = new ArrayList<EdgeModel>();
        backwardEdges = new ArrayList<EdgeModel>();
    }

    /**
	 * Restores the used-by-references of all nodes affected by this way.
	 */
    public void restore() {
        for (EdgeModel e : forwardEdges) {
            e.getFromNode().addUsedBy(e);
            e.getToNode().addUsedBy(e);
        }
        for (EdgeModel e : backwardEdges) {
            e.getFromNode().addUsedBy(e);
            e.getToNode().addUsedBy(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WayModel) || obj == null) return false;
        if (this == obj) return true;
        WayModel way = (WayModel) obj;
        boolean result = true;
        result &= (lastForwardEdge != null ? lastForwardEdge.equals(way.getLastForwardEdge()) : true);
        result &= (lastBackwardEdge != null ? lastBackwardEdge.equals(way.getLastBackwardEdge()) : true);
        result &= (firstForwardEdge != null ? firstForwardEdge.equals(way.getFirstForwardEdge()) : true);
        result &= (firstBackwardEdge != null ? firstBackwardEdge.equals(way.getFirstBackwardEdge()) : true);
        result &= forwardEdges.size() == way.getForwardEdges().size();
        result &= backwardEdges.size() == way.getBackwardEdges().size();
        result &= description.equals(way.getDescription());
        return result;
    }

    /**
	 * This default constructor is required for hibernate 
	 */
    @SuppressWarnings("unused")
    private WayModel() {
    }

    /**
	 * Returns the ways description.
	 * @return The textual representation of the way, e.g. a street name.
	 * @author Christian Holz
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description for this way.
	 * @param description The textual description of the way, e.g. street name
	 * @author Nico Naumann
	 */
    public void setDescription(String description) {
        boolean changed = ((description != null) && (!description.equals(this.description)));
        this.description = description;
        if (changed) {
            this.setChanged();
            this.notifyObservers(new ObserverNotification(NotificationType.elementChanged, this));
            this.clearChanged();
        }
    }

    /**
	 * Adds an edge to the way collection.
	 * @param edge The edge to be added.
	 * @author Christian Holz
	 */
    public void addForwardEdge(EdgeModel edge) {
        forwardEdges.add(edge);
        if (firstForwardEdge == null) {
            firstForwardEdge = edge;
        }
        EdgeModel previousEdge = lastForwardEdge;
        lastForwardEdge = edge;
        if ((firstForwardEdge != lastForwardEdge) && (previousEdge != null)) {
            previousEdge.setNextEdge(lastForwardEdge);
            lastForwardEdge.setPreviousEdge(previousEdge);
        }
        this.setChanged();
        this.notifyObservers(new ObserverNotification(NotificationType.elementChanged, this));
        this.clearChanged();
    }

    /**
	 * Point of interest integration
	 * split an edge in two edges and integrate the poi
	 * 
	 * @param old_edge Old edge which will be splitted
	 * @param first_Edge New first edge
	 * @param second_Edge New second edge
	 * @param forward Either forward or backward
	 * 
	 * @author Franz Goerke, Mike Nagora
	 */
    public void splitEdge(EdgeModel old_edge, EdgeModel first_Edge, EdgeModel second_Edge, boolean forward) {
        if (forward) {
            if (old_edge == firstForwardEdge) firstForwardEdge = first_Edge;
            if (old_edge == lastForwardEdge) lastForwardEdge = second_Edge;
        } else {
            if (old_edge == firstBackwardEdge) firstBackwardEdge = first_Edge;
            if (old_edge == lastBackwardEdge) lastBackwardEdge = second_Edge;
        }
        EdgeModel previousEdge = old_edge.getPreviousEdge();
        if (previousEdge != null) {
            previousEdge.setNextEdge(first_Edge);
            first_Edge.setPreviousEdge(previousEdge);
        }
        if (old_edge.getNextEdge() != null) {
            second_Edge.setNextEdge(old_edge.getNextEdge());
            old_edge.getNextEdge().setPreviousEdge(second_Edge);
        }
        first_Edge.setNextEdge(second_Edge);
        second_Edge.setPreviousEdge(first_Edge);
        if (forward) {
            forwardEdges.remove(old_edge);
            forwardEdges.add(first_Edge);
            forwardEdges.add(second_Edge);
        } else {
            backwardEdges.remove(old_edge);
            backwardEdges.add(first_Edge);
            backwardEdges.add(second_Edge);
        }
        this.setChanged();
        this.notifyObservers(new ObserverNotification(NotificationType.elementChanged, this));
        this.clearChanged();
    }

    /**
	 * Adds an edge to the way collection.
	 * @param edge The edge to be added.
	 * @author Christian Holz
	 */
    public void addBackwardEdge(EdgeModel edge) {
        backwardEdges.add(edge);
        if (lastBackwardEdge == null) {
            lastBackwardEdge = edge;
        }
        EdgeModel previousEdge = firstBackwardEdge;
        firstBackwardEdge = edge;
        if ((firstBackwardEdge != lastBackwardEdge) && (previousEdge != null)) {
            previousEdge.setPreviousEdge(firstBackwardEdge);
            firstBackwardEdge.setNextEdge(previousEdge);
        }
        this.setChanged();
        this.notifyObservers(new ObserverNotification(NotificationType.elementChanged, this));
        this.clearChanged();
    }

    /**
	 * Returns All edges belonging to the current way in forward direction.
	 * @return All edges belonging to the current way in forward direction.
	 * @author Christian Holz
	 */
    public List<EdgeModel> getForwardEdges() {
        return forwardEdges;
    }

    /**
	 * Returns All edges belonging to the current way in backward direction.
	 * @return All edges belonging to the current way in backward direction.
	 */
    public List<EdgeModel> getBackwardEdges() {
        return backwardEdges;
    }

    /**
	 * Returns the first edge of the way in forward direction.
	 * @return The first edge of the way in forward direction.
	 * @author Christian Holz
	 */
    public EdgeModel getFirstForwardEdge() {
        return firstForwardEdge;
    }

    /**
	 * Returns the last edge of the way in forward direction.
	 * @return The last edge of the way in forward direction.
	 * @author Christian Holz
	 */
    public EdgeModel getLastForwardEdge() {
        return lastForwardEdge;
    }

    /**
	 * Returns the first edge of the way in backward direction.
	 * @return The first edge of the way in backward direction.
	 * @author Frank Huxol
	 */
    public EdgeModel getFirstBackwardEdge() {
        return firstBackwardEdge;
    }

    /**
	 * Returns the last edge of the way in backward direction.
	 * @return The last edge of the way in backward direction.
	 * @author Frank Huxol
	 */
    public EdgeModel getLastBackwardEdge() {
        return lastBackwardEdge;
    }
}
