package cn.myapps.core.deploy.application.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * @see cn.myapps.core.deploy.application.ejb.CopyApplicationProcessBena
 * @since jdk1.5
 * @author eshow
 * 
 */
public interface CopyApplicationProcess extends IDesignTimeProcess {

    /**
	 * copy roles
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyRole(String applicationid) throws Exception;

    /**
	 * copy menu
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyMenu(String applicationid) throws Exception;

    /**
	 * copy Macrolibs
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyMacrolibs(String applicationid) throws Exception;

    /**
	 * copy style css
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyStylelibs(String applicationid) throws Exception;

    /**
	 * copy validatelibs
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyValidatelibs(String applicationid) throws Exception;

    /**
	 * copy excel的导入配置
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyExcelConf(String applicationid) throws Exception;

    /**
	 * copy reminder 提醒
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyReminder(String applicationid) throws Exception;

    /**
	 * copy page 页
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyPage(String applicationid) throws Exception;

    /**
	 * copy component 组件
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyComponent(String applicationid) throws Exception;

    /**
	 * copy homepage
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyHomepage(String applicationid) throws Exception;

    /**
	 * copy application datasource
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyDataSource(String applicationid) throws Exception;

    /**
	 * copy workflow
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyStatelabel(String applicationid) throws Exception;

    /**
	 * copy module
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyModule(String applicationid) throws Exception;

    /**
	 * copy All( page,module,statelable,datasource,style.....)
	 * 
	 * @param applicationid
	 * @throws Exception
	 */
    public void copyAll(String applicationid) throws Exception;
}
