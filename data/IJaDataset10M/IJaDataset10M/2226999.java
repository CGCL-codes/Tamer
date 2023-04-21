package org.kommando.core.search;

import java.awt.Dimension;
import javax.swing.Icon;
import org.kommando.core.Displayable;

/**
 * @author Peter De Bruycker
 */
public interface Result<T extends Displayable> extends Displayable {

    /**
     * Returns the html formatted tooltip text to be used in the gui.
     * 
     * @return the tooltip text
     */
    String getToolTip();

    T getValue();

    Icon getIcon(Dimension size);

    /**
     * Returns the html formatted name that can be used in the gui.
     * 
     * @return the formatted name
     */
    String getHtmlFormattedName();
}
