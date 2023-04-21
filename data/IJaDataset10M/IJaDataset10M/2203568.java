package edu.uci.ics.jung.visualization.control;

import java.awt.event.MouseEvent;

/**
 * This interface allows users to register listeners to register to receive
 * vertex clicks.
 * 
 * @author danyelf
 */
public interface GraphMouseListener<V> {

    void graphClicked(V v, MouseEvent me);

    void graphPressed(V v, MouseEvent me);

    void graphReleased(V v, MouseEvent me);
}
