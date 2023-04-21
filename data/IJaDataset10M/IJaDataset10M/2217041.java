package org.gudy.azureus2.ui.swt.components.graphics;

import org.eclipse.swt.widgets.Canvas;

/**
 * @author Olivier
 *
 */
public interface Graphic {

    public void initialize(Canvas canvas);

    public void refresh();
}
