package datastructures;

import java.io.Serializable;

public class DS_House implements Serializable {

    private static final long serialVersionUID = 2251293600035748254L;

    public static final int RECORD_SIZE = 24 + 4 + 4 + 1;

    private String name;

    private int id;

    private int x, y;

    private int distance;

    private boolean visited;

    private DS_GenericList<DS_Path> houses;

    private DS_Path shortest;

    /**
	 * Vertex constructor
	 * @param name of the house
	 * @param x position of the house
	 * @param y position of the house
	 */
    public DS_House(String name, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = -1;
        this.setName(name);
        this.distance = -1;
        this.visited = false;
        this.houses = new DS_GenericList<DS_Path>();
        this.shortest = null;
    }

    /**
     * Clear the content of this House
     */
    public void clear() {
        this.distance = -1;
        this.visited = false;
        this.shortest = null;
    }

    /**
	 * Determine if the current house object has been cleared or not
	 * @return true if it is, false otherwise
	 */
    public boolean isClear() {
        return this.distance == -1;
    }

    /**
	 * Get of all the paths that go out of this house
	 * @return list of edges
	 */
    public DS_GenericList<DS_Path> getPaths() {
        return this.houses;
    }

    /**
     * Add a connecting path
     * @param path object
     */
    public void addPath(DS_Path edge) {
        this.houses.add(edge);
    }

    /**
     * Set this house to the "visited" status
     * @param visited status
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Determine is this house has been visited
     * @return the visited status
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Set the minimum distance to this house
     * @param minimum distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Get the minimum distance to this house
     * @return minimum distance
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Get this houses' X coordinate on the map canvas
     * @return the integer X coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set this houses' X coordinate on the map canvas
     * @param x coordinate of the house
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set this houses' Y coordinate on the map canvas
     * @param y coordinate of the house
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get this houses' Y coordinate on the map canvas
     * @return the integer Y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the shortest connecting path to this house
     * @param v is the shortest path
     */
    public void setShortest(DS_Path v) {
        this.shortest = v;
    }

    /**
	 * Get the shortest distance to this house
	 * @return the shortest distance
	 */
    public DS_Path getShortest() {
        return this.shortest;
    }

    /**
	 * Get the name of the house
	 * @return name of the house
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Set the name of the house
	 * @param name of the house
	 */
    public void setName(String name) {
        this.name = name.substring(0, Math.min(name.length(), 24));
    }

    /**
	 * Get the ID of the house
	 * @return ID of the house
	 */
    public int getID() {
        return this.id;
    }

    /**
     * Set the ID of this house (this can only be set once!)
     * @param id to set the house to
     */
    public void setID(int id) {
        if (this.id < 0) this.id = id;
    }

    /**
	 * Over-ride the toString() method to return the name
	 */
    public String toString() {
        return this.name;
    }
}
