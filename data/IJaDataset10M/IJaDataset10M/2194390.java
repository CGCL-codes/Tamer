package net.paoding.rose.web.impl.thread;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import net.paoding.rose.RoseConstants;
import net.paoding.rose.util.SpringUtils;
import net.paoding.rose.util.StackTraceSimplifier;
import net.paoding.rose.web.ControllerErrorHandler;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.MultipartCleanup;
import net.paoding.rose.web.annotation.NotForSubModules;
import net.paoding.rose.web.annotation.SuppressMultipartResolver;
import net.paoding.rose.web.impl.module.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.util.WebUtils;

/**
 * {@link ModuleEngine} 负责从表示的模块中找出可匹配的 控制器引擎 {@link ControllerEngine}
 * 并委托其返回匹配的 {@link ActionEngine}对象，最终构成 {@link InvocationBean}对象返回.
 * <p>
 * {@link ModuleEngine}能够从失败控制器引擎匹配中走出来，继续匹配下一个控制器引擎，找到最终的
 * {@link ActionEngine}对象。即，如果一个匹配的控制器引擎中没有匹配的 {@link ActionEngine}对象，
 * {@link ModuleEngine}能够自动到下一个匹配的{@link ControllerEngine}对象去判断.
 * <p>
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * @author Li Weibo[weibo.leo@gmail.com]
 */
public class ModuleEngine implements Engine {

    /** 日志对象 */
    private static Log logger = LogFactory.getLog(ModuleEngine.class);

    /** 模块对象 */
    private final Module module;

    private final MultipartResolver multipartResolver;

    /**
     * 构造能够正确匹配出到所给模块请求的控制器和方法的引擎，返回到相应 {@link InvocationBean}对象的模块引擎.
     * 
     * @param module
     * @throws NullPointerException 如果所传入的模块为空时
     */
    public ModuleEngine(Module module) {
        if (module == null) {
            throw new NullPointerException("module");
        }
        this.module = module;
        this.multipartResolver = initMultipartResolver(module.getApplicationContext());
    }

    /**
     * 返回所包含模块对象
     * 
     * @return
     */
    public Module getModule() {
        return module;
    }

    public MultipartResolver getMultipartResolver() {
        return multipartResolver;
    }

    @Override
    public int isAccepted(HttpServletRequest rose) {
        return 1;
    }

    @Override
    public Object execute(Rose rose) throws Throwable {
        Invocation inv = rose.getInvocation();
        inv.getRequest().setAttribute(RoseConstants.WEB_APPLICATION_CONTEXT_ATTRIBUTE, module.getApplicationContext());
        boolean isMultiPartRequest = false;
        try {
            isMultiPartRequest = checkMultipart(inv);
            return rose.doNext();
        } catch (Throwable invException) {
            Throwable cause = invException;
            while (cause instanceof InvocationTargetException) {
                cause = ((InvocationTargetException) cause).getTargetException();
            }
            Module errorHandlerModule = module;
            ControllerErrorHandler errorHandler = errorHandlerModule.getErrorHandler();
            while (errorHandler == null && errorHandlerModule != null) {
                errorHandlerModule = errorHandlerModule.getParent();
                if (errorHandlerModule != null) {
                    errorHandler = errorHandlerModule.getErrorHandler();
                    if (errorHandler != null) {
                        if (errorHandler.getClass().isAnnotationPresent(NotForSubModules.class)) {
                            errorHandler = null;
                            continue;
                        }
                    }
                } else {
                    errorHandler = null;
                    break;
                }
            }
            Object instruction = null;
            if (errorHandler != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("exception happended； " + errorHandler.getClass().getName() + " will handle the exception: " + cause.getClass().getName() + ":" + cause.getMessage());
                }
                rose.getInvocation().setViewModule(errorHandlerModule);
                HttpServletRequest request = rose.getInvocation().getRequest();
                WebUtils.exposeErrorRequestAttributes(request, cause, null);
                StackTraceSimplifier.simplify(cause);
                instruction = errorHandler.onError(rose.getInvocation(), cause);
            }
            if ((errorHandler == null) || (instruction == null)) {
                if (invException instanceof Exception) {
                    throw (Exception) invException;
                } else {
                    throw (Error) invException;
                }
            }
            return instruction;
        } finally {
            if (isMultiPartRequest) {
                cleanupMultipart(inv);
            }
        }
    }

    public void destroy() {
        WebApplicationContext applicationContext = module.getApplicationContext();
        if (applicationContext instanceof AbstractApplicationContext) {
            ((AbstractApplicationContext) applicationContext).close();
        }
    }

    /**
     * 返回该module engine的映射地址
     */
    @Override
    public String toString() {
        return this.module.getUrl().toString();
    }

    protected boolean checkMultipart(Invocation inv) throws MultipartException {
        if (inv.getRequest().getMethod() == null) {
            throw new NullPointerException("request.method");
        }
        if (this.multipartResolver.isMultipart(inv.getRequest())) {
            if (inv.getRequest() instanceof MultipartHttpServletRequest) {
                logger.debug("Request is already a MultipartHttpServletRequest");
                return true;
            } else {
                if (!inv.getMethod().isAnnotationPresent(SuppressMultipartResolver.class)) {
                    inv.setRequest(this.multipartResolver.resolveMultipart(inv.getRequest()));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clean up any resources used by the given multipart request (if any).
     * 
     * @see MultipartResolver#cleanupMultipart
     */
    protected void cleanupMultipart(Invocation inv) {
        HttpServletRequest src = inv.getRequest();
        while (src != null && !(src instanceof MultipartHttpServletRequest) && src instanceof HttpServletRequestWrapper) {
            src = (HttpServletRequest) ((HttpServletRequestWrapper) src).getRequest();
        }
        if (src instanceof MultipartHttpServletRequest) {
            final MultipartHttpServletRequest request = (MultipartHttpServletRequest) src;
            MultipartCleanup multipartCleaner = inv.getMethod().getAnnotation(MultipartCleanup.class);
            if (multipartCleaner == null || multipartCleaner.after() == MultipartCleanup.After.CONTROLLER_INVOCATION) {
                multipartResolver.cleanupMultipart(request);
            } else {
                inv.addAfterCompletion(new AfterCompletion() {

                    @Override
                    public void afterCompletion(Invocation inv, Throwable ex) throws Exception {
                        ModuleEngine.this.multipartResolver.cleanupMultipart(request);
                    }
                });
            }
        }
    }

    private static MultipartResolver initMultipartResolver(ApplicationContext context) {
        MultipartResolver multipartResolver = (MultipartResolver) SpringUtils.getBean(context, MultipartResolver.class);
        if (multipartResolver != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using MultipartResolver [" + multipartResolver + "]");
            }
        } else {
            multipartResolver = new CommonsMultipartResolver();
            if (logger.isDebugEnabled()) {
                logger.debug("No found MultipartResolver in context, " + "Using MultipartResolver by default [" + multipartResolver + "]");
            }
        }
        return multipartResolver;
    }
}
