package org.freeworld.prilib.impl.display;

import java.awt.Color;

/**
 * Allows a table to be colored. This is a UI / backend hybrid class which
 * allows for very specialized control over the presentation of the GUI given
 * specific backend changes.
 * 
 * @author dchemko
 */
public interface TableColorable extends TableColored {

    public void setBackground(int row, int column, Color color);

    public void setForeground(int row, int column, Color color);

    public void setSelectionBackground(int row, int column, Color color);

    public void setSelectionForeground(int row, int column, Color color);
}
