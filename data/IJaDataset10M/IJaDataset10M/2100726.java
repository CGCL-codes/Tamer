package com.genia.toolbox.persistence.test.factory;

import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.persistence.manager.PersistenceManager;
import com.genia.toolbox.persistence.test.bean.PersistentObjectA;
import com.genia.toolbox.persistence.test.bean.PersistentObjectB;
import com.genia.toolbox.persistence.test.bean.PersistentObjectC;

/**
 * the factory of all objects needed to perform the test of the persistence API.
 */
public abstract class AbstractObjectFactory {

    /**
   * return a new object of type <code>PersistentObjectA</code>.
   * 
   * @return a new object of type <code>PersistentObjectA</code>
   */
    public PersistentObjectA getNewObjectA() {
        return new PersistentObjectA();
    }

    /**
   * return a new object of type <code>PersistentObjectB</code>.
   * 
   * @return a new object of type <code>PersistentObjectB</code>
   */
    public PersistentObjectB getNewObjectB() {
        return new PersistentObjectB();
    }

    /**
   * return a new object of type <code>PersistentObjectC</code>.
   * 
   * @return a new object of type <code>PersistentObjectC</code>
   */
    public PersistentObjectC getNewObjectC() {
        return new PersistentObjectC();
    }

    /**
   * return the <code>PersistenceManager</code> to test.
   * 
   * @return the <code>PersistenceManager</code> to test
   * @throws BundledException
   *           when an unexpected error occurred.
   */
    public abstract PersistenceManager getPersistenceManager() throws BundledException;
}
