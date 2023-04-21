package org.columba.calendar.ui.action;

import java.awt.event.ActionEvent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.calendar.resourceloader.ResourceLoader;
import org.columba.calendar.ui.frame.api.ICalendarMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.action.AbstractSelectableAction;

/**
 * @author fdietz
 *
 */
public class DayViewSelectableAction extends AbstractSelectableAction {

    /**
	 * @param controller
	 * @param name
	 */
    public DayViewSelectableAction(IFrameMediator controller) {
        super(controller, "Show Day");
        putValue(AbstractColumbaAction.TOOLBAR_NAME, "Day");
        setShowToolBarText(true);
        putValue(AbstractColumbaAction.LARGE_ICON, ResourceLoader.getIcon("dayview.jpg"));
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        ICalendarMediator calendarFrame = (ICalendarMediator) frameMediator;
        calendarFrame.showDayView();
    }
}
