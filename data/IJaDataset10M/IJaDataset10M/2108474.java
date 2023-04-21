package net.sf.myway.edit.ui.editors.commands;

import net.sf.myway.base.db.Position;
import net.sf.myway.edit.EditPlugin;
import net.sf.myway.edit.ui.editors.CoordinateSystem;
import net.sf.myway.map.da.entities.MapNode;
import net.sf.myway.map.da.entities.MapNodeRef;
import net.sf.myway.map.da.entities.MapObject;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class AddNodeCommand extends Command {

    private MapNode _node;

    private Point _location;

    private MapObject _target;

    private CoordinateSystem _coordSys;

    /**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
    @Override
    public void execute() {
        final Position position = _coordSys.toPosition(_location);
        MapNode n = EditPlugin.getBL().findNode(position);
        if (n == null) {
            n = _node;
            n.setPosition(position);
        }
        final MapNodeRef ref = new MapNodeRef(n);
        _target.addNode(ref);
    }

    /**
	 * @return the coordSys
	 */
    public CoordinateSystem getCoordSys() {
        return _coordSys;
    }

    /**
	 * @return the location
	 */
    public Point getLocation() {
        return _location;
    }

    /**
	 * @return the node
	 */
    public MapNode getNode() {
        return _node;
    }

    /**
	 * @return the target
	 */
    public MapObject getTarget() {
        return _target;
    }

    /**
	 * @param coordSys
	 *            the coordSys to set
	 */
    public void setCoordSys(final CoordinateSystem coordSys) {
        _coordSys = coordSys;
    }

    /**
	 * @param location
	 *            the location to set
	 */
    public void setLocation(final Point location) {
        _location = location;
    }

    /**
	 * @param node
	 *            the node to set
	 */
    public void setNode(final MapNode node) {
        _node = node;
    }

    /**
	 * @param target
	 *            the target to set
	 */
    public void setTarget(final MapObject target) {
        _target = target;
    }
}
