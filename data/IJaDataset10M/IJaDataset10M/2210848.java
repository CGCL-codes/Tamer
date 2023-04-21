package de.mguennewig.pobjform.swing;

import java.util.Iterator;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import de.mguennewig.pobjects.metadata.ArrayEntry;
import de.mguennewig.pobjform.AbstractArrayElement;
import de.mguennewig.pobjform.Message;

/** A Swing implementation of an array element.
 *
 * <p>TODO: The child element(s) are not displayed so far.</p>
 *
 * @author Michael G�nnewig
 */
public class SwingArrayElement extends AbstractArrayElement implements SwingFormElement {

    protected final JLabel errorComponent;

    protected final JLabel labelComponent;

    /** Creates a new SwingArrayElement. */
    public SwingArrayElement(final PObjSwingForm form, final ArrayEntry entry) {
        super(form, entry);
        errorComponent = new JLabel("");
        labelComponent = new JLabel(getLabel());
    }

    protected SwingFormElement getSwingElement(final int index) {
        return (SwingFormElement) getElement(index);
    }

    public JComponent getComponent() {
        return null;
    }

    public final JComponent getErrorComponent() {
        return errorComponent;
    }

    public final JLabel getLabelComponent() {
        return labelComponent;
    }

    public void updateErrorComponent() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Message> i = getForm().getMessages(getProperty());
        final ResourceBundle resources = ((PObjSwingForm) getForm()).getResourceBundle();
        while (i.hasNext()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(i.next().format(resources));
        }
        errorComponent.setText(sb.toString());
    }
}
