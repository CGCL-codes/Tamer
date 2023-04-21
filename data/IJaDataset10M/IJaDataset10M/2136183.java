package com.jmeurope.service;

import java.util.List;
import com.jmeurope.model.SysLog;

/**
 * <p>
 * Description: 
 * </p>
 *
 * @author wangwei  leo_java@163.com
 * @version 1.0

 * <p>
 * History: 
 *
 * Date                     Author         Version     Description
 * ---------------------------------------------------------------------------------
 * Dec 26, 2011 12:25:08 PM          wangwei        1.0         To create
 * </p>
 *
 * @since 
 * @see     
 */
public interface SysLogService {

    Integer addSysLog(SysLog log) throws Exception;

    List<SysLog> getAllLogs() throws Exception;
}
