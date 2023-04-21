package br.com.mcampos.ejb.cloudsystem.user.person.session;

import br.com.mcampos.ejb.cloudsystem.locality.city.session.CitySessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.address.entity.Address;
import br.com.mcampos.ejb.cloudsystem.user.address.session.AddressSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.attribute.civilstate.entity.CivilState;
import br.com.mcampos.ejb.cloudsystem.user.attribute.civilstate.session.CivilStateSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.attribute.gender.session.GenderSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.attribute.title.session.TitleSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.attribute.usertype.entity.entity.UserType;
import br.com.mcampos.ejb.cloudsystem.user.attribute.usertype.session.UserTypeSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.contact.entity.UserContact;
import br.com.mcampos.ejb.cloudsystem.user.contact.session.UserContactSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.document.entity.UserDocument;
import br.com.mcampos.ejb.cloudsystem.user.document.session.UserDocumentSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.person.entity.Person;
import br.com.mcampos.ejb.session.core.Crud;
import br.com.mcampos.exception.ApplicationException;
import br.com.mcampos.sysutils.SysUtils;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name = "NewPersonSession", mappedName = "CloudSystems-EjbPrj-NewPersonSession")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class NewPersonSessionBean extends Crud<Integer, Person> implements NewPersonSessionLocal {

    @EJB
    UserTypeSessionLocal userTypeSession;

    @EJB
    private CivilStateSessionLocal civilStateSession;

    @EJB
    private CitySessionLocal citySession;

    @EJB
    private GenderSessionLocal genderSession;

    @EJB
    private TitleSessionLocal titleSession;

    @EJB
    private AddressSessionLocal addressSession;

    @EJB
    private UserDocumentSessionLocal userDocumentSession;

    @EJB
    private UserContactSessionLocal userContactSesion;

    public NewPersonSessionBean() {
    }

    public void delete(Integer key) throws ApplicationException {
        delete(Person.class, key);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Person get(Integer key) throws ApplicationException {
        return get(Person.class, key);
    }

    @Override
    public Person add(Person entity) throws ApplicationException {
        String splitted[] = splitName(entity.getName());
        entity.setFirstName(splitted[0]);
        entity.setMiddleName(splitted[1]);
        entity.setLastName(splitted[2]);
        applyRules(entity);
        linkToEntities(entity);
        return super.add(entity);
    }

    @Override
    public Person update(Person entity) throws ApplicationException {
        linkToEntities(entity);
        return super.update(entity);
    }

    protected void linkToEntities(Person person) throws ApplicationException {
        if (person.getUserType() != null) {
            UserType type = getEntityManager().find(UserType.class, "1");
            person.setUserType(type);
        }
        if (person.getCivilState() != null) {
            CivilState cs = civilStateSession.get(person.getCivilState().getId());
            person.setCivilState(cs);
        }
        if (person.getGender() != null) person.setGender(genderSession.get(person.getGender().getId()));
        if (person.getTitle() != null) person.setTitle(titleSession.get(person.getTitle().getId()));
        if (person.getBornCity() != null) person.setBornCity(citySession.get(person.getBornCity().getId()));
    }

    protected void updateAddresses(Person person) throws ApplicationException {
        if (SysUtils.isEmpty(person.getAddresses())) return;
        addressSession.delete(person);
        for (Address addr : person.getAddresses()) {
            Address managed = addressSession.add(addr);
            person.getAddresses().set(person.getAddresses().indexOf(addr), managed);
        }
    }

    protected void updateDocuments(Person person) throws ApplicationException {
        if (SysUtils.isEmpty(person.getDocuments())) return;
        userDocumentSession.delete(person);
        for (UserDocument addr : person.getDocuments()) {
            UserDocument managed = userDocumentSession.add(addr);
            person.getDocuments().set(person.getDocuments().indexOf(addr), managed);
        }
    }

    protected void updateContacts(Person person) throws ApplicationException {
        if (SysUtils.isEmpty(person.getContacts())) return;
        userContactSesion.delete(person);
        for (UserContact addr : person.getContacts()) {
            UserContact managed = userContactSesion.add(addr);
            person.getContacts().set(person.getContacts().indexOf(addr), managed);
        }
    }

    public Person find(Person targetPerson) throws ApplicationException {
        if (targetPerson == null) return null;
        if (targetPerson.getId() != null) {
            return get(targetPerson.getId());
        }
        List<UserDocument> documents = targetPerson.getDocuments();
        if (SysUtils.isEmpty(documents)) return null;
        for (UserDocument document : documents) {
            UserDocument doc = userDocumentSession.find(document);
            if (doc != null) return (Person) doc.getUser();
        }
        return null;
    }

    protected void applyRules(Person person) {
        person.setFatherName(SysUtils.toUpperCase(person.getFatherName()));
        person.setFirstName(SysUtils.toUpperCase(person.getFirstName()));
        person.setLastName(SysUtils.toUpperCase(person.getLastName()));
        person.setMotherName(SysUtils.toUpperCase(person.getMotherName()));
        person.setName(SysUtils.toUpperCase(person.getName()));
        person.setNickName(SysUtils.toUpperCase(person.getNickName()));
    }

    protected String[] splitName(String name) {
        String splitted[] = name.split(" ");
        String firstName = null, lastName = null, middleName = null;
        int nIndex;
        for (nIndex = 0; nIndex < splitted.length; nIndex++) {
            if (nIndex == 0) firstName = splitted[nIndex]; else if (nIndex == splitted.length - 1) lastName = splitted[nIndex]; else {
                if (middleName == null || middleName.isEmpty()) middleName = splitted[nIndex]; else middleName += " " + splitted[nIndex];
            }
        }
        splitted = new String[3];
        splitted[0] = firstName;
        splitted[1] = middleName;
        splitted[2] = lastName;
        return splitted;
    }
}
