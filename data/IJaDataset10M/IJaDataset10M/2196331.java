package com.frog4orcl.business.alert.biz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.frog4orcl.framework.core.ProcessResult;
import com.frog4orcl.framework.core.db.DBManagerImpl;
import com.frog4orcl.framework.core.db.TableInfo;
import com.frog4orcl.framework.core.page.Pagination;

/**
 * @说明: 
 * @author: dandan
 * @email: xrzp_dh@yahoo.com.cn
 * @create: Feb 11, 2011 5:20:16 PM
 * @version: 1.0
 */
public interface StoreMgrBiz {

    /**
	 * 查询存在的超过某范围区的对象
	 * @param request
	 * @param response
	 * @param dba
	 * @param page
	 * @return
	 */
    public ProcessResult<TableInfo> queryOverXExtentsInfo(HttpServletRequest request, HttpServletResponse response, DBManagerImpl dba, Pagination page);

    /**
	 * 查询存在的超过某范围块的对象
	 * @param request
	 * @param response
	 * @param dba
	 * @param page
	 * @return
	 */
    public ProcessResult<TableInfo> queryOverXBlocksInfo(HttpServletRequest request, HttpServletResponse response, DBManagerImpl dba, Pagination page);
}
