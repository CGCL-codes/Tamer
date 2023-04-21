package net.sf.jqql.debug;

import java.util.Vector;

/**
 * <pre>
 * Debug切换类，单一实例，控制debug状态的设置和管理Debug客户端，其负责向客户端转发
 * Debug事件。所以可以有多个调试客户端同时存在。
 * </pre>
 *
 * @author luma
 */
public class DebugSwitch {

    private Vector<IDebugListener> listeners;

    private boolean debug;

    private static DebugSwitch instance = new DebugSwitch();

    /**
     * Private constructor
     */
    private DebugSwitch() {
        listeners = new Vector<IDebugListener>();
        debug = false;
    }

    /**
     * @return 单一实例
     */
    public static DebugSwitch getInstance() {
        return instance;
    }

    /**
     * 添加一个调试事件监听器
     *
     * @param dl IDebugListener
     */
    public void addDebugListener(IDebugListener dl) {
        if (dl != null) listeners.add(dl);
        if (listeners.size() > 0) debug = true;
    }

    /**
     * 删除一个调试事件监听器
     *
     * @param dl IDebugListener
     */
    public void removeDebugListener(IDebugListener dl) {
        if (dl != null) listeners.remove(dl);
        if (listeners.size() <= 0) debug = false;
    }

    /**
     * 转发包调试事件
     *
     * @param e IDebugObject对象
     */
    public void deliverDebugObject(IDebugObject obj) {
        int size = listeners.size();
        for (int i = 0; i < size; i++) (listeners.get(i)).deliverDebugObject(obj);
    }

    /**
     * @return Returns the debug.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug The debug to set.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
