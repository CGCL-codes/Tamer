package corner.orm.tapestry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.components.IPrimaryKeyConverter;
import corner.util.BeanUtils;

/**
 * 根据主键值来进行对Object进行转换.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision:3677 $
 * @since	2005-11-4
 */
public class ReflectPrimaryKeyConverter<T extends Object> implements IPrimaryKeyConverter {

    /**
	 * Logger for this class
	 */
    private static final Log logger = LogFactory.getLog(ReflectPrimaryKeyConverter.class);

    private String idStr;

    private Class<? extends Object> c;

    public ReflectPrimaryKeyConverter(Class<? extends Object> c, String idStr) {
        this.idStr = idStr;
        this.c = c;
    }

    /**
	 * 根据object来得到对应的主建值.
	 * @see org.apache.tapestry.components.IPrimaryKeyConverter#getPrimaryKey(java.lang.Object)
	 */
    public Object getPrimaryKey(Object obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("getPrimaryKey(Object) -  : Object obj=" + obj);
        }
        return BeanUtils.getProperty(obj, idStr);
    }

    /**
	 * 根据主键值来得到对应的Object.
	 * @see org.apache.tapestry.components.IPrimaryKeyConverter#getValue(java.lang.Object)
	 */
    public Object getValue(Object id) {
        try {
            Object obj = c.newInstance();
            BeanUtils.setProperty(obj, idStr, id);
            if (logger.isDebugEnabled()) {
                logger.debug("getValue(Object) id[" + id + "]");
            }
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
