package org.progeeks.util.swing;

import javax.swing.JComponent;
import org.progeeks.util.ViewContext;

/**
 *  Implementations of this factory create a JComponent for a
 *  specified ViewContext.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public interface JComponentFactory {

    /**
     *  Creates a JComponent associated with the specified view context.
     */
    public JComponent createViewComponent(ViewContext context);
}
