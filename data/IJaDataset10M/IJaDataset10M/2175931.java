package cn.myapps.core.networkdisk.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface NetDiskFileProcess extends IDesignTimeProcess<NetDiskFile> {

    /**
	 * 获得总数
	 * @param hql
	 * @return
	 * @throws Exception
	 */
    public abstract int getTotalLines(String hql) throws Exception;
}
