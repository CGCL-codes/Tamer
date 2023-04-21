package net.sourceforge.huntforgold.model.path;

import net.sourceforge.huntforgold.model.Position;

/**
 * Models a node, i.e. a destination point in travel path
 */
public class Node {

    /**
   * the position of this node in the world
   */
    private Position position;

    /**
   * the identifying name of this node 
   */
    private String name;

    /**
   * Construct a node with the given characteristics
   * @param name the name of the node
   * @param position the position this node is at
   */
    public Node(String name, Position position) {
        setName(name);
        setPosition(position);
    }

    /**
   * Set the identifying name of the node
   * @param name the name to give the node
   */
    private void setName(String name) {
        this.name = name;
    }

    /**
   * Get the name of the node
   * @return the name of the node
   */
    public String getName() {
        return name;
    }

    /**
   * set the position of the node
   * @param position the position this node is at
   */
    private void setPosition(Position position) {
        this.position = position;
    }

    /**
   * get the position of the node
   * @return the position of the node
   */
    public Position getPosition() {
        return position;
    }
}
