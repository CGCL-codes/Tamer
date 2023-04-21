package net.sf.nanomvc.core.config;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.nanomvc.core.annotations.ContentType;
import net.sf.nanomvc.core.annotations.Render;
import net.sf.nanomvc.core.annotations.Url;
import net.sf.nanomvc.core.annotations.View;
import net.sf.nanomvc.core.render.RenderInfo;
import net.sf.nanomvc.core.runtime.Action;
import net.sf.nanomvc.core.runtime.ControllerAccessor;
import net.sf.nanomvc.core.runtime.ControllerExecutor;
import net.sf.nanomvc.core.runtime.RequestMatcher;
import net.sf.nanomvc.core.util.Reflection;

public class ControllerExecutorFactory {

    private InvokerFactory invokerFactory;

    public ControllerExecutorFactory(final InvokerFactory invokerFactory) {
        this.invokerFactory = invokerFactory;
    }

    public ControllerExecutor createControllerExecutor(final Collection<ControllerAccessor> accessors) {
        final Map<RequestMatcher, Action> actions = new HashMap<RequestMatcher, Action>();
        for (final ControllerAccessor accessor : accessors) {
            addActions(actions, accessor);
        }
        return new ControllerExecutor(actions);
    }

    private void addActions(final Map<RequestMatcher, Action> actions, final ControllerAccessor accessor) {
        for (final Method method : Reflection.findMethods(accessor.getControllerClass(), Url.class, null, true, false)) {
            actions.put(createRequestMatcher(method), createAction(accessor, method));
        }
    }

    private RequestMatcher createRequestMatcher(final Method method) {
        return null;
    }

    private Action createAction(final ControllerAccessor accessor, final Method method) {
        return new Action(invokerFactory.createInvoker(method), accessor, createDefaultRenderInfo(method), createExceptions(method));
    }

    private RenderInfo createDefaultRenderInfo(final Method method) {
        Render render = method.getAnnotation(Render.class);
        View view = method.getAnnotation(View.class);
        if (render != null && view != null) {
            throw new NanoConfigurationException(method, "Conflict between @View and @Render");
        } else if (render == null && view == null) {
            throw new NanoConfigurationException(method, "Missing @View or @Render");
        } else if (view != null) {
            return new RenderInfo(view.value());
        } else {
            ContentType contentType = method.getAnnotation(ContentType.class);
            return new RenderInfo(render.value(), contentType != null ? contentType.value() : null, null);
        }
    }

    private LinkedHashMap<Class<? extends Throwable>, RenderInfo> createExceptions(final Method method) {
        return null;
    }
}
