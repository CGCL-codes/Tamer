package com.javaeye.lonlysky.lforum.interceptor;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.RequestConfig;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 初始化接口拦截器<br>
 * 
 * 用于初始化Action实现的自定义接口
 * 
 * @author 黄磊
 *
 */
public class ForumInterceptor extends AbstractInterceptor implements StrutsStatics {

    private static final long serialVersionUID = 491815684932676174L;

    private static final Logger logger = LoggerFactory.getLogger(ForumInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final Object action = invocation.getAction();
        String basePath = ServletActionContext.getRequest().getScheme() + "://" + ServletActionContext.getRequest().getServerName() + ":" + ServletActionContext.getRequest().getServerPort() + ServletActionContext.getRequest().getContextPath() + "/";
        ServletActionContext.getRequest().setAttribute("path", basePath);
        if (action instanceof RequestConfigAware) {
            ((RequestConfigAware) action).setRequestConfig(new RequestConfig());
            if (action instanceof BaseAction) {
                long startTime = System.currentTimeMillis();
                RequestConfig reqcfg = ((BaseAction) action).getReqcfg().setPageTime(startTime);
                LForumRequest.getRequest().setAttribute(GlobalsKeys.REQUEST_CONFIG, reqcfg);
            }
        }
        if (action instanceof ConfigAware) {
            Config config = ConfigLoader.getConfig();
            ((ConfigAware) action).setConfig(config);
            String uri = LForumRequest.getPageName();
            if (config.getInstall() == 0) {
                if (uri.indexOf(GlobalsKeys.WEB_INSTALL) == -1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("当前访问地址：" + uri + ",跳转安装向导");
                    }
                    return GlobalsKeys.WEB_INSTALL;
                }
            } else {
                if (uri.indexOf(GlobalsKeys.WEB_INSTALL) != -1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("当前访问地址：" + uri + ",系统已经执行安装向导");
                    }
                    if (action instanceof BaseAction) {
                        RequestConfig reqcfg = ((BaseAction) action).getReqcfg().addErrLine("非法访问,系统已经执行过安装向导");
                        ServletActionContext.getRequest().setAttribute(GlobalsKeys.REQUEST_CONFIG, reqcfg);
                    }
                    return GlobalsKeys.WEB_MESSAGE;
                }
            }
        }
        if (action instanceof ActionInitAware) {
            try {
                String result = ((ActionInitAware) action).initAction();
                if (!GlobalsKeys.ACTION_INIT.equals(result)) {
                    return result;
                }
            } catch (Exception e) {
                logger.error("初始化论坛异常", e);
                if (action instanceof BaseAction) {
                    RequestConfig reqcfg = ((BaseAction) action).getReqcfg().addErrLine("初始化论坛失败：" + e.getMessage());
                    LForumRequest.getRequest().setAttribute(GlobalsKeys.REQUEST_CONFIG, reqcfg);
                }
                return GlobalsKeys.WEB_MESSAGE;
            }
        }
        return invocation.invoke();
    }
}
