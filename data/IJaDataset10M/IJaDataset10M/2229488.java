package net.sf.japi.swing.misc;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** Implementation of {@link ErrorHandler} for displaying XML parser errors on the screen.
 * @warning DO NOT RELY ON THE INHERITANCE!
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public final class JSAXErrorHandler extends JOptionPane implements ErrorHandler {

    /** Action Builder. */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.japi.swing");

    /** The JTextArea which displays the errors.
     * @serial include
     */
    private final JTextArea errorPane = new JTextArea();

    /** The Dialog.
     * @serial include
     */
    private JDialog dialog;

    /** Parent component.
     * @serial include
     */
    private final Component parent;

    /** Button for closing.
     * @serial include
     */
    private final JButton closeButton;

    /** Create a JSAXErrorHandler.
     * @param parent Parent component to display on.
     */
    public JSAXErrorHandler(final Component parent) {
        errorPane.setEditable(false);
        setMessage(new JScrollPane(errorPane));
        final JButton clearButton = new JButton(JSAXErrorHandler.ACTION_BUILDER.createAction(false, "saxErrorClear", this));
        closeButton = new JButton(JSAXErrorHandler.ACTION_BUILDER.createAction(false, "saxErrorClose", this));
        this.parent = parent;
        setOptions(new Object[] { closeButton, clearButton });
    }

    /** {@inheritDoc} */
    public void warning(final SAXParseException exception) throws SAXException {
        add("warning", exception);
    }

    /** {@inheritDoc} */
    public void error(final SAXParseException exception) throws SAXException {
        add("error", exception);
    }

    /** {@inheritDoc} */
    public void fatalError(final SAXParseException exception) throws SAXException {
        add("fatal", exception);
    }

    /** Add an exception.
     * @param level Error level
     * @param exception Exception
     */
    private void add(final String level, final SAXParseException exception) {
        final StringBuilder errorMsg = new StringBuilder();
        final int lineNumber = exception.getLineNumber();
        final int columnNumber = exception.getColumnNumber();
        errorMsg.append(exception.getSystemId());
        errorMsg.append(':');
        if (lineNumber != 0) {
            errorMsg.append(lineNumber);
            errorMsg.append(':');
            if (columnNumber != 0) {
                errorMsg.append(columnNumber);
                errorMsg.append(':');
            }
        }
        errorMsg.append(' ');
        errorMsg.append(level);
        errorMsg.append(": ");
        errorMsg.append(exception.getMessage());
        if (exception.getException() != null) {
            errorMsg.append(" (Cause: ");
            errorMsg.append(exception.getException().toString());
            errorMsg.append(')');
        }
        errorMsg.append('\n');
        errorPane.append(errorMsg.toString());
        showDialog();
    }

    /** Show the dialog. */
    private void showDialog() {
        if (dialog == null) {
            dialog = createDialog(parent, JSAXErrorHandler.ACTION_BUILDER.getString("saxError_title"));
            closeButton.requestFocusInWindow();
            dialog.getRootPane().setDefaultButton(closeButton);
            dialog.setModal(false);
            dialog.setResizable(true);
            dialog.setVisible(true);
        }
    }

    /** Action method for clearing. */
    public void saxErrorClear() {
        errorPane.setText("");
    }

    /** Action method for closing. */
    public void saxErrorClose() {
        setValue(closeButton);
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
    }
}
