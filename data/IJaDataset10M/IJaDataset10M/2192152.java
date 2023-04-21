package com.bones.query.sysexpert_new.service;

import com.bones.core.utils.exception.ServiceAccessException;
import com.bones.query.sysexpert_new.dao.entity.ResAttrHis;
import com.bones.core.web.tags.IPagination;

/** 访问接口定义 */
public interface IResAttrHisService {

    /** 分页查询—— RES_ATTR_HIS列表 */
    public IPagination queryResAttrHisList(ResAttrHis resAttrHis, int first, int max) throws ServiceAccessException;

    /** 查询单条—— RES_ATTR_HIS详情 */
    public ResAttrHis viewResAttrHis(ResAttrHis resAttrHis) throws ServiceAccessException;

    /** 删除单条—— RES_ATTR_HIS记录 */
    public void delResAttrHis(ResAttrHis resAttrHis) throws ServiceAccessException;
}
