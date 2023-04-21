package csheets.ext.assertion;

import java.util.EventListener;

/**
 * A listener interface for receiving notification on events occurring in an
 * assertable cell.
 * @author Einar Pehrson
 */
public interface AssertableCellListener extends EventListener {

    /**
	 * Invoked when an assertion is added to or removed from a cell.
	 * @param cell the cell that was modified
	 */
    public void assertionsChanged(AssertableCell cell);
}
