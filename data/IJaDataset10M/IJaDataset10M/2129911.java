package cn.myapps.core.networkdisk.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.networkdisk.ejb.NetDisk;

public class HibernateNetDiskDAO extends HibernateBaseDAO<NetDisk> implements NetDiskDAO {

    public HibernateNetDiskDAO(String voClassName) {
        super(voClassName);
    }
}
