package net.sf.graphiti.ui.figure;

import net.sf.graphiti.model.Vertex;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * This class implements {@link CellEditorLocator} to edit a {@link Vertex}'s
 * id. It is based on Daniel Lee's implementation for the flow example.
 * 
 * @author Daniel Lee
 * @author Matthieu Wipliez
 */
public class VertexCellEditorLocator implements CellEditorLocator {

    private VertexFigure vertexFigure;

    /**
	 * Creates a new VertexCellEditorLocator for the given vertexFigure
	 * 
	 * @param figure
	 *            the figure
	 */
    public VertexCellEditorLocator(VertexFigure figure) {
        vertexFigure = figure;
    }

    /**
	 * @see CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
    public void relocate(CellEditor celleditor) {
        Text text = (Text) celleditor.getControl();
        Point pref;
        if (text.getText().isEmpty()) {
            pref = new Point(13, 13);
        } else {
            pref = text.computeSize(-1, -1);
        }
        Label label = vertexFigure.getLabelId();
        Rectangle labelBounds = label.getBounds().getCopy();
        label.translateToAbsolute(labelBounds);
        Rectangle figureBounds = vertexFigure.getBounds().getCopy();
        vertexFigure.translateToAbsolute(figureBounds);
        int start = (figureBounds.width - pref.x) / 2;
        text.setBounds(figureBounds.x + start, labelBounds.y, pref.x, pref.y);
    }
}
