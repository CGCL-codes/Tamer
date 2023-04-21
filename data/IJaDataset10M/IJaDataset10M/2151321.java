package net.sf.doolin.tabular.support;

import net.sf.doolin.util.Utils;

/**
 * Column based on a property of an item.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <T>
 *            Type of data in the rows
 */
public class PropertyTabularColumn<T> extends AbstractTabularColumn<T> {

    private final String propertyPath;

    /**
	 * Constructor
	 * 
	 * @param title
	 *            Title of the column
	 * @param propertyPath
	 *            Path to the property of the row item to take as the column
	 *            value
	 */
    public PropertyTabularColumn(String title, String propertyPath) {
        super(title);
        this.propertyPath = propertyPath;
    }

    @Override
    public Object getValue(T item) {
        return Utils.getProperty(item, this.propertyPath);
    }
}
