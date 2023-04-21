package com.guigarage.jgrid.renderer;

import java.awt.Component;
import com.guigarage.jgrid.JGrid;

/**
 * Works like the <code>ListCellRenderer</code>
 * Identifies components that can be used to paint
 * the cells in a JGrid
 * 
 * @author hendrikebbers
 * @since 0.1
 * @version 0.1
 * @see ListCellRenderer
 * @see JGrid
 */
public interface GridCellRenderer {

    /**
	 * Returns a component for rendering / painting the cell with the given index & properties
	 * @param grid The JGrid where the renderer is painting
	 * @param value The value that will be rendered by thy returning Component
	 * @param index The cell index in the JGrid 
	 * @param isSelected true if the cell is selected
	 * @param cellHasFocus true if the cell has focus
	 * @return A Component for paint the cell in the JGrid
	 */
    public Component getGridCellRendererComponent(JGrid grid, Object value, int index, boolean isSelected, boolean cellHasFocus);
}
