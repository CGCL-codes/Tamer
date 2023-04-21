package cn.webwheel.database.engine;

import cn.webwheel.ObjectFactory;
import cn.webwheel.database.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IPool factory class.
 */
public final class IPoolFactory {

    Map<Class, Class<? extends IPoolObject>> iclsmap = new ConcurrentHashMap<Class, Class<? extends IPoolObject>>();

    Map<String, UserTypeInfo> userTypeInfoMap = new HashMap<String, UserTypeInfo>();

    ObjectFactory defObjectFactory;

    SqlProvider sqlProvider;

    public void setSqlProvider(SqlProvider sqlProvider) {
        this.sqlProvider = sqlProvider;
    }

    /**
     * user type for result fetching
     *
     * @param name     name of user type
     * @param bCls     bean class
     * @param dCls     database class
     * @param userType user type
     */
    public <B, D> void addUserType(String name, Class<B> bCls, Class<D> dCls, UserType<B, D> userType) {
        if (userType == null) {
            userTypeInfoMap.remove(name);
            return;
        }
        if (bCls == null) throw new IllegalArgumentException("bCls is null");
        if (dCls == null) throw new IllegalArgumentException("dCls is null");
        if (!Utils.isBasicType(dCls)) throw new IllegalArgumentException("dCls is not basic type for database");
        UserTypeInfo<B, D> uti = new UserTypeInfo<B, D>();
        uti.userType = userType;
        uti.bCls = bCls;
        uti.dCls = dCls;
        userTypeInfoMap.put(name, uti);
    }

    /**
     * factory method.<br/>
     * use no translators
     * @param conProvider database connection factory object
     * @param objectFactory object factory object
     * @return IPool
     */
    public IPool create(ConnectionProvider conProvider, ObjectFactory objectFactory) {
        return new IPoolImpl(this, conProvider, objectFactory);
    }

    /**
     * factory method.<br/>
     * use default object factory: Class.newInstance()<br/>
     * use no translators
     * @param conProvider database connection factory object
     * @return IPool
     */
    public IPool create(ConnectionProvider conProvider) {
        if (defObjectFactory == null) defObjectFactory = new ObjectFactory() {

            @Override
            public <T> T getInstanceOf(Class<T> cls) {
                try {
                    return cls.newInstance();
                } catch (Exception e) {
                    throw new IException("can not create instance of " + cls, e);
                }
            }
        };
        return create(conProvider, defObjectFactory);
    }
}
