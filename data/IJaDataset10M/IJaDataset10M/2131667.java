package fcseiseki.schedule.pages;

import org.apache.click.Context;
import org.apache.click.control.Decorator;
import fcseiseki.schedule.model.Schedule;

public abstract class ScheduleDecorator implements Decorator {

    @Override
    public final String render(final Object object, final Context context) {
        return renderSupport((Schedule) object);
    }

    protected abstract String renderSupport(Schedule schedule);
}
