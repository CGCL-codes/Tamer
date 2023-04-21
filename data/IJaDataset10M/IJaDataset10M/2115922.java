package frontend.ApplicationFrontEnd.ebm.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import frontend.ApplicationFrontEnd.ebm.vo.OrganizationVO;
import frontend.ApplicationFrontEnd.ebm.vo.PersonVO;

/**
 * Bean usually stored in the session
 * 
 * @author mcarpentier - EBM Websourcing
 * 
 */
public class UserSession {

    private Map<String, OrganizationVO> organizations;

    private int clearCount;

    public Map<String, OrganizationVO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Map<String, OrganizationVO> organizations) {
        this.organizations = organizations;
    }

    public void addOrganization(OrganizationVO orga) {
        if (this.organizations == null) {
            this.organizations = new HashMap<String, OrganizationVO>();
        }
        if (this.organizations.containsKey(orga.getOrgId())) {
            this.organizations.remove(orga.getOrgId());
        }
        this.organizations.put(orga.getOrgId(), orga);
        System.out.println("usersession ->" + orga.getOrgId());
    }

    public void removeOrganization(String id) {
        if (this.organizations != null) {
            if (this.organizations.containsKey(id)) {
                OrganizationVO orga = this.organizations.get(id);
                this.organizations.remove(id);
            }
        }
    }

    private Map<String, PersonVO> persons;

    public Map<String, PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(Map<String, PersonVO> persons) {
        this.persons = persons;
    }

    public void addPerson(PersonVO person) {
        if (this.persons == null) {
            this.persons = new HashMap<String, PersonVO>();
        }
        if (this.persons.containsKey(person.getId())) {
            this.persons.remove(person.getId());
        }
        this.persons.put(person.getId(), person);
    }

    public void removePerson(String id) {
        if (this.persons != null) {
            if (this.persons.containsKey(id)) {
                PersonVO person = this.persons.get(id);
                this.persons.remove(id);
            }
        }
    }

    public static UserSession getUserSession(HttpSession iSession) {
        UserSession session = (UserSession) iSession.getAttribute("UserSession");
        if (session == null) {
            session = new UserSession();
            iSession.setAttribute("UserSession", session);
        }
        return session;
    }

    /**
     * reset session
     */
    public void reset() {
        this.organizations = null;
        this.persons = null;
    }

    public int getClearCount() {
        return this.clearCount;
    }

    public void incClearCount() {
        this.clearCount = this.clearCount + 1;
    }
}
