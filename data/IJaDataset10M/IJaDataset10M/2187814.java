package com.jspx.txweb;

import com.jspx.txweb.config.ResultConfigBean;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2006-12-26
 * Time: 19:57:19
 *
 */
public interface Result extends Serializable {

    ResultConfigBean getResultConfig();

    void setResultConfig(ResultConfigBean resultConfig);

    String getConfigLocationUrl(ActionInvocation actionInvocation) throws Exception;

    void execute(ActionInvocation actionInvocation) throws Exception;
}
