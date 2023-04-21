package org.rapla.plugin.weekview;

import javax.swing.Icon;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.gui.CalendarModel;
import org.rapla.gui.SwingCalendarView;
import org.rapla.gui.ViewFactory;
import org.rapla.gui.images.Images;
import org.rapla.servletpages.RaplaPageGenerator;

public class WeekViewFactory extends RaplaComponent implements ViewFactory {

    public WeekViewFactory(RaplaContext context) throws RaplaException {
        super(context);
    }

    public static final String WEEK_VIEW = "week";

    public SwingCalendarView createSwingView(RaplaContext context, CalendarModel model, boolean editable) throws RaplaException {
        return new SwingWeekCalendar(context, model, editable);
    }

    public RaplaPageGenerator createHTMLView(RaplaContext context, CalendarModel model) throws RaplaException {
        return new HTMLWeekViewPage(context, model);
    }

    public String getViewId() {
        return WEEK_VIEW;
    }

    public String getName() {
        return getString(WEEK_VIEW);
    }

    Icon icon;

    public Icon getIcon() {
        if (icon == null) {
            icon = Images.getIcon("/org/rapla/plugin/weekview/images/week.png");
        }
        return icon;
    }

    public String getMenuSortKey() {
        return "B";
    }
}
