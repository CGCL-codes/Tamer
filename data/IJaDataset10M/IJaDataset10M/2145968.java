package org.ethontos.owlwatcher.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.ethontos.owlwatcher.project.Project;
import org.ethontos.owlwatcher.view.ViewerLib.DataViewer;
import org.ethontos.owlwatcher.view.dialogs.InstancesDialog;

public abstract class ClassDisplayPane extends JPanel {

    /**
     * Defines commands for working with terms (classes).  Hint: these are interpreted in
     * ConcreteTreeNode
     * @author peter
     * created Feb 4, 2008
     *
     */
    public enum Command {

        nocommand, addsubclass, deletesubclass, showinstances, settreeview, setbuttonview
    }

    ;

    Project project;

    DataViewer viewer = null;

    WatchTab owner = null;

    private InstancesDialog insDialog;

    private Command nodeSelectionCommand = Command.nocommand;

    /**
     * specifies command to be applied to a tree node when touched, rather
     * than registering an event.
     * @param command int specifying command to be applied to the next selected ontology term
     * @return new value of command
     */
    public Command setNodeSelectionCommand(Command command) {
        nodeSelectionCommand = command;
        return command;
    }

    public Command getNodeSelectionCommand() {
        return nodeSelectionCommand;
    }

    /**
     * This should return something containing an OWLClass that will understand commands from here.
     * @param x coordinate of a mouse event
     * @param y coordinate of a mouse event
     * @return the container whose display area either contains or is closest to the event's coordinates
     */
    public abstract Object getNearestTarget(int x, int y);

    public ClassDisplayPane() {
    }

    /**
     * Callback function from the owning WatchTab to get the nodeSelectionCommand
     * to correspond to the state of the buttons on the bar.
     *
     */
    public void updateCommandState() {
        nodeSelectionCommand = owner.queryButtonStates();
    }

    /**
     * Shows an input dialog dependent on this pane.
     * @param title
     * @param prompt
     * @return the object returned by the dialog
     */
    public Object showInputDialog(String title, String prompt) {
        final int plain = JOptionPane.PLAIN_MESSAGE;
        return JOptionPane.showInputDialog(this, title, prompt, plain, null, null, null);
    }

    /**
     * Shows a warning message dialog dependent on this pane.
     * @param text the warning message
     * @param title dialog window title
     */
    public void showWarning(String text, String title) {
        JOptionPane.showMessageDialog(this, text, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a yes/no confirmation dialog dependent on this pane
     * @param text the query message
     * @param title dialog window title
     * @return true when the user selects the 'yes' button.
     */
    public boolean showQuery(String text, String title) {
        final int options = JOptionPane.YES_NO_OPTION;
        int value = JOptionPane.showConfirmDialog(this, text, title, options);
        return (value == JOptionPane.YES_OPTION);
    }

    /**
     * Shows an information dialog dependent on this pane
     * @param text
     * @param title
     */
    public void showInformation(String text, String title) {
        JOptionPane.showMessageDialog(this, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This connects the pane to the view - so when events are recorded, the viewer's
     * notion of the current time can be stored.  This should only be called if the DataViewer changes.
     * @param d Viewer this pane is paired with.
     */
    void setViewer(DataViewer d) {
        viewer = d;
    }

    /**
     * This method suggests the button bar should be here, not on the watch tab
     * @param t
     */
    public void setOwner(WatchTab t) {
        owner = t;
        insDialog = new InstancesDialog(t.theApp.getFrame());
    }

    /**
     * 
     * @return the Pane's instance dialog
     */
    protected InstancesDialog getInstancesDialog() {
        return insDialog;
    }
}
