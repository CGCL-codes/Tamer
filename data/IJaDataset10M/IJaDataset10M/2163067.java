package jiso.map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Point;
import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;

public interface Map extends MouseListener, MouseMotionListener {

    public enum Dimension {

        X, Y, Z
    }

    ;

    public enum RotationDirection {

        CW, CCW
    }

    ;

    /**
     * Get the dimensions of the field
     * 
     * @param d which dimension to return
     * @return The designated dimension of this field object
     */
    public int getDimension(Dimension d);

    /**
     * Method to adjust the size of the Map you are editing. 
     * It does NOT delete all of the data.  
     * However, it will delete tiles that are outside the new bounds.
     * 
     * @param d the dimension to set
     * @param value the value to be set to
     */
    public void setMapDimension(Dimension d, int v);

    /**
     * Manually set the location to draw the map
     * (Map draw at the top-most tile's tip when at a height of 0)
     * @param x New x-location for drawing
     * @param y New y-location for drawing
     */
    public void setDrawLocation(int x, int y);

    /**
     * Draw the map to the screen
     * @param g The Graphics object with which to draw the map
     */
    public void draw(Graphics g);

    public MapTile getTile(int[] coordinate);

    /**
     * Select the tile that is under the mouse click
     * 
     * @param x X position of the mouse click
     * @param y Y position of the mouse click
     */
    public void findAndSelectTile(int x, int y);

    /**
     * Set the tile at a certain position to the new tile    
     * @param x Tiles x-location on the map
     * @param y Tiles y-location on the map
     * @param data The tile that replaces the old tile
     */
    public void setTileData(int[] coordinate, MapTile data);

    /**
     * Set the selected tile on the Map
     * @param x The value 1 -> MapSettings.field_width that represents the new selected tile
     * @param y The value 1 -> MapSettings.field_height that represents the new selected tile
     */
    public void setSelectedTile(int[] coordinate);

    /**
     * Get the tile that is currently selected
     * 
     * @return The MapTile which is currently selected
     */
    public MapTile getSelectedTile();

    /**Rotate the map 90 degrees CW or CCW
     * 
     * @param direction which direction to rotate
     */
    public void rotate(RotationDirection direction);

    /**produce a string representation of the map
     *
     * @param v
     * @return
     */
    public String toString();

    /**
     * Save the Map to a file.
     * 
     * @param map the file to save to
     */
    public void writeToFile();

    /**
     * Return a map generated from the given file
     * 
     * @param map The .imf file that has map data in it.
     */
    public void loadMap();

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void mouseDragged(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void mouseClicked(MouseEvent e);

    public void mouseEntered(MouseEvent e);

    public void mouseExited(MouseEvent e);
}
