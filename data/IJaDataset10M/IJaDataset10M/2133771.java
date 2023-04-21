package whf.framework.util;

import java.util.Map;

/**
 * 用户跟踪用户创建对象的信息
 * @author wanghaifeng
 * @create Apr 18, 2007 9:20:44 AM
 * 
 */
public final class TraceableObject<T> {

    /**
	 * 创建时间
	 * @property long:createTime
	 */
    private long createTime;

    /**
	 * 目标对象
	 * @property T:target
	 */
    private T target;

    /**
	 * 其它属性
	 * @property Map<String,Object>:properties
	 */
    private Map<String, Object> properties = Utils.newHashMap();

    public TraceableObject(T target) {
        this.target = target;
        this.createTime = System.currentTimeMillis();
    }

    /**
	 * @modify wanghaifeng Apr 18, 2007 9:33:10 AM
	 * @param propertyName
	 * @param value
	 */
    public final void setProperty(String propertyName, Object value) {
        this.properties.put(propertyName, value);
    }

    /**
	 * @modify wanghaifeng Apr 18, 2007 9:33:24 AM
	 * @param propertyName
	 */
    public final void removeProperty(String propertyName) {
        this.properties.remove(propertyName);
    }

    /**
	 * @modify wanghaifeng Apr 18, 2007 9:33:27 AM
	 */
    public final void clearProperties() {
        this.properties.clear();
    }

    /**
	 * @modify wanghaifeng Apr 18, 2007 9:33:29 AM
	 * @param propertyName
	 * @return
	 */
    public final Object getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }

    /**
	 * 获取创建时间
	 * @modify wanghaifeng Apr 18, 2007 9:27:11 AM
	 * @return
	 */
    public final long getCreateTime() {
        return this.createTime;
    }

    /**
	 * 获取目标对象
	 * @modify wanghaifeng Apr 18, 2007 9:27:21 AM
	 * @return
	 */
    public final T get() {
        return this.target;
    }
}
