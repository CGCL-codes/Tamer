package test.worldofzuul;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A room describes the current environment.  Only one
 * room can be displayed at one time.
 *
 */
public class Room {

    private double width, height, depth;

    private double bgRed, bgGreen, bgBlue;

    private String textureNorth, textureSouth, textureEast, textureWest;

    private String description;

    private ArrayList<Block> blocks;

    private HashMap<String, Room> exits;

    /**
     * Constructor for objects of class Room
     */
    public Room(double w, double h, double d, String desc) {
        width = w;
        height = h;
        depth = d;
        description = desc;
        textureNorth = "textures/fence0.jpg";
        textureEast = "textures/fence0.jpg";
        textureSouth = "textures/fence0.jpg";
        textureWest = "textures/fence0.jpg";
        blocks = new ArrayList<Block>();
        exits = new HashMap<String, Room>();
    }

    /**
     * Add a block to this room.
     *
     * @param block The block object to be added to the room
     */
    public void addBlock(Block block) {
        blocks.add(block);
    }

    /**
     * Get the block
     *
     * @return The collection of blocks
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Get the description of the room
     *
     * @return The description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureNorth(String fileName) {
        textureNorth = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureEast(String fileName) {
        textureEast = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureSouth(String fileName) {
        textureSouth = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureWest(String fileName) {
        textureWest = fileName;
    }

    /**
     * Create an exit to a room
     *
     * @param direction the direction of the exit
     * @param room the room that this direction exits to
     */
    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    /**
     * Get the room a direction exits to.
     *
     * @param direction a string indicating a direction
     * @return the room that the direction exits to.  null if no exit
     * in that direction
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * Accessor for the room's dimension
     */
    public double getWidth() {
        return width;
    }

    /**
     * Accessor for room's dimension
     */
    public double getDepth() {
        return depth;
    }
}
