package com.genia.toolbox.projects.uml2model_test.business.dao.test;

import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.model.exemple.AssociateOfB;
import com.genia.toolbox.model.exemple.impl.AssociateOfBImpl;
import com.genia.toolbox.persistence.dao.AbstractDAO;
import com.genia.toolbox.persistence.exception.AggregateException;
import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.persistence.manager.PersistenceManager;
import com.genia.toolbox.projects.tests.uml2model_test.business.dao.AssociateOfBbusinessDao;
import com.genia.toolbox.spring.initializer.ApplicationContextHolder;

/**
 * this class test methods from business dao object of
 * <code>AssociateOfBImpl</code>.
 */
public class AssociateOfBImplDaoTest extends AbstractDAO {

    /**
   * the business dao associated to <code>AssociateOfBImpl</code>.
   */
    AssociateOfBbusinessDao associateBusiness = null;

    /**
   * the object <code>AssociateOfBImpl</code>.
   */
    AssociateOfBImpl associateOfB = null;

    /**
   * id associated to <code>AssociateOfBImpl</code>
   */
    Long idAssociate = null;

    /**
   * initialize object.
   * 
   * @throws BundledException
   *           occurs if spring configuration file is not found.
   * @throws BeansException
   *           occurs when the bean is not found.
   */
    @Before
    public void initObject() throws BeansException, BundledException {
        setPersistenceManager((PersistenceManager) ApplicationContextHolder.getApplicationContext().getBean("persistenceManager"));
        associateBusiness = (AssociateOfBbusinessDao) ApplicationContextHolder.getApplicationContext().getBean("associateBusiness");
        associateOfB = associateBusiness.createNewAssociateOfB();
        associateOfB.setNameOfAssociate("name");
    }

    /**
   * test create method.
   */
    public void testCreate() {
        Assert.assertNotNull(associateOfB);
    }

    /**
   * test save method.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    public void testSave() throws PersistenceException {
        idAssociate = associateBusiness.saveAssociateOfB(associateOfB);
        Assert.assertNotNull(idAssociate);
    }

    /**
   * test get method.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   * @throws AggregateException
   *           is throw when an already persistant object is saved.
   */
    public void testGetAssociate() throws AggregateException, PersistenceException {
        AssociateOfB newAssociate = associateBusiness.getAssociate(idAssociate);
        Assert.assertNotNull(newAssociate);
        Assert.assertEquals("name", newAssociate.getNameOfAssociate());
        AssociateOfB associateFromName = associateBusiness.getAssociate("name");
        Assert.assertNotNull(associateFromName);
    }

    /**
   * test the getAll method.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    public void testGetListAssociates() throws PersistenceException {
        AssociateOfB associateTest1 = associateBusiness.createNewAssociateOfB();
        Long idTest1 = associateBusiness.saveAssociateOfB(associateTest1);
        Assert.assertNotNull(idTest1);
        List<AssociateOfBImpl> listAssociates = associateBusiness.getListAssociateOfB();
        Assert.assertFalse(listAssociates.isEmpty());
        Assert.assertTrue(listAssociates.size() == 2);
        Assert.assertTrue(listAssociates.contains(get(AssociateOfBImpl.class, idTest1)));
        Assert.assertTrue(listAssociates.contains(get(AssociateOfBImpl.class, idAssociate)));
        associateBusiness.delete(idTest1);
    }

    /**
   * test delete method from <code>AssociateOfBbusinessDao</code>.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    public void testDelete() throws PersistenceException {
        associateBusiness.delete(idAssociate);
        Assert.assertNull(get(AssociateOfBImpl.class, idAssociate));
    }

    /**
   * delete all object that was saved.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    @After
    public void cleanUp() throws PersistenceException {
        for (AssociateOfBImpl associate : getAll(AssociateOfBImpl.class)) {
            delete(associate);
        }
        Assert.assertTrue(getAll(AssociateOfBImpl.class).isEmpty());
    }

    /**
   * execute all test method.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    @Test
    public void doTest() throws PersistenceException {
        testSave();
        testGetAssociate();
        testGetListAssociates();
        testDelete();
    }
}
