package net.sf.vorg.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import net.sf.gef.core.model.AbstractEndPoint;
import net.sf.gef.core.model.Route;
import net.sf.gef.core.model.RouteEndPoint;

public class GNodePolicy extends GraphicalNodeEditPolicy {

    public GNodePolicy() {
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
        cmd.setTarget((AbstractEndPoint) getHost().getModel());
        return cmd;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        AbstractEndPoint source = (AbstractEndPoint) getHost().getModel();
        int style = ((Integer) request.getNewObjectType()).intValue();
        ConnectionCreateCommand cmd = new ConnectionCreateCommand(source, style);
        request.setStartCommand(cmd);
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        Route conn = (Route) request.getConnectionEditPart().getModel();
        AbstractEndPoint newSource = (AbstractEndPoint) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewSource(newSource);
        return cmd;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        Route conn = (Route) request.getConnectionEditPart().getModel();
        AbstractEndPoint newTarget = (AbstractEndPoint) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        cmd.setNewTarget(newTarget);
        return cmd;
    }
}

/**
 * A command to create a connection between two shapes. The command can be undone or redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy. To use this command properly,
 * following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getConnectionCreateCommand(...)</tt> method, to create a new instance of this class
 * and put it into the CreateConnectionRequest.</li>
 * <li>Override the <tt>getConnectionCompleteCommand(...)</tt> method, to obtain the Command from the
 * ConnectionRequest, call setTarget(...) to set the target endpoint of the connection and return this command
 * instance.</li>
 * </ol>
 * 
 * @author Elias Volanakis
 */
class ConnectionCreateCommand extends Command {

    /** The connection instance. */
    private Route connection;

    /** The desired line style for the connection (dashed or solid). */
    @SuppressWarnings("unused")
    private final int lineStyle;

    /** Start endpoint for the connection. */
    private final AbstractEndPoint source;

    /** Target endpoint for the connection. */
    private AbstractEndPoint target;

    /**
	 * Instantiate a command that can create a connection between two shapes.
	 * 
	 * @param source
	 *          the source endpoint (a non-null Shape instance)
	 * @param lineStyle
	 *          the desired line style. See Connection#setLineStyle(int) for details
	 */
    public ConnectionCreateCommand(AbstractEndPoint source, int lineStyle) {
        if (source == null) throw new IllegalArgumentException();
        setLabel("connection creation");
        this.source = source;
        this.lineStyle = lineStyle;
    }

    @Override
    public boolean canExecute() {
        if (source.equals(target)) return false;
        for (Object element : source.getSourceConnections()) {
            Route conn = (Route) element;
            if (conn.getTarget().equals(target)) return false;
        }
        return true;
    }

    @Override
    public void execute() {
        connection = new Route(source, target);
    }

    @Override
    public void redo() {
        connection.reconnect();
    }

    /**
	 * Set the target endpoint for the connection.
	 * 
	 * @param target
	 *          that target endpoint (a non-null Shape instance)
	 * @throws IllegalArgumentException
	 *           if target is null
	 */
    public void setTarget(AbstractEndPoint target) {
        if (target == null) throw new IllegalArgumentException();
        this.target = target;
    }

    @Override
    public void undo() {
        connection.disconnect();
    }
}

/**
 * A command to reconnect a connection to a different start point or end point. The command can be undone or
 * redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy. To use this command propertly,
 * following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getReconnectSourceCommand(...)</tt> method. Here you need to obtain the Connection
 * model element from the ReconnectRequest, create a new ConnectionReconnectCommand, set the new connection
 * <i>source</i> by calling the <tt>setNewSource(Shape)</tt> method and return the command instance.
 * <li>Override the <tt>getReconnectTargetCommand(...)</tt> method.</li>
 * Here again you need to obtain the Connection model element from the ReconnectRequest, create a new
 * ConnectionReconnectCommand, set the new connection <i>target</i> by calling the
 * <tt>setNewTarget(Shape)</tt> method and return the command instance.</li>
 * </ol>
 * 
 * @author Elias Volanakis
 */
