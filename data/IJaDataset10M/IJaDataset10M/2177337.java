package cn.myapps.core.macro.repository.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.ejb.UserVO;

public interface RepositoryProcess extends IDesignTimeProcess {

    /**
	 * 根据函数库名,返回库RepositoryVO对象
	 * 
	 * @param name
	 *            libname 函数库名
	 * @param application
	 *            应用标识
	 * @return RepositoryVO对象
	 * @throws Exception
	 */
    public RepositoryVO getRepositoryByName(String name, String application) throws Exception;
}
