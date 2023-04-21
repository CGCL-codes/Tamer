package com.bones.query.sysexpert_new.dao;

import com.bones.core.utils.exception.DaoAccessException;
import com.bones.query.sysexpert_new.dao.entity.ResAttrHisHour;
import com.bones.core.web.tags.IPagination;

/** 访问接口定义 */
public interface IResAttrHisHourDao {

    /** 分页查询—— RES_ATTR_HIS_HOUR列表 */
    public IPagination queryResAttrHisHourList(ResAttrHisHour resAttrHisHour, int first, int max) throws DaoAccessException;

    /** 查询单条—— RES_ATTR_HIS_HOUR详情 */
    public ResAttrHisHour viewResAttrHisHour(ResAttrHisHour resAttrHisHour) throws DaoAccessException;

    /** 删除单条—— RES_ATTR_HIS_HOUR记录 */
    public void delResAttrHisHour(ResAttrHisHour resAttrHisHour) throws DaoAccessException;
}
