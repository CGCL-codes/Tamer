package de.fhg.igd.semoa.bin.starter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The basic idea of this generic <code>ConverterPanel</code> implementation
 * is that embedded {@link ParameterComponent} objects handover user input
 * for conversion. Afterwards, the <code>ConverterPanel</code> returns the
 * converted user input to the first item of the embedded
 * <code>ParameterComponent</code> chain, whose elements are linked by
 * {@link ParameterChangeListener} registrations. As soon as the last element
 * has been reached, the enclosing <code>ConverterPanel</code> (as the very
 * last element in the {@link ParameterChangeEvent} chain) will notify
 * registered {@link ChangeListener} objects, which are typically one or more
 * other <code>ConverterPanel</code> or {@link StringIcon} objects that are
 * supposed to automatically adapt accordingly.
 * <p><b>Notice:</b> User input may also be dropped (via Drag 'n' Drop) into
 * embedded components.
 *
 * @author Matthias Pressfreund
 * @version "$Id: AbstractConverterPanel.java 1616 2005-07-06 10:34:39Z jpeters $"
 */
public abstract class AbstractConverterPanel extends JPanel implements ChangeListener, ConverterPanel, DropDataHandler, ParameterChangeListener {

    /**
     * The storage for registered {@link ChangeListener} objects
     */
    protected Set changeListeners_;

    /**
     * Indicates whether or not the content of this panel is
     * required for the startup of the <i>SeMoA</i> server
     */
    protected boolean required_;

    /**
     * The status of this input panel
     */
    protected Status status_;

    /**
     * Create an <code>AbstractConverterPanel</code>.
     */
    public AbstractConverterPanel(boolean required) {
        super(new GridBagLayout());
        changeListeners_ = new HashSet();
        required_ = required;
        Utils.addConstrained(this, build(), 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1, 1, new Insets(5, 5, 5, 5));
        setStatus();
    }

    /**
     * This method builds the component content.
     *
     * @return A <code>JPanel</code> containing all sub-components
     */
    protected abstract JPanel build();

    /**
     * Convert the given user input into an appropriate <code>Parameter</code>.
     *
     * @param userInput The user input to be converted
     * @return The corresponding <code>Parameter</code> object
     * @throws ConverterException
     *   if the user input cannot be converted
     */
    protected abstract Parameter convert(String userInput) throws ConverterException;

    /**
     * Apply the given parameter.
     *
     * @param parameter The parameter
     */
    protected abstract void applyParameter(Parameter parameter);

    /**
     * Apply the given converter failure.
     *
     * @param ce The converter failure
     */
    protected abstract void applyFailure(ConverterException ce);

    /**
     * Apply the current status.
     *
     * @param tooltip The tooltip text
     */
    protected abstract void applyStatus(String tooltip);

    /**
     * Get the help text for this panel associated with the current status.
     *
     * @return The help text
     */
    protected abstract String getStatusHelp();

    protected void setStatus() {
        Parameter parameter;
        parameter = requestParameter();
        if (required_) {
            status_ = parameter == null || parameter.isFlawed() ? Status.ERROR : Status.OK;
        } else {
            status_ = parameter != null && parameter.isFlawed() ? Status.WARNING : Status.OK;
        }
    }

    public Status getStatus() {
        return status_;
    }

    public void applyUserInput(String userInput) {
        if (isEnabled()) {
            try {
                applyParameter(convert(userInput));
            } catch (ConverterException ce) {
                applyFailure(ce);
            }
        }
    }

    public void applyDroppedUserInput(String userInput) {
        applyUserInput(userInput);
    }

    public void reconfirmParameter(Parameter parameter) {
        applyParameter(null);
        if (parameter != null) {
            applyUserInput(parameter.toShortString());
        }
    }

    public void parameterChangeCommitted(ParameterChangeEvent pce) {
        setStatus();
        applyStatus(getStatusHelp());
        fireStateChanged();
    }

    public void parameterChangePending(ParameterChangeEvent pce) {
    }

    public void parameterChangeFailed(ParameterChangeEvent pce) {
        ConverterException ce;
        StringTokenizer toki;
        StringBuffer html;
        String text;
        setStatus();
        ce = pce.getConverterException();
        text = ce != null ? ce.getDescription() : null;
        if (text != null) {
            JOptionPane.showMessageDialog(null, text, "SeMoA Starter Alert: " + ce.getReason(), JOptionPane.ERROR_MESSAGE);
        }
        toki = new StringTokenizer(text, "\n");
        html = new StringBuffer("<html>");
        while (toki.hasMoreTokens()) {
            html.append(toki.nextToken());
            if (toki.hasMoreTokens()) {
                html.append("<br>");
            }
        }
        html.append("</html>");
        applyStatus(html.toString());
        fireStateChanged();
    }

    public void addChangeListener(ChangeListener l) throws NullPointerException {
        if (l == null) {
            throw new NullPointerException("l");
        }
        changeListeners_.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners_.remove(l);
    }

    /**
     * Fire a {@link ChangeEvent} to all registered listeners.
     */
    protected void fireStateChanged() {
        ChangeEvent ce;
        Iterator i;
        ce = new ChangeEvent(this);
        for (i = changeListeners_.iterator(); i.hasNext(); ) {
            ((ChangeListener) i.next()).stateChanged(ce);
        }
    }
}
