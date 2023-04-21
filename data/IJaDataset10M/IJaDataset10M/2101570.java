package org.columba.calendar.ui.action;

import java.awt.event.ActionEvent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.calendar.ui.frame.api.ICalendarMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

/**
 * @author fdietz
 *
 */
public class TodayAction extends AbstractColumbaAction {

    /**
	 * @param frameMediator
	 * @param name
	 */
    public TodayAction(IFrameMediator frameMediator) {
        super(frameMediator, "Show Today");
        putValue(AbstractColumbaAction.TOOLBAR_NAME, "Today");
        setShowToolBarText(false);
        putValue(AbstractColumbaAction.LARGE_ICON, ImageLoader.getIcon(IconKeys.HOME));
        putValue(AbstractColumbaAction.SMALL_ICON, ImageLoader.getSmallIcon(IconKeys.HOME));
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        ICalendarMediator calendarFrame = (ICalendarMediator) frameMediator;
        calendarFrame.getCalendarView().viewToday();
    }
}
