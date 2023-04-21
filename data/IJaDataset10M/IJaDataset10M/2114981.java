package com.coyousoft.wangyu.logic;

import java.util.List;
import com.coyousoft.wangyu.entity.CoreUrlTag;

public interface CoreUrlTagLogic {

    /**
     * 插入一条数据。
     *
     * @param coreUrlTag
     * @throws Exception  
     */
    public void create(CoreUrlTag coreUrlTag) throws Exception;

    /**
     * 批量插入数据。
     * 
     * @param coreUrlTagList
     * @throws Exception
     */
    public void create(List<CoreUrlTag> coreUrlTagList) throws Exception;

    /** 
     * 根据 ID 删除数据。
     *  
     * @param tagId
     * @return 被删除的记录数量  
     * @throws Exception
     */
    public int remove(Integer tagId) throws Exception;

    /** 
     * 根据 ID 修改其它字段。
     *  
     * @param coreUrlTag
     * @return  被修改的记录数量
     * @throws Exception
     */
    public int update(CoreUrlTag coreUrlTag) throws Exception;

    /** 
     * 根据 ID 获得数据。
     *  
     * @param tagId
     * @return  
     * @throws Exception
     */
    public CoreUrlTag fetch(Integer tagId) throws Exception;

    /** 
     * 获得数据列表。 
     *  
     * @param offset 偏移量，第一条数据的偏移量为0
     * @param limit 数据量
     * @return  
     * @throws Exception
     */
    public List<CoreUrlTag> export(int offset, int limit) throws Exception;

    /** 
     * 提交缓存里的数据。
     *  
     * @throws Exception
     */
    public void flush() throws Exception;
}
