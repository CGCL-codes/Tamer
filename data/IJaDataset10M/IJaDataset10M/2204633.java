package de.erdesignerng.visual.jgraph.cells;

import de.erdesignerng.model.Comment;
import de.erdesignerng.model.ModelItem;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * A comment cell.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-13 16:48:59 $
 */
public class CommentCell extends DefaultGraphCell implements ModelCellWithPosition<Comment> {

    public CommentCell(Comment aTable) {
        super(aTable);
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(20, 20, 40, 20));
        GraphConstants.setOpaque(getAttributes(), false);
        GraphConstants.setAutoSize(getAttributes(), true);
        GraphConstants.setResize(getAttributes(), true);
        GraphConstants.setEditable(getAttributes(), true);
    }

    @Override
    public void transferAttributesToProperties(Map aAttributes) {
        Comment theComment = (Comment) getUserObject();
        Rectangle2D theBounds = GraphConstants.getBounds(aAttributes);
        theComment.getProperties().setPointProperty(ModelItem.PROPERTY_LOCATION, (int) theBounds.getX(), (int) theBounds.getY());
    }

    @Override
    public void transferPropertiesToAttributes(Comment aObject) {
        Point2D thePoint = aObject.getProperties().getPoint2DProperty(ModelItem.PROPERTY_LOCATION);
        if (thePoint != null) {
            GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(thePoint.getX(), thePoint.getY(), 100, 100));
        }
    }

    @Override
    public void setBounds(Rectangle2D aBounds) {
        GraphConstants.setBounds(getAttributes(), aBounds);
    }
}
