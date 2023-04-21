package takatuka.classreader.dataObjs;

import java.util.*;
import takatuka.io.BufferedByteCountingOutputStream;
import takatuka.classreader.logic.util.*;

/**
 * 
 * Description:
 * <p>
 *
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class MultiplePoolsFacade {

    private int maxSize = -1;

    private HashMap pools = new HashMap();

    public static final int DEFAULT_SINGLE_POOP_ID = -1;

    public TreeSet poolIds = null;

    private boolean singlePool = false;

    /**
     * it creates single pool with pool id DEFAULT_SINGLE_POOP_ID;
     * It sets maxSize as -1 (infinity)
     */
    public MultiplePoolsFacade() {
        this(-1);
    }

    /**
     * creates a single pool with id = DEFAULT_SINGLE_POOP_ID;
     * 
     * @param maxSize
     */
    public MultiplePoolsFacade(int maxSize) {
        poolIds = new TreeSet();
        this.maxSize = maxSize;
        poolIds.add(DEFAULT_SINGLE_POOP_ID);
        createPools();
        singlePool = true;
    }

    public MultiplePoolsFacade(TreeSet poolIds, int maxSize) {
        this.maxSize = maxSize;
        this.poolIds = poolIds;
        createPools();
    }

    /**
     * 
     * @return
     */
    public TreeSet getPoolIds() {
        return poolIds;
    }

    /**
     * It takes vector of poolId (integers) and create pool. 
     * Note that number of pools created will be equal to the size of vector.
     * In case already some pool exist then it delete those pools
     * 
     */
    public void createPools() {
        Iterator iterator = poolIds.iterator();
        int poolId = -1;
        ControllerBase base = null;
        while (iterator.hasNext()) {
            poolId = (Integer) iterator.next();
            base = new ControllerBase(maxSize);
            pools.put(poolId, base);
        }
    }

    /**
     * In case it is created using constructor that create single pool then
     * all pool ids are ignored and always that single pool is returned
     * 
     * @param poolId
     * @return
     */
    protected ControllerBase getPool(int poolId) {
        if (singlePool) {
            poolId = (Integer) pools.keySet().iterator().next();
        }
        return (ControllerBase) pools.get(poolId);
    }

    /**
     * we can restrict that a pool/controller can only have single kind of elements
     * and its subobject. In case one wishes that all different kinds of objects
     * are allowed in a pool and it has no restriction 
     * then he use java/lang/Object as pool class. 
     * 
     * @param classNamewithpackage
     * @param poolId
     */
    public void setAllowedClassName(Class classNamewithpackage, int poolId) {
        getPool(poolId).setAllowedClassName(classNamewithpackage);
    }

    /**
     * It works the same way however, it set a same allowed class to all the pools
     * @param classNamewithpackage
     */
    public void setAllAllowedClassName(Class classNamewithpackage) {
        Vector key = new Vector(pools.keySet());
        int poolId = -1;
        for (int loop = 0; loop < key.size(); loop++) {
            poolId = (Integer) key.elementAt(loop);
            setAllowedClassName(classNamewithpackage, poolId);
        }
    }

    /**
     * returns type of allowed classes for a given pool
     * @param poolId
     * @return
     */
    public Class getAllowedClass(int poolId) {
        return getPool(poolId).getAllowedClass();
    }

    /**
     * return true of false depending upon and object is present in a given pool or not...
     * 
     * @param obj
     * @param poolId
     * @return
     */
    public boolean contains(Object obj, int poolId) {
        return getPool(poolId).contains(obj);
    }

    /**
     * returns the index of a given pool object
     * return -1 if the object does not exist in a given pool
     * @param elem
     * @return
     */
    public int indexOf(Object elem, int poolId) {
        return getPool(poolId).indexOf(elem);
    }

    /**
     * As a pool could have duplicate elements. Hence this search an element
     * from the given start Index and return indexed of the search element. 
     * @param elem
     * @param start
     * @param poolId
     * @return
     */
    public int indexOf(Object elem, int start, int poolId) {
        return getPool(poolId).indexOf(elem, start);
    }

    /**
     * clear all the pools
     */
    public void clear() {
        Vector key = new Vector(pools.keySet());
        int poolId = -1;
        for (int loop = 0; loop < key.size(); loop++) {
            poolId = (Integer) key.elementAt(loop);
            getPool(poolId).clear();
        }
    }

    private int checkMaxSize() {
        Vector key = new Vector(pools.keySet());
        int poolId = -1;
        int totalSize = 0;
        for (int loop = 0; loop < key.size(); loop++) {
            poolId = (Integer) key.elementAt(loop);
            totalSize += getPool(poolId).getCurrentSize();
        }
        try {
            if (totalSize > maxSize && maxSize > 0) {
                throw new Exception(" Pool size cannot be greater than " + maxSize);
            }
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return totalSize;
    }

    /**
     * populate a given pool with a collection.
     * check the max size and exist with error if pools total size is create 
     * than allowed maxsize
     * @param collection
     * @param poolId
     */
    public void addAll(Collection collection, int poolId) {
        getPool(poolId).addAll(collection);
        checkMaxSize();
    }

    /**
     * Sort a part of a pool
     * 
     * @param comp
     * @param startIndex
     * @param endIndex
     * @param poolId
     * @throws java.lang.Exception
     */
    public void sort(java.util.Comparator comp, int startIndex, int endIndex, int poolId) throws Exception {
        getPool(poolId).sort(comp, startIndex, endIndex);
    }

    /**
     * sort a complete pool
     * @param comp
     * @throws java.lang.Exception
     */
    public void sort(java.util.Comparator comp, int poolId) throws Exception {
        getPool(poolId).sort(comp);
    }

    /**
     * Sort all the pools
     * 
     * @param comp
     * @throws java.lang.Exception
     */
    public void sort(java.util.Comparator comp) throws Exception {
        Vector key = new Vector(pools.keySet());
        int poolId = -1;
        for (int loop = 0; loop < key.size(); loop++) {
            poolId = (Integer) key.elementAt(loop);
            getPool(poolId).sort(comp);
        }
    }

    /**
     * Any specific validation for object required while adding them in a pool
     * @throws java.lang.Exception
     */
    public void validate(int poolId) throws Exception {
        getPool(poolId).validate();
    }

    /**
     * remove object based on index in a given pool
     * return object removed
     * @param index
     * @param poolId
     * @return
     */
    public Object remove(int index, int poolId) {
        return getPool(poolId).remove(index);
    }

    public Object[] getAllArray(int poolId) {
        return getPool(poolId).getAllArray();
    }

    /**
     * get all the object of a given pool
     * @param poolId
     * @return
     */
    public Vector getAll(int poolId) {
        return getPool(poolId).getAll();
    }

    /**
     * set (i.e. overwrite) an object at a given index
     * @param index
     * @param obj
     * @param poolId
     */
    public void set(int index, Object obj, int poolId) {
        getPool(poolId).set(index, obj);
    }

    /**
     * add object in a pool
     * returns the index of the added object in the pool
     * @param obj
     * @param poolId
     * @return
     * @throws java.lang.ArrayIndexOutOfBoundsException
     * @throws java.lang.Exception
     */
    public int add(Object obj, int poolId) throws ArrayIndexOutOfBoundsException, Exception {
        ControllerBase pool = getPool(poolId);
        int toBeAdded = pool.getCurrentSize();
        pool.add(obj);
        return toBeAdded;
    }

    /**
     * get an object 
     * @param index
     * @param poolId
     * @return
     */
    public Object get(int index, int poolId) {
        return getPool(poolId).get(index);
    }

    /**
     * return the current size of a given pool
     * @param poolId
     * @return
     */
    public int getCurrentSize(int poolId) {
        return getPool(poolId).getCurrentSize();
    }

    /**
     * return the total current size of all the pools.
     * @return
     */
    public int getCurrentSize() {
        return checkMaxSize();
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(int poolId, int maxSize) {
        getPool(poolId).setMaxSize(maxSize);
    }

    public int getMaxSize(int poolId) {
        return getPool(poolId).getMaxSize();
    }

    @Override
    public String toString() {
        return null;
    }

    public String writeSelected(BufferedByteCountingOutputStream buff) throws Exception {
        return null;
    }
}
