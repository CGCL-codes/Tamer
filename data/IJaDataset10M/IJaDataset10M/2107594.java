package net.sf.graphiti.ui.commands.refinement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.graphiti.GraphitiModelPlugin;
import net.sf.graphiti.io.GenericGraphParser;
import net.sf.graphiti.io.IncompatibleConfigurationFile;
import net.sf.graphiti.model.Graph;
import net.sf.graphiti.model.IRefinementPolicy;
import net.sf.graphiti.model.ObjectType;
import net.sf.graphiti.model.Vertex;
import net.sf.graphiti.ui.GraphitiUiPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * This class provides facilities to prompt the user for a source port or target
 * port by parsing the refinement (if any), or by asking the user to enter a
 * port name.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class PortChooser {

    private class PortContentProvider implements IStructuredContentProvider {

        @Override
        public void dispose() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return ((List<?>) inputElement).toArray();
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    private String connection;

    /**
	 * Creates a new port chooser using the given refinement manager.
	 * 
	 * @param manager
	 *            The refinement manager.
	 * @param connection
	 *            The title of the connection, as "source - target".
	 */
    public PortChooser(String connection) {
        this.connection = connection;
    }

    /**
	 * Displays a {@link ListDialog} filled with the given port list, asking the
	 * user to choose one. The <code>edgePort</code> string is used to identify
	 * the port (ie source or target) to choose.
	 * 
	 * @param ports
	 *            A list of port names.
	 * @param edgePort
	 *            A string identifying the port ("source port" or
	 *            "target port").
	 * @return The name of the chosen port.
	 */
    private String choosePort(List<String> ports, String edgePort) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        Shell shell = window.getShell();
        ListDialog dialog = new ListDialog(shell);
        dialog.setContentProvider(new PortContentProvider());
        dialog.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return (String) element;
            }
        });
        dialog.setAddCancelButton(false);
        dialog.setInput(ports);
        dialog.setMessage("Please choose a " + edgePort + ":");
        dialog.setTitle("Connection: " + connection);
        dialog.open();
        if (dialog.getResult() == null || dialog.getResult().length == 0) {
            return null;
        } else {
            return (String) dialog.getResult()[0];
        }
    }

    /**
	 * Displays an error message with the given exception.
	 * 
	 * @param message
	 *            A description of the error.
	 * @param exception
	 *            An exception.
	 */
    private void errorMessage(String message, Throwable exception) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        Shell shell = window.getShell();
        IStatus status = new Status(IStatus.ERROR, GraphitiUiPlugin.PLUGIN_ID, message, exception);
        ErrorDialog.openError(shell, "Error", message, status, IStatus.ERROR);
    }

    /**
	 * Returns a port name from the current vertex (set by getSourcePort or
	 * getTargetPort).
	 * 
	 * @param vertex
	 *            vertex
	 * @param edgePort
	 *            The label to use when prompting the user to choose
	 *            ("source port" or "target port").
	 * @param portTypes
	 *            An array where each entry is a valid port type in this
	 *            context, either {@link Vertex#TYPE_INPUT_PORT},
	 *            {@link Vertex#TYPE_OUTPUT_PORT} or {@link Vertex#TYPE_PORT}.
	 * @return A port name if found, <code>null</code> otherwise.
	 */
    private String getPort(Vertex vertex, String edgePort, String[] portTypes) {
        IRefinementPolicy policy = vertex.getConfiguration().getRefinementPolicy();
        IFile sourceFile = policy.getRefinementFile(vertex);
        if (sourceFile != null) {
            List<String> ports = getPorts(sourceFile, portTypes);
            if (!ports.isEmpty()) {
                return choosePort(ports, edgePort);
            }
        }
        return null;
    }

    /**
	 * Prompts the user for an arbitrary port name using a simple
	 * {@link InputDialog}. The <code>portName</code> parameter indicates the
	 * role of the given port.
	 * 
	 * @param portName
	 *            "source port" or "target port".
	 * @return The port name, or <code>null</code> if the user entered a blank
	 *         port name (equivalent to "no port").
	 */
    private String getPortName(String portName) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        Shell shell = window.getShell();
        InputDialog dialog = new InputDialog(shell, "Connection: " + connection, "Please enter a " + portName + " name:", "", null);
        dialog.open();
        String value = dialog.getValue();
        if (value.isEmpty()) {
            return null;
        } else {
            return value;
        }
    }

    /**
	 * Returns the list of ports read from the given file that have the given
	 * port type.
	 * 
	 * @param sourceFile
	 *            The source file as an {@link IFile}.
	 * @param portTypes
	 *            An array where each entry is a valid port type in this
	 *            context, either {@link Vertex#TYPE_INPUT_PORT},
	 *            {@link Vertex#TYPE_OUTPUT_PORT} or {@link Vertex#TYPE_PORT}.
	 * @return A list of port names.
	 */
    private List<String> getPorts(IFile sourceFile, String[] portTypes) {
        GenericGraphParser parser = new GenericGraphParser(GraphitiModelPlugin.getDefault().getConfigurations());
        Graph graph = null;
        try {
            graph = parser.parse(sourceFile);
        } catch (IncompatibleConfigurationFile e) {
            errorMessage(e.getMessage(), e.getCause());
        }
        if (graph == null) {
            return new ArrayList<String>();
        } else {
            Set<Vertex> vertices = graph.vertexSet();
            List<String> ports = new ArrayList<String>();
            for (Vertex vertex : vertices) {
                for (String portType : portTypes) {
                    if (vertex.getType().getName().equals(portType)) {
                        String id = (String) vertex.getValue(ObjectType.PARAMETER_ID);
                        ports.add(id);
                        break;
                    }
                }
            }
            return ports;
        }
    }

    /**
	 * Returns a port from the given vertex.
	 * 
	 * @param source
	 *            The edge's source vertex.
	 * @return A port name.
	 */
    public String getSourcePort(Vertex source) {
        String port = getPort(source, "source port", new String[] { Vertex.TYPE_OUTPUT_PORT, Vertex.TYPE_PORT });
        if (port == null) {
            return getPortName("source port");
        } else {
            return port;
        }
    }

    /**
	 * Returns a port from the given vertex.
	 * 
	 * @param target
	 *            The edge's target vertex.
	 * @return A port name.
	 */
    public String getTargetPort(Vertex target) {
        String port = getPort(target, "target port", new String[] { Vertex.TYPE_INPUT_PORT, Vertex.TYPE_PORT });
        if (port == null) {
            return getPortName("target port");
        } else {
            return port;
        }
    }
}
