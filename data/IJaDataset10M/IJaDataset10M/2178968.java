package com.xy.sframe.frame.workspace;

import java.io.Serializable;
import java.util.*;

/**
 * Created on 2005-12-27
 * ��Ź�����Ԫ���û�������䡣һ��session����һ��ʵ��ÿ���û����������ÿ��ҵ�����Ϊһ��������
 * ������Ԫ��
 * @author chengang
 * @version 1.0
 */
public class UserWorkHome implements Serializable {

    private Map workUnitMap = new HashMap(20);

    private User userInfo;

    private String currentSvcId;

    private String sessionId;

    private HashMap attributeMap = new HashMap();

    private int state = 0;

    private String stateDesc = "";

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId The sessionId to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserWorkHome() {
        super();
    }

    /**
     * ��ҵ������Ԫע������û�������䡣
     * @param serviceId ҵ����ˮ
     * @param workUnit ҵ������Ԫ
     */
    public void addWorkUnit(String serviceId, WorkUnit workUnit) {
        workUnitMap.put(serviceId, workUnit);
    }

    public WorkUnit getWorkUnit(String serviceId) {
        return (WorkUnit) workUnitMap.get(serviceId);
    }

    public void removeWorkUnit(String serviceId) {
        workUnitMap.remove(serviceId);
    }

    /**
     * @return Returns the userInfo.
     */
    public User getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo The userInfo to set.
     */
    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * @return Returns the currentSvcId.
     */
    public String getCurrentSvcId() {
        return currentSvcId;
    }

    /**
     * @param currentSvcId The currentSvcId to set.
     */
    public void setCurrentSvcId(String currentSvcId) {
        this.currentSvcId = currentSvcId;
    }

    public void setAttribute(String attrName, Object obj) {
        attributeMap.put(attrName, obj);
    }

    public Object getAttribute(String attrName) {
        return attributeMap.get(attrName);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }
}
