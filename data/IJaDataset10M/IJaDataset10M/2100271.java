package viewer.search;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A convenience adapter that delegates all three different event of a DocumentListener to a
 * single update method. It allows implementing a DocumentListener by implementing a single
 * method.
 * 
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 */
public abstract class DocumentAdapter implements DocumentListener {

    /**
	 * A general event that will be triggerd by any of the DocumentListener's events.
	 * 
	 * @param e
	 *            the event that occurred.
	 */
    public abstract void update(DocumentEvent e);

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }
}
