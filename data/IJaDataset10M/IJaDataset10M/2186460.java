package ua.org.nuos.sdms.clientgui.client.events;

import com.github.wolfie.blackboard.Event;
import ua.org.nuos.sdms.middle.entity.Group;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 27.03.12
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class UpdateGroupEvent implements Event {

    private Group group;

    public UpdateGroupEvent(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
