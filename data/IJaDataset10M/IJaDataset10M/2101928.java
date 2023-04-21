package whf.test;

import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import whf.framework.entity.AbstractEntity;
import whf.framework.entity.Entity;
import whf.framework.log.Log;
import whf.framework.log.LogFactory;
import whf.framework.meta.MetaFactory;
import whf.framework.service.Service;
import whf.framework.util.BeanFactory;
import whf.framework.util.BeanUtils;
import whf.framework.util.StringUtils;

/**
 * @author wanghaifeng
 *
 */
public abstract class BaseTest<T extends Entity> extends TestCase {

    protected static Log log = LogFactory.getLog(BaseTest.class);

    public abstract Class<T> getBoClass();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MetaFactory.initialize();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public Service<T> getService() throws Exception {
        return BeanFactory.getService(MetaFactory.findByModelClass(this.getBoClass()));
    }

    public Object invokeMethod(String methodName, Class[] types, Object[] params) throws Exception {
        return this.getService().invokeMethod(methodName, types, params);
    }

    public Object invokeMethod(String methodName) throws Exception {
        return this.invokeMethod(methodName, null, null);
    }

    public Object invokeBaseMethods(String methods) throws Exception {
        if (methods == null) return null;
        String[] methodArr = StringUtils.split(methods, ",");
        for (String method : methodArr) {
            if (StringUtils.equalsIgnoreCase(method, "create")) {
                AbstractEntity bo = (AbstractEntity) getBoClass().newInstance();
                bo.setName(System.currentTimeMillis() + "");
                log.info(bo);
                return this.invokeMethod("create", new Class[] { getBoClass() }, new Object[] { bo });
            } else if (StringUtils.equalsIgnoreCase(method, "findAll")) {
                List<Entity> list = BeanUtils.convert2BoList((Collection) this.invokeMethod("findAll"));
                log.info("FindAll Count:" + list.size());
                for (Entity bo : list) {
                    log.info(bo.getId() + "\t" + bo.getName());
                }
                return list;
            }
        }
        return null;
    }
}
