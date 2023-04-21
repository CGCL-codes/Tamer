package com.hitao.codegen.configs.dao;

import java.util.List;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.ICodeValidate;
import com.hitao.codegen.configs.basic.IConfigObject;

/***
 * This interface represents all the resources to generates DAO, DAOImpl,
 * Manager, ManagerImpl, AO, AOImpl.
 * 
 * @author zhangjun.ht
 * @created 2011-1-14
 * @version $Id: IDaoConfigs.java 32 2011-03-02 02:28:51Z guest $
 */
public interface IDaoConfigs extends IConfigObject, ICodeValidate {

    /***
	 * Get configuration list.
	 * @return
	 */
    List<ICodeGenConfig> getConfigs();

    /***
	 * Get a configuration object by class name.
	 * 
	 * @return
	 */
    ICodeGenConfig getConfigByName(String argClassName);
}
