package com.magic.magicstore.core;

import com.magic.magicstore.core.log.Loggable;

/**
 * The class <code>SupportAction</code><BR>
 * ʵ��Action�Ļ���<BR>
 * �Ϳ�ܼ̳е�һЩ���ܣ���Action����Ǳ���̳д���
 * 
 * @author madawei
 * @version 0.1 2011-7-18
 * @since Ver0.1
 */
public abstract class SupportAction implements Loggable {

    public String execute() throws Exception {
        return "success";
    }
}
