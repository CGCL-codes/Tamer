package com.bones.query.sysexpert_new.service;

import com.bones.core.utils.exception.ServiceAccessException;
import com.bones.query.sysexpert_new.dao.entity.ResAttrHisHour;
import com.bones.core.web.tags.IPagination;

/** 访问接口定义 */
public interface IResAttrHisHourService {

    /** 分页查询—— RES_ATTR_HIS_HOUR列表 */
    public IPagination queryResAttrHisHourList(ResAttrHisHour resAttrHisHour, int first, int max) throws ServiceAccessException;

    /** 查询单条—— RES_ATTR_HIS_HOUR详情 */
    public ResAttrHisHour viewResAttrHisHour(ResAttrHisHour resAttrHisHour) throws ServiceAccessException;

    /** 删除单条—— RES_ATTR_HIS_HOUR记录 */
    public void delResAttrHisHour(ResAttrHisHour resAttrHisHour) throws ServiceAccessException;
}
