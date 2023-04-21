package org.lightframework.mvc;

import java.util.ArrayList;
import org.lightframework.mvc.HTTP.Request;
import org.lightframework.mvc.HTTP.Response;
import org.lightframework.mvc.Result.Return;
import org.lightframework.mvc.core.CorePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the main class of mvc framework
 *
 * @author fenghm(live.fenghm@gmail.com)
 * 
 * @since 1.0.0
 */
public class Framework {

    private static final Logger log = LoggerFactory.getLogger(Framework.class);

    /** represents was framework initialized ?*/
    private static boolean initialized = false;

    /** core framework plugins */
    private static final ArrayList<Plugin> plugins = new ArrayList<Plugin>();

    static {
    }

    protected static void initialize() {
        if (!initialized) {
            log.info("[mvc] -> version : '{}'", Version.getVersion());
            log.info("[mvc] -> initializing... ");
            synchronized (Framework.class) {
                if (!initialized) {
                    plugins.add(new CorePlugin());
                    initialized = true;
                }
            }
            log.info("[mvc] -> initialized!");
        }
    }

    protected static void start(Application application) {
        initialize();
        try {
            log.debug("[app:{}] -> starting...", application.name());
            application.start();
            log.debug("[app:{}] -> started!", application.name());
            start(application.getRootModule());
        } catch (Throwable e) {
            log.error("[app:{}] -> start error", application.name(), e);
        }
    }

    protected static void stop(Application application) {
        try {
            log.debug("[app:{}] -> stopping...", application.name());
            application.end();
            log.debug("[app:{}] -> stopped!", application.name());
            stop(application.getRootModule());
        } catch (Exception e) {
            log.error("[app:{}] -> stop error", application.name(), e);
        } finally {
            Result.reset();
            Request.reset();
            Application.removeCurrent();
        }
    }

    /**
	 * start the module
	 */
    private static void start(Module module) {
        initialize();
        try {
            log.debug("[module:{}] -> starting...", module.getName());
            module.start();
            log.debug("[module:{}] -> started!", module.getName());
        } catch (Exception e) {
            log.error("[module:{}] -> start error", module.getName(), e);
        }
    }

    /**
	 * stop the module
	 */
    private static void stop(Module module) {
        try {
            log.debug("[module:{}] -> stopping...", module.getName());
            module.stop();
            log.debug("[module:{}] -> stopped!", module.getName());
        } catch (Exception e) {
            log.error("[module:{}] -> stop error", module.getName(), e);
        }
    }

    /**
	 * is this request ignored by current module
	 */
    protected static boolean ignore(Request request) throws Throwable {
        return PluginInvoker.ignore(request);
    }

