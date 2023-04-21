package de.objectcode.time4u.server.web;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.objectcode.time4u.server.api.ACL;
import de.objectcode.time4u.server.api.PersonService;
import de.objectcode.time4u.server.api.Team;

public class ACLBean {

    private static final Log log = LogFactory.getLog(ACLBean.class);

    private ACL m_acl;

    public ACL getAcl() {
        if (m_acl == null) {
            try {
                m_acl = EJBHelper.getPersonService().getACL();
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
        return m_acl;
    }

    public List<SelectItem> getAllowedTeams() {
        ACL acl = getAcl();
        List<SelectItem> teams = new ArrayList<SelectItem>();
        try {
            PersonService personService = EJBHelper.getPersonService();
            teams.add(new SelectItem(-1L, ""));
            for (Team team : personService.getAllTeams()) {
                if (acl.isCanAdmin() || (team.getOwnerId() == m_acl.getAuthPerson().getId()) || (acl.getTeamAccess().get(team.getId()) != null && acl.getTeamAccess().get(team.getId()).isCanRead())) teams.add(new SelectItem(team.getId(), team.getName()));
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return teams;
    }
}
