package org.soybeanMilk.web.exe.th;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soybeanMilk.web.WebObjectSource;
import org.soybeanMilk.web.exe.WebAction;
import org.soybeanMilk.web.exe.WebAction.Target;

/**
 * {@linkplain TypeTargetHandler 类型目标处理器}的默认实现。
 * @author earthAngry@gmail.com
 * @date 2011-4-19
 *
 */
public class DefaultTypeTargetHandler implements TypeTargetHandler {

    private static Log log = LogFactory.getLog(DefaultTypeTargetHandler.class);

    private Map<String, TargetHandler> typeHandlers;

    public DefaultTypeTargetHandler() {
        this(null);
    }

    public DefaultTypeTargetHandler(Map<String, TargetHandler> typeHandlers) {
        this.typeHandlers = typeHandlers;
        addDefaultTargetHandler();
    }

    public void handleTarget(WebAction webAction, WebObjectSource webObjectSource) throws ServletException, IOException {
        Target target = webAction.getTarget();
        if (target == null) {
            if (log.isDebugEnabled()) log.debug("the action " + webAction + " has no Target defined, handling is not needed.");
            return;
        }
        TargetHandler th = getTargetHandler(target.getType());
        if (th == null) throw new NullPointerException("no TargetHandler found for handling Target of type '" + target.getType() + "'");
        th.handleTarget(webAction, webObjectSource);
    }

    public Map<String, TargetHandler> getTypeHandlers() {
        return typeHandlers;
    }

    public void setTypeHandlers(Map<String, TargetHandler> typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public TargetHandler getTargetHandler(String type) {
        Map<String, TargetHandler> h = getTypeHandlers();
        return h == null ? null : h.get(consistentTargetType(type));
    }

    public void addTargetHandler(String type, TargetHandler targetHandler) {
        if (this.typeHandlers == null) this.typeHandlers = new HashMap<String, TargetHandler>();
        this.typeHandlers.put(consistentTargetType(type), targetHandler);
        if (log.isDebugEnabled()) log.debug("add " + targetHandler + " for handling '" + consistentTargetType(type) + "' type target");
    }

    /**
	 * 添加默认目标处理器
	 * @date 2011-4-19
	 */
    protected void addDefaultTargetHandler() {
        addTargetHandler(Target.FORWARD, new ForwardTargetHandler());
        addTargetHandler(Target.REDIRECT, new RedirectTargetHandler());
    }

    /**
	 * {@linkplain Target 目标}类型一致性处理，它处理输入{@linkplain Target 目标}类型，以保证大小写一致，使得输入可以忽略大小写。
	 * @param targetType
	 * @return
	 * @date 2011-4-19
	 */
    protected String consistentTargetType(String targetType) {
        return targetType == null ? null : targetType.toLowerCase();
    }
}
