package jmxm.worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import jmxm.gui.ResultListener;
import jmxm.gui.monitor.TaskNotificationListener;
import jmxm.jmxmodel.JmxServerGraph;
import jmxm.jmxmodel.MBean;
import jmxm.jmxmodel.MBeanAttribute;

public class Task {

    public static final int DO_ONCE = -1;

    public static final int UPDATE_STRUCTURE = 0;

    public static final int GET_ATTRIBUTE = 1;

    public static final int SET_ATTRIBUTE = 2;

    public static final int INVOKE_METHOD = 3;

    public static final int UNREGISTER = 4;

    public static final int REGISTER = 5;

    public static final int LISTEN = 6;

    public static final int STOP_LISTEN = 7;

    private int freq;

    private int taskType;

    private String method;

    private String newMBeanName;

    private MBean mbean;

    private MBeanAttribute attribute;

    private Object[] params;

    private String[] signature;

    private JmxServerGraph server;

    private NotificationListener listener;

    private NotificationFilter filter;

    private Serializable handBack;

    private ObjectName queryObject;

    private List<ResultListener> listeners;

    public Task(int freq, int taskType, JmxServerGraph graph) {
        this.freq = freq;
        this.taskType = taskType;
        this.server = graph;
        listeners = new ArrayList<ResultListener>();
    }

    public void fireTaskDone(Object result) {
        for (ResultListener l : listeners) {
            l.result(result);
        }
    }

    public void addResultListener(ResultListener temp) {
        if (listener == null) {
            listeners.add(temp);
        } else if (listener instanceof TaskNotificationListener) {
            TaskNotificationListener tempListener = (TaskNotificationListener) listener;
            tempListener.addResultListener(temp);
        }
    }

    public void removeResultListener(int index) {
        listeners.remove(index);
    }

    public void removeResultListener(ResultListener temp) {
        listeners.remove(temp);
    }

    public ResultListener getResultListener(int index) {
        return listeners.get(index);
    }

    public int getResultListenerCount() {
        return listeners.size();
    }

    public ObjectName getQueryObject() {
        return queryObject;
    }

    public void setQueryObject(ObjectName queryObject) {
        this.queryObject = queryObject;
    }

    public NotificationListener getListener() {
        return listener;
    }

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    public String getNewMBeanName() {
        return newMBeanName;
    }

    public void setNewMBeanName(String newMBeanName) {
        this.newMBeanName = newMBeanName;
    }

    public String[] getSignature() {
        return signature;
    }

    public void setSignature(String[] signature) {
        this.signature = signature;
    }

    public JmxServerGraph getGraph() {
        return server;
    }

    public MBeanAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(MBeanAttribute attribute) {
        this.attribute = attribute;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public MBean getMbean() {
        return mbean;
    }

    public void setMbean(MBean mbean) {
        this.mbean = mbean;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public JmxServerGraph getServer() {
        return server;
    }

    public void setFilter(NotificationFilter filter) {
        this.filter = filter;
    }

    public NotificationFilter getFilter() {
        return filter;
    }

    public Serializable getHandBack() {
        return handBack;
    }

    public void setHandBack(Serializable handBack) {
        this.handBack = handBack;
    }
}
