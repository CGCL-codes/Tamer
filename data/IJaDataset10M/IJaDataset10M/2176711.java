package org.argetr.resim.ui.model.commands;

import java.util.Iterator;
import java.util.List;
import org.argetr.resim.proc.Processor;
import org.argetr.resim.proc.tools.ProcessorGraph;
import org.argetr.resim.ui.model.ComponentItem;
import org.argetr.resim.ui.model.Connection;
import org.argetr.resim.ui.model.Shape;
import org.eclipse.gef.commands.Command;
import java.util.ArrayList;

/**
 * A command to create a connection between two shapes. The command can be
 * undone or redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy.
 * To use this command properly, following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getConnectionCreateCommand(...)</tt> method, to
 * create a new instance of this class and put it into the
 * CreateConnectionRequest.</li>
 * <li>Override the <tt>getConnectionCompleteCommand(...)</tt> method, to
 * obtain the Command from the ConnectionRequest, call setTarget(...) to set the
 * target endpoint of the connection and return this command instance.</li>
 * </ol>
 * 
 * @see org.argetr.resim.parts.ShapeEditPart#createEditPolicies()
 *      for an example of the above procedure.
 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
 * @author Elias Volanakis
 */
public class ConnectionCreateCommand extends Command {

    /** The connection instance. */
    private Connection connection;

    /** The desired line style for the connection (dashed or solid). */
    private final int lineStyle;

    /** Start endpoint for the connection. */
    private final Shape source;

    /** Target endpoint for the connection. */
    private Shape target;

    /**
	 * Instantiate a command that can create a connection between two shapes.
	 * 
	 * @param source
	 *            the source endpoint (a non-null Shape instance)
	 * @param lineStyle
	 *            the desired line style. See Connection#setLineStyle(int) for
	 *            details
	 * @throws IllegalArgumentException
	 *             if source is null
	 * @see Connection#setLineStyle(int)
	 */
    public ConnectionCreateCommand(Shape source, int lineStyle) {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        setLabel("connection creation");
        this.source = source;
        this.lineStyle = lineStyle;
    }

    public boolean canExecute() {
        if (source.equals(target)) {
            return false;
        }
        for (Iterator<Connection> iter = source.getSourceConnections().iterator(); iter.hasNext(); ) {
            Connection conn = (Connection) iter.next();
            if (conn.getTarget().equals(target)) {
                return false;
            }
        }
        if (target instanceof ComponentItem) {
            ComponentItem srcCI = (ComponentItem) source;
            ComponentItem dstCI = (ComponentItem) target;
            List<Connection> conns = dstCI.getTargetConnections();
            int noInputs = dstCI.getProcessor().getConnectionRule().getNoInputs();
            if (conns.size() == noInputs) {
                return false;
            }
            Processor srcProc = srcCI.getProcessor();
            Processor dstProc = dstCI.getProcessor();
            int srcIndex = Connection.CANDIDATE_SRC_INDEX;
            ArrayList<Integer> validConnIndices = new ArrayList<Integer>();
            for (int i = 0; i < noInputs; i++) {
                validConnIndices.add(new Integer(i));
            }
            for (Iterator<Connection> iterator = conns.iterator(); iterator.hasNext(); ) {
                Connection connection = (Connection) iterator.next();
                if (connection.isConnected()) {
                    validConnIndices.remove(new Integer(connection.getDstIndex()));
                }
            }
            for (int k = 0; k < noInputs; k++) {
                boolean validConnection = ProcessorGraph.getInstance().validConnection(srcProc, srcIndex, dstProc, k);
                if (!validConnection) {
                    validConnIndices.remove(new Integer(k));
                }
            }
            for (int k = 0; k < validConnIndices.size(); k++) {
            }
            if (validConnIndices.size() == 0) {
                return false;
            }
            dstCI.getIconFigure().setValidInputIndices(validConnIndices);
        }
        return true;
    }

    public void execute() {
        connection = new Connection(source, target, Connection.CANDIDATE_SRC_INDEX, Connection.CANDIDATE_DST_INDEX);
        connection.setLineStyle(lineStyle);
    }

    public void redo() {
        connection.reconnect();
    }

    /**
	 * Set the target endpoint for the connection.
	 * 
	 * @param target
	 *            that target endpoint (a non-null Shape instance)
	 * @throws IllegalArgumentException
	 *             if target is null
	 */
    public void setTarget(Shape target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        this.target = target;
    }

    public void undo() {
        connection.disconnect();
    }
}
