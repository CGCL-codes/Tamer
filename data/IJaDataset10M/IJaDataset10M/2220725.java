package com.wangyu001.dao;

import java.sql.SQLException;
import java.util.List;
import com.wangyu001.entity.UserGroup;

public interface UserGroupDao {

    /** 
     * create: 插入新数据。返回数据ID，或者 null。
     * 
     * @param clickFrom 包含数据的实体 Bean
     * @param returnId 是否返回新数据ID，true-返回，false-不返回
     * @return
     * @throws SQLException
     */
    public Long create(UserGroup userGroup, boolean returnId) throws SQLException;

    /**
     * delete
     * 
     * @param userGroupId
     * @return 
     * 
     * @author Song ChengMing
     */
    public boolean remove(Long userGroupId) throws SQLException;

    /**
     * fetch
     * 
     * @param userGroupId
     * @return 
     * 
     * @author Song ChengMing
     */
    public UserGroup fetch(Long userGroupId) throws SQLException;

    /**
     * 根据用户Id获得最新分组
     * @param userId：用户ID
     * @return 最新好友分组列表
     * @throws SQLException
     */
    public List<UserGroup> fetchGroupByUser(Long userId) throws SQLException;

    /**
     * export: 导出数据。
     *
     * @param offset 偏移量，第一条数据的偏移量为0
     * @param limit 记录条数
     * @return
     * @throws SQLException
     */
    public List<UserGroup> export(int offset, int limit) throws SQLException;
}
