package com.google.appengine.datanucleus.jdo;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.datanucleus.Utils;
import com.google.appengine.datanucleus.test.HasMultiValuePropsJDO;

/**
 * @author Max Ross <maxr@google.com>
 */
public class JDOMakeTransientTest extends JDOTestCase {

    public void testListAccessibleOutsideTxn() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity e = new Entity(HasMultiValuePropsJDO.class.getSimpleName());
        e.setProperty("strList", Utils.newArrayList("a", "b", "c"));
        e.setProperty("str", "yar");
        ds.put(e);
        beginTxn();
        HasMultiValuePropsJDO pojo = pm.getObjectById(HasMultiValuePropsJDO.class, e.getKey().getId());
        pojo.setStr("yip");
        pojo.getStrList();
        commitTxn();
        assertEquals("yip", pojo.getStr());
        assertEquals(3, pojo.getStrList().size());
        pm = pmf.getPersistenceManager();
    }
}
