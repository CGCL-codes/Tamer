package ru.yep.forum.events;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import ru.yep.forum.core.User;
import ru.yep.forum.utils.ForumUtil;
import ru.yep.forum.utils.ForumUtil.URLs;

/**
 * @author Oleg Orlov
 */
public final class EventShiftAdd extends GenericEvent {

    private final User currentUser;

    private final Date startShiftDate;

    public EventShiftAdd(User currentUser, Date startShiftDate) {
        this.currentUser = currentUser;
        this.startShiftDate = startShiftDate;
    }

    public String getAsNotifierHtml(HttpServletRequest request) {
        return "" + currentUser.getName() + " добавил смену " + ForumUtil.decorate(startShiftDate) + "\n" + ForumUtil.createHref(ForumUtil.getAppURL(request) + "/" + URLs.WORKSHOP_PAGE, "Посмотреть график");
    }
}
