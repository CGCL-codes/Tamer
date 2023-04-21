package whf.framework.entity.decorator;

import java.lang.reflect.Constructor;
import java.util.Map;
import whf.framework.config.ApplicationConfig;
import whf.framework.entity.Entity;
import whf.framework.log.Log;
import whf.framework.log.LogFactory;
import whf.framework.util.StringUtils;
import whf.framework.util.Utils;

/**
 * 包装业务对象的包装工厂<br>
 * 根据业务对象类名可以找到是否有该类的封装类,如果有根据被封装对象实例创建并返回封装对象实例;
 * @author wanghaifeng
 * @create Oct 8, 2006 10:40:39 PM
 * 
 */
public class WrapperFactory {

    /**
	 * @property Log:log
	 */
    private static Log log = LogFactory.getLog(WrapperFactory.class);

    private static Map<String, Class> classCache = Utils.newHashMap();

    /**
	 * 返回包装后的对象
	 * @modify wanghaifeng Oct 8, 2006 10:41:56 PM
	 * @param entity
	 * @return
	 */
    public static EntityWrapper getWrapper(Entity entity) {
        if (entity == null) return null;
        String sourceClassName = entity.getClass().getName();
        EntityWrapper result = null;
        try {
            String wrapperClassName = ApplicationConfig.getInstance().getWrapperClass(sourceClassName);
            Class wrapperClass = null;
            if (classCache.containsKey(wrapperClassName)) {
                wrapperClass = classCache.get(wrapperClassName);
            } else {
                wrapperClass = Utils.getClassByClassName(wrapperClassName);
                classCache.put(wrapperClassName, wrapperClass);
            }
            if (wrapperClass == null) return null;
            Constructor[] constructors = wrapperClass.getConstructors();
            if (constructors != null) {
                for (Constructor c : constructors) {
                    Class paramTypes[] = c.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1 && paramTypes[0] == entity.getClass()) {
                        result = (EntityWrapper) c.newInstance(new Object[] { entity });
                    }
                }
            }
        } catch (Exception e) {
            log.warn("wrap class error;", e);
        }
        return result;
    }

    /**
	 * 判定是否有包装类
	 * @modify wanghaifeng Oct 8, 2006 11:09:24 PM
	 * @param cls
	 * @return
	 */
    public static boolean hasWrapperClass(Class cls) {
        return !StringUtils.isEmpty(ApplicationConfig.getInstance().getWrapperClass(cls.getName()));
    }
}
