package dukestutoring.ejb;

import dukestutoring.entity.Address;
import dukestutoring.entity.Address_;
import dukestutoring.entity.Guardian;
import dukestutoring.entity.Person;
import dukestutoring.entity.Student;
import dukestutoring.entity.Student_;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;

/**
 *
 * @author ian
 */
@Path("/tutoring/admin")
@Stateless
@Named
public class AdminBean {

    private static final Logger logger = Logger.getLogger("dukestutoring.ejb.RequestBean");

    private CriteriaBuilder cb;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    private void init() {
        cb = em.getCriteriaBuilder();
    }

    public String createStudent(Student student) {
        em.persist(student);
        return "createdStudent";
    }

    public void createStudents(List<Student> students) {
        Iterator<Student> i = students.iterator();
        while (i.hasNext()) {
            this.createStudent(i.next());
        }
    }

    public String editStudent(Student student) {
        em.merge(student);
        return "editedStudent";
    }

    public void editStudents(List<Student> students) {
        Iterator<Student> i = students.iterator();
        while (i.hasNext()) {
            this.editStudent(i.next());
        }
    }

    public String removeStudent(Student student) {
        student.setActive(false);
        em.merge(student);
        return "removedStudent";
    }

    public void removeStudents(List<Student> students) {
        Iterator<Student> i = students.iterator();
        while (i.hasNext()) {
            this.removeStudent(i.next());
        }
    }

    public String createGuardian(Guardian guardian, Student student) {
        student.getGuardians().add(guardian);
        guardian.getStudents().add(student);
        em.merge(student);
        em.persist(guardian);
        return "createdGuardian";
    }

    public String createGuardian(Guardian guardian, List<Student> students) {
        Iterator<Student> i = students.iterator();
        while (i.hasNext()) {
            Student student = i.next();
            student.getGuardians().add(guardian);
            guardian.getStudents().add(student);
            em.merge(student);
        }
        em.persist(guardian);
        return "createdGuardian";
    }

    public String editGuardian(Guardian guardian) {
        em.merge(guardian);
        return "editedGuardian";
    }

    public void editGuardians(List<Guardian> guardians) {
        Iterator<Guardian> i = guardians.iterator();
        while (i.hasNext()) {
            this.editGuardian(i.next());
        }
    }

    public String removeGuardian(Guardian guardian) {
        guardian.setActive(false);
        List<Student> students = guardian.getStudents();
        Iterator<Student> i = students.iterator();
        while (i.hasNext()) {
            Student student = i.next();
            student.getGuardians().remove(guardian);
            em.merge(student);
        }
        em.merge(guardian);
        return "removedGuardian";
    }

    public void removeGuardians(List<Guardian> guardians) {
        Iterator<Guardian> i = guardians.iterator();
        while (i.hasNext()) {
            this.removeGuardian(i.next());
        }
    }

    public void addGuardiansToStudent(List<Guardian> guardians, Student student) {
        Iterator<Guardian> i = guardians.iterator();
        while (i.hasNext()) {
            Guardian guardian = i.next();
            student.getGuardians().add(guardian);
            guardian.getStudents().add(student);
            em.merge(guardian);
        }
        em.merge(student);
    }

    public String createAddress(Address address, Person person) {
        person.getAddresses().add(address);
        address.setPerson(person);
        em.merge(person);
        em.persist(address);
        return "createdAddress";
    }

    public String editAddress(Address address) {
        em.merge(address);
        return "editedAddress";
    }

    public void editAddresses(List<Address> addresses) {
        Iterator<Address> i = addresses.iterator();
        while (i.hasNext()) {
            this.editAddress(i.next());
        }
    }

    public String removeAddress(Address address) {
        address.setActive(false);
        Person person = address.getPerson();
        person.getAddresses().remove(address);
        em.merge(person);
        em.merge(address);
        return "removedAddress";
    }

    public void removeAddresses(List<Address> addresses) {
        Iterator<Address> i = addresses.iterator();
        while (i.hasNext()) {
            this.removeAddress(i.next());
        }
    }

    public List<Guardian> getAllGuardians() {
        CriteriaQuery<Guardian> cq = em.getCriteriaBuilder().createQuery(Guardian.class);
        Root<Guardian> guardian = cq.from(Guardian.class);
        cq.select(guardian);
        cq.distinct(true);
        TypedQuery<Guardian> q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<Address> getAllAddresses() {
        CriteriaQuery<Address> cq = em.getCriteriaBuilder().createQuery(Address.class);
        Root<Address> address = cq.from(Address.class);
        cq.select(address);
        cq.where(cb.isTrue(address.get(Address_.active)));
        cq.distinct(true);
        TypedQuery<Address> q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<Student> getAllInactiveStudents() {
        CriteriaQuery<Student> cq = em.getCriteriaBuilder().createQuery(Student.class);
        Root<Student> student = cq.from(Student.class);
        cq.select(student);
        cq.where(cb.isFalse(student.get(Student_.active)));
        cq.distinct(true);
        TypedQuery<Student> q = em.createQuery(cq);
        return q.getResultList();
    }

    public void activateStudent(Student student) {
        student.setActive(true);
        em.merge(student);
    }

    public Student findStudentById(Long id) {
        CriteriaQuery<Student> cq = em.getCriteriaBuilder().createQuery(Student.class);
        Root<Student> student = cq.from(Student.class);
        cq.select(student);
        cq.where(cb.isTrue(student.get(Student_.active)));
        cq.where(cb.equal(student.get(Student_.id), id));
        cq.distinct(true);
        TypedQuery<Student> q = em.createQuery(cq);
        return q.getSingleResult();
    }
}
