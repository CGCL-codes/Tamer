package lt.bsprendimai.ddesk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.faces.component.UIParameter;
import javax.faces.model.SelectItem;
import lt.bsprendimai.ddesk.dao.CertificateEntry;
import lt.bsprendimai.ddesk.dao.Company;
import lt.bsprendimai.ddesk.dao.CompanyContract;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Backing bean and logic for working with clients and users. <br/>
 * Client is {@link Company} and user is {@link Person}.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ClientAccessor implements Serializable {

    private UserHandler userHandler;

    private Company selected = new Company();

    private CompanyContract contract = new CompanyContract();

    private Person person = new Person();

    private Integer companyId;

    private Integer personId;

    private String pwd1;

    private String pwd2;

    private Integer language = 1;

    private transient UIParameter clientIdParameter = new UIParameter();

    private transient UIParameter personIdParameter = new UIParameter();

    /** Creates a new instance of ClientAccessor */
    public ClientAccessor() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        if (!userHandler.isOwner()) return;
        if (ChangeDetector.safeEquals(companyId, this.companyId)) return;
        this.companyId = companyId;
        if (companyId == null || companyId < 0) {
            this.selected = new Company();
            this.contract = new CompanyContract();
            return;
        }
        try {
            this.selected = (Company) SessionHolder.currentSession().getSess().createQuery(" FROM " + Company.class.getName() + " m WHERE m.id = ?").setInteger(0, companyId).setMaxResults(1).uniqueResult();
            this.contract = (CompanyContract) SessionHolder.currentSession().getSess().createQuery(" FROM " + CompanyContract.class.getName() + " k WHERE k.company = ? ").setInteger(0, companyId).setMaxResults(1).uniqueResult();
            if (this.contract == null) this.contract = new CompanyContract();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            this.selected = null;
        }
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        if (!userHandler.isOwner()) return;
        if (ChangeDetector.safeEquals(personId, this.personId)) return;
        this.personId = personId;
        if (personId == null || personId < 0) {
            this.person = new Person();
            return;
        }
        try {
            this.person = (Person) SessionHolder.currentSession().getSess().createQuery(" FROM " + Person.class.getName() + " WHERE id = ?").setInteger(0, personId).uniqueResult();
            for (Entry<Integer, String> c : ParameterAccess.getLanguages().entrySet()) {
                if (c.getValue().equals(person.getLanguage())) this.setLanguage(c.getKey());
            }
            this.person.setLoginChange(false);
            setCompanyId(person.getCompany());
        } catch (HibernateException ex) {
            this.person = null;
        }
    }

    public Company getSelected() {
        return selected;
    }

    public void setSelected(Company selected) {
        this.selected = selected;
    }

    public CompanyContract getContract() {
        return contract;
    }

    public void setContract(CompanyContract contract) {
        this.contract = contract;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @SuppressWarnings("unchecked")
    public List<Company> getListCompanies() {
        if (!userHandler.isOwner()) return new ArrayList<Company>(0);
        try {
            return (List<Company>) SessionHolder.currentSession().getSess().createQuery("from " + Company.class.getName() + " t  WHERE t.id >= 0 ORDER BY t.id ").list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<Company>(0);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Person> getListPersons() {
        if (!userHandler.isOwner()) return new ArrayList<Person>(0);
        try {
            return (List<Person>) SessionHolder.currentSession().getSess().createQuery("from " + Person.class.getName() + " t WHERE t.company = ? AND t.company >= 0 AND t.id <> 0 ORDER BY t.id ").setInteger(0, this.companyId).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<Person>(1);
        }
    }

    public String updatePerson() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            String message;
            if ((userHandler.getUser().getLoginLevel() == null || userHandler.getUser().getLoginLevel() != 0) && person.getId() != userHandler.getUser().getId()) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.unsaved");
                UIMessenger.addInfoMessage(message, "");
                return StandardResults.FAIL;
            }
            if (pwd1 != null && !pwd1.equals("")) {
                if (!pwd1.equals(pwd2)) {
                    message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.passwordsDoNotMatch");
                    UIMessenger.addErrorMessage(message, "");
                    return StandardResults.FAIL;
                }
                String password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(pwd2.getBytes())).toString(16);
                person.setPassword("");
                if (password.length() < 32) {
                    for (int i = (32 - password.length()); i > 0; i--) {
                        password = "0" + password;
                    }
                }
                person.setPassword(password);
            }
            String lang = ParameterAccess.getLanguages().get(getLanguage()).toLowerCase();
            person.setLanguage(lang);
            if (person.isLoginChange()) {
                Session sess = SessionHolder.currentSession().getSess();
                sess.evict(this.person);
                int i = (Integer) sess.createQuery("SELECT count(*) FROM " + Person.class.getName() + " e WHERE e.loginCode = ? ").setString(0, person.getLoginCode()).iterate().next();
                if (i > 0) {
                    message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.loginExists");
                    UIMessenger.addErrorMessage(message, "");
                    return StandardResults.FAIL;
                } else {
                    this.person.setLoginChange(false);
                }
            }
            if (!person.update().equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                return StandardResults.FAIL;
            }
            return StandardResults.SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }

    public String addPerson() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            String message;
            if ((userHandler.getUser().getLoginLevel() == null || userHandler.getUser().getLoginLevel() != 0) && this.getCompanyId() == 0) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.unsaved");
                UIMessenger.addInfoMessage(message, "");
                return StandardResults.FAIL;
            }
            if (!pwd1.equals(pwd2)) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.passwordsDoNotMatch");
                UIMessenger.addErrorMessage(message, "");
                return StandardResults.FAIL;
            }
            String password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(pwd2.getBytes())).toString(16);
            person.setPassword("");
            if (password.length() < 32) {
                for (int i = (32 - password.length()); i > 0; i--) {
                    password = "0" + password;
                }
            }
            person.setPassword(password);
            person.setCompany(this.getCompanyId());
            String lang = ParameterAccess.getLanguages().get(getLanguage()).toLowerCase();
            person.setLanguage(lang);
            Session sess = SessionHolder.currentSession().getSess();
            sess.evict(this.person);
            int i = (Integer) sess.createQuery("SELECT count(*) FROM " + Person.class.getName() + " e WHERE e.loginCode = ? ").setString(0, person.getLoginCode()).iterate().next();
            if (i > 0) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.loginExists");
                UIMessenger.addErrorMessage(message, "");
                return StandardResults.FAIL;
            }
            person.setLastLogin(new Date());
            if (!person.add().equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                return StandardResults.FAIL;
            }
            this.person = new Person();
            return StandardResults.SUCCESS;
        } catch (Exception ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }

    @SuppressWarnings("unchecked")
    public String cancelCertificates() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            List<CertificateEntry> lce = (List<CertificateEntry>) SessionHolder.currentSession().getSess().createQuery("FROM " + CertificateEntry.class.getName() + " WHERE person = ? AND valid = true ").setInteger(0, getPersonId()).list();
            for (CertificateEntry cea : lce) {
                cea.setValid(false);
                cea.update();
            }
        } catch (Exception ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            UIMessenger.addErrorMessage(ex.getMessage(), "");
        }
        return StandardResults.STAY;
    }

    public String deletePerson() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            Person cmp = (Person) SessionHolder.currentSession().getSess().get(Person.class, (Serializable) this.getPersonIdParameter().getValue());
            this.getPersonIdParameter().setValue(null);
            SessionHolder.currentSession().getSess().delete(cmp);
            SessionHolder.currentSession().getSess().flush();
            return StandardResults.SUCCESS;
        } catch (HibernateException ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }

    public String getPwd1() {
        return pwd1;
    }

    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }

    public String addCompany() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        String ret = selected.add();
        if (ret.equals(StandardResults.SUCCESS)) {
            contract.setCompany(selected.getId());
            ret = contract.add();
            if (!ret.equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                String message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.clients.unsaved");
                UIMessenger.addErrorMessage(message, "");
            }
            this.selected = new Company();
            this.contract = new CompanyContract();
        } else {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            String message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.clients.unsaved");
            UIMessenger.addErrorMessage(message, "");
        }
        return ret;
    }

    public String updateCompany() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        String ret = selected.update();
        pwd1 = selected.getNotes();
        if (ret.equals(StandardResults.SUCCESS)) {
            ret = contract.update();
            if (!ret.equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                String message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.clients.unsaved");
                UIMessenger.addErrorMessage(message, "");
            }
        } else {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            String message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.clients.unsaved");
            UIMessenger.addErrorMessage(message, "");
        }
        return ret;
    }

    public String deleteCompany() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            Company cmp = (Company) SessionHolder.currentSession().getSess().get(Company.class, (Serializable) clientIdParameter.getValue());
            SessionHolder.currentSession().getSess().delete(cmp);
            SessionHolder.currentSession().getSess().flush();
            return StandardResults.CLIENTList;
        } catch (HibernateException ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getPersonSelect() {
        List<Person> la = new ArrayList<Person>(0);
        try {
            la = (List<Person>) SessionHolder.currentSession().getSess().createQuery("from " + Person.class.getName() + " t WHERE t.company = ? AND t.company >= 0 AND t.id <> 0 ORDER BY t.id ").setInteger(0, this.companyId).list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si;
        for (Person c : la) {
            si = new SelectItem();
            si.setValue(c.getId().intValue());
            si.setLabel(c.getName());
            ll.add(si);
        }
        return ll;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCompanysPersons() {
        List<Person> la = new ArrayList<Person>(0);
        try {
            la = (List<Person>) SessionHolder.currentSession().getSess().createQuery("from " + Person.class.getName() + " t WHERE t.company = ? AND t.company >= 0 AND t.id <> 0 ORDER BY t.id ").setInteger(0, companyId).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si = new SelectItem();
        si.setValue("");
        si.setLabel("");
        ll.add(si);
        for (Person c : la) {
            si = new SelectItem();
            si.setValue(c.getId().intValue());
            si.setLabel(c.getName());
            ll.add(si);
        }
        return ll;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCompanies() {
        List<Company> la = new ArrayList<Company>(0);
        try {
            if (!userHandler.isOwner()) {
                la = (List<Company>) SessionHolder.currentSession().getSess().createQuery("from " + Company.class.getName() + " t  WHERE t.id >= 0  ORDER BY t.id ").list();
            } else {
                la = (List<Company>) SessionHolder.currentSession().getSess().createQuery("from " + Company.class.getName() + " t  WHERE t.id = ? OR t.id = 0 ORDER BY t.id ").setInteger(0, userHandler.getUser().getCompany()).list();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si = new SelectItem();
        si.setValue("");
        si.setLabel("");
        ll.add(si);
        for (Company c : la) {
            si = new SelectItem();
            si.setValue(c.getId().intValue());
            si.setLabel(c.getName());
            ll.add(si);
        }
        return ll;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAssignSelect() {
        List<Person> la = new ArrayList<Person>(0);
        try {
            la = (List<Person>) SessionHolder.currentSession().getSess().createQuery("from " + Person.class.getName() + " t WHERE t.company = 0 AND t.id <> 0 ORDER BY t.id ").list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si = new SelectItem();
        si.setValue("");
        si.setLabel("");
        ll.add(si);
        for (Person c : la) {
            si = new SelectItem();
            si.setValue(c.getId().intValue());
            si.setLabel(c.getName());
            ll.add(si);
        }
        try {
            la = (List<Person>) SessionHolder.currentSession().getSess().createQuery("from " + Person.class.getName() + " t WHERE (t.loginLevel = ? ) AND t.id <> 0 ORDER BY t.id ").setInteger(0, Person.PARTNER).list();
        } catch (Exception ex) {
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            ex.printStackTrace();
        }
        si = new SelectItem();
        si.setValue("");
        si.setLabel("------------------");
        si.setDisabled(true);
        ll.add(si);
        for (Person c : la) {
            si = new SelectItem();
            si.setValue(c.getId().intValue());
            si.setLabel(c.getName());
            ll.add(si);
        }
        si = new SelectItem();
        si.setValue(new Integer(-1));
        si.setLabel("N/A");
        ll.add(si);
        si = new SelectItem();
        si.setValue(new Integer(-2));
        si.setLabel("Any");
        ll.add(si);
        return ll;
    }

    public List<SelectItem> getLanguageSelect() {
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si;
        for (Entry<Integer, String> c : ParameterAccess.getLanguages().entrySet()) {
            si = new SelectItem();
            si.setValue(c.getKey());
            si.setLabel(c.getValue());
            ll.add(si);
        }
        return ll;
    }

    public List<SelectItem> getPersonLevelSelect() {
        List<SelectItem> ll = new LinkedList<SelectItem>();
        SelectItem si = new SelectItem();
        si.setValue(new Integer(Person.ADMIN));
        si.setLabel(UIMessenger.getMessage(this.userHandler.getUserLocale(), "pplication.person.loginLevel.admin"));
        ll.add(si);
        si = new SelectItem();
        si.setValue(new Integer(Person.OTHER));
        si.setLabel(UIMessenger.getMessage(this.userHandler.getUserLocale(), "pplication.person.loginLevel.other"));
        ll.add(si);
        si = new SelectItem();
        si.setValue(new Integer(Person.PARTNER));
        si.setLabel(UIMessenger.getMessage(this.userHandler.getUserLocale(), "pplication.person.loginLevel.partner"));
        ll.add(si);
        return ll;
    }

    public UIParameter getClientIdParameter() {
        return clientIdParameter;
    }

    public void setClientIdParameter(UIParameter clientIdParameter) {
        this.clientIdParameter = clientIdParameter;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public UIParameter getPersonIdParameter() {
        return personIdParameter;
    }

    public void setPersonIdParameter(UIParameter personIdParameter) {
        this.personIdParameter = personIdParameter;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    /**
     * Will reset clientIdParameter and personIdParameter.
     * For serialization purposes, see {@link Serializable}.
     *
     * @param s
     * @throws IOException
     */
    private void readObject(ObjectInputStream s) throws IOException {
        try {
            s.defaultReadObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex.getMessage());
        }
        clientIdParameter = new UIParameter();
        personIdParameter = new UIParameter();
    }
}
