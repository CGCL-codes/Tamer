package com.companyname.common.viewframe.manager;

import java.util.List;
import org.apache.log4j.Logger;
import com.companyname.common.base.manager.BaseManager;
import com.companyname.common.viewframe.dao.AreaDAO;
import com.companyname.common.viewframe.model.Area;

/**
 * <p>Title: 分区</p>
 * <p>Description: 分区 Area </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class AreaManagerimp extends BaseManager implements AreaManager {

    static Logger logger = Logger.getLogger(AreaManagerimp.class);

    /**分区DAO*/
    private AreaDAO areaDAO;

    public void setAreaDAO(AreaDAO areaDAO) {
        super.setDAO(areaDAO);
        this.areaDAO = areaDAO;
    }

    /**
         * 描述:分区
         *
         * @ param:
         * @ param:orderBy 排序属性（for hql）
         * @ param:sortType 排序方式（asc or desc）
         * @ Exception:
         * @ return model集合
         */
    public List findAreas(String homeId, String code, String name, String orderBy, String sortType) {
        return this.areaDAO.findAreas(homeId, code, name, orderBy, sortType);
    }

    public Area getByCode(String code) {
        return this.areaDAO.getByCode(code);
    }
}
