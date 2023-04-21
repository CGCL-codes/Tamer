package net.sf.jmoney;

import org.eclipse.ui.IMemento;

/**
 * An implementation of this interface is returned by all implementations
 * of the createFormPage method in the IBookkeepingPageFactory interface.
 *
 * @author Nigel Westbury
 */
public interface IBookkeepingPage {

    /**
	 * Save the state of the page in a memento.  This method
	 * is called when the editor containing this page is closed.
	 * If the editor is re-created, the memento will be passed
	 * to the <code>createFormPage</code> method.
	 */
    void saveState(IMemento memento);
}
