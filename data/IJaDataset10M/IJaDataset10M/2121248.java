package com.hy.erp.inventory.service;

import java.util.List;
import javax.ejb.Stateless;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.async.Schedule;
import com.hy.enterprise.architecture.foundation.ArchitectureFoundationException;
import com.hy.enterprise.framework.service.business.AbstractBusinessService;
import com.hy.erp.inventory.dao.interfaces.ISkillLevelDao;
import com.hy.erp.inventory.dao.interfaces.IUserDao;
import com.hy.erp.inventory.pojo.SkillLevel;
import com.hy.erp.inventory.pojo.interfaces.ISkillLevel;
import com.hy.erp.inventory.pojo.interfaces.IUser;
import com.hy.erp.inventory.service.interfaces.ISkillLevelService;

/**
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-10-12；时间：下午03:29:31</li>
 * <li>字段类型：long；字段名称：serialVersionUID</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
@Name("skillLevelService")
@Scope(ScopeType.SESSION)
@Stateless
public class SkillLevelService extends AbstractBusinessService<ISkillLevel> implements ISkillLevelService {

    private static final long serialVersionUID = -799375524483137667L;

    /**
	 * 
	 * 构造函数
	 */
    public SkillLevelService() {
        super();
    }

    @Override
    public String[] addSkillLevel(Object[] objects) {
        if (objects == null) {
            return null;
        } else {
            ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
            SkillLevel[] skillLevels = new SkillLevel[objects.length];
            for (int i = 0; i < objects.length; i++) {
                SkillLevel skillLevel = (SkillLevel) objects[i];
                skillLevels[i] = skillLevel;
            }
            return dao.persist(skillLevels);
        }
    }

    @Override
    public Integer removeSkillLevel(String[] levelIds) {
        if ((null == levelIds) || (levelIds.length == 0)) {
            return new Integer(0);
        } else {
            ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
            return dao.remove(levelIds);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public boolean modifySkillLevel(SkillLevel skillLevel) {
        if ((null == skillLevel) || (null == skillLevel.getIdentifier())) {
            return true;
        } else {
            ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
            ISkillLevel iSkillLevel = skillLevel;
            if (null == iSkillLevel) {
                throw new ArchitectureFoundationException("修改用户实体数据时发生错误，所需要修改的实体在数据库中并不存在");
            }
            return (dao.merge(iSkillLevel) != null);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public List<ISkillLevel> getSkillLevelById(String[] Id) {
        if ((Id.length != 0) || Id != null) {
            ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
            return dao.findByIdentifier(Id);
        }
        return null;
    }

    @Override
    public List<ISkillLevel> getAllSkillLevel() {
        ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
        return dao.find();
    }

    @Override
    public List<ISkillLevel> getSkillLevelByQueryName(String queryName, Object[] args) {
        ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
        return dao.findByNamedQuery(queryName, args);
    }

    @Override
    public String[] modifySkillLevel(Object[] objects) {
        if (null == objects) {
            return null;
        } else {
            ISkillLevelDao dao = (ISkillLevelDao) this.getDao(ISkillLevel.class);
            SkillLevel[] types = new SkillLevel[objects.length];
            for (int i = 0; i < objects.length; i++) {
                SkillLevel type = (SkillLevel) objects[i];
                types[i] = type;
            }
            return dao.merge(types);
        }
    }
}
