package com.liferay.portlet.polls.ejb;

import org.springframework.context.ApplicationContext;
import com.liferay.portal.util.SpringUtil;

/**
 * <a href="PollsQuestionRemoteManagerFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PollsQuestionRemoteManagerFactory {

    private static final String NAME = PollsQuestionRemoteManagerFactory.class.getName();

    public static PollsQuestionRemoteManager getManager() {
        ApplicationContext ctx = SpringUtil.getContext();
        PollsQuestionRemoteManagerFactory factory = (PollsQuestionRemoteManagerFactory) ctx.getBean(NAME);
        return factory._manager;
    }

    public void setManager(PollsQuestionRemoteManager manager) {
        _manager = manager;
    }

    private PollsQuestionRemoteManager _manager;
}
