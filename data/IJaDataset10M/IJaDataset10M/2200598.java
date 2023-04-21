package net.sf.doolin.gui.swing;

/**
 * This completion handler allows the creation of new strings.
 * 
 * @author Damien Coraboeuf
 * @version $Id: StringCompletionHandler.java,v 1.1 2007/08/10 16:54:40 guinnessman Exp $
 */
public class StringCompletionHandler implements CompletionHandler {

    /**
	 * Returns the given text.
	 * 
	 * @see net.sf.doolin.gui.swing.CompletionHandler#createItem(java.lang.String)
	 */
    public Object createItem(String text) throws UnsupportedOperationException {
        return text;
    }

    /**
	 * Returns <code>true</code>.
	 * 
	 * @see net.sf.doolin.gui.swing.CompletionHandler#isCompletionAllowsCreation()
	 */
    public boolean isCompletionAllowsCreation() {
        return true;
    }
}
