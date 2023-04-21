package org.fenggui.event;

import org.fenggui.IWidget;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class SizeChangedEvent extends WidgetEvent {

    private Dimension oldSize;

    private Dimension newSize;

    public SizeChangedEvent(IWidget source, Dimension oldSize, Dimension newSize) {
        super(source);
        this.oldSize = oldSize;
        this.newSize = newSize;
    }

    /**
   * @return Returns the oldSize.
   */
    public Dimension getOldSize() {
        return oldSize;
    }

    /**
   * @return Returns the newSize.
   */
    public Dimension getNewSize() {
        return newSize;
    }
}
