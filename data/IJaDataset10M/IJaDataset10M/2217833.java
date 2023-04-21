package com.genia.toolbox.projects.uml_test.test.persistence.test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.transaction.annotation.Transactional;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.persistence.bean.Persistable;
import com.genia.toolbox.persistence.dao.AbstractDAO;
import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.persistence.manager.PersistenceManager;
import com.genia.toolbox.spring.initializer.ApplicationContextHolder;

/**
 * This abstract class is used to test the entities from model test.
 */
public abstract class AbstractPersistenceModelTester extends AbstractDAO {

    /**
   * constructor.
   * 
   * @throws BundledException
   *           occurs when the spring config file is not found.
   */
    public AbstractPersistenceModelTester() throws BundledException {
        setPersistenceManager((PersistenceManager) ApplicationContextHolder.getApplicationContext().getBean("persistenceManager"));
    }

    /**
   * a new Bean.
   */
    private Persistable<? extends Serializable> newBeanToTest;

    /**
   * return the bean to test.
   * 
   * @return the bean to test.
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    public abstract Persistable<? extends Serializable> getNewObjectModel() throws PersistenceException;

    /**
   * return a property in order to test the update method.
   * 
   * @return a property in order to test the update method.
   */
    public abstract String getPropertyToTest();

    /**
   * return a value to set to the property we're using.
   * 
   * @return a value to set.
   */
    public abstract Object getValueToTest();

    /**
   * init object.
   * 
   * @throws BundledException
   *           when the spring configuration file is not found.
   * @throws BeansException
   *           when the bean is not found.
   */
    public void initNewObject() throws BeansException, BundledException {
        newBeanToTest = getNewObjectModel();
        Assert.assertNotNull(newBeanToTest);
    }

    /**
   * test if the bean can be saved.
   * 
   * @throws BundledException
   *           if an error occurred during
   *           <code>getTransactionTemplate().execute()</code>.
   */
    public void testSave() throws BundledException {
        Assert.assertNull(newBeanToTest.getIdentifier());
        save(newBeanToTest);
        Assert.assertNotNull(newBeanToTest.getIdentifier());
    }

    /**
   * test if the bean can being get from his id.
   * 
   * @throws BundledException
   *           if an error occurred during
   *           <code>getTransactionTemplate().execute()</code>
   */
    @SuppressWarnings("unchecked")
    public void testGetFromId() throws BundledException {
        Persistable<? extends Serializable> beanGet = get(newBeanToTest.getClass(), newBeanToTest.getIdentifier());
        Assert.assertNotNull(beanGet);
        Assert.assertEquals(newBeanToTest, beanGet);
    }

    /**
   * test if the object can be updated.
   * 
   * @throws BundledException
   *           if an error occurred during
   *           <code>getTransactionTemplate().execute()</code>
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
    @SuppressWarnings("unchecked")
    public void testUpdate() throws BundledException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String propertyFromBean = getPropertyToTest();
        Object valueToSet = getValueToTest();
        Persistable<? extends Serializable> otherBeanGet = get(newBeanToTest.getClass(), newBeanToTest.getIdentifier());
        if (propertyFromBean != null && valueToSet != null) {
            PropertyUtils.setProperty(otherBeanGet, propertyFromBean, valueToSet);
            update(otherBeanGet);
            Persistable<? extends Serializable> beanUpdated = get(newBeanToTest.getClass(), newBeanToTest.getIdentifier());
            Assert.assertEquals(beanUpdated, otherBeanGet);
            Assert.assertEquals(valueToSet, PropertyUtils.getProperty(otherBeanGet, propertyFromBean));
            Assert.assertEquals(valueToSet, PropertyUtils.getProperty(beanUpdated, propertyFromBean));
        }
    }

    /**
   * test if the bean can be deleted.
   * 
   * @throws BundledException
   *           if an error occurred during
   *           <code>getTransactionTemplate().execute()</code>
   */
    @SuppressWarnings("unchecked")
    public void testDelete() throws BundledException {
        Persistable<? extends Serializable> otherBeanToTest = get(newBeanToTest.getClass(), newBeanToTest.getIdentifier());
        delete(otherBeanToTest);
        Assert.assertNull(get(newBeanToTest.getClass(), newBeanToTest.getIdentifier()));
    }

    /**
   * clean all objects from model that was saved in memory.
   * 
   * @throws PersistenceException
   *           occurs during persistence process.
   */
    public void deleteAllObjectModels(List<? extends Persistable<?>> elements) throws PersistenceException {
        if (elements != null) {
            for (Persistable<? extends Serializable> objModel : elements) {
                delete(objModel);
            }
        }
    }

    /**
   * test all method test.
   * 
   * @throws BundledException
   *           if an error occurred during
   *           <code>getTransactionTemplate().execute()</code>
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
    @SuppressWarnings("unchecked")
    @Test
    @Transactional
    public void doGenericTest() throws BundledException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        initNewObject();
        deleteAllObjectModels((List<? extends Persistable<?>>) getAll(newBeanToTest.getClass()));
        testSave();
        testGetFromId();
        testUpdate();
        testDelete();
    }
}
