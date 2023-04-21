package net.frede.gui.file;

import java.io.File;
import net.frede.toolbox.filter.FilterFilePattern;

/**
 * a class that filter files. implementation of accept( File pathname ) has to
 * be provided by subclasses
 */
public class FilterPattern extends FilterFilePattern {

    /**
	 * Creates a new FilterPattern object.
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 * @param negate
	 *            DOCUMENT ME!
	 */
    public FilterPattern(String s, boolean negate) {
        super(s, negate);
    }

    /**
	 * determines if an object is accepted by this filter
	 * 
	 * @param o
	 *            the object to filter
	 * 
	 * @return true if the object is accepted, false otherwise
	 */
    public boolean accept(Object o) {
        boolean back = false;
        if (o instanceof File) {
            back = super.accept(o);
        } else if (o instanceof NodeFile) {
            back = super.accept(((NodeFile) o).getFile());
        }
        return back;
    }
}
