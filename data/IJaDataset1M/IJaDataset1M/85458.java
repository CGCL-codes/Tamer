package org.basegen.base.business.iterator;

import java.util.Date;
import java.util.Iterator;
import org.basegen.base.business.logic.AbstractBusinessLogic;
import org.basegen.base.exception.BusinessLogicException;
import org.basegen.base.exception.CommunicationException;
import org.basegen.base.exception.RepositoryException;
import org.basegen.base.persistence.query.Criteria;
import org.basegen.base.persistence.query.Filter;

/**
 * This class implements a BeanRangeBaseIteratorContainer using a AbstractBusinessLogic
 */
public class BusinessLogicBeanRangeBaseIteratorContainer implements BeanRangeBaseIteratorContainer {

    /**
     * Default constructor
     */
    public BusinessLogicBeanRangeBaseIteratorContainer() {
    }

    /**
     * Constructor using new abstract business logic
     * @param newAbstractBusinessLogic new abstract business logic
     */
    public BusinessLogicBeanRangeBaseIteratorContainer(AbstractBusinessLogic newAbstractBusinessLogic) {
        setAbstractBusinessLogic(newAbstractBusinessLogic);
    }

    /**
     * Holds value of property abstractBusinessLogic.
     */
    private AbstractBusinessLogic abstractBusinessLogic;

    /**
     * Getter for property abstractBusinessLogic.
     * 
     * @return Value of property abstractBusinessLogic.
     */
    public AbstractBusinessLogic getAbstractBusinessLogic() {
        return this.abstractBusinessLogic;
    }

    /**
     * Setter for property abstractBusinessLogic.
     * 
     * @param newAbstractBusinessLogic New value of property abstractBusinessLogic.
     */
    public void setAbstractBusinessLogic(AbstractBusinessLogic newAbstractBusinessLogic) {
        this.abstractBusinessLogic = newAbstractBusinessLogic;
    }

    /**
     * Checks if the repository is HibernateRepository. If positive, flush and clears the session
     * 
     * @throws RepositoryException repository exception
     */
    private void freeMemory() throws RepositoryException {
        getAbstractBusinessLogic().flush();
    }

    /** 
     * Recebe um filter e retorna um conjunto de beans, na forma de um iterador.
     * @param criteria criteria
     * @return iterator containing the beans   
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Iterator list(Criteria criteria) throws CommunicationException, RepositoryException, BusinessLogicException {
        freeMemory();
        return getAbstractBusinessLogic().list(criteria).iterator();
    }

    /** 
     * Recebe um filter e retorna um conjunto de beans, na forma de um iterador.
     * @param criteria criteria
     * @return iterator containing lazy beans 
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Iterator listLazy(Criteria criteria) throws CommunicationException, RepositoryException, BusinessLogicException {
        freeMemory();
        return getAbstractBusinessLogic().list(criteria).iterator();
    }

    /**
     * Recebe um filter e uma column, retornando o m�ximo dos registros da column dos beans que satisfazem o filter.
     * @param filter filter
     * @return count
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public int count(Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().count(filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�ximo dos registros da column dos beans que satisfazem o filter.
     * @return maximum as integer
     * @param column column
     * @param filter filter
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public int maximumInt(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().maximumInt(column, filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�nimo dos registros da column dos beans que satisfazem o filter.
     * @param column column
     * @param filter filter
     * @return minimum as integer
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public int minimumInt(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().minimumInt(column, filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�ximo dos registros da column dos beans que satisfazem o filter.
     * @param column column
     * @param filter filter
     * @return maximum as float
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public float maximumFloat(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().maximumFloat(column, filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�nimo dos registros da column dos beans que satisfazem o filter.
     * @param column column
     * @param filter filter
     * @return minimum as float
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public float minimumFloat(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().minimumFloat(column, filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�ximo dos registros da column dos beans que satisfazem o filter.
     * @param column column
     * @param filter filter
     * @return maximum as date
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Date maximumDate(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().maximumDate(column, filter);
    }

    /**
     * Recebe um filter e uma column, retornando o m�nimo dos registros da column dos beans que satisfazem o filter.
     * @param column column
     * @param filter filter
     * @return minimum date 
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Date minimumDate(String column, Filter filter) throws CommunicationException, RepositoryException, BusinessLogicException {
        return getAbstractBusinessLogic().minimumDate(column, filter);
    }
}
