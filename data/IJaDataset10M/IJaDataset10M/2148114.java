package de.erdesignerng.visual.jgraph.tools;

import de.erdesignerng.visual.common.GenericModelEditor;
import de.erdesignerng.visual.jgraph.ERDesignerGraph;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Tool to add comments to the editor.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-13 16:48:59 $
 */
public class CommentTool extends BaseTool {

    public CommentTool(GenericModelEditor aEditor, ERDesignerGraph aGraph) {
        super(aEditor, aGraph);
    }

    @Override
    public boolean isForceMarqueeEvent(MouseEvent event) {
        return true;
    }

    @Override
    public boolean startCreateNew(MouseEvent e) {
        graph.commandNewComment(graph.fromScreen(new Point2D.Double(e.getX(), e.getY())));
        return true;
    }
}
