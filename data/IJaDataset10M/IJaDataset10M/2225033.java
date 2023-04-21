package com.wwg.cms.service.impl;

import java.util.Collection;
import com.wwg.cms.bo.*;
import com.wwg.cms.service.*;
import com.css.framework.dao.GeneralDao;
import com.wwg.cms.bo.entity.*;

/**
 * 
 * create by wwl
 * 模板方案表
 * @author wwl
 */
public class TemplatesetServiceImpl implements TemplatesetService {

    private GeneralDao generalDao;

    public void setGeneralDao(GeneralDao generalDao) {
        this.generalDao = generalDao;
    }

    /**
	 *获取所有模板方案表
	 */
    public Collection getTemplatesetList() {
        String hsql = "select ent from TemplatesetEntity as ent ";
        return generalDao.find(hsql);
    }

    /**
	 * 添加模板方案表
	 * @param templateset 模板方案表
	 * @return
	 */
    public Templateset addTemplateset(Templateset templateset) {
        generalDao.save(templateset);
        return templateset;
    }

    /**
	 * 修改模板方案表
	 * @param templateset 模板方案表
	 * @return
	 */
    public Templateset updateTemplateset(Templateset templateset) {
        generalDao.saveOrUpdate(templateset);
        return templateset;
    }

    /**
	 * 删除模板方案表
	 * @param templateset 模板方案表
	 * @return
	 */
    public Templateset deleteTemplateset(Templateset templateset) {
        generalDao.delete(templateset);
        return templateset;
    }

    /**
     *  获取模板方案表 by id
     *  @param id 编号
     * @return
	 */
    public Templateset getTemplatesetById(Long id) {
        return (Templateset) generalDao.fetch(id, TemplatesetEntity.class);
    }
}
