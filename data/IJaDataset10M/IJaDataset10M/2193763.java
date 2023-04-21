package net.sourceforge.ondex.dialog.inputs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.sourceforge.ondex.dialog.ErrorDialog;
import net.sourceforge.ondex.taverna.TavernaException;
import net.sourceforge.ondex.taverna.wrapper.TavernaInput;

/**
 * Poppup gui to collect the inputs for a workflow.
 * 
 * Inputs can come directly from users, from files or from URIs.
 * <p>
 * Limited checking is done that that inputs are valid.
 * All input are considered String because they will be passed to the command line as String 
 * and because the workflow file does not contain type information. 
 * 
 * @author Christian
 */
public class InputGui implements WindowListener, ActionListener {

    private TavernaInput[] output;

    private JButton doneButton;

    private AbstractRow[] rows;

    private JDialog dialog;

    private Frame parent;

    private static final String DONE = "Done";

    private static final String CANCEL = "Cancel";

    /**
     * Creates a popup dialog which if closed with the done button prepares the input values.
     * 
     * The popup will includes a row for each inputs based on the depth of that input.
     * 
     * @param parent Somewhere to root the popup on.
     * @param workflowName Name of the workflow. Only used for the title.
     * @param inputs A map of the names of each input to the depth of that input. 
     *     May not be null but can be empty in which case the popup never displays.
     *     Only single values depth 0 and simple arrays depth 1 are supported by Taverna value or file inputs.
     * @throws TavernaException Thrown if any input has a depth other than 0 or 1. 
     *     Also thrown as a wrapper around an unexpected exception to avoid having to throw it.
     */
    public InputGui(Frame frame, String workflowName, Map<String, Integer> inputs) throws TavernaException {
        if (inputs.isEmpty()) {
            output = new TavernaInput[0];
        } else {
            parent = frame;
            dialog = new JDialog(frame, "Inputs for " + workflowName, false);
            dialog.addWindowListener(this);
            dialog.setModal(true);
            Container pane = dialog.getContentPane();
            pane.setLayout(new GridBagLayout());
            output = null;
            rows = new AbstractRow[inputs.size()];
            String[] keys = inputs.keySet().toArray(new String[0]);
            int row;
            for (row = 0; row < inputs.size(); row++) {
                JLabel label = new JLabel(keys[row]);
                GridBagConstraints gridBagConstraints = AbstractRow.getConstraints(0, row);
                pane.add(label, gridBagConstraints);
                Integer depth = inputs.get(keys[row]);
                gridBagConstraints = AbstractRow.getConstraints(1, row);
                gridBagConstraints.weightx = 1;
                switch(depth) {
                    case 0:
                        rows[row] = new SingleRow(this, keys[row]);
                        break;
                    case 1:
                        rows[row] = new ListRow(this, keys[row]);
                        gridBagConstraints.weighty = 1;
                        break;
                    default:
                        throw new TavernaException("Unexpected depth " + depth + " for input " + keys[row]);
                }
                pane.add(rows[row], gridBagConstraints);
            }
            addButton(pane, CANCEL, 0, row);
            doneButton = addButton(pane, DONE, 1, row);
            doneButton.setEnabled(false);
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private JButton addButton(Container pane, String text, int column, int row) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setActionCommand(text);
        button.addActionListener(this);
        GridBagConstraints gridBagConstraints = AbstractRow.getConstraints(column, row);
        pane.add(button, gridBagConstraints);
        return button;
    }

    /**
     * Returns the values previously entered by the user and stored when they hit Done.
     * 
     * @return An Array of the Inputs that can be used to fill the command line parameters.
     *    The length of the array will be equal to the number of inputs which could be zero.
     * 
     * @throws TavernaException Thrown if this method is called before the GUI was closed using the done button.
     */
    public TavernaInput[] getValueArray() throws TavernaException {
        if (output == null) {
            throw new TavernaException("Illgeal call to getValueArray() before GUI closed with done.");
        }
        return output;
    }

    private void cancel() {
        output = null;
        dialog.dispose();
    }

    private void done() throws TavernaException, IOException {
        output = new TavernaInput[rows.length];
        for (int row = 0; row < rows.length; row++) {
            output[row] = rows[row].getInput();
        }
        dialog.dispose();
    }

    void checkReady() {
        boolean ready = true;
        for (int row = 0; row < rows.length; row++) {
            if (!rows[row].ready()) {
                ready = false;
                System.out.println("row " + row + " not ready");
            }
        }
        doneButton.setEnabled(ready);
    }

    Frame getParent() {
        return parent;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        cancel();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(e);
            Object source = e.getSource();
            String command = e.getActionCommand();
            if (DONE.equals(command)) {
                done();
            } else if (CANCEL.equals(command)) {
                cancel();
            }
        } catch (Exception ex) {
            ErrorDialog.show(parent, ex);
        }
    }
}
