package com.litt.core.web.listener;

import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.litt.core.common.BeanManager;

/**
 * <b>标题：</b>Spring Bean加载监听器.
 * <pre><b>描述</b>
 *    取得web容器内的context环境，通过单例模式提供给系统的其他调度者，需要与BeanManager配合使用 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 */
public class BeanLoaderListener extends ContextLoaderListener {

    /**
     * 进行容器初始化，并将上下文传播到BeanManager中.
     * @param context
     */
    public void contextInitialized(ServletContextEvent context) {
        super.contextInitialized(context);
        context.getServletContext().log("Attempt to inject WebApplicationContext into [com.litt.core.common.BeanManager]...");
        BeanManager.setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(context.getServletContext()));
        BeanManager.setServletContext(context.getServletContext());
    }
}
