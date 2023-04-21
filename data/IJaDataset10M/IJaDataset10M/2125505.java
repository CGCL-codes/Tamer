package kea.busfw;

import java.util.*;

/**
 *  the ObjectMapPool create or get a ObjectMap Object.
 * 
 * <H3>Intent</H3>
 * <P>
 * <H3>Features</H3>
 *  
 * <H3>Architecture </H3> 
 *   
 * <H3>Example </H3>
 * 
 * @author <A HREF="mailto: zbingfeng@163.net">bingfeng zhang</A>
</A>
 */
public class ObjectMapPool {

    /** the ObjectMap pool*/
    Hashtable pool = new Hashtable();

    /** a instance of BusinessFactory */
    protected static ObjectMapPool instance = null;

    /** default construction*/
    private ObjectMapPool() {
    }

    /** 
     * get a  ObjectMapPool instance
     * the method through which this class is accessed
     * @return a  ObjectMapPoolinstance
     * @author <A HREF="mailto: zbingfeng@163.net">bingfeng zhang</A>
     */
    public static ObjectMapPool getInstance() {
        if (instance == null) {
            synchronized (ObjectMapPool.class) {
                if (instance == null) {
                    instance = new ObjectMapPool();
                }
            }
        }
        return instance;
    }

    /**
     * get a ObjectMap object 
     * @param name  the ObjectMap name
     * @return a ObjectMap object
     */
    public ObjectMap getObjectMap(String name) {
        ObjectMap objectMap = (ObjectMap) this.pool.get(name);
        if (objectMap == null) {
            try {
                objectMap = new ObjectMap(name);
                this.pool.put(name, objectMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return objectMap;
    }
}
