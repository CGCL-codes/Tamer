package com.bones.query.sysexpert_new.dao;

import com.bones.core.utils.exception.DaoAccessException;
import com.bones.query.sysexpert_new.dao.entity.EventFlashTb;
import com.bones.core.web.tags.IPagination;

/** 访问接口定义 */
public interface IEventFlashTbDao {

    /** 分页查询—— EVENT_FLASH_TB列表 */
    public IPagination queryEventFlashTbList(EventFlashTb eventFlashTb, int first, int max) throws DaoAccessException;

    /** 查询单条—— EVENT_FLASH_TB详情 */
    public EventFlashTb viewEventFlashTb(EventFlashTb eventFlashTb) throws DaoAccessException;

    /** 删除单条—— EVENT_FLASH_TB记录 */
    public void delEventFlashTb(EventFlashTb eventFlashTb) throws DaoAccessException;
}
