package com.jspx.task;

import com.jspx.utils.DateUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2007-9-26
 * Time: 14:40:13
 * 
 */
public class TestTask {

    public void sayHello() {
        System.out.println("-----------定时运行成功:" + DateUtil.getDateTimeST());
    }
}