class ConnectionReconnectCommand extends Command {

    /** The connection instance to reconnect. */
    private final Route connection;

    /** The new source endpoint. */
    private RouteEndPoint newSource;

    /** The new target endpoint. */
    private RouteEndPoint newTarget;

    /** The original source endpoint. */
    private final RouteEndPoint oldSource;

    /** The original target endpoint. */
    private final RouteEndPoint oldTarget;

    /**
	 * Instantiate a command that can reconnect a Connection instance to a different source or target endpoint.
	 * 
	 * @param conn
	 *          the connection instance to reconnect (non-null)
	 * @throws IllegalArgumentException
	 *           if conn is null
	 */
    public ConnectionReconnectCommand(Route conn) {
        if (conn == null) throw new IllegalArgumentException();
        connection = conn;
        oldSource = conn.getSource();
        oldTarget = conn.getTarget();
    }

    @Override
    public boolean canExecute() {
        if (newSource != null) return checkSourceReconnection(); else if (newTarget != null) return checkTargetReconnection();
        return false;
    }

    /**
	 * Return true, if reconnecting the connection-instance to newSource is allowed.
	 */
    private boolean checkSourceReconnection() {
        if (newSource.equals(oldTarget)) return false;
        for (Object element : newSource.getSourceConnections()) {
            Route conn = (Route) element;
            if (conn.getTarget().equals(oldTarget) && !conn.equals(connection)) return false;
        }
        return true;
    }

    /**
	 * Return true, if reconnecting the connection-instance to newTarget is allowed.
	 */
    private boolean checkTargetReconnection() {
        if (newTarget.equals(oldSource)) return false;
        for (Object element : newTarget.getTargetConnections()) {
            Route conn = (Route) element;
            if (conn.getSource().equals(oldSource) && !conn.equals(connection)) return false;
        }
        return true;
    }

    /**
	 * Reconnect the connection to newSource (if setNewSource(...) was invoked before) or newTarget (if
	 * setNewTarget(...) was invoked before).
	 */
    @Override
    public void execute() {
        if (newSource != null) {
            connection.reconnect(newSource, oldTarget);
        } else if (newTarget != null) {
            connection.reconnect(oldSource, newTarget);
        } else throw new IllegalStateException("Should not happen");
    }

    /**
	 * Set a new source endpoint for this connection. When execute() is invoked, the source endpoint of the
	 * connection will be attached to the supplied Shape instance.
	 * <p>
	 * Note: Calling this method, deactivates reconnection of the <i>target</i> endpoint. A single instance of
	 * this command can only reconnect either the source or the target endpoint.
	 * </p>
	 * 
	 * @param connectionSource
	 *          a non-null Shape instance, to be used as a new source endpoint
	 * @throws IllegalArgumentException
	 *           if connectionSource is null
	 */
    public void setNewSource(RouteEndPoint connectionSource) {
        if (connectionSource == null) throw new IllegalArgumentException();
        setLabel("move connection startpoint");
        newSource = connectionSource;
        newTarget = null;
    }

    /**
	 * Set a new target endpoint for this connection When execute() is invoked, the target endpoint of the
	 * connection will be attached to the supplied Shape instance.
	 * <p>
	 * Note: Calling this method, deactivates reconnection of the <i>source</i> endpoint. A single instance of
	 * this command can only reconnect either the source or the target endpoint.
	 * </p>
	 * 
	 * @param connectionTarget
	 *          a non-null Shape instance, to be used as a new target endpoint
	 * @throws IllegalArgumentException
	 *           if connectionTarget is null
	 */
    public void setNewTarget(RouteEndPoint connectionTarget) {
        if (connectionTarget == null) throw new IllegalArgumentException();
        setLabel("move connection endpoint");
        newSource = null;
        newTarget = connectionTarget;
    }

    /**
	 * Reconnect the connection to its original source and target endpoints.
	 */
    @Override
    public void undo() {
        connection.reconnect(oldSource, oldTarget);
    }
}
