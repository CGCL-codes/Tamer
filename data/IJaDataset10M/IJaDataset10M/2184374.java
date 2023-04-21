package com.liferay.portal.ejb;

import org.springframework.context.ApplicationContext;
import com.liferay.portal.util.SpringUtil;

/**
 * <a href="GroupManagerFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class GroupManagerFactory {

    private static final String NAME = GroupManagerFactory.class.getName();

    public static GroupManager getManager() {
        ApplicationContext ctx = SpringUtil.getContext();
        GroupManagerFactory factory = (GroupManagerFactory) ctx.getBean(NAME);
        return factory._manager;
    }

    public void setManager(GroupManager manager) {
        _manager = manager;
    }

    private GroupManager _manager;
}
