package core.graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Node modélise un carrefour sur la carte de la ville
 * 
 * @author Latour Quentin
 * 
 */
public class Node extends GraphElement {

    /** La liste des routes sortantes */
    protected List<Lane> outLanes;

    /** La liste des routes entrantes */
    protected List<Lane> inLanes;

    /** La position du carrefour sur la carte */
    protected int x = 0, y = 0;

    public Node(String name, int x, int y) {
        super(name);
        this.x = x;
        this.y = y;
        outLanes = new ArrayList<Lane>(5);
        inLanes = new ArrayList<Lane>(5);
    }

    /** @return {@link Node#inLanes} */
    public List<Lane> getInLanes() {
        return inLanes;
    }

    /** @return {@link Node#outLanes} */
    public List<Lane> getOutLanes() {
        return outLanes;
    }

    /**
	 * Ajoute une route à {@link Node#outLanes}
	 * 
	 * @param lane
	 *            La route à ajouter
	 */
    public void addOutLane(Lane lane) {
        if (!outLanes.contains(lane)) outLanes.add(lane);
    }

    /**
	 * Ajoute une route à {@link Node#inLanes}
	 * 
	 * @param lane
	 *            La route à ajouter
	 */
    public void addInLane(Lane lane) {
        if (!inLanes.contains(lane)) inLanes.add(lane);
    }

    /** @return {@link Node#x} */
    public int getX() {
        return x;
    }

    /**
	 * Change la position en X du carrefour
	 * 
	 * @param x
	 *            La nouvelle position
	 */
    public void setX(int x) {
        this.x = x;
    }

    /** @return {@link Node#y} */
    public int getY() {
        return y;
    }

    /**
	 * Change la position en Y du carrefour
	 * 
	 * @param y
	 *            La nouvelle position
	 */
    public void setY(int y) {
        this.y = y;
    }

    /**
	 * Change la position en X et Y du carrefour
	 * 
	 * @param point
	 *            La nouvelle position
	 */
    public void setPosition(Point point) {
        x = point.x;
        y = point.y;
    }

    /**
	 * Retire une route de {@link Node#outLanes}
	 * 
	 * @param lane
	 *            La route à retirer
	 */
    public void removeOutLane(Lane lane) {
        if (outLanes.contains(lane)) outLanes.remove(lane);
    }

    /**
	 * Retire une route de {@link Node#inLanes}
	 * 
	 * @param lane
	 *            La route à retirer
	 */
    public void removeInLane(Lane lane) {
        if (inLanes.contains(lane)) inLanes.remove(lane);
    }

    /**
	 * Test si le carrefour est vide
	 * 
	 * @return <b>true</b> s'il est vide, <b>false</b> sinon.
	 */
    public boolean isEmpty() {
        return characters.isEmpty();
    }
}
