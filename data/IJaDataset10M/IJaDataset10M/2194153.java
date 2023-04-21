package net.jabbra.core.interfaces;

import net.jabbra.core.JabbraConnection;
import net.jabbra.core.roster.JabbraContact;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 23.10.2007
 * Time: 20:30:52
 * To change this template use File | Settings | File Templates.
 */
public interface JabbraRosterEventListener {

    void changeRosterVisible();

    void changeChatVisible();

    void processContactDoubleClick(JabbraConnection connection, JabbraContact contact);
}
