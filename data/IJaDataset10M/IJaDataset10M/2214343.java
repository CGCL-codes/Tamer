package com.javaxyq.core;

/**
 * ��ϷӦ�ó��������
 * 
 * @author gongdewei
 * @date 2010-10-9 create
 */
public class ApplicationHelper {

    private static Application application;

    static void setApplication(Application app) {
        ApplicationHelper.application = app;
    }

    public static Application getApplication() {
        return ApplicationHelper.application;
    }
}
