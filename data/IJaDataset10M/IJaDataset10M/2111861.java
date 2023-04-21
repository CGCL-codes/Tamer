package remote;

import entity.StudentAssignment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class StudentAssignmentFacade extends AbstractFacade<StudentAssignment> implements StudentAssignmentFacadeRemote {

    @PersistenceContext(unitName = "ejb_pro-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentAssignmentFacade() {
        super(StudentAssignment.class);
    }
}
