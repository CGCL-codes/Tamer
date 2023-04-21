package org.tju.ebs.domain.manager;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.ebs.bean.ProdPackage;
import org.tju.ebs.domain.dao.ProdPackageDAO;

/**
 * 
 * @创建人: tju
 * @创建日期:Jan 27, 2012 8:45:03 AM
 * @最近修改人:tju
 * @最近修改日期:Jan 27, 2012 8:45:03 AM
 * @功能描述:
 * @参数:
 * @约束:
 * @改动历史：
 * 
 */
public class ProdPackageManager extends BaseManager {

    /** Logger */
    private static final Logger log = LoggerFactory.getLogger(ProdPackageDAO.class);

    /** 依赖注入　ProdPackage */
    private ProdPackageDAO prodPackageDAO;

    /**
	 * 
	 * @创建人: tju
	 * @创建日期:Jan 27, 2012 8:45:03 AM
	 * @最近修改人:tju
	 * @最近修改日期:Jan 27, 2012 8:45:03 AM
	 * @功能名称:
	 * @功能描述:删除ProdPackage
	 * @参数:ProdPackage
	 * @约束:
	 * @改动历史：
	 * 
	 */
    public ProdPackage getProdPackageById(String id) {
        return this.prodPackageDAO.getProdPackageById(id);
    }

    /**
	 * 
	 * @创建人: tju
	 * @创建日期:Jan 27, 2012 8:45:03 AM
	 * @最近修改人:tju
	 * @最近修改日期:Jan 27, 2012 8:45:03 AM
	 * @功能名称:
	 * @功能描述:删除ProdPackage
	 * @参数:ProdPackage
	 * @约束:
	 * @改动历史：
	 * 
	 */
    public void delete(ProdPackage instance) throws Exception {
        log.debug("Deleting ProdPackage: ID=" + instance.getId());
        this.prodPackageDAO.delete(instance);
    }

    /**
	 * 
	 * @创建人: tju
	 * @创建日期:Jan 27, 2012 8:45:03 AM
	 * @最近修改人:tju
	 * @最近修改日期:Jan 27, 2012 8:45:03 AM
	 * @功能名称:
	 * @功能描述:保存ProdPackage
	 * @参数:ProdPackage
	 * @约束:
	 * @改动历史：
	 * 
	 */
    public void save(ProdPackage instance) throws Exception {
        log.debug("Saving ProdPackage: ID=" + instance.getId());
        this.prodPackageDAO.save(instance);
    }

    /**
	 * 
	 * @创建人: tju
	 * @创建日期:Jan 27, 2012 8:45:03 AM
	 * @最近修改人:tju
	 * @最近修改日期:Jan 27, 2012 8:45:03 AM
	 * @功能名称:
	 * @功能描述:根据全部结果集，无分页。
	 * @参数:
	 * @约束:
	 * @改动历史：
	 * 
	 */
    public List<ProdPackage> getAll() {
        return this.prodPackageDAO.getAll();
    }

    public ProdPackageDAO getProdPackageDAO() {
        return prodPackageDAO;
    }

    public void setProdPackageDAO(ProdPackageDAO prodPackageDAO) {
        this.prodPackageDAO = prodPackageDAO;
    }
}
