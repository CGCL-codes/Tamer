package net.taylor.worklist.entity.editor;

import net.taylor.seam.AbstractFinder;
import net.taylor.seam.PieChart;
import net.taylor.worklist.entity.Comment;

/**
 * The local interface for a Finder user interface.
 *
 * @author jgilbert
 * @version $Id: CommentFinder.java,v 1.4 2008/03/13 17:55:50 jgilbert01 Exp $
 * @generated
 */
public interface CommentFinder extends AbstractFinder<Comment> {

    /** @generated */
    PieChart getTaskPieChart();
}
