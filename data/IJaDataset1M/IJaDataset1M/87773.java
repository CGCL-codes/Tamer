package net.butfly.bus.context;

import java.util.HashMap;
import java.util.Map;

/**
 * request上下文管理器
 * 
 */
public class ContextManager {

    private InheritableThreadLocal<Context> pool = new InheritableThreadLocal<Context>();

    private static ContextManager instance = new ContextManager();

    /**
	 * 获取ContextManager单例
	 * 
	 * @return
	 */
    public static ContextManager getInstance() {
        return instance;
    }

    /**
	 * 装载request上下文
	 * 
	 * @param map
	 */
    public void loadContext(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        Context context = new Context(map);
        pool.set(context);
    }

    /**
	 * 获取request上下文属性
	 * 
	 * @param attr
	 */
    @SuppressWarnings("unchecked")
    public <T extends Object> T getAttribute(String attr) {
        Context context = pool.get();
        return context != null ? (T) context.getAttribute(attr) : null;
    }

    /**
	 * 清除request上下文 WARNING:因为共享线程池的缘故，每次request处理完退出时都要清理request上下文
	 */
    public void clearContext() {
        pool.remove();
    }

    /**
	 * 获取request上下文对象
	 * 
	 * @return
	 */
    public Context getContext() {
        return pool.get();
    }
}
