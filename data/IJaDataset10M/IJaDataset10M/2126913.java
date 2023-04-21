package riaf.facade;

import java.util.List;

/**
 * The {@link IRowStyleable} interface identifies components who support style
 * classes for their rows.
 */
public interface IRowStyleable {

    /**
	 * Adds the given style class to the row identified by the given index.
	 * 
	 * @param row
	 *            the row's model index.
	 * @param styleClass
	 *            the style class to add.
	 */
    public void addRowStyleClass(int row, String styleClass);

    /**
	 * Removes the given style class from the row identified by the given index.
	 * 
	 * @param row
	 *            the row's model index.
	 * @param styleClass
	 *            the style class to remove.
	 */
    public void removeRowStyleClass(int row, String styleClass);

    /**
	 * Removes all style classes from the row specified by the given index
	 * 
	 * @param row
	 *            the row's model index.
	 */
    public void clearRowStyleClasses(int row);

    /**
	 * Gets the classes assigned for the row specified by the given index
	 * 
	 * @param row
	 *            the row's model index.
	 * @return a list with the style classes assigned for the row, or
	 *         <code>null</code> if there aren't any.
	 */
    public List<String> getRowStyleClasses(int row);
}
