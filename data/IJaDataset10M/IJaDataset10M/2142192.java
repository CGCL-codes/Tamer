package DAOs;

import DAOs.exceptions.IllegalOrphanException;
import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import DAOs.exceptions.RollbackFailureException;
import beans.Person;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import beans.Candidate;
import beans.Employee;
import beans.Study;
import beans.Town;
import beans.SecondaryStudies;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ioana C
 */
public class PersonJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "Licenta_PU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (person.getSecondaryStudiesCollection() == null) {
            person.setSecondaryStudiesCollection(new ArrayList<SecondaryStudies>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Candidate candidate = person.getCandidate();
            if (candidate != null) {
                candidate = em.getReference(candidate.getClass(), candidate.getPersonID());
                person.setCandidate(candidate);
            }
            Employee employee = person.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getPersonID());
                person.setEmployee(employee);
            }
            Study mainStudyID = person.getMainStudyID();
            if (mainStudyID != null) {
                mainStudyID = em.getReference(mainStudyID.getClass(), mainStudyID.getStudyID());
                person.setMainStudyID(mainStudyID);
            }
            Town townID = person.getTownID();
            if (townID != null) {
                townID = em.getReference(townID.getClass(), townID.getTownID());
                person.setTownID(townID);
            }
            List<SecondaryStudies> attachedSecondaryStudiesCollection = new ArrayList<SecondaryStudies>();
            for (SecondaryStudies secondaryStudiesCollectionSecondaryStudiesToAttach : person.getSecondaryStudiesCollection()) {
                secondaryStudiesCollectionSecondaryStudiesToAttach = em.getReference(secondaryStudiesCollectionSecondaryStudiesToAttach.getClass(), secondaryStudiesCollectionSecondaryStudiesToAttach.getSecondaryStudiesPK());
                attachedSecondaryStudiesCollection.add(secondaryStudiesCollectionSecondaryStudiesToAttach);
            }
            person.setSecondaryStudiesCollection(attachedSecondaryStudiesCollection);
            em.persist(person);
            if (candidate != null) {
                Person oldPersonOfCandidate = candidate.getPerson();
                if (oldPersonOfCandidate != null) {
                    oldPersonOfCandidate.setCandidate(null);
                    oldPersonOfCandidate = em.merge(oldPersonOfCandidate);
                }
                candidate.setPerson(person);
                candidate = em.merge(candidate);
            }
            if (employee != null) {
                Person oldPersonOfEmployee = employee.getPerson();
                if (oldPersonOfEmployee != null) {
                    oldPersonOfEmployee.setEmployee(null);
                    oldPersonOfEmployee = em.merge(oldPersonOfEmployee);
                }
                employee.setPerson(person);
                employee = em.merge(employee);
            }
            if (mainStudyID != null) {
                mainStudyID.getPersonCollection().add(person);
                mainStudyID = em.merge(mainStudyID);
            }
            if (townID != null) {
                townID.getPersonCollection().add(person);
                townID = em.merge(townID);
            }
            for (SecondaryStudies secondaryStudiesCollectionSecondaryStudies : person.getSecondaryStudiesCollection()) {
                Person oldPersonOfSecondaryStudiesCollectionSecondaryStudies = secondaryStudiesCollectionSecondaryStudies.getPerson();
                secondaryStudiesCollectionSecondaryStudies.setPerson(person);
                secondaryStudiesCollectionSecondaryStudies = em.merge(secondaryStudiesCollectionSecondaryStudies);
                if (oldPersonOfSecondaryStudiesCollectionSecondaryStudies != null) {
                    oldPersonOfSecondaryStudiesCollectionSecondaryStudies.getSecondaryStudiesCollection().remove(secondaryStudiesCollectionSecondaryStudies);
                    oldPersonOfSecondaryStudiesCollectionSecondaryStudies = em.merge(oldPersonOfSecondaryStudiesCollectionSecondaryStudies);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPerson(person.getPersonID()) != null) {
                throw new PreexistingEntityException("Person " + person + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Person persistentPerson = em.find(Person.class, person.getPersonID());
            Candidate candidateOld = persistentPerson.getCandidate();
            Candidate candidateNew = person.getCandidate();
            Employee employeeOld = persistentPerson.getEmployee();
            Employee employeeNew = person.getEmployee();
            Study mainStudyIDOld = persistentPerson.getMainStudyID();
            Study mainStudyIDNew = person.getMainStudyID();
            Town townIDOld = persistentPerson.getTownID();
            Town townIDNew = person.getTownID();
            List<SecondaryStudies> secondaryStudiesCollectionOld = persistentPerson.getSecondaryStudiesCollection();
            List<SecondaryStudies> secondaryStudiesCollectionNew = person.getSecondaryStudiesCollection();
            List<String> illegalOrphanMessages = null;
            if (candidateOld != null && !candidateOld.equals(candidateNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Candidate " + candidateOld + " since its person field is not nullable.");
            }
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Employee " + employeeOld + " since its person field is not nullable.");
            }
            for (SecondaryStudies secondaryStudiesCollectionOldSecondaryStudies : secondaryStudiesCollectionOld) {
                if (!secondaryStudiesCollectionNew.contains(secondaryStudiesCollectionOldSecondaryStudies)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SecondaryStudies " + secondaryStudiesCollectionOldSecondaryStudies + " since its person field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (candidateNew != null) {
                candidateNew = em.getReference(candidateNew.getClass(), candidateNew.getPersonID());
                person.setCandidate(candidateNew);
            }
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getPersonID());
                person.setEmployee(employeeNew);
            }
            if (mainStudyIDNew != null) {
                mainStudyIDNew = em.getReference(mainStudyIDNew.getClass(), mainStudyIDNew.getStudyID());
                person.setMainStudyID(mainStudyIDNew);
            }
            if (townIDNew != null) {
                townIDNew = em.getReference(townIDNew.getClass(), townIDNew.getTownID());
                person.setTownID(townIDNew);
            }
            List<SecondaryStudies> attachedSecondaryStudiesCollectionNew = new ArrayList<SecondaryStudies>();
            for (SecondaryStudies secondaryStudiesCollectionNewSecondaryStudiesToAttach : secondaryStudiesCollectionNew) {
                secondaryStudiesCollectionNewSecondaryStudiesToAttach = em.getReference(secondaryStudiesCollectionNewSecondaryStudiesToAttach.getClass(), secondaryStudiesCollectionNewSecondaryStudiesToAttach.getSecondaryStudiesPK());
                attachedSecondaryStudiesCollectionNew.add(secondaryStudiesCollectionNewSecondaryStudiesToAttach);
            }
            secondaryStudiesCollectionNew = attachedSecondaryStudiesCollectionNew;
            person.setSecondaryStudiesCollection(secondaryStudiesCollectionNew);
            person = em.merge(person);
            if (candidateNew != null && !candidateNew.equals(candidateOld)) {
                Person oldPersonOfCandidate = candidateNew.getPerson();
                if (oldPersonOfCandidate != null) {
                    oldPersonOfCandidate.setCandidate(null);
                    oldPersonOfCandidate = em.merge(oldPersonOfCandidate);
                }
                candidateNew.setPerson(person);
                candidateNew = em.merge(candidateNew);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                Person oldPersonOfEmployee = employeeNew.getPerson();
                if (oldPersonOfEmployee != null) {
                    oldPersonOfEmployee.setEmployee(null);
                    oldPersonOfEmployee = em.merge(oldPersonOfEmployee);
                }
                employeeNew.setPerson(person);
                employeeNew = em.merge(employeeNew);
            }
            if (mainStudyIDOld != null && !mainStudyIDOld.equals(mainStudyIDNew)) {
                mainStudyIDOld.getPersonCollection().remove(person);
                mainStudyIDOld = em.merge(mainStudyIDOld);
            }
            if (mainStudyIDNew != null && !mainStudyIDNew.equals(mainStudyIDOld)) {
                mainStudyIDNew.getPersonCollection().add(person);
                mainStudyIDNew = em.merge(mainStudyIDNew);
            }
            if (townIDOld != null && !townIDOld.equals(townIDNew)) {
                townIDOld.getPersonCollection().remove(person);
                townIDOld = em.merge(townIDOld);
            }
            if (townIDNew != null && !townIDNew.equals(townIDOld)) {
                townIDNew.getPersonCollection().add(person);
                townIDNew = em.merge(townIDNew);
            }
            for (SecondaryStudies secondaryStudiesCollectionNewSecondaryStudies : secondaryStudiesCollectionNew) {
                if (!secondaryStudiesCollectionOld.contains(secondaryStudiesCollectionNewSecondaryStudies)) {
                    Person oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies = secondaryStudiesCollectionNewSecondaryStudies.getPerson();
                    secondaryStudiesCollectionNewSecondaryStudies.setPerson(person);
                    secondaryStudiesCollectionNewSecondaryStudies = em.merge(secondaryStudiesCollectionNewSecondaryStudies);
                    if (oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies != null && !oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies.equals(person)) {
                        oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies.getSecondaryStudiesCollection().remove(secondaryStudiesCollectionNewSecondaryStudies);
                        oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies = em.merge(oldPersonOfSecondaryStudiesCollectionNewSecondaryStudies);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getPersonID();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getPersonID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Candidate candidateOrphanCheck = person.getCandidate();
            if (candidateOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Candidate " + candidateOrphanCheck + " in its candidate field has a non-nullable person field.");
            }
            Employee employeeOrphanCheck = person.getEmployee();
            if (employeeOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Employee " + employeeOrphanCheck + " in its employee field has a non-nullable person field.");
            }
            List<SecondaryStudies> secondaryStudiesCollectionOrphanCheck = person.getSecondaryStudiesCollection();
            for (SecondaryStudies secondaryStudiesCollectionOrphanCheckSecondaryStudies : secondaryStudiesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the SecondaryStudies " + secondaryStudiesCollectionOrphanCheckSecondaryStudies + " in its secondaryStudiesCollection field has a non-nullable person field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Study mainStudyID = person.getMainStudyID();
            if (mainStudyID != null) {
                mainStudyID.getPersonCollection().remove(person);
                mainStudyID = em.merge(mainStudyID);
            }
            Town townID = person.getTownID();
            if (townID != null) {
                townID.getPersonCollection().remove(person);
                townID = em.merge(townID);
            }
            em.remove(person);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Person as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Person as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
