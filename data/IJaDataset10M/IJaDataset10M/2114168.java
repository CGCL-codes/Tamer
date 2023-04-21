package wilos.business.services.misc.concreteiteration;

import java.util.HashSet;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.hibernate.misc.concreteiteration.ConcreteIterationDao;
import wilos.model.misc.concretebreakdownelement.ConcreteBreakdownElement;
import wilos.model.misc.concreteiteration.ConcreteIteration;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteIterationService {

    public ConcreteIterationDao concreteIterationDao;

    /**
	 * Allows to get the concreteIteration with the id
	 * 
	 * @param _concreteIterationId
	 * @return the concreteIteration
	 */
    public ConcreteIteration getConcreteIteration(String _concreteIterationId) {
        return this.getConcreteIterationDao().getConcreteIteration(_concreteIterationId);
    }

    /**
	 * Allows to save the concreteIteration
	 * 
	 * @param _concreteIteration
	 */
    public void saveConcreteIteration(ConcreteIteration _concreteIteration) {
        this.concreteIterationDao.saveOrUpdateConcreteIteration(_concreteIteration);
    }

    /**
	 * Allows to get the set of all concreteBreakdownElement
	 * 
	 * @param _concreteIteration
	 * @return the set of all concreteBreakdownElement
	 */
    public Set<ConcreteBreakdownElement> getAllConcreteBreakdownElements(ConcreteIteration _concreteIteration) {
        Set<ConcreteBreakdownElement> tmp = new HashSet<ConcreteBreakdownElement>();
        this.getConcreteIterationDao().getSessionFactory().getCurrentSession().saveOrUpdate(_concreteIteration);
        for (ConcreteBreakdownElement element : _concreteIteration.getConcreteBreakdownElements()) {
            tmp.add(element);
        }
        return tmp;
    }

    /**
	 * Allows to get the concreteIterationDao
	 * 
	 * @return the concreteIterationDao
	 */
    public ConcreteIterationDao getConcreteIterationDao() {
        return concreteIterationDao;
    }

    /**
	 * Allows to set the concreteIterationDao
	 * 
	 * @param concreteIterationDao
	 * 
	 */
    public void setConcreteIterationDao(ConcreteIterationDao concreteIterationDao) {
        this.concreteIterationDao = concreteIterationDao;
    }
}
