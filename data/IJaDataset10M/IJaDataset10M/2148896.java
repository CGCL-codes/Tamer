package de.hbrs.inf.atarrabi.action;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Session manager to manage online members.
 * @author Florian Quadt
 *
 */
@Name("atarrabiHttpSessionManager")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class AtarrabiHttpSessionManager implements Serializable {

    private static final long serialVersionUID = 4386195764977704963L;

    private static final String SESSION_ATTR_USER = "currentUser";

    private static final DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    /**
	 * Count members being online.
	 * @return names of members being online
	 */
    public List<String> getOnlineMembers() {
        List<String> onlineMembers = new ArrayList<String>();
        Collection<HttpSession> sessions = AtarrabiServletListener.getSessions().values();
        for (HttpSession httpSession : sessions) {
            String username = (String) httpSession.getAttribute(SESSION_ATTR_USER);
            if (username != null) {
                onlineMembers.add(username + " (Login: " + DF.format(new Date(httpSession.getCreationTime())) + ")");
            }
        }
        return onlineMembers;
    }
}
