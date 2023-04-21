package com.bones.query.sysexpert_new.dao;

import com.bones.core.utils.exception.DaoAccessException;
import com.bones.query.sysexpert_new.dao.entity.ResEvent;
import com.bones.core.web.tags.IPagination;

/** 访问接口定义 */
public interface IResEventDao {

    /** 分页查询—— RES_EVENT列表 */
    public IPagination queryResEventList(ResEvent resEvent, int first, int max) throws DaoAccessException;

    /** 查询单条—— RES_EVENT详情 */
    public ResEvent viewResEvent(ResEvent resEvent) throws DaoAccessException;

    /** 删除单条—— RES_EVENT记录 */
    public void delResEvent(ResEvent resEvent) throws DaoAccessException;
}
