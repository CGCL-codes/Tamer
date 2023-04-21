package diagram;

import diagram.figures.PolyLink;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.*;

/**
 * @class DefaultLinkEditor
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 * This editor can be used to edit both simple & poly link object. The values
 * mapped to those objects are treated as String values used for labels.
 */
public class DefaultLinkEditor extends DefaultFigureEditor {

    protected DefaultLabelRenderer renderer;

    /**
   * Create a new link editor
   */
    public DefaultLinkEditor() {
        this(new DefaultLabelRenderer());
    }

    /**
   * Create a new link editor
   */
    public DefaultLinkEditor(DefaultLabelRenderer renderer) {
        super(renderer);
        this.renderer = (DefaultLabelRenderer) getComponent();
    }

    /**
   * Get the component used to perform the editing, which in this case is just the 
   * modified JTextfield that is also used as a renderer
   */
    public Component getFigureEditorComponent(Diagram diagram, Figure figure, boolean isSelected) {
        return renderer.getRendererComponent(diagram, figure, false);
    }

    /**
   * This should return the bounds of the box bounding the label that should be edited
   */
    public Rectangle2D getDecoratedBounds(Diagram diagram, Figure figure, Rectangle2D rcBounds) {
        return renderer.getDecoratedBounds(diagram, figure, rcBounds);
    }
}
