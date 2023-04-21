package org.remus.infomngmnt.image.gef;

import java.awt.Point;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.util.InformationUtil;
import org.remus.infomngmnt.image.ImagePlugin;
import org.remus.infomngmnt.image.comments.ShapableInfoDelegate;

/**
 * A command to add a Shape to a ShapeDiagram. The command can be undone or
 * redone.
 * 
 * @author Elias Volanakis
 */
public class CommentCreateCommand extends Command {

    /** The new shape. */
    private final ShapableInfoDelegate newShape;

    /** ShapeDiagram to add to. */
    private final InformationUnit parent;

    /** The bounds of the new Shape. */
    private final Rectangle bounds;

    private final InformationUnit linkInfo;

    /**
	 * Create a command that will add a new Shape to a ShapesDiagram.
	 * 
	 * @param newShape
	 *            the new Shape that is to be added
	 * @param parent
	 *            the ShapesDiagram that will hold the new element
	 * @param bounds
	 *            the bounds of the new shape; the size can be (-1, -1) if not
	 *            known
	 * @throws IllegalArgumentException
	 *             if any parameter is null, or the request does not provide a
	 *             new Shape instance
	 */
    public CommentCreateCommand(final ShapableInfoDelegate newShape, final InformationUnit parent, final Rectangle bounds) {
        this.newShape = newShape;
        linkInfo = InformationUtil.getChildByType(parent, ImagePlugin.NODE_NAME_LINKS);
        this.parent = parent;
        this.bounds = bounds;
        setLabel("shape creation");
    }

    /**
	 * Can execute if all the necessary information has been provided.
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
    @Override
    public boolean canExecute() {
        return newShape != null && parent != null && bounds != null;
    }

    @Override
    public void execute() {
        newShape.setLocation(new Point(bounds.getLocation().x, bounds.getLocation().y));
        Dimension size = bounds.getSize();
        if (size.width > 0 && size.height > 0) {
            newShape.setSize(new java.awt.Dimension(size.width, size.height));
        }
        redo();
    }

    @Override
    public void redo() {
        linkInfo.getChildValues().add(newShape.getOrigInfoObject());
    }

    @Override
    public void undo() {
        linkInfo.getChildValues().remove(newShape.getOrigInfoObject());
    }
}
