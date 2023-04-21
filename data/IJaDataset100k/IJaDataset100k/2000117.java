package com.sun.mmedia;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Canvas;

public interface MMCreator {

    /**
     * This method creates an instance of MMItem with the given
     * label, and size of the item.
     */
    Item createMMItem(String label, int width, int height);

    /**
     * This method connects a canvas with a listener. This enables
     * the canvas to notify the listener of any changes to its
     * visibility.
     */
    void setMMCanvas(Canvas c, MMItemListener list);
}
