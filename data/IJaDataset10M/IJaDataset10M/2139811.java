package org.tju.ebs.domain.dao;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.tju.ebs.bean.SysMenu;
import org.tju.ebs.bean.SysUser;

/**
 * @see org.tju.ebs.bean.SysMenu
 * @author Tony Ju
 * @created date: Jan 13, 2012 3:20:53 PM
 */
public class SysMenuDAO extends BaseDAO<SysMenu> {

    private static final Logger log = LoggerFactory.getLogger(SysMenuDAO.class);

    protected void initDao() {
    }

    /**
	 * @作者: tju
	 * @创建日期: Jan 13, 2012 3:20:53 PM
	 * @功能说明；保存SysMenu
	 * @参数: SysMenu
	 */
    public void save(SysMenu instance) throws Exception {
        log.debug("SysMenuDAO: saving->" + instance.getId());
        super.save(instance);
    }

    /**
	 * @作者: tju
	 * @创建日期: Jan 13, 2012 3:20:53 PM
	 * @功能说明: 删除SysMenu
	 * @参数: SysMenu
	 */
    public void delete(SysMenu instance) throws Exception {
        super.delete(instance);
    }

    /**
	 * @作者: tju
	 * @创建日期: Jan 13, 2012 3:20:53 PM
	 * @功能说明: 根据ID查找SysMenu
	 * @参数: ID,字符型，ID不能为空或Null,否则则报出异常。
	 */
    public SysMenu getSysMenuById(String id) {
        return super.getObjectById(id);
    }

    /**
	 * @作者: tju
	 * @创建日期: Jan 13, 2012 3:20:53 PM
	 * @功能说明: 返回全部结果集，不分页.
	 * @参数:
	 */
    public List<SysMenu> getAll() {
        return super.getAll();
    }

    public List<SysMenu> getSysMenuListByModelId(String modelId) {
        String hql = "from SysMenu where modelId = '" + modelId + "'";
        return super.getObjectsByHQL(hql);
    }

    public List<SysMenu> getSysMenuListByParentId(String parentId) {
        String hql = "from SysMenu ";
        if (parentId == null) {
            hql = hql + " where parentId = null";
        } else {
            hql = hql + " where parentId = '" + parentId + "'";
        }
        log.debug(hql);
        return super.getObjectsByHQL(hql);
    }

    public static SysMenuDAO getFromApplicationContext(ApplicationContext ctx) {
        return (SysMenuDAO) ctx.getBean("SysMenuDAO");
    }
}
