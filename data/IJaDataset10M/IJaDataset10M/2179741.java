package name.xwork.model.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象查询条件对象
 * @author  zhaoxingyun
 */
@SuppressWarnings({ "unchecked" })
public class ObjectQueryModel {

    /**
	 * 分段集合返回名称
	 * @uml.property  name="sectionName"
	 */
    String sectionName;

    /**
	 * 查询对象类型
	 */
    Class[] clazz;

    /**
	 * 条件信息
	 */
    List<Conditions> conditionModeList = new ArrayList<Conditions>();

    /**
	 * 排序信息
	 */
    Map<String, OrderType> orderMap = new LinkedHashMap<String, OrderType>();

    /**
	 * 数量限制信息
	 * @uml.property  name="limit"
	 * @uml.associationEnd  
	 */
    Limit limit = new Limit();

    /**
	 * 是否使用查询缓存
	 * @uml.property  name="cacheable"
	 */
    boolean cacheable;

    /**
	 * 缓存模式
	 * @uml.property  name="cacheMode"
	 * @uml.associationEnd  
	 */
    CacheMode cacheMode = CacheMode.NORMAL;

    /**
	 * 缓存区域
	 * @uml.property  name="cacheRegion"
	 */
    String cacheRegion;

    @SuppressWarnings("unused")
    private ObjectQueryModel() {
    }

    ;

    /**
	 * 创建一个对象查询条件对象
	 * 
	 * @param clazz 可变参数，查询结果对象返回的对象类型。
	 */
    public ObjectQueryModel(Class... clazz) {
        if (clazz == null || clazz.length == 0) {
            throw new IllegalArgumentException();
        }
        this.clazz = clazz;
    }

    public ObjectQueryModel(String sectionName, Class... clazz) {
        if (sectionName == null || sectionName.length() < 1 || clazz == null || clazz.length == 0) {
            throw new IllegalArgumentException();
        }
        this.sectionName = sectionName;
        this.clazz = clazz;
    }

    /**
	 * 获得查询结果需要的对象类型
	 * 
	 * @return
	 */
    public Class[] getEntityClasses() {
        return this.clazz;
    }

    /**
	 * 添加一个查询条件
	 * 
	 * @param conditions
	 * @return
	 */
    public ObjectQueryModel add(Conditions conditions) {
        conditionModeList.add(conditions);
        return this;
    }

    /**
	 * 复制另外的查询条件
	 * 
	 * @param objectQueryModel
	 */
    public void copy(ObjectQueryModel objectQueryModel) {
        if (!this.equals(objectQueryModel)) this.conditionModeList.addAll(objectQueryModel.conditionModeList);
    }

    /**
	 * 添加一组排序
	 * 
	 * @param orderMap
	 */
    public void addOrderedMap(Map<String, OrderType> orderMap) {
        if (!this.orderMap.equals(orderMap)) this.orderMap.putAll(orderMap);
    }

    /**
	 * 添加一个排序
	 * 
	 * @param column
	 * @param orderType
	 */
    public void addOrder(String column, OrderType orderType) {
        this.orderMap.put(column, orderType);
    }

    /**
	 * 设置查询结果开始记录下标和返回的最大记录数
	 * 
	 * @param pageIndex
	 * @param maxRowNum
	 * 
	 * @return
	 */
    public void setQueryLimit(int pageIndex, int maxRowNum) {
        this.limit.setPageIndex(pageIndex);
        this.limit.setMaxRowNum(maxRowNum);
    }

    /**
	 * 获得查询起始下标
	 * 
	 * @return
	 */
    public int getStartRowNum() {
        return this.limit.getStartRowNum();
    }

    /**
	 * 获得查询最大记录数
	 * 
	 * @return
	 */
    public int getMaxRowNum() {
        return this.limit.getMaxRowNum();
    }

    /**
	 * 当前查询条件是否限制了记录数
	 * 
	 * @return true：限制了结果数；<br />false：没有限制结果数。
	 */
    public boolean isHaveLimit() {
        return this.limit.getMaxRowNum() > 0l;
    }

    /**
	 * 获得查询结果全部记录数
	 * 
	 * @return
	 */
    public long getAllRowNum() {
        return this.limit.getAllRowNum();
    }

    /**
	 * 设置查询结果全部记录数
	 * 
	 * @param rowCount
	 */
    public void setAllRowNum(int rowCount) {
        this.limit.setAllRowNum(rowCount);
    }

    /**
	 * 是否使用查询缓存
	 * @return
	 * @uml.property  name="cacheable"
	 */
    public boolean isCacheable() {
        return cacheable;
    }

    /**
	 * 设置是否使用查询缓存
	 * @param  cacheable
	 * @uml.property  name="cacheable"
	 */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
	 * 获得缓存模式
	 * @return
	 * @uml.property  name="cacheMode"
	 */
    public CacheMode getCacheMode() {
        return cacheMode;
    }

    /**
	 * 设置缓存模式
	 * @param  cacheMode
	 * @uml.property  name="cacheMode"
	 */
    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    /**
	 * 获得缓存区域
	 * @return
	 * @uml.property  name="cacheRegion"
	 */
    public String getCacheRegion() {
        return cacheRegion;
    }

    /**
	 * 设置缓存区域
	 * @param  cacheRegion
	 * @uml.property  name="cacheRegion"
	 */
    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    /**
	 * 获得当前查询的页面限制条件
	 * @return
	 * @uml.property  name="limit"
	 */
    public Limit getLimit() {
        return this.limit;
    }

    /**
	 * 获得查询要返回的分段集合名称
	 * @return
	 * @uml.property  name="sectionName"
	 */
    public String getSectionName() {
        return sectionName;
    }
}
