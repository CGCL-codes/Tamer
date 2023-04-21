package com.frameworkset.common.mbean;

import javax.management.Notification;
import javax.management.monitor.MonitorNotification;

/**
 * <p>Title: DefaultStringMoniterProcess</p>
 *
 * <p>Description: ���������ַ����ֵ�����ַ��ֵ����仯������Ӧ�Ĵ���</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultStringMoniterProcess extends StringMoniterProcess implements StringMoniterProcessMBean {

    public DefaultStringMoniterProcess() {
        super();
    }

    public void sendNotification(Notification not) {
        if (not.getType().equals(MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED)) {
            try {
                server.invoke(handler, executeMethod, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.sendNotification(not);
    }

    public static void main(String[] args) {
        DefaultStringMoniterProcess defaultstringmoniterprocess = new DefaultStringMoniterProcess();
    }
}