    /**
	 * handle a http request in mvc framework <p>
	 * 
	 * after you call this method , you must call {@link #handleFinally(Request, Response)} manual to clear context data.
	 * 
	 * @param request      mvc http request
	 * @param response     mvc http response
	 * @return true if mvc framework has handled this request
	 */
    protected static boolean handle(Request request, Response response) throws Throwable {
        Assert.notNull("request.module", request.getModule());
        if (log.isDebugEnabled()) {
            log.debug("[mvc] -> request path '{}' handling...", request.getPath());
        }
        boolean managed = false;
        try {
            Request.current.set(request);
            try {
                managed = PluginInvoker.request(request, response);
                if (!managed) {
                    Action[] actions = PluginInvoker.route(request, response);
                    if (actions.length > 0) {
                        for (Action action : actions) {
                            if (log.isDebugEnabled()) {
                                log.debug("[action] -> route uri '{}' to action '{}'", request.getUriString(), action.getName());
                            }
                            action.onRouted();
                        }
                        Action action = resolveAction(request, response, actions);
                        if (null != action) {
                            Action.Setter.setResolved(action, true);
                            action.onResolved();
                            request.action = action;
                            managed = invokeAction(request, response, action);
                        } else {
                            return false;
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("[action] -> not found for uri '{}'", request.getUriString());
                        }
                    }
                }
            } catch (Return e) {
                request.result = e.result();
                managed = PluginInvoker.render(request, response, e.result());
            }
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.debug("[mvc] -> an error occurs while handling request,message : {}", e.getMessage());
            }
            Result.ErrorResult error = new Result.ErrorResult(e.getMessage(), e);
            request.result = error;
            if (!PluginInvoker.error(request, response, error)) {
                throw e;
            } else {
                managed = true;
            }
        } finally {
            PluginInvoker.response(request, response, request.result);
        }
        if (log.isDebugEnabled()) {
            log.debug("[mvc] -> request path '{}' {}managed", request.getPath(), managed ? "" : "not ");
        }
        return managed;
    }

    protected static void handleFinally(Request request, Response response) {
        Result.reset();
        Request.reset();
    }

    private static Action resolveAction(Request request, Response response, Action[] actions) throws Throwable {
        for (Action action : actions) {
            if (action.isResolved()) {
                return action;
            } else if (PluginInvoker.resolve(request, response, action)) {
                return action;
            }
            if (log.isDebugEnabled()) {
                log.debug("[action:'{}'] -> can not resolved", action.getName());
            }
        }
        return null;
    }

    private static boolean invokeAction(Request request, Response response, Action action) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("[action:'{}'] -> resolved as '{}${}'", new Object[] { action.getName(), action.getControllerClass().getName(), action.getMethod().getName() });
        }
        if (!action.isBinded()) {
            PluginInvoker.binding(request, response, action);
            Action.Setter.setBinded(action, true);
        }
        action.onBinded();
        Result result = null;
        try {
            result = PluginInvoker.execute(request, response, action);
        } catch (Return e) {
            result = e.result();
        }
        action.onExecuted();
        request.result = result;
        boolean rendered = PluginInvoker.render(request, response, result);
        if (!rendered && !action.isResolved()) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * @since 1.0.0 
	 */
    private static final class PluginInvoker {

        static boolean ignore(Request request) throws Throwable {
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.ignore(request)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:ignore] -> request ignored by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.ignore(request)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:ignore] -> request ignored by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            return false;
        }

        static boolean request(Request request, Response response) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:requet] -> request handling...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.request(request, response)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:request] -> request managed by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.request(request, response)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:request] -> request managed by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:request] -> request not managed by any plugins");
            }
            return false;
        }

        static Action[] route(Request request, Response response) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:route] -> routing action...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                Action[] actions = plugin.route(request, response);
                if (null != actions && actions.length > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:route] -> routed {} actions", actions.length);
                    }
                    return actions;
                }
            }
            for (Plugin plugin : plugins) {
                Action[] actions = plugin.route(request, response);
                if (null != actions && actions.length > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:route] -> routed {} actions", actions.length);
                    }
                    return actions;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:route] -> routed 0 actions");
            }
            return Plugin.EMPTY_ACTIONS;
        }

        static boolean resolve(Request request, Response response, Action action) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:resolve] -> resolving action...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.resolve(request, response, action)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:resolve] -> resolved by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.resolve(request, response, action)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:resolve] -> resolved by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:resolve] -> resolving not done by any plugins");
            }
            return false;
        }

        static boolean binding(Request request, Response response, Action action) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:binding] -> binding arguments...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.binding(request, response, action)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:binding] -> binded by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.binding(request, response, action)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:binding] -> binded by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:binding] -> binding not done by any plugins");
            }
            return false;
        }

        static Result execute(Request request, Response response, Action action) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:execute] -> executing action...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                Result result = plugin.execute(request, response, action);
                if (null != result) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:execute] -> executed by '{}'", plugin.getName());
                    }
                    return result;
                }
            }
            for (Plugin plugin : plugins) {
                Result result = plugin.execute(request, response, action);
                if (null != result) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:execute] -> executed by '{}'", plugin.getName());
                    }
                    return result;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:execute] -> executing not done by any plugins");
            }
            return null;
        }

        static boolean render(Request request, Response response, Result result) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:render] -> rendering result...");
            }
            request.result = result;
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.render(request, response, result)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:render] -> rendered by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.render(request, response, result)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:render] -> rendered by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:render] -> rendering not done by any plugins");
            }
            return false;
        }

        static boolean error(Request request, Response response, Result.ErrorResult e) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:error] -> handling error...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.error(request, response, e)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:error] -> handled by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (request.getApplication().error(request, response, e)) {
                if (log.isDebugEnabled()) {
                    log.debug("[plugin-invoker:error] -> handled by application handler");
                }
                return true;
            }
            for (Plugin plugin : plugins) {
                if (plugin.error(request, response, e)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:error] -> handled by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:error] -> handling not done by any plugins");
            }
            return false;
        }

        static boolean response(Request request, Response response, Result result) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:response] -> handling response...");
            }
            for (Plugin plugin : request.getModule().getPlugins()) {
                if (plugin.response(request, response, result)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:response] -> handled by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            for (Plugin plugin : plugins) {
                if (plugin.response(request, response, result)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[plugin-invoker:response] -> handled by '{}'", plugin.getName());
                    }
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[plugin-invoker:response] -> handling not done by any plugins");
            }
            return false;
        }
    }
}
